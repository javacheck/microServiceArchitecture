<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<m:table pageNo="${page.pageNo }" pages="${page.pages }" id="productListDataId"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th><input type="checkbox" id="all"/></th>
			<th>商品名称</th>
			<th>规格</th>
			<th>商家名称</th>
			<th>可调库存</th>
			<th>商品条码</th>
			
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="productStock">
			<tr>
				<td><input name="listProudctStockName" type="checkbox" value="${productStock.id}"/></td>
				<td>${productStock.productName }</td>
				<td>${productStock.attributeValues}</td>
				<td>${productStock.storeName }</td>
				<td>
					<c:if test="${productStock.stock >= 0}">
						<c:if test="${productStock.stock-productStock.alarmValue>=0 }">
							${productStock.stock-productStock.alarmValue}
						</c:if>
						<c:if test="${productStock.stock-productStock.alarmValue<0 }">
							0
						</c:if>
					</c:if>
					<c:if test="${productStock.stock < 0}">
						无限
					</c:if>
				</td>
				<td>${productStock.barCode }</td>
				</tr>
		</c:forEach>
	</tbody>
</m:table>
