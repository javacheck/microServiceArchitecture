package cn.self.cloud.commonutils.password;

import cn.self.cloud.commonutils.internet.http.Response;
import cn.self.cloud.commonutils.api.JsonResult;
import java.util.Map;

/**
 * Created by cp on 2017/10/23.
 */
public interface SecureClient {
    /****
     * 发送请求到前置服务器
     *
     * @param method
     * @param urlSuffix
     * @param parameters
     * @return
     * @throws Exception
     */
    Response sendRequest(String method, String urlSuffix, Map<String, Object> parameters) throws Exception;

    /**
     * 进行加密字符串的解密
     *
     * @param cipher
     * @return
     */
    String decryptString(String cipher) throws Exception ;

    /***
     * 向前置服务注册
     *
     * @return
     */
    boolean regist();

    /***
     * 将返回对象反序列化为JsonResult<String>
     * @param responseBody
     * @return
     * @throws Exception
     */
      JsonResult<String> transResonpseToJsonResult(String responseBody) throws Exception  ;

}
