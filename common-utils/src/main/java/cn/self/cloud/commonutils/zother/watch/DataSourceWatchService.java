package cn.self.cloud.commonutils.zother.watch;

import cn.self.cloud.commonutils.zother.config.ConfigManager;
import cn.self.cloud.commonutils.zother.config.Config;
import cn.self.cloud.commonutils.zother.source.MyJdbcDataSource;
import org.apache.commons.lang.StringUtils;
import org.redkale.boot.Application;
import org.redkale.boot.watch.AbstractWatchService;
import org.redkale.net.http.RestMapping;
import org.redkale.net.http.RestService;
import org.redkale.source.DataSource;
import org.redkale.source.DataSources;
import org.redkale.util.AnyValue;
import org.redkale.util.ResourceFactory;

import javax.annotation.Resource;
import java.util.List;
import java.util.Properties;

/**
 * 本来想用来做watch注入数据库配置的机制，
 * 后来发现MyJdbcDataSource直接可用，所以弃用此类
 * 在注释掉 test 方法时，不会被自动加载
 * Created by cp on 2017/11/29.
 */
@RestService(name = "dsource", catalog = "watch", repair = false)
public class DataSourceWatchService extends AbstractWatchService {

    @Resource(name = "property.config.mode")
    String configMode;

    @Resource(name = "property.zookeeper.config")
    String zkConfig;

    @Resource
    Application application;


    @Override
    public void init(AnyValue config) {
        ResourceFactory rf = application.getResourceFactory();
        List<DataSource> dataSources = rf.query(DataSource.class);
        for (DataSource dataSource : dataSources) {
            MyJdbcDataSource mySqlDataSource = (MyJdbcDataSource) dataSource;
            if (StringUtils.isNotEmpty(mySqlDataSource.getName())) { //必须指定了数据库名称才进行连接的初始化
                Properties dbProperties;
                if (configMode.equalsIgnoreCase("local")) {
                    dbProperties = getLocalDBProperties(mySqlDataSource.getName());
                } else {
                    Config remoteConfig = ConfigManager.getConfig("zookeeperdb", mySqlDataSource.getName(), zkConfig);
                    if (remoteConfig == null || remoteConfig.getKeys().isEmpty()) {
                        throw new RuntimeException("获取远程数据库配置失败");
                    }
                    dbProperties = getRemoteDbProperties(remoteConfig);
                }
                mySqlDataSource.initJdbcConfig(dbProperties, dbProperties);
            }
        }
    /*    List<NodeServer> nodeServerList = application.getNodeServers();
        for(NodeServer server:nodeServerList){
            if(!server.isSNCP() && !server.isWATCH()){
                List<DataSource> a=server.getResourceFactory().query(DataSource.class);
                Set<Service> serviceList=server.getLocalServices();
                for(Service service:serviceList){
                     Class cls=service.getClass();
                     if(hasFieldOfType(cls,DataSource.class)){
                         server.getResourceFactory().inject(mySqlDataSource,service);
                     }
                }
            }
        }
        */
    }

    private Properties getRemoteDbProperties(Config remoteConfig) {
        Properties properties = new Properties();
        if (remoteConfig == null) {
            return properties;
        }
        properties.put(DataSources.JDBC_DRIVER, "com.mysql.jdbc.Driver");
        properties.put(DataSources.JDBC_URL, String.format("jdbc:mysql://%s:%d/%s?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull",
                remoteConfig.getString("host"), remoteConfig.getInt("port"), remoteConfig.getString("dbname")));
        properties.put(DataSources.JDBC_USER, remoteConfig.getString("username"));
        properties.put(DataSources.JDBC_PWD, remoteConfig.getString("password"));
        return properties;
    }

    private Properties getLocalDBProperties(String dbname) {
        Properties properties = new Properties();
        ConfigHelper helper = ConfigHelper.getInstance();
        properties.put(DataSources.JDBC_DRIVER, helper.get(dbname + "." + DataSources.JDBC_DRIVER));
        properties.put(DataSources.JDBC_URL, helper.get(dbname + "." + DataSources.JDBC_URL));
        properties.put(DataSources.JDBC_USER, helper.get(dbname + "." + DataSources.JDBC_USER));
        properties.put(DataSources.JDBC_PWD, helper.get(dbname + "." + DataSources.JDBC_PWD));
        return properties;
    }
/*
    @RestMapping(name="test")
    public String test(){
        return "test";
    }
*/
}
