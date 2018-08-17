package cn.self.cloud.commonutils.zother.source;

import cn.self.cloud.commonutils.zother.config.Config;
import cn.self.cloud.commonutils.zother.config.ConfigManager;
import org.apache.commons.lang.StringUtils;
import org.redkale.boot.Application;
import org.redkale.service.Service;
import org.redkale.source.DataJdbcSource;
import org.redkale.source.DataSource;
import org.redkale.source.DataSources;
import org.redkale.util.AnyValue;
import org.redkale.util.AutoLoad;
import org.redkale.util.ResourceFactory;

import javax.annotation.Resource;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 对高版本的dataSource进行支持，在初始化的时候进行数据库配置信息的获取
 * Created by cp on 2017/11/29.
 */
@AutoLoad(false)
public class MyJdbcDataSource extends DataJdbcSource implements Service {

    Logger logger = Logger.getLogger(getClass().getCanonicalName());
    @Resource(name = "property.config.mode")
    String configMode;

    @Resource(name = "property.zookeeper.config")
    String zkConfig;

    @Resource
    Application application;

    public MyJdbcDataSource(String unitName, Properties readprop, Properties writeprop) {
        super(unitName, readprop, writeprop);
    }

    public String getName() {
        return name;
    }

    @Override
    public void init(AnyValue config) {
        if (StringUtils.isNotEmpty(name)) { //必须指定了数据库名称才进行连接的初始化
            Properties dbProperties;
            String locate;
            if (configMode.equalsIgnoreCase("local") || configMode.equalsIgnoreCase("redkale")) {
                dbProperties = getLocalDBProperties(name);
                locate = "本地配置文件";
            } else {
                Config remoteConfig = ConfigManager.getConfig("zookeeperdb", name, zkConfig);
                if (remoteConfig == null || remoteConfig.getKeys().isEmpty()) {
                    throw new RuntimeException("获取远程数据库配置失败");
                }
                dbProperties = getRemoteDbProperties(remoteConfig);
                locate = "远程配置服务器";
            }
            logger.log(Level.INFO, "从" + locate + "获取到数据库配置信息：" + dbProperties.toString());
            this.readPool.change(dbProperties);
            this.writePool.change(dbProperties);
        } else {
            logger.log(Level.INFO, "没有配置要连接的数据库名");
        }
    }


    private Properties getRemoteDbProperties(Config remoteConfig) {
        Properties properties = new Properties();
        if (remoteConfig == null) {
            return properties;
        }
        properties.put(DataSources.JDBC_DRIVER, "com.mysql.jdbc.Driver");
        properties.put(DataSources.JDBC_URL, String.format("jdbc:mysql://%s:%d/%s?characterEncoding=utf8",
                remoteConfig.getString("host"), remoteConfig.getInt("port"), remoteConfig.getString("dbname")));
        properties.put(DataSources.JDBC_USER, remoteConfig.getString("username"));
        properties.put(DataSources.JDBC_PWD, remoteConfig.getString("password"));
        return properties;
    }

    private Properties getLocalDBProperties(String dbname) {
        Properties properties = new Properties();

        ResourceFactory factory = ResourceFactory.root();
        String driver = factory.find("property." + dbname + "." + DataSources.JDBC_DRIVER, String.class);
        String url = factory.find("property." + dbname + "." + DataSources.JDBC_URL, String.class);
        String user = factory.find("property." + dbname + "." + DataSources.JDBC_USER, String.class);
        String pass = factory.find("property." + dbname + "." + DataSources.JDBC_PWD, String.class);
        properties.put(DataSources.JDBC_DRIVER, driver);
        properties.put(DataSources.JDBC_URL, url);
        properties.put(DataSources.JDBC_USER, user);
        properties.put(DataSources.JDBC_PWD, pass);
        return properties;
    }

    public void initJdbcConfig(Properties readProterties, Properties writePropterties) {
        this.readPool.change(readProterties);
        this.writePool.change(writePropterties);
    }
}
