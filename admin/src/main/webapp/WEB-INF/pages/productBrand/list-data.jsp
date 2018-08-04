<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }" >
	<thead>
		<tr> 
			<m:hasPermission permissions="productBrandDelete">
				<th><input type="checkbox" id="all"/>&nbsp;<a class="btn btn-small btn-danger" onclick="delAll();">批量删除</a> </th>
			</m:hasPermission>
			<th>品牌名称</th>
			<th>商家名称</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="productBrand">
			<tr id="listproductBrand">
				<m:hasPermission permissions="productBrandDelete">
					<td><input name="listproductBrandName" type="checkbox" value="${productBrand.id}"/></td>
				</m:hasPermission>
				<td>${productBrand.name }</td>
				<td>${productBrand.storeName }</td>
				<td>
				<c:if test="${isMainStore==true}">
					<c:if test="${productBrand.storeId==storeIdCache}">
					<m:hasPermission permissions="productBrandEdit">
						<a href="${contextPath }/productBrand/update/${productBrand.id}" class="btn btn-small btn-warning">修改</a> 
					</m:hasPermission>
					<m:hasPermission permissions="productBrandDelete">
						<a  onclick="deleteproductBrand(${productBrand.id});" id="deletebutton"class="btn btn-small btn-danger">删除</a> 
					</m:hasPermission>
					</c:if>
				</c:if>
				<c:if test="${isMainStore==false}">
					<m:hasPermission permissions="productBrandEdit">
						<a href="${contextPath }/productBrand/update/${productBrand.id}" class="btn btn-small btn-warning">修改</a> 
					</m:hasPermission>
					<m:hasPermission permissions="productBrandDelete">
						<a  onclick="deleteproductBrand(${productBrand.id});" id="deletebutton"class="btn btn-small btn-danger">删除</a> 
					</m:hasPermission>
				</c:if>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>