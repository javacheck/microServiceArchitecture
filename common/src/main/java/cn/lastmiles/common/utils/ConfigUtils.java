package cn.lastmiles.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigUtils {
	private static Properties props = new Properties();

	public static void clearAndLoad(InputStream in) {
		try {
			props.clear();
			props.load(in);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static void clearAndLoad(Properties pro){
		props.clear();
		props = pro;
	}

	public static void load(InputStream in) {
		try {
			props.load(in);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static String getProperty(String key) {
		return props.getProperty(key);
	}

	public static Integer getInteger(String key) {
		return Integer.valueOf(props.getProperty(key));
	}
	
	public static Boolean getBoolean(String key){
		return Boolean.valueOf(props.getProperty(key));
	}
}
