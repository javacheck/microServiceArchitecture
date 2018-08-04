package cn.lastmiles.config;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import cn.lastmiles.constant.MQTopic;
import cn.lastmiles.mq.MQReceiverService;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

@Configuration
//@ComponentScan(basePackages = { "cn.lastmiles" }, excludeFilters = {
//		@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class),
//		@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class) })
@EnableTransactionManagement(proxyTargetClass = true)
@EnableScheduling
// @EnableRedisHttpSession
@Import(BaseConfig.class)
public class RootConfig {
	private final static Logger logger = LoggerFactory
			.getLogger(RootConfig.class);

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
		properties.setProperty("kaptcha.background.clear.from", "BLACK");
		properties.setProperty("kaptcha.background.clear.to", "WHITE");
		properties.setProperty("kaptcha.textproducer.char.length", "4");
		properties.setProperty("kaptcha.textproducer.font.size", "32");
		properties.setProperty("kaptcha.textproducer.char.string","ABCDEFGHJKMNPRSTVWXabcde2345678gfmnprswx");
		properties.setProperty("kaptcha.noise.impl","com.google.code.kaptcha.impl.NoNoise");

		Config config = new Config(properties);
		producer.setConfig(config);
		return producer;
	}

	@Bean
	RedisMessageListenerContainer container(
			RedisConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(listenerAdapter, new ChannelTopic(MQTopic.USERCARDPOINT));

		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(MQReceiverService receiver,JdkSerializationRedisSerializer serializer) {
		MessageListenerAdapter adapter = new MessageListenerAdapter(receiver,
				"receiveMessage");
		adapter.setSerializer(serializer);
		return adapter;
	}

	@Bean
	MQReceiverService receiver() {
		return new MQReceiverService();
	}

	@Bean
	JdkSerializationRedisSerializer jdkSerializationRedisSerializer() {
		return new JdkSerializationRedisSerializer();
	}

}
