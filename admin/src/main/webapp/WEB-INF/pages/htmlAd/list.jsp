<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>广告列表</title>
<script type="text/javascript">
function callback(obj){
	$("#htmlAd_storeName").val(obj.name);
	$("#htmlAd_storeId").val(obj.id);
}

function deleteId(id){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/htmlAd/delete/delete-by-Id",{id:id},function(data){
			if(data==1){
				lm.alert("删除成功！");
			} else {
				lm.alert("删除失败！");
			}
			loadCurrentList_htmlAdList();
		});
	});
}
function formReset(){
	$("#htmlAd_storeName").val("");
	$("#htmlAd_storeId").val("");
}

$(function(){
	// 点击弹出商家选择窗口
	$("#htmlAd_storeName").click(function (){
		$("#htmlAdShowStore").modal();
	});
});
</script>
</head>
<body>
	<m:hasPermission permissions="htmlAdAdd" flagName="addFlag"/>
	<m:list title="广告列表" id="htmlAdList"
		listUrl="${contextPath }/htmlAd/list/list-data"
		addUrl="${addFlag == true ? '/htmlAd/add' : '' }"
		searchButtonId="cateogry_search_btn" formReset="formReset">
		
		<div class="input-group" style="max-width: 600px;">
			<span class="input-group-addon">商家</span>
			<input type="text" name = "storeName"  readonly="readonly" style="width: 200px;margin-right:40px;"  class='form-control' id = "htmlAd_storeName"  value=""  >
						<input type="hidden" name = "storeId" class='form-control' id = "htmlAd_storeId"  value=""  >
		</div>
	</m:list>
<m:select_store path="${contextPath}/htmlAd/showModel/list/list-data" modeId="htmlAdShowStore" callback="callback"> </m:select_store>
</body>
</html>