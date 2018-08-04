<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr>
			<th>序号</th>
			<th>类型名称</th>
			<th>创建时间</th>
			<th>创建人</th>
			<th>更新时间</th>
			<th>更新人</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" varStatus="status" var="roomCategory">
			<tr>
				<td>${status.count}</td>
				<td>${roomCategory.name }</td>
				<td>${roomCategory.createDate }</td>
				<td>${roomCategory.createAccountName }</td>
				<td>${roomCategory.updateDate }</td>
				<td>${roomCategory.updateAccountName }</td>
				<td>
					<m:hasPermission permissions="accountEdit"><a id="update_id"
					href="${contextPath }/roomCategory/update/${roomCategory.id}"
					class="btn btn-small btn-warning">修改</a>
					</m:hasPermission>
					<!-- 
					<m:hasPermission permissions="accountDelete">
					</m:hasPermission> 
					 -->
					<a onclick="deleteConfirm(${roomCategory.id})"
						class="btn btn-small btn-danger">删除</a>
				</td>
					
			</tr>
		</c:forEach>
	</tbody>
</m:table>