<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<table class='table table-hover table-striped table-bordered one'>
	<tbody class="lines">
	 	<tr style="font-weight: 1500px; " class="mag" >
		   <td class="mal">商家总数</td>
		   <td>订单总数</td>
		   <td>销售总额</td>
		   <td>销售总成本</td>
		   <td>参考总毛利</td>
		   <td>促销总让利</td>
		   <td>实销总毛利</td>
		</tr>
		<tr class="lines-tr">   
		   <td class="mal">${reportStoreSum.storeSum }</td>
		   <td>${reportSum.orderNum }</td>
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
			<th>商家名称</th>
			<th>订单数量</th>
			<th class="two">销售额<span class="arrows" name="numAsc" value="1"></span><span class="arrows boult" name="numAsc" value="2"></span></th>	
			<th class="two">销售成本<span class="arrows" name="numAsc" value="3"></span><span class="arrows boult" name="numAsc" value="4"></span></th>
			<th class="two">参考毛利<span class="arrows" name="numAsc" value="5"></span><span class="arrows boult" name="numAsc" value="6"></span></th>
			<th class="two">促销让利<span class="arrows" name="numAsc" value="7"></span><span class="arrows boult" name="numAsc" value="8"></span></th>
			<th class="two">实销毛利<span class="arrows" name="numAsc" value="9"></span><span class="arrows boult" name="numAsc" value="10"></span></th>
<!-- 			<th>操作</th> -->
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="reportSales">
			<tr >
				<td>${reportSales.storeName }</td>
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