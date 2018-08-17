package cn.self.cloud.commonutils.password;

import cn.self.cloud.commonutils.basictype.StringUtils;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

/**
 * 3DES加密工具类
 */
public class Des3 {
	// 密钥
	private final static String secretKey = "123456789012345678901234";
	// 向量
//	private final static String iv = "tjchehai";
	// 加解密统一使用的编码方式
	private final static String encoding = "utf-8";

	/**
	 * 产生一个长度为24字节的密钥,如果key不为空，则覆盖到secretKey的相应字节，如果为空，则直接采用secretKey的字节数组
	 * 
	 * @param key
	 * @return
	 */
	private static byte[] genkey(String key) {
		byte[] keyBytes = new byte[24];
		System.arraycopy(secretKey.getBytes(), 0, keyBytes, 0, 24);
		if (StringUtils.isNotEmpty(key)) {
			byte[] bs = key.getBytes();
			int length = bs.length > 24 ? 24 : bs.length;
			System.arraycopy(bs, 0, keyBytes, 0, length);
		}
		return keyBytes;
	}

	/**
	 * 3DES加密
	 * 
	 * @param plainText
	 *            普通文本
	 * @return
	 * @throws Exception
	 */
	public static String encode(String key, String plainText) throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(genkey(key));
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);

		Cipher cipher = Cipher.getInstance("desede/ECB/PKCS5Padding");
//		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
//		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
//		cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
		cipher.init(Cipher.ENCRYPT_MODE, deskey);
		byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
		return new String(Base64Util.encode(encryptData), "ISO-8859-1");
	}

	/**
	 * 3DES解密
	 * 
	 * @param encryptText
	 *            加密文本
	 * @return
	 * @throws Exception
	 */
	public static String decode(String key, String encryptText)
			throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(genkey(key));
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("desede/ECB/PKCS5Padding");
//		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
//		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
//		cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
		
		cipher.init(Cipher.DECRYPT_MODE, deskey);

		byte[] decryptData = cipher.doFinal(Base64Util.decode(encryptText));

		return new String(decryptData, encoding);
	}

	public static void main(String[] args) throws Exception {
		String str = "fdaf;daksjfdl;sjfioowejiriewfldsjfdieiowere测试中文用的";
		String cipher = encode("hello", str);
		String text = decode("hello", cipher);
		System.out.println(str);
		System.out.println(cipher);
		System.out.println(text);
	}
}