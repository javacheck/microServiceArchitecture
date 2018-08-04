<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>售后记录列表</title>
	
	<!-- ztree.core核心包 -->
	<script type="text/javascript" src="${staticPath }/js/ztree/jquery.ztree.core-3.5.js"></script>
	<!-- 升级树控件 -->
	<script type="text/javascript" src="${staticPath }/js/ztree/jquery.ztree.exhide-3.5.js"></script>
	<!-- 树形样式 -->
	<link rel="stylesheet" href="${staticPath }/css/ztree/zTreeStyle/zTreeStyle.css" type="text/css">	
	
<script type="text/javascript">

var _isMain = false;
<c:if test="${isMainStore==true }">
var _mainStoreId = "${account_session_key.storeId}";
_isMain = true;
</c:if>

function callback(obj){
	$("#afterSales_storeName").val(obj.name);
	$("#afterSales_storeId").val(obj.id);
	$("#treeStoreId").val(obj.id);
	
	if (_mainStoreId == obj.id){
		$("#categoryId").find("option").val("");
		$("#categoryId").find("option").text("全部");
		$("#categoryId").prop("disabled",true);
	}else {
		$("#categoryId").prop("disabled",false);
	}
}
	$(function(){
		if (_isMain){
			$("#categoryId").prop("disabled",true);
		}
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
	
	function formReset(){
		$("#treeStoreId").val("");
		$("#categoryId").find("option").val("");
		$("#categoryId").find("option").text("全部");
		$("#afterSales_storeId").val("");
		if (_isMain){
			$("#categoryId").prop("disabled",true);
		}
	}
	
	function getExe() {
		var storeId=$("#afterSales_storeId").val();//店铺
		if( null == storeId || undefined == storeId ){
			storeId = "";
		} 
		var orderId=$("#orderId").val();//订单编号
		var productName=$("#productName").val();
		var categoryId=$("#categoryId").val();
		var accountMobile=$("#accountMobile").val();
		var afterSalesType=$("#afterSalesType option:selected").val();
		var beginTime =$("#beginTime").val();//开始时间
		var endTime = $("#endTime").val();//结束时间
		
		window.open("${contextPath}/afterSales/list/ajax/list-by-search?storeId=" + storeId + "&orderId=" + orderId + "&productName=" + productName + 
				"&categoryId=" + categoryId + "&accountMobile=" + accountMobile + "&afterSalesType=" + afterSalesType + 
				"&beginTime=" + beginTime + "&endTime=" + endTime);
	}
</script>

</head>

<body>

<!-- 售后记录列表DIV   start --> 				            							
<m:hasPermission permissions="afterSalesAdd" flagName="addFlag"/>
	<m:list title="售后记录" id="afterSalesList"
		listUrl="${contextPath }/afterSales/list-data"
		addUrl="${((addFlag == true)&&(isSys == false)&&(isMainStore == false)) ? '/afterSales/add' : '' }"
		searchButtonId="afterSales_search_btn" formReset="formReset">

		<input type="hidden" name="productStockArray" id="productStockArray" value=""/>

		<div class="input-group" style="max-width:1000px;">
			<c:choose>
				<c:when test="${isSys==true }">
					<span class="input-group-addon">商　家</span>
					<input value='' name="storeName" id="afterSales_storeName" readOnly="readOnly" placeholder="请选择商家" style="width: 200px;margin-right:40px;" class="form-control" isRequired="1" tipName="商家"/>
					<input type="hidden" name="storeId" id="afterSales_storeId" value="" />
				</c:when>
				<c:otherwise>
					<c:if test="${isMainStore==true }">
						<span class="input-group-addon">商　家</span>
						<input value='${store_session_key.name}' name="storeName" id="afterSales_storeName" readOnly="readOnly" placeholder="请选择商家" style="width: 200px;margin-right:40px;" class="form-control" isRequired="1" tipName="商家"/>
						<input type="hidden" name="storeId" id="afterSales_storeId" value="" />
					</c:if>
				</c:otherwise>
			</c:choose>

			<span class="input-group-addon">订单号</span> 
				<input type="text" id="orderId" name="orderId" class="form-control" placeholder="输入订单号" style="width: 200px;margin-right:40px;">

			<span class="input-group-addon">商品名称</span> 
				<input type="text" id="productName" name="productName" class="form-control" placeholder="输入商品名称" style="width: 200px;margin-right:40px;">

			<span class="input-group-addon">商品分类</span>
	        	<m:treeSelect treeRoot="treeRoot" showAllOption="1" selectID="categoryId" ></m:treeSelect>
	        	<input type="hidden" name="treeStoreId" id="treeStoreId" value="">
		</div>
		<br/>
		<div class="input-group" style="max-width:1000px;">

			<span class="input-group-addon">操作人</span> 
				<input type="text" id="accountMobile" name="accountMobile" class="form-control" placeholder="操作人用户名" style="width: 200px;margin-right:40px;">

			<span class="input-group-addon">售后类型</span>
				<select class="form-control" id="afterSalesType" style="width: auto;margin-right:40px;" name="afterSalesType"> 
					<option value="">全部</option>
					<c:forEach items="${afterSalesTypeList }" var="types">
						<option value="${types.id }">${types.name }</option>
					</c:forEach>
				</select>

			<span  class="input-group-addon">开始时间</span> 
				<input id = "beginTime" type="text" name="beginTime" class="form-control" placeholder="选择开始日期" readonly  style="width: 200px;margin-right:40px;">

			<span  class="input-group-addon">结束时间</span> 
				<input id = "endTime" type="text" name="endTime" class="form-control" placeholder="选择结束日期" readonly style="width: 200px;margin-right:40px;">

		</div>
		<a id="reportafterSales" onclick="getExe()" class="btn btn-small btn-danger" style="width: auto;margin-top:5px;">导出</a>
	</m:list> 
<!-- 商品列表DIV   end -->  
	<m:select_store path="${contextPath}/order/showModel/list/list-data" modeId="afterSalesShowStore" callback="callback"> </m:select_store>
</body>

</html>