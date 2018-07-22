package cn.self.cloud.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.self.cloud.bean.APIResponse;
import cn.self.cloud.commonutils.basictype.StringUtils;
import cn.self.cloud.commonutils.cache.CacheService;
import cn.self.cloud.commonutils.json.JsonUtils;
import jodd.util.Wildcard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class APIHandlerInterceptor implements HandlerInterceptor {
	private final static String[] ignoreLoginUrls = new String[] { "/api/login*",
		"**/ajax/**", "**/test/**" };
	private final static Logger logger = LoggerFactory
			.getLogger(APIHandlerInterceptor.class);

	private CacheService cacheService;

	public APIHandlerInterceptor(CacheService cacheService) {
		super();
		this.cacheService = cacheService;
	}

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object arg2) throws Exception {
		String path = request.getRequestURI();
		String contextPath = request.getContextPath();
		if (contextPath.length() > 1) {
			path = path.substring(contextPath.length());
		}
		for (String pattern : ignoreLoginUrls) {
			if (Wildcard.matchPath(path, pattern)) {
				return true;
			}
		}
		
		String token = request.getParameter("token");
		if (StringUtils.isNotBlank(token)){
			Object obj = cacheService.get(token);
			if (obj != null){
				return true;
			}
		}
		logger.debug("api token = {} ,uri = {}", token,request.getRequestURI());
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");

		APIResponse apiResponse = new APIResponse();
		apiResponse.set(-100, "请登录");

		response.getWriter().write(JsonUtils.objectToJson(apiResponse));
		response.getWriter().flush();
		response.getWriter().close();
		return false;
	}
}
