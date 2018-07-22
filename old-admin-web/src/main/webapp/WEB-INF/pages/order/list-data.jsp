<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr> 
			<th>订单编号</th>
			<th>收银</th>
			<th>商店</th>
			<th>会员</th>
			<th>订单总价</th>
			<th>状态</th>
			<th>时间</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="order">
			<tr>
				<td>${order.id }</td>
				<td>${order.account.name}</td>
				<td>${order.store.name }</td>
				<td>${order.user.name }</td>
				<td>${order.price }</td>
				<td>
				<c:if test="${order.status==0 }">成  功</c:if>
				<c:if test="${order.status==1 }">待发货</c:if>
				<c:if test="${order.status==2 }">已取消</c:if>
				</td>
				<td><fmt:formatDate value="${order.createdTime }" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
				<td><button id ="" data-remote="${contextPath }/order/info/showMode/${order.id }" data-toggle="modal"  class="btn btn-small btn-primary">查看</button></td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>