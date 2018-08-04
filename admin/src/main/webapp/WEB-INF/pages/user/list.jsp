<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>会员列表</title>
	
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
	function deleteConfirm(id,status) {
		var message = status==0?'启用':'禁用';
		lm.confirm("确定要"+message+"账户吗？", function() {
			lm.post("${contextPath }/user/ajax/modifyStatus/" + id, {status:status}, function(data) {
				if (data == 1) {
					noty("已"+message);
					loadCurrentList_userList();
				}
			});
		});
	}
	
	$(function(){
		$("#reportOrderID").click(function(){
			var reportBeginTime = $("#beginTime").val(); // 开始时间
			reportBeginTime = $.trim(reportBeginTime);  // 用jQuery的trim方法删除前后空格
			var reportEndTime = $("#endTime").val(); // 结束时间
			reportEndTime = $.trim(reportEndTime);  // 用jQuery的trim方法删除前后空格
			var reportName = $("#name").val(); // 姓名
			reportName = $.trim(reportName);  // 用jQuery的trim方法删除前后空格
			var reportMobile = $("#mobile").val(); // 手机号码
			reportMobile = $.trim(reportMobile);  // 用jQuery的trim方法删除前后空格
			location.href = "${contextPath}/user/ajax/reportOrderToExcel?reportBeginTime="+reportBeginTime+"&reportEndTime="+reportEndTime +"&reportName="+reportName+"&reportMobile="+reportMobile;
			//window.open("${contextPath}/user/ajax/reportOrderToExcel?reportBeginTime="+reportBeginTime+"&reportEndTime="+reportEndTime +"&reportName="+reportName+"&reportMobile="+reportMobile);
		});
	});
</script>
</head>
<body>
	<m:hasPermission permissions="userAdd" flagName="addFlag"/>
	<m:list title="会员列表" id="userList"
		listUrl="${contextPath }/user/list-data" 
		
		searchButtonId="user_search_btn">
		<div class="input-group" style="max-width: 1200px;">
			<span  class="input-group-addon">开始时间</span> 
			<input id = "beginTime" type="text" name="beginTime" class="form-control form-date" placeholder="选择开始日期" readonly  style="width: 200px;">
			<span  class="input-group-addon">结束时间</span> 
			<input id = "endTime" type="text"	name="endTime" class="form-control form-date" placeholder="选择结束日期" readonly style="width: 200px;">
			<span class="input-group-addon">姓名</span> 
			<input type="text" id="name" name="name" class="form-control" placeholder="请输入会员姓名" style="width: 200px;">
			<span class="input-group-addon">手机号码</span> 
			<input type="text" id="mobile" name="mobile" class="form-control" placeholder="请输入会员手机号码" style="width: 200px;">
		</div>
		<a id="reportOrderID" class="btn btn-small btn-danger" style="width: auto;margin-top:5px;">导出会员</a>
	</m:list>
</body>
</html>