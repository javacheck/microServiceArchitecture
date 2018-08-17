package cn.self.cloud.commonutils.zother.servlet;

import cn.self.cloud.commonutils.zother.entity.UserInfo;
import cn.self.cloud.commonutils.zother.service.RedisService;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.redkale.convert.json.JsonConvert;
import org.redkale.net.http.HttpRequest;
import org.redkale.net.http.HttpResponse;
import org.redkale.net.http.HttpServlet;
import org.redkale.net.http.HttpUserType;
import org.redkale.util.AnyValue;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by cp on 2017/7/21.
 */
@HttpUserType(UserInfo.class)
public class BaseServlet extends HttpServlet {

    Logger logger = Logger.getLogger(BaseServlet.class.getCanonicalName());

    @Resource(name = "property.isBizService")
    boolean isRunAsBizService;

    @Resource
    RedisService redisService;


    @Override
    protected void preExecute(HttpRequest request, HttpResponse response) throws IOException {
        if (isRunAsBizService) {
            String sessionid = request.getHeader("sessionid");
            if (StringUtils.isEmpty(sessionid)) {
                if (StringUtils.isEmpty(sessionid)) {
                    sessionid = request.getCookie("sessionid");
                }
            }
            if (StringUtils.isEmpty(sessionid)) {
                sessionid = request.getSessionid(false);
            }
            if (StringUtils.isNotEmpty(sessionid))
                request.setCurrentUser(fetchCurrentUser(sessionid));
        }
        response.recycleListener((req, resp) -> {
            String bodyString = "";
            try {
                if (resp.getOutput() instanceof String)
                    bodyString = (String) resp.getOutput();
                else {
                    if (resp.getOutput() != null)
                        bodyString = resp.getOutput().toString();
                    else
                        bodyString = "";
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            LogUtils.info(getClass(), "base servlet", request.getMethod(), request.getRequestURI(), HttpRequestUtils.parseParameters(request),
                    request.getBodyUTF8(), response.getStatus(), bodyString, (System.currentTimeMillis() - request.getCreatetime()));

        });
        response.nextEvent();
    }

    private UserInfo fetchCurrentUser(String sessionid) {
        Jedis jedis = null;
        try {
            String key = "sessionid@" + sessionid;
            jedis = redisService.getReadJedis("uc").getResource();
            String json = jedis.get(key);
            logger.info("根据会话信息获取到用户信息,key=" + key + ", json=" + json + ",jedis=" + jedis);
            if (StringUtils.isNotEmpty(json)) {
                UserInfo userInfo = JsonConvert.root().convertFrom(UserInfo.class, json);
                logger.info("获取到当前用户信息：" + userInfo);
                return userInfo;
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (jedis != null)
                jedis.close();
        }
        return null;
    }

    @Override
    protected void authenticate(HttpRequest request, HttpResponse response) throws IOException {
        if (isRunAsBizService) {
            String timestamp = request.getHeader("timestamp");
            String random = request.getHeader("random");
            String sign = request.getHeader("sign");
            String sessionid = request.getHeader("sessionid");
            boolean sessionidInCookie = false;
            if (StringUtils.isEmpty(sessionid)) {
                sessionid = request.getCookie("sessionid");
                sessionidInCookie = true;
            }
            int iversion = request.getIntHeader("iversion", 1); // 做接口的兼容 ，如果没传默认iversion=1
            if (StringUtils.isEmpty(sessionid)) {
                sessionid = request.getSessionid(false);
            }
            if (StringUtils.isEmpty(timestamp) ||
                    StringUtils.isEmpty(random) ||
                    StringUtils.isEmpty(sign)
                    ) {
                logger.info("安全验证参数：timestamp=" + timestamp + ",random=" + random + ",sign=" + sign);
                response.finish(HttpStatus.SC_FORBIDDEN, "缺少安全验证参数");
                return;
            }
            if (iversion > 1) { //高版本的app访问，必须额外传参数
                int apptype = request.getIntHeader("apptype", 0); //1:  车海通android 调用
//                2： 车海通ios调用
//                3:  牛运android调用
//                4:  牛运 ios 客户端
//                5： 天骄联盟管理软件android客户端
//                6： 天骄联盟软件ios客户端
//                7： 油宝安卓客户端
//                8： 油宝ios客户端
//                9： 油宝后台web端
//                10：无车承运web端
                int versioncode = request.getIntHeader("versioncode", 0);
                if (apptype <= 0) {
                    response.finish(HttpStatus.SC_FORBIDDEN, "apptype参数错误");
                    return;
                }
                if (versioncode <= 0) {
                    response.finish(HttpStatus.SC_FORBIDDEN, "versioncode参数错误");
                    return;
                }
            }
            if (!checkTimestamp(timestamp)) {
                response.finish(HttpStatus.SC_FORBIDDEN, "时间戳参数错误");
                return;
            }
            if (!checkRandom(random)) {
                response.finish(HttpStatus.SC_FORBIDDEN, "随机数参数错误");
                return;
            }
            if (!request.getMethod().equalsIgnoreCase("get") && !request.getMethod().equalsIgnoreCase("post")) {
                response.finish(HttpStatus.SC_METHOD_NOT_ALLOWED, "请求方法不支持");
                return;
            }
            if (!checkSessionKey(sessionid)) {
                response.finish(HttpStatus.SC_UNAUTHORIZED, "Session已经超时，请重新登录");
                return;
            }
            AnyValue.DefaultAnyValue headers = (AnyValue.DefaultAnyValue) request.getHeaders();
            AnyValue.DefaultAnyValue parameters = (AnyValue.DefaultAnyValue) request.getParameters();
            String secretKey = fetchSecretKey(sessionid);
            if (StringUtils.isEmpty(secretKey)) {
                response.finish(HttpStatus.SC_UNAUTHORIZED, "获取内部秘钥失败，请重新登录");
                return;
            }
            logger.info("获取到秘钥：" + secretKey);
            boolean isPassWithJson = false;
            String data = "";

            String contentType = request.getContentType();
            if(contentType == null)
                contentType = request.getHeader("content-type");

            try{
                if (request.getMethod().toLowerCase().equalsIgnoreCase("post") && contentType.indexOf("application/json") >= 0) {
                    isPassWithJson = true;
                }
                if(StringUtils.isEmpty(data)){
                    data = parameters.getValue("data");
                    parameters.removeValue("data", data);
                }
                if(StringUtils.isEmpty(data)){
                    data = request.getBodyUTF8();
                    if(data.indexOf("data=") != -1){
                        data = data.replaceAll("data=","");
                    }
                }


            }catch (Exception e){
                //异常正常
            }


            logger.info("获取到的加密数据：" + data);
            if (!checkSign(headers, parameters, sessionid, sign, secretKey, data, sessionidInCookie)) {
                response.finish(HttpStatus.SC_FORBIDDEN, "签名错误");
                return;
            }
            logger.info("签名验证结束");


            if (StringUtils.isNotEmpty(data)) {
                String crypted = new String(Base64Util.decode(data));
                String paramStr = decrypt(crypted, secretKey);
                logger.info("获取到的解密数据：" + paramStr);
                if (StringUtils.isNotEmpty(paramStr)) {
                    Map<String, String> map;
                    if (isPassWithJson) {
//                        map = JsonConvert.root().convertFrom(JsonConvert.TYPE_MAP_STRING_STRING, paramStr);
                        map = gson.fromJson(paramStr, JsonConvert.TYPE_MAP_STRING_STRING);
                    } else {
                        map = URLParameterUtils.parseQueryStringToMap(paramStr);
                    }
                    logger.info("获取到的解密Map：" + map);
                    if (map != null) {
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            parameters.setValue(entry.getKey(), entry.getValue());
                        }
                    }
                }
            }
            logger.info("参数处理的结果：" + parameters);
            response.nextEvent();
        } else {
            response.nextEvent();
        }
    }

    Gson gson = new Gson();

    public static void main(String[] args) {
        System.out.println("测试一下加密");
        BaseServlet baseServlet = new BaseServlet();
        AnyValue.DefaultAnyValue headers = new AnyValue.DefaultAnyValue();
        headers.addValue("currenttime", "1501230031");
        headers.addValue("random", "58850005");
        headers.addValue("sign", "CA3FA2F9E2FFB7668D6451CA144082C6");
        AnyValue.DefaultAnyValue parameters = new AnyValue.DefaultAnyValue();
        parameters.addValue("apptype", "3");
        parameters.addValue("appversion", "489");
        parameters.addValue("data", "5hD3nOiR+Bt8hCaN3EDOpg==");
        parameters.addValue("iversion", "2");
        parameters.addValue("sessionkey", "6189ceab2490f171ebee874c6c8b3a24");
        parameters.addValue("method", "system.index.getadvertise");

        String sessionkey = "6189ceab2490f171ebee874c6c8b3a24";
        String sign = "CA3FA2F9E2FFB7668D6451CA144082C6";

        System.out.println("验证签名：" + baseServlet.checkSign(headers, parameters, sessionkey, sign, "", "", false));
        String crypted = "5hD3nOiR+Bt8hCaN3EDOpg==";
        String plainText = "{\"apps\":\"3\"}";
        String random = "58850005";
        System.out.println("解密：" + plainText.equals(baseServlet.decrypt(crypted, APP_KEY_SEED + random)));
    }

    public static final String APP_KEY_SEED = "terjoycht2014!@#";

    /**
     * 获取到用户进行3DES加密的秘钥,生成秘钥的算法： md5( APP_KEY_SEED + 登陆时的secretkey )
     *
     * @param sessionkey 用于找到3des加密的秘钥 secretkey在登录时，会被写在redis中，
     *                   redis中key的格式："secretkey@"+sessionkey ->3des secretkey
     * @return
     */
    private String fetchSecretKey(String sessionkey) {
        String secretkey = getSecretKeyFromRedis(sessionkey);
        return secretkey;
    }

    private String getSecretKeyFromRedis(String sessionid) {
        Jedis jedis = null;
        try {
            jedis = redisService.getReadJedis("uc").getResource();
            String key = "secretkey@" + sessionid;
            String value = jedis.get(key);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    private boolean checkSessionKey(String sessionkey) {
        Jedis jedis = null;
        try {
            jedis = redisService.getReadJedis("uc").getResource();
            String key = "sessionid@" + sessionkey;
            return jedis.exists(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    private boolean checkSign(AnyValue.DefaultAnyValue headers, AnyValue.DefaultAnyValue parameters, String sessionkey, String sign, String secretKey, String data, boolean sessionidInCookie) {

        try {
            Map<String, String> requestMap = new HashMap();
            requestMap.put("sessionid", sessionkey);
            requestMap.put("secretkey", secretKey);
            for (String key : headers.getNames()) {
                if (key.equals("random") || key.equals("sessionid") || key.equals("timestamp")
                        || key.equals("apptype") || key.equals("iversion") || key.equals("versioncode") || key.equals("versionname"))
                    requestMap.put(key, headers.getValue(key));
            }
            if (sessionidInCookie) { //sessionid在cookie中的情况，需要从签名字符串中排除sessionid
                requestMap.remove("sessionid");
            }
            if (StringUtils.isEmpty(data))
                data = parameters.getValue("data");
            if (StringUtils.isNotEmpty(data))
                requestMap.put("data", URLDecoder.decode(data, "utf-8"));
            String toSign = CryptoUtils.changeMapToSortedString(requestMap);
            System.out.println("签名字符串：" + toSign);
            String calcSign = MD5.getInstance().getMd5(toSign).toUpperCase();
            System.out.println("传入的签名：" + sign + ",计算得到的签名：" + calcSign);
            if (calcSign.equals(sign)) {
                return true;
            } else {
                return false;
            }
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (Exception exx) {
            exx.printStackTrace();
        }
        return false;
    }

    private String decrypt(String data, String secretKey) {
        try {
            return Des3.decode(secretKey, data);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private boolean checkRandom(String random) {
        Jedis jedis = null;
        try {
            jedis = redisService.getWriteJedis("uc").getResource();
            String key = "random@" + random;
            String value = jedis.get(key);
            System.out.println("随机数： " + key + "->" + value);
            long now = System.currentTimeMillis();
            if (StringUtils.isEmpty(value)) {
                jedis.set(key, "" + now, "NX", "EX", RandomTimeout);
                return true;
            } else {
                long time = Long.parseLong(value);
                if (time >= time - Interval && time <= time + Interval) { //指定的时间内有重复，此时随机数无效
                    return false;
                } else {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    public static final long Interval = 5 * 60;
    public static final long RandomTimeout = 10 * 60;

    private boolean checkTimestamp(String timestamp) {
        long nowTime = System.currentTimeMillis() / 1000; //获取到时间戳，秒数
        long t = Long.parseLong(timestamp);
        if (nowTime - Interval <= t && t <= nowTime + Interval)
            return true;
        else
            return false;
    }
}