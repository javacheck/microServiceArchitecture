<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>结算账户列表</title>
</head>
<body>
	<div class="panel">
		<m:table pageNo="${page.pageNo }" pages="${page.pages }"
			     pageSize="${page.pageSize }" total="${page.total }">
			<thead>
				<tr class='text-center'>
					<th>银行名称</th>
					<th>卡号</th>
					<th>持卡人姓名</th>
					<th>持卡人手机</th>
					<th>绑定时间</th>
					<th>是否默认结算账户</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${page.data}" var="businessBank">
					<tr>
						<td>${businessBank.bankName }</td>
						<td>${businessBank.accountNumber }</td>
						<td>${businessBank.accountName }</td>
						<td>${businessBank.mobile }</td>
						<td>${businessBank.createdTime }</td>
						<c:if test="${businessBank.isDefault==1 }">
							<td>是</td>
						</c:if>
						<c:if test="${businessBank.isDefault==0 }">
							<td>否</td>
						</c:if>
						<td> 
						<m:hasPermission permissions="agentEdit">
							<a href='${contextPath }/agent/business/update/${businessBank.id}' class='btn btn-small btn-warning'>修改</a>
							<a onclick='deleteBusinessBank(${businessBank.id},${businessBank.businessId})' id="deletebutton" class="btn btn-small btn-danger">删除</a>
						</m:hasPermission>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</m:table>
	</div>
</body>
</html>