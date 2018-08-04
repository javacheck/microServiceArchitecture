<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<m:table pageNo="${page.pageNo }" pages="${page.pages }" pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th>员工名称</th>
			<th>性别</th>
			<th>类型</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="employee">
			<tr>
				<td>${employee.name }</td>
				<td>
					<c:if test="${employee.sex == 0 }">
						女
					</c:if>
					<c:if test="${employee.sex == 1 }">
						男
					</c:if>
				</td>
				<td>${employee.type == 0 ? '导购员' : '收银员'  }</td>
				<td>
					<m:hasPermission permissions="employeeEdit">
						<a href='${contextPath }/employee/update/${employee.id}' class='btn btn-small btn-warning'>修改</a>
					</m:hasPermission>
					
					<m:hasPermission permissions="employeeDelete">
						<a onclick='deleteById(${employee.id });' id="deletebutton"class="btn btn-small btn-danger">删除</a>
					</m:hasPermission>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>