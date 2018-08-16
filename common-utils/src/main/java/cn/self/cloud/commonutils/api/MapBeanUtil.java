package cn.self.cloud.commonutils.api;

import cn.self.cloud.commonutils.json.JsonUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapBeanUtil {
	public static List<MapBean> toMapBean(Map<String, Object> objs){
		List<MapBean> maps = new ArrayList<MapBean> ();
		for (String key : objs.keySet()) {
			maps.add(new MapBean(key, objs.get(key)));
		}
		return maps;
	}
	public static String toString(Map<String, Object> objs){
		List<MapBean> maps = new ArrayList<MapBean> ();
		for (String key : objs.keySet()) {
			maps.add(new MapBean(key, objs.get(key)));
		}
		return JsonUtils.objectToJson(objs);
	}
}
