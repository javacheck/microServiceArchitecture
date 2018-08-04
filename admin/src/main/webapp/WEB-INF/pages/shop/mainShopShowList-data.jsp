<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>合作者列表</title>
</head>
<body>
	<m:table pageNo="${page.pageNo }" pages="${page.pages }" id="mainShopListDataId"
		pageSize="${page.pageSize }" total="${page.total }">
		<thead>
			<tr class='text-center'>
				<th>总部名称</th>
				<th>手机号码</th>
				<th>是否会员共享</th>
				
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.data}" var="mainShop">
				<tr style="cursor: pointer;" val="${mainShop.id }">
					<td>${mainShop.name }</td>
					<td>${mainShop.mobile }</td>
					<td>${mainShop.isShareUser == 0 ? '不共享' : '共享' }</td>
				</tr>
			</c:forEach>
		</tbody>
	</m:table>
</body>
</html>