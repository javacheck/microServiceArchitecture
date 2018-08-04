<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr> 
			<th>编号</th>
			<th>手机号</th>
			<th>姓名</th>
			<th>订单数</th>
			<th>总金额</th>
			<th>开始时间</th>
			<th>交班时间</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="wr">
			<tr>
				<td name="id">${wr.id }</td>
				<td name="accountMobile">${wr.accountMobile}</td>
				<td name="accountName">
				${wr.accountName }
				</td>
				<td name="totalNum">
					${wr.totalNum }
				</td>
				<td name="sales">
					${wr.sales }
				</td>
				<td name="startDate"><fmt:formatDate value="${wr.startDate }" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
				<td name="endDate"><fmt:formatDate value="${wr.endDate }" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
				<td>
					<button class="btn btn-info" name="detail_btn">查看详情</button>
				</td>
					<input type="hidden" name="userCard" value="${wr.userCard }"></input>
					<input type="hidden" name="userCardNum" value="${wr.userCardNum }"></input>
				
					<input type="hidden" name="alipay" value="${wr.alipay }"></input>
					<input type="hidden" name="alipayNum" value="${wr.alipayNum }"></input>
					
					<input type="hidden" name="wxPay" value="${wr.wxPay }"></input>
					<input type="hidden" name="wxPayNum" value="${wr.wxPayNum }"></input>
					
					<input type="hidden" name="bankCardPay" value="${wr.bankCardPay }"></input>
					<input type="hidden" name="bankCardPayNum" value="${wr.bankCardPayNum }"></input>
					
					<input type="hidden" name="cashPay" value="${wr.cashPay }"></input>
					<input type="hidden" name="cashPayNum" value="${wr.cashPayNum }"></input>
					
					<input type="hidden" name="orderNum" value="${wr.orderNum }"></input>
					<input type="hidden" name="orderSales" value="${wr.orderSales }"></input>
					
					<input type="hidden" name="userCardRechargeNum" value="${wr.userCardRechargeNum }"></input>
					<input type="hidden" name="userCardRechargeSales" value="${wr.userCardRechargeSales }"></input>
			</tr>
		</c:forEach>
	</tbody>
</m:table>