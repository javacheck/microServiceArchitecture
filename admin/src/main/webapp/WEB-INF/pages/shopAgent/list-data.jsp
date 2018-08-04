<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr>
			<th>手机</th>
			<th>商家/代理商</th>
			<th>所属商家/代理商</th>
			<th>创建时间</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="account">
			<tr>
				<td>${account.mobile }</td>
				<td>
					<c:if test="${account.type==2 }">代理商</c:if>
					<c:if test="${account.type==3 }">商家</c:if>
				</td>
				<td>
					<c:if test="${not empty account.storeName}">${account.storeName }</c:if>
					<c:if test="${not empty account.agentName}">${account.agentName }</c:if>
				</td>
				<td><fmt:formatDate value="${account.createdTime }"
						pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td>
					<m:hasPermission permissions="shopAgentEdit"><a id="update_id"
					href="${contextPath }/shopAgent/update/${account.id}"
					class="btn btn-small btn-warning">修改密码</a>
					</m:hasPermission>
				</td>
					
			</tr>
		</c:forEach>
	</tbody>
</m:table>