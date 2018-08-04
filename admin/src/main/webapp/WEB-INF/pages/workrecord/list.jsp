<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>交接班记录</title>
	
<script type="text/javascript">
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
			endDate:""+nowFormat+""//默认最后可选择为当前日期
			
		 });
		$("#endDate").datetimepicker({
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
		$('#endDate').datetimepicker().on('changeDate', function(ev){
			if((new Date(ev.date.valueOf()))<(new Date($('#startDate').val().replace(/-/g,"/")))){//开始时间大于结束时间
				lm.alert("结束时间不能大于开始时间");
				$("#endDate").val(endDateInput);//默认值上一次输入
				$('#endDate').datetimepicker('update');//更新
			}
			endDateInput= $("#endTime").val();
			$('#startDate').datetimepicker('setEndDate', $("#endDate").val());//设置开始时间最后可选结束时间
		});
	});
	
	function listCallback(){
		$("[name='detail_btn']").click(function(){
			var detail = $("#workrecordDetail");
			var t = $(this).parent().parent();
			
			detail.find("[name='id']").html(t.find("[name='id']").html());
			detail.find("[name='startDate']").html(t.find("[name='startDate']").html());
			detail.find("[name='endDate']").html(t.find("[name='endDate']").html());
			detail.find("[name='accountName']").html(t.find("[name='accountName']").html());
			detail.find("[name='accountMobile']").html(t.find("[name='accountMobile']").html());
			detail.find("[name='totalNum']").html(t.find("[name='totalNum']").html()+"笔");
			detail.find("[name='sales']").html("￥" + t.find("[name='sales']").html());
			detail.find("[name='alipayNum']").html(t.find("[name='alipayNum']").val()+"笔");
			detail.find("[name='alipay']").html("￥" +t.find("[name='alipay']").val());
			
			detail.find("[name='userCardNum']").html(t.find("[name='userCardNum']").val()+"笔");
			detail.find("[name='userCard']").html("￥" +t.find("[name='userCard']").val());
			
			detail.find("[name='wxPayNum']").html(t.find("[name='wxPayNum']").val()+"笔");
			detail.find("[name='wxPay']").html("￥" +t.find("[name='wxPay']").val());
			detail.find("[name='bankCardPayNum']").html(t.find("[name='bankCardPayNum']").val()+"笔");
			detail.find("[name='bankCardPay']").html("￥" +t.find("[name='bankCardPay']").val());
			detail.find("[name='cashPayNum']").html("");
			detail.find("[name='cashPay']").html("￥" +t.find("[name='cashPay']").val());
			
			detail.find("[name='orderNum']").html(t.find("[name='orderNum']").val()+"笔");
			detail.find("[name='orderSales']").html("￥" +t.find("[name='orderSales']").val());
			
			detail.find("[name='userCardRechargeNum']").html(t.find("[name='userCardRechargeNum']").val()+"笔");
			detail.find("[name='userCardRechargeSales']").html("￥" +t.find("[name='userCardRechargeSales']").val());
			
			detail.modal();
		});
	}
	function getExe() {
		var accountName=$("#accountName").val();//收银员姓名
		var accountMobile =$("#accountMobile").val();//手机号码
		var startDate = $("#startDate").val();//开始时间
		var endDate = $("#endDate").val();//结束时间
		window.open("${contextPath}/workrecord/list/ajax/workrecordList-by-search?accountName="+accountName
						+"&accountMobile="+accountMobile+"&startDate="+startDate+"&endDate="+endDate);
	}	
