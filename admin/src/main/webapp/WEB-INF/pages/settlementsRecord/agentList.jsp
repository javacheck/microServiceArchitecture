<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>代理商账户流水账列表</title>
<script type="text/javascript">
$(function(){
	var format = "yyyy-MM-dd";//默认格式
	var now = lm.Now;//当前日期
	var nowFormat=now.Format(format);
	$("#startTime").datetimepicker({
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
		if((new Date(ev.date.valueOf()))<(new Date($('#startTime').val().replace(/-/g,"/")))){//开始时间大于结束时间
			lm.alert("结束时间不能大于开始时间");
			$("#endTime").val(endDateInput);//默认值上一次输入
			$('#endTime').datetimepicker('update');//更新
		}
		endDateInput= $("#endTime").val();
		$('#startTime').datetimepicker('setEndDate', $("#endTime").val());//设置开始时间最后可选结束时间
	});
});

//点击搜索时先判断价格参数的正确性
function checkParameter(){
	var orderId = $("input[name='orderId']").val();
	orderId = $.trim(orderId); // 用jQuery的trim方法删除前后空格
	if( null != orderId && orderId != "" ){
		if( !(/^[0-9]*$/.test(orderId)) ){
			lm.alert("请输入正确的订单编号！");
			return ;
		}
	}
	
	return true; 
}
function getExe() {
	var orderId=$("#orderId").val();//订单编号
	//var type=$("#type").val();//户主类型
	//var name=$("#name").val();//户主名称
	var startTime =$("#startTime").val();//开始时间
	var endTime = $("#endTime").val();//结束时间
	window.open("${contextPath}/settlementsRecord/list/ajax/list-by-search?startTime="+startTime+"&endTime="+endTime
					+"&orderId="+orderId);
}
</script>
</head>
<body>
	<m:list title="代理商账户流水账列表" id="agentList"
		listUrl="${contextPath }/settlementsRecord/list-data"
		searchButtonId="cateogry_search_btn" beforeSearch="checkParameter">
		 <div class="input-group" style="max-width: 600px;"> 
		 	
            <span class="input-group-addon">订单编号</span> 
            	<input type="text" id="orderId" name="orderId" maxlength="15" value="" class="form-control" placeholder="订单编号" style="width: 200px;">
           	
           	<span  class="input-group-addon">开始时间</span> 
				<input id = "startTime"  type="text"    name="startTime" class="form-control form-date" placeholder="开始时间" readonly  style="width: 200px;">
			
			<span  class="input-group-addon" >结束时间</span> 
				<input id = "endTime"  type="text"	name="endTime" class="form-control form-date" placeholder="结束时间" readonly style="width: 200px;">
		</div>
		<a href="" onclick="getExe()" class="btn btn-small btn-danger" style="width: auto;margin-top:5px;">导出流水</a>
	</m:list>
</body>
</html>