<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th>商超广告ID</th>
			<th>商家名称</th>
			<th>商品分类</th>
			<th>创建时间</th>
			<th>图片信息</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="supermarketAd">
			<tr>
				<td>${supermarketAd.id }</td>
				<td>${supermarketAd.storeName }</td>
				<td>${supermarketAd.productCategoryName }</td>
				<td><fmt:formatDate value="${supermarketAd.createTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td align="center">
					<img alt="图片展示" style="cursor: pointer;" id="imageId" name="imageId" src="${picUrl }${supermarketAd.imageId }?iopcmd=thumbnail&type=4&width=100" >
				</td>
				<td>
					<m:hasPermission permissions="supermarketAdEdit">
						<a href='${contextPath }/supermarketAd/update/${supermarketAd.id}' class='btn btn-small btn-warning'>修改</a>
					</m:hasPermission> 
					
					<m:hasPermission permissions="supermarketAdDelete">
						<a onclick='deleteId(${supermarketAd.id},${supermarketAd.storeId });' id="deletebutton"class="btn btn-small btn-danger">删除</a>
					</m:hasPermission>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>
