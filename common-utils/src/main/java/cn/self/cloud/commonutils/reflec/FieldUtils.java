package cn.self.cloud.commonutils.reflec;

import java.lang.reflect.Field;

import jodd.util.ReflectUtil;

public final class FieldUtils {
    public static Field[] getDeclaredFields(Class<?> cl, boolean includeParent) {
        if (includeParent) {
            return ReflectUtil.getSupportedFields(cl);
        } else {
            return cl.getDeclaredFields();
        }
    }
}