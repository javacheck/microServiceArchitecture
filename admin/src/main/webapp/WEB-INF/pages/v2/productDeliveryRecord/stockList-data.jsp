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
			<th>单位</th>
			<th>进货价</th>
			<th>缺货提醒</th>
			<th>库存数量</th>
			<th>商品状态</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="productStock">
			<tr>
				<td><input name="listProudctStockName" type="checkbox" value="${productStock.id}"/></td>
				<td>${productStock.productName } </td>
				<td>${productStock.barCode }</td>
				<td>${productStock.attributeValues}</td>
				<td>${productStock.unitName}</td>
				<td>￥<fmt:formatNumber value="${productStock.costPrice }"  type="currency" pattern="0.00"/></td>
				<td>${productStock.alarmValue}</td>
				<td>
					<c:if test="${productStock.stock >= 0}">
						${productStock.stock}
					</c:if>
					<c:if test="${productStock.stock < 0}">
						无限
					</c:if>
				</td>
				<td>
					<c:if test="${productStock.shelves==0}">收银端上架</c:if>
					<c:if test="${productStock.shelves==1}">收银端下架</c:if>
					<c:if test="${productStock.shelves==2}">APP上架</c:if>
					<c:if test="${productStock.shelves==3}">APP下架</c:if>
					<c:if test="${productStock.shelves==4}">全部上架</c:if>
					<c:if test="${productStock.shelves==5}">全部下架</c:if>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>
