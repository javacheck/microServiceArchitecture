<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>属性列表</title>
<script type="text/javascript">
function deleteProductAttribute(id){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/productAttribute/delete/delete-by-productAttributeId",{id:id},function(data){
			if(data==0){
				lm.alert("该属性已设属性值，不能删除！");
				return;
			}else{
				lm.alert("删除成功");
			}
			 window.location.href="${contextPath}/productAttribute/list";
		});
	});
}
</script>
</head>
<body>
	<m:list title="属性列表" id="productAttributeList"
		listUrl="${contextPath }/productAttribute/list/list-data" addUrl="${contextPath }/productAttribute/add"
		searchButtonId="cateogry_search_btn" >
		<div class="input-group">
			<span class="input-group-addon">名称</span> <input type="text"
				name="name" class="form-control" placeholder="名称" style="width: 200px;">
		</div>
	</m:list>
</body>
</html>