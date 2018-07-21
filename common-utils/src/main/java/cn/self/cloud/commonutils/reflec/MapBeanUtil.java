package cn.self.cloud.commonutils.reflec;

import cn.self.cloud.commonutils.api.MapBean;
import cn.self.cloud.commonutils.json.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Map处理工具
 */
public class MapBeanUtil {
    public static List<MapBean> toMapBean(Map<String, Object> objectMap) {
        List<MapBean> maps = new ArrayList<MapBean>();
        for (String key : objectMap.keySet()) {
            maps.add(new MapBean(key, objectMap.get(key)));
        }
        return maps;
    }

    public static String toString(Map<String, Object> objectMap) {
        List<MapBean> maps = new ArrayList<>();
        for (String key : objectMap.keySet()) {
            maps.add(new MapBean(key, objectMap.get(key)));
        }
        return JsonUtils.objectToJson(objectMap);
    }

    /**
     * Create a map from passed nameX, valueX parameters
     *
     * @return The resulting Map
     */
    public static <V, V1 extends V> Map<String, V> toMap(String name1, V1 value1) {
        return populateMap(new HashMap<>(), name1, value1);
    }

    /**
     * Create a map from passed nameX, valueX parameters
     *
     * @return The resulting Map
     */
    public static <V, V1 extends V, V2 extends V> Map<String, V> toMap(String name1, V1 value1, String name2, V2 value2) {
        return populateMap(new HashMap<String, V>(), name1, value1, name2, value2);
    }

    /**
     * Create a map from passed nameX, valueX parameters
     *
     * @return The resulting Map
     */
    public static <V, V1 extends V, V2 extends V, V3 extends V> Map<String, V> toMap(String name1, V1 value1, String name2, V2 value2, String name3, V3 value3) {
        return populateMap(new HashMap<String, V>(), name1, value1, name2, value2, name3, value3);
    }

    /**
     * Create a map from passed nameX, valueX parameters
     *
     * @return The resulting Map
     */
    public static <V, V1 extends V, V2 extends V, V3 extends V, V4 extends V> Map<String, V> toMap(String name1, V1 value1, String name2, V2 value2, String name3, V3 value3, String name4, V4 value4) {
        return populateMap(new HashMap<String, V>(), name1, value1, name2, value2, name3, value3, name4, value4);
    }

    /**
     * Create a map from passed nameX, valueX parameters
     *
     * @return The resulting Map
     */
    public static <V, V1 extends V, V2 extends V, V3 extends V, V4 extends V, V5 extends V> Map<String, V> toMap(String name1, V1 value1, String name2, V2 value2, String name3, V3 value3, String name4, V4 value4, String name5, V5 value5) {
        return populateMap(new HashMap<String, V>(), name1, value1, name2, value2, name3, value3, name4, value4, name5, value5);
    }

    /**
     * Create a map from passed nameX, valueX parameters
     *
     * @return The resulting Map
     */
    public static <V, V1 extends V, V2 extends V, V3 extends V, V4 extends V, V5 extends V, V6 extends V> Map<String, V> toMap(String name1, V1 value1, String name2, V2 value2, String name3, V3 value3, String name4, V4 value4, String name5, V5 value5, String name6, V6 value6) {
        return populateMap(new HashMap<String, V>(), name1, value1, name2, value2, name3, value3, name4, value4, name5, value5, name6, value6);
    }

    private static <K, V> Map<String, V> populateMap(Map<String, V> map, Object... data) {
        for (int i = 0; i < data.length; ) {
            map.put((String) data[i++], (V) data[i++]);
        }
        return map;
    }
}
