<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr> 
			<th>用户</th>
			<th>服务类型</th>
			<th>标题</th>
			<th>服务内容</th>
			<th>服务地址</th>
			<th>创建时间</th>
			<th>审核状态</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="publish">
			<tr>
				<td>${publish.userMobile }</td>
				<td>${publish.type == 0 ? '买买买' : '卖卖卖' }</td>
				<td>${publish.title }</td>
				<td>${publish.content }</td>
				<td>${publish.address }</td>
				<td><fmt:formatDate value="${publish.createdTime }" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
				<td>
					<c:if test="${publish.status==0 }">审核中</c:if> 
				    <c:if test="${publish.status==1 }">审核通过</c:if>
				    <c:if test="${publish.status==2 }">撤销</c:if>
				    <c:if test="${publish.status==3 }">审核不通过</c:if>
				</td>
				<td>
					<c:if test="${publish.status==0}">
						<button type="button" onclick="showModelWindow(${publish.id},0)" name='showAuditMode' class="btn btn-small btn-primary">进行审核</button>
					</c:if>
					<c:if test="${publish.status==1}">
						<button type="button" onclick="showModelWindow(${publish.id},2)" name='cancelAuditMode' class="btn btn-small btn-primary">撤销审核状态</button>
					</c:if>
					<c:if test="${publish.status==2}">
						已撤销
					</c:if>
					<c:if test="${publish.status==3}">
						审核不通过
					</c:if>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>