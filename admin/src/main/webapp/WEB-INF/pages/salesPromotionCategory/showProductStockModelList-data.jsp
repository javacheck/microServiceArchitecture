<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商家列表</title>
<script type="text/javascript">
$(function(){
	if(cacheArray.length > 0){ 
		$("#show_ProductStock_Model input[type='checkbox']").each(function(key,value){
			for (var i = 0; i < cacheArray.length; i++) {
				if(cacheArray[i] == ($(value).attr("stockIdSign") + "_" + $(value).attr("originalPriceSign") + "_" + $(value).attr("shelvesSign")) ){
					$(value).attr("checked",true);
					return; //在缓存数组中找到了当前的值就重新再在外围循环
				}
			}
		});
	}

	stock_salesNum = $("#stock_salesNum").val();
	if( -1 == stock_salesNum ){
		stock_salesNum = "";
	}
});
</script>
</head>
<body>
	<div class="panel" id="show_ProductStock_Model">
		<m:table pageNo="${page.pageNo }" pages="${page.pages }" pageSize="${page.pageSize }" total="${page.total }">
			<thead>
				<tr class='text-center'>
					<th>
						<input type="checkbox" id="pageALLCheckBox" onclick="checkPageALL(this);"/>
						<input type="hidden" id="stock_salesNum" name="stock_salesNum" value="${stock_salesNum}"/>
					</th>
					<th>商品ID</th>
					<th>商家名称</th>
					<th>商家手机号码</th>
					<th>商品名称</th>
					<th>商品库存</th>
					<th>成本价格</th>
					<th>市场价格</th>
					<th>销售单价</th>
					<th>商品销售量</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${page.data}" var="productStock">
					<tr style="cursor: pointer;" id="${productStock.id }" stockIdSign="${productStock.id }" originalPriceSign="${productStock.price }" shelvesSign="${productStock.shelves }" onclick="trSelectEvent(this);">
						<td><input type="checkbox" id="${productStock.id }" stockIdSign="${productStock.id }" originalPriceSign="${productStock.price }" shelvesSign="${productStock.shelves }" onclick="checkboxSelectEvent(this);" /></td>
						<td>${productStock.id }</td>
						<td>${productStock.storeName }</td>
						<td>${productStock.store.mobile }</td>
						<td>${productStock.productName } <c:if test="${productStock.attributeValuesListJointValue != null }"> | ${productStock.attributeValuesListJointValue }</c:if> </td>
						<td>${productStock.stock != -99 ? productStock.stock:'无限'}</td>
						<td>
							<fmt:formatNumber value="${productStock.costPrice }" type="currency" pattern="0.00"/>
						</td>
						<td>
							<fmt:formatNumber value="${productStock.marketPrice }" type="currency" pattern="0.00"/>
						</td>
						<td>
							<fmt:formatNumber value="${productStock.price }" type="currency" pattern="0.00"/>
						</td>
						<td>${productStock.sales }</td>
					</tr>
				</c:forEach>
			</tbody>
		</m:table>
	</div>
</body>
</html>