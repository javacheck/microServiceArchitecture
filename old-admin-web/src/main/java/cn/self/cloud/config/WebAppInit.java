package cn.self.cloud.config;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import cn.self.cloud.filter.SecurityFilter;
import jodd.decora.DecoraServletFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInit extends
		AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { RootConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { WebConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	@Override
	public void onStartup(ServletContext servletContext)
			throws ServletException {
		servletContext.setAttribute("staticPath",
				servletContext.getContextPath() + "/static");
		servletContext.setAttribute("contextPath",
				servletContext.getContextPath());

		super.onStartup(servletContext);

		FilterRegistration.Dynamic encodingFilter = servletContext.addFilter(
				"encodingFilter", new CharacterEncodingFilter());
		encodingFilter.setInitParameter("encoding", "UTF-8");
		encodingFilter.setInitParameter("forceEncoding", "true");
		encodingFilter.setAsyncSupported(isAsyncSupported());
		encodingFilter.addMappingForServletNames(getDispatcherTypes(), true,
				getServletName());

		FilterRegistration.Dynamic decoraFilter = servletContext.addFilter(
				"decoraFilter", new DecoraServletFilter());
		decoraFilter.setInitParameter("decora.manager",
				"cn.lastmiles.decora.AppDecoraManager");
		decoraFilter.setAsyncSupported(isAsyncSupported());
		decoraFilter.addMappingForServletNames(getDispatcherTypes(), true,
				getServletName());

		FilterRegistration.Dynamic securityFilter = servletContext.addFilter(
				"securityFilter", new SecurityFilter());
		securityFilter.setAsyncSupported(isAsyncSupported());
		securityFilter.addMappingForUrlPatterns(getDispatcherTypes(), false,
				"/*");
	}

	private EnumSet<DispatcherType> getDispatcherTypes() {
		return isAsyncSupported() ? EnumSet.of(DispatcherType.REQUEST,
				DispatcherType.ERROR, DispatcherType.ASYNC) : EnumSet.of(
				DispatcherType.REQUEST, DispatcherType.ERROR);
	}
}
