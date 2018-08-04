<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>支付宝授权情况列表</title>
<script type="text/javascript">
$(function(){
	// 点击弹出商家选择窗口
	$("#storeName").click(function (){
		$("#alipayAuthSituationShowStore").modal();
	});
});

function callback(obj){
	$("#storeName").val(obj.name);
	$("#storeId").val(obj.id);
}

function formReset(){
	$("#storeName").val("");
	$("#storeId").val("");
}

</script>
</head>
<body>
	<m:list title="支付宝授权情况列表" id="alipayAuthSituationList"
		listUrl="${contextPath }/alipayAuthSituation/list/list-data"
		searchButtonId="alipayAuthSituation_cateogry_search_btn"
		formReset="formReset">
		
		<div class="input-group" style="max-width: 700px;">
		
		 <span class="input-group-addon">商家</span> 
            	<input type="text" id="storeName" name="storeName" readonly="readonly" class="form-control" placeholder="商家" style="width: 200px;">
            	<input type="hidden" name="storeId" id="storeId" value="" />
		 <span class="input-group-addon">响应状态码</span> 
            	<input type="text" id="code" name="code" class="form-control" placeholder="响应状态码" style="width: 200px;">
		</div>
	</m:list>
	<m:select_store path="${contextPath}/commodityManager/showModel/list/list-data" modeId="alipayAuthSituationShowStore" callback="callback"> </m:select_store>
</body>
</html>