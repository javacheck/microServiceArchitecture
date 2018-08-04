<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<m:table pageNo="${page.pageNo }" pages="${page.pages }" pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th>商家</th>
			<th>授权商户的ID</th>
			<th>授权类型</th>
			<th>刷新令牌（授权Token）</th>
			<th>创建时间</th>
			<th>响应状态码</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="my">
			<tr>
				<td>${my.storeName }</td>
				<td>${my.user_id }</td>
				<td>${my.grant_type }</td>
				<td>${my.refresh_token }</td>
				<td><fmt:formatDate value="${my.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${my.code }</td>
				<td>
					<button type="button" data-toggle="modal" name='infoButton' data-remote="${contextPath }/alipayAuthSituation/info/showMode/${my.getAuthID }" class="btn btn-primary ">查看详情</button>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>
