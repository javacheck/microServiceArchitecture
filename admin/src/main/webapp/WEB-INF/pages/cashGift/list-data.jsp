<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th>用户手机号</th>
			<th>商家名称</th>
			<th>优惠活动名称</th>
			<th>优惠类型</th>
			<th>折扣/金额</th>
			<th>创建时间</th>
			<th>失效日期</th>
			<th>使用情况</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="cashGift">
			<tr>
				<td>${cashGift.mobile }</td>
				<td>${cashGift.storeName == null ?'所有商家':cashGift.storeName }</td>
				<td>${cashGift.pcName }</td>
				<td>
					<c:if test="${cashGift.type == 0 }">折扣劵</c:if> 
					<c:if test="${cashGift.type == 1 }">现金劵</c:if>
				</td>
				<td><fmt:formatNumber value="${cashGift.amount }" type="currency" pattern="0.00"/></td>
				<td><fmt:formatDate value="${cashGift.createdTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>
					<fmt:formatDate value="${cashGift.validTime }" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<c:if test="${cashGift.status == 0 }">未使用</c:if> 
					<c:if test="${cashGift.status == 1 }">已使用</c:if>
					<c:if test="${cashGift.status == 2 }">已过期</c:if>
				</td>
				<td>	
					 <m:hasPermission permissions="cashGiftDelete">
						<a onclick='deleteId(${cashGift.id},${cashGift.userId });' id="deletebutton"class="btn btn-small btn-danger">删除</a>
					 </m:hasPermission>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>
