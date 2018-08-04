<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<m:table pageNo="${page.pageNo }" pages="${page.pages } "
	id="shopListDataId" pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th>商家名称</th>
			<th>手机号码</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="shop">
			<tr style="cursor: pointer;" val="${shop.id }">
				<td>${shop.name }</td>
				<td>${shop.mobile }</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>
