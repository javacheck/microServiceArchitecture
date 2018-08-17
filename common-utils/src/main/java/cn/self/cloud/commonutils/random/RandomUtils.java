package cn.self.cloud.commonutils.random;

import java.security.SecureRandom;
import java.util.Random;

/**
 * 随机数相关工具类
 * Created by cp on 2017/7/28.
 */
public class RandomUtils {
    /**
     * 获取随机字符串
     *
     * @param length
     * @return
     */
    public static String getRand(int length) {
        if (length <= 0)
            return "";
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int r = rand.nextInt(10);
            sb.append(r);
        }
        return sb.toString();
    }

    static final SecureRandom random = new SecureRandom();

    public static String createSessionid() {
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        return new String(bytes);
    }
}
