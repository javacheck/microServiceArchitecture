package cn.self.cloud.commonutils.basictype;

import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.util.DigestUtils;
import jodd.util.RandomString;
import jodd.util.StringUtil;

/**
 * 字符串处理
 */
public final class StringUtils {

    /**
     * 功能：根据条件截取分割字符串成字符串数组
     * @param src 需要截取分割的字符串
     * @param delimiter  截取分割的条件
     * @return 返回字符串数组
     */
    public static String[] split(String src, String delimiter) {
        // 调用第三方Jodd的截取分割方法
        return StringUtil.split(src, delimiter);
    }

    /**
     * 功能：将传入的字符串首字母变成大写
     * @param str 传入的源字符串
     * @return 返回字符串
     */
    public static String capitalize(String str) {
        // 调用第三方Jodd的方法
        return StringUtil.capitalize(str);
    }

    /**
     * 功能：判断传入的字符串是否不为空
     * @param str 传入的字符串
     * @return 不为空返回true
     */
    public static boolean isNotBlank(String str) {
        // 调用第三方Jodd的判断不是为空的方法
        return StringUtil.isNotBlank(str);
    }

    /**
     * 功能：判断传入的字符串是否为空
     * @param str 传入的字符串
     * @return 为空返回true
     */
    public static boolean isBlank(String str) {
        // 调用第三方Jodd的判断为空的方法
        return StringUtil.isBlank(str);
    }

    /**
     * 功能：检测传入的字符串是否符合Email规则
     * @param email 待判断的字符串
     * @return 符合规则返回true
     */
    public static boolean checkEmail(String email) {
        String regex = "[a-zA-Z][\\w_]+@\\w+(\\.\\w+)+"; // 验证Email
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 功能：手机号验证
     * @param mobile 待判断的字符串
     * @return 验证通过返回true
     */
    public static boolean isMobile(String mobile) {
        if (isBlank(mobile)) {
            return false;
        }

        String regex = "^[1][3,4,5,8,7][0-9]{9}$"; // 验证手机号
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(mobile);
        return m.matches();
    }

    /**
     * 功能：电话号码验证（会分别验证带区号的和不带区号的-->带区号的大于9位数）
     * @param phone 待判断的字符串
     * @return 验证通过返回true
     */
    public static boolean isPhone(String phone) {
        if (isBlank(phone)) {
            return false;
        }
        boolean b = false;
        if (phone.length() > 9) {
            String yesRegex = "^[0][1-9]{2,3}-[0-9]{5,10}$"; // 验证带区号的
            Pattern p1 = Pattern.compile(yesRegex);
            Matcher m = p1.matcher(phone);
            b = m.matches();
        } else {
            String noRegex = "^[1-9]{1}[0-9]{5,8}$"; // 验证没有区号的
            Pattern p2 = Pattern.compile(noRegex);
            Matcher m = p2.matcher(phone);
            b = m.matches();
        }
        return b;
    }

    /**
     * 功能：生成一串随机字符串数(通过UUID)
     * @return 随机字符串数
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 功能：比较两个字符串是否相等
     * @param str1 第一个字符串
     * @param str2 第二个字符串
     * @return
     */
    public static boolean equals(String str1, String str2) {
        if ( str1 == null || str2 == null ) {
            return false;
        }
        return str1.equals(str2);
    }

    /**
     * 功能：MD5加密字符串
     * @param str 需要加密的字符串
     * @return 加密后的字符串
     */
    public static String md5(String str) {
        try {
            return DigestUtils.md5DigestAsHex(str.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }

    /**
     * 功能：随机生成由数字和字母(大小写)组成的随机字符串
     * @param count 个数，需要生产的随机字符串长度
     * @return 随机字符串
     */
    public static String getRandomStr(int count) {
        return RandomString.getInstance().randomAlphaNumeric(count);
    }

    /**
     * 功能：去掉前缀
     * @param str 需要处理的字符串
     * @param prefix 需要去掉的前缀
     * @return 处理后的字符串
     */
    public static String removePrefix(String str, String prefix) {
        while (str.startsWith(prefix)) {
            str = str.substring(1);
        }
        return str;
    }

    /**
     * 功能：验证正整数
     * @param str 传入的待检验字符串
     * @return 验证通过返回true
     */
    public static boolean isPositiveIntegral(String str) {
        Pattern p = Pattern.compile("[^1-9]/D*$"); // 验证正整数
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 功能：验证数字
     * @param str 传入的待检验字符串
     * @return 验证通过返回true
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 功能：验证是否保留两位小数
     * @param str 传入的待检验字符串
     * @return 验证通过返回true
     */
    public static boolean isTwoPointFloat(String str) {
        Pattern pattern = Pattern.compile("^[0-9]+([.][0-9]{1,2})?$");
        return pattern.matcher(str).matches();
    }

    // 单元测试
    public static void main(String[] args) {
        System.out.println("根据条件进行截取分割字符串：" + split("1.234.3534.534.6.6456.75675.2.",".").length);
        System.out.println("将传入的首字符大写：" + capitalize("zhong guo"));
        System.out.println("判断传入的字符串是否不为空：" + isNotBlank(""));
        System.out.println("判断传入的字符串是否为空：" + isBlank(""));
        System.out.println("检验传入的是否是邮箱地址：" + checkEmail("12321@139.com"));
        System.out.println("检验传入的是否是手机号码：" + isMobile("15975555555555555"));
        System.out.println("检验传入的是否是电话号码：" + isPhone("020-5689526"));
        System.out.println("获取随机字符串：" + uuid());
        System.out.println("比较两个字符串是否相等：" + equals("1","2"));
        System.out.println("使用MD5加密指定的字符串：" + md5("123456"));
        System.out.println("生成随机数：" + getRandomStr(5));
        System.out.println("**************************" + removePrefix("<h<h<h<html><habcd<bdfdf123","<h"));
        System.out.println("验证是否是正整数：" + isPositiveIntegral("2343243.3534543"));
        System.out.println("验证传入的是否是数字：" + isNumeric("235435345435435435434534543543"));
        System.out.println("验证传入的是否有小数点后两位：" + isTwoPointFloat("23432.20"));
    }
}
