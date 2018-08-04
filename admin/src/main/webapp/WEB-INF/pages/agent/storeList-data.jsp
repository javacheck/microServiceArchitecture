<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商家查询列表</title>
</head>
<body>
	<div class="panel">
		<m:table pageNo="${page.pageNo }" pages="${page.pages }"
			     pageSize="${page.pageSize }" total="${page.total }">
			<thead>
				<tr class='text-center'>
					<th>商家名称</th>
					<th>商家类型</th>
					<th>POS商户名称</th>
					<th>代理商名称</th>
					<th>POS商户号</th>
					<th>手机号码</th>
					<th>创建时间</th>
					<th>联系人</th>
					<th>地址</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${page.data}" var="shop">
					<tr>
						<td>${shop.name }</td>
						<td>${shop.shopTypeName }</td>
						<td>${shop.merchantName }</td>
						<td>${shop.agentName }</td>
						<td>${shop.merchantNo }</td>
						<td>${shop.mobile }</td>
						<td>
							<fmt:formatDate value="${shop.createdTime }" pattern="yyyy-MM-dd HH:mm:ss" />
						</td>
						<td>${shop.contact }</td>
						<td>${shop.address }</td>
					</tr>
				</c:forEach>
			</tbody>
		</m:table>
	</div>
</body>
</html>