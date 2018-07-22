<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>分类列表</title>
</head>
<body>
	<m:list title="分类列表" id="productCategoryList"
		listUrl="${contextPath }/productCategory/list/list-data"
		searchButtonId="cateogry_search_btn">
		<div class="input-group">
			<span class="input-group-addon">名称</span> <input type="text"
				name="name" class="form-control" placeholder="名称" style="width: 200px;">
		</div>
	</m:list>
</body>
</html>