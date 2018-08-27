package cn.self.cloud.commonutils.o.AES_2016.decrypt;

import cn.self.cloud.commonutils.o.AES_2016.tools.CommonUtils;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 利用AES进行数据的解密操作
 * @createDate 2016年2月26日 下午9:46:36
 */
public class AESDecrypt {
	
	/**
	 * 将content内容根据password密钥进行AES解密处理
	 // * @param byte[] content 待解密的内容byte数组
	 // * @param String password 解密的密钥字符串
	 * @return null或者解密后的byte数组
	 */
	protected byte[] decrypt(byte[] content, String password) { 
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
              cipher.init(Cipher.DECRYPT_MODE, key);// 初始化 
	          return cipher.doFinal(content); // 解密 
	     } catch (Exception e) { 
	          e.printStackTrace(); 
	     } 
	     return null; 
	}
	
	/**
	 * 将content内容根据password密钥进行AES解密处理
	 // * @param byte[] content 待解密的内容byte数组(待解密内容的长度必须是16位或者16的倍数  ！！经过优化，随便多少位都可以了)
	 // * @param String password 解密的密钥字符串(密钥必须是16位的  ！！经过优化，只需要位数不大于16位就可以了)
	 * @return null或者解密后的byte数组
	 */
	protected byte[] unLock(byte[] content,String password) {
		if( null == content || null == password ){
			return null;
		}
		if( password.length() > 16 ){
			return null;
		}
		password = CommonUtils.autoAddStringLength(password); // 组拼数据到16位
		
		try {
			SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化 
			return cipher.doFinal(content); // 加密
		} catch (Exception e) {
			e.printStackTrace();
		} 
         return null;  
	}
	
	/**
	 * 将content内容根据password密钥进行AES解密处理
	 // * @param String content 待解密的内容字符串
	 // * @param String paramassword 解密的密钥字符串
	 * @return null或者解密后的字符串内容
	 */
	public String decryptContentToString(String content, String password){
		byte[] result = decrypt(CommonUtils.parseHexStr2Byte(content),password);
		try {
			return (null == result) ? null : new String(result,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 将content内容根据password密钥进行AES解密处理
	 // * @param String content 待解密的内容字符串(待解密内容的长度必须是16位或者16的倍数  ！！经过优化，随便多少位都可以了)
	 // * @param String password 解密的密钥字符串(密钥必须是16位的  ！！经过优化，只需要位数不大于16位就可以了)
	 * @return null或者解密后的字符串内容
	 */
	public String unLockContentToString(String content, String password){
		byte[] result = unLock(CommonUtils.parseHexStr2Byte(content),password);
		if( null == result )
			return null;
		try {
			String returnString = new String(result,"UTF-8");
			int location = returnString.lastIndexOf("#");
			if( location!=-1 ){
				returnString = returnString.substring(0,location);				
			}
			return returnString;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/* 单元测试  */
	public static void main(String[] args) {
		 //System.out.println(new AESDecrypt().decryptContentToString("FB56E7DDB605160EA864877DE3001D4E", ""));
		 System.out.println(new AESDecrypt().unLockContentToString("A3B8000925E5B7925F865B266CF1A4EC", "555"));
	}
}