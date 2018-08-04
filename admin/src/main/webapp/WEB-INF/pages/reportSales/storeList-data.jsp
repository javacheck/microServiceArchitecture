<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<table class='table table-hover table-striped table-bordered one'>
	<tbody class="lines">
 	<tr style="font-weight: 1500px; " class="mag" >
 		<c:if test="${isSys==true}">
 			<td class="mal">商家总数</td>
 			<td>订单总数</td>
 		</c:if>
 		<c:if test="${isSys==false}">
 			<td class="mal">订单总数</td>
 		</c:if>
	   <td>销售总额</td>
	   <td>销售总成本</td>
	   <td>参考总毛利</td>
	   <td>促销总让利</td>
	   <td>实销总毛利</td>
	</tr>
	<tr class="lines-tr">
	   <c:if test="${isSys==true}">
	   		<td class="mal">${reportStoreSum.storeSum }</td>
	   		<td>${reportSum.orderNum }</td>
	   </c:if>
	   <c:if test="${isSys==false}">
	   		<td class="mal">${reportSum.orderNum }</td>
	   </c:if>
	   <td>￥<fmt:formatNumber value="${reportSum.salesNum }"  type="currency" pattern="0.00"/></td>
	   <td>￥<fmt:formatNumber value="${reportSum.costPrice }"  type="currency" pattern="0.00"/></td>
	   <td>￥<fmt:formatNumber value="${reportSum.grossProfit }"  type="currency" pattern="0.00"/></td>
	   <td>￥<fmt:formatNumber value="${reportSum.promotionsGrossProfit }"  type="currency" pattern="0.00"/></td>
	   <td>￥<fmt:formatNumber value="${reportSum.actualGrossProfit}"  type="currency" pattern="0.00"/></td>
	</tr>
	</tbody>
</table>
<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }" >
	<thead>
		<tr> 
			<th>日期</th>
			<th>订单数量</th>
			<th>销售额</th>	
			<th>销售成本</th>
			<th>参考毛利</th>
			<th>促销让利</th>
			<th>实销毛利</th>
<!-- 			<th>操作</th> -->
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="reportSales">
			<tr >
				<td>${reportSales.dateString }</td>
				<td>${reportSales.orderNum }</td>
				<td>￥<fmt:formatNumber value="${reportSales.salesNum }"  type="currency" pattern="0.00"/></td>
				<td>￥<fmt:formatNumber value="${reportSales.costPrice }"  type="currency" pattern="0.00"/></td>
				<td>￥<fmt:formatNumber value="${reportSales.grossProfit }"  type="currency" pattern="0.00"/></td>
				<td>￥<fmt:formatNumber value="${reportSales.promotionsGrossProfit }"  type="currency" pattern="0.00"/></td>
				<td>￥<fmt:formatNumber value="${reportSales.actualGrossProfit }"  type="currency" pattern="0.00"/></td>
<!-- 				<td><a onclick="goReportProduct();">查看商品销售</a></td> -->
			</tr>
		</c:forEach>
	</tbody>
</m:table>