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
			<th>名字</th>
			<th>创建时间</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="user">
			<tr>
				<td>${user.mobile }</td>
				<td>${user.name }</td>
				<td><fmt:formatDate value="${user.createdTime }" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
				<td><a href="${contextPath }/user/delete/${user.id}" class="btn btn-small btn-danger">删除</a> 
				<a href="${contextPath }/user/update/${user.id}" class="btn btn-small btn-danger">修改</a> </td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>