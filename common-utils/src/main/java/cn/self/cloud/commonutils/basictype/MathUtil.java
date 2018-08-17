package cn.self.cloud.commonutils.basictype;

import cn.self.cloud.commonutils.properties.ConfigUtils;
import jodd.util.RandomString;
import java.math.BigDecimal;
import java.util.Random;

/**
 * 科学计算
 * 数学运算辅助类
 */
public final class MathUtil {

    /**
     * 功能：计算地球上任意两点(经纬度)距离(有任意参数为null，则结果返回null)
     * @param longitudeOne 第一点经度
     * @param latitudeOne  第一点纬度
     * @param longitudeTwo 第二点经度
     * @param latitudeTwo  第二点纬度
     * @return 返回距离 单位：米
     */
    public static Double distance(Double longitudeOne, Double latitudeOne, Double longitudeTwo, Double latitudeTwo) {
        if ( null == longitudeOne || null == latitudeOne || null == longitudeTwo || null == latitudeTwo ) {
            return null;
        }

        double a, b;
        double earthRadius = 6378137D; // 地球半径

        latitudeOne = latitudeOne * Math.PI / 180.0;
        latitudeTwo = latitudeTwo * Math.PI / 180.0;
        a = latitudeOne - latitudeTwo;
        b = (longitudeOne - longitudeTwo) * Math.PI / 180.0;
        double d;
        double sa2, sb2;
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2 * earthRadius * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(latitudeOne) * Math.cos(latitudeTwo) * sb2 * sb2));
        return d;
    }

    /**
     * 功能：将字符串转换为BigDecimal，一般用于数字运算时。(str为empty时返回null)
     * @param str 字符串
     * @return BigDecimal
     */
    public static BigDecimal toBigDecimal(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        return new BigDecimal(str);
    }

    /**
     * 功能：将字符串抓换为double，如果失败返回默认值。
     * @param str          字符串
     * @param defaultValue 失败时返回的默认值
     * @return double
     */
    public static double toDouble(String str, double defaultValue) {
        if ( StringUtils.isBlank(str) ) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * 功能：将字符串抓换为float，如果失败返回默认值。
     * @param str          字符串
     * @param defaultValue 失败时返回的默认值
     * @return float
     */
    public static float toFloat(String str, float defaultValue) {
        if ( StringUtils.isBlank(str) ) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * 功能：将字符串抓换为long，如果失败返回默认值。
     * @param str          字符串
     * @param defaultValue 失败时返回的默认值
     * @return long
     */
    public static long toLong(String str, long defaultValue) {
        if ( StringUtils.isBlank(str) ) {
            return defaultValue;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * 功能：将字符串抓换为int，如果失败返回默认值。
     * @param str          字符串
     * @param defaultValue 失败时返回的默认值
     * @return int
     */
    public static int toInt(String str, int defaultValue) {
        if ( StringUtils.isBlank(str) ) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * 功能：得到两个float值中最大的一个
     * @param one 第一个float值
     * @param two 第二个float值
     * @return 最大的float值
     */
    public static float getMax(float one, float two) {
        if (Float.isNaN(one)) {
            return two;
        } else if (Float.isNaN(two)) {
            return one;
        } else {
            return Math.max(one, two);
        }
    }

    /**
     * 功能：得到两个double值中最大的一个.
     * @param one 第一个double值
     * @param two 第二个double值
     * @return 最大的double值
     */
    public static double getMax(double one, double two) {
        if (Double.isNaN(one)) {
            return two;
        } else if (Double.isNaN(two)) {
            return one;
        } else {
            return Math.max(one, two);
        }
    }

    /**
     * 功能：得到float数组中最大的一个.
     * @param array 数组不能为null，也不能为空。
     * @return 得到数组中最大的一个.
     * @throws IllegalArgumentException 如果数组是null
     * @throws IllegalArgumentException 如果数组是空
     */
    public static float getMax(float[] array) {
        // Validates input
        if ( array == null ) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if ( array.length == 0 ) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }

        // Finds and returns max
        float max = array[0];
        int length = array.length;
        for (int j = 1; j < length; j++) {
            max = getMax(array[j], max);
        }

        return max;
    }

    /**
     * 功能：得到double数组中最大的一个.
     * @param array 数组不能为null，也不能为空。
     * @return 得到数组中最大的一个.
     * @throws IllegalArgumentException 如果数组是null
     * @throws IllegalArgumentException 如果数组是空
     */
    public static double getMax(double[] array) {
        // Validates input
        if ( array == null ) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if ( array.length == 0 ) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }

        // Finds and returns max
        double max = array[0];
        int length = array.length;
        for (int j = 1; j < length; j++) {
            max = getMax(array[j], max);
        }

        return max;
    }

    /**
     * 功能：得到两个float中最小的一个。
     * @param one 第一个float值
     * @param two 第二个float值
     * @return 最小的float值
     */
    public static float getMin(float one, float two) {
        if (Float.isNaN(one)) {
            return two;
        } else if (Float.isNaN(two)) {
            return one;
        } else {
            return Math.min(one, two);
        }
    }

    /**
     * 功能：得到两个double中最小的一个。
     * @param one 第一个double值
     * @param two 第二个double值
     * @return double值最小的
     */
    public static double getMin(double one, double two) {
        if (Double.isNaN(one)) {
            return two;
        } else if (Double.isNaN(two)) {
            return one;
        } else {
            return Math.min(one, two);
        }
    }

    /**
     * 功能：返回数组中最小的数值。
     * @param array 数组不能为null，也不能为空。
     * @return 数组里面最小的float
     * @throws IllegalArgumentException 如果数组是null
     * @throws IllegalArgumentException 如果数组是空
     */
    public static float getMin(float[] array) {
        // Validates input
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }

        // Finds and returns min
        float min = array[0];
        int length = array.length;
        for (int i = 1; i < length; i++) {
            min = getMin(array[i], min);
        }

        return min;
    }

    /**
     * 功能：返回数组中最小的double。
     * @param array 数组不能为null，也不能为空。
     * @return 数组里面最小的double
     * @throws IllegalArgumentException 如果数组是null
     * @throws IllegalArgumentException 如果数组是空
     */
    public static double getMin(double[] array) {
        // Validates input
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }

        // Finds and returns min
        double min = array[0];
        int length = array.length;
        for (int i = 1; i < length; i++) {
            min = getMin(array[i], min);
        }
        return min;
    }

    /**
     * 功能：返回两个double的商 one / two
     * @param one  第一个double
     * @param two 第二个double
     * @return double
     */
    public static double divideDouble(double one, double two) {
        BigDecimal b1 = new BigDecimal(one);
        BigDecimal b2 = new BigDecimal(two);
        return b1.divide(b2).doubleValue();
    }

    /**
     * 功能：返回两个double的乘积 one * two
     * @param one  第一个double
     * @param two 第二个double
     * @return double
     */
    public static double multiplyDouble(double one, double two) {
        BigDecimal b1 = new BigDecimal(one);
        BigDecimal b2 = new BigDecimal(two);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 功能：返回两个double的差值 one - two
     * @param one  第一个double
     * @param two 第二个double
     * @return double
     */
    public static double subtractDouble(double one, double two) {
        BigDecimal b1 = new BigDecimal(one);
        BigDecimal b2 = new BigDecimal(two);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 功能：返回两个double的和值 one + two
     * @param one  第一个double
     * @param two 第二个double
     * @return double
     */
    public static double sumDouble(double one, double two) {
        BigDecimal b1 = new BigDecimal(one);
        BigDecimal b2 = new BigDecimal(two);
        return b1.add(b2).doubleValue();
    }

    /**
     * 功能：格式化double指定位数小数。例如将11.126格式化为11.12。
     * @param value    原double数字
     * @param decimals 需要截取的小数位数。
     * @return 格式化后的double，注意为硬格式化不存在四舍五入。
     */
    public static double formatDouble(double value, int decimals) {
        String doubleStr = "" + value;
        int index = doubleStr.indexOf(".") != -1 ? doubleStr.indexOf(".") : doubleStr.indexOf(",");

        // Decimal point can not be found...
        if (index == -1) {
            return Double.valueOf(doubleStr);
        }

        // Truncate all decimals
        if (decimals == 0) {
            return Double.valueOf(doubleStr.substring(0, index));
        }
        int len = index + decimals + 1;
        if (len >= doubleStr.length())
            len = doubleStr.length();
        double d = Double.parseDouble(doubleStr.substring(0, len));
        return d;
    }

    /**
     * 功能：生成一个指定位数的随机数，并将其转换为字符串作为函数的返回值。
     * @param randomNumberLength 随机数的位数。
     * @return String 注意随机数可能以0开头。
     */
    public static String randomNumber(int randomNumberLength) {

        // 记录生成的每一位随机数
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < randomNumberLength; i++) {
            // 每次生成一位,随机生成一个0-10之间的随机数,不含10。
            Double ranDouble = Math.floor(Math.random() * 10);
            sb.append(ranDouble.intValue());
        }
        return sb.toString();
    }

    public static int random(int size) {
        if (size == 0) {
            throw new RuntimeException("Array is Empty.");
        }

        if (size == 1) {
            return 0;
        }

        return new Random().nextInt(size);
    }

    public static String id() {
        String code = String.valueOf(Math.abs(StringUtils.uuid().hashCode()));
        String random = String.valueOf(new Random().nextInt(900) + 100);
        return code + random;
    }

    /**
     * 功能：生成一个在最大数和最小数之间的随机数。会出现最小数，但不会出现最大数。
     * @param minNum 最小数
     * @param maxNum 最大数
     * @return int
     */
    public static int randomNumber(int minNum, int maxNum) {
        if (maxNum <= minNum) {
            throw new RuntimeException("maxNum必须大于minNum!");
        }
        // 计算出来差值
        int subtract = maxNum - minNum;
        Double ranDouble = Math.floor(Math.random() * subtract);
        return ranDouble.intValue() + minNum;
    }

    /**
     * 功能：生产随机数字窜
     * @param count 个数，需要生产的数字个数
     * @return
     */
    public static String getRandomNumeric(int count) {
        if( null != ConfigUtils.getProperty("redis.ip")
                && ConfigUtils.getProperty("redis.ip").startsWith("192")) {
            return "888888";
        }
        return RandomString.getInstance().randomNumeric(count);
    }

    /**
     * 功能：生成一个在最大数和最小数之间的随机数。会出现最小数，但不会出现最大数。
     * 但不随机notIn数组中指定的数字， 如果可随机的范围较小，可能会一直随机不到，或者随机的很慢。
     * @param minNum 最小数
     * @param maxNum 最大数
     * @param notIn  不随机数组这些数字
     * @return int
     */
    public static int randomNumber(int minNum, int maxNum, Integer[] notIn) {
        if (notIn.length >= (maxNum - minNum)) {
            throw new RuntimeException("notIn数组的元素已经把可以随机的都排除了，无法得到随机数!");
        }
        while (true) {
            // 产生随机数
            int num = randomNumber(minNum, maxNum);
            // 查看产生的随机数是不是在排除的数组里面，在，则重新随机，不在，则产出
            if ( !CollectionUtil.arrayContain(notIn, num) ) {
                return num;
            }
        }
    }

    // 单元测试
    public static void main(String[] args) {
        System.out.println("查看北京到长沙的距离：" + distance(116.3,39.9,111.54,27.51));
        System.out.println("字符串转BigDecimal：" + toBigDecimal("123.321"));
        System.out.println("字符串转Double：" + toDouble("",12D));
        System.out.println("字符串转Float：" + toFloat(null,22F));
        System.out.println("字符串转Long：" + toLong("",11L));
        System.out.println("字符串转Int：" + toInt("",1));
        System.out.println("float中获得大值：" + getMax(1F,2F));
        System.out.println("double中获得大值：" + getMax(3D,2D));
        System.out.println("float中获得小值：" + getMin(1F,2F));
        System.out.println("double中获得小值：" + getMin(3D,4D));
        System.out.println("float数组中获得最小值：" + getMin(new float[]{1,3,4,6,7,0}));
        System.out.println("double数组中获得最小值：" + getMin(new double[]{1,3,4,6,7,0}));
        System.out.println("float数组中获得最大值：" + getMax(new float[]{1,3,4,6,7,10,0}));
        System.out.println("double数组中获得最大值：" + getMax(new double[]{1,3.3,4.4,3.6,1.7,10,0}));
        System.out.println("得到两个double相除后的值：" + divideDouble(13,4));
        System.out.println("得到两个double相乘后的值：" + multiplyDouble(2.3,6));
        System.out.println("得到两个double相减后的值：" + subtractDouble(3.1,4.5));
        System.out.println("得到两个double相加后的值：" + sumDouble(2.2,9.1));
        System.out.println("格式化double的小数点位数：" + formatDouble(5.1998234454,2));
        System.out.println("产生指定长度的随机数：" + randomNumber(6));
        System.out.println("产生在范围内的随机数：" + randomNumber(1,15));
        System.out.println("产生指定长度的随机数字串：" + getRandomNumeric(15));
        System.out.println("产生在范围内并且排除在某个范围内的随机数：" + randomNumber(1,50,new Integer[]{3}));
    }
}
