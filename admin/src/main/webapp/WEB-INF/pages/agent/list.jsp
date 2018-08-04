<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>代理商列表</title>
<script type="text/javascript">

</script>
</head>
<body>
	<m:hasPermission permissions="agentAdd" flagName="addFlag"/>
	<m:list title="代理商列表" id="agentList"
		listUrl="${contextPath }/agent/list/list-data"  
		addUrl="${addFlag == true ? '/agent/add' : '' }"
		searchButtonId="cateogry_search_btn" >
		
		<div class="input-group" style="max-width: 1500px;">
			
            <input type="hidden" id="mobile" name="mobile" value=""/>	
            <span class="input-group-addon">代理商名称</span> 
            	<input type="text" id="name" name="name" class="form-control" placeholder="代理商名称" style="width: 200px;">
           	
           	<c:if test="${isAdmin }">
            <span class="input-group-addon">代理商类型</span>
            	<select name="type" class="form-control" style="width: 200px;" id="type" >
					<option  value ="">全部</option>
					<option  value ="1">总代理商</option>
					<option  value ="2">运营商</option>
					<option  value ="3">子公司</option>
            	</select>
            </c:if>
            <span class="input-group-addon">联系方式</span>
            	<input type="text" id="contactName" name="contactName" class="form-control" placeholder="联系方式" style="width: 200px;">
		</div>
	</m:list>
</body>
</html>