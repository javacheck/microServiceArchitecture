package cn.self.cloud.commonutils.o.AES_2016.tools;

public class CommonUtils {
	
	/** 将二进制转换成16进制
	  * @param buf byte数组
	  * @return 转换后的字符串
	  */ 
	public static String parseByte2HexStr(byte buf[]) { 
		 if( null == buf )
			 return null;
	     StringBuffer sb = new StringBuffer(); 
	     for (int i = 0; i < buf.length; i++) { 
	             String hex = Integer.toHexString(buf[i] & 0xFF); 
	             if (hex.length() == 1) { 
	                     hex = '0' + hex; 
	             } 
	             sb.append(hex.toUpperCase()); 
	     } 
	     return sb.toString(); 
	} 
	
	/** 将16进制转换为二进制
	  * @param hexStr 待转换的字符串
	  * @return 转换后的byte数组
	  */ 
	public static byte[] parseHexStr2Byte(String hexStr) {
	     if( null == hexStr || hexStr.length() < 1) 
	         return null; 
	     byte[] result = new byte[hexStr.length()/2]; 
	     for (int i = 0;i< hexStr.length()/2; i++) { 
	             int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16); 
	             int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16); 
	             result[i] = (byte) (high * 16 + low); 
	     } 
	     return result; 
	} 
	
	/**
	 * 将内容长度不是16位或者16倍数的内容自动补位
	 * @param content 需要补位的内容
	 * @return 补位后的内容
	 */
	public static String autoAddStringLength(String content) {
		try {
			if( null == content )
				return null;
			if( content.length() <= 0 ){
				content = "0";
			}
			int contentLength = content.getBytes("UTF-8").length;
			int multiple = contentLength / 16;
			int remaining = contentLength % 16;
			if( remaining != 0 ){
				int forNumber = ((multiple<=0 ? ++multiple : multiple)*16-remaining);
				for (int i = 0; i < forNumber ; i++) {
					if( i==0 ){
						content+="#";
						continue;
					}
					content+="0";
				}
			}
			return content;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}