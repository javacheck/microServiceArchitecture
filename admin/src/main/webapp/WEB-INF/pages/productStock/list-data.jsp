<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }" >
	<thead>
		<tr> 
			<m:hasPermission permissions="productStockDelete">
				<th><input type="checkbox" id="all"/>&nbsp;<a class="btn btn-small btn-danger" onclick="delAll();">批量删除</a> </th>
			</m:hasPermission>
			<th>商品名称</th>
			<th>商家名称</th>
			<th>分类名称</th>
			<th>所属品牌</th>
			<th>是否有属性</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="product">
			<tr id="listProudctStock">
				<m:hasPermission permissions="productStockDelete">
					<td><input name="listProudctName" type="checkbox" value="${product.id}"/></td>
				</m:hasPermission>
				<td>${product.name }</td>
				<td>${product.storeName }</td>
				<td>${product.categoryName }</td>
				<td>${product.brandName }</td>
				<td><c:if test="${product.type==0}">有</c:if><c:if test="${product.type==1}">无</c:if></td>
				<td>
					<c:if test="${isMainStore==true}">
						<c:if test="${product.storeId==storeId}">
							<m:hasPermission permissions="productStockEdit">
								<a href="${contextPath }/productStock/update/${product.id}" class="btn btn-small btn-warning">修改</a> 
							</m:hasPermission>
							<m:hasPermission permissions="productStockDelete">
								<a  onclick="deleteProductStock(${product.id});" id="deletebutton"class="btn btn-small btn-danger">删除</a> 
							</m:hasPermission>
						</c:if>
					</c:if>
					<c:if test="${isMainStore==false}">
							<m:hasPermission permissions="productStockEdit">
								<a href="${contextPath }/productStock/update/${product.id}" class="btn btn-small btn-warning">修改</a> 
							</m:hasPermission>
							<m:hasPermission permissions="productStockDelete">
								<a  onclick="deleteProductStock(${product.id});" id="deletebutton"class="btn btn-small btn-danger">删除</a> 
							</m:hasPermission>
					</c:if>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>