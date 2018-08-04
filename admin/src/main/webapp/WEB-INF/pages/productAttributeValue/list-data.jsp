<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }" >
	<thead>
		<tr> 
			<m:hasPermission permissions="productAttributeValueDelete">
				<th><input type="checkbox" id="all"/>&nbsp;<a class="btn btn-small btn-danger" onclick="delAll();">批量删除</a> </th>
			</m:hasPermission>
			<th>商品属性值</th>
			<th>商品属性名称</th>
			<th>分类名称</th>
			<th>商家名称</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="productAttributeValue">
			<tr id="listProductAttributeValue">
				<m:hasPermission permissions="productAttributeValueDelete">
					<td><input name="listProductAttributeValueName" type="checkbox" value="${productAttributeValue.id}"/></td>
				</m:hasPermission>
				<td>${productAttributeValue.value }</td>
				<td>${productAttributeValue.productAttributeName }</td>
				<td>${productAttributeValue.categoryName }</td>
				<td>${productAttributeValue.storeName }</td>
				<td>
					<m:hasPermission permissions="productAttributeValueEdit">
						<a href="${contextPath }/productAttributeValue/update/${productAttributeValue.id}" class="btn btn-small btn-warning">修改</a> 
					</m:hasPermission>
					<m:hasPermission permissions="productAttributeValueDelete">
						<a  onclick="deleteProductAttributeValue(${productAttributeValue.id});" id="deletebutton"class="btn btn-small btn-danger">删除</a> 
					</m:hasPermission>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>