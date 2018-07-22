<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>登录</title>
<link rel="stylesheet" href="${staticPath }/css/all.admin.css">
<script type="text/javascript">var config={}</script>
<script type="text/javascript" src="${staticPath }/js/all.admin.js"></script>
<script type="text/javascript">
	$(function() {
		$("#_captcha").click(function() {
			$(this).attr("src", "${contextPath }/captcha?" + new Date());
		});
	});
</script>
<style>
.user-control-nav {
	margin-bottom: 20px;
}

@media ( max-width : 480px) {
	.hidden-xxs {
		display: none
	}
	.page {
		font-size: 12px
	}
}

@media ( max-width : 400px) {
	.hidden-xxxs {
		display: none
	}
}

#login .panel {
	width: 80%;
	margin: 5% auto;
	max-width: 450px;
}

#login .panel-body {
	min-height: 230px;
}

#login .panel-heading {
	background-color: transparent;
}

.btn-oauth {
	text-align: left;
	padding-left: 130px;
	font-size: 24px;
	line-height: 50px;
	position: relative;
	text-shadow: 0 1px 0 rgba(0, 0, 0, 0.5);
	color: #333;
	background-color: #FCFCFC;
	border: 1px solid #DEDEDE;
}

.btn-oauth:hover {
	color: #333;
	background-color: #E8E8E8;
	border-color: #ccc;
	-moz-box-shadow: 0 2px 1px rgba(0, 0, 0, 0.1);
	-webkit-box-shadow: 0 2px 1px rgba(0, 0, 0, 0.1);
	box-shadow: 0 2px 1px rgba(0, 0, 0, 0.1);
}

.btn-oauth .icon {
	font-size: 40px;
	position: absolute;
	left: 75px;
	text-shadow: none;
}

.btn-oauth .icon:before {
	display: none;
}

#adminLogin {
	max-width: 500px;
	margin: 5% auto;
	padding: 0;
	background-color: #FFF;
	border: 1px solid #DDD;
	-moz-box-shadow: 0px 1px 15px rgba(0, 0, 0, 0.15);
	-webkit-box-shadow: 0px 1px 15px rgba(0, 0, 0, 0.15);
	box-shadow: 0px 1px 15px rgba(0, 0, 0, 0.15);
	border-radius: 6px;
}

#responser {
	line-height: 30px;
	font-size: 12px
}

#logo {
	margin-bottom: 10px;
}

#adminLogin #siteName {
	padding: 20px 20px 10px;
	background: #fafafa;
	border-bottom: 1px solid #e5e5e5;
	border-radius: 6px 6px 0 0
}

#adminLogin #ajaxForm {
	padding: 20px 20px;
}

#adminLogin #submit {
	min-width: 100px;
}

@media ( max-width : 700px) {
	#adminLogin {
		border: none;
		margin: 0 auto;
		box-shadow: none;
		padding: 20px 15px;
		background-color: #fff;
		margin: 0 auto;
		border-radius: 0
	}
	body {
		padding: 0;
	}
	.container {
		padding: 0;
	}
	#adminLogin #siteName {
		background: #fff
	}
}

@media ( max-width : 767px) {
	#login .panel {
		margin: 20px auto;
		width: 100%;
	}
	#login .panel-heading {
		padding: 0 0 10px 0;
	}
	#login .panel-body {
		padding: 10px 0;
		min-height: inherit;
	}
	.btn-oauth {
		padding-left: 80px;
	}
	.btn-oauth .icon {
		left: 20px;
	}
}

#siteName {
	font-size: 14px;
	color: #444;
	font-weight: 600;
	display: block;
}

hr {
	margin-top: 8px;
}

body {
	background-color: #f6f5f5
}
</style>
</head>
<body>
	<div class='container'>
		<div id='adminLogin'>
			<div id='siteName'>后台管理系统</div>
			<form method='post'>
				<c:if test="${not empty error }">
					<div id='formError' class='alert alert-danger'>${error }</div>
				</c:if>
				<div class='row'>
					<div class='col-xs-8'>
						<table class="table table-form">
							<tr>
								<th class='w-60px'>用户名</th>
								<td><input type='text' name='username' id='username'
									value='' class='form-control' placeholder='请输入用户名' /></td>
							</tr>
							<tr>
								<th>密码</th>
								<td><input type='password' name='password' id='password'
									value='' class='form-control' placeholder='请输入密码' /></td>
							</tr>
							<tr>
								<th>验证码</th>
								<td><input type='text' id="captcha" name="captcha" value=''
									class='form-control' placeholder='请输入验证码' /> <img
									id="_captcha" style="cursor: pointer; margin-top: 5px;" alt=""
									src="${contextPath }/captcha"></td>
							</tr>
							<tr>
								<th></th>
								<td><input type='submit'
									class='btn btn-primary btn' value='登录' data-loading='稍候...' />
								</td>
							</tr>
						</table>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>