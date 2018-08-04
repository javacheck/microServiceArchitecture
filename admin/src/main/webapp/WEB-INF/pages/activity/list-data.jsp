<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th>活动标题</th>
			<th>开始时间</th>
			<th>结束时间</th>
			<th>活动区域</th>
			<th>状态</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="activity">
			<tr>
				<td>${activity.title }</td>
				<td><fmt:formatDate value="${activity.startDate }" pattern="yyyy年MM月dd日"/></td>
				<td><fmt:formatDate value="${activity.endDate }" pattern="yyyy年MM月dd日"/></td>
				<td>${activity.areaName }</td>
				<td>
				     <c:if test="${activity.status==0 }">未启动</c:if> 
				     <c:if test="${activity.status==1 }">已启动</c:if>
				</td>
				<td>
					<m:hasPermission permissions="activityDelete">
						<a onclick="deleteConfirm(${activity.id})"class="btn btn-small btn-danger">删除</a>
					</m:hasPermission>
					<m:hasPermission permissions="activityEdit">
						<a href='${contextPath }/activity/update/${activity.id}' class='btn btn-small btn-warning'>修改</a>
					</m:hasPermission>
					<m:hasPermission permissions="activityDetailAdd">
						<a href='${contextPath }/activityDetail/add/${activity.id}' class='btn btn-small btn-primary'>添加详情</a>
					</m:hasPermission>
					<m:hasPermission permissions="activityDetailList">
						<a href='${contextPath }/activity/update/${activity.id}' class='btn btn-small btn-info'>详情列表</a>
					</m:hasPermission>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>