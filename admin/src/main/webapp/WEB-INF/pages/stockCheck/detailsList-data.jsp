<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }" >
	<thead>
		<tr> 
			<th>商家名称</th>
			<th>商品名称</th>
			<th>商品条码</th>
			<th>商品分类</th>
			<th class="two">库存数量<span class="arrows" name="numAsc" value="1"></span><span class="arrows boult" name="numAsc" value="2"></span></th>	
			<th class="two">盘点库存<span class="arrows" name="numAsc" value="3"></span><span class="arrows boult" name="numAsc" value="4"></span></th>
			<th class="two">盘盈<span class="arrows" name="numAsc" value="5"></span><span class="arrows boult" name="numAsc" value="6"></span></th>
			<th class="two">盘亏<span class="arrows" name="numAsc" value="7"></span><span class="arrows boult" name="numAsc" value="8"></span></th>
			<th>盘点人</th>
			<th class="two">盘点时间<span class="arrows" name="numAsc" value="9"></span><span class="arrows boult" name="numAsc" value="10"></span></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="stockCheckDetail">
			<tr>
				<td>${stockCheckDetail.storeName }</td>
				<td>${stockCheckDetail.productName }</td>
				<td>${stockCheckDetail.barCode }</td>
				<td>${stockCheckDetail.categoryName }</td>
				<td>${stockCheckDetail.stock }</td>
				<td>${stockCheckDetail.checkedStock }</td>
				<td>${stockCheckDetail.inventoryProfit }</td>
				<td>${stockCheckDetail.inventoryLoss }</td>
				<td>${stockCheckDetail.checkedName }</td>
				<td>${stockCheckDetail.checkedTime }</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>