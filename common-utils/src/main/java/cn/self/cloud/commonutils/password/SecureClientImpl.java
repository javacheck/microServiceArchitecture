package cn.self.cloud.commonutils.password;

import cn.self.cloud.commonutils.internet.URLParameterUtils;
import cn.self.cloud.commonutils.api.JsonResult;
import cn.self.cloud.commonutils.internet.http.HttpClient;
import cn.self.cloud.commonutils.internet.http.Response;
import cn.self.cloud.commonutils.random.RandomUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cp on 2017/10/23.
 */
public class SecureClientImpl implements SecureClient {
    /***
     * @param clientno   客户端代码，对应会员号
     * @param apptype    长沙银行服务回调客户端 ：13
     * @param urlPreffix 服务前缀 比如： http://xxxx:6063/nyd
     * @param publicKey  客户端公钥
     * @param privateKey 客户端私钥
     */
    public SecureClientImpl(String clientno, int apptype, String urlPreffix, String publicKey, String privateKey) {
        this(clientno, apptype, urlPreffix, publicKey, privateKey, "123456");
    }

    /***
     * @param clientno   客户端代码，对应会员号
     * @param apptype    长沙银行服务回调客户端 ：13
     * @param urlPreffix 服务前缀 比如： http://xxxx:6063/nyd
     * @param publicKey  客户端公钥
     * @param privateKey 客户端私钥
     */
    public SecureClientImpl(String clientno, int apptype, String urlPreffix, String publicKey, String privateKey, String password) {
        this.clientno = clientno;
        this.apptype = apptype;
        this.urlPreffix = urlPreffix;
        this.myPrivateKey = privateKey;
        this.myPublicKey = publicKey;
        this.password = password;
    }

    public Response sendRequest(String method, String urlSuffix, Map<String, Object> parameters) throws Exception {
        Map<String, String> header = commonHeader();

        Response response;
        if (method.equalsIgnoreCase("get")) {
            Map<String, Object> map = new HashMap();
            String data = new String(Base64Util.encode(Des3.encode(produceSecretKey(), URLParameterUtils.parseMapToQueryString(parameters)).getBytes()));
            String sign = produceSign(header, data);
            header.put("sign", sign);
            map.put("data", data);
            String url = URLParameterUtils.concatUrlWithParameters(urlPreffix + urlSuffix, map);
            response = HttpClient.Do(method, url, header, null);
        } else {
            String plainData = new Gson().toJson(parameters);
            String data = new String(Base64Util.encode(Des3.encode(produceSecretKey(), plainData).getBytes()));
            String sign = produceSign(header, data);
            header.put("sign", sign);
            String bodyStr = data;
            response = HttpClient.Do(method, urlPreffix + urlSuffix, header, bodyStr.getBytes());
        }
        return response;
    }

    @Override
    public String decryptString(String cipher) throws Exception {
        return CryptoUtils.decodeAndDecryptByBase64AndDes3(produceSecretKey(), cipher);
    }

    public JsonResult<String> transResonpseToJsonResult(String responseBody) throws Exception {
        JsonResult<String> jsonResult =  new Gson().fromJson(responseBody,new TypeToken<JsonResult<String>>() {}.getType());
        return jsonResult;
    }

    private String secretKey = "";
    private String sessionid = "";
    private int apptype;
    private String clientno;
    private String password = "";

    private static final String APP_KEY_SEED = "terjoycht2014!@#";
    //    public static final String UrlPreffix = "http://121.41.88.144:6063";
//        public static final String UrlPreffix = "http://120.26.80.24:6063";
    private String urlPreffix;
    //    public static final String UrlPreffix = "http://210.43.57.135:6063";
//    public static final String SettleUrlPreffix = "http://121.41.88.144:6067";
//    public static final String SettleUrlPreffix = "http://localhost:6067";


