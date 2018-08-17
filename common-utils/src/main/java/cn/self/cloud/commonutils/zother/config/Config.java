package cn.self.cloud.commonutils.zother.config;

import java.util.List;
import java.util.Map;

/**
 * 获取配置信息的接口,配置里面字符串用逗号分割，
 * map结构的配置方式采用json字符串 ：   key= "{key1=value1,key2=value2,key3=value3}"
 * Created by cp on 2017/3/6.
 */
public interface Config {
    String getString(String key, String def);
    String getString(String key);
    int getInt(String key);
    int getInt(String key, int def);
    long getLong(String key);
    long getLong(String key, long def);

    /***
     * 获取用逗号分割的字符串数组
     * @param key
     * @return
     */
    String[] getStringArrary(String key);
    List<String> getStringList(String key);
    int[] getIntArrary(String key);
    List<Integer> getIntList(String key);
    Map<String,Object> getMap(String key);
    List<String> getKeys();
}
