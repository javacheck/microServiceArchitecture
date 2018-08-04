<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<m:table pageNo="${page.pageNo }" pages="${page.pages }" pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr>
			<th width="30%">商家名称</th>
			<th>剩余短信数量</th>
			<th>充值数量</th>
			<th>充值金额</th>
			<th>充值时间</th>
			<th>操作人员</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="messageRechargeRecode">
			<tr>
				<td>${messageRechargeRecode.storeName }</td>
				<td>${messageRechargeRecode.remainNumber }</td>
				<td>${messageRechargeRecode.number }</td>
				<td>${messageRechargeRecode.price }</td>
				<td><fmt:formatDate value="${messageRechargeRecode.createdTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td>
					${messageRechargeRecode.operationName }
				</td>
					
			</tr>
		</c:forEach>
	</tbody>
</m:table>