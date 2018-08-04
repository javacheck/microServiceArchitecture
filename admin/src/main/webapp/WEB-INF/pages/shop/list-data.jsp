<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th>商家名称</th>
			<th>商家类型</th>
			<th>所属组织</th>
			<th>代理商</th>
			<th>手机号码</th>
			<th>营业状态</th>
			<c:if test="${isStore == null }">
				<th>操作</th>
			</c:if>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="shop">
			<tr>
				<td>${shop.name }</td>
				<td>${shop.shopTypeName }</td>
				<td>${shop.organizationName }</td>
				<td>${shop.agentName }</td>
				<td>${shop.mobile }</td>
				<td>${shop.status == 1 ? '营业中': '已结业' }</td>
				<c:if test="${isStore == null }">
				
				<td><a href='${contextPath }/shop/view/${shop.id}'
							class='btn btn-small btn-info'>查看</a>
							<m:hasPermission permissions="shopEdit">
						<a href='${contextPath }/shop/update/${shop.id}'
							class='btn btn-small btn-warning'>修改</a>
					</m:hasPermission> 
					<c:if test="${isStore == null }">
						<a href='${contextPath }/shop/businessList/${shop.id}'
						class='btn btn-small btn-success'>结算账户</a> 
					</c:if>
					<m:hasPermission
						permissions="shopDelete">
						<!-- <a onclick='deleteById(${shop.id});' id="deletebutton"
							class="btn btn-small btn-danger">删除</a>
							 -->
					</m:hasPermission></td>
				</c:if>
			</tr>
		</c:forEach>
	</tbody>
</m:table>
