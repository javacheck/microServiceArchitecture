<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }" >
	<thead>
		<tr> 
			<th>商品名称</th>
			<th>所属商品分类</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="product">
			<tr>
				<td>${product.name }</td>
				<td>${product.categoryName }</td>
				<td> 
					<a href="${contextPath }/product/update/${product.id}" class="btn btn-small btn-warning">修改</a> 
				 	<a  onclick="deleteProduct(${product.id});" id="deletebutton"class="btn btn-small btn-danger">删除</a> 
					<a onclick="getStockList(${product.id})" class="btn btn-small btn-info">库存列表</a> 
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>