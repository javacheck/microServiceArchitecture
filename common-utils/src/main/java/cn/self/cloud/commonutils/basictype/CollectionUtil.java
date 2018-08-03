package cn.self.cloud.commonutils.basictype;

import java.util.*;

/**
 * 集合(List,Map,Set)辅助类。
 */
public final class CollectionUtil {

    /**
     * 功能：判断集合是否为空。如果传入的值为null或者集合不包含元素都认为为空。
     * @param collection 集合
     * @return boolean 为空返回true，否则返回false。
     */
    public static boolean isEmpty(Collection collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * 功能：判断数组是不是空(数组值为null或者length==0都认为为空)
     * @param array 数组
     * @return boolean 空返回true，否则返回false。
     */
    public static <T> boolean isEmpty(T[] array) {
        return (array == null || array.length == 0);
    }

    /**
     * 功能：判断Map是否为空。如果传入的值为null或者集合不包含元素都认为为空。
     * @param map Map
     * @return boolean 为空返回true，否则返回false。
     */
    public static boolean isEmpty(Map map) {
        return (map == null || map.isEmpty());
    }

    /**
     * 功能：从List中随机取出一个元素。
     * @return T List的一个元素
     */
    public static <T> T randomOne(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        return list.get(MathUtil.randomNumber(0, list.size()));
    }

    /**
     * 功能：从数组中随机取出一个元素。
     * @param objs 源数组
     * @return T 数组的一个元素
     */
    public static <T> T randomOne(T[] objs) {
        if (isEmpty(objs)) {
            return null;
        }
        return objs[MathUtil.randomNumber(0, objs.length)];
    }

    /**
     * 功能：数组中是否存在这个元素。(数组为空则默认返回false)
     * @param objArr  数组
     * @param compare 元素
     * @return 存在返回true，否则返回false。
     */
    public static <T> boolean arrayContain(T[] objArr, T compare) {
        if (isEmpty(objArr)) {
            return false;
        }
        for (T obj : objArr) {
            if( ObjectUtils.equals(obj,compare)){
                return true;
            }
        }
        return false;
    }

    /**
     * 功能：向list中添加数组。
     * @param list  List
     * @param array 数组
     */
    public static <T> void addArrayToList(List<T> list, T[] array) {
        if (isEmpty(list)) {
            return;
        }
        for (T t : array) {
            list.add(t);
        }
    }

    /**
     * 功能：将数组进行反转，倒置。
     * @param objs 源数组
     * @return T[] 反转后的数组
     */
    public static <T> T[] reverseArray(T[] objs) {
        if (isEmpty(objs)) {
            return null;
        }

        // 复制一个数组
        T[] res = (T[]) java.lang.reflect.Array.newInstance(objs[0].getClass(), objs.length);

        //倒序将源数组的序号赋给复制数组
        int k = 0;
        int length = objs.length -1 ;
        for (int i = length; i >= 0; i--) {
            res[k++] = objs[i];
        }
        return res;
    }

    /**
     * 功能：将数组转为list。
     * @param objs 源数组
     * @return List
     */
    public static <T> List<T> arrayToList(T[] objs) {
        if (isEmpty(objs)) {
            return null;
        }
        List<T> list = new LinkedList<T>();
        for (T obj : objs) {
            list.add(obj);
        }
        return list;
    }

    /**
     * 功能：将list转为数组。(前提是list中的元素是同一类型)
     * @param list 源list
     * @return T[]
     */
    public static <T> T[] listToArray(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        T[] objs = (T[]) java.lang.reflect.Array.newInstance(list.get(0).getClass(), list.size());
        int i = 0; //数组下标。
        for (T obj : list) {
            objs[i++] = obj;
        }
        return objs;
    }

    /**
     * 功能：将一个字符串数组的内容全部添加到另外一个数组中，并返回一个新数组。
     * @param array1 第一个数组
     * @param array2 第二个数组
     * @return T[] 拼接后的新数组
     */
    public static <T> T[] concatenateArrays(T[] array1, T[] array2) {
        if (isEmpty(array1)) {
            return array2;
        }
        if (isEmpty(array2)) {
            return array1;
        }
        T[] resArray = (T[]) java.lang.reflect.Array.newInstance(array1[0].getClass(), array1.length + array2.length);
        System.arraycopy(array1, 0, resArray, 0, array1.length);
        System.arraycopy(array2, 0, resArray, array1.length, array2.length);
        return resArray;
    }

    /**
     * 功能：将一个object添加到一个数组中，并返回一个新数组。
     * @return T[] 返回的新数组
     */
    public static <T> T[] addObjectToArray(T[] array, T obj) {
        //结果数组
        T[] resArray;
        // 原数组为空时。
        if (isEmpty(array)) {
            resArray = (T[]) java.lang.reflect.Array.newInstance(obj.getClass(), 1);
            resArray[0] = obj;
            return resArray;
        }
        //原数组不为空时。
        resArray = (T[]) java.lang.reflect.Array.newInstance(array[0].getClass(), array.length + 1);
        System.arraycopy(array, 0, resArray, 0, array.length);
        resArray[array.length] = obj;
        return resArray;
    }

    // 单元测试
    public static void main(String[] args) {
        System.out.println("检查集合是否为空：" + isEmpty(new ArrayList()));
        System.out.println("检查数组是否为空：" + isEmpty(new Object[]{}));
        System.out.println("检查Map是否为空：" + isEmpty(new HashMap()));
        List list = new ArrayList();
        list.add(12);
        list.add(9);
        list.add(2);
        System.out.println("随机取出list中的值：" + randomOne(list));
        Object[] obj = new Object[]{1,5,8,0,10,51,28,19};
        System.out.println("随机取出数组中的值：" + randomOne(obj));
        System.out.println("检查数组中是否存在某个值：" + arrayContain(obj,11));
        addArrayToList(list,obj);
        System.out.println("增加后的集合是：" + list);
        System.out.println("反转数组：" + reverseArray(obj));
        System.out.println("转换后的集合是：" + arrayToList(obj));
        System.out.println("转换后的数组是：" + listToArray(list));
        Object[] objects = new Object[]{3,2,-1};
        System.out.println("组合后的数组是：" + concatenateArrays(obj,objects));
        System.out.println("添加后的数组：" + addObjectToArray(obj,555));
    }
}
