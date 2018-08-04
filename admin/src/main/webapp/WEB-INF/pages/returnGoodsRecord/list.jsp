<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- ztree.core核心包 -->
	<script type="text/javascript" src="${staticPath }/js/ztree/jquery.ztree.core-3.5.js"></script>
	<!-- 升级树控件 -->
	<script type="text/javascript" src="${staticPath }/js/ztree/jquery.ztree.exhide-3.5.js"></script>
	<!-- 树形样式 -->
	<link rel="stylesheet" href="${staticPath }/css/ztree/zTreeStyle/zTreeStyle.css" type="text/css">	
<title>退货记录</title>
<script type="text/javascript">
var _isMain = false;
<c:if test="${isMainStore==true }">
var _mainStoreId = "${account_session_key.storeId}";
_isMain = true;
</c:if>

function callback(obj){
	$("#order_storeName").val(obj.name);
	$("#order_storeId").val(obj.id);
	$("#treeStoreId").val(obj.id);
	
	if (_mainStoreId == obj.id){
		$("#categoryId").find("option").val("");
		$("#categoryId").find("option").text("全部");
		$("#categoryId").prop("disabled",true);
	}else {
		$("#categoryId").prop("disabled",false);
	}
}
function deleteId(id){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/returnGoodsRecord/delete/delete-by-Id",{id:id},function(data){
			if(data==1){
				lm.alert("删除成功！");
			} else {
				lm.alert("删除失败！");
			}
			loadCurrentList_returnGoodsRecordList();
		});
	});
}

$(document).ready(function(){
	// 点击弹出商家选择窗口
	$("#order_storeName").click(function (){
		$("#orderShowStore").modal();
	});
	
	if (_isMain){
		$("#categoryId").prop("disabled",true);
	}
});

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
function formRest(){
	$("#treeStoreId").val("");
	$("#categoryId").find("option").val("");
	$("#categoryId").find("option").text("全部");
}
function getExe() {
	var storeId=$("#storeId").val();//店铺
	if( null == storeId || undefined == storeId ){
		storeId = "";
	} 
	var orderId=$("#orderId").val();//订单编号
	var productName=$("#productName").val();//商品名称
	var barcode=$("#barcode").val();//条码
	var categoryId=$("#categoryId").val();//分类
	var beginTime =$("#beginTime").val();//开始时间
	var endTime = $("#endTime").val();//结束时间
	
	window.open("${contextPath}/returnGoodsRecord/list/ajax/list-by-search?beginTime="+beginTime+"&endTime="+endTime
					+"&storeId="+storeId+"&productName="+productName+"&barcode="+barcode
					+"&orderId="+orderId+"&categoryId="+categoryId);
}
</script>
</head>
<body>
	<m:list title="退货记录" id="returnGoodsRecordList" formReset="formRest"
		listUrl="${contextPath }/returnGoodsRecord/list-data"
		searchButtonId="returnGoodsRecord_cateogry_search_btn">
		
		<div class="input-group" style="max-width: 600px;">
   			<c:if test="${isMainStore==true }">
				<span class="input-group-addon">商家</span>
				<input name="storeName" id="order_storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="" class="form-control" isRequired="1" tipName="商家"  />
				<input type="hidden" name="storeId" id="order_storeId" value="" />
			</c:if>
					
            <span class="input-group-addon">订单编号</span> 
            <input type="text" id="orderId" name="orderId" class="form-control" placeholder="订单编号" style="width: 200px;margin-right:40px;">
           
            <span class="input-group-addon">商品名称</span> 
            <input type="text" id="productName" name="productName" class="form-control" placeholder="商品名称" style="width: 200px;margin-right:40px;">
            
            <span class="input-group-addon">商品条码</span> 
            <input type="text" id="barcode" name="barcode" class="form-control" placeholder="商品条码" style="width: 200px;margin-right:40px;">
            
			</div>
			<br/>
			<div class="input-group" style="max-width: 1000px;">	        
            <span class="input-group-addon">商品分类</span>
	        	<m:treeSelect treeRoot="treeRoot" showAllOption="1" selectID="categoryId" ></m:treeSelect>
	        	<input type="hidden" name="treeStoreId" id="treeStoreId" value="">
			
			<span  class="input-group-addon">开始时间</span> 
			<input id = "beginTime" type="text" name="beginTime" class="form-control" placeholder="选择开始日期" readonly  style="width: 200px;margin-right:40px;">
			
			<span  class="input-group-addon">结束时间</span> 
			<input id = "endTime" type="text" name="endTime" class="form-control" placeholder="选择结束日期" readonly style="width: 200px;margin-right:40px;">
			
		</div>
		<button type="button" onclick="getExe()" class="btn btn-small btn-danger" style="width: auto;margin-top:5px;">导出</button>
	</m:list>
	<m:select_store path="${contextPath}/order/showModel/list/list-data" modeId="orderShowStore" callback="callback"> </m:select_store>
</body>
</html>