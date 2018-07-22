<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>会员管理</title>
<script type="text/javascript">
	$(function(){
		 $("#beginTime").datetimepicker({
			minView: "month", //选择日期后，不会再跳转去选择时分秒 
			format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
			language: 'zh-CN', //汉化 
			autoclose:true //选择日期后自动关闭 
		 });
		 $("#endTime").datetimepicker({
			minView: "month", //选择日期后，不会再跳转去选择时分秒 
			format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
			language: 'zh-CN', //汉化 
			autoclose:true //选择日期后自动关闭 
		 });
	});
</script>
</head>
<body>
	<m:list title="订单列表" id="userList"
		listUrl="${contextPath }/order/list-data"
		 searchButtonId="user_search_btn">
		<div class="input-group">
			<span  class="input-group-addon">开始时间</span> 
			<input id = "beginTime"  type="text"    name="beginTime" class="form-control form-date" placeholder="选择开始日期" readonly  style="width: 300px;">
			<span  class="input-group-addon">结束时间</span> 
			<input id = "endTime"  type="text"	name="endTime" class="form-control form-date" placeholder="选择结束日期" readonly style="width: 300px;">
			<span class="input-group-addon">用户手机</span> 
			<input type="text"	name="mobile" class="form-control" placeholder="请输入手机号码" style="width: 200px;">
			<span class="input-group-addon">订单编号</span> 
			<input type="text"	name="orderId" class="form-control" placeholder="请输入订单编号" style="width: 200px;">
		</div>
	</m:list>
</body>
</html>