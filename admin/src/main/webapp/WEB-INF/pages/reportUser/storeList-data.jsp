<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<table class='table table-hover table-striped table-bordered one'>
	<tbody class="lines">
 	<tr style="font-weight: 1500px; " class="mag" >
	   
	   <td class="mal">新增会员总数</td>
	   <td>新增总储值</td>
	   <td>储值卡总消费</td>
	   <td>储值卡余额</td>
	   <td>会员剩余积分</td>
	   <td>累计会员</td>
	   <td>累计储值</td>
	   <td>储值卡累计消费</td>
	 </tr> 
	 <tr class="lines-tr">
	   <td class="mal">${reportSumByTime.userNum }</td>
	   <td>￥<fmt:formatNumber value="${reportSumByTime.recharge }"  type="currency" pattern="0.00"/></td>
	   <td>￥<fmt:formatNumber value="${reportSumByTime.consumption }"  type="currency" pattern="0.00"/></td>
	   <td>￥<fmt:formatNumber value="${reportSum.totalBalance }"  type="currency" pattern="0.00"/></td>
	   <td><fmt:formatNumber value="${reportSum.totalPoint }"  type="currency" pattern="0"/></td>
	   <td>${reportSum.totalUserNum }</td>
	   <td>￥<fmt:formatNumber value="${reportSum.totalRecharge }"  type="currency" pattern="0.00"/></td>
	   <td>￥<fmt:formatNumber value="${reportSum.totalConsumption }"  type="currency" pattern="0.00"/></td>
	   
	</tr>
	</tbody>
</table>
<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }" >
	<thead>
		<tr> 
			<th>日期</th>
			<th>新增会员数</th>	
			<th>新增储值</th>
			<th>储值卡消费</th>
			<th>储值卡余额</th>
			<th>会员剩余积分</th>
			<th>累计会员</th>
			<th>累计储值</th>
			<th>储值卡累计消费</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="reportUser">
			<tr >
				
				<td>${reportUser.dateString }</td>
				<td>${reportUser.userNum }</td>
				<td>￥<fmt:formatNumber value="${reportUser.recharge }"  type="currency" pattern="0.00"/></td>
				<td>￥<fmt:formatNumber value="${reportUser.consumption }"  type="currency" pattern="0.00"/></td>
				<td>￥<fmt:formatNumber value="${reportUser.totalBalance }"  type="currency" pattern="0.00"/></td>
				<td><fmt:formatNumber value="${reportUser.totalPoint }"  type="currency" pattern="0"/></td>
				<td>${reportUser.totalUserNum }</td>
				<td>￥<fmt:formatNumber value="${reportUser.totalRecharge }"  type="currency" pattern="0.00"/></td>
				<td>￥<fmt:formatNumber value="${reportUser.totalConsumption }"  type="currency" pattern="0.00"/></td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>