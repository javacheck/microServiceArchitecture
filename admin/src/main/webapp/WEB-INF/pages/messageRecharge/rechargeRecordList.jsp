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
<title>充值记录列表</title>
<script type="text/javascript">
$(function(){
	var format = "yyyy-MM-dd";//默认格式
	var now = lm.Now;//当前日期
	var nowFormat=now.Format(format);
	$("#searchTime").datetimepicker({
		minView: "month", //选择日期后，不会再跳转去选择时分秒 
		format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
		language: 'zh-CN', //汉化 
		autoclose:true , //选择日期后自动关闭
		todayBtn:true,//可选择当天按钮
		todayHighlight:false,//高亮当前日期
		endDate:""+nowFormat+""//默认最后可选择为当前日期
		
	 });
});
	function beforeSearch(){
		return true;
	}
	
</script>
</head>
<body>
	<m:list title="充值记录列表" id="messageRecharge_recharge_List" beforeSearch="beforeSearch"
		listUrl="/messageRecharge/recharge/record/list-data"
		searchButtonId="messageRecharge_recharge_search_btn">
		<div class="input-group" style="max-width: 1300px;">
		
		<span  class="input-group-addon">操作人员</span> 
			<select name="accountId" style="width: auto" class="form-control">
				<option  value="-1">全部</option>
				<c:forEach items="${accountIdList}" var="accountArray">
					<option value="${accountArray.id }">${accountArray.mobile }</option>
				</c:forEach>
		    </select>
		    
			<input type="hidden"	name="storeId" value="${storeId }" style="width: 200px;">
			<span class="input-group-addon">充值时间</span>
			<input id="searchTime" value="" type="text" name="searchTime" class="form-control form-date" placeholder="选择充值时间" readonly style="width: 200px;">
		</div>
		 <a href='${contextPath }/messageRecharge/returnList/' class='btn btn-small btn-warning' style="width: auto;margin-top:5px;">返回短信充值列表</a>
	</m:list>
</body>
</html>