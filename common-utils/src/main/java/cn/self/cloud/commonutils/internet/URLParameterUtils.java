package cn.self.cloud.commonutils.internet;

import cn.self.cloud.commonutils.basictype.StringUtils;

import java.lang.reflect.Constructor;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cp on 2016/7/21.
 */
public class URLParameterUtils {
    public static Map<String, String> parseQueryStringToMap(String queryString) {
        if (StringUtils.isEmpty(queryString))
            return null;
        Map<String, String> map = new HashMap<String, String>();
        String[] arr = StringUtils.split(queryString, "&");
        if (arr != null && arr.length > 0)
            for (String str : arr) {
                int index = str.indexOf('=');
                if (index > 0) {
                    String key = str.substring(0, index);
                    String value = str.substring(index + 1);
                    value = value.trim();
                    if (StringUtils.isNotEmpty(value)) {
                        try {
                            map.put(key, URLDecoder.decode(value, "utf-8"));
                        } catch (Exception ex) {
                        }
                    } else {
                        map.put(key, value);
                    }
                }
            }
        return map;
    }

    public static String changeAndTrimString(String str, String fromChar, String toChar) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        String[] arr = StringUtils.split(str, fromChar);
        StringBuilder stringBuilder = new StringBuilder();
        boolean isFirst = true;
        for (String id : arr) {
            if (StringUtils.isNotEmpty(id)) {
                if (isFirst)
                    isFirst = false;
                else
                    stringBuilder.append(toChar);
                stringBuilder.append(id);
            }
        }
        return stringBuilder.toString();
    }

    public static <T> T fetchParameter(Class<T> cls, String parameter) throws Exception {
        if (StringUtils.isEmpty(parameter)) {
            if (cls == String.class)
                return (T) "";
            else if (cls == Integer.class || cls == Long.class || cls == Float.class || cls == Double.class) {
                Constructor<T> constructor = cls.getConstructor(String.class);
                return constructor.newInstance("0");
            }
            return null;
        } else {
            Constructor<T> constructor = cls.getConstructor(String.class);
            return constructor.newInstance(parameter);
        }
    }

    /***
     * 根据指定的参数以及url前缀组装url
     *
     * @param urlPreffix
     * @param parameters
     * @return
     */
    public static String concatUrlWithParameters(String urlPreffix, Map<String, Object> parameters) {
        if (StringUtils.isEmpty(urlPreffix))
            return null;
        if (parameters == null || parameters.isEmpty())
            return urlPreffix;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(urlPreffix);
        int index = urlPreffix.indexOf("?");
        if (index <= 0) {
            stringBuilder.append("?");
        } else {
            if (index != urlPreffix.length() - 1) {
                stringBuilder.append("&");
            }
        }
        boolean isFirst = true;
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            if (isFirst)
                isFirst = false;
            else
                stringBuilder.append("&");
            Object value = entry.getValue();
            stringBuilder.append(entry.getKey() + "=");
            if (value != null) {
                try {
                    String v = URLEncoder.encode(value.toString(), "UTF-8");
                    stringBuilder.append(v);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }

    /***
     * 将hashmap转换成queryString
     *
     * @param parameters
     * @return
     */
    public static String parseMapToQueryString(Map<String, Object> parameters) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean isFirst = true;
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            if (isFirst)
                isFirst = false;
            else
                stringBuilder.append("&");
            Object value = entry.getValue();
            stringBuilder.append(entry.getKey() + "=");
            if (value != null) {
                try {
                    String v = URLEncoder.encode(value.toString(), "UTF-8");
                    stringBuilder.append(v);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }
}
