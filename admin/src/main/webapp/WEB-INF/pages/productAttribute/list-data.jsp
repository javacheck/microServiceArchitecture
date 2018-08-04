<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }" >
	<thead>
		<tr> 
			<m:hasPermission permissions="productAttributeDelete">
				<th><input type="checkbox" id="all"/>&nbsp;<a class="btn btn-small btn-danger" onclick="delAll();">批量删除</a> </th>
			</m:hasPermission>
			<th>属性名称</th>
			<th>分类名称</th>
			<th>商家名称</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="productAttribute">
			<tr id="listProductAttribute">
				<m:hasPermission permissions="productAttributeDelete">
					<td><input name="listProductAttributeName" type="checkbox" value="${productAttribute.id}"/></td>
				</m:hasPermission>
				<td>${productAttribute.name }</td>
				<td>${productAttribute.categoryName }</td>
				<td>${productAttribute.storeName }</td>
				<td>
					<m:hasPermission permissions="productAttributeEdit">
						<a href="${contextPath }/productAttribute/update/${productAttribute.id}" class="btn btn-small btn-warning">修改</a> 
					</m:hasPermission>
					<m:hasPermission permissions="productAttributeDelete">
						<a  onclick="deleteProductAttribute(${productAttribute.id});" id="deletebutton"class="btn btn-small btn-danger">删除</a> 
					</m:hasPermission>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>