<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商家类型列表</title>
</head>
<body>
	<m:table pageNo="${page.pageNo }" pages="${page.pages }" id="shopTypeListDataId"
		pageSize="${page.pageSize }" total="${page.total }">
		<thead>
			<tr class='text-center'>
				<th>商家类型名称</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.data}" var="shopType">
				<tr style="cursor: pointer;" val="${shopType.id }">
					<td>${shopType.name }</td>
				</tr>
			</c:forEach>
		</tbody>
	</m:table>
</body>
</html>