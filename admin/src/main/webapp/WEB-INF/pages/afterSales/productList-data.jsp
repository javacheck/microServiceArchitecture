<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<m:table pageNo="${page.pageNo }" pages="${page.pages }" id="productListDataId"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th><input type="checkbox" id="all"/></th>
			<th>商品名称</th>
			<th>商品条码</th>
			<th>规格</th>
			<th>销售价</th>
			<th>购买数量</th>
			<th>售后状态</th>
			<th>售后数量</th>
			<th>退货数量</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="product">
			<tr>
				<td>
					<input name="listProudctName" type="checkbox" value="${product.id}"/>
					<input type="hidden" id="productId" value="${product.productStock.productId }">
					<input type="hidden" id="productName" value="${product.realName }">
					<input type="hidden" id="barCode" value="${product.productStock.barCode }">
					<input type="hidden" id="unitName" value="${product.productStock.attributeValues }">
					<input type="hidden" id="productCategoryId" value="${product.productStock.categoryId }">
					<input type="hidden" id="price" value="${product.price }">
					<input type="hidden" id="orderItemId" value="${product.id }">
				</td>
				<td>${product.realName }</td>
				<td>${product.productStock.barCode }</td>
				<td>${product.productStock.attributeValues }</td>
				<td>￥<fmt:formatNumber value="${product.price }"  type="currency" pattern="0.00"/></td>
				<td><fmt:formatNumber value="${product.amount }"  type="currency" pattern="0.00"/></td>
				<td>
					<c:if test="${product.afterSalesType==null||product.afterSalesType=='' }" var="typeFlag">无</c:if>
					<c:if test="${typeFlag==false }">${product.afterSalesType }</c:if>
				</td>
				<td>
					<c:if test="${product.afterSalesAmount!=null }">
						<fmt:formatNumber value="${product.afterSalesAmount }"  type="currency" pattern="0.00"/>
					</c:if>
				</td>
				<td>
					<c:if test="${product.returnNumber!=null }">
						<fmt:formatNumber value="${product.returnNumber }"  type="currency" pattern="0.00"/>
					</c:if>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>
