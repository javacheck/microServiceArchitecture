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
			<th>商家</th>
			<th>会员</th>
			<th>交易状态</th>
			<th>交易时间</th>
			<th>商品总价</th>
			<th>运费</th>
			<th>交易总额</th>
			<th>总优惠</th>
			<th>实付金额</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="order">
			<tr>
				<td>${order.id }</td>
				<td>${order.store.name }</td>
				<td>${order.user!=null?order.user.mobile:'非会员' }</td>
				<td>${order.statusDes}</td>
				<td><fmt:formatDate value="${order.createdTime }" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
				<td>￥<fmt:formatNumber value="${order.price }"  type="currency" pattern="0.00"/></td>
				<td>￥<fmt:formatNumber value="${order.shipAmount }"  type="currency" pattern="0.00"/></td>
				<td>￥<fmt:formatNumber value="${order.price+order.shipAmount}"  type="currency" pattern="0.00"/></td>
				<td>
					<c:if test="${order.actualPrice==null}">
						
					</c:if>
					<c:if test="${order.actualPrice!=null}">
					￥<fmt:formatNumber value="${order.price-order.actualPrice-order.balance+order.shipAmount}"  type="currency" pattern="0.00"/>
					</c:if>
				</td>
				<td>
					<c:if test="${order.actualPrice==null}">
						${order.actualPrice }
					</c:if>
					<c:if test="${order.actualPrice!=null}">
					￥<fmt:formatNumber value="${order.actualPrice+order.balance }"  type="currency" pattern="0.00"/>
					</c:if>
				</td>
				
				
				<td>
					<button type="button"  data-toggle="modal" name='infoButton' data-remote="${contextPath }/order/appInfo/showMode/${order.id }" class="btn btn-small btn-primary">订单详情</button>
					<!-- 订单来源 常量ORDER_SOURCE_APP = 0;// 从APP获取 -->
					<!--  0待付款 1待确认(已支付,线下没有支付) -->
					<c:if test="${isStore==true && order.source==0 && order.status==1}">
						<button type="button"  name='typeChangeButton' onclick="typeChage(${order.id },5,'${page.pageNo }');" class="btn btn-small btn-warning">确认</button>
					</c:if>
					<c:if test="${isStore==true && order.source==0 && order.status==5}">
						<button type="button"  name='typeChangeButton3' onclick="typeChage(${order.id },4,'${page.pageNo }');" class="btn btn-small btn-warning">发货</button>
					</c:if>
					
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>