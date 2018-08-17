package cn.self.cloud.commonutils.zother.config;

import cn.self.cloud.commonutils.basictype.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * 基于本地classes目录下的config.properties文件的配置信息服务提供者
 * Created by cp on 2017/3/6.
 */
public class ConfigPropertiesFileProvider implements ConfigProvider {

    public static final String DefaultConfigFileName = "config";
    private static Map<String, ConfigPropertiesFileProvider> providerMap =
            new HashMap<String, ConfigPropertiesFileProvider>();
    private String configFileName;

    private ConfigPropertiesFileProvider(String configFileName) {
        this.configFileName = configFileName;
    }

    public synchronized static ConfigPropertiesFileProvider getInstance(String configFileName) {
        String name = configFileName;
        if (StringUtils.isEmpty(configFileName)) {
            name = DefaultConfigFileName;
        }
        ConfigPropertiesFileProvider provider = providerMap.get(name);
        if (provider == null) {
            provider = new ConfigPropertiesFileProvider(name);
            providerMap.put(name, provider);
        }
        return provider;
    }

    @Override
    public Config getConfig() {
        lastRefreshTime = System.currentTimeMillis();
        return refreshConfig();
    }

    private Config refreshConfig() {
        try {
            ResourceBundle bundle = PropertyResourceBundle.getBundle(configFileName);
            return new ResourceBundleConfig(bundle, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private long lastRefreshTime = 0;

    @Override
    public void checkAndRefreshConfig(Config config) {
        synchronized (this) {
            long now = System.currentTimeMillis();
            if (now - lastRefreshTime >= ConfigProvider.ConfigRefreshInterval) {
                lastRefreshTime = now;
                config = refreshConfig();
            }
        }
    }
}
