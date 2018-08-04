<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<m:table pageNo="${page.pageNo }" pages="${page.pages }" id="productListDataId"
	pageSize="${page.pageSize }" total="${page.total }" >
	<thead>
		<tr class='text-center'> 
			<th>商品名称</th>
			<th>商家名称</th>
			<th>分类名称</th>
			<th>是否有属性</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="product">
			<tr style="cursor: pointer;" val="${product.id }">
				<td>${product.name }</td>
				<td>${product.storeName }</td>
				<td>${product.categoryName }</td>
				<td><c:if test="${product.type==0}">有</c:if><c:if test="${product.type==1}">无</c:if></td>
				<td style="display: none;">${product.categoryStoreId }</td>
				<td style="display: none;">${product.categoryId }</td>
				<td style="display: none;">${product.categoryName }</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>