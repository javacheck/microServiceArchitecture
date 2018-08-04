<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty account ? '添加' : '修改' }账号密码 </title>
<script type="text/javascript">

function updatePwd(){
    var id= $("#id").val();// 账号ID
    var password=$("#password").val();//密码
    if (!$("#password").val() || !$("#password").val().match(/([\w]){2,15}$/)){//只处验证和上面一样
		lm.alert("密码不能为空且只能为英文或者数字");
    	return;
	}
    if ($("#password").val().length < 6	|| $("#password").val().length > 18) {
		lm.alert("密码长度为6-18");
		return;
	}
    if(!$("#password1").val() || $("#password1").val() != $("#password").val() ){//只处验证和上面一样
    	lm.alert("密码为空或者和上面的密码不一致");
    	return;
	}
	lm.post("${contextPath}/shopAgent/updatePwd",{id:id,password:password},function(data){
		if(data=="1"){
	    	lm.alert("账号密码修改成功！");
	    	 window.location.href="${contextPath}/shopAgent/list";
		}
	}); 
}	
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong><i class='icon-plust'></i>${empty account ? '添加' : '修改' }账号密码  </strong>
		</div>
		<div class='panel-body'>
			<form>
			<div  class='form-horizontal'>
				<input name="id" type="hidden" id="id" value="${account.id }" />
				<div class="form-group">
					<label class="col-md-1 control-label">手机号码(账号)</label>
					<div class="col-md-2">
						<input type='text' id="mobile" name="mobile" value="${account.mobile }" class='form-control' disabled="disabled"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>密码</label>
					<div class="col-md-2">
						<input  maxlength="18" type="password" id="password" name="password" value="${account.password }" class='form-control' />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>密码确认</label>
					<div class="col-md-2">
						<input  maxlength="18" type="password" id="password1" name="password1" value="" class='form-control' />
					</div>
				</div>
				
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button"  id="accountAddBtn" class="btn btn-primary"
							value="${empty account ? '添加' : '修改' }" onclick="updatePwd();"/>
					</div>
				</div>
			</div>
			</form>
		</div>
	</div>

</body>
</html>