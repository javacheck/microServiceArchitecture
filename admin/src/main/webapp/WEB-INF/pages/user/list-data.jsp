<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr> 
			<th>手机号码</th>
			<th>姓名</th>
			<th>性别</th>
			<!-- <th>商店</th> -->
			<th>创建时间</th>
			<th>推荐人</th>
<!-- 			<th>操作</th> -->
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="user">
			<tr>
				<td>${user.mobile }</td>
				<td>${empty user.realName ? user.name:user.realName}</td>
				<td>
				<c:if test="${user.sex==0}">男</c:if>
				<c:if test="${user.sex==1}">女</c:if>
				<c:if test="${user.sex==2}">保密</c:if>
				</td>
				<!-- <td>${user.store.name }</td> -->
				<td><fmt:formatDate value="${user.createdTime }" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
<!-- 				<td> -->
<%-- 				<m:hasPermission permissions="userDelete"> --%>
<%-- 					<c:if test="${user.status==1}"><a id="delete_account_${user.id}"onclick="deleteConfirm(${user.id},0)"class="btn btn-small btn-info">启用</a></c:if> --%>
<%-- 					<c:if test="${user.status==0}"><a id="delete_account_${user.id}"onclick="deleteConfirm(${user.id},1)"class="btn btn-small btn-danger">禁用</a></c:if> --%>
					
<%-- 				</m:hasPermission> --%>
<!-- 				</td> -->
				<td>${user.recommended }</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>