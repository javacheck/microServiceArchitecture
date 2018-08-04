<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
	function deleteConfirm(id) {
		lm.confirm("确定要删除吗？", function() {
			lm.post("${contextPath }/role/delete/" + id, {}, function(data) {
				if (data == 1) {
					noty("删除成功");
					location=location;
				}
			});
		});
	}
	
	function showModeRole(id){
		$('#mySmModal').modal('show');
	}
</script>
<title>角色列表</title>
</head>
<body>
	<div class="panel">
		<div class="panel-heading">
			<strong><i class="icon-list-ul"></i> 角色列表</strong>
			<div class="panel-actions">
				<m:hasPermission permissions="roleAdd">
				<a href='${contextPath }/role/add' class="btn btn-primary"><i class="icon-plus"></i>添加</a>
				</m:hasPermission>
			</div>
		</div>
		<div class="panel-body">
		<table class='table table-hover table-striped table-bordered'>
			<thead>
				<tr class='text-center'>
					<th>角色</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${data }" var="role">
					<tr class='text-center'>
						<td>${role.name }</td>
						<td class='operate'>
							<c:if test="${loginAccount.id==1||role.value == -1 }">
								<m:hasPermission permissions="roleEdit">
									<a class="btn btn-small btn-warning" href="${contextPath }/role/update/${role.id}">修改</a>
								</m:hasPermission>
								<c:if test="${role.value == -1 }">
									<m:hasPermission permissions="roleDelete">
										<!--  <a class="btn btn-small btn-danger"	onclick="deleteConfirm('${role.id}')">删除</a>-->
									</m:hasPermission>
								</c:if>
								<m:hasPermission permissions="rolePermission">
									<a class="btn btn-small  btn-primary" href="${contextPath }/role/permission/${role.id}/status/update">授权</a>
								</m:hasPermission>
							</c:if>
							<m:hasPermission permissions="rolePermission">
								<a class="btn btn-small btn-info" href="${contextPath }/role/permission/${role.id}/status/select">查权</a>
							</m:hasPermission>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
	</div>
</body>
</html>