<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>优惠列表</title>
<script type="text/javascript">
function reportCashGiftBarcode(promotionCouponId){
	location.href ="${contextPath }/promotionCoupon/ajax/reportCashGiftBarcode?promotionCouponId="+promotionCouponId;
	$("#reportCashGiftBarcode").text("重新导出");
}
$(function(){
	var format = "yyyy-MM-dd";//默认格式
	var now = lm.Now;//当前日期
	var nowFormat=now.Format(format);
	$("#startDate").datetimepicker({
		minView: "month", //选择日期后，不会再跳转去选择时分秒 
		format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
		language: 'zh-CN', //汉化 
		autoclose:true , //选择日期后自动关闭
		todayBtn:true,//可选择当天按钮
		todayHighlight:false,//高亮当前日期
		//endDate:""+nowFormat+""//默认最后可选择为当前日期
		
	 });
	$("#endDate").datetimepicker({
		minView: "month", //选择日期后，不会再跳转去选择时分秒 
		format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
		language: 'zh-CN', //汉化 
		autoclose:true, //选择日期后自动关闭 
		todayBtn:true,//可选择当天按钮
		todayHighlight:false,//高亮当前日期
		//endDate:""+nowFormat+""//默认最后可选择为当前日期
	 });
	var endDateInput=nowFormat;
	//判断输入结束日期时候输入合法
	$('#endDate').datetimepicker().on('changeDate', function(ev){
		if((new Date(ev.date.valueOf()))<(new Date($('#startDate').val().replace(/-/g,"/")))){//开始时间大于结束时间
			lm.alert("结束时间不能大于开始时间");
			$("#endDate").val(endDateInput);//默认值上一次输入
			$('#endDate').datetimepicker('update');//更新
		}
		endDateInput= $("#endDate").val();
		$('#startDate').datetimepicker('setEndDate', $("#endDate").val());//设置开始时间最后可选结束时间
	});
});

</script>
<style>
  .input-group2{
  float:left;width:auto; background-color: #e5e5e5;border: 1px solid #ccc;border-radius: 4px; color: #222;font-size: 13px;font-weight: 400; padding: 5px 12px;text-align: center;
  }
  </style>
</head>
<body>
	<m:hasPermission permissions="promotionCouponAdd" flagName="addFlag"/>
	<m:list title="优惠列表" id="promotionCouponList"
		listUrl="${contextPath }/promotionCoupon/list/list-data" 
		addUrl="${addFlag == true ? '/promotionCoupon/add' : '' }" 
		searchButtonId="cateogry_search_btn" >
		
		<div class="input-group" style="max-width:1500px;">
            <span class="input-group2">活动名称</span> 
            <input type="text" id="name" name="name" class="form-control"  style="width: 200px;float:left;margin-right:40px;">
            
            <span class="input-group2">类型</span> 
            <select name="type" class="form-control" style="margin-right:40px;width: auto;" id="type">
           		<option  value ="">全部</option>
				<option  value ="0">折扣券</option>
				<option  value ="1">现金券</option>
           	</select>
            <span class="input-group2">状态</span> 
            <select name="status" class="form-control" style="margin-right:40px;width: auto;" id="status">
           		<option  value ="">全部</option>
				<option  value ="0">已停止</option>
				<option  value ="1">进行中</option>
				<option  value ="2">即将开始</option>
           	</select>
            
            <span  class="input-group-addon">开始时间</span> 
				<input id = "startDate"  type="text"    name="startDate" class="form-control form-date" placeholder="开始时间" readonly  style="width: 200px;float:left;margin-right:40px;">
			
			<span  class="input-group-addon" >结束时间</span> 
				<input id = "endDate"  type="text"	name="endDate" class="form-control form-date" placeholder="结束时间" readonly style="width: 200px;float:left;margin-right:40px;">
		</div>
	</m:list>
</body>
</html>