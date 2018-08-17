package cn.self.cloud.commonutils.zother.config;

import org.redkale.util.ResourceFactory;

/**
 * Created by cp on 2017/8/15.
 */
public class RedkaleConfigProvider implements ConfigProvider {

    private String name;
    private ResourceFactory resourceFactory = ResourceFactory.root();

    public RedkaleConfigProvider() {
    }

    public RedkaleConfigProvider(String name) {
        this.name = name;
    }

    @Override
    public Config getConfig() {
        return new RedkaleConfig(resourceFactory);
    }

    @Override
    public void checkAndRefreshConfig(Config config) {

    }
}
