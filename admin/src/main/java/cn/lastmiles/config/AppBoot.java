package cn.lastmiles.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import jodd.util.ClassLoaderUtil;
import jodd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import cn.lastmiles.common.utils.ConfigUtils;

@SpringBootApplication(
		exclude = {
		MultipartAutoConfiguration.class,
		DataSourceAutoConfiguration.class,
		FreeMarkerAutoConfiguration.class,
		RedisAutoConfiguration.class
		})
public class AppBoot extends SpringBootServletInitializer {
	private final static Logger logger = LoggerFactory.getLogger(AppBoot.class);

	static {
		// 加载配置文件
		try {
			String filename = "common.properties";
			ConfigUtils.load(ClassLoaderUtil.getResourceAsStream(filename));

			String osname = System.getProperty("os.name").toLowerCase();
			String configDir = ConfigUtils.getProperty("config.dir");
			if (osname.startsWith("win")) {
				configDir = ConfigUtils.getProperty("config.windows.dir");
			}
			logger.debug("config dir = {}", configDir);
			if (StringUtil.isNotBlank(configDir)) {
				File file = new File(configDir + File.separatorChar + filename);
				if (file.exists()) {
					logger.debug("file = {}", file.getAbsolutePath());
					ConfigUtils.clearAndLoad(new FileInputStream(file));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AppBoot.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(AppBoot.class, args);
	}
}
