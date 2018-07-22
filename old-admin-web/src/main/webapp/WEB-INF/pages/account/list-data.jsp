<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<m:hasPermission permissions="accountDelete" flagName="deleteFlag" />
<m:hasPermission permissions="accountEdit" flagName="editFlag" />

<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr>
			<th>手机号码</th>
			<th>名字</th>
			<th>性别</th>
			<th>邮箱</th>
			<th>地址</th>
			<th>身份证号</th>
			<th>创建时间</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="account">
			<tr>
				<td>${account.mobile }</td>
				<td>${account.name }</td>
				<td>${account.sex }</td>
				<td>${account.email }</td>
				<td>${account.address }</td>
				<td>${account.idCard }</td>
				<td><fmt:formatDate value="${account.createdTime }"
						pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td><m:hasPermission permissions="accountDelete">
						<a id="delete_account_${account.id}"
							onclick="deleteConfirm(${account.id})"
							class="btn btn-small btn-danger">删除</a>
					</m:hasPermission> 
					<m:hasPermission permissions="accountEdit"><a id="update_id"
					href="${contextPath }/account/update/${account.id}"
					class="btn btn-small btn-warning">修改</a>
					</m:hasPermission>
				</td>
					
			</tr>
		</c:forEach>
	</tbody>
</m:table>