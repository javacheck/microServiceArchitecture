package cn.self.cloud.commonutils.id;

import com.relops.snowflake.Snowflake;

/**
 * 唯一ID生成工具
 */
public class IdUtils {
    private static int node = NodeUtil.getNode();
    final static Snowflake snowflake = new Snowflake(node);

    public static Long getId() {
        return snowflake.next();
    }
}