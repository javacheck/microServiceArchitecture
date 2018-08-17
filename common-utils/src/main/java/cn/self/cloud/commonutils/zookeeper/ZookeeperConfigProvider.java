package cn.self.cloud.commonutils.zookeeper;

import cn.self.cloud.commonutils.zother.config.Config;
import cn.self.cloud.commonutils.zother.config.ConfigProvider;
import cn.self.cloud.commonutils.zother.config.MapBasedConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.zookeeper.CreateMode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cp on 2017/3/7.
 */
public class ZookeeperConfigProvider implements ConfigProvider {

    private ZooKeeperService zooKeeperService;
    public static final String ConfigPreffix = "/tj/config/common/";
    private String nodePath;
    private Gson gson = new Gson();


    public ZookeeperConfigProvider(String configName, String zkConfig) {
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
            if (!zooKeeperService.exist(nodePath)) {
                try {
                    zooKeeperService.create(nodePath, null, CreateMode.PERSISTENT);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            byte[] data = zooKeeperService.getData(nodePath);
            Map<String, Object> map;
            if (data != null) {
                String json = new String(data);
                map = gson.fromJson(json, new TypeToken<Map<String, Object>>() {
                }.getType());
                if (map == null)
                    map = new HashMap();
            } else {
                map = new HashMap();
            }
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
