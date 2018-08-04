<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }" >
	<thead>
		<tr> 
			<th>会员旧手机号</th>
			<th>会员新手机号</th>
			<th>操作商家</th>
			<th>操作人</th>
			<th>操作时间</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="userUpdateMobileRecord">
			<tr >
				<td>${userUpdateMobileRecord.oldMobile }</td>
				<td>${userUpdateMobileRecord.newMobile }</td>
				<td>${userUpdateMobileRecord.accountStoreName }</td>
				<td>${userUpdateMobileRecord.accountName }</td>
				<td><fmt:formatDate value="${userUpdateMobileRecord.createdTime }" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>