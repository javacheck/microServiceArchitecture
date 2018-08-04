package cn.lastmiles.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.util.Wildcard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.lastmiles.bean.Permission;
import cn.lastmiles.common.utils.ConfigUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.service.ApplicationService;
import cn.lastmiles.utils.SecurityUtils;
import cn.lastmiles.utils.WebUtils;

public class SecurityFilter implements Filter {
	private final static Logger logger = LoggerFactory
			.getLogger(SecurityFilter.class);
	// 不需要登录
	private final static String[] ignoreLoginUrls = new String[] { "/login/**",
			"/api/**", "/file/image/**", "/static/**", "/captcha/**",
			"/test/**" ,"/area/**","/apk/getVersion","/alipayDirectUrl/**"};
	// 不需要权限验证
	private final static String[] ignorePermissionUrls = new String[] { "/",
			"/logout/**" };

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		ServletContext servletContext = servletRequest.getServletContext();
		
		servletContext.setAttribute("staticPath",
				servletContext.getContextPath() + "/static");
		
//		servletContext.setAttribute("staticPath","http://lastmiles.ufile.ucloud.com.cn/admin");
		
		servletContext.setAttribute("contextPath",
				servletContext.getContextPath());

		String picUrl = ConfigUtils.getProperty("picUrl");
		if (StringUtils.isBlank(picUrl)) {
			picUrl = servletContext.getContextPath() + "/file/image/";
		} else {
			if (!picUrl.startsWith("http")) {
				picUrl = servletContext.getContextPath() + picUrl;
			}
		}
		servletContext.setAttribute("picUrl", picUrl);
		
		
		servletRequest.setCharacterEncoding("UTF-8");
		servletResponse.setCharacterEncoding("UTF-8");
		
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		WebUtils.setRequest(request);
		WebUtils.setResponse(response);

		String path = request.getRequestURI();
		String contextPath = request.getContextPath();
		if (contextPath.length() > 1) {
			path = path.substring(contextPath.length());
		}
		if (!isIgnoreLoginUrls(path)) {
			// 没有登录
			if (!SecurityUtils.isLogin()) {
				// ajax请求
				if (request.getHeader("x-requested-with") != null
						&& request.getHeader("x-requested-with")
								.equalsIgnoreCase("XMLHttpRequest")) {
					response.addHeader("sessionstatus", "0");
					response.addHeader("loginUrl", request.getContextPath()
							+ "/login");
					return;
				} else {
					response.sendRedirect(request.getContextPath() + "/login");
//					Writer out = response.getWriter();
//					out.write("<html>");  
//				    out.write("<script>");  
//				    out.write("self.parent.location.href='" + request.getContextPath() + "/login" + "'");  
//				    out.write("</script>");  
//				    out.write("</html>");  
//				    out.flush();
//				    out.flush();
					return;
				}
			} else if (!isIgnorePermissionUrls(path) && !hasRes(path)) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "没有权限");
				return;
			}
		}

		filterChain.doFilter(servletRequest, servletResponse);
	}

	private boolean hasRes(String path) {
		List<Permission> permissons = SecurityUtils.getPermissionList();
		for (Permission permission : permissons) {
			if (StringUtils.isNotBlank(permission.getUrl())) {
				String[] urls = permission.getUrl().split(",");
				for (String url : urls) {
					if (Wildcard.matchPath(path, "**" + url + "/**")) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean isIgnoreLoginUrls(String path) {
		for (String pattern : ignoreLoginUrls) {
			if (Wildcard.matchPath(path, pattern)) {
				return true;
			}
		}
		return false;
	}

	private boolean isIgnorePermissionUrls(String path) {
		for (String pattern : ignorePermissionUrls) {
			if (Wildcard.matchPath(path, pattern)) {
				return true;
			}
		}

		for (String url : ApplicationService.ALLPERMISSIONURL) {
			for (String u : url.split(",")) {
				if (Wildcard.matchPath(path, "**" + u + "/**")) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}
