package cn.self.cloud.commonutils.basictype;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumberUtils {
	
	public static Double add(String a, String b) {
		return new BigDecimal(String.valueOf(a)).add(
				new BigDecimal(String.valueOf(b))).doubleValue();
	}

	public static Double multiply(String a, String b) {
		return new BigDecimal(String.valueOf(a)).multiply(
				new BigDecimal(String.valueOf(b))).doubleValue();
	}

	public static Double subtract(String a, String b) {
		return new BigDecimal(String.valueOf(a)).subtract(
				new BigDecimal(String.valueOf(b))).doubleValue();
	}

	public static Integer multiply(String a, Integer b) {
		return new BigDecimal(String.valueOf(a)).multiply(
				new BigDecimal(String.valueOf(b))).intValue();
	}

	public static Double add(Double a, Double b) {
		return new BigDecimal(String.valueOf(a)).add(
				new BigDecimal(String.valueOf(b))).doubleValue();
	}
	
	/**
	 * 除法 保留两位小数点  进一位
	 * @param a
	 * @param b
	 * @return
	 */
	public static Double divide(Double a, Double b) {
		return new BigDecimal(String.valueOf(a)).divide(
				new BigDecimal(String.valueOf(b)),2,RoundingMode.UP).doubleValue();
	}
	/**
	 * 除法 保留两位小数点  进一位
	 * @param a
	 * @param b
	 * @return
	 */
	public static Double divide(Double a, Integer b) {
		return new BigDecimal(String.valueOf(a)).divide(
				new BigDecimal(String.valueOf(b)),2,RoundingMode.UP).doubleValue();
	}
	
	/**
	 * 除法 保留两位小数点 四舍五入
	 * @param a
	 * @param b
	 * @return
	 */
	public static Double divideHALFEVEN(Double a, Double b) {
		return new BigDecimal(String.valueOf(a)).divide(
				new BigDecimal(String.valueOf(b)),2,RoundingMode.HALF_EVEN).doubleValue();
	}
	/**
	 * 除法 保留两位小数点  四舍五入
	 * @param a
	 * @param b
	 * @return
	 */
	public static Double divideHALFEVEN(Double a, Integer b) {
		return new BigDecimal(String.valueOf(a)).divide(
				new BigDecimal(String.valueOf(b)),2,RoundingMode.HALF_EVEN).doubleValue();
	}
	
	/**
	 * 除法取整
	 * @param a
	 * @param b
	 * @return
	 */
	public static Integer divideForInt(Double a ,Double b){
		return new BigDecimal(String.valueOf(a)).divide(
				new BigDecimal(String.valueOf(b))).intValue();
	}

	public static Double multiply(Double a, Double b) {
		return new BigDecimal(String.valueOf(a)).multiply(
				new BigDecimal(String.valueOf(b))).doubleValue();
	}

	public static Double multiply(Double a, Double b, Double c) {
		return new BigDecimal(String.valueOf(a))
				.multiply(new BigDecimal(String.valueOf(b)))
				.multiply(new BigDecimal(String.valueOf(c))).doubleValue();
	}

	public static Double multiply(Double a, Double b, Double c, Double d) {
		return new BigDecimal(String.valueOf(a))
				.multiply(new BigDecimal(String.valueOf(b)))
				.multiply(new BigDecimal(String.valueOf(c)))
				.multiply(new BigDecimal(String.valueOf(d))).doubleValue();
	}

	public static Double subtract(Double a, Double b) {
		return new BigDecimal(String.valueOf(a)).subtract(
				new BigDecimal(String.valueOf(b))).doubleValue();
	}

	public static Double subtract(Double a, Double b, Double c) {
		return new BigDecimal(String.valueOf(a))
				.subtract(new BigDecimal(String.valueOf(b)))
				.subtract(new BigDecimal(String.valueOf(c))).doubleValue();
	}
	public static Double subtract(Double a, Double b, Double c, Double d) {
		return new BigDecimal(String.valueOf(a))
				.subtract(new BigDecimal(String.valueOf(b)))
				.subtract(new BigDecimal(String.valueOf(c)))
				.subtract(new BigDecimal(String.valueOf(d))).doubleValue();
	}

	public static Integer multiply(Double a, Integer b) {
		return new BigDecimal(String.valueOf(a)).multiply(
				new BigDecimal(String.valueOf(b))).intValue();
	}
	/**
	 * 默认2位进位处理
	 * @param a 要处理的参数
	 * @return
	 */
	public static Double dealDecimal(Double a) {

		return dealDecimal(a,2);// 进位处理，2.331变成2.34
	}
	/**
	 * 进位处理
	 * @param a 要处理的数据
	 * @param b 保留的小数个数
	 * @return
	 */
	public static Double dealDecimal(Double a,int decimal) {
		return new BigDecimal(String.valueOf(a)).setScale(decimal,
				BigDecimal.ROUND_UP).doubleValue();// 进位处理，2.341变成2.41
	}
	public static Double dealDecimalSubdecimal(Double d,int decimal) {
		  DecimalFormat formater = new DecimalFormat();
	      formater.setMaximumFractionDigits(decimal);
	      formater.setGroupingSize(0);
	      formater.setRoundingMode(RoundingMode.FLOOR);
		return new BigDecimal(String.valueOf(formater.format(d))).doubleValue();//
	}
	public static Double dealDecimalSubdecimal(Double d) {

		return dealDecimalSubdecimal(d,2);
	}

	public static String dobleToString(Double a) {
		if (a == null) {
			return "";
		}
		return new BigDecimal(a.toString()).toPlainString();
	}
	
	/**
	 * 保留两位的四舍五入
	 * @param d
	 * @return
	 */
	public static Double halfEven(Double d){
		return new BigDecimal(d).setScale(2,
				RoundingMode.HALF_EVEN).doubleValue();
	}
	
	/**
	 * 多个数值累加。
	 * @param args 要累加的数
	 * @return Double 结果
	 */
	public static Double addAll(String... args) {
		if (args == null || args.length == 0)
			throw new RuntimeException("加法运算参数不能为空！");
		BigDecimal result = new BigDecimal(args[0]);
		for (int i = 1; i < args.length; i++)
			result = result.add(new BigDecimal(args[i]));
		return result.doubleValue();
	}

	/**
	 * 多个数值累加。
	 * @param args 要累加的数
	 * @return Double 结果
	 */
	public static Double addAll(Number... args) {
		if (args == null || args.length == 0)
			throw new RuntimeException("加法运算参数不能为空！");
		BigDecimal result = new BigDecimal(args[0].toString());
		for (int i = 1; i < args.length; i++)
			result = result.add(new BigDecimal(args[i].toString()));
		return result.doubleValue();
	}
	
	/**
	 * 多个数值累加
	 * @param args 要累加的数
	 * @return 结果
	 */
	public static Double addAll(Object... args) {
		if (args == null || args.length == 0)
			throw new RuntimeException("加法运算参数不能为空！");
		BigDecimal result = new BigDecimal(0);
		for (int i = 0; i < args.length; i++)
			if (args[i] instanceof String || args[i] instanceof Number)
				result = result.add(new BigDecimal(String.valueOf(args[i])));
		return result.doubleValue();
	}
}
