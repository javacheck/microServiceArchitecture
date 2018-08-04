<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>广播系统管理列表</title>
<script type="text/javascript">

</script>
</head>
<body>
	<m:hasPermission permissions="informAdd" flagName="addFlag"/>
	<m:list title="广播系统管理列表" id="informList"
		listUrl="${contextPath }/inform/list-data"
		addUrl="${addFlag == true ? '/inform/add' : '' }"
		searchButtonId="inform_search_btn">
		
		<div class="input-group" style="max-width: 600px;">
			
            <span class="input-group-addon">名称</span> 
            <input type="text" id="name" name="name" class="form-control" placeholder="名称" style="width: 200px;margin-right:40px;">
		</div>
	</m:list>
</body>
</html>