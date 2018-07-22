package cn.self.cloud.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public final class WebUtils {
	private final static ThreadLocal<HttpServletRequest> threadLocalRequest = new ThreadLocal<HttpServletRequest>();
	private final static ThreadLocal<HttpServletResponse> threadLocalResponse = new ThreadLocal<HttpServletResponse>();

	public static void setRequest(HttpServletRequest request) {
		threadLocalRequest.set(request);
	}

	public static HttpServletRequest getRequest() {
		return threadLocalRequest.get();
	}

	public static void setResponse(HttpServletResponse response) {
		threadLocalResponse.set(response);
	}

	public static HttpServletResponse getResponse() {
		return threadLocalResponse.get();
	}

	public static HttpSession getSession() {
		return getRequest().getSession();
	}

	public static Object getAttributeFromRequest(String key) {
		return getRequest().getAttribute(key);
	}

	public static void setAttributeToRequest(String key, Object value) {
		getRequest().setAttribute(key, value);
	}

	public static Object getAttributeFromSession(String key) {
		return getSession().getAttribute(key);
	}

	public static void setAttributeToSession(String key, Object value) {
		getSession().setAttribute(key, value);
	}
}