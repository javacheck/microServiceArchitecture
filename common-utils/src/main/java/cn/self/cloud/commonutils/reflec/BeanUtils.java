package cn.self.cloud.commonutils.reflec;

import cn.self.cloud.commonutils.validate.ValidateUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

public abstract class BeanUtils {
    public static void copyProperties(Object source, Object target) {
        org.springframework.beans.BeanUtils.copyProperties(source, target);
    }

    public static <T> Map<String, Object> toMap(Object obj) {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(obj));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return map;
    }

    public static <T> T toBean(Class<T> cl, Map<String, Object> map) {
        if (ValidateUtils.isEmpty(map)) {
            return null;
        }
        T instance;
        try {
            instance = cl.newInstance();
            Field[] fields = FieldUtils.getDeclaredFields(cl, true);
            Set<String> keys = map.keySet();

            for (String key : keys) {
                for (Field field : fields) {
                    field.setAccessible(true);
                    String fieldName = field.getName();

                    int index = key.indexOf(".");
                    if (index != -1) {
                        String beanName = key.substring(0, index);
                        String attr = key.substring(index + 1);
                        if (beanName.equalsIgnoreCase(fieldName)) {
                            Object _cl = field.get(instance);
                            if (ValidateUtils.isEmpty(_cl)) {
                                _cl = field.getType().newInstance();
                            }
                            Field[] _fields = field.getType().getDeclaredFields();

                            for (Field f : _fields) {
                                f.setAccessible(true);
                                String fn = f.getName();
                                if (fn.equalsIgnoreCase(attr) || fn.replaceAll("_", "").equalsIgnoreCase(attr)) {
                                    Object value = map.get(key);

                                    if (ValidateUtils.isNotEmpty(value)) {
                                        setFieldValue(_cl, f, value);
                                    }

                                    field.set(instance, _cl);
                                }
                            }
                        }
                    } else {
                        if (key.equalsIgnoreCase(fieldName) || key.replaceAll("_", "").equalsIgnoreCase(fieldName)) {
                            Object value = map.get(key);
                            if (ValidateUtils.isNotEmpty(value)) {
                                setFieldValue(instance, field, value);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return instance;
    }

    private static void setFieldValue(Object instance, Field field, Object value) throws Exception {
        if (long.class.equals(field.getType()) || Long.class.equals(field.getType())) {
            field.set(instance, Long.valueOf(value.toString()));
        } else if (int.class.equals(field.getType()) || Integer.class.equals(field.getType())) {
            field.set(instance, Double.valueOf(value.toString()).intValue());
        } else if (short.class.equals(field.getType()) || Short.class.equals(field.getType())) {
            field.set(instance, Short.valueOf(value.toString()));
        } else if (byte.class.equals(field.getType()) || Byte.class.equals(field.getType())) {
            field.set(instance, Byte.valueOf(value.toString()));
        } else if (double.class.equals(field.getType()) || Double.class.equals(field.getType())) {
            field.set(instance, Double.valueOf(value.toString()));
        } else if (float.class.equals(field.getType()) || Float.class.equals(field.getType())) {
            field.set(instance, Float.valueOf(value.toString()));
        } else if (field.getType().isEnum()) {
            Object[] _objs = field.getType().getEnumConstants();
            field.set(instance, _objs[Integer.valueOf(value.toString())]);
        } else if (boolean.class.equals(field.getType()) || Boolean.class.equals(field.getType())) {
            if (value instanceof Number) {
                if (Integer.valueOf(value.toString()).intValue() == 0) {
                    field.set(instance, false);
                } else {
                    field.set(instance, true);
                }
            } else {
                field.set(instance, value);
            }
        } else if (BigDecimal.class.equals(field.getType())) {
            field.set(instance, new BigDecimal(value.toString()));
        } else {
            field.set(instance, value);
        }
    }

    public static <T> List<T> toList(Class<T> cl, List<Map<String, Object>> list) {
        List<T> ret = new ArrayList<T>();
        for (Map<String, Object> map : list) {
            ret.add(toBean(cl, map));
        }
        return ret;
    }

    public static String toRequestPath(Object obj) {
        try {
            StringBuilder builder = new StringBuilder();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                Object value = f.get(obj);
                if (ValidateUtils.isNotEmpty(value)) {
                    if (builder.length() > 0) {
                        builder.append("&");
                    }
                    if (f.getType().equals(Date.class)) {
                        builder.append(f.getName() + "=" + ((Date) value).getTime());
                    } else {
                        builder.append(f.getName() + "=" + value);
                    }
                }
            }
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String toRequestPath(Map<String, Object> map) {
        try {
            StringBuilder builder = new StringBuilder();
            Set<String> keys = map.keySet();
            for (String f : keys) {
                if (builder.length() > 0) {
                    builder.append("&");
                }
                Object obj = map.get(f);
                if (obj.getClass().equals(Date.class)) {
                    builder.append(f + "=" + ((Date) obj).getTime());
                } else {
                    builder.append(f + "=" + obj);
                }
            }
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
