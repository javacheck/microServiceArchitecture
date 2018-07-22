<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr>
			<th>名字</th>
			<th>创建时间</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="store">
			<tr>
				<td>${store.name }</td>
				<td><fmt:formatDate value="${store.createdTime }"
						pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td><a name="updateStoreBtn" val="${store.id }" href="${contextPath }/store/edit/${store.id}"class="btn btn-small btn-warning">修改</a> 
				<a name="deleteStoreBtn" onclick="deleteStore(${store.id});" class="btn btn-small btn-danger">删除</a></td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>