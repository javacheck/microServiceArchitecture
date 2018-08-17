package cn.self.cloud.commonutils.zother.config;

/**
 * Created by cp on 2017/3/6.
 */
public class ConfigManager {
    /**
     * 根据指定的配置类型，获取配置信息
     *
     * @param type       1  本地配置，基于properties文件的配置，需要在src目录下存在配置文件
     *                   2  远程配置模式,微服务下，通过zookeeper管理配置信息
     * @param configName 如果是本地配置文件方式，则此参数传配置文件名称，
     *                   比如 配置文件是config.properties 则传 config
     *                   如果是远程配置文件的方式，则此参数传入参数为zookeeper相应的配置目录下的分类
     * @return
     */
    public static Config getConfig(int type, String configName) {
        if (type == 2) {
            return ConfigProviderFactory
                    .getConfigProvider(ConfigProviderFactory.ZookeeperConfigProvider, configName, "")
                    .getConfig();
        }
        return ConfigProviderFactory.getDefaultProvider(configName).getConfig();
    }

    /***
     * 根据指定的模式，获取对应的Config对象
     *
     * @param configMode local: 读取本地根目录下的config.properties  zookeeper:读取zookeeper中的配置 zookeeperdb: 读取zookeeper中的数据库配置
     * @param moduleName 模块名称
     * @param zkConfig   zookeeper服务器配置信息
     * @return Config对象
     */
    public static Config getConfig(String configMode, String moduleName, String zkConfig) {
        if (configMode.equalsIgnoreCase("redkale")) {
            return ConfigProviderFactory.getConfigProvider(ConfigProviderFactory.RedkaleConfigProvider, "default", "").getConfig();
        } else if (configMode.equalsIgnoreCase("local")) {
            return ConfigProviderFactory.getDefaultProvider("").getConfig();
        } else if (configMode.equalsIgnoreCase("zookeeper")) {
            return ConfigProviderFactory
                    .getConfigProvider(ConfigProviderFactory.ZookeeperConfigProvider, moduleName, zkConfig)
                    .getConfig();
        } else if (configMode.equalsIgnoreCase("zookeeperdb")) {
            return ConfigProviderFactory
                    .getConfigProvider(ConfigProviderFactory.ZookeeperDBConfigProvider, moduleName, zkConfig)
                    .getConfig();
        } else {
            return null;
        }
    }


    public static void main(String[] args) {
        System.out.println("...config test...");

        /**
         *  配置信息：  /tj/config/common/test = {"aa":1,"bb":2,"cc":"test config info","arr": ["xx","yy"],"ilist":[22,33,66,55,44,33]}
         *  config.properties:
         #start
         aa=1
         bb=2
         cc=test config info
         arr=xx,yy
         ilist=22,33,66,55,44,33
         #end
         */
//        Config config=getConfig(2,"test");
        Config config = getConfig(1, "");
        if (config == null)
            System.out.println("获取配置出错");
        else {
            System.out.println(config.getInt("aa"));
            System.out.println(config.getInt("bb"));
            System.out.println(config.getString("cc"));
            System.out.println(config.getStringList("arr"));
            System.out.println(config.getStringArrary("arr"));
            System.out.println(config.getIntList("ilist"));
            System.out.println(config.getKeys());
        }
        System.out.println("...config test end....");
    }
}
