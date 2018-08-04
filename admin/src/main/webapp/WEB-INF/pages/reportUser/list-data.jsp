<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<table class='table table-hover table-striped table-bordered one'>
	<tbody class="lines">
 	<tr style="font-weight: 1500px; " class="mag" >
	   <td class="mal">商家总数</td>
	   <td>新增会员总数</td>
	   <td>新增总储值</td>
	   <td>储值卡总消费</td>
	   <td>储值卡总余额</td>
	   <td>会员总剩余积分</td>
	   <td>累计会员总数</td>
	   <td>累计总储值</td>
	   <td>储值卡累计总消费</td>
	  </tr>
	  <tr class="lines-tr">
	   <td class="mal">${reportStoreSum.storeSum }</td>
	   <td>${reportSumByTime.userNum }</td>
	   <td>￥<fmt:formatNumber value="${reportSumByTime.recharge }"  type="currency" pattern="0.00"/></td>
	   <td>￥<fmt:formatNumber value="${reportSumByTime.consumption }"  type="currency" pattern="0.00"/></td>
	   <td>￥<fmt:formatNumber value="${reportSumByTime.totalBalance }"  type="currency" pattern="0.00"/></td>
	   <td><fmt:formatNumber value="${reportSumByTime.totalPoint }"  type="currency" pattern="0"/></td>
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
			<th>商家名称</th>
			<th class="two">新增会员数<span class="arrows" name="numAsc" value="1"></span><span class="arrows boult" name="numAsc" value="2"></span></th>	
			<th class="two">新增储值<span class="arrows" name="numAsc" value="3"></span><span class="arrows boult" name="numAsc" value="4"></span></th>
			<th class="two">储值卡消费<span class="arrows" name="numAsc" value="5"></span><span class="arrows boult" name="numAsc" value="6"></span></th>
			<th class="two">储值卡余额<span class="arrows" name="numAsc" value="7"></span><span class="arrows boult" name="numAsc" value="8"></span></th>
			<th class="two">会员剩余积分<span class="arrows" name="numAsc" value="9"></span><span class="arrows boult" name="numAsc" value="10"></span></th>
			<th class="two">累计会员<span class="arrows" name="numAsc" value="11"></span><span class="arrows boult" name="numAsc" value="12"></span></th>
			<th class="two">累计储值<span class="arrows" name="numAsc" value="13"></span><span class="arrows boult" name="numAsc" value="14"></span></th>
			<th class="two">储值卡累计消费<span class="arrows" name="numAsc" value="15"></span><span class="arrows boult" name="numAsc" value="16"></span></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="reportUser">
			<tr >
				
				<td>${reportUser.storeName }</td>
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