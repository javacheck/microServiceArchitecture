<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th>商家名称</th>
			<th>商品名称</th>
			<th width="200px">价格(单击修改)</th>
			<th width="300px">可促销商品数(单击修改,不填表示无限数量)</th>
			<th>创建时间</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody id="salespromotion_list_data_array">
		<c:forEach items="${page.data}" var="salesPromotion">
			<tr>
				<td>${salesPromotion.storeName }</td>
				<td>${salesPromotion.productName } <c:if test="${salesPromotion.attributeName != null }">| ${salesPromotion.attributeName}</c:if></td>
				<td id="${salesPromotion.id }" name="price_show">${salesPromotion.price }</td>
				<td name="sales_num_show" sign="${salesPromotion.id }" stockId_sign="${salesPromotion.stockId }">
					<c:if test="${salesPromotion.salesNum == -1 }">
						无限数量
					</c:if>
					<c:if test="${salesPromotion.salesNum != null && salesPromotion.salesNum != -1 }">
						${salesPromotion.salesNum}
					</c:if>
				</td>
				<td><fmt:formatDate value="${salesPromotion.createTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td>
					<m:hasPermission permissions="salesPromotionDelete">
						<a onclick='deleteId(${salesPromotion.id },${salesPromotion.stockId },${salesPromotion.shelves });' id="deletebutton"class="btn btn-small btn-danger">删除</a>
					</m:hasPermission></td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>