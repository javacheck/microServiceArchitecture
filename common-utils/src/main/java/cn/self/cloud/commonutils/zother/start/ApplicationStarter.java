package cn.self.cloud.commonutils.zother.start;

import org.redkale.boot.Application;
import org.redkale.util.ResourceFactory;

import java.util.logging.Logger;

/**
 * Created by cp on 2017/7/27.
 */
public class ApplicationStarter {
    Logger logger=Logger.getLogger(getClass().getCanonicalName());
    public void start(String[] args) throws Exception {
        logger.info("应用服务器正在启动...");
   /*     ConfigHelper configHelper = ConfigHelper.getInstance();
        boolean isLocal = Boolean.parseBoolean(configHelper.get("runAsLocalService"));
        if (isLocal) {
            logger.info("以本地模式运行应用..");
            System.setProperty(Application.RESNAME_APP_HOME, configHelper.get("localModuleDirName"));
        }else{
            logger.info("以服务器模式运行应用..");
        } */
        Application.main(args);
        logger.info("服务器启动完成.");
    }
}
