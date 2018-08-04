<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="panel" id="show_packageManager_Model">
<m:table pageNo="${page.pageNo }" pages="${page.pages }" pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th>套餐名称</th>
			<th>服务次数</th>
			<th>套餐价格</th>
			<th>备注</th>
			<th>创建时间</th>
			<th>操作人</th>
			<th width="15%">操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="package_data">
			<tr>
				<td>${package_data.name }</td>
				<td>${package_data.times }</td>
				<td>${package_data.packagePrice }</td>
				<td>
					${package_data.remark }
				</td>
				<td><fmt:formatDate value="${package_data.createdTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${package_data.operator }</td>
				<td>
					<m:hasPermission permissions="commodityManagerEdit"> 
						<a href='${contextPath }/commodityManager/update/${package_data.id}' class='btn btn-small btn-warning'>修改</a>						
					</m:hasPermission>
					<m:hasPermission permissions="commodityManagerDelete"> 
						<a onclick='deleteById(${package_data.id});' id="deletebutton"class="btn btn-small btn-danger">删除</a>						
					</m:hasPermission>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>
</div>