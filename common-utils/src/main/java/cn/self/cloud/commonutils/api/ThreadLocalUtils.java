package cn.self.cloud.commonutils.api;

public class ThreadLocalUtils {
    private final static ThreadLocal<Object> threadLocal = new ThreadLocal<>();

    public static void setObject(Object object) {
        threadLocal.set(object);
    }

    public static Object getObject() {
        return threadLocal.get();
    }
}