<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设备列表</title>
<script type="text/javascript">
function callback(obj){
	$("#order_storeName").val(obj.name);
	$("#order_storeId").val(obj.id);
}
function deleteId(id){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/device/delete/delete-by-Id",{id:id},function(data){
			if(data==1){
				lm.alert("删除成功！");
			} else {
				lm.alert("删除失败！");
			}
			loadCurrentList_deviceList();
		});
	});
}

$(document).ready(function(){
	// 点击弹出商家选择窗口
	$("#order_storeName").click(function (){
		$("#orderShowStore").modal();
	});
	
	var storeHaveAll = $("#storeHaveAll").val();
	if(null == storeHaveAll || "" == storeHaveAll || storeHaveAll != "storeHaveAll"){
		$("#storeSelect option[id='allStore']").remove();
	}
});


</script>
</head>
<body>
	<m:hasPermission permissions="deviceAdd" flagName="addFlag"/>
	<m:list title="设备列表" id="deviceList"
		listUrl="${contextPath }/device/list-data"
		addUrl="${addFlag == true ? '/device/add' : '' }"
		searchButtonId="cateogry_search_btn">
		
		<input id="storeHaveAll" name="storeHaveAll" type="hidden" value="${storeHaveAll }" />
		
		<div class="input-group" style="max-width: 600px;">
            <c:choose>  
			   <c:when test="${isSys==true }">  
			   		<span class="input-group-addon">商家</span>
					<input name="storeName" id="order_storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="" class="form-control" isRequired="1" tipName="商家"  />
					<input type="hidden" name="storeId" id="order_storeId" value="" />
			   </c:when>  
			   <c:otherwise> 
		   			<c:if test="${isMainStore==true }">
						<span class="input-group-addon">商家</span>
						<input name="storeName" id="order_storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="" class="form-control" isRequired="1" tipName="商家"  />
						<input type="hidden" name="storeId" id="order_storeId" value="-1" />
					</c:if>
			   </c:otherwise>  
			</c:choose>  
			
            <span class="input-group-addon">终端SN号</span> 
            <input type="text" id="deviceName" name="deviceSn" class="form-control" placeholder="终端SN号" style="width: 200px;margin-right:40px;">
           
		</div>
	</m:list>
	<m:select_store path="${contextPath}/order/showModel/list/list-data" modeId="orderShowStore" callback="callback"> </m:select_store>
</body>
</html>