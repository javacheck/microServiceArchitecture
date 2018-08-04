<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th>广告ID</th>
			<th>商家名称</th>
			<th>标题</th>
			<th>广告URL</th>
			<th>LOGO</th>
			<th>城市地址</th>
			<th>是否有效</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="htmlAd">
			<tr>
				<td>${htmlAd.id }</td>
				<td>${htmlAd.storeName }</td>
				<td>${htmlAd.title }</td>
				<td>${htmlAd.url }</td>
				<td><img alt="LOGO"  width="80" height="80" src="${htmlAd.picUrl }"/></td>
				<td>${htmlAd.cityName }</td>
				<td>
					<c:if test="${htmlAd.valid==0 }">有效</c:if>
					<c:if test="${htmlAd.valid==1 }">无效</c:if>
				</td>
				<td>
					<m:hasPermission permissions="htmlAdEdit">
						<a href='${contextPath }/htmlAd/update/${htmlAd.id}' class='btn btn-small btn-warning'>修改</a>
					</m:hasPermission> 
					
					<m:hasPermission permissions="htmlAdDelete">
						<a onclick='deleteId(${htmlAd.id});' id="deletebutton"class="btn btn-small btn-danger">删除</a>
					</m:hasPermission></td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>
