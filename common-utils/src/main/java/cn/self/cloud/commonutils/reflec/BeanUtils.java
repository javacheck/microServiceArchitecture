package cn.self.cloud.commonutils.reflec;

import cn.self.cloud.commonutils.basictype.StringUtils;
import cn.self.cloud.commonutils.validate.ValidateUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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


// 下面的是从其他地方弄过来的
    /**
     * 从指定的bean列表中，取出指定的字段，生成由该字段值组成的ArrayList
     *
     * @param cls
     * @param beanList
     * @param propertyName
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> ArrayList<T> fetchPropertyAsList(Class<T> cls,
                                                       Collection<? extends Object> beanList,
                                                       String propertyName) throws Exception {
        ArrayList<T> list = new ArrayList<T>();
        if (beanList == null || beanList.isEmpty())
            return list;
        if (StringUtils.isEmpty(propertyName))
            return list;
        for (Object obj : beanList) {
            Object fvalue = null;
            Field f = null;
            Class objCls = obj.getClass();
            boolean fetched = false;
            try {
                f = objCls.getField(propertyName);
                fvalue = f.get(obj);
                fetched = true;
            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
            }
            if (!fetched) {
                String methodName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
                try {
                    Method m = objCls.getMethod(methodName);
                    fvalue = m.invoke(obj);
                    fetched = true;
                }catch (NoSuchMethodException e){
                    e.printStackTrace();
                }
            }
            if(fetched) {
                try {//先尝试强制转换，
                    T v = (T) fvalue;
                    list.add(v);
                } catch (Exception ex) {//如果异常，就用构造函数来试试
                    Constructor<T> constructor = cls.getConstructor(Object.class);
                    if (constructor.isAccessible()) {
                        T v = constructor.newInstance(fvalue);
                        list.add(v);
                    }
                }
            }
        }
        return list;
    }

    public static   <T> Set<T> fetchPropertyAsSet(Class<T> cls, List<? extends Object> beanList, String propertyName)   {
        Set<T> set = new HashSet<T>();
        if (beanList == null || beanList.isEmpty())
            return set;
        if (StringUtils.isEmpty(propertyName))
            return set;
        for (Object obj : beanList) {
            Object fvalue = null;
            Field f = null;
            Class objCls = obj.getClass();
            boolean fetched = false;
            try {
                f = objCls.getField(propertyName);
                fvalue = f.get(obj);
                fetched = true;
            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
            } catch (IllegalAccessException e) {
//                e.printStackTrace();
            }
            if (!fetched) {
                String methodName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
                try {
                    Method m = objCls.getMethod(methodName);
                    fvalue = m.invoke(obj);
                    fetched = true;
                }catch (NoSuchMethodException e){
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
//                    e.printStackTrace();
                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
                }
            }
            if(fetched) {
                try {//先尝试强制转换，
                    T v = (T) fvalue;
                    set.add(v);
                } catch (Exception ex) {//如果异常，就用构造函数来试试
                    Constructor<T> constructor = null;
                    try {
                        constructor = cls.getConstructor(Object.class);
                        if (constructor.isAccessible()) {
                            T v = constructor.newInstance(fvalue);
                            if(v!=null)
                                set.add(v);
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return set;
    }
}
