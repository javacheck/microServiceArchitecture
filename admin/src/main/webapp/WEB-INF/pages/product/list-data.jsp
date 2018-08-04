<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<m:hasPermission permissions="productDelete">
				<th><input type="checkbox" id="all"/>&nbsp;<a class="btn btn-small btn-danger" onclick="delAll();">批量删除</a> </th>
			</m:hasPermission>
			<th>商品名称</th>
			<th>商家名称</th>
			<th>分类名称</th>
			<th>库存</th>
			<th>缺货提醒</th>
			<th>销售单价</th>
			<th>会员价</th>
			<th>商品条码</th>
			<th>商品状态</th>
			<th>单位名称</th>
			<th>排序</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="productStock">
			<tr id="listProudctStock">
				<m:hasPermission permissions="productDelete">
					<td><input name="listProudctStockName" type="checkbox" value="${productStock.id}"/></td>
				</m:hasPermission>
				<td>${productStock.productName } <c:forEach
						items="${productStock.pavList }" var="productAttributeValue">
							|${productAttributeValue.value }
							</c:forEach>
				</td>
				<td>${productStock.storeName }</td>
				<td>${productStock.categoryName }</td>
				<td>${productStock.stock != -99 ? productStock.stock:'无限'}</td>
				<td>${productStock.alarmValue }</td>
				<td>
				<c:if test="${empty productStock.price }">面议</c:if>
				<c:if test="${not empty productStock.price }">${productStock.price }</c:if>
				</td>
				<td>
					${productStock.memberPrice }
				</td>
				<td>${productStock.barCode }</td>
				<c:if test="${productStock.shelves==0}">
					<td>上架</td>
				</c:if>
				<c:if test="${productStock.shelves==1}">
					<td>下架</td>
				</c:if>
				<c:if test="${productStock.shelves!=0 && productStock.shelves!=1}">
					<td></td>
				</c:if>
				<td>${productStock.unitName}</td>
				<td>${productStock.sort}</td>
				<td>
				<c:if test="${isMainStore==true}">
					<c:if test="${productStock.storeId==storeIdCache}">
						<m:hasPermission permissions="productEdit">
						<a href='${contextPath }/product/update/${productStock.id}/${page.pageNo }'
							class='btn btn-small btn-warning'>修改</a>
					</m:hasPermission> <m:hasPermission permissions="productDelete">
						<a onclick='deleteProductAndProductStock(${productStock.id});'
							id="deletebutton" class="btn btn-small btn-danger">删除</a>
					</m:hasPermission>
					</c:if>
				</c:if>
				<c:if test="${isMainStore==false}">
				<m:hasPermission permissions="productEdit">
						<a href='${contextPath }/product/update/${productStock.id}/${page.pageNo }'
							class='btn btn-small btn-warning'>修改</a>
					</m:hasPermission> <m:hasPermission permissions="productDelete">
						<a onclick='deleteProductAndProductStock(${productStock.id});'
							id="deletebutton" class="btn btn-small btn-danger">删除</a>
					</m:hasPermission>
				</c:if>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>
