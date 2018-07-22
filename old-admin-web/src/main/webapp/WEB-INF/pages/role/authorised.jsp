<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Cache-Control" content="no-transform">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="${staticPath }/css/zui.min.css">
<script src="${staticPath }/js/jquery-1.11.2.min.js"></script>
<script src="${staticPath }/js/zui.min.js"></script>
<!--[if lt IE 9]>
<script src='${staticPath }/js/all.ie8.js' type='text/javascript'></script>
<![endif]-->
<!--[if lt IE 10]>
<script src='${staticPath }/js/all.ie9.js' type='text/javascript'></script>
<![endif]-->
<script type="text/javascript" src="${staticPath }/js/common.js"></script>
<script type="text/javascript" src="${staticPath }/js/jquery.pager.js"></script>
<script type="text/javascript" src="${staticPath }/js/list_page.js"></script>
<script type="text/javascript">
	$(function(){
		var nowRoles = "${permissionIds}";
		$("input[name='permissionIds']").each(
			function() {
				var thisVal=$(this).val();
				for (var i=0;i<nowRoles.length;i++){
					if(thisVal==nowRoles[i]){
						this.checked=true;
					}
				}
			}
		)
	});
</script>
<title>角色列表</title>
</head>
<body>
		<div class='panel'>
		<div class='panel-heading'>
		<strong><i class='icon-plust'></i>对角色${role.name}授权</strong>
		</div>
		<div class='panel-body'>
			<form id="roleAddForm" method='post' class='form-horizontal'
				action="${contextPath }/role/permissionAdd">
				
				<input name="roleId" type="hidden" value="${role.id }" />
				
				<div class="form-group">
					<label class="col-md-2 control-label">角色</label>
					<div class="col-md-4">
					<label class="col-md-2 control-label">${role.name }</label>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-2 control-label">权限</label>
					<div class="col-md-8">
					<c:forEach items="${permissions}" var="permission">
						<div class="col-md-2">
						<input type="checkbox" value="${permission.id}" name="permissionIds">
						${permission.desc}
						</div>
					</c:forEach>
					</div>
				</div>
				
				<div class="form-group">
					<div class="col-md-offset-2 col-md-10">
						<input type="submit"  id='accountAddBtn' class='btn btn-primary'
							value="添加" data-loading='稍候...' />
					</div>
				</div>
			</form>
		</div>
	</div>
	
</body>
</html>