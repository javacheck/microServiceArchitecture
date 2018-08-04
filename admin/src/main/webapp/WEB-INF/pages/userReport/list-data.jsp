<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th>举报类型</th>
			<th>举报内容</th>
			<th>联系方式</th>
			<th>创建时间</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="userReprot">
			<tr>
				<td>${userReprot.typeName }</td>
				<td>${userReprot.content }</td>
				<td>${userReprot.contact }</td>
				<td>${userReprot.createdTime }</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>
