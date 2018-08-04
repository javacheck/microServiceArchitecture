package cn.lastmiles.decora;

import javax.servlet.http.HttpServletRequest;

import cn.lastmiles.utils.SecurityUtils;
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
		if (Wildcard.matchPath(actionPath, "**/agentList/**")) {
			return null;
		}
		if (Wildcard.matchPath(actionPath, "**/partnerList/**")) {
			return null;
		}
		
		if (Wildcard.matchPath(actionPath, "/productStock/preview/**")) {
			return null;
		}
		if (Wildcard.matchPath(actionPath, "**/shopTypeList/**")) {
			return null;
		}
		if (Wildcard.matchPath(actionPath, "**/businessList-data/**")) {
			return null;
		}
		if (Wildcard.matchPath(actionPath, "**/product/shopList/list/**")) {
			return null;
		}
		if (Wildcard.matchPath(actionPath, "**/productStock/shopList/list/**")) {
			return null;
		}
		if (Wildcard.matchPath(actionPath, "**/product/productList/list/**")) {
			return null;
		}
		if (Wildcard.matchPath(actionPath, "**/detailList-data/**")) {
			return null;
		}
		if (Wildcard.matchPath(actionPath, "**/storeList-data/**")) {
			return null;
		}
		if (Wildcard.matchPath(actionPath, "**/listFrom-data/**")) {
			return null;
		}
		if (Wildcard.matchPath(actionPath, "**/detailsList-data/**")) {
			return null;
		}
		if (Wildcard.matchPath(actionPath, "**/storageList/list-data/**")) {
			return null;
		}
		if (Wildcard.matchPath(actionPath, "**/alipayDirectUrl/**")) {
			return null;
		}
		
		if (SecurityUtils.isStore()){
			return "/WEB-INF/pages/decora/store.jsp";
		}else {
			return "/WEB-INF/pages/decora/main.jsp";
		}
		
//		return DECORATOR;
	}

	public boolean decorateContentType(String contentType, String mimeType,
			String encoding) {
		return "text/html".equals(mimeType);
	}
}
