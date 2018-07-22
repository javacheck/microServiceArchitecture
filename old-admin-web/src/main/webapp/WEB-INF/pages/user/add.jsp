<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty User ? '添加' : '修改' }会员</title>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong><i class='icon-plust'></i>${empty User ? '添加' : '修改' }会员</strong>
		</div>
		<div class='panel-body'>
			<form method='post' class='form-horizontal'
				action="${contextPath }/user/add">
				<input name="id" type="hidden" value="${User.id }" />
				
				<div class="form-group">
					<label class="col-md-2 control-label">姓名</label>
					<div class="col-md-4">
						<input type='text' id="name" name="name"
							value="${User.name }" class='form-control' />
					</div>
				</div>

				<div class="form-group">
					<label class="col-md-2 control-label">手机</label>
					<div class="col-md-4">
						<input type='text' id="mobile" name="mobile"
							value="${User.mobile }" class='form-control' />
					</div>
				</div>
				
				<div class="form-group">
					<div class="col-md-offset-2 col-md-10">
						<input type='submit' id='submit' class='btn btn-primary'
							value="${empty User ? '添加' : '修改' }" data-loading='稍候...' />
					</div>
				</div>
			</form>
		</div>
	</div>

</body>
</html>