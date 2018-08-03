<%@tag import="cn.self.cloud.utils.SecurityUtils"%>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%--
	是否有权限的标识名称 在页面可以通过这个名称判断是否有权限 ${flagName == true} ，flagName是标签传进来的值
--%>
<%@ attribute name="flagName" %>
<%@ attribute name="permissions" required="true"%>

<%
	String permissions = (String) this.getJspContext().getAttribute(
			"permissions");
	boolean flag = SecurityUtils.hasAnyPermission(permissions.split(","));
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