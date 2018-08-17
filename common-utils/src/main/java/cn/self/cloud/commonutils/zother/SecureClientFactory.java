package cn.self.cloud.commonutils.zother;


import cn.self.cloud.commonutils.password.MD5Utils;
import cn.self.cloud.commonutils.password.SecureClient;
import cn.self.cloud.commonutils.password.SecureClientImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cp on 2017/10/23.
 */
public class SecureClientFactory {
    private static Map<String, SecureClient> clientMap = new HashMap();

    /***
     * 获取客户端对象
     *
     * @param clientno
     * @param apptype    1:  车海通android 调用
     *                   2： 车海通ios调用
     *                   3:  牛运android调用
     *                   4:  牛运 ios 客户端
     *                   5： 天骄联盟管理软件android客户端
     *                   6： 天骄联盟软件ios客户端
     *                   7： 油宝安卓客户端
     *                   8： 油宝ios客户端
     *                   9： 油宝后台web端
     *                   10：无车承运web端
     *                   11：通贝安卓客户端
     *                   12：通贝ios客户端
     *                   13：通贝长沙银行回调客户端
     *                   14：测试客户端
     *                   15 ： 牛运2.0安卓版
     *                   16 ：牛运2.0IOS版
     *                   17 ：无车承运2.0 web 货主版
     *                   18 ： 牛运交易保障 2.0 web货主版
     *                   19  ：无车承运2.0 web 调度版
     *                   20  ： 牛运2.0及无车承运2.0后台管理web端
     * @param urlPreffix
     * @param publicKey
     * @param privateKey
     * @return
     */
    public static SecureClient getSecureClient(String clientno, int apptype, String urlPreffix, String publicKey, String privateKey) {
        String hash = MD5Utils.getMd5(clientno + apptype + urlPreffix + publicKey + privateKey);
        synchronized (clientMap) {
            if (clientMap.containsKey(hash)) {
                return clientMap.get(hash);
            } else {
                SecureClient client = new SecureClientImpl(clientno, apptype, urlPreffix, publicKey, privateKey);
                clientMap.put(hash, client);
                return client;
            }
        }
    }

    /***
     * 获取客户端对象
     *
     * @param clientno
     * @param apptype    1:  车海通android 调用
     *                   2： 车海通ios调用
     *                   3:  牛运android调用
     *                   4:  牛运 ios 客户端
     *                   5： 天骄联盟管理软件android客户端
     *                   6： 天骄联盟软件ios客户端
     *                   7： 油宝安卓客户端
     *                   8： 油宝ios客户端
     *                   9： 油宝后台web端
     *                   10：无车承运web端
     *                   11：通贝安卓客户端
     *                   12：通贝ios客户端
     *                   13：通贝长沙银行回调客户端
     *                   14：测试客户端
     *                   15 ： 牛运2.0安卓版
     *                   16 ：牛运2.0IOS版
     *                   17 ：无车承运2.0 web 货主版
     *                   18 ： 牛运交易保障 2.0 web货主版
     *                   19  ：无车承运2.0 web 调度版
     *                   20  ： 牛运2.0及无车承运2.0后台管理web端
     * @param urlPreffix
     * @param publicKey
     * @param privateKey
     * @return
     */
    public static SecureClient getSecureClient(String clientno, int apptype, String urlPreffix, String publicKey, String privateKey, String password) {
        String hash = MD5Utils.getMd5(clientno + apptype + urlPreffix + publicKey + privateKey);
        synchronized (clientMap) {
            if (clientMap.containsKey(hash)) {
                return clientMap.get(hash);
            } else {
                SecureClient client = new SecureClientImpl(clientno, apptype, urlPreffix, publicKey, privateKey, password);
                clientMap.put(hash, client);
                return client;
            }
        }
    }
}
