package cn.lastmiles.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import jodd.util.ClassLoaderUtil;
import jodd.util.StringUtil;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import cn.lastmiles.common.service.FileService;
import cn.lastmiles.common.service.SimpleFileService;
import cn.lastmiles.common.utils.ConfigUtils;

@Configuration
@ComponentScan(basePackages = { "cn.lastmiles" }, excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class),
		@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class) })
@EnableTransactionManagement(proxyTargetClass = true)
// @EnableRedisHttpSession
public class RootConfig {
	static {
		// 加载配置文件
		try {
			String filename = "common.properties";
			ConfigUtils.load(ClassLoaderUtil.getResourceAsStream(filename));
			String configDir = ConfigUtils.getProperty("config.dir");
			if (StringUtil.isNotBlank(configDir)) {
				File file = new File(configDir + filename);
				if (file.exists()) {
					ConfigUtils.clearAndLoad(new FileInputStream(file));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Bean
	public FileService fileService() {
		SimpleFileService fileService = new SimpleFileService(
				ConfigUtils.getProperty("file.store.path"));
		return fileService;
	}
}
