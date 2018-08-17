package cn.self.cloud.commonutils.zother.config;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapBasedConfig implements Config {

    private Map<String, Object> map;
    private Gson gson = new Gson();
    private ConfigProvider configProvider;

    public MapBasedConfig(Map<String, Object> map,   ConfigProvider configProvider) {
        this.map = map;
        this.configProvider=configProvider;
    }

    @Override
    public String getString(String key, String def) {
        configProvider.checkAndRefreshConfig(this);
        Object str = map.get(key);
        if (str == null)
            return def;
        return String.valueOf(str);
    }

    @Override
    public String getString(String key) {
        configProvider.checkAndRefreshConfig(this);
        return String.valueOf(map.get(key));
    }

    @Override
    public int getInt(String key) {
        configProvider.checkAndRefreshConfig(this);
        Object str = map.get(key);
        if (str == null)
            return 0;
        return ((Double) str).intValue();
    }

    @Override
    public int getInt(String key, int def) {
        configProvider.checkAndRefreshConfig(this);
        Object str = map.get(key);
        if (str == null)
            return def;
        return ((Double) str).intValue();
    }

    @Override
    public long getLong(String key) {
        configProvider.checkAndRefreshConfig(this);
        Object str = map.get(key);
        if (str == null)
            return 0;
        return ((Double) str).longValue();
    }

    @Override
    public long getLong(String key, long def) {
        configProvider.checkAndRefreshConfig(this);
        Object str = map.get(key);
        if (str == null)
            return def;
        return ((Double) str).longValue();
    }

    @Override
    public String[] getStringArrary(String key) {
        configProvider.checkAndRefreshConfig(this);
        Object str = map.get(key);
        if (str == null)
            return null;
        return ((List<String>) str).toArray(new String[0]);
    }

    @Override
    public List<String> getStringList(String key) {
        configProvider.checkAndRefreshConfig(this);
        Object str = map.get(key);
        if (str == null)
            return null;
        return (List<String>) str;
    }

    @Override
    public int[] getIntArrary(String key) {
        configProvider.checkAndRefreshConfig(this);
        List<Integer> list = getIntList(key);
        if (list == null || list.isEmpty())
            return new int[0];
        int[] arr = new int[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    @Override
    public List<Integer> getIntList(String key) {
        configProvider.checkAndRefreshConfig(this);
        Object str = map.get(key);
        if (str == null)
            return null;
        return (List<Integer>) str;
    }

    @Override
    public Map<String, Object> getMap(String key) {
        configProvider.checkAndRefreshConfig(this);
        Object str = map.get(key);
        if (str == null)
            return null;
        return (Map<String, Object>) str;
    }

    @Override
    public List<String> getKeys() {
        configProvider.checkAndRefreshConfig(this);
        List<String> list = new ArrayList<>();
        for (String key : map.keySet()) {
            list.add(key);
        }
        return list;
    }
}
