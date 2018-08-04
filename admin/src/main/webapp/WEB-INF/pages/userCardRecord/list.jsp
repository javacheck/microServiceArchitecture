<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>会员交易记录列表</title>
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

$(function(){
	$("#reportUserCard").click(function(){
		var reportBeginTime = $("#beginTime").val(); // 开始时间
		reportBeginTime = $.trim(reportBeginTime);  // 用jQuery的trim方法删除前后空格
		
		var reportEndTime = $("#endTime").val(); // 结束时间
		reportEndTime = $.trim(reportEndTime);  // 用jQuery的trim方法删除前后空格
		
		var reportMobile = $("#mobile").val(); // 会员号
		reportMobile = $.trim(reportMobile);  // 用jQuery的trim方法删除前后空格
		
		var reportOrderId = $("#orderId").val(); // 订单编号
		reportOrderId = $.trim(reportOrderId);  // 用jQuery的trim方法删除前后空格
		
		var reportCardNum = $("#cardNum").val(); // 会员卡号
		reportCardNum = $.trim(reportCardNum);  // 用jQuery的trim方法删除前后空格
		
		var reportType = $("#type").val();
		reportType = $.trim(reportType);
		
		location.href = "${contextPath}/userCardRecord/ajax/reportUserCardRecordToExcel?reportBeginTime="+reportBeginTime+"&reportEndTime="+reportEndTime +"&reportCardNum="+reportCardNum+"&reportType="+reportType +"&reportOrderId="+reportOrderId+"&reportMobile="+reportMobile;
		//window.open("${contextPath}/user/ajax/reportOrderToExcel?reportBeginTime="+reportBeginTime+"&reportEndTime="+reportEndTime +"&reportName="+reportName+"&reportMobile="+reportMobile);
	});
});

//点击搜索时先判断价格参数的正确性
function checkParameter(){
	var mobile = $("input[name='mobile']").val();
	mobile = $.trim(mobile); // 用jQuery的trim方法删除前后空格
	if( null != mobile && mobile != "" ){
		if(!lm.isMobile(mobile)){
			  lm.alert("会员号的格式错误");
			  return;
		  }
	}
	
	var orderId = $("input[name='orderId']").val();
	orderId = $.trim(orderId); // 用jQuery的trim方法删除前后空格
	if( null != orderId && orderId != "" ){
		if( !(/^\+?[1-9][0-9]*$/.test(orderId)) ){
			  lm.alert("订单编号必须为非零的正整数");
			  return;
		  }
	}
	
	var cardNum = $("input[name='cardNum']").val();
	cardNum = $.trim(cardNum); // 用jQuery的trim方法删除前后空格
	if( null != cardNum && cardNum != "" ){
		if( !(/^[0-9]*$/.test(cardNum)) ){
			  lm.alert("卡号必须为非零的正整数");
			  return;
		  }
	}
	return true; 
}
</script>
</head>
<body>
	<m:list title="会员交易记录列表" id="userCardRecordList"
		listUrl="${contextPath }/userCardRecord/list-data"
		searchButtonId="userCardRecord_search_btn" beforeSearch="checkParameter" >
		
		<div class="input-group" style="max-width: 1200px;">
		 	<span class="input-group-addon">会员号</span> 
           	<input type="text" id="mobile" name="mobile" class="form-control" placeholder="请输入会员手机号" style="width: 200px;">
           	
           	<span class="input-group-addon">订单编号</span> 
           	<input type="text" id="orderId" name="orderId" class="form-control" placeholder="订单编号" style="width: 200px;">
           	
           	<span class="input-group-addon">会员卡号</span> 
           	<input type="text" id="cardNum" name="cardNum" class="form-control" placeholder="会员卡号" style="width: 200px;">
        </div>
        <div class="input-group" style="max-width: 1000px;">
           	<span class="input-group-addon">交易类型</span>
           	<select name="type" class="form-control" style="width: 190px;" id="type">
           		<option id="first" value="0" selected="selected">全部</option>
           		<option id="first" value="1">充值</option>
           		<option id="two" value="2">消费</option>
           		<option id="threee" value="3">积分兑换</option>
           		<option id="last" value="4">积分抵扣</option>
           		<option id="last" value="7">退换货</option>
           	</select>
           	
           	<span  class="input-group-addon">开始时间</span> 
			<input id = "beginTime" type="text" name="beginTime" class="form-control form-date" placeholder="选择开始日期" readonly  style="width: 200px;">
			<span  class="input-group-addon">结束时间</span> 
			<input id = "endTime" type="text"	name="endTime" class="form-control form-date" placeholder="选择结束日期" readonly style="width: 200px;">
        </div>
			<a id="reportUserCard" class="btn btn-small btn-danger" style="width: auto;margin-top:5px;">导出</a>
	</m:list>
</body>
</html>