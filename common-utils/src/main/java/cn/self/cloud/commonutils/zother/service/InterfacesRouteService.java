package cn.self.cloud.commonutils.zother.service;

import cn.self.cloud.commonutils.zother.config.ConfigManager;
import com.google.gson.Gson;
import com.terjoy.interfaces.entity.JsonResult;
import com.terjoy.interfaces.exception.InterfaceCallException;
import com.terjoy.interfaces.exception.InterfaceCallExceptionFactory;
import com.terjoy.interfaces.exception.InterfaceNotFoundException;
import com.terjoy.interfaces.exception.ModuleNotFoundException;
import cn.self.cloud.commonutils.internet.http.HttpClient;
import cn.self.cloud.commonutils.internet.http.Response;
import cn.self.cloud.commonutils.zother.service.impl.InterfacesRouteServiceImpl;
import cn.self.cloud.commonutils.zother.service.impl.LocalInterfacesRouteServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.redkale.service.Service;
import org.redkale.util.AnyValue;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 进行接口路由的获取
 * Created by cp on 2017/7/10.
 */
public class InterfacesRouteService implements Service {


    @Resource(name = "property.module.name")
    private String moduleName;

    private boolean isLocalDebugMode;

    @Resource
    InterfacesRouteServiceImpl impl;
    @Resource
    LocalInterfacesRouteServiceImpl localImpl;

    @Override
    public void init(AnyValue config) {
        String localModeString = ConfigManager.getConfig("redkale", moduleName, "").getString("property.isLocalDebugMode", "");
        if (StringUtils.isEmpty(localModeString)) {
            isLocalDebugMode = false;
        } else {
            isLocalDebugMode = Boolean.valueOf(localModeString);
        }
    }

    /***
     * 获取指定模块的访问url前缀 比如 ： http://192.168.1.123:2001/
     * 用于组合得到具体接口的访问url
     *
     * @param moduleName
     * @return
     */
    public String getRoute(String moduleName) {
        if (!isLocalDebugMode)
            return impl.getRoute(moduleName);
        else
            return localImpl.getRoute(moduleName);
    }

    public <T> T callInterface(String method, String moduleName, String url, Map<String, String> header, byte[] body, Type cls) throws InterfaceCallException {
        String preffix = getRoute(moduleName);
        if (StringUtils.isEmpty(preffix)) {
            throw new ModuleNotFoundException("没有找到指定的模块");
        }
        String totalUrl = preffix + "/" + moduleName + url;
        Response response = HttpClient.Do(method, totalUrl, header, body);
        if (response == null) {
            throw new InterfaceNotFoundException("接口未找到");
        } else {
            System.out.println("url:" + totalUrl + "传入的返回类型：" + cls.toString() + " \n参数" + new String((body == null ? new byte[0] : body)));
            String json = response.getBody();
            System.out.println("url:" + totalUrl + "返回的数据：" + json);
            if (StringUtils.isEmpty(json) && response.getStatuscode() == 500) {
                throw InterfaceCallExceptionFactory.produceHttpException(500, "调用内部接口出错");
            }
            JsonResult<T> result = null;
            try {
                result = gson.fromJson(json, cls);
//                result =JsonConvert.root().convertFrom(cls, json);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw InterfaceCallExceptionFactory.produceHttpException(500, ex.getMessage());
            }
            if (response.getStatuscode() < 200 || response.getStatuscode() >= 300) {
                if (result != null)
                    throw InterfaceCallExceptionFactory.produceHttpException(response.getStatuscode(), result.getMsg());
                else
                    throw InterfaceCallExceptionFactory.produceHttpException(response.getStatuscode(), "");
            } else {
                if (result.getCode() == 1 || result.getCode() == 200)
                    return (T) result.getData();
                else
                    throw InterfaceCallExceptionFactory.produceHttpException(result.getCode(), result.getMsg());
            }
        }
    }

    Gson gson = new Gson();
    /***
     * 基本类型的名称集合，用于排除返回为基本类型的情况
     */
    private static List<String> singleTypeList = new ArrayList();

    static {
        singleTypeList.add(boolean.class.getTypeName());
        singleTypeList.add(String.class.getTypeName());
        singleTypeList.add(double.class.getTypeName());
        singleTypeList.add(int.class.getTypeName());
        singleTypeList.add(float.class.getTypeName());
    }
}
