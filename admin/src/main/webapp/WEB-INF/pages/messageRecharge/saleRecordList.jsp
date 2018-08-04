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
@media ( min-width :992px) {
	.modal-sale-show {
		width: 620px;
	}
}
</style>
<title>消费记录列表</title>
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
	
	function operationDetailFunction(obj){
		$("#userAccountMobileSHOW").html($(obj).prevAll().val());
		$("#sendTimeSHOW").html($(obj).prevAll().prevAll().prevAll().val());
		$("#messageContentSHOW").html($(obj).prevAll().prevAll().val());
		$("#messageRechargeSaleShowID").modal();
	}
	
	$(function(){
		$("#SaleShowSure").click(function(){
			$("#SaleShowClose").click();
		});
	});
	
	$(function(){
		$("#reportMessageRechargeSale").click(function(){
			var userAccount = $("#userAccount").val(); // 开始时间
			userAccount = $.trim(userAccount);  // 用jQuery的trim方法删除前后空格
			
			var type = $("#type").val(); // 结束时间
			type = $.trim(type);  // 用jQuery的trim方法删除前后空格
			
			var searchTime = $("#searchTime").val(); // 会员号
			searchTime = $.trim(searchTime);  // 用jQuery的trim方法删除前后空格
			var storeId = $("#storeId").val();
			location.href = "${contextPath}/messageRecharge/ajax/reportMessageRechargeSaleToExcel?storeId="+storeId+"&userAccount="+userAccount+"&type="+type +"&searchTime="+searchTime;
			//window.open("${contextPath}/user/ajax/reportOrderToExcel?reportBeginTime="+reportBeginTime+"&reportEndTime="+reportEndTime +"&reportName="+reportName+"&reportMobile="+reportMobile);
		});
	});
	
</script>
</head>
<body>
	<m:list title="消费记录列表" id="messageRecharge_sale_List" beforeSearch="beforeSearch"
		listUrl="/messageRecharge/sale/record/list-data"
		searchButtonId="messageRecharge_sale_search_btn">
		<div class="input-group" style="max-width: 1300px;">
		
		<span  class="input-group-addon">会员帐号</span> 
			<input type="text" name="userAccount" class="form-control" placeholder="请输入会员帐号" style="width: 200px;">
			
		<span  class="input-group-addon">类型</span> 
			<select name="type" style="width: auto" class="form-control">
				<option value="-1">全部</option>
				<option value="1">通知</option>
		    </select>
		    
			<input type="hidden" id="storeId" name="storeId" value="${storeId }" style="width: 200px;">
			<span class="input-group-addon">消费时间</span>
			<input id="searchTime" value="" type="text" name="searchTime" class="form-control form-date" placeholder="选择消费时间" readonly style="width: 200px;">
		</div>
		 <a href='${contextPath }/messageRecharge/returnList/' class='btn btn-small btn-warning' style="width: auto;margin-top:5px;">返回短信充值列表</a>
		 <a id="reportMessageRechargeSale" class="btn btn-small btn-danger" style="width: auto;margin-top:5px;">导出</a>
	</m:list>
	
	<!-- 信息详情展示start -->
		  <div class="modal fade" id="messageRechargeSaleShowID">
			 <div class="modal-dialog modal-sale-show">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" id="SaleShowClose" name="SaleShowClose" class="close" data-dismiss="modal">
							<span aria-hidden="true">×</span><span class="sr-only">关闭</span>
						</button>
						<h4 class="modal-title" style="text-align: center;">短信息详情</h4>
					</div>
					   <table class="table table-hover table-striped table-bordered" cellpadding="0" cellspacing="0">
						 <tbody>
		            		<tr class="bg-co"  style="width: 100%;height: 30px">
		                    	<td colspan="2" width="100%"><span style="color:#176AE8">会员手机：</span><span id="userAccountMobileSHOW"></span></td>
		                	</tr>
		                	<tr class="bg-co"  style="width: 100%;height: 30px">
		                    	<td colspan="2" width="100%"><span style="color:#176AE8">发送时间：</span><span id="sendTimeSHOW"></span></td>
		                	</tr>
		                	<tr class="bg-co" style="width: 100%; height: 30px;">
								<td colspan="2" width="100%"><span style="color:#176AE8">信息内容：</span><span id="messageContentSHOW"></span></td>
		            		</tr>
		            		
		            		<tr class="bg-co" style="width: 100%; height: 30px;">
								<td colspan="2" width="100%" style="text-align: center;">
									<button id="SaleShowSure"  name="SaleShowSure" class='btn btn-small btn-warning'>确定</button>
								</td>
		            		</tr>
		            		
            		   </tbody>
					</table>
			   </div>
			</div>
		</div>
	  <!-- 信息详情展示end -->
</body>
</html>