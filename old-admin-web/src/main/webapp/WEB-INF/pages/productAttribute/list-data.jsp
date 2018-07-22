<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }" >
	<thead>
		<tr> 
			<th>名称</th>
			<th>商品名称</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="productAttribute">
			<tr>
				<td>${productAttribute.name }</td>
				<td>${productAttribute.productName }</td>
				<td>
					<a href="${contextPath }/productAttribute/update/${productAttribute.id}" class="btn btn-small btn-warning">修改</a> 
					<a  onclick="deleteProductAttribute(${productAttribute.id});" id="deletebutton"class="btn btn-small btn-danger">删除</a> 
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>