<%@tag import="cn.lastmiles.utils.SecurityUtils"%>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%--
	是否有权限的标识名称 在页面可以通过这个名称判断是否有权限 ${flagName == true} ，flagName是标签传进来的值
--%>
<%@ attribute name="flagName" %>
<%@ attribute name="accountType" required="true"%>
<%@ attribute name="isTrue" %>

<%
    boolean flag =false;
	String accountType =(String) this.getJspContext().getAttribute("accountType");
	
	Object isTrueObj=this.getJspContext().getAttribute("isTrue");
	String isTrue =isTrueObj!=null?(String) isTrueObj:"true";
	if(accountType.toLowerCase().equals("admin")){
		flag=SecurityUtils.isAdmin();
	}else if (accountType.toLowerCase().equals("store")){
		flag=SecurityUtils.isNormalStore();
	}else if (accountType.toLowerCase().equals("agent")){
		flag=SecurityUtils.isAgent();
	}else if (accountType.toLowerCase().equals("mainstore")){
		flag = SecurityUtils.isMainStore();
	}else if (accountType.toLowerCase().equals("chainstore")){
		flag = SecurityUtils.isChainStore();
	}
	if(isTrue.toLowerCase().equals("false")){
		flag=!flag;
	}
	
	this.getJspContext().setAttribute("flag", flag);
	
	String flagName = (String) this.getJspContext().getAttribute(
			"flagName");
	if (flagName != null && !"".equals(flagName)){
		request.setAttribute(flagName, flag);
	}
%>

<c:if test="${flag == true }">
	<jsp:doBody></jsp:doBody>
</c:if>