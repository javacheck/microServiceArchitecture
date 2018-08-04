<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<table class='table table-hover table-striped table-bordered one'>
 	<tbody class="lines">
 		<tr style="font-weight: 1500px; " class="mag" >
		   <td class="mal">商品总数</td>
		   <td>库存总数量</td>
		   <td>库存总成本</td>
		   <td>预计总销售额</td>
		   <td>预计总毛利</td>
	   </tr>
	   <tr class="lines-tr">
	   <td class="mal">${productSum.nameSum }</td>
	   <td><fmt:formatNumber value="${reportSum.stockSum }"  type="currency" pattern="0.00"/></td>
	    <td>￥<fmt:formatNumber value="${reportSum.totalCostPriceSum }"  type="currency" pattern="0.00"/></td>
	    <td>￥<fmt:formatNumber value="${reportSum.preSalesSum }"  type="currency" pattern="0.00"/></td>
	    <td>￥<fmt:formatNumber value="${reportSum.preGrossProfitSum }"  type="currency" pattern="0.00"/></td>
	</tr>
	</tbody>
</table>
<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }" >
	<thead>
		<tr> 
			<c:if test="${isSys==true || isMainStore==true}">
			<th>商家名称</th>
			</c:if>
			<th>商品名称</th>
			<th>规格</th>	
			<th>商品条码</th>
			<th>商品分类</th>
			<th class="two">库存数量<span class="arrows" name="numAsc" value="1"></span><span class="arrows boult" name="numAsc" value="2"></span></th>
			<th class="two">进货价<span class="arrows" name="numAsc" value="3"></span><span class="arrows boult" name="numAsc" value="4"></span></th>
			<th class="two">销售单价<span class="arrows" name="numAsc" value="5"></span><span class="arrows boult" name="numAsc" value="6"></span></th>
			<th class="two">库存成本<span class="arrows" name="numAsc" value="7"></span><span class="arrows boult" name="numAsc" value="8"></span></th>
			<th class="two">预计销售额<span class="arrows" name="numAsc" value="9"></span><span class="arrows boult" name="numAsc" value="10"></span></th>
			<th class="two">预计毛利<span class="arrows" name="numAsc" value="11"></span><span class="arrows boult" name="numAsc" value="12"></span></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="reportStock">
			<tr >
				<c:if test="${isSys==true || isMainStore==true}">
				<td>${reportStock.storeName }</td>
				</c:if>
				<td>${reportStock.productName}</td>
				<td>${reportStock.attributeValues}</td>
				<td>${reportStock.barCode}</td>
				<td>${reportStock.categoryName}</td>
				<td><fmt:formatNumber value="${reportStock.stock }"  type="currency" pattern="0.00"/></td>
				<td>￥<fmt:formatNumber value="${reportStock.costPrice }"  type="currency" pattern="0.00"/></td>
				<td>￥<fmt:formatNumber value="${reportStock.price }"  type="currency" pattern="0.00"/></td>
				<td>￥<fmt:formatNumber value="${reportStock.totalCostPrice }"  type="currency" pattern="0.00"/></td>
				<td>￥<fmt:formatNumber value="${reportStock.preSales }"  type="currency" pattern="0.00"/></td>
				<td>￥<fmt:formatNumber value="${reportStock.preGrossProfit }"  type="currency" pattern="0.00"/></td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>