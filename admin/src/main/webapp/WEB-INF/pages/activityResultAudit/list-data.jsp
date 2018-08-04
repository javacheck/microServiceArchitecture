<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th>会员名称</th>
			<th>手机号码</th>
			<th>参与时间</th>
			<th>上传时间</th>
			<th>审核状态</th>
			<th>审核人员</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="activityResultAudit">
			<tr>
				<td>${activityResultAudit.name }</td>
				<td>${activityResultAudit.mobile }</td>
				<td><fmt:formatDate value="${activityResultAudit.upLoadTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td><fmt:formatDate value="${activityResultAudit.participationTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>
					<c:if test="${activityResultAudit.status==0 }">审核中</c:if> 
				    <c:if test="${activityResultAudit.status==1 }">审核通过</c:if>
				    <c:if test="${activityResultAudit.status==2 }">审核不通过</c:if>
				</td>
				<td>${activityResultAudit.accountName }</td>
				<td>
					<m:hasPermission permissions="activityResultAudit">
						<c:if test="${activityResultAudit.status==0 }">
							<button type="button" onclick="showModelWindow(${activityResultAudit.id },${activityResultAudit.userId })" name='showAuditMode' class="btn btn-small btn-primary">进行审核</button>						
						</c:if>
					</m:hasPermission>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>