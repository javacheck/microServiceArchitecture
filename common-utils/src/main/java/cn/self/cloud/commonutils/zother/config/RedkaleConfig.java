package cn.self.cloud.commonutils.zother.config;

import org.redkale.util.ResourceFactory;
import org.redkale.util.TypeToken;

import java.util.List;
import java.util.Map;

/**
 * Created by cp on 2017/8/15.
 */
public class RedkaleConfig implements Config {
    private ResourceFactory resourceFactory;

    public RedkaleConfig(ResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
    }

    public RedkaleConfig() {
        this.resourceFactory = ResourceFactory.root();
    }

    @Override
    public String getString(String key, String def) {
        String value = resourceFactory.find(key, String.class);
        return value == null ? def : value;
    }

    @Override
    public String getString(String key) {
        String value = resourceFactory.find(key, String.class);
        return value;
    }

    @Override
    public int getInt(String key) {
        Integer value = resourceFactory.find(key, Integer.class);
        return value == null ? 0 : value;
    }

    @Override
    public int getInt(String key, int def) {
        Integer value = resourceFactory.find(key, Integer.class);
        return value == null ? def : value;
    }

    @Override
    public long getLong(String key) {
        Long value = resourceFactory.find(key, Long.class);
        return value;
    }

    @Override
    public long getLong(String key, long def) {
        Long value = resourceFactory.find(key, Long.class);
        return value == null ? def : value;
    }

    @Override
    public String[] getStringArrary(String key) {
        return resourceFactory.find(key, new TypeToken<String[]>() {
        }.getType());
    }

    @Override
    public List<String> getStringList(String key) {
        return resourceFactory.find(key, new TypeToken<List<String>>() {
        }.getType());
    }

    @Override
    public int[] getIntArrary(String key) {
        return resourceFactory.find(key, new TypeToken<int[]>() {
        }.getType());
    }

    @Override
    public List<Integer> getIntList(String key) {
        return resourceFactory.find(key, new TypeToken<List<Integer>>() {
        }.getType());
    }

    @Override
    public Map<String, Object> getMap(String key) {
        return resourceFactory.find(key, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    @Override
    public List<String> getKeys() {
        return null;
    }
}
