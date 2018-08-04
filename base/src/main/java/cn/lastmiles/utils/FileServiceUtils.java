package cn.lastmiles.utils;

import cn.lastmiles.common.utils.ConfigUtils;
import cn.lastmiles.common.utils.StringUtils;

public class FileServiceUtils {
	public static String baseUrl = "";

	public static String getFileUrl(String id) {
		String picUrl = ConfigUtils.getProperty("picUrl");
		if (StringUtils.isBlank(id) || StringUtils.isBlank(picUrl)) {
			return "";
		}
		
		if (id.indexOf(picUrl)!=-1) {
			return id;
		}
		if (StringUtils.isBlank(picUrl)) {
			return baseUrl + "file/image/" + id;
		} else {
			if (picUrl.startsWith("http")) {
				return picUrl + id;
			} else {
				return baseUrl + picUrl + id;
			}
		}
	}
}
