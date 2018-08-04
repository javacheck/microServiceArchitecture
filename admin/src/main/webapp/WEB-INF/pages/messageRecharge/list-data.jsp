<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<m:table pageNo="${page.pageNo }" pages="${page.pages }" pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr>
			<th width="30%">商家名称</th>
			<th>剩余短信数量</th>
			<th>最近充值数量</th>
			<th>最近充值时间</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="messageRecharge">
			<tr>
				<td>${messageRecharge.storeName }</td>
				<td>${messageRecharge.remainNumber }</td>
				<td>${messageRecharge.number }</td>
				<td><fmt:formatDate value="${messageRecharge.updateTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td>
					<m:hasPermission permissions="messageRechargeSaleList"> 
					<a  href="${contextPath }/messageRecharge/sale/${messageRecharge.storeId}"	class="btn btn-small btn-info">消费记录</a>
					</m:hasPermission>
					<m:hasPermission permissions="messageRechargeRechargeList"> 
					<a  href="${contextPath }/messageRecharge/recharge/${messageRecharge.storeId}"	class="btn btn-small btn-warning">充值记录</a>
					</m:hasPermission>
				</td>
					
			</tr>
		</c:forEach>
	</tbody>
</m:table>