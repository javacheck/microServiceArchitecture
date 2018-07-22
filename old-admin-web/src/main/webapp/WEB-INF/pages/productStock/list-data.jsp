<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }" >
	<thead>
		<tr> 
			<th>库存</th>
			<th>商品属性值</th>
			<th>商品属性名称</th>
			<th>商品名称</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="productStock">
			<tr>
				<td>${productStock.stock }</td>
				<td>${productStock.productAttributeValueName }</td>
				<td>${productStock.productAttributeName }</td>
				<td>${productStock.productName }</td>
				<td>
					<a  onclick="deleteProductStock(${productStock.id});" id="deletebutton"class="btn btn-small btn-danger">删除</a> 
					<a href="${contextPath }/productStock/update/${productStock.id}" class="btn btn-small btn-danger">修改</a> 
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>