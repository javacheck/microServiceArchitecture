package cn.lastmiles.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.transaction.PlatformTransactionManager;

import redis.clients.jedis.JedisPoolConfig;
import cn.lastmiles.cache.CacheService;
import cn.lastmiles.cache.RedisCacheService;
import cn.lastmiles.common.service.FileService;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.service.RedisIdService;
import cn.lastmiles.common.utils.ConfigUtils;
import cn.lastmiles.common.utils.JdbcUtils;
import cn.lastmiles.ufile.UFileService;

@Configuration
public class BaseConfig  implements SchedulingConfigurer {
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
	public PlatformTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean
	@Autowired
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
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
	public RedisTemplate<String, Object> redisTemplate(
			JedisConnectionFactory connectionFactory) {
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

//	@Bean
//	@Autowired
//	public IdService idService(JdbcTemplate jdbcTemplate) {
//		return new MysqlIdService(jdbcTemplate);
//	}
	
	@Bean
	@Autowired
	public IdService idService(CacheService cacheService) {
		return new RedisIdService(cacheService);
	}

	@Bean
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(50);
		executor.setQueueCapacity(1000);
		executor.setMaxPoolSize(100);
		executor.setKeepAliveSeconds(60 * 5);
		executor.initialize();
		return executor;
	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(taskExecutor());
	}

	@Bean(destroyMethod = "shutdown")
	public Executor taskExecutor() {
		return Executors.newScheduledThreadPool(100);
	}

	@Bean
	public FileService fileService() {
		return new UFileService();
	}
	
	@Bean
	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
		NamedParameterJdbcTemplate jt = new NamedParameterJdbcTemplate(dataSource);
		JdbcUtils.init(jt);
		return jt;
	}
}
