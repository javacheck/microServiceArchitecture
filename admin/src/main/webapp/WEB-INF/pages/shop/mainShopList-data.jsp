<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th>总部名称</th>
			<th>手机号码</th>
			<th>登录账号</th>
			<th>是否用户共享</th>
			<!-- <th>操作</th> -->
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="mainShop">
			<tr>
				<td>${mainShop.name }</td>
				<td>${mainShop.mobile }</td>
				<td>${mainShop.storeAcountName }</td>
				<td>${mainShop.isShareUser == 0 ? '不共享' : '共享' }</td>
				<!-- 
				<td>
					<m:hasPermission permissions="mainShopEdit">
						<a href='${contextPath }/shop/mainShop/update/${mainShop.id}' class='btn btn-small btn-warning'>修改</a>
					</m:hasPermission> 
					<m:hasPermission
						permissions="mainShopDelete">
						< <a onclick='deleteById(${shop.id});' id="deletebutton"
							class="btn btn-small btn-danger">删除</a>
					</m:hasPermission>
				</td>
							 -->
			</tr>
		</c:forEach>
	</tbody>
</m:table>