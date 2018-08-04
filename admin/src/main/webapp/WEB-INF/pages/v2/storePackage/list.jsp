<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>套餐列表</title>

</head>
<body>
	<m:hasPermission permissions="storePackageAdd" flagName="addFlag"/>
	<m:list title="套餐列表" id="storePackageList"
		listUrl="${contextPath }/storePackage/list-data"
		addUrl="${addFlag == true ? '/storePackage/add' : '' }"
		searchButtonId="package_search_btn">
		
		<div class="input-group" style="max-width:800px;">
		 	<span class="input-group-addon">套餐名称</span> 
            	<input type="text" id="name" name="name" class="form-control" placeholder="套餐名称" style="width: 200px;">
        </div>
        
	</m:list>
</body>
</html>