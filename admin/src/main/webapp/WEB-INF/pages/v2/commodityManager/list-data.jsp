<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="panel" id="show_productManager_Model">
<m:table pageNo="${page.pageNo }" pages="${page.pages }" pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<c:if test="${isCurrentStore == true }">
				<th width="5%">
					<input type="checkbox" id="pageALLCheckBox" onclick="checkPageALL(this);"/>全选
				</th>
			</c:if>
			<th>商家名称</th>
			<th>商品名称</th>
			<th>商品分类</th>
			<th>商品状态</th>
			<th>排序</th>
			<th>品牌</th>
			<th width="15%">操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="product">
			<tr>
				<c:if test="${isCurrentStore == true }">
					<td>
						<input type="checkbox"  id="${product.id }" sign="${product.id }" onclick="checkboxSelectEvent(this);"/>
					</td>
				</c:if>
				<td>${product.storeName }</td>
				<td>${product.name }</td>
				<td>${product.categoryName }</td>
				<td>
					<c:if test="${product.shelves == 0 }">
						收银端上架
					</c:if>
					<c:if test="${product.shelves == 1 }">
						收银端下架
					</c:if>
					<c:if test="${product.shelves == 2 }">
						APP端上架
					</c:if>
					<c:if test="${product.shelves == 3 }">
						APP端下架
					</c:if>
					<c:if test="${product.shelves == 4 }">
						收银端上架、APP端上架
					</c:if>
					<c:if test="${product.shelves == 5 }">
						全部下架
					</c:if>
				</td>
				<td>${product.sort }</td>
				<td>${product.brandName }</td>
				<td>
					<m:hasPermission permissions="commodityManagerDetails"> 
						<a href='${contextPath }/commodityManager/details/${product.id}' class='btn btn-primary'>详情</a>						
					</m:hasPermission>
					
					<c:if test="${isCurrentStore == true }">
						<m:hasPermission permissions="commodityManagerEdit"> 
							<a href='${contextPath }/commodityManager/update/${product.id}' class='btn btn-small btn-warning'>修改</a>						
						</m:hasPermission>
						<m:hasPermission permissions="commodityManagerDelete"> 
							<a onclick='deleteById(${product.id});' id="deletebutton"class="btn btn-small btn-danger">删除</a>						
						</m:hasPermission>
					</c:if>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>
</div>