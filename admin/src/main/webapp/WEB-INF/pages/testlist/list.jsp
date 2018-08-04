<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
<script type="text/javascript" src="${staticPath }/js/jquery.pager.js"></script>
<script type="text/javascript" src="${staticPath }/js/list_page.js"></script>
</head>
<body>
	<m:list title="用户列表" 
		listUrl="${contextPath }/user/listData"
		searchButtonId="user_search_btn" >
			用户名：
			<input name="username" class="form-control" placeholder='用户名'/>
	</m:list>
</body>
</html>