package cn.self.cloud.commonutils.zother.config;

/**
 * 配置信息服务提供者
 * Created by cp on 2017/3/6.
 */
public interface ConfigProvider {
    /***
     * 每隔1分钟进行配置信息的更新
     */
    public static final long ConfigRefreshInterval = 60000;

    /**
     * 获取配置信息对象
     *
     * @return
     */
    Config getConfig();

    /***
     * 为了实现定时刷新配置信息，在每次进行配置信息获取是，去检查配置信息是否刷新
     *
     * @param config
     */
    void checkAndRefreshConfig(Config config);
}
