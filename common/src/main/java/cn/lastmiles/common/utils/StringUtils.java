package cn.lastmiles.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.DigestUtils;

import jodd.util.RandomString;
import jodd.util.StringUtil;

public final class StringUtils {
	public static String[] split(String src, String delimiter){
		
		return StringUtil.split(src, delimiter);
	}
	
	public static String capitalize(String str){
		return StringUtil.capitalize(str);
	}
	
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	public static boolean isBlank(String str) {
		return StringUtil.isBlank(str);
	}

	public static boolean checkEmail(String s) {
		String regex = "[a-zA-Z][\\w_]+@\\w+(\\.\\w+)+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(s);
		return m.matches();
	}

	/**
	 * 手机号验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(String str) {
		if (isBlank(str)){
			return false;
		}
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,8,7][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	/**
	 * 电话号码验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isPhone(String str) {
		Pattern p1 = null, p2 = null;
		Matcher m = null;
		boolean b = false;
		p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$"); // 验证带区号的
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // 验证没有区号的
		if (str.length() > 9) {
			m = p1.matcher(str);
			b = m.matches();
		} else {
			m = p2.matcher(str);
			b = m.matches();
		}
		return b;
	}

	/**
	 * 随机数字窜
	 * 
	 * @param count
	 *            个数
	 * @return
	 */
	public static String getRandomNumeric(int count) {
		if (ConfigUtils.getProperty("redis.ip") != null
				&& ConfigUtils.getProperty("redis.ip").startsWith("192")) { 
			return "888888";
		}
		return RandomString.getInstance().randomNumeric(count);
	}

	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static boolean equals(String str1, String str2) {
		if (str1 == null || str2 == null) {
			return false;
		}
		return str1.equals(str2);
	}

	public static String md5(String str) {
		try {
			return DigestUtils.md5DigestAsHex(str.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return DigestUtils.md5DigestAsHex(str.getBytes());
	}
	
	public static String getRandomStr(int count){
		return RandomString.getInstance().randomAlphaNumeric(count);
	}
	
	/**
	 * 去掉前缀
	 * @param str
	 * @param prefix
	 * @return
	 */
	public static String removePrefix(String str,String prefix){
		while (str.startsWith(prefix)){
			str = str.substring(1);
		}
		return str;
	}
	
	public static void main(String[] args){
		System.out.println("FFWWWWWEEEEENNNNN".substring(12));
	}
	
	public static boolean isPositiveIntegral(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("[^1-9]/D*$"); // 验证正整数
		m = p.matcher(str);
		b = m.matches();
		return b;
	}
	
	public static boolean isNumeric(String str){ 
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    return pattern.matcher(str).matches();    
	 }
	
	public static boolean isTwoPointFloat(String str){
	    Pattern pattern = Pattern.compile("^[0-9]+([.][0-9]{1,2})?$"); 
	    return pattern.matcher(str).matches();
	 }

}
