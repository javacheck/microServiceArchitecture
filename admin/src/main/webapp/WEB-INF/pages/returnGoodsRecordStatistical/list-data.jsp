<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr>
			<c:if test="${isMainStore==true }">
				<th>商家名称</th>
			</c:if>
			<th>商品名称</th>
			<th>商品条码</th>
			<th>规格</th>
			<th>商品分类</th>
			<th class="two">退货数量<span class="arrows" name="numAsc" value="1"></span><span class="arrows boult" name="numAsc" value="2"></span></th>
			<th class="two">退款金额<span class="arrows" name="numAsc" value="3"></span><span class="arrows boult" name="numAsc" value="4"></span></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="returnGoods">
			<tr>
				<c:if test="${isMainStore==true }">
					<td>${returnGoods.storeName }</td>
				</c:if>
				<td>${returnGoods.productName }</td>
				<td>${returnGoods.barcode }</td>
				<td>${returnGoods.standard }</td>
				<td>${returnGoods.categoryName }</td>
				<td><fmt:formatNumber value="${returnGoods.returnNumber }"  type="currency" pattern="0"/></td>
				<td>￥<fmt:formatNumber value="${returnGoods.returnPrice }"  type="currency" pattern="0.00"/></td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>
