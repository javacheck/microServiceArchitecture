package cn.self.cloud.commonutils.zother;

import org.apache.commons.collections.Predicate;

import java.lang.reflect.Method;
import java.math.BigDecimal;

/**
 * Created by Yumei on 2017/1/12.
 */
public class MyPredicate implements Predicate {
    private String property;

    private Object value;

    public MyPredicate(String property, Object value) {
        this.property = property;
        this.value = value;
    }

    public boolean evaluate(Object object) {

        try {
            Object beanValue;
            beanValue = getFieldValueByName(property,object);
            if (beanValue == null) {
                return false;
            }
            if (!value.getClass().equals(beanValue.getClass())) {
                throw new RuntimeException("value.class!=beanValue.class");
            }
            return myCompare(beanValue, value);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }

    }

    private boolean myCompare(Object value, Object beanValue) {
        if (beanValue.getClass().equals(Integer.class)) {
            if (((Integer) beanValue).equals(value)) {
                return true;
            }
        }
        if (beanValue.getClass().equals(BigDecimal.class)) {
            if (((BigDecimal) beanValue).compareTo((BigDecimal) value) == 0) {
                return true;
            }
        }
        if (beanValue.getClass().equals(String.class)) {
            if (beanValue.toString().equals(value.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据属性名获取属性值
     * */
    private Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
