//package cn.lastmiles.config;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.EnumSet;
//
//import javax.servlet.DispatcherType;
//import javax.servlet.FilterRegistration;
//import javax.servlet.ServletContext;
//import javax.servlet.ServletException;
//
//import jodd.decora.DecoraServletFilter;
//import jodd.util.ClassLoaderUtil;
//import jodd.util.StringUtil;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
//
//import cn.lastmiles.common.utils.ConfigUtils;
//import cn.lastmiles.common.utils.StringUtils;
//import cn.lastmiles.filter.SecurityFilter;
//
//public class WebAppInit extends
//		AbstractAnnotationConfigDispatcherServletInitializer {
//	public static final String DEFAULT_FILTER_NAME = "springSessionRepositoryFilter";
//	private final static Logger logger = LoggerFactory
//			.getLogger(WebAppInit.class);
//	
//	static {
//		// 加载配置文件
//		try {
//			String filename = "common.properties";
//			ConfigUtils.load(ClassLoaderUtil.getResourceAsStream(filename));
//			
//			String osname = System.getProperty("os.name").toLowerCase();
//			String configDir = ConfigUtils.getProperty("config.dir");
//			if (osname.startsWith("win")){
//				configDir = ConfigUtils.getProperty("config.windows.dir");
//			}
//			logger.debug("config dir = {}",configDir);;
//			if (StringUtil.isNotBlank(configDir)) {
//				File file = new File(configDir + File.separatorChar + filename);
//				if (file.exists()) {
//					logger.debug("file = {}",file.getAbsolutePath());;
//					ConfigUtils.clearAndLoad(new FileInputStream(file));
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//	}
//
//	@Override
//	protected Class<?>[] getRootConfigClasses() {
//		return new Class[] { RootConfig.class };
//	}
//
//	@Override
//	protected Class<?>[] getServletConfigClasses() {
//		return new Class[] { WebConfig.class };
//	}
//
//	@Override
//	protected String[] getServletMappings() {
//		return new String[] { "/" };
//	}
//
//	@Override
//	public void onStartup(ServletContext servletContext)
//			throws ServletException {
//		servletContext.setAttribute("staticPath",
//				servletContext.getContextPath() + "/static");
//		
////		servletContext.setAttribute("staticPath","http://lastmiles.ufile.ucloud.com.cn/admin");
//		
//		servletContext.setAttribute("contextPath",
//				servletContext.getContextPath());
//
//		String picUrl = ConfigUtils.getProperty("picUrl");
//		if (StringUtils.isBlank(picUrl)) {
//			picUrl = servletContext.getContextPath() + "/file/image/";
//		} else {
//			if (!picUrl.startsWith("http")) {
//				picUrl = servletContext.getContextPath() + picUrl;
//			}
//		}
//		logger.debug("pic url = {}",picUrl);
//		servletContext.setAttribute("picUrl", picUrl);
//
//		super.onStartup(servletContext);
//
//		FilterRegistration.Dynamic decoraFilter = servletContext.addFilter(
//				"decoraFilter", new DecoraServletFilter());
//		decoraFilter.setInitParameter("decora.manager",
//				"cn.lastmiles.decora.AppDecoraManager");
//		decoraFilter.setAsyncSupported(isAsyncSupported());
//		decoraFilter.addMappingForServletNames(getDispatcherTypes(), true,
//				getServletName());
//
//		// spring session filter
////		String filterName = DEFAULT_FILTER_NAME;
////		FilterRegistration.Dynamic springSessionFilter = servletContext
////				.addFilter(filterName, new DelegatingFilterProxy(filterName));
////		springSessionFilter.setAsyncSupported(isAsyncSupported());
////		springSessionFilter.addMappingForServletNames(getDispatcherTypes(),
////				false, getServletName());
//
//		FilterRegistration.Dynamic securityFilter = servletContext.addFilter(
//				"securityFilter", new SecurityFilter());
//		securityFilter.setAsyncSupported(isAsyncSupported());
//		securityFilter.addMappingForServletNames(getDispatcherTypes(), false,
//				getServletName());
//	}
//
//	private EnumSet<DispatcherType> getDispatcherTypes() {
//		return isAsyncSupported() ? EnumSet.of(DispatcherType.REQUEST,
//				DispatcherType.ERROR, DispatcherType.ASYNC) : EnumSet.of(
//				DispatcherType.REQUEST, DispatcherType.ERROR);
//	}
//}
