<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th>序号</th>
			<th>客屏广告图片</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="userScreenAd" varStatus="v">
			<tr>
				<td>${v.index+1}</td>
				<td><img alt="LOGO"  width="80" height="80" src="${userScreenAd.picUrl }"/></td>
				<td>
					<m:hasPermission permissions="userScreenAdDelete">
						<a onclick="deleteId('${userScreenAd.imageId}');" id="deletebutton"class="btn btn-small btn-danger">删除</a>
					</m:hasPermission></td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>
