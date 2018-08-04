package cn.lastmiles.exception;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.util.Wildcard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import cn.lastmiles.utils.JsonUtils;

/**
 * web的统一异常处理
 * 
 * @author hql
 *
 */
public class WebHandlerExceptionResolver extends
		DefaultHandlerExceptionResolver {
	private final static Logger logger = LoggerFactory
			.getLogger(WebHandlerExceptionResolver.class);

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		String path = request.getRequestURI();
		String contextPath = request.getContextPath();
		if (contextPath.length() > 1) {
			path = path.substring(contextPath.length());
		}
		logger.error(path, ex);
		if (Wildcard.matchPath(path, "/api/**")) {
			response.setContentType("application/json;charest=utf-8");
			Map<String, Object> ret = new HashMap<String, Object>();
			ret.put("code", "500");
			ret.put("message", "服务器出错");
			try {
				response.getWriter().write(JsonUtils.objectToJson(ret));
				response.getWriter().flush();
				response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		} else {
			ModelAndView modelAndView = new ModelAndView("webexception");
			modelAndView.addObject("ex", ex);
			return modelAndView;
		}
	}
}
