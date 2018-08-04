<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
html {
	font: 500 14px 'roboto';
	color: #333;
	background-color: #fafafa;
}
ul, ol, li {
	list-style: none;
	padding: 0;
	margin: 0;
}
#demo {
	width: 300px;
	margin: 150px auto;
}
p {
	margin: 0;
}
#dt {
	margin: 30px auto;
	height: 28px;
	width: 200px;
	padding: 0 6px;
	border: 1px solid #ccc;
	outline: none;
}
</style>
<title>短信充值列表</title>
<script type="text/javascript">
	function beforeSearch(){
		return true;
	}
	
</script>
</head>
<body>
	<m:hasPermission permissions="messageRechargeAdd" flagName="addFlag"/>

	<m:list title="短信充值列表" id="messageRechargeList" beforeSearch="beforeSearch"
		listUrl="/messageRecharge/list-data"
		searchButtonId="messageRecharge_search_btn">
		<div class="input-group" style="max-width: 1300px;">
			<span  class="input-group-addon">商家名称</span> 
			<input type="text"	name="storeName" class="form-control" placeholder="请输入商家名称" style="width: 200px;">
		</div>
		 <a href='${contextPath }/messageRecharge/add' class='btn btn-small btn-warning' style="width: auto;margin-top:5px;">短信充值</a>
	</m:list>
</body>
</html>