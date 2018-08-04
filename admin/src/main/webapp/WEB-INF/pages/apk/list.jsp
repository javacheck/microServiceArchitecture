<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>APK管理列表</title>

</head>
<body>
	<m:hasPermission permissions="apkAdd" flagName="addFlag"/>
	<m:list title="apk管理列表" id="apkList"
		listUrl="${contextPath }/apk/list-data"
		addUrl="${addFlag == true ? '/apk/add' : '' }">
	</m:list>
</body>
</html>