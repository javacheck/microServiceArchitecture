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
	$("#storeAdImage_storeName").val(obj.name);
	$("#storeAdImage_storeId").val(obj.id);
}

function deleteId(type,storeId){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/storeAdImage/delete/delete-by-Id",{type:type,storeId:storeId},function(data){
			if(data==1){
				lm.alert("删除成功！");
			} else {
				lm.alert("删除失败！");
			}
			loadCurrentList_storeAdImageList();
		});
	});
}
function formReset(){
	$("#storeAdImage_storeName").val("");
	$("#storeAdImage_storeId").val("");
}

$(function(){
	// 点击弹出商家选择窗口
	$("#storeAdImage_storeName").click(function (){
		$("#storeAdImageShowStore").modal();
	});
});
</script>
</head>
<body>
	<m:hasPermission permissions="storeAdImageAdd" flagName="addFlag"/>
	<m:list title="商广图片列表" id="storeAdImageList"
		listUrl="/storeAdImage/list-data"
		addUrl="${addFlag == true ? '/storeAdImage/add' : '' }"
		searchButtonId="storeAdImage_search_btn" formReset="formReset">
		
		<div class="input-group" style="max-width: 600px;">
			<span class="input-group-addon">商家</span>
			<input type="text" name = "storeName"  readonly="readonly" style="width: 200px;margin-right:40px;"  class='form-control' id = "storeAdImage_storeName"  value="${storeAdImage.storeName }"  >
						<input type="hidden" name = "storeId" class='form-control' id = "storeAdImage_storeId"  value="${storeAdImage.storeId  }"  >
		</div>
	</m:list>
<m:select_store path="${contextPath}/supermarketAd/showModel/list/list-data" modeId="storeAdImageShowStore" callback="callback"> </m:select_store>
</body>
</html>