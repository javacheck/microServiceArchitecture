package cn.self.cloud.commonutils.properties;

import cn.self.cloud.commonutils.validate.ValidateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Properties文件解析工具
 */
public class PropertiesUtils {
    private final static Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);

    public static Map<String, String> getPropertyMap(String propertiesFilePath) throws IOException {
        if (fileValidate(propertiesFilePath)) {
            Map<String, String> map = new HashMap<>();
            Properties properties = new Properties();

            File file = new File(propertiesFilePath);
            InputStream inputStream = new FileInputStream(file);
            properties.load(inputStream);

            Iterator<String> it = properties.stringPropertyNames().iterator();
            while (it.hasNext()) {
                String key = it.next();
                map.put(key, properties.getProperty(key));
            }
            inputStream.close();

            return map;
        } else {
            return null;
        }
    }


    /**
     * 获取配置文件
     */
    public static Properties getProperties(String propertiesFilePath) throws IOException {
        Properties properties = new Properties();
        if (fileValidate(propertiesFilePath)) {
            File file = new File(propertiesFilePath);
            InputStream inputStream = new FileInputStream(file);
            properties.load(inputStream);
            inputStream.close();

            return properties;
        } else {
            return null;
        }
    }

    /**
     * 文件合法性校验
     */
    private static boolean fileValidate(String propertiesFilePath) {
        if (ValidateUtils.isEmpty(propertiesFilePath)) {
            logger.error("文件路径为空");
            return false;
        }

        if (!propertiesFilePath.endsWith(".properties")) {
            logger.error("不是一个properties类型的文件");
            return false;
        }

        File file = new File(propertiesFilePath);
        if (!file.exists()) {
            logger.error("properties文件不存在");
            return false;
        }

        return true;
    }
}