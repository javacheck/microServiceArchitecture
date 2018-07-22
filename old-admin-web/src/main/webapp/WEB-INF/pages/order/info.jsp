<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>会员管理</title>
<style type="text/css">
</style>
<script type="text/javascript">
	
</script>
</head>
<body>
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">×</span><span class="sr-only">关闭</span>
				</button>
				<h4 class="modal-title">订单详情</h4>
			</div>
			<table class="table table-hover table-striped table-bordered"
				cellpadding="0" cellspacing="0">

				<tr style="width: 100%;height: 30px">
					<td colspan="3">会员详情</td>
				</tr>
				<tr style="width: 100%;height: 30px">
					<td width="10%"></td>
					<td width="45%">会员名称：${order.user.name }</td>
					<td width="45%">会员手机：${order.user.mobile }</td>
				</tr>
				<tr style="width: 100%;height: 30px">
					<td colspan="3">账户详情</td>
				</tr>
				<tr style="width: 100%;height: 30px">
					<td width="10%"></td>
					<td width="45%">账户名称：${order.account.name }</td>
					<td width="45%">手机：${order.account.mobile }</td>
				</tr>
				<tr style="width: 100%;height: 30px">
					<td></td>
					<td>性别：${order.account.sex==0?'男':'女' }</td>
					<td></td>
				</tr>
				<tr style="width: 100%;height: 30px">
					<td colspan="3">商店详情</td>
				</tr>
				<tr style="width: 100%;height: 30px">
					<td width="10%"></td>
					<td width="45%">商店名称：${order.store.name }</td>
					<td width="45%"></td>
				</tr>
				<tr style="width: 100%;height: 30px">
					<td colspan="3">订单详情</td>
				</tr>
				<tr style="width: 100%;height: 30px">
					<td width="10%"></td>
					<td width="45%">订单编号：${order.id }</td>
					<td width="45%">订单时间：<fmt:formatDate value="${order.createdTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				</tr>
				<tr style="width: 100%;height: 30px">
					<td width="10%"></td>
					<td width="45%">订单总价：${order.price }￥</td>
					<td width="45%">订单状态：
						<c:if test="${order.status==0 }">成  功</c:if>
						<c:if test="${order.status==1 }">待发货</c:if> 
						<c:if test="${order.status==2 }">已取消</c:if>
					</td>
				</tr>
				<tr style="width: 100%">
					<td width="10%"></td>
					<td colspan="2" width="100%">
						<table style="width: 100%;height: 100%"
							class='table table-hover table-striped table-bordered'>
							<thead>
								<tr class='text-center'>
									<th>商品</th>
									<th>单价</th>
									<th>数量</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${order.orderItems}" var="orderItem">
									<tr class='text-center'>
										<td>${orderItem.productStock.product.name}</td>
										<td>${orderItem.price}</td>
										<td>${orderItem.amount}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</td>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>