<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th width="10%">合作者ID</th>
			<th width="20%">合作者appKey</th>
			<th width="20%">token</th>
			<th width="30%">合作者名称</th>
			<th width="10%">总部名称</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="partner">
			<tr>
				<td>${partner.id } </td>
				<td>${partner.appKey }</td>
				<td>${partner.token }</td>
				<td>${partner.name }</td>
				<td>${partner.storeName }</td>
				<td>
					<m:hasPermission permissions="partnerEdit">
						<a href='${contextPath }/partner/update/${partner.id}' class='btn btn-small btn-warning'>修改</a>
					</m:hasPermission>
					
					<m:hasPermission permissions="partnerDelete">
						<a onclick='deletePartner(${partner.id});' id="deletebutton" class="btn btn-small btn-danger">删除</a>
					</m:hasPermission>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>