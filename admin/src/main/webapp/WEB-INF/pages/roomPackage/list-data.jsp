<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<m:table pageNo="${page.pageNo }" pages="${page.pages }" pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th>套餐名称</th>
			<th>创建时间</th>
			<th>创建人</th>
			<th>更新时间</th>
			<th>更新人</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="roomPackage">
			<tr>
				<td>${roomPackage.name }</td>
				<td><fmt:formatDate value="${roomPackage.createdTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td>${roomPackage.createName }</td>
				<td><fmt:formatDate value="${roomPackage.updatedTime == null ? roomPackage.createdTime : roomPackage.updatedTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td>${roomPackage.updateName == null ? roomPackage.createName : roomPackage.updateName }</td>
				<td>
					<m:hasPermission permissions="roomPackageEdit"> 
						<a href='${contextPath }/roomPackage/update/${roomPackage.id}' class='btn btn-small btn-warning'>修改</a>						
					</m:hasPermission>
					<m:hasPermission permissions="roomPackageDelete"> 
						<a id="deleteBtn" onclick="deleteConfirm(${roomPackage.id})" class="btn btn-small btn-danger">删除</a>
					</m:hasPermission>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>