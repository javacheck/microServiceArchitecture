package cn.self.cloud.commonutils.zother;

import cn.self.cloud.commonutils.api.JsonResult;
import com.terjoy.interfaces.entity.JsonResult;
import com.terjoy.platform.entity.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.redkale.convert.json.JsonConvert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 一些关于返回JsonResult的功能封装
 * Created by cp on 2017/8/10.
 */
public class JsonResultUtils {
    /**
     * 将Map<String,Object>封装成加密后的JsonResult对象
     *
     * @param userInfo
     * @param map
     * @return
     */
    public static JsonResult produceJsonResult(UserInfo userInfo, Map<String, Object> map) {
        return produceJsonResult(userInfo, map, false);
    }

    /***
     * 封装成指定code和msg字段的JsonResult对象
     *
     * @param userInfo
     * @param code
     * @param msg
     * @param map
     * @return
     */
    public static JsonResult produceJsonResult(UserInfo userInfo, int code, String msg, Map<String, Object> map) {
        return produceJsonResult(userInfo, code, msg, map, false);
    }

    /***
     * 返回json对象
     *
     * @param userInfo
     * @param code
     * @param msg
     * @param obj
     * @return
     */
    public static JsonResult produceJsonResult(UserInfo userInfo, int code, String msg, Object obj) {
        String data = "";
        if (obj != null) {
            data = CryptoUtils.encryptAndEncodeByDes3AndBase64(userInfo.getSecretkey(), JsonConvert.root().convertTo(obj));
        }
        if (code == 1)
            return new JsonResult(code, msg, data);
        else
            return new JsonResult(code, msg);
    }

    /***
     * @param userInfo
     * @param code
     * @param msg
     * @param obj
     * @param isPlainText 是否为明文
     * @return
     */
    public static JsonResult produceJsonResult(UserInfo userInfo, int code, String msg, Object obj, boolean isPlainText) {
        JsonResult jsonResult = new JsonResult(code, msg);
        if (obj != null) {
            if (isPlainText) {
                jsonResult.setData(obj);
            } else {
                jsonResult.setData(CryptoUtils.encryptAndEncodeByDes3AndBase64(userInfo.getSecretkey(), JsonConvert.root().convertTo(obj)));
            }
        }
        return jsonResult;
    }


    /**
     * 将Map<String,Object>封装成JsonResult对象
     *
     * @param userInfo
     * @param map
     * @param isPlainText
     * @return
     */
    public static JsonResult produceJsonResult(UserInfo userInfo, Map<String, Object> map, boolean isPlainText) {
        return produceJsonResult(userInfo, 0, null, map, isPlainText);
    }

    /**
     * 将Map<String,Object>封装成加密后的JsonResult对象
     *
     * @param userInfo
     * @param map
     * @param isPlainText 是否为明文封装 true 是  false 否
     * @return
     */
    public static JsonResult produceJsonResult(UserInfo userInfo, int code, String msg, Map<String, Object> map, boolean isPlainText) {

        System.out.println("产生结果数据传入参数：userInfo=" + userInfo + ",code= " + code + ",msg=" + msg + ",map=" + map + ",isPlainText=" + isPlainText);
        if (!isPlainText && userInfo == null) {
            return new JsonResult(-4, "登录超时，请重新登录");
        }
        int result = Integer.MAX_VALUE;
        String message = null;
        //先进行空字段的过滤
        if (map != null && !map.isEmpty()) {
            List<String> removeList = new ArrayList<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getKey().equals("result")) {
                    result = (Integer) entry.getValue();
                    removeList.add(entry.getKey());
                } else if (entry.getKey().equals("message")) {
                    message = (String) entry.getValue();
                    removeList.add(entry.getKey());
                } else if (entry.getValue() == null) {
                    removeList.add(entry.getKey());
                }
            }
            for (String key : removeList) {
                map.remove(key);
            }
        }
        if (code != 0 && StringUtils.isNotEmpty(msg)) { //都传了，不用处理

        } else if (code == 0 && StringUtils.isNotEmpty(msg)) { // 传了msg没传code，表示成功
            code = 1;
        } else if (code == 0 && StringUtils.isEmpty(msg)) {// 都没传，则优先去map里面取
            if (result != Integer.MAX_VALUE && StringUtils.isNotEmpty(message)) {
                code = result;
                msg = message;
            } else { //都没有，就采用默认返回
                code = 1;
                msg = "OK";
            }
        }
        if (isPlainText) {
            return new JsonResult(code, msg, map);
        } else {
            if (map != null) {
                return new JsonResult(code, msg, CryptoUtils.encryptAndEncodeByDes3AndBase64(userInfo.getSecretkey(), JsonConvert.root().convertTo(map)));
            } else {
                return new JsonResult(code, msg);
            }
        }
    }


}
