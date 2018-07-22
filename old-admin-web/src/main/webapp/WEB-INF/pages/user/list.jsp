<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>会员列表</title>
</head>
<body>
	<m:list title="会员列表" id="userList"
		listUrl="${contextPath }/user/list-data" addUrl="${contextPath }/user/add"
		searchButtonId="user_search_btn">
		<div class="input-group">
			<span class="input-group-addon">手机号码</span> <input type="text"
				name="mobile" class="form-control" style="width: 200px;">
		</div>
	</m:list>
</body>
</html>