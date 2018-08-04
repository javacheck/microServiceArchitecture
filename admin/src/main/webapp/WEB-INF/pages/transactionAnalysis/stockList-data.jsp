<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<m:table pageNo="${page.pageNo }" pages="${page.pages }" id="productListDataId"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			
			<th>商品名称</th>
			<th>规格</th>
			<th>商家名称</th>
			<th>商家分类</th>
			<th>库存</th>
			<th>缺货提醒</th>
			<th>销售单价</th>
			<th>商品条码</th>
			<th>商品状态</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="productStock">
			<tr style="cursor: pointer;" val="${productStock.id }">
				<td>${productStock.productName } </td>
				<td>${productStock.attributeValues } </td>
				<td>${productStock.storeName }</td>
				<td><c:if test="${productStock.type==1}">${productStock.categoryName }</c:if></td>
				<td>${productStock.stock != -99 ? productStock.stock:'无限'}</td>
				<td>${productStock.alarmValue }</td>
				<td>
				<c:if test="${empty productStock.price }">面议</c:if>
				<c:if test="${not empty productStock.price }">${productStock.price }</c:if>
				</td>
				<td>${productStock.barCode }</td>
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
