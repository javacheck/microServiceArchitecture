<%@ tag language="java" pageEncoding="UTF-8"%>
<%@tag import="cn.lastmiles.utils.SecurityUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ attribute name="isTrue" %>

<%
    boolean flag = SecurityUtils.isMainStore();
	Object isTrueObj=this.getJspContext().getAttribute("isTrue");
	boolean isTrue = true;
	if (isTrueObj != null){
		isTrue = Boolean.valueOf(isTrueObj.toString());
	}
	if (isTrue != true){
		flag = !flag;
	}
	this.getJspContext().setAttribute("flag", flag);
%>

<c:if test="${flag == true }">
	<jsp:doBody></jsp:doBody>
</c:if>