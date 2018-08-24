package cn.self.cloud.commonutils.password;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * DES文件加密解密
 *@function 已DES的方式进行文件的加解密
 *@author WL
 *@date 2013-4-12下午11:09:26
 **/
public class DESEncryptionCommonFile {
	
	// 确定密钥
	private static String key = "wangling";
	
	// 需要加密的文件
	private static String inputFileString = "F:\\表空间b.sql";
	
	// 加密后的文件
	private static String outputFileString = "F:\\表空间a.sql";
	
	/**
	 *@function 用DES的方式进行文件的加密并以流的方式输出
	 *@author WL
	 *@date 2013-4-13下午12:06:57
	 *@param inputFileStrings 需要加密的文件
	 *@param outputFileStrings 加密后的文件
	 *@param keys 加密时使用的密钥
	 */
	public static String EncryptionFile(String inputFileStrings, String outputFileStrings, String keys) {
		try {
			
			File inputFile = new File(inputFileStrings);
			
			File outputFile = new File(outputFileStrings);
			
			// 需要加密的文件不存在,直接返回并提示
			if( !inputFile.exists()){
				return "需要加密的文件不存在！";
			}
			
			// 输出目录不存在，则创建
			if( !outputFile.exists()){
				outputFile.getParentFile().mkdirs(); // 创建父类目录
				outputFile.createNewFile(); // 创建文件
			}
			
			// 用输入流将要加密的文件加载进来
			FileInputStream fileinputstream = new FileInputStream(inputFile);
			
			// 用输出流将加密后的文件保存地址加载进来
			FileOutputStream fileoutputstream = new FileOutputStream(outputFile);
			
			// 加载密钥
			DESKeySpec deskeyspec = new DESKeySpec(keys.getBytes());
			
			// 创建DES的密钥工厂
			SecretKeyFactory secretkeyfactory = SecretKeyFactory.getInstance("DES");
			
			// 加载密钥工厂指定的密钥对象
			SecretKey secretkey = secretkeyfactory.generateSecret(deskeyspec);
			
			// 使用密码对象
			Cipher cipher = Cipher.getInstance("DES");
			
			// 进行加密处理
			cipher.init(Cipher.ENCRYPT_MODE, secretkey);
			
			// 使用密码输出流，确定输出文件对象
			CipherOutputStream cipheroutputstream = new CipherOutputStream(fileoutputstream, cipher);
			
			// 记录读取的字节数
			int readLen = 0;
			
			// 定义缓冲字节数组
			byte[] buffer = new byte[1024];
			
			// 一次性读取buffer个字节并将其保存起来，将读取的字节数用readLen记录起来
			while( ( readLen = fileinputstream.read(buffer)) != -1  ){
				
				// 用密码对象输出流将读取的字节数组经过加密后从第0个字节读取，一次性读取readLen个字节。并将加密后的文件内容输出到输出文件对象
				cipheroutputstream.write(buffer,0,readLen);
			}
			
			// 处理缓冲区中的数据并关闭流
			fileoutputstream.flush();
			fileoutputstream.close();
			cipheroutputstream.close();
			fileinputstream.close();
			return "加密成功！";
		} catch (Exception e) {
			e.printStackTrace();
			return "加密失败！";
		}
	}
	
	/**
	 *@function 用DES的方式进行文件的解密并以流的方式输出
	 *@author WL
	 *@date 2013-4-13下午04:22:17
	 *@param inputFileStrings 
	 *@param outputFileStrings
	 *@param keys 解密时使用的密钥
	 */
	public static String UnEncryptionFile(String inputFileStrings, String outputFileStrings, String keys) {
		try {
			
			File inputFile = new File(inputFileStrings);
			
			File outputFile = new File(outputFileStrings);
			
			// 需要解密的文件不存在,直接返回并提示
			if( !inputFile.exists()){
				return "需要解密的文件不存在！";
			}
			
			// 输出目录不存在，则创建
			if( !outputFile.exists()){
				outputFile.getParentFile().mkdirs(); // 创建父类目录
				outputFile.createNewFile(); // 创建文件
			}
			
			FileInputStream fileinputstream = new FileInputStream(inputFileStrings);
			FileOutputStream fileoutputstream = new FileOutputStream(outputFileStrings);
			
			DESKeySpec deskeyspec = new DESKeySpec(keys.getBytes());
			SecretKeyFactory secretkeyfactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretkey = secretkeyfactory.generateSecret(deskeyspec);
			Cipher cipher = Cipher.getInstance("DES");
			
			// 解密和加密差不多，唯一的不同就是此处使用的是解密模式(DECRYPT_MODE)
			cipher.init(Cipher.DECRYPT_MODE, secretkey);
			
			// 将需要解密的文件以DES的方式加载进密码对象的读取对象流
			CipherInputStream cipherinpuutstream = new CipherInputStream(fileinputstream, cipher);
			
			int readLen = 0;
			byte[] buffer = new byte[1024];
			while( ( readLen = cipherinpuutstream.read(buffer)) != -1  ){
				
				// 将已经解密的文件内容输出到输出文件中
				fileoutputstream.write(buffer,0,readLen);
			}
			
			// 处理缓冲区中的数据并关闭流
			fileoutputstream.flush();
			fileoutputstream.close();
			cipherinpuutstream.close();
			fileinputstream.close();
			return "解密成功！";
		} catch (Exception e) {
			e.printStackTrace();
			return "解密失败！";
		}
	}
	
	//*****单元测试******
	public static void main(String[] args) {
		
		// 加密
		System.out.println(EncryptionFile(inputFileString,outputFileString,key));
		
		// 解密
		System.out.println(UnEncryptionFile(inputFileString,outputFileString,key));
	}
}