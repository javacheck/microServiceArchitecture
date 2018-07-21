/**
 * createDate : 2016年6月12日下午3:12:30
 */
package cn.self.cloud.commonutils.spell;

import cn.self.cloud.commonutils.basictype.StringUtils;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class SpellHelper {

    // 将中文转换为英文
    @Deprecated
    public static String getEname(String name) {
        HanyuPinyinOutputFormat pyFormat = new HanyuPinyinOutputFormat();
        pyFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        pyFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        pyFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

        try {
            return PinyinHelper.toHanyuPinyinString(name, pyFormat, "");
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return null;
    }

    // 姓、名的第一个字母需要为大写
    public static String getUpEname(String name) {
        char[] strs = name.toCharArray();
        String newName = null;

        // 名字的长度
        if (strs.length == 2) {
            newName = toUpCase(getEname("" + strs[0])) + " " + toUpCase(getEname("" + strs[1]));
        } else if (strs.length == 3) {
            newName = toUpCase(getEname("" + strs[0])) + " " + toUpCase(getEname("" + strs[1] + strs[2]));
        } else if (strs.length == 4) {
            newName = toUpCase(getEname("" + strs[0] + strs[1])) + " " + toUpCase(getEname("" + strs[2] + strs[3]));
        } else {
            newName = toUpCase(getEname(name));
        }

        return newName;
    }

    // 首字母大写
    private static String toUpCase(String str) {
        StringBuffer newstr = new StringBuffer();
        newstr.append((str.substring(0, 1)).toUpperCase()).append(str.substring(1, str.length()));

        return newstr.toString();
    }

    /**
     * 半角、全角字符判断
     *
     * @param c 字符
     * @return true：半角； false：全角
     */
    public static boolean isDbcCase(char c) {
        // 基本拉丁字母（即键盘上可见的，空格、数字、字母、符号）
        if (c >= 32 && c <= 127) {
            return true;
        }
        // 日文半角片假名和符号
        else if (c >= 65377 && c <= 65439) {
            return true;
        }
        return false;
    }

    /**
     * 字符串长度取得（区分半角、全角）
     *
     * @param str 字符串
     * @return 字符串长度
     */
    public static int getLength(String str) {
        int len = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (isDbcCase(c)) { // 半角
                len = len + 1;
            } else { // 全角
                len = len + 2;
            }
        }
        return len;
    }

    public static String getPingYin(String src, HanyuPinyinOutputFormat hanyuPinyinOutputFormat) {
        return getPingYin(src, hanyuPinyinOutputFormat, false);
    }

    public static String getPingYin(String src) {
        return getPingYin(src, null, true);
    }

    public static String getPingYin(String src, HanyuPinyinOutputFormat hanyuPinyinOutputFormat, boolean isFirstLetter) {
        if (StringUtils.isBlank(src)) {
            return null;
        }
        if (null == hanyuPinyinOutputFormat) {
            hanyuPinyinOutputFormat = new HanyuPinyinOutputFormat();
            hanyuPinyinOutputFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
            hanyuPinyinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            hanyuPinyinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        }
        char[] chars = src.toCharArray();
        StringBuilder linkString = new StringBuilder();
        for (char c : chars) {
            if (c > 127) {
                try {
                    String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c, hanyuPinyinOutputFormat);
                    if (null == pinyinArray) {
                        linkString.append(c);
                    } else {
                        if (isFirstLetter) {
                            linkString.append(pinyinArray[0].toCharArray()[0]);
                        } else {
                            linkString.append(pinyinArray[0].toCharArray());
                        }
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                linkString.append(c);
            }
        }
        return linkString.toString();
    }
}