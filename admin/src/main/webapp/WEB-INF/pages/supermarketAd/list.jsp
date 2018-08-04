<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商超广告列表</title>
<script type="text/javascript">
function callback(obj){
	$("#supermarketAd_storeName").val(obj.name);
	$("#supermarketAd_storeId").val(obj.id);
}

function deleteId(id,storeId){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/supermarketAd/delete/delete-by-Id",{id:id,storeId:storeId},function(data){
			if(data==1){
				lm.alert("删除成功！");
			} else {
				lm.alert("删除失败！");
			}
			loadCurrentList_supermarketAdList();
		});
	});
}
function formReset(){
	$("#supermarketAd_storeName").val("");
	$("#supermarketAd_storeId").val("");
}

$(function(){
	// 点击弹出商家选择窗口
	$("#supermarketAd_storeName").click(function (){
		$("#supermarketAdShowStore").modal();
	});
});
</script>
</head>
<body>
	<m:hasPermission permissions="supermarketAdAdd" flagName="addFlag"/>
	<m:list title="商超广告列表" id="supermarketAdList"
		listUrl="/supermarketAd/list-data"
		addUrl="${addFlag == true ? '/supermarketAd/add' : '' }"
		searchButtonId="supermarketAdAdd_search_btn" formReset="formReset">
		
		<div class="input-group" style="max-width: 600px;">
			<span class="input-group-addon">商家</span>
			<input type="text" name = "storeName"  readonly="readonly" style="width: 200px;margin-right:40px;"  class='form-control' id = "supermarketAd_storeName"  value="${supermarketAd.storeName }"  >
						<input type="hidden" name = "storeId" class='form-control' id = "supermarketAd_storeId"  value="${supermarketAd.storeId  }"  >
		</div>
	</m:list>
<m:select_store path="${contextPath}/supermarketAd/showModel/list/list-data" modeId="supermarketAdShowStore" callback="callback"> </m:select_store>
</body>
</html>