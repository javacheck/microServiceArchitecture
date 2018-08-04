<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商家列表</title>
</head>
<body>
	<div class="panel">
		<m:table pageNo="${page.pageNo }" pages="${page.pages }" id = "shop_ListData_mode_Id"
			     pageSize="${page.pageSize }" total="${page.total }">
			<thead>
				<tr class='text-center'>
					<th>商家名称</th>
					<th>手机号码</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${page.data}" var="shop">
					<tr style="cursor: pointer;" mobile = "${shop.mobile }"  shopId="${shop.id }" shopName="${shop.name }">
						<td>${shop.name }</td>
						<td>${shop.mobile }</td>
					</tr>
				</c:forEach>
			</tbody>
		</m:table>
	</div>
</body>
</html>