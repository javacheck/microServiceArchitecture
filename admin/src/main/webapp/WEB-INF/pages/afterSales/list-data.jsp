<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
		<c:if test="${isMainStore==true }">
			<th>商家名称</th>
		</c:if>
			<th>订单编号</th>
			<th>商品名称</th>
			<th>商品条码</th>
			<th>规格</th>
			<th>商品分类</th>
			<th>销售价</th>
			<th>售后类型</th>
			<th>数量</th>
			<th>备注</th>
			<th>时间</th>
			<th>操作人</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="afterSales">
			<tr>
			<c:if test="${isMainStore==true }">
				<td>${afterSales.storeName }</td>
			</c:if>
				<td>${afterSales.orderId }</td>
				<td>${afterSales.productName }</td>
				<td>${afterSales.barCode }</td>
				<td>${afterSales.unitName }</td>
				<td>${afterSales.productCategoryName }</td>
				<td>￥<fmt:formatNumber value="${afterSales.price }"  type="currency" pattern="0.00"/></td>
				<td>${afterSales.afterSalesTypeName }</td>
				<td><fmt:formatNumber value="${afterSales.amount }"  type="currency" pattern="0.00"/></td>
				<td>${afterSales.remark }</td>
				<td><fmt:formatDate value="${afterSales.createdTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${afterSales.username }</td>
				<td>
				<c:if test="${afterSales.source==0 }">
					<button type="button"  data-toggle="modal" name='infoButton' data-remote="${contextPath }/order/appInfo/showMode/${afterSales.orderId }" class="btn btn-small btn-primary">线上订单详情</button>
				</c:if>
				<c:if test="${afterSales.source==1 }">
					<button type="button"  data-toggle="modal" name='infoButton' data-remote="${contextPath }/order/info/showMode/${afterSales.orderId }" class="btn btn-small btn-primary">POS收银详情</button>
				</c:if>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>
