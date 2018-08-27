package cn.self.cloud.commonutils.o.AES_2016.encrypt;

import cn.self.cloud.commonutils.o.AES_2016.tools.CommonUtils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 利用AES进行数据的加密操作
 * @createDate 2016年2月26日 下午9:44:04
 */
public class AESEncrypt {
	
	/**
	 * 将content内容根据password密钥进行AES加密处理
	 // * @param byte[] content 需要加密的内容byte数组
	 // * @param Stringg password 加密的密钥字符串
	 * @return null或者加密后的byte数组
	 */
	protected byte[] encrypt(String content, String password){
		if( null == content || null == password ){
			return null;
		}
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes())); 
	        SecretKey secretKey = kgen.generateKey(); 
	        byte[] enCodeFormat = secretKey.getEncoded(); 
	        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES"); 
	        Cipher cipher = Cipher.getInstance("AES");// 创建密码器 
	        byte[] byteContent = content.getBytes("UTF-8"); 
	        cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化 
	        return cipher.doFinal(byteContent); // 加密 
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	/**
	 * 将content内容根据password密钥进行AES加密处理
	 // * @param byte[] content 需要加密的内容byte数组(需要加密内容的长度必须是16位或者16的倍数  ！！经过优化，随便多少位都可以了)
	 // * @param String password 加密的密钥字符串(密钥必须是16位的  ！！经过优化，只需要位数不大于16位就可以了)
	 * @return null或者加密后的byte数组
	 */
	protected byte[] lock(String content,String password) {
		if( null == content || null == password ){
			return null;
		}
		if( password.length() > 16 ){
			return null;
		}
		content = CommonUtils.autoAddStringLength(content);
		password = CommonUtils.autoAddStringLength(password);
		
		try {
			SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
			byte[] byteContent = content.getBytes("UTF-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化 
			return cipher.doFinal(byteContent); // 加密
		} catch (Exception e) {
			e.printStackTrace();
		} 
         return null;  
	}

	/**
	 * 将content内容根据password密钥进行AES加密处理
	 // * @param String content 需要加密的内容字符串
	 // * @param String password 加密的密钥字符串
	 * @return null或者加密后的字符串内容
	 */
	public String encryptContentToString(String content, String password){
		byte[] result = encrypt(content,password);
		if( null == result ){
			return null;
		}
		return CommonUtils.parseByte2HexStr(result);
	}
	
	/**
	 * 将content内容根据password密钥进行AES加密处理
	 // * @param String content 需要加密的内容字符串(需要加密内容的长度必须是16位或者16的倍数  ！！经过优化，随便多少位都可以了)
	 // * @param String password 加密的密钥字符串(密钥必须是16位的  ！！经过优化，只需要位数不大于16位就可以了)
	 * @return null或者加密后的字符串内容
	 */
	public String lockContentToString(String content, String password){
		byte[] result = lock(content,password);
		if( null == result ){
			return null;
		}
		return CommonUtils.parseByte2HexStr(result);
	}
	
	/* 单元测试  */
	public static void main(String[] args) {
		//System.out.println(new AESEncrypt().encryptContentToString("解密",""));
		System.out.println(new AESEncrypt().lockContentToString("解1密#", ""));
	}
}