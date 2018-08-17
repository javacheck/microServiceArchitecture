package cn.self.cloud.commonutils.basictype;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精 确的浮点数运算，包括加减乘除和四舍五入。
 * 精准数字计算
 * BigDecimal----> JDK的解释：
 * 参数类型为double的构造方法的结果有一定的不可预知性。
 * 有人可能认为在Java中写入newBigDecimal(0.1)所创建的BigDecimal正好等于 0.1（非标度值 1，其标度为 1），
 * 但是它实际上等于0.1000000000000000055511151231257827021181583404541015625。这是因为0.1无法准确地表示为 double
 * （或者说对于该情况，不能表示为任何有限长度的二进制小数）。
 * 这样，传入到构造方法的值不会正好等于 0.1（虽然表面上等于该值）。
 *
 *
 * 另一方面，String 构造方法是完全可预知的：写入 newBigDecimal(“0.1”) 将创建一个 BigDecimal，它正好等于预期的 0.1。
 *
 * ************************************************
 * 因此，比较而言， 通常建议优先使用String构造方法。
 * ************************************************
 *
 * 当double必须用作BigDecimal的源时，请注意，此构造方法提供了一个准确转换；
 * 它不提供与以下操作相同的结果：先使用Double.toString(double)方法，然后使用BigDecimal(String)构造方法，
 * 将double转换为String。要获取该结果，请使用static valueOf(double)方法。
 * 参考网址：https://blog.csdn.net/lisongjia123/article/details/51232657
 */
public final class NumberUtils {

	// 这个类不能实例化
	private NumberUtils() {
	}

