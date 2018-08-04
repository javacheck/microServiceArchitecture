<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<m:table pageNo="${page.pageNo }" pages="${page.pages }" id="orderListDataId"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th>订单编号</th>
			<th>商品总价</th>
			<th>总优惠</th>
			<th>实付金额</th>
			<th>状态</th>
			<th>交易时间</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="order">
			<tr style="cursor: pointer;" onclick="selectThisOrder(${order.id})" data-dismiss="modal">
				<td>${order.id } </td>
				<td>￥<fmt:formatNumber value="${order.price }"  type="currency" pattern="0.00"/></td>
				<td>
					<c:if test="${order.source==1 }">
						<c:if test="${order.actualPrice!=null }">
							￥<fmt:formatNumber value="${order.price-order.actualPrice }"  type="currency" pattern="0.00"/>
						</c:if>
					</c:if>
					<c:if test="${order.source==0 }">
						<c:if test="${order.actualPrice!=null }">
							￥<fmt:formatNumber value="${order.price-order.actualPrice-order.balance+order.shipAmount}"  type="currency" pattern="0.00"/>
						</c:if>
					</c:if>
				</td>
				<td>
					<c:if test="${order.actualPrice!=null }">
						￥<fmt:formatNumber value="${order.actualPrice }"  type="currency" pattern="0.00"/>
					</c:if>
				</td>
				<td>${order.statusDes }</td>
				<td><fmt:formatDate value="${order.createdTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>
