<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品库存列表</title>
<script type="text/javascript">
/* $(document).ready(function(){
	getProductAttributeList();
});
function getProductAttributeList(){
	var productId=$("#productId").val();
	lm.post("${contextPath}/productStock/list/ajax/productAttributeList",{productId:productId},function(data){
		$("thead").children().append("<th>商品名称</th>");
		$("thead").children().append("<th>库存</th>");
		$("thead").children().append("<th>价格</th>");
		for(var i=0;i<data.length;i++){
			$("thead").children().append("<th>"+data[i].name+"</th>");
		}
		$("thead").children().append("<th>操作</th>");
	});
	lm.post("${contextPath}/productStock/list/ajax/productAttributeValueList",{productId:productId},function(data){
		for(var i=0;i<data.length;i++){
			$("tbody").append("<tr></tr>");
			$("tbody").children().append("<td>"+data[i].productName+"</td>");
			$("tbody").children().append("<td>"+data[i].stock+"</td>");
			$("tbody").children().append("<td>"+data[i].price+"</td>");
			var arr= new Array();
			arr=data[i].pavList;
			
			for(var j=0;j<arr.length;j++){
				$("tbody").children().append("<td>"+arr[j].value+"</td>");
			}
			$("tbody").children().append("<td>"+
											"<a  onclick='deleteProductStock(${productStock.id});' id='deletebutton' class='btn btn-small btn-danger'>删除</a>"+
											"<a href='${contextPath }/productStock/update/${productStock.id}' class='btn btn-primary'>修改</a>"+
										"</td>");
			
		}
	});
} */
function deleteProductStock(id){
	var productId=$("#productId").val();
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/productStock/delete/delete-by-productStockId",{id:id},function(data){
			if(data==1){
				lm.alert("删除成功");
			}
			 window.location.href="${contextPath }/product/list";
		});
	});
}
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-heading">
			<strong><i class="icon-list-ul"></i> 库存列表</strong>
			<div class="panel-actions">
				<a href="${contextPath }/productStock/add/${productId }" class="btn btn-primary"><i
					class="icon-plus"></i>添加</a>
			</div>
		</div>
		<table class='table table-hover table-striped table-bordered'>
			<thead>
				<tr class='text-center'>
					<th>商品名称</th>
					<th>库存</th>
					<th>警报值</th>
					<th>价格</th>
					<th>条形码</th>
					<c:forEach items="${productAttributeList}" var="productAttribute">
					<th>${productAttribute.name }</th>
					</c:forEach>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${psList}" var="productStock">
					<tr>
						<td>${productStock.productName }</td>
						<td>${productStock.stock }</td>
						<td>${productStock.alarmValue }</td>
						<td>${productStock.price }</td>
						<td>${productStock.barCode }</td>
					<c:forEach items="${productStock.pavList }" var="productAttributeValue">
						<td>${productAttributeValue.value }</td>
					</c:forEach> 
						<td> 
							<a href='${contextPath }/productStock/update/${productStock.id}' class='btn btn-primary'>修改</a>
						 	<a  onclick='deleteProductStock(${productStock.id});' id="deletebutton"class="btn btn-small btn-danger">删除</a> 
							<a href='${contextPath }/productStock/uploadImage/${productStock.id}' class='btn btn-primary'>上传图片</a> 
						</td>
					</tr>
				</c:forEach>
			</tbody>

		</table>
	</div>
</body>
</html>