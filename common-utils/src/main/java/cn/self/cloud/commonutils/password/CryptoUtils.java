package cn.self.cloud.commonutils.password;

import cn.self.cloud.commonutils.basictype.StringUtils;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by cp on 2017/7/28.
 */
public class CryptoUtils {
    public static String changeMapToSortedString(Map<String, String> map) {
        Map<String, String> sortedMap = sortMapByKey(map);
        StringBuilder result = new StringBuilder("[");

        boolean first = true;
        for (String key : sortedMap.keySet()) {
            if (first) {
                first = false;
            } else {
                result.append(",");
            }
            result.append(key);
            result.append("=");
            result.append(sortedMap.get(key));
        }
        result.append("]");
        return result.toString();
    }

    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<>((String str1, String str2) -> {
            return str1.compareTo(str2);
        });
        sortMap.putAll(map);
        return sortMap;
    }

    /***
     * 用指定的秘钥对json数据进行加密，然后用base64编码
     *
     * @param key 秘钥
     * @param plainText 明文
     * @return
     */
    public static String encryptAndEncodeByDes3AndBase64(String key, String plainText) {
        if (StringUtils.isEmpty(plainText) || StringUtils.isEmpty(key))
            return null;
        try {
            return new String(Base64Util.encode(Des3.encode(key, plainText).getBytes()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     *用base64解码，然后用指定的秘钥对数据进行解密
     * @param key  秘钥
     * @param crypted 密文
     * @return
     */
    public static String decodeAndDecryptByBase64AndDes3(String key, String crypted) {
        if (StringUtils.isEmpty(crypted) || StringUtils.isEmpty(key))
            return null;
        try {
            return  Des3.decode(key, new String(Base64Util.decode(crypted)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