    private String myPrivateKey;//= "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAIZnE/e7erkmYVys/bO94z5lgLnsGQokIF1qEKP9EEPQ+mgwgEQIO6tnfi/dZeZIlCGccLuIyYaUeFJFSwuTp0dznexXc0pYVgwqgDvaGjkhmojlg2cKzD+1nHnE9/DVnwc7hHeBcdo4HO+vrAcKR7SiLI6Tr6qyphiszqPdYGNJAgMBAAECgYBYtYBAasW9ns90U9m/4P00Gdq03FhKkT2ram5BTq6FlFEyo5CFVsnLgCuRfBfdvtnGtXLS1J3u7d/crU+y/GnTjfTVeB5gX0lGRiQWNSOeDPjglJZ0ar9VNHMGUDjB42Ga+3mFYwvOx2rO+JKzXf+I7LgRf1yGAaMTgEiPUlxIAQJBAOBgvit/Me8JbWua152mfBwXN+PKdY1rBAMcZCqX1TJa0nNrExhSUacqm8QbmdI04B4CWwHWALE8C9ugJAvtOGECQQCZWCW2/Gzj9o84Ot9sWwE+YcM4aa4b94KBKhZvoKDMMzf4misYS+LJ5PxbevSf4t//BZ0PYFTvgtQ3/kGKdPPpAkEAmocmEXDNI/IbMKN4YXO6P+JhtHjH0rhkD/xqo8bjpoVUMPcKNJLk9zrqAfp8Sn/t5dz7bl1DJszS4dP9X1VloQJBAJaVViv0hGX0SL5aKe85CuackF3EgaKbEwAOOYwS+/Sq02sA0hMA6kuC30JiYvjjjT3c4FgqLB4rpoT53Ji/sokCQQDfnO7roKJ8zmBmkM2kQI7Rz6PM6/nnPTaNls91yTlfKOHmIjRTWL/R7JkCEK+mBXCCXhUmJ55T/+Fgp8pik4X3";
    private String myPublicKey;//= "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCGZxP3u3q5JmFcrP2zveM+ZYC57BkKJCBdahCj/RBD0PpoMIBECDurZ34v3WXmSJQhnHC7iMmGlHhSRUsLk6dHc53sV3NKWFYMKoA72ho5IZqI5YNnCsw/tZx5xPfw1Z8HO4R3gXHaOBzvr6wHCke0oiyOk6+qsqYYrM6j3WBjSQIDAQAB";
    private static String serverPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCH8vvb0YGmBWCz9KIgRQ/kmyDJqggdNB4K5VYpLifvbbtCLsseCRwB03FwqwqGUCJ4QCJP9dxWXTqD1CtKqPmqV81ijxwRlqs5YxIno0sOzuJeNTNWcfzBRvK0iBik72ygt8wMz0xvu1kWG2gC2V5S2bMR/7sUcSaix4kMsguzDQIDAQAB";


    /***
     * 向前置服务注册
     *
     * @return
     */
    public boolean regist() {
        Map<String, String> header = new HashMap();
        Map<String, Object> params = new HashMap();
        params.put("username", clientno);
        params.put("userpass", password);
        header.put("apptype", "" + apptype);
        params.put("publickey", myPublicKey);
        String up =  new Gson().toJson(params);
        String url = urlPreffix + "/login";
        Response response = HttpClient.Do("post", url, header, up.getBytes());
        if (response.getStatuscode() != 200) {
            System.out.println("出错：" + response.toString());
            return false;
        }
        String json = response.getBody();
        System.out.println(json);
        Map<String, Object> map = new Gson().fromJson(json, new TypeToken<Map<String, Object>>() {
        }.getType());
        String encryptedKey = (String) ((Map<String, Object>) map.get("data")).get("secretkey");

        System.out.println(encryptedKey);
        String plainText = decrypt(encryptedKey);
        System.out.println("plainText:" + plainText);
        secretKey = plainText;
        String cryptedInfo = (String) ((Map<String, Object>) map.get("data")).get("info");
        System.out.println("得到加密info：" + cryptedInfo);
        // Map<String, String> infoMap = JsonConvert.root().convertFrom(JsonConvert.TYPE_MAP_STRING_STRING, CryptoUtils.decodeAndDecryptByBase64AndDes3(produceSecretKey(), cryptedInfo));
        // System.out.println("解密infoMap:" + infoMap);
        // sessionid = infoMap.get("sessionid");
        return true;
    }

    private String produceSecretKey() {
        return MD5Utils.getMd5(APP_KEY_SEED + secretKey);
    }

    private String produceSign(Map<String, String> header, String data) throws Exception {
        Map<String, String> allParams = new HashMap();
        allParams.putAll(header);
        allParams.put("data", data);
        allParams.put("secretkey", produceSecretKey());
        allParams.remove("sign");
        String signStr = CryptoUtils.changeMapToSortedString(allParams);
        System.out.println("签名字符串:" + signStr);
        return MD5Utils.getMd5(signStr);
    }

    private String decrypt(String encryptedKey) {
        try {
            RSAPublicKey servPubKey = RSAEncrypt.loadPublicKeyByStr(serverPublicKey);
            RSAPrivateKey usrPriKey = RSAEncrypt.loadPrivateKeyByStr(myPrivateKey);
            return new String(RSAEncrypt.decrypt(servPubKey, RSAEncrypt.decrypt(usrPriKey, Base64Util.decode(encryptedKey))));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private Map<String, String> commonHeader() {
        Map<String, String> header = new HashMap();
        header.put("timestamp", "" + System.currentTimeMillis() / 1000);
        header.put("random", RandomUtils.getRand(8));
        header.put("sessionid", sessionid);
        header.put("versioncode", "369");
        header.put("iversion", "2");
        header.put("versionname","2.0.0");
        header.put("apptype", "" + apptype);
        return header;
    }
}
