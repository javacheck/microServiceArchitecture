<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr> 
			<th>名字</th>
			<th>上级名字</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="category">
			<tr>
				<td>${category.name }</td>
				<td>${category.parentName }</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>