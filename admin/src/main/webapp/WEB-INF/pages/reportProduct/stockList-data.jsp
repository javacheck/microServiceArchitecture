<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<table class='table table-hover table-striped table-bordered one'>
 	<tbody class="lines">
 	<tr style="font-weight: 1500px; " class="mag" >
		<c:if test="${isSys==true || isMainStore==true}">
		   <td class="mal">商家总数</td>
	   		<td>商品总数</td>
	 	</c:if>
	 	<c:if test="${isSys==false && isMainStore==false}">
	 		<td class="mal">商品总数</td>
	 	</c:if>
	   <td>总销量</td>
	   <td>销售总成本</td>
	   <td>参考销售总额</td>
	   <td>参考总毛利</td>
	</tr>
   	<tr class="lines-tr">
	   <c:if test="${isSys==true || isMainStore==true}">
	   		<td class="mal">${reportStoreSum.storeSum }</td>
	   		<td>${reportStockSum.stockSum }</td>
	   </c:if>
	   <c:if test="${isSys==false && isMainStore==false}">
	   		<td class="mal">${reportStockSum.stockSum }</td>
	   </c:if>
	   <td>${reportSum.salesNum }</td>
	   <td>￥<fmt:formatNumber value="${reportSum.totalCostPrice }"  type="currency" pattern="0.00"/></td>
	   <td>￥<fmt:formatNumber value="${reportSum.totalPrice }"  type="currency" pattern="0.00"/></td>
	   <td>￥<fmt:formatNumber value="${reportSum.totalGrossProfit }"  type="currency" pattern="0.00"/></td>
	   
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
			<th>品牌名称</th>
			<th class="two">销量<span class="arrows" name="numAsc" value="1"></span><span class="arrows boult" name="numAsc" value="2"></span></th>
			<th class="two">进货价<span class="arrows" name="numAsc" value="3"></span><span class="arrows boult" name="numAsc" value="4"></span></th>
			<th class="two">销售单价<span class="arrows" name="numAsc" value="5"></span><span class="arrows boult" name="numAsc" value="6"></span></th>
			<th class="two">销售成本<span class="arrows" name="numAsc" value="7"></span><span class="arrows boult" name="numAsc" value="8"></span></th>
			<th class="two">参考销售额<span class="arrows" name="numAsc" value="9"></span><span class="arrows boult" name="numAsc" value="10"></span></th>
			<th class="two">参考毛利<span class="arrows" name="numAsc" value="11"></span><span class="arrows boult" name="numAsc" value="12"></span></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="reportProduct">
			<tr >
				<c:if test="${isSys==true || isMainStore==true}">
					<td>${reportProduct.storeName }</td>
				</c:if>
				<td>${reportProduct.productName }</td>
				<td>${reportProduct.attributeValues }</td>
				<td>${reportProduct.barCode }</td>
				<td>${reportProduct.categoryName }</td>
				<td>${reportProduct.brandName }</td>
				<td>${reportProduct.salesNum }</td>
				<td>￥<fmt:formatNumber value="${reportProduct.costPrice }"  type="currency" pattern="0.00"/></td>
				<td>￥<fmt:formatNumber value="${reportProduct.price }"  type="currency" pattern="0.00"/></td>
				<td>￥<fmt:formatNumber value="${reportProduct.totalCostPrice }"  type="currency" pattern="0.00"/></td>
				<td>￥<fmt:formatNumber value="${reportProduct.totalPrice }"  type="currency" pattern="0.00"/></td>
				<td>￥<fmt:formatNumber value="${reportProduct.totalGrossProfit }"  type="currency" pattern="0.00"/></td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>