<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>店铺列表</title>
<script type="text/javascript">
/* function callback() {
	$("[name='deleteStoreBtn']").click(function() {
		var e = $(this);
		var id = e.attr("val");
		lm.confirm("确定要删除吗？",function(){
			lm.post("${contextPath}/store/delete/"+id,function(data){
				lm.alert("操作成功");
				e.parent().parent().remove();
			});
		});
	});
} */
function deleteStore(id){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/store/delete/delete-by-storeId",{id:id},function(data){
			if(data==1){
				lm.alert("删除成功");
			}
			 window.location.href="${contextPath}/store/list";
		});
	});
}
</script>
</head>
<body>
	<m:list title="店铺列表" id="storeList"
		listUrl="${contextPath }/store/list/list-data"
		addUrl="${contextPath }/store/add" callback="callback"
		searchButtonId="cateogry_search_btn">
		<div class="input-group">
			<span class="input-group-addon">名称</span> <input type="text"
				name="name" class="form-control" placeholder="名称"
				style="width: 200px;">
		</div>
	</m:list>
</body>
</html>