</script>
</head>
<body>
	<m:list title="交接班记录" id="workrecordList"
		listUrl="${contextPath }/workrecord/list-data" callback="listCallback"
		searchButtonId="user_search_btn">
		<div class="input-group" style="max-width: 1200px;">
			<span class="input-group-addon">收银员姓名</span> 
			<input type="text" id="accountName" name="accountName" class="form-control" placeholder="请输入会员姓名" style="width: 200px;">
			<span class="input-group-addon">手机号码</span> 
			<input type="text" id="accountMobile" name="accountMobile" class="form-control" placeholder="请输入手机号码" style="width: 200px;">
			<span  class="input-group-addon">开始时间</span> 
			<input id = "startDate" type="text" name="startDate" class="form-control form-date" placeholder="选择开始日期" readonly  style="width: 200px;">
			<span  class="input-group-addon">结束时间</span> 
			<input id = "endDate" type="text"	name="endDate" class="form-control form-date" placeholder="选择结束日期" readonly style="width: 200px;">
		</div>
		<button type="button" onclick="getExe();" class="btn btn-small btn-danger" style="width: auto;margin-top:5px;">导出报表</button>
	</m:list>
	
	
	<div class="modal fade" id="workrecordDetail">
		<div class="modal-dialog ">
		  <div class="modal-content">
		    <div class="modal-header">
		      <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
		      <h4 class="modal-title">交接详情</h4>
		    </div>
		    <div class="modal-body">
		    	<div class="row">
			    	<div class="col-sm-3">
			    		编号：
			    	</div>
			    	<div class="col-sm-6">
			    		
			    	</div>
			    	<div class="col-sm-3" name="id">
			    		
			    	</div>
		    	</div>
		    	<div class="row">
			    	<div class="col-sm-3">
			    		开始时间：
			    	</div>
			    	<div class="col-sm-6">
			    		
			    	</div>
			    	<div class="col-sm-3" name="startDate">
			    		
			    	</div>
		    	</div>
		    	<div class="row">
			    	<div class="col-sm-3">
			    		交班时间：
			    	</div>
			    	<div class="col-sm-6">
			    		
			    	</div>
			    	<div class="col-sm-3" name="endDate">
			    		
			    	</div>
		    	</div>
		    	<div class="row">
			    	<div class="col-sm-3">
			    		收银员：
			    	</div>
			    	<div class="col-sm-6">
			    		
			    	</div>
			    	<div class="col-sm-3" name="accountName">
			    		
			    	</div>
		    	</div>
		    	<div class="row">
			    	<div class="col-sm-3">
			    		手机号：
			    	</div>
			    	<div class="col-sm-6">
			    		
			    	</div>
			    	<div class="col-sm-3" name="accountMobile">
			    		
			    	</div>
		    	</div>
		    	<hr/>
		    	
		    	<div class="row">
			    	<div class="col-sm-3">
			    		销售订单：
			    	</div>
			    	<div class="col-sm-6" name="orderNum">
			    		
			    	</div>
			    	<div class="col-sm-3" name="orderSales">
			    		
			    	</div>
		    	</div>
		    	
		    	<div class="row">
			    	<div class="col-sm-3">
			    		会员充值：
			    	</div>
			    	<div class="col-sm-6" name="userCardRechargeNum">
			    		
			    	</div>
			    	<div class="col-sm-3" name="userCardRechargeSales">
			    		
			    	</div>
		    	</div>
		    	
		    	<div class="row">
			    	<div class="col-sm-3">
			    		合计：
			    	</div>
			    	<div class="col-sm-6" name="totalNum">
			    		
			    	</div>
			    	<div class="col-sm-3" name="sales">
			    		
			    	</div>
		    	</div>
		    	<hr/>
		    	
		    	<div class="row">
			    	<div class="col-sm-3">
			    		支付宝收入：
			    	</div>
			    	<div class="col-sm-6" name='alipayNum'>
			    		
			    	</div>
			    	<div class="col-sm-3" name="alipay">
			    		
			    	</div>
		    	</div>
		    	
		    	<div class="row">
			    	<div class="col-sm-3">
			    		微信收入：
			    	</div>
			    	<div class="col-sm-6" name='wxPayNum'>
			    		
			    	</div>
			    	<div class="col-sm-3" name="wxPay">
			    		
			    	</div>
		    	</div>
		    	
		    	<div class="row">
			    	<div class="col-sm-3">
			    		刷卡收入：
			    	</div>
			    	<div class="col-sm-6" name='bankCardPayNum'>
			    		
			    	</div>
			    	<div class="col-sm-3" name="bankCardPay">
			    		
			    	</div>
		    	</div>
		    	
		    	<div class="row">
			    	<div class="col-sm-3">
			    		现金收入：
			    	</div>
			    	<div class="col-sm-6">
			    		
			    	</div>
			    	<div class="col-sm-3" name="cashPay">
			    		
			    	</div>
		    	</div>
		    	<div class="row">
			    	<div class="col-sm-3">
			    		会员卡支付：
			    	</div>
			    	<div class="col-sm-6">
			    		
			    	</div>
			    	<div class="col-sm-3" name="userCard">
			    		
			    	</div>
		    	</div>
		    </div>
		    <div class="modal-footer">
		    </div>
		  </div>
		</div>
	</div>
</body>
</html>