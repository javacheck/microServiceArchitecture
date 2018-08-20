package cn.self.cloud.commonutils.simple;

/**
 * 字符串的重复性检查器，对数据进行重复判断，检查器中的数据只保存24小时
 * Created by ChenPing on 16-1-26.
 */
public interface StringRepeatChecker {
    /***
     * 检查指定的字符串是否为重复，如果不是重复的数据，则添加到检查器中，如果是重复的，则覆盖旧的数据，同时返回旧数据
     * @param key
     * @return
     */
    String check(String key, String value);
}
