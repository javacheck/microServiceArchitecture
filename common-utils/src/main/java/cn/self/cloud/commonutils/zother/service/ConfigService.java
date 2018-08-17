package cn.self.cloud.commonutils.zother.service;

import cn.self.cloud.commonutils.zother.config.Config;
import cn.self.cloud.commonutils.zother.config.ConfigManager;
import org.redkale.service.AbstractService;
import org.redkale.service.Local;
import org.redkale.service.Service;
import org.redkale.util.AnyValue;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 进行配置读取的服务
 * Created by cp on 2017/7/7.
 */
@Local
public class ConfigService extends AbstractService implements Service {

    private Config config;

    @Resource(name = "property.config.mode")
    private String configMode;

    @Resource(name = "property.module.name")
    private String moduleName;
    @Resource(name = "property.zookeeper.config")
    String zkConfig;

    @Override
    public void init(AnyValue config) {
        this.config = ConfigManager.getConfig(configMode, moduleName, zkConfig);
    }


    public String getString(String key, String def) {
        return config.getString(key, def);
    }


    public String getString(String key) {
        return config.getString(key);
    }


    public int getInt(String key) {
        return config.getInt(key);
    }


    public int getInt(String key, int def) {
        return config.getInt(key, def);
    }


    public long getLong(String key) {
        return config.getLong(key);
    }


    public long getLong(String key, long def) {
        return config.getLong(key, def);
    }


    public String[] getStringArrary(String key) {
        return config.getStringArrary(key);
    }


    public List<String> getStringList(String key) {
        return config.getStringList(key);
    }


    public int[] getIntArrary(String key) {
        return config.getIntArrary(key);
    }


    public List<Integer> getIntList(String key) {
        return config.getIntList(key);
    }


    public Map<String, Object> getMap(String key) {
        return config.getMap(key);
    }


    public List<String> getKeys() {
        return config.getKeys();
    }
}
