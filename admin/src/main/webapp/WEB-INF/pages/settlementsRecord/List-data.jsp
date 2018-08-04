<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>账户流水账列表</title>
</head>
<body>
	<div class="panel">
		<m:table pageNo="${page.pageNo }" pages="${page.pages }"
			     pageSize="${page.pageSize }" total="${page.total }">
			<thead>
				<tr class='text-center'>
					<th>流水单号</th>
					<th>订单号</th>
					<th>商家名称</th>
					<th>创建时间</th>
					<th>金额(元)</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${page.data}" var="settlementsRecord">
					<tr>
						<td>${settlementsRecord.id }</td>
						<td>${settlementsRecord.orderId }</td>
						<td>${settlementsRecord.store.name }</td>
						<td><fmt:formatDate value="${settlementsRecord.createdTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						<td>${settlementsRecord.amountString }</td>
					</tr>
				</c:forEach>
			</tbody>
		</m:table>
	</div>
</body>
</html>