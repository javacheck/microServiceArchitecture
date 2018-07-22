package cn.self.cloud.filter;

import java.io.IOException;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.self.cloud.bean.Permission;
import cn.self.cloud.service.ApplicationService;
import cn.self.cloud.utils.SecurityUtils;
import cn.self.cloud.utils.WebUtils;
import jodd.util.Wildcard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityFilter implements Filter {
	private final static Logger logger = LoggerFactory
			.getLogger(SecurityFilter.class);
	// 不需要登录
	private final static String[] ignoreLoginUrls = new String[] { "/login/**",
			"/api/**", "/static/**", "/captcha/**", "/test/**" };
	// 不需要权限验证
	private final static String[] ignorePermissionUrls = new String[] { "/",
			"/logout/**" };

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
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
					return;
				}
			} else if (!isIgnorePermissionUrls(path) && !hasRes(path)) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "没有权限");
				return;
			}
		}

		filterChain.doFilter(servletRequest, servletResponse);

	}

	@SuppressWarnings("unchecked")
	private boolean hasRes(String path) {
		List<Permission> permissons = SecurityUtils.getPermissionList();
		for (Permission permission : permissons) {
			if (Wildcard.matchPath(path, "**" + permission.getUrl())) {
				return true;
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
			if (Wildcard.matchPath(path, "**" + url)) {
				return false;
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
