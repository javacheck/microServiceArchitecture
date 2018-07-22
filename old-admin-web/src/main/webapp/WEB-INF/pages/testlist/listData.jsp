<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr> 
			<th>用户名</th>
			<th>创建时间</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="user">
			<tr>
				<td>${user.get('username') }</td>
				<td>${user.get('createdTime') }</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>