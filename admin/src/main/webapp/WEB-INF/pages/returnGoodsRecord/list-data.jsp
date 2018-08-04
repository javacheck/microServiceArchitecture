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
			<th>退货数量</th>
			<th>退款金额</th>
			<th>退款方式</th>
			<th>退款时间</th>
			<th>操作人</th>
			<th>操作</th>
			
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="returnGoods">
			<tr>
			<c:if test="${isMainStore==true }">
				<td>${returnGoods.storeName }</td>
			</c:if>
				<td>${returnGoods.orderId }</td>
				<td>${returnGoods.productName }</td>
				<td>${returnGoods.barcode }</td>
				<td>${returnGoods.standard }</td>
				<td>${returnGoods.categoryName }</td>
				<td>￥<fmt:formatNumber value="${returnGoods.price }"  type="currency" pattern="0.00"/></td>
				<td><fmt:formatNumber value="${returnGoods.returnNumber }"  type="currency" pattern="0"/></td>
				<td>￥<fmt:formatNumber value="${returnGoods.returnPrice }"  type="currency" pattern="0.00"/></td>
				
				<td><c:if test="${returnGoods.type==0 }">退储值</c:if> <c:if test="${returnGoods.type==1 }">退现金</c:if></td>
				<td><fmt:formatDate value="${returnGoods.createdTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${returnGoods.operationName }</td>
				<td>
				<button type="button"  data-toggle="modal" name='infoButton' data-remote="${contextPath }/order/info/showMode/${returnGoods.orderId }" class="btn btn-small btn-primary">订单详情</button>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>
