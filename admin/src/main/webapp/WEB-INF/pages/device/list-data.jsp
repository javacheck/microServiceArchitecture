<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th>SN编号</th>
			<th>终端名称</th>
			<th>系统序列号</th>
			<th>商家</th>
			<th>厂商</th>
			<th>状态</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="device">
			<tr>
				<td>${device.deviceSn }</td>
				<td>${device.deviceName }</td>
				<td>${device.serialId }</td>
				<td>${device.storeName }</td>
				<td>${device.factory }</td>
				<td><c:if test="${device.status==0 }">停用</c:if> <c:if
						test="${device.status==1 }">启用</c:if></td>
				<td><m:hasPermission permissions="deviceEdit">
						<a href='${contextPath }/device/update/${device.id}'
							class='btn btn-small btn-warning'>修改</a>
					</m:hasPermission> <m:hasPermission permissions="deviceDelete">
						<!--  <a onclick='deleteId(${device.id});' id="deletebutton"class="btn btn-small btn-danger">删除</a>-->
					</m:hasPermission></td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>
