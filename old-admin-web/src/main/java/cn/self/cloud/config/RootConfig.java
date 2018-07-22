package cn.self.cloud.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.sql.DataSource;
import cn.self.cloud.commonutils.cache.CacheService;
import cn.self.cloud.commonutils.cache.RedisCacheService;
import cn.self.cloud.commonutils.id.IdService;
import cn.self.cloud.commonutils.id.MysqlIdService;
import cn.self.cloud.commonutils.properties.ConfigUtils;
import jodd.util.ClassLoaderUtil;
import jodd.util.StringUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import redis.clients.jedis.JedisPoolConfig;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

@Configuration
@ComponentScan(basePackages = { "cn.self.cloud" }, excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class),
		@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class) })
@EnableTransactionManagement(proxyTargetClass = true)
@EnableScheduling
public class RootConfig implements SchedulingConfigurer{
	private final static Logger logger = LoggerFactory
			.getLogger(RootConfig.class);
	static {
		logger.debug("web app on start up >>>>>>>>>>>>>>>>>加载配置文件");
		// 加载配置文件
		try {
			String filename = "common.properties";
			ConfigUtils.load(ClassLoaderUtil.getResourceAsStream(filename));
			String configDir = ConfigUtils.getProperty("config.dir");
			if (StringUtil.isNotBlank(configDir)) {
				File file = new File(configDir + filename);
				if (file.exists()) {
					ConfigUtils.clearAndLoad(new FileInputStream(file));
					logger.debug(">>>>>>>>>>>>>>>>>加载目录配置文件");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Bean
	public DataSource dataSource() {
		BasicDataSource source = new BasicDataSource();
		source.setDriverClassName(ConfigUtils.getProperty("jdbc.driver"));
		source.setUrl(ConfigUtils.getProperty("jdbc.url"));
		source.setUsername(ConfigUtils.getProperty("jdbc.username"));
		source.setPassword(ConfigUtils.getProperty("jdbc.password"));
		return source;
	}

	@Bean
	@Autowired
	public PlatformTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean
	@Autowired
	public JdbcTemplate jdbcTemplate(@Qualifier("dataSource")DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	@Autowired
	public IdService idService(JdbcTemplate jdbcTemplate) {
		return new MysqlIdService(jdbcTemplate);
	}

//	@Bean
//	@Autowired
//	public FileService fileService() {
//
//		return new SimpleFileService(ConfigUtils.getProperty("file.storePath"));
//	}

	/**
	 * kaptcha.border.color 边框颜色 默认为Color.BLACK kaptcha.border.thickness 边框粗细度
	 * 默认为1 kaptcha.producer.impl 验证码生成器 默认为DefaultKaptcha
	 * kaptcha.textproducer.impl 验证码文本生成器 默认为DefaultTextCreator
	 * kaptcha.textproducer.char.string 验证码文本字符内容范围 默认为abcde2345678gfynmnpwx
	 * kaptcha.textproducer.char.length 验证码文本字符长度 默认为5
	 * kaptcha.textproducer.font.names 验证码文本字体样式 默认为new Font("Arial", 1,
	 * fontSize), new Font("Courier", 1, fontSize)
	 * kaptcha.textproducer.font.size 验证码文本字符大小 默认为40
	 * kaptcha.textproducer.font.color 验证码文本字符颜色 默认为Color.BLACK
	 * kaptcha.textproducer.char.space 验证码文本字符间距 默认为2 kaptcha.noise.impl
	 * 验证码噪点生成对象 默认为DefaultNoise kaptcha.noise.color 验证码噪点颜色 默认为Color.BLACK
	 * kaptcha.obscurificator.impl 验证码样式引擎 默认为WaterRipple kaptcha.word.impl
	 * 验证码文本字符渲染 默认为DefaultWordRenderer kaptcha.background.impl 验证码背景生成器
	 * 默认为DefaultBackground kaptcha.background.clear.from 验证码背景颜色渐进
	 * 默认为Color.LIGHT_GRAY kaptcha.background.clear.to 验证码背景颜色渐进 默认为Color.WHITE
	 * kaptcha.image.width 验证码图片宽度 默认为200 kaptcha.image.height 验证码图片高度 默认为50
	 */
	@Bean(name = "com.google.code.kaptcha.Producer")
	public Producer produce() {
		DefaultKaptcha producer = new DefaultKaptcha();
		Properties properties = new Properties();
		properties.setProperty("kaptcha.image.width", "120");
		properties.setProperty("kaptcha.image.height", "40");
		properties.setProperty("kaptcha.textproducer.char.length", "4");
		properties.setProperty("kaptcha.textproducer.font.size", "32");
		properties.setProperty("kaptcha.textproducer.char.string",
				"ABCDEFGHJKMNPRSTVWXabcde2345678gfmnprswx");

		Config config = new Config(properties);
		producer.setConfig(config);
		return producer;
	}

	@Bean
	public JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(ConfigUtils.getInteger("redis.pool.maxTotal"));
		poolConfig.setMaxIdle(ConfigUtils.getInteger("redis.pool.maxIdle"));
		poolConfig.setTestOnBorrow(ConfigUtils
				.getBoolean("redis.pool.testOnBorrow"));
		poolConfig.setMaxWaitMillis(ConfigUtils
				.getInteger("redis.pool.maxWait"));
		return poolConfig;
	}

	@Bean
	@Autowired
	public JedisConnectionFactory jedisConnectionFactory(
			JedisPoolConfig jedisPoolConfig) {
		JedisConnectionFactory connectionFactory = new JedisConnectionFactory(
				jedisPoolConfig);
		connectionFactory.setHostName(ConfigUtils.getProperty("redis.ip"));
		connectionFactory.setPort(ConfigUtils.getInteger("redis.port"));
		connectionFactory.setUsePool(true);
		return connectionFactory;
	}

	@Bean
	@Autowired
	public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(connectionFactory);
		return redisTemplate;
	}

	@Bean
	@Autowired
	public CacheService cacheService(RedisTemplate<String, Object> redisTemplate) {
		RedisCacheService cacheService = new RedisCacheService();
		cacheService.setRedisTemplate(redisTemplate);
		return cacheService;
	}
	//定时任务
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(taskExecutor());
	}

	@Bean
	public Executor taskExecutor() {
		return Executors.newScheduledThreadPool(100);
	}

}
