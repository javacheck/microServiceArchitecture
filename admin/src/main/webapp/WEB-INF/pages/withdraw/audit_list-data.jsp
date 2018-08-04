<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>提现审核列表</title>
</head>
<body>
	<div class="panel">
		<m:table pageNo="${page.pageNo }" pages="${page.pages }"
			     pageSize="${page.pageSize }" total="${page.total }">
			<thead>
				<tr class='text-center'>
					<th>申请ID</th>
					<th>户主名称 </th>
					<th>银行名称</th>
					<th>银行账号</th>
					<th>申请状态</th>
					<th>提现金额(元)</th>
					<th>创建时间</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${page.data}" var="withdraw">
					<tr>
						<td>${withdraw.id }</td>
						<td>${withdraw.nameS }</td>
						<td>${withdraw.bankName }</td>
						<td>${withdraw.bankAccountNumber }</td>
						<td>
							<c:if test="${withdraw.status == 0 }">
								处理中
							</c:if>
						</td>
						<td>${withdraw.amountString }</td>
						<td><fmt:formatDate value="${withdraw.createdTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						<td>
						<c:if test="${withdraw.status==0}">
							<a id="delete_withdraw_${withdraw.id}" onclick="deleteConfirm(${withdraw.id},3,${withdraw.ownerId},${withdraw.type})"class="btn btn-small btn-info">通过</a>
							<a id="delete_withdraw_${withdraw.id}" onclick="deleteConfirm(${withdraw.id},4,${withdraw.ownerId},${withdraw.type})"class="btn btn-small btn-danger">不通过</a>
						</c:if>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</m:table>
	</div>
</body>
</html>