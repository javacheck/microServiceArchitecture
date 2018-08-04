<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }" >
	<thead>
		<tr> 
			<m:hasPermission permissions="productUnitDelete">
				<th><input type="checkbox" id="all"/>&nbsp;<a class="btn btn-small btn-danger" onclick="delAll();">批量删除</a> </th>
			</m:hasPermission>
			<th>序号</th>
			<th>商品单位名称</th>
			<th>商家名称</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="productUnit" varStatus="v">
			<tr id="listproductUnit">
				<m:hasPermission permissions="productUnitDelete">
					<td><input name="listproductUnitName" type="checkbox" value="${productUnit.id}"/></td>
				</m:hasPermission>
				<td>${v.index+1 }</td>
				<td>${productUnit.name }</td>
				<td>${productUnit.storeName }</td>
				<td>
					<m:hasPermission permissions="productUnitEdit">
						<a href="${contextPath }/productUnit/update/${productUnit.id}" class="btn btn-small btn-warning">修改</a> 
					</m:hasPermission>
					<m:hasPermission permissions="productUnitDelete">
						<a  onclick="deleteproductUnit(${productUnit.id});" id="deletebutton"class="btn btn-small btn-danger">删除</a> 
					</m:hasPermission>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>