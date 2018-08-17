package cn.self.cloud.commonutils.zookeeper;

import cn.self.cloud.commonutils.zother.config.Config;
import cn.self.cloud.commonutils.zother.config.ConfigProvider;
import cn.self.cloud.commonutils.zother.config.MapBasedConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

/**
 * Created by cp on 2017/3/7.
 */
public class ZookeeperDBConfigProvider implements ConfigProvider {

    private ZooKeeperService zooKeeperService;
    public static final String ConfigPreffix = "/tj/config/mysql/";
    private String nodePath;
    private Gson gson = new Gson();

    public ZookeeperDBConfigProvider(String configName, String zkConfig) {
        zooKeeperService = ZookeeperManager.getZookeeperService(zkConfig);
        nodePath = ConfigPreffix + configName;
    }

    @Override
    public Config getConfig() {
        lastRefreshTime = System.currentTimeMillis();
        return refreshConfig();
    }

    private Config refreshConfig() {
        try {
            if (!zooKeeperService.exist(nodePath))
                return null;
            byte[] data = zooKeeperService.getData(nodePath);
            String json = new String(data);
            Map<String, Object> map = ((List<Map<String, Object>>) gson.fromJson(json, new TypeToken<List<Map<String, Object>>>() {
            }.getType())).get(0);
            MapBasedConfig config = new MapBasedConfig(map, this);
            return config;
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
