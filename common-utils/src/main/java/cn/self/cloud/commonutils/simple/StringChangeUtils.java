package cn.self.cloud.commonutils.simple;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by cp on 2016/10/11.
 */
public class StringChangeUtils {
    /**
     * 将形如： .210051.18229767211. 的点分格式的用户名进行切分，获得处于第二节的手机号码
     *
     * @param username
     * @return
     */
    public static String getMobile(String username) {
        if (StringUtils.isEmpty(username))
            return null;
        String[] arr = StringUtils.split(username, ".");
        if (arr == null || arr.length < 2) {
            return null;
        } else {
            return arr[1];
        }
    }

    public static <T> ArrayList<T> splitStringToList(Class<T> cls, String str, String seperator) {
        ArrayList<T> list = new ArrayList<T>();
        if (StringUtils.isEmpty(str)) {
            return list;
        }
        if (StringUtils.isEmpty(seperator)) {
            seperator = ",";
        }
        String[] arr = StringUtils.split(str, seperator);
        if (arr == null || arr.length <= 0)
            return list;
        for (String s : arr) {
            try {
                Constructor<T> constructor = cls.getConstructor(String.class);
                T obj = constructor.newInstance(s);
                list.add(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 将指定的objList转换成以 seperator为分隔符的字符串
     *
     * @param objList
     * @param seperator
     * @return
     */
    public static String concatListAsString(Collection<? extends Object> objList, String seperator) {
        if (objList == null || objList.isEmpty())
            return "";
        String sep = seperator;

        if (StringUtils.isEmpty(sep))
            sep = ",";
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (Object obj : objList) {
            if (isFirst)
                isFirst = false;
            else
                sb.append(sep);
            sb.append(obj);
        }
        return sb.toString();
    }

    public static String getTelePhone(String phone1, String phone2, String phone3) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isNotEmpty(phone1) && (phone1.startsWith("0") || phone1.length() == 7 || phone1.indexOf("-") > 0)) {
            builder.append(phone1);
        }
        if (StringUtils.isNotEmpty(phone2) && (phone2.startsWith("0") || phone2.length() == 7 || phone2.indexOf("-") > 0)) {
            if (StringUtils.isNotEmpty(builder.toString())) {
                builder.append(",");
            }
            builder.append(phone2);
        }
        if (StringUtils.isNotEmpty(phone3) && (phone3.startsWith("0") || phone3.length() == 7 || phone3.indexOf("-") > 0)) {
            if (StringUtils.isNotEmpty(builder.toString())) {
                builder.append(",");
            }
            builder.append(phone3);
        }
        return builder.toString();
    }
}
