<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<m:table pageNo="${page.pageNo }" pages="${page.pages }" pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th style="text-align:center;">订单编号</th>
			<th style="text-align:center;">会员号</th>
			<th style="text-align:center;">会员卡号</th>
			<th style="text-align:center;">交易类型</th>
			<th style="text-align:center;">实收金额(元)</th>
			<th style="text-align:center;">充值/消费(元)</th>
			<th style="text-align:center;">积分</th>
			<th style="text-align:center;">剩余积分</th>
			<th style="text-align:center;">发卡商家</th>
			<th style="text-align:center;">日期</th>
			<th style="text-align:center;">操作人员</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="userCardRecord">
			<tr>
				<td align="center">${userCardRecord.orderId }</td>
				<td align="center">${userCardRecord.mobile }</td>
				<td align="center">${userCardRecord.userCard.cardNum }</td>
				<td align="center">
					<c:if test="${userCardRecord.type == 1 }">
						充值
					</c:if>
					<c:if test="${userCardRecord.type == 2 }">
						消费
					</c:if>
					<c:if test="${userCardRecord.type == 3 }">
						积分兑换
					</c:if>
					<c:if test="${userCardRecord.type == 4 }">
						积分抵扣
					</c:if>
					<c:if test="${userCardRecord.type == 6 }">
						服务套餐
					</c:if>
					<c:if test="${userCardRecord.type == 7 }">
						退换货
					</c:if>
				</td>
				<td align="center">
					<c:if test="${userCardRecord.type == 1 }">
							<fmt:formatNumber value="${userCardRecord.amount }" type="currency" pattern="0.00"/>
						</c:if>
						<c:if test="${userCardRecord.type == 2 || userCardRecord.type == 6 }">
							<!--<fmt:formatNumber value="${userCardRecord.amount }" type="currency" pattern="0.00"/>-->
						</c:if>
						<c:if test="${userCardRecord.type == 3 }">
						--
						</c:if>
						<c:if test="${userCardRecord.type == 4 }">
						--
						</c:if>
				</td>
					<td align="center">
						<c:if test="${userCardRecord.type == 1 }">
							<fmt:formatNumber value="${userCardRecord.actualAmount }" type="currency" pattern="0.00"/>
						</c:if>
						<c:if test="${userCardRecord.type == 2 || userCardRecord.type == 6 }">
							<c:if test="${userCardRecord.actualAmount != 0 }">
								-<fmt:formatNumber value="${userCardRecord.actualAmount }" type="currency" pattern="0.00"/>								
							</c:if>
						</c:if>
						<c:if test="${userCardRecord.type == 3 }">
						--
						</c:if>
						<c:if test="${userCardRecord.type == 4 }">
						--
						</c:if>
						<c:if test="${userCardRecord.type == 7}">
							<fmt:formatNumber value="${userCardRecord.actualAmount }" type="currency" pattern="0.00"/>
						</c:if>
					</td>
							<td align="center">
								<fmt:formatNumber value="${userCardRecord.point }" type="currency" pattern="0"/>
							</td>
								<td align="center">
								<fmt:formatNumber value="${userCardRecord.remainPoint }" type="currency" pattern="0"/>
								</td>
								<td align="center">${userCardRecord.userCard.storeName }</td>
								<td align="center"><fmt:formatDate value="${userCardRecord.createdTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td align="center">${userCardRecord.userCard.accountName }</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>