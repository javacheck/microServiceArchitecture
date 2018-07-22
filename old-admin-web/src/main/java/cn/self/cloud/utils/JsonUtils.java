package cn.self.cloud.utils;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtils {
	/**
	 * 对象转成json字符
	 * 
	 * @param object
	 * @return
	 */
	public static String objectToJson(Object object) {
		try {
			return new ObjectMapper().writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * json字符串转成map
	 * @param json
	 * @return
	 */
	public static Map<String, Object> jsonToMap(String json) {
		try {
			return new ObjectMapper().readValue(json,
					new TypeReference<Map<String, Object>>() {
					});
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * json字符串转成bean
	 * @param json
	 * @param cl
	 * @return
	 */
	public static <T> T jsonToBean(String json, Class<T> cl) {
		try {
			return new ObjectMapper().readValue(json, cl);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
