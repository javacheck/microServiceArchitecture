<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品列表</title>
<script type="text/javascript">
function deleteProduct(id){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/product/delete/delete-by-productId",{id:id},function(data){
			if(data==1){
				lm.alert("删除成功");
			}
			 window.location.href="${contextPath}/product/list";
		});
	});
}
function getStockList(id){
	lm.post("${contextPath }/productStock/list/json/find-by-productId",{id:id},function(data){
		if(data==0){
			lm.alert("该商品还没有增加属性！");
		}else{
		 	window.location.href="${contextPath }/productStock/productStock/"+id;
		}
	});
}
</script>
</head>
<body>
	<m:list title="商品列表" id="productList"
		listUrl="${contextPath }/product/list/list-data" addUrl="${contextPath }/product/add" 
		searchButtonId="cateogry_search_btn" >
		<div class="input-group">
			<span class="input-group-addon">商品名称</span> <input type="text"
				name="name" class="form-control" placeholder="名称" style="width: 200px;">
		</div>
	</m:list>
</body>
</html>