package cn.lastmiles.config;

import java.util.List;

import jodd.decora.DecoraServletFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import cn.lastmiles.cache.CacheService;
import cn.lastmiles.exception.WebHandlerExceptionResolver;
import cn.lastmiles.filter.SecurityFilter;
import cn.lastmiles.interceptor.LogHandlerInterceptor;
import cn.lastmiles.service.LogService;
import cn.lastmiles.service.WorkRecordService;

@Configuration
@ComponentScan(basePackages = { "cn.lastmiles" }, excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class) })
@Import(RootConfig.class)
// @EnableWebMvc
//@ComponentScan(basePackages = { "cn.lastmiles.controller",
//		"cn.lastmiles.v2.controller" }, includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = org.springframework.stereotype.Controller.class))
public class WebConfig extends WebMvcConfigurerAdapter {
	@Autowired
	private CacheService cacheService;
	@Autowired
	private WorkRecordService workRecordService;
	@Autowired
	private LogService logService;
	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Bean
	public FilterRegistrationBean securityFilter() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new SecurityFilter());
		registration.addServletNames("dispatcherServlet");
		registration.setName("SecurityFilter");
		registration.setOrder(1);
		return registration;
	}

	@Bean
	public FilterRegistrationBean decoraFilter() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new DecoraServletFilter());
		registration.addServletNames("dispatcherServlet");
		registration.setName("decoraFilter");
		registration.addInitParameter("decora.manager",
				"cn.lastmiles.decora.AppDecoraManager");
		registration.setOrder(2);
		return registration;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(
				new LogHandlerInterceptor(logService, threadPoolTaskExecutor))
				.addPathPatterns("/**");

		super.addInterceptors(registry);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations(
				"/WEB-INF/static/");
	}

	@Override
	public void configureHandlerExceptionResolvers(
			List<HandlerExceptionResolver> exceptionResolvers) {
		super.configureHandlerExceptionResolvers(exceptionResolvers);
		exceptionResolvers.add(new WebHandlerExceptionResolver());
	}

	@Bean(name = "multipartResolver")
	public MultipartResolver multipartResolver() {
		return new CommonsMultipartResolver();
	}
}
