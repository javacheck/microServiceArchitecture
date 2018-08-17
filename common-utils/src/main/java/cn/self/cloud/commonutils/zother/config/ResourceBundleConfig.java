package cn.self.cloud.commonutils.zother.config;

import cn.self.cloud.commonutils.basictype.StringUtils;
import cn.self.cloud.commonutils.basictype.StringChangeUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.*;

/**
 * Created by cp on 2017/3/6.
 */
public class ResourceBundleConfig implements Config {

    private ResourceBundle resourceBundle;
    private ConfigProvider configProvider;

    public ResourceBundleConfig(ResourceBundle resourceBundle, ConfigProvider configProvider) {
        this.resourceBundle = resourceBundle;
        this.configProvider = configProvider;
    }

    @Override
    public String getString(String key, String def) {
        configProvider.checkAndRefreshConfig(this);
        String string = resourceBundle.getString(key);
        if (StringUtils.isEmpty(string))
            return def;
        return string;
    }

    @Override
    public String getString(String key) {
        configProvider.checkAndRefreshConfig(this);
        return resourceBundle.getString(key);
    }

    @Override
    public int getInt(String key) {
        configProvider.checkAndRefreshConfig(this);
        return getInt(key, 0);
    }

    @Override
    public int getInt(String key, int def) {
        configProvider.checkAndRefreshConfig(this);
        String string = resourceBundle.getString(key);
        int i = def;
        try {
            i = Integer.parseInt(string);
        } catch (NumberFormatException e) {
        }
        return i;
    }

    @Override
    public long getLong(String key) {
        configProvider.checkAndRefreshConfig(this);
        return getLong(key, 0);
    }

    @Override
    public long getLong(String key, long def) {
        configProvider.checkAndRefreshConfig(this);
        String string = resourceBundle.getString(key);
        long i = def;
        try {
            i = Long.parseLong(string);
        } catch (NumberFormatException e) {
        }
        return i;
    }

    @Override
    public String[] getStringArrary(String key) {
        configProvider.checkAndRefreshConfig(this);
        String stringArr = resourceBundle.getString(key);
        List<String> list = StringChangeUtils.splitStringToList(String.class, stringArr, ",");
        return list.toArray(new String[]{});
    }

    @Override
    public List<String> getStringList(String key) {
        configProvider.checkAndRefreshConfig(this);
        String stringArr = resourceBundle.getString(key);
        List<String> list = StringChangeUtils.splitStringToList(String.class, stringArr, ",");
        return list;
    }

    @Override
    public int[] getIntArrary(String key) {
        configProvider.checkAndRefreshConfig(this);
        String stringArr = resourceBundle.getString(key);
        List<Integer> list = StringChangeUtils.splitStringToList(Integer.class, stringArr, ",");
        int[] arr = new int[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    @Override
    public List<Integer> getIntList(String key) {
        configProvider.checkAndRefreshConfig(this);
        String stringArr = resourceBundle.getString(key);
        List<Integer> list = StringChangeUtils.splitStringToList(Integer.class, stringArr, ",");
        return list;
    }

    @Override
    public Map<String, Object> getMap(String key) {
        configProvider.checkAndRefreshConfig(this);
        String stringArr = resourceBundle.getString(key);
        if (StringUtils.isEmpty(stringArr)) {
            return new HashMap<String, Object>();
        }
        Gson gson = new Gson();
        return gson.fromJson(stringArr, new TypeToken<HashMap<String, Object>>() {
        }.getType());
    }

    @Override
    public List<String> getKeys() {
        configProvider.checkAndRefreshConfig(this);
        Enumeration<String> enumeration = resourceBundle.getKeys();
        List<String> list = new ArrayList<>();
        while (enumeration.hasMoreElements())
            list.add(enumeration.nextElement());
        return list;
    }
}
