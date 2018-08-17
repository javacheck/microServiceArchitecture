package cn.self.cloud.commonutils.zookeeper;

/**
 * Created by cp on 2017/7/8 .
 */
public class TreeNode {
    public static final String root = "/tj";
    public static final String module = root + "/" + "service";
    public static final String black = root + "/" + "serviceblack";
    public static final String config = root + "/" + "config";
    public static final String mysql = config + "/" + "mysql";
    public static final String redis = config + "/" + "redis";
    public static final String influxdb = config + "/" + "influxdb";
    public static final String mongodb = config + "/" + "mongodb";
}
