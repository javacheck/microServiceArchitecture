<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th>商家名称</th>
			<th>促销名称</th>
			<th>促销类型</th>
			<th>促销数量</th>
			<th>购买数量限制</th>
			<th>促销开始时间</th>
			<th>促销结束时间</th>
			<th>促销状态</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody id="salespromotionCategory_list_data_array">
		<c:forEach items="${page.data}" var="salesPromotionCategory">
			<tr>
				<td>${salesPromotionCategory.storeName }</td>
				<td>${salesPromotionCategory.name }</td>
				<td>
					<c:if test="${salesPromotionCategory.amount != null }">${salesPromotionCategory.amount}元</c:if>
					<c:if test="${salesPromotionCategory.discount != null }">${salesPromotionCategory.discount}折</c:if>
				</td>
				<td>
					<c:if test="${salesPromotionCategory.salesNum == -1 }">
						无限数量
					</c:if>
					<c:if test="${salesPromotionCategory.salesNum != null && salesPromotionCategory.salesNum != -1 }">
						${salesPromotionCategory.salesNum}
					</c:if>
				</td>
				<td>
					<c:if test="${salesPromotionCategory.buyNum == -1 }">
						无限数量
					</c:if>
					<c:if test="${salesPromotionCategory.buyNum != null && salesPromotionCategory.buyNum != -1 }">
						${salesPromotionCategory.buyNum}
					</c:if>
				</td>
				<td><fmt:formatDate value="${salesPromotionCategory.startDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td><fmt:formatDate value="${salesPromotionCategory.endDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td>
					<c:if test="${salesPromotionCategory.status != null && salesPromotionCategory.status == 0 }">
						未开始
					</c:if>
					<c:if test="${salesPromotionCategory.status != null && salesPromotionCategory.status == 1 }">
						进行中
					</c:if>
					<c:if test="${salesPromotionCategory.status != null && salesPromotionCategory.status == 2 }">
						已结束
					</c:if>
				</td>
				
				<td>
					<m:hasPermission permissions="salesPromotionCategoryDelete_">
						<c:if test="${salesPromotionCategory.status == 0 || salesPromotionCategory.status == 2 }">
							<a onclick='deleteById(${salesPromotionCategory.storeId },${salesPromotionCategory.id });' id="deletebutton"class="btn btn-small btn-danger">删除</a>
						</c:if>
					</m:hasPermission>
					
					<m:hasPermission permissions="salesPromotionCategoryUpdate">
						<c:if test="${salesPromotionCategory.status != 2 }">
							<a href='${contextPath }/salesPromotionCategory/update/${salesPromotionCategory.id}' class='btn btn-small btn-warning'>修改</a>					
						</c:if>
					</m:hasPermission>
					
					 	<a href='${contextPath }/salesPromotionCategory/view/${salesPromotionCategory.id}' class='btn btn-small btn-warning'>查看</a>
					
					<m:hasPermission permissions="salesPromotionAdd">
						<c:if test="${salesPromotionCategory.status != 2 }">
							<a onclick='showById(${salesPromotionCategory.storeId },${salesPromotionCategory.id });' id="showbutton" class="btn btn-small btn-danger">添加促销商品</a>
						</c:if>
					</m:hasPermission>
					
					<m:hasPermission permissions="salesPromotionCategoryAgain">
						<c:if test="${salesPromotionCategory.status != null && salesPromotionCategory.status == 2 }">
							<a onclick='againById(${salesPromotionCategory.storeId },${salesPromotionCategory.id });' id="againbutton"class="btn btn-small btn-danger">重新开始</a>
						</c:if>
					</m:hasPermission>
					<a onclick='viewById(${salesPromotionCategory.storeId },${salesPromotionCategory.id });' id="viewbutton" class="btn btn-small btn-danger">查看促销商品</a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>