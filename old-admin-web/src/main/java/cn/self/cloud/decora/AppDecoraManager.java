package cn.self.cloud.decora;

import javax.servlet.http.HttpServletRequest;

import jodd.decora.DecoraManager;
import jodd.util.Wildcard;

public class AppDecoraManager extends DecoraManager {
	private static final String DECORATOR = "/WEB-INF/pages/decora/main.jsp";

	public String resolveDecorator(HttpServletRequest request, String actionPath) {
		if (Wildcard.matchPath(actionPath, "/test/**")) {
			return null;
		}
		if (Wildcard.matchPath(actionPath, "/login")) {
			return null;
		}
		if (Wildcard.matchPath(actionPath, "**/listData/**")) {
			return null;
		}
		if (Wildcard.matchPath(actionPath, "**/list-data/**")) {
			return null;
		}
		if (Wildcard.matchPath(actionPath, "**/ajax/**")) {
			return null;
		}
		if (Wildcard.matchPath(actionPath, "**/showMode/**")) {
			return null;
		}
		if (Wildcard.matchPath(actionPath, "**/api/**")) {
			return null;
		}
		return DECORATOR;
	}

	public boolean decorateContentType(String contentType, String mimeType,
			String encoding) {
		return "text/html".equals(mimeType);
	}
}
