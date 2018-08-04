<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="${contextPath}/static/css/calendar/calendar.css">
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
<script src="${contextPath}/static/js/calendar/calendar.js"></script>
<script src="${contextPath}/static/js/map/map.js"></script>  
<title>预订列表</title>
<script type="text/javascript">
	$(function(){
		var format = "yyyy-MM-dd";//默认格式
		var now = lm.Now;//当前日期
		var nowFormat=now.Format(format);
		$("#beginTime").datetimepicker({
			minView: "month", //选择日期后，不会再跳转去选择时分秒 
			format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
			language: 'zh-CN', //汉化 
			autoclose:true , //选择日期后自动关闭
			todayBtn:true,//可选择当天按钮
			todayHighlight:false,//高亮当前日期
			endDate:""+nowFormat+""//默认最后可选择为当前日期
			
		 });
		$("#endTime").datetimepicker({
			minView: "month", //选择日期后，不会再跳转去选择时分秒 
			format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
			language: 'zh-CN', //汉化 
			autoclose:true, //选择日期后自动关闭 
			todayBtn:true,//可选择当天按钮
			todayHighlight:false,//高亮当前日期
			endDate:""+nowFormat+""//默认最后可选择为当前日期
		 });
		var endDateInput=nowFormat;
		//判断输入结束日期时候输入合法
		$('#endTime').datetimepicker().on('changeDate', function(ev){
			if((new Date(ev.date.valueOf()))<(new Date($('#beginTime').val().replace(/-/g,"/")))){//开始时间大于结束时间
				lm.alert("结束时间不能大于开始时间");
				$("#endTime").val(endDateInput);//默认值上一次输入
				$('#endTime').datetimepicker('update');//更新
			}
			endDateInput= $("#endTime").val();
			$('#beginTime').datetimepicker('setEndDate', $("#endTime").val());//设置开始时间最后可选结束时间
		});
	});
	function changStatus(id,status){
		var info = status==0?"关闭":"开启";
		lm.confirm("确定要"+info+"吗？", function() {
			lm.post("${contextPath }/room/changStatus", {id:id,status:status}, function(data) {
				if (data == 1) {
					noty(info+"成功");
					loadCurrentList_roomList();
				}
			});
		});
	}
	function beforeSearch(){
		return true;
	}
</script>
</head>
<body>
	<m:hasPermission permissions="accountAdd" flagName="addFlag"/>


	<m:list title="预订列表" id="roomBookingList" beforeSearch="beforeSearch"
		listUrl="/roomBooking/list-data"
		 searchButtonId="user_search_btn">
		<div class="input-group" style="max-width: 1300px;">
			<span  class="input-group-addon">开始时间</span> 
			<input id = "beginTime"  type="text"    name="beginTime" class="form-control form-date"  readonly  style="width: 200px;">
			<span  class="input-group-addon" >结束时间</span> 
			<input id = "endTime"  type="text"	name="endTime" class="form-control form-date"  readonly style="width: 200px;">
			<span  class="input-group-addon">包间号码</span> 
			<input type="text"	name="number" class="form-control" placeholder="请输入包间号码" style="width: 200px;">
			<span  class="input-group-addon">联系人</span> 
			<input type="text"	name="phone" class="form-control" placeholder="请输入手机号码" style="width: 200px;">
			<span  class="input-group-addon">状态</span> 
			<select name=status style="width: auto" class="form-control">
				<option value="">全部</option>
				<option value="0">预定中</option>
				<option value="1">已开台</option>
				<option value="3">已取消</option>
			</select>
		</div>
	</m:list>
</body>
</html>