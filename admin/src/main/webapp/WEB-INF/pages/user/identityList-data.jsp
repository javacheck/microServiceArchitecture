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
			<th>证件类型</th>
			<th>证件号码</th>
			<th>审核状态</th>
			<th>创建时间</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="user">
			<tr>
				<td>${user.mobile }</td>
				<td>${user.realName }</td>
				<td>
					<c:if test="${user.identifyTypeId==1 }">身份证</c:if>
					<c:if test="${user.identifyTypeId==2 }">军官证</c:if>
					<c:if test="${user.identifyTypeId==3 }">户口本</c:if>
				</td>
				<td>${user.identity}</td>
				<td>
					<c:if test="${user.idAudit==0 }">审核中</c:if>
					<c:if test="${user.idAudit==1 }">审核成功</c:if>
					<c:if test="${user.idAudit==2 }">审核失败</c:if>
				</td>
				<td><fmt:formatDate value="${user.createdTime }" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
				<td>
				<m:hasPermission permissions="idAuditEdit">
					<c:if test="${user.idAudit==0}">
						<a id="update_idAudit_${user.id}" onclick="updateIdAuditConfirm(${user.id},1)"class="btn btn-small btn-info">通过</a>
						<a id="update_idAudit_${user.id}" onclick="updateIdAuditConfirm(${user.id},2)"class="btn btn-small btn-danger">不通过</a>
					</c:if>
					<c:if test="${user.idAudit==1}">
						审核成功
					</c:if>
					<c:if test="${user.idAudit==2}">
						<a id="update_idAudit_${user.id}" onclick="updateIdAuditConfirm(${user.id},1)"class="btn btn-small btn-info">通过</a>
					</c:if>
				</m:hasPermission>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>