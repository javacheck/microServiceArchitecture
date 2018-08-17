package cn.self.cloud.commonutils.zother.config;

import cn.self.cloud.commonutils.basictype.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by cp on 2017/3/6.
 */
public class ConfigProviderFactory {
    //用于微服务下的配置管理实现
    public static final String ZookeeperConfigProvider = "ZookeeperConfigProvider";
    public static final String ZookeeperDBConfigProvider = "ZookeeperDBConfigProvider";
    public static final String RedkaleConfigProvider = "RedkaleConfigProvider";


    private static ConfigProvider defaultProvider = null;

    public static ConfigProvider getConfigProvider(String providerClassName, String configName,String zkconfig) {
        try {
            Class providerClass = Class.forName(providerClassName);
            if(StringUtils.isEmpty(zkconfig)){
                Constructor<ConfigProvider> constructor = providerClass.getConstructor(String.class);
                return constructor.newInstance(configName);
            }else {
                Constructor<ConfigProvider> constructor = providerClass.getConstructor(String.class, String.class);
                return constructor.newInstance(configName, zkconfig);
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        if (StringUtils.isEmpty(configName))
            return getDefaultProvider(ConfigPropertiesFileProvider.DefaultConfigFileName);
        else
            return getDefaultProvider(configName);
    }

    /****
     * 获取默认的基于文件的配置提供者
     *
     * @param propertiesFileName 配置文件名 ,如果是config.properties 只需要传 "config"
     * @return
     */
    public static ConfigProvider getDefaultProvider(String propertiesFileName) {
        if (defaultProvider == null) {
            defaultProvider = ConfigPropertiesFileProvider.getInstance(propertiesFileName);
        }
        return defaultProvider;
    }
}
