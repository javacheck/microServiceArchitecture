package cn.self.cloud.commonutils.basictype;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
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

    private static final int INDEX_NOT_FOUND = -1;
    private static final String EMPTY_STRING = "";
    private static final int PAD_LIMIT = 8192;

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
     * 功能：判断传入的字符串是否为空，判断某字符串是否为空或长度为0或由空白符(whitespace) 构成
     * @param str 传入的字符串
     * @return 为空返回true
     */
    public static boolean isBlank(String str) {
        // 调用第三方Jodd的判断为空的方法
        return StringUtil.isBlank(str);
    }

    /**
     * 功能：判断传入的字符串是否为空，为空的标准是 str==null 或 str.length()==0
     * @param str 传入的字符串
     * @return 为空返回true
     */
    public static boolean isEmpty(String str) {
        // 调用第三方Jodd的判断为空的方法
        return StringUtil.isEmpty(str);
    }
    /**
     * 功能：判断传入的字符串是否为空
     * @param str 传入的字符串
     * @return 为空返回true
     */
    public static boolean isNotEmpty(String str) {
        // 调用第三方Jodd的判断为空的方法
        return StringUtil.isNotEmpty(str);
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

        return UUID.randomUUID().toString().replaceAll("-", EMPTY_STRING);
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

    /**
     * 功能：将半角的符号转换成全角符号.(即英文字符转中文字符)
     * @param str 源字符串
     * @return String 转换后的字符串
     */
    public static String changeToFull(String str) {
        String source = "1234567890!@#$%^&*()abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_=+\\|[];:'\",<.>/?";
        String[] decode = { "１", "２", "３", "４", "５", "６", "７", "８", "９", "０",
                "！", "＠", "＃", "＄", "％", "︿", "＆", "＊", "（", "）", "ａ", "ｂ",
                "ｃ", "ｄ", "ｅ", "ｆ", "ｇ", "ｈ", "ｉ", "ｊ", "ｋ", "ｌ", "ｍ", "ｎ",
                "ｏ", "ｐ", "ｑ", "ｒ", "ｓ", "ｔ", "ｕ", "ｖ", "ｗ", "ｘ", "ｙ", "ｚ",
                "Ａ", "Ｂ", "Ｃ", "Ｄ", "Ｅ", "Ｆ", "Ｇ", "Ｈ", "Ｉ", "Ｊ", "Ｋ", "Ｌ",
                "Ｍ", "Ｎ", "Ｏ", "Ｐ", "Ｑ", "Ｒ", "Ｓ", "Ｔ", "Ｕ", "Ｖ", "Ｗ", "Ｘ",
                "Ｙ", "Ｚ", "－", "＿", "＝", "＋", "＼", "｜", "【", "】", "；", "：",
                "'", "\"", "，", "〈", "。", "〉", "／", "？" };
        StringBuilder result = new StringBuilder();
        int length = str.length();
        for (int i = 0; i < length; i++) {
            int pos = source.indexOf(str.charAt(i));
            if (pos != INDEX_NOT_FOUND) {
                result.append(decode[pos]);
            } else {
                result.append(str.charAt(i));
            }
        }
        return result.toString();
    }

    /**
     * <p>
     * 功能：只从源字符串中移除指定开头子字符串.
     * </p>
     * 例如：
     * <pre>
     * StringUtils.removeStart(null, *)      = null
     * StringUtils.removeStart("", *)        = ""
     * StringUtils.removeStart(*, null)      = *
     * StringUtils.removeStart("www.domain.com", "www.")   = "domain.com"
     * StringUtils.removeStart("domain.com", "www.")       = "domain.com"
     * StringUtils.removeStart("www.domain.com", "domain") = "www.domain.com"
     * StringUtils.removeStart("abc", "")    = "abc"
     * </pre>
     * @param str 源字符串
     * @param remove 将要被移除的子字符串
     * @return 处理后的String字符串
     */
    public static String removeStart(String str, String remove) {
        if ( isBlank(str) || isBlank(remove) ) {
            return str;
        }
        if (str.startsWith(remove)) {
            return str.substring(remove.length());
        }
        return str;
    }

    /**
     * <p>
     * 功能：只从源字符串中移除指定结尾的子字符串.
     * </p>
     * 例如：
     * <pre>
     * StringUtils.removeEnd(null, *)      = null
     * StringUtils.removeEnd("", *)        = ""
     * StringUtils.removeEnd(*, null)      = *
     * StringUtils.removeEnd("www.domain.com", ".com.")  = "www.domain.com"
     * StringUtils.removeEnd("www.domain.com", ".com")   = "www.domain"
     * StringUtils.removeEnd("www.domain.com", "domain") = "www.domain.com"
     * StringUtils.removeEnd("abc", "")    = "abc"
     * </pre>
     * @param str 源字符串
     * @param remove 将要被移除的子字符串
     * @return 处理后的String字符串
     */
    public static String removeEnd(String str, String remove) {
        if ( isBlank(str) || isBlank(remove)) {
            return str;
        }
        if (str.endsWith(remove)) {
            return str.substring(0, str.length() - remove.length());
        }
        return str;
    }

    /**
     * 功能：将某个字符重复N次.
     * @param ch 某个字符
     * @param repeat 重复次数(必须大于0)
     * @return String
     */
    public static String repeat(char ch, int repeat) {
        char[] buf = new char[repeat];
        for (int i = repeat - 1; i >= 0; i--) {
            buf[i] = ch;
        }
        return new String(buf);
    }

    /**
     * <p>
     * 功能：将一个字符串重复N次
     * </p>
     * 例如：
     * <pre>
     * StringUtils.repeat(null, 2) = null
     * StringUtils.repeat("", 0)   = ""
     * StringUtils.repeat("", 2)   = ""
     * StringUtils.repeat("a", 3)  = "aaa"
     * StringUtils.repeat("ab", 2) = "abab"
     * StringUtils.repeat("a", -2) = "a"
     * </pre>
     * @param str 源字符串
     * @param repeat 重复的次数(必须大于0)
     * @return 处理后的String字符串
     */
    public static String repeat(String str, int repeat) {
        // Performance tuned for 2.0 (JDK1.4)

        if ( null == str ) {
            return null;
        }

        if ( repeat <= 0 ) {
            return str;
        }

        int length = str.trim().length();
        if ( repeat == 1 || length == 0 ) {
            return str;
        }
        // 暂时还不知道判断边界为什么是8192
        if ( length == 1 && repeat <= PAD_LIMIT ) {
            // 单个字符时，则调用字符重复方法
            return repeat(str.charAt(0), repeat);
        }

        int outputLength = length * repeat;
        switch (length) {
            case 2:
                char ch0 = str.charAt(0);
                char ch1 = str.charAt(1);
                char[] output2 = new char[outputLength];
                for (int i = repeat * 2 - 2; i >= 0; i--, i--) {
                    output2[i] = ch0;
                    output2[i + 1] = ch1;
                }
                return new String(output2);
            default:
                // 字符串超过2个字符时
                StringBuilder buf = new StringBuilder(outputLength);
                for (int i = 0; i < repeat; i++) {
                    buf.append(str);
                }
                return buf.toString();
        }
    }

    /**
     * <p>
     * 功能：将一个字符串重复N次，并且中间加上指定的分隔符
     * </p>
     * 例如：
     * <pre>
     * StringUtils.repeat(null, null, 2) = null
     * StringUtils.repeat(null, "x", 2)  = null
     * StringUtils.repeat("", null, 0)   = ""
     * StringUtils.repeat("", "", 2)     = ""
     * StringUtils.repeat("", "x", 3)    = "xxx"
     * StringUtils.repeat("?", ", ", 3)  = "?, ?, ?"
     * </pre>
     * @param str 源字符串
     * @param separator 分隔符
     * @param repeat 重复次数(必须大于0)
     * @return 处理后的String字符串
     */
    public static String repeat(String str, String separator, int repeat) {
        if (str == null || separator == null) {
            return repeat(str, repeat);
        } else {
            // given that repeat(String, int) is quite optimized, better to rely
            // on it than try and splice this into it
            String result = repeat(str + separator, repeat);
            // 移除掉最后多余的separator符号
            return removeEnd(result, separator);
        }
    }

    /**
     * <p>
     * 功能：检查字符串是否全部为小写.
     * </p>
     * 例如：
     * <pre>
     * StringUtils.isAllLowerCase(null)   = false
     * StringUtils.isAllLowerCase("")     = false
     * StringUtils.isAllLowerCase("  ")   = false
     * StringUtils.isAllLowerCase("abc")  = true
     * StringUtils.isAllLowerCase("abC") = false
     * </pre>
     * @param cs 源字符串
     * @return boolean true则是全部为小写，false表示有非小写
     */
    public static boolean isAllLowerCase(String cs) {
        if ( isBlank(cs)) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isLowerCase(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>
     * 功能：检查是否都是大写.
     * </p>
     * 例如：
     * <pre>
     * StringUtils.isAllUpperCase(null)   = false
     * StringUtils.isAllUpperCase("")     = false
     * StringUtils.isAllUpperCase("  ")   = false
     * StringUtils.isAllUpperCase("ABC")  = true
     * StringUtils.isAllUpperCase("aBC") = false
     * </pre>
     * @param cs 源字符串
     * @return boolean true则是全部为小写，false表示有非大写
     */
    public static boolean isAllUpperCase(String cs) {
        if ( isBlank(cs) ) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isUpperCase(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>
     * 功能：切换字符串中的所有字母大小写。<br/>
     * </p>
     * 例如：
     * <pre>
     * StringUtils.swapCase(null)                 = null
     * StringUtils.swapCase("")                   = ""
     * StringUtils.swapCase("The dog has a BONE") = "tHE DOG HAS A bone"
     * </pre>
     * @param str  源字符串
     * @return 处理后的String字符串
     */
    public static String swapCase(String str) {
        if ( isBlank(str) ) {
            return str;
        }
        char[] buffer = str.toCharArray();

        boolean whitespace = true;

        for (int i = 0; i < buffer.length; i++) {
            char ch = buffer[i];
            if (Character.isUpperCase(ch)) {
                buffer[i] = Character.toLowerCase(ch);
                whitespace = false;
            } else if (Character.isTitleCase(ch)) {
                buffer[i] = Character.toLowerCase(ch);
                whitespace = false;
            } else if (Character.isLowerCase(ch)) {
                if (whitespace) {
                    buffer[i] = Character.toTitleCase(ch);
                    whitespace = false;
                } else {
                    buffer[i] = Character.toUpperCase(ch);
                }
            } else {
                whitespace = Character.isWhitespace(ch);
            }
        }
        return new String(buffer);
    }

    /**
     * <p>
     * 功能：反转字符串.
     * </p>
     * 例如：
     * <pre>
     * StringUtils.reverse(null)  = null
     * StringUtils.reverse("")    = ""
     * StringUtils.reverse("bat") = "tab"
     * </pre>
     * @param str 源字符串
     * @return 处理后的String字符串
     */
    public static String reverse(String str) {
        if ( isBlank(str)) {
            return null;
        }
        return new StringBuilder(str).reverse().toString();
    }

    /**
     * <p>
     * 功能：截取一个字符串的前几个.
     * </p>
     * 例如：
     * <pre>
     * StringUtils.left(null, *)    = null
     * StringUtils.left(*, -ve)     = ""
     * StringUtils.left("", *)      = ""
     * StringUtils.left("abc", 0)   = ""
     * StringUtils.left("abc", 2)   = "ab"
     * StringUtils.left("abc", 4)   = "abc"
     * </pre>
     * @param str 源字符串
     * @param len 截取的长度
     * @return 处理后的String字符串
     */
    public static String left(String str, int len) {
        if ( isBlank(str) ) {
            return null;
        }
        if ( len < 0) {
            return str;
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(0, len);
    }

    /**
     * 功能：如果字符串没有超过最长显示长度返回原字符串，否则从开头截取指定长度并加...返回。
     * @param str 原字符串
     * @param length 字符串最长显示的长度
     * @return 转换后的字符串
     */
    public static String trimString(String str, int length) {
        if ( isBlank(str)) {
            return str;
        }
        if ( str.length() > length ) {
            return str.substring(0, length - 3) + "...";
        } else {
            return str;
        }
    }

    /**
     * 功能：截取出第一个标志位之前的字符串.<br/>
     * 如果sourceStr为empty或者expr为null，直接返回源字符串。<br/>
     * 如果expr长度为0，直接返回sourceStr。<br/>
     * 如果expr在sourceStr中不存在，直接返回sourceStr。<br/>
     * 如果expr在sourceStr中存在不止一个，以第一个位置为准。
     * @param sourceStr 被截取的字符串
     * @param expr 分隔符
     * @return String
     */
    public static String substringBefore(String sourceStr, String expr) {
        // 这里expr不做空格判断，因为可能截取的就是空格之前的字符串呢
        if (isBlank(sourceStr) || expr == null) {
            return sourceStr;
        }
        if (expr.length() == 0) {
            return sourceStr;
        }
        int pos = sourceStr.indexOf(expr);
        if (pos == INDEX_NOT_FOUND) {
            return sourceStr;
        }
        return sourceStr.substring(0, pos);
    }

    /**
     * 功能：截取出第一个标志位之后的字符串.<br/>
     * 如果sourceStr为empty或者expr为null，直接返回源字符串。<br/>
     * 如果expr长度为0，直接返回sourceStr。<br/>
     * 如果expr在sourceStr中不存在，直接返回sourceStr。<br/>
     * @param sourceStr 被截取的字符串
     * @param expr 分隔符
     * @return String
     */
    public static String substringAfter(String sourceStr, String expr) {
        // 这里expr不做空格判断，因为可能截取的就是空格之前的字符串呢
        if (isBlank(sourceStr) || expr == null) {
            return sourceStr;
        }
        if (expr.length() == 0) {
            return sourceStr;
        }

        int pos = sourceStr.indexOf(expr);
        if (pos == INDEX_NOT_FOUND) {
            return sourceStr;
        }
        return sourceStr.substring(pos + expr.length());
    }

    /**
     * 功能：截取出最后一个标志位之前的字符串.<br/>
     * 如果sourceStr为empty或者expr为null，直接返回源字符串。<br/>
     * 如果expr长度为0，直接返回sourceStr。<br/>
     * 如果expr在sourceStr中不存在，直接返回sourceStr。<br/>
     * @param sourceStr 被截取的字符串
     * @param expr 分隔符
     * @return String
     */
    public static String substringBeforeLast(String sourceStr, String expr) {
        // 这里expr不做空格判断，因为可能截取的就是空格之前的字符串呢
        if (isBlank(sourceStr) || expr == null) {
            return sourceStr;
        }
        if (expr.length() == 0) {
            return sourceStr;
        }
        int pos = sourceStr.lastIndexOf(expr);
        if (pos == INDEX_NOT_FOUND) {
            return sourceStr;
        }
        return sourceStr.substring(0, pos);
    }

    /**
     * 功能：截取出最后一个标志位之后的字符串.<br/>
     * 如果sourceStr为empty或者expr为null，直接返回源字符串。<br/>
     * 如果expr长度为0，直接返回sourceStr。<br/>
     * 如果expr在sourceStr中不存在，直接返回sourceStr。<br/>
     * @param sourceStr 被截取的字符串
     * @param expr 分隔符
     * @return String
     */
    public static String substringAfterLast(String sourceStr, String expr) {
        if (isBlank(sourceStr) || expr == null) {
            return sourceStr;
        }
        if (expr.length() == 0) {
            return sourceStr;
        }

        int pos = sourceStr.lastIndexOf(expr);
        if (pos == INDEX_NOT_FOUND) {
            return sourceStr;
        }
        return sourceStr.substring(pos + expr.length());
    }

    /**
     * <p>
     * 得到两个字符串中间的子字符串，只返回第一个匹配项。
     * </p>
     * 例如：
     * <pre>
     * StringUtils.substringBetween("wx[b]yz", "[", "]") = "b"
     * StringUtils.substringBetween(null, *, *)          = null
     * StringUtils.substringBetween(*, null, *)          = null
     * StringUtils.substringBetween(*, *, null)          = null
     * StringUtils.substringBetween("", "", "")          = ""
     * StringUtils.substringBetween("", "", "]")         = null
     * StringUtils.substringBetween("", "[", "]")        = null
     * StringUtils.substringBetween("yabcz", "", "")     = ""
     * StringUtils.substringBetween("yabcz", "y", "z")   = "abc"
     * StringUtils.substringBetween("yabczyabcz", "y", "z")   = "abc"
     * </pre>
     * @param str 源字符串
     * @param open 起字符串。
     * @param close 末字符串。
     * @return String 子字符串, 如果没有符合要求的，返回null。
     */
    public static String substringBetween(String str, String open, String close) {
        if ( null == str || open == null || close == null) {
            return null;
        }
        int start = str.indexOf(open);
        if (start != INDEX_NOT_FOUND) {
            int end = str.indexOf(close, start + open.length());
            if (end != INDEX_NOT_FOUND) {
                return str.substring(start + open.length(), end);
            }
        }
        return null;
    }

    /**
     * <p>
     * 功能：得到两个字符串中间的子字符串，所有匹配项组合为数组并返回。
     * </p>
     * 例如：
     * <pre>
     * StringUtils.substringsBetween("[a][b][c]", "[", "]") = ["a","b","c"]
     * StringUtils.substringsBetween(null, *, *)            = null
     * StringUtils.substringsBetween(*, null, *)            = null
     * StringUtils.substringsBetween(*, *, null)            = null
     * StringUtils.substringsBetween("", "[", "]")          = []
     * </pre>
     * @param str  源字符串
     * @param open 起字符串。
     * @param close 末字符串。
     * @return String 子字符串数组, 如果没有符合要求的，返回null。
     */
    public static String[] substringsBetween(String str, String open, String close) {
        if (str == null || isEmpty(open) || isEmpty(close)) {
            return null;
        }
        int strLen = str.length();
        if (strLen == 0) {
            return new String[0];
        }
        int closeLen = close.length();
        int openLen = open.length();
        List<String> list = new ArrayList<String>();
        int pos = 0;
        while (pos < strLen - closeLen) {
            int start = str.indexOf(open, pos);
            if (start < 0) {
                break;
            }
            start += openLen;
            int end = str.indexOf(close, start);
            if (end < 0) {
                break;
            }
            list.add(str.substring(start, end));
            pos = end + closeLen;
        }
        if (list.isEmpty()) {
            return null;
        }
        return list.toArray(new String[list.size()]);
    }

    /**
     * <p>
     * 功能：得到tag字符串中间的子字符串，只返回第一个匹配项。
     * </p>
     * 例如：
     * <pre>
     * StringUtil.substringBetween(null, *)            = null
     * StringUtil.substringBetween("", "")             = ""
     * StringUtil.substringBetween("", "tag")          = null
     * StringUtil.substringBetween("tagabctag", null)  = null
     * StringUtil.substringBetween("tagabctag", "")    = ""
     * StringUtil.substringBetween("tagabctag", "tag") = "abc"
     * </pre>
     * @param str 源字符串。
     * @param tag 标识字符串。
     * @return String 子字符串, 如果没有符合要求的，返回{@code null}。
     */
    public static String substringBetween(String str, String tag) {
        return substringBetween(str, tag, tag);
    }

    /**
     * <p>
     * 功能：字符串长度达不到指定长度时，在字符串右边补指定的字符.
     * </p>
     * 例如：
     * <pre>
     * StringUtil.rightPad(null, *, *)     = null
     * StringUtil.rightPad("", 3, 'z')     = "zzz"
     * StringUtil.rightPad("bat", 3, 'z')  = "bat"
     * StringUtil.rightPad("bat", 5, 'z')  = "batzz"
     * StringUtil.rightPad("bat", 1, 'z')  = "bat"
     * StringUtil.rightPad("bat", -1, 'z') = "bat"
     * </pre>
     * @param str 源字符串
     * @param size 指定的长度
     * @param padChar 进行补充的字符
     * @return String
     */
    public static String rightPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (pads > PAD_LIMIT) {
            return rightPad(str, size, String.valueOf(padChar));
        }
        return str.concat(repeat(padChar, pads));
    }

    /**
     * <p>
     * 功能：扩大字符串长度，从左边补充指定字符
     * </p>
     * 例如：
     * <pre>
     * StringUtil.rightPad(null, *, *)      = null
     * StringUtil.rightPad("", 3, "z")      = "zzz"
     * StringUtil.rightPad("bat", 3, "yz")  = "bat"
     * StringUtil.rightPad("bat", 5, "yz")  = "batyz"
     * StringUtil.rightPad("bat", 8, "yz")  = "batyzyzy"
     * StringUtil.rightPad("bat", 1, "yz")  = "bat"
     * StringUtil.rightPad("bat", -1, "yz") = "bat"
     * StringUtil.rightPad("bat", 5, null)  = "bat  "
     * StringUtil.rightPad("bat", 5, "")    = "bat  "
     * </pre>
     * @param str 源字符串
     * @param size 扩大后的长度
     * @param padStr 在右边补充的字符串
     * @return String
     */
    public static String rightPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = " ";
        }
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (padLen == 1 && pads <= PAD_LIMIT) {
            return rightPad(str, size, padStr.charAt(0));
        }

        if (pads == padLen) {
            return str.concat(padStr);
        } else if (pads < padLen) {
            return str.concat(padStr.substring(0, pads));
        } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return str.concat(new String(padding));
        }
    }

    /**
     * <p>
     * 功能：扩大字符串长度，从左边补充空格
     * </p>
     * 例如：
     * <pre>
     * StringUtil.leftPad(null, *)   = null
     * StringUtil.leftPad("", 3)     = "   "
     * StringUtil.leftPad("bat", 3)  = "bat"
     * StringUtil.leftPad("bat", 5)  = "  bat"
     * StringUtil.leftPad("bat", 1)  = "bat"
     * StringUtil.leftPad("bat", -1) = "bat"
     * </pre>
     * @param str 源字符串
     * @param size 扩大后的长度
     * @return String
     */
    public static String leftPad(String str, int size) {

        return leftPad(str, size, ' ');
    }

    /**
     * <p>
     * 功能：扩大字符串长度，从左边补充指定的字符
     * </p>
     * 例如：
     * <pre>
     * StringUtil.leftPad(null, *, *)     = null
     * StringUtil.leftPad("", 3, 'z')     = "zzz"
     * StringUtil.leftPad("bat", 3, 'z')  = "bat"
     * StringUtil.leftPad("bat", 5, 'z')  = "zzbat"
     * StringUtil.leftPad("bat", 1, 'z')  = "bat"
     * StringUtil.leftPad("bat", -1, 'z') = "bat"
     * </pre>
     * @param str 源字符串
     * @param size 扩大后的长度
     * @param padChar 补充的字符
     * @return String
     */
    public static String leftPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (pads > PAD_LIMIT) {
            return leftPad(str, size, String.valueOf(padChar));
        }
        return repeat(padChar, pads).concat(str);
    }

    /**
     * <p>
     * 功能：扩大字符串长度，从左边补充指定的字符
     * </p>
     * 例如：
     * <pre>
     * StringUtil.leftPad(null, *, *)      = null
     * StringUtil.leftPad("", 3, "z")      = "zzz"
     * StringUtil.leftPad("bat", 3, "yz")  = "bat"
     * StringUtil.leftPad("bat", 5, "yz")  = "yzbat"
     * StringUtil.leftPad("bat", 8, "yz")  = "yzyzybat"
     * StringUtil.leftPad("bat", 1, "yz")  = "bat"
     * StringUtil.leftPad("bat", -1, "yz") = "bat"
     * StringUtil.leftPad("bat", 5, null)  = "  bat"
     * StringUtil.leftPad("bat", 5, "")    = "  bat"
     * </pre>
     * @param str 源字符串
     * @param size 扩大后的长度
     * @param padStr 补充的字符串
     * @return String
     */
    public static String leftPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = " ";
        }
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (padLen == 1 && pads <= PAD_LIMIT) {
            return leftPad(str, size, padStr.charAt(0));
        }

        if (pads == padLen) {
            return padStr.concat(str);
        } else if (pads < padLen) {
            return padStr.substring(0, pads).concat(str);
        } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return new String(padding).concat(str);
        }
    }

    /**
     * <p>
     * 功能：扩大字符串长度并将现在的字符串居中，被扩大部分用空格填充。
     * <p>
     * 例如：
     * <pre>
     * StringUtil.center(null, *)   = null
     * StringUtil.center("", 4)     = "    "
     * StringUtil.center("ab", -1)  = "ab"
     * StringUtil.center("ab", 4)   = " ab "
     * StringUtil.center("abcd", 2) = "abcd"
     * StringUtil.center("a", 4)    = " a  "
     * </pre>
     * @param str 源字符串
     * @param size 扩大后的长度
     * @return String
     */
    public static String center(String str, int size) {

        return center(str, size, ' ');
    }

    /**
     * <p>
     * 功能：将字符串长度修改为指定长度，并进行居中显示。
     * </p>
     * 例如：
     * <pre>
     * StringUtil.center(null, *, *)     = null
     * StringUtil.center("", 4, ' ')     = "    "
     * StringUtil.center("ab", -1, ' ')  = "ab"
     * StringUtil.center("ab", 4, ' ')   = " ab"
     * StringUtil.center("abcd", 2, ' ') = "abcd"
     * StringUtil.center("a", 4, ' ')    = " a  "
     * StringUtil.center("a", 4, 'y')    = "yayy"
     * </pre>
     * @param str 源字符串
     * @param size 指定的长度
     * @param padChar 长度不够时补充的字符串
     * @return String
     */
    public static String center(String str, int size, char padChar) {
        if (str == null || size <= 0) {
            return str;
        }
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str;
        }
        str = leftPad(str, strLen + pads / 2, padChar);
        str = rightPad(str, size, padChar);
        return str;
    }

    /**
     * <p>
     * 功能：将字符串长度修改为指定长度，并进行居中显示。
     * </p>
     * 例如：
     * <pre>
     * StringUtil.center(null, *, *)     = null
     * StringUtil.center("", 4, " ")     = "    "
     * StringUtil.center("ab", -1, " ")  = "ab"
     * StringUtil.center("ab", 4, " ")   = " ab"
     * StringUtil.center("abcd", 2, " ") = "abcd"
     * StringUtil.center("a", 4, " ")    = " a  "
     * StringUtil.center("a", 4, "yz")   = "yayz"
     * StringUtil.center("abc", 7, null) = "  abc  "
     * StringUtil.center("abc", 7, "")   = "  abc  "
     * </pre>
     * @param str 源字符串
     * @param size 指定的长度
     * @param padStr 长度不够时补充的字符串
     * @return String
     */
    public static String center(String str, int size, String padStr) {
        if (str == null || size <= 0) {
            return str;
        }
        if (isEmpty(padStr)) {
            padStr = " ";
        }
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str;
        }
        str = leftPad(str, strLen + pads / 2, padStr);
        str = rightPad(str, size, padStr);
        return str;
    }

    /**
     * <p>
     * 功能：字符串达不到一定长度时在右边补空白.
     * </p>
     * 例如：
     * <pre>
     * StringUtil.rightPad(null, *)   = null
     * StringUtil.rightPad("", 3)     = "   "
     * StringUtil.rightPad("bat", 3)  = "bat"
     * StringUtil.rightPad("bat", 5)  = "bat  "
     * StringUtil.rightPad("bat", 1)  = "bat"
     * StringUtil.rightPad("bat", -1) = "bat"
     * </pre>
     * @param str 源字符串
     * @param size 指定的长度
     * @return String
     */
    public static String rightPad(String str, int size) {

        return rightPad(str, size, ' ');
    }

    /**
     * <p>
     * 功能：从右边截取字符串.
     * </p>
     * 例如：
     * <pre>
     * StringUtil.right(null, *)    = null
     * StringUtil.right(*, -ve)     = ""
     * StringUtil.right("", *)      = ""
     * StringUtil.right("abc", 0)   = ""
     * StringUtil.right("abc", 2)   = "bc"
     * StringUtil.right("abc", 4)   = "abc"
     * </pre>
     * @param str 源字符串
     * @param len 长度
     * @return String
     */
    public static String right(String str, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return EMPTY_STRING;
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(str.length() - len);
    }

    /**
     * 功能：cs串中是否一个都不包含字符数组searchChars中的字符。
     * @param cs 字符串
     * @param searchChars 字符数组
     * @return boolean 都不包含返回true，否则返回false。
     */
    public static boolean containsNone(CharSequence cs, char... searchChars) {
        if (cs == null || searchChars == null) {
            return true;
        }
        int csLen = cs.length();
        int csLast = csLen - 1;
        int searchLen = searchChars.length;
        int searchLast = searchLen - 1;
        for (int i = 0; i < csLen; i++) {
            char ch = cs.charAt(i);
            for (int j = 0; j < searchLen; j++) {
                if (searchChars[j] == ch) {
                    if (Character.isHighSurrogate(ch)) {
                        if (j == searchLast) {
                            // missing low surrogate, fine, like
                            // String.indexOf(String)
                            return false;
                        }
                        if (i < csLast
                                && searchChars[j + 1] == cs.charAt(i + 1)) {
                            return false;
                        }
                    } else {
                        // ch is in the Basic Multilingual Plane
                        return false;
                    }
                }
            }
        }
        return true;
    }


    /**
     * <p>
     * 功能：编码为Unicode，格式 '\u0020'.
     * </p>
     * 例如：
     * <pre>
     *   CharUtils.unicodeEscaped(' ') = "\u0020"
     *   CharUtils.unicodeEscaped('A') = "\u0041"
     * </pre>
     * @param ch  源字符串
     * @return 转码后的字符串
     */
    public static String unicodeEscaped(char ch) {
        if (ch < 0x10) {
            return "\\u000" + Integer.toHexString(ch);
        } else if (ch < 0x100) {
            return "\\u00" + Integer.toHexString(ch);
        } else if (ch < 0x1000) {
            return "\\u0" + Integer.toHexString(ch);
        }
        return "\\u" + Integer.toHexString(ch);
    }

    /**
     * <p>
     * 功能：进行tostring操作，如果传入的是null，返回空字符串。
     * </p>
     * <pre>
     * ObjectUtils.toString(null)         = ""
     * ObjectUtils.toString("")           = ""
     * ObjectUtils.toString("bat")        = "bat"
     * ObjectUtils.toString(Boolean.TRUE) = "true"
     * </pre>
     * @param obj  源
     * @return String
     */
    public static String toString(Object obj) {

        return obj == null ? EMPTY_STRING : obj.toString();
    }

    /**
     * <p>
     * 功能：进行tostring操作，如果传入的是null，返回指定的默认值。
     * </p>
     * <pre>
     * ObjectUtils.toString(null, null)           = null
     * ObjectUtils.toString(null, "null")         = "null"
     * ObjectUtils.toString("", "null")           = ""
     * ObjectUtils.toString("bat", "null")        = "bat"
     * ObjectUtils.toString(Boolean.TRUE, "null") = "true"
     * </pre>
     * @param obj 源
     * @param nullStr 如果obj为null时返回这个指定值
     * @return String
     */
    public static String toString(Object obj, String nullStr) {

        return obj == null ? nullStr : obj.toString();
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
