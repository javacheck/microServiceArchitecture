package cn.self.cloud.commonutils.reflec;

import cn.self.cloud.commonutils.validate.ValidateUtils;
import jodd.util.ReflectUtil;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ClassUtil {
    public static <T> Map<String, Object> getFields(Object object) {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = ReflectUtil.getSupportedFields(object.getClass());
        for (Field field : fields) {
            field.setAccessible(true);
            Object obj;
            try {
                obj = field.get(object);
                map.put(field.getName(), obj);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static <T> Map<String, Object> getFields(Object object, boolean nullable) {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = ReflectUtil.getSupportedFields(object.getClass());
        for (Field field : fields) {
            field.setAccessible(true);
            Object obj;
            try {
                obj = field.get(object);
                if (nullable == true) {
                    map.put(field.getName(), obj);
                } else {
                    if (ValidateUtils.isNotEmpty(obj)) {
                        map.put(field.getName(), obj);
                    }
                }

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