	/**
	 * 功能：两个数字进行相加操作
	 * @param a String 加数
	 * @param b String 被加数
	 * @return 和 两个参数的和
	 */
	public static Double add(String a, String b) {
		BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(a));
		BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(b));
		return bigDecimal1.add(bigDecimal2).doubleValue();
	}

	/**
	 * 功能：两个数字进行相加操作
	 * @param a Double 加数
	 * @param b Double 被加数
	 * @return 和 两个参数的和
	 */
	public static Double add(Double a, Double b) {
		BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(a));
		BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(b));
		return bigDecimal1.add(bigDecimal2).doubleValue();
	}

	/**
	 * 功能：多个数值累加。
	 * @param args 要累加的数 String类型
	 * @return Double 结果
	 */
	public static Double addAll(String... args) {
		if (args == null || args.length == 0){
			throw new RuntimeException("加法运算参数不能为空！");
		}
		BigDecimal result = new BigDecimal(args[0]);
		int length = args.length;
		for (int i = 1; i < length; i++) {
			result = result.add(new BigDecimal(args[i]));
		}
		return result.doubleValue();
	}

	/**
	 * 功能：多个数值累加。
	 * @param args 要累加的数 Number类型
	 * @return Double 结果
	 */
	public static Double addAll(Number... args) {
		if (args == null || args.length == 0){
			throw new RuntimeException("加法运算参数不能为空！");
		}
		BigDecimal result = new BigDecimal(String.valueOf(args[0]));
		int length = args.length;
		for (int i = 1; i < length; i++){
			result = result.add(new BigDecimal(String.valueOf(args[i])));
		}
		return result.doubleValue();
	}

	/**
	 * 功能：多个数值累加
	 * @param args 要累加的数 Object类型(String | Number)
	 * @return Double 结果
	 */
	public static Double addAll(Object... args) {
		if (args == null || args.length == 0){
			throw new RuntimeException("加法运算参数不能为空！");
		}
		BigDecimal result = new BigDecimal(0);
		int length = args.length;
		for (int i = 0; i < length; i++) {
			if (args[i] instanceof String || args[i] instanceof Number) {
				result = result.add(new BigDecimal(String.valueOf(args[i])));
			}
		}
		return result.doubleValue();
	}

	/**
	 * 功能：两个数字进行相减操作
	 * @param a String
	 * @param b String
	 * @return 差
	 */
	public static Double subtract(String a, String b) {
		BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(a));
		BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(b));
		return bigDecimal1.subtract(bigDecimal2).doubleValue();
	}

	/**
	 * 功能：两个数字进行相减操作
	 * @param a Double
	 * @param b Double
	 * @return 差
	 */
	public static Double subtract(Double a, Double b) {
		BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(a));
		BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(b));
		return bigDecimal1.subtract(bigDecimal2).doubleValue();
	}

	/**
	 * 功能：三个数字进行相减操作
	 * @param a Double
	 * @param b Double
	 * @param c Double
	 * @return 差
	 */
	public static Double subtract(Double a, Double b, Double c) {
		BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(a));
		BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(b));
		BigDecimal bigDecimal3 = new BigDecimal(String.valueOf(c));
		return bigDecimal1.subtract(bigDecimal2).subtract(bigDecimal3).doubleValue();
	}

	/**
	 * 功能：四个数字进行相减操作
	 * @param a Double
	 * @param b Double
	 * @param c Double
	 * @param d Double
	 * @return 差
	 */
	public static Double subtract(Double a, Double b, Double c, Double d) {
		BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(a));
		BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(b));
		BigDecimal bigDecimal3 = new BigDecimal(String.valueOf(c));
		BigDecimal bigDecimal4 = new BigDecimal(String.valueOf(d));
		return bigDecimal1.subtract(bigDecimal2).subtract(bigDecimal3).subtract(bigDecimal4).doubleValue();
	}

	/**
	 * 功能：两个数字进行相乘操作
	 * @param a String
	 * @param b String
	 * @return 乘积
	 */
	public static Double multiply(String a, String b) {
		BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(a));
		BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(b));
		return bigDecimal1.multiply(bigDecimal2).doubleValue();
	}

	/**
	 * 功能：两个数字进行相乘操作
	 * @param a String
	 * @param b Integer
	 * @return 乘积
	 */
	public static Integer multiply(String a, Integer b) {
		BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(a));
		BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(b));
		return bigDecimal1.multiply(bigDecimal2).intValue();
	}

	/**
	 * 功能：两个数字进行相乘操作
	 * @param a Double
	 * @param b Integer
	 * @return 乘积
	 */
	public static Integer multiply(Double a, Integer b) {
		BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(a));
		BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(b));
		return bigDecimal1.multiply(bigDecimal2).intValue();
	}

	/**
	 * 功能：两个数字进行相乘操作
	 * @param a Double
	 * @param b Double
	 * @return 乘积
	 */
	public static Double multiply(Double a, Double b) {
		BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(a));
		BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(b));
		return bigDecimal1.multiply(bigDecimal2).doubleValue();
	}

	/**
	 * 功能：三个数字进行相乘操作
	 * @param a Double
	 * @param b Double
	 * @param c Double
	 * @return 乘积
	 */
	public static Double multiply(Double a, Double b, Double c) {
		BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(a));
		BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(b));
		BigDecimal bigDecimal3 = new BigDecimal(String.valueOf(c));
		return bigDecimal1.multiply(bigDecimal2).multiply(bigDecimal3).doubleValue();
	}

	/**
	 * 功能：四个数字进行相乘操作
	 * @param a Double
	 * @param b Double
	 * @param c Double
	 * @param d Double
	 * @return 乘积
	 */
	public static Double multiply(Double a, Double b, Double c, Double d) {
		BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(a));
		BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(b));
		BigDecimal bigDecimal3 = new BigDecimal(String.valueOf(c));
		BigDecimal bigDecimal4 = new BigDecimal(String.valueOf(d));
		return bigDecimal1.multiply(bigDecimal2).multiply(bigDecimal3).multiply(bigDecimal4).doubleValue();
	}

	/**
	 * 功能：除法 保留两位小数点  进一位
	 * @param a Double
	 * @param b Double
	 * @return 商
	 */
	public static Double divide(Double a, Double b) {
		BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(a));
		BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(b));
		return bigDecimal1.divide(bigDecimal2,2,RoundingMode.UP).doubleValue();
	}
	/**
	 * 功能：除法 保留两位小数点  进一位
	 * @param a Double
	 * @param b Integer
	 * @return 商
	 */
	public static Double divide(Double a, Integer b) {
		BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(a));
		BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(b));
		return bigDecimal1.divide(bigDecimal2,2,RoundingMode.UP).doubleValue();
	}
	
	/**
	 * 功能：除法 保留两位小数点 四舍五入
	 * @param a Double
	 * @param b Double
	 * @return 商
	 */
	public static Double divideHALFEVEN(Double a, Double b) {
		BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(a));
		BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(b));
		return bigDecimal1.divide(bigDecimal2,2,RoundingMode.HALF_EVEN).doubleValue();
	}

	/**
	 * 功能：除法 保留两位小数点  四舍五入
	 * @param a Double
	 * @param b Integer
	 * @return 商
	 */
	public static Double divideHALFEVEN(Double a, Integer b) {
		BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(a));
		BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(b));
		return bigDecimal1.divide(bigDecimal2,2,RoundingMode.HALF_EVEN).doubleValue();
	}
	
	/**
	 * 功能：除法取整(必须是除的近的，如果结果为无穷位的话，则会报错)
	 * @param a Double
	 * @param b Double
	 * @return 商
	 */
	public static Integer divideForInt(Double a ,Double b){
		BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(a));
		BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(b));
		return bigDecimal1.divide(bigDecimal2).intValue();
	}

	// ----------------------------------------------------start
	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 * @param v1 被除数
	 * @param v2 除数
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2) {

		return div(v1, v2, 10);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 * @param v1    被除数
	 * @param v2    除数
	 * @param scale 表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException( "The   scale   must   be   a   positive   integer   or   zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	// ----------------------------------------------------end

	/**
	 * 功能：传入数字默认2位进位处理 如：2.331变成2.34
	 * 注意：如果传入6.19353454 则返回结果会是6.2
	 * @param a 要处理的参数 Double类型
	 * @return 处理结果
	 */
	public static Double dealDecimal(Double a) {

		return dealDecimal(a,2);
	}
	/**
	 * 功能：传入数字和进位数字 进位处理(四舍五入) 如：2.341 1 则变成2.4
	 * 注意：如果传入2.9245435 1 则返回结果是3.0，因为2.9然后进一位会变成3.0
	 * @param a 要处理的数据 Double
	 * @param decimal 保留的小数个数
	 * @return 处理结果
	 */
	public static Double dealDecimal(Double a,int decimal) {
		BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(a));
		return bigDecimal1.setScale(decimal,BigDecimal.ROUND_UP).doubleValue();
	}

	/**
	 * 功能：传入数字默认2位截位处理 如：2.336变成2.33
	 * @param d 要处理的参数 Double类型
	 * @return 处理结果
	 */
	public static Double dealDecimalSubDecimal(Double d) {

		return dealDecimalSubDecimal(d,2);
	}

	/**
	 * 功能：传入数字和进位数字 截位处理 如：2.376 1 则变成2.3
	 * @param d 要处理的参数 Double类型
	 * @param decimal 保留的小数个数
	 * @return 处理结果
	 */
	public static Double dealDecimalSubDecimal(Double d,int decimal) {
		  DecimalFormat forMater = new DecimalFormat();
		  forMater.setMaximumFractionDigits(decimal);
		  forMater.setGroupingSize(0);
		  forMater.setRoundingMode(RoundingMode.FLOOR);
		return new BigDecimal(String.valueOf(forMater.format(d))).doubleValue();
	}

	/**
	 * 功能：将传入的Double转换成String
	 * @param a 要处理的参数 Double类型
	 * @return 处理结果
	 */
	public static String dobleToString(Double a) {
		if (a == null) {
			return "";
		}
		return new BigDecimal(String.valueOf(a)).toPlainString();
	}
	
	/**
	 * 功能：保留两位的四舍五入
	 * @param d 要处理的参数 Double类型
	 * @return 处理结果
	 */
	public static Double halfEven(Double d){
		return new BigDecimal(String.valueOf(d)).setScale(2,RoundingMode.HALF_EVEN).doubleValue();
	}

	// 单元测试
	public static void main(String[] args) {
		System.out.println("两个字符串的数字进行相加操作：" + add("1","2.3"));
		System.out.println("两个double的数字进行相加操作：" + add(2.3D,5.5D));
		System.out.println("多个String数字进行相加操作：" + addAll("1","2","4","1"));
		System.out.println("多个Number数字进行相加操作：" + addAll(1D,5,7,2,9));
		System.out.println("多个String|Number数字进行相加操作：" + addAll(1,"3.2","5",6D));
		System.out.println("两个字符串的数字进行相乘操作：："+ multiply("10.2","5"));
		System.out.println("两个String|Integer数字进行相乘操作：" + multiply("5.3",6));
		System.out.println("两个Double|Integer数字进行相乘操作：" + multiply(2.2D,8));
		System.out.println("两个Double数字进行相乘操作：" + multiply(5D,6D));
		System.out.println("三个Double数字进行相乘操作：" + multiply(1D,2D,6D));
		System.out.println("四个Double数字进行相乘操作：" + multiply(5D,6D,7D,8D));
		System.out.println("两个String数字进行相减操作：" + subtract("14","7"));
		System.out.println("两个Double数字进行相减操作：" + subtract(18D,7D));
		System.out.println("三个Double数字进行相减操作：" + subtract(20.8D,5D,1.2D));
		System.out.println("四个Double数字进行相减操作：" + subtract(100D,5.2D,27.1D,15.5D));
		System.out.println("两个Double数字进行相除操作(保留两位小数，进一位)：" + divide(8D,3D));
		System.out.println("两个Double|Integer数字进行相除操作(保留两位小数，进一位)：" + divide(8D,3));
		System.out.println("两个Double数字进行相除操作(保留两位，四舍五入)：" + divideHALFEVEN(8D,3D));
		System.out.println("两个Double|Integer数字进行相除操作(保留两位，四舍五入)：" + divideHALFEVEN(8D,3));
		System.out.println("两个Double数字进行相除操作(取整)：" + divideForInt(8.6D,2D));
		System.out.println("传入数字进行操作(两位进位)：" + dealDecimal(6.193542810D));
		System.out.println("传入数字进行取位进位操作：" + dealDecimal(6.183542810D,2));
		System.out.println("传入数字进行操作(两位截位)：" + dealDecimalSubDecimal(2.4591D));
		System.out.println("传入数字进行取位截位操作：" + dealDecimalSubDecimal(2.459586D,3));
		System.out.println("传入的数字转换输出为字符串操作：" + dobleToString(5.23432D));
		System.out.println("传入数字进行操作(保留两位小数，四舍五入)：" + halfEven(2.459586D));
	}
}
