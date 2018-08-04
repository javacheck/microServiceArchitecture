<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>提现记录列表</title>
</head>
<body>
	<div class="panel">
		<m:table pageNo="${page.pageNo }" pages="${page.pages }"
			     pageSize="${page.pageSize }" total="${page.total }">
			<thead>
				<tr class='text-center'>
					<th>银行名称</th>
					<th>银行账号</th>
					<th>账号名称</th>
					<th>提现金额</th>
					<th>操作时间</th>
					<th>操作状态</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${page.data}" var="withdraw">
					<tr>
						<td>${withdraw.bankName }</td>
						<td>${withdraw.bankAccountNumber }</td>
						<td>${withdraw.bankAccountName }</td>
						<td>${withdraw.amountString }</td>
						<td><fmt:formatDate value="${withdraw.createdTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						<td>
						    <c:if test="${withdraw.status == 0}">
						    	处理中
						    </c:if>
						    <c:if test="${withdraw.status == 1}">
						    	成功
						    </c:if>
						    <c:if test="${withdraw.status == 2}">
						    	失败
						    </c:if>
						     <c:if test="${withdraw.status == 3}">
						    	审核成功
						    </c:if>
						     <c:if test="${withdraw.status == 4}">
						    	审核失败
						    </c:if>
					   </td>
					</tr>
				</c:forEach>
			</tbody>
		</m:table>
	</div>
</body>
</html>