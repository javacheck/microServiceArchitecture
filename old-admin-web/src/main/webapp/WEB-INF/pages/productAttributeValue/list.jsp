<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>属性值列表</title>
<script type="text/javascript">
function deleteProductAttributeValue(id){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/productAttributeValue/delete/delete-by-productAttributeValueId",{id:id},function(data){
			if(data==1){
				lm.alert("删除成功");
			}
			 window.location.href="${contextPath}/productAttributeValue/list";
		});
	});
}
</script>
</head>
<body>
	<m:list title="属性值列表" id="productAttributeValueList"
		listUrl="${contextPath }/productAttributeValue/list/list-data" addUrl="${contextPath }/productAttributeValue/add"
		searchButtonId="cateogry_search_btn" >
		<div class="input-group">
			<span class="input-group-addon">名称</span> <input type="text"
				name="name" class="form-control" placeholder="名称" style="width: 200px;">
		</div>
	</m:list>
</body>
</html>