<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th>名称</th>
			<th>通知商家</th>
			<th>失效时间</th>
			<th>创建时间</th>
			<th>创建人</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="inform">
			<tr>
				<td>${inform.name }</td>
				<td>${inform.storeName }</td>
				<td><fmt:formatDate value="${inform.loseTime }" pattern="yyyy-MM-dd"/></td>
				<td><fmt:formatDate value="${inform.createdTime }" pattern="yyyy-MM-dd"/></td>
				<td>${inform.createdName }</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>
