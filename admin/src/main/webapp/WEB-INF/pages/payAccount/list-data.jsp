<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr>
			<th>类型</th>
			<th>名称</th>
			<th>手机</th>
			<th>创建时间</th>
			<th>状态</th>
			<th>冻结金额</th>
			<th>余额</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="payAccount">
			<tr>
				<td>
				<input type="hidden" value="${payAccount.id}">
				<c:if test="${payAccount.type==0}">商家</c:if>
				<c:if test="${payAccount.type==1}">代理商</c:if>
				<c:if test="${payAccount.type==2}">用户</c:if>
				<c:if test="${payAccount.type==-1}">平台</c:if>
				</td>
				<td>${payAccount.name }</td>
				<td>${payAccount.mobile }</td>
				<td><fmt:formatDate value="${payAccount.createdTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td>
				<c:if test="${payAccount.status==0}">未激活</c:if>
				<c:if test="${payAccount.status==1}">正常</c:if>
				<c:if test="${payAccount.status==2}">冻结</c:if>
				<c:if test="${payAccount.status==3}">销户</c:if>
				<c:if test="${payAccount.status==4}">挂失</c:if>
				<c:if test="${payAccount.status==5}">锁定</c:if>
				</td>
				<td>${payAccount.frozenAmountString }</td>
				<td>${payAccount.balanceString }</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>