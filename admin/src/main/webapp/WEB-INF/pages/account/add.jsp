<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty account ? '添加' : '修改' }账号 </title>
<script type="text/javascript">
	
	//手机号码重复验证
	/*
		$(function() {
			$("#mobile").change(function(){
				checkMomile();
			});
		});
	
	//验证手机号码合法
	function checkMomile(){
		var mobile = $("#mobile").val();
		if(!lm.isMobile(mobile)){ 
    		lm.alert("不是正确的手机号码"); 
    		return false; 
		 }
		var returnVar=true;
	 	if(mobile!=$('#mobileUpada').val()){
			lm.postSync("${contextPath }/account/checkMobile/"+mobile,{mobile:mobile,id:"${account.id}"},function(data){
				if(data == 1){$("#error").text("用户重复");returnVar=false; }
				else{$("#error").text("");}
			});
	 	}else{$("#error").text("");}
	 	return returnVar;
	}
	*/
	//初始化事件
	$(function() {
		//性别
		var aa = "${empty account.sex ? '0':account.sex}";
		$("#account_sex_" + aa).attr("checked", true);
		
		//角色
		var nowRoles=${empty defaultroles ?[]:defaultroles};
		$("input[name='roleIds']").each(
			function() {
				var thisVal=$(this).val();
				for (var i=0;i<nowRoles.length;i++){
					if(thisVal==nowRoles[i].id){
						this.checked=true;
					}
				}
			}
		)
	
		//密码
		$("#password").blur(function() {//用户名文本框失去焦点触发验证事件
			if (!$("#password").val() || !$("#password").val().match(/([\w]){2,15}$/)){//只处验证和上面一样
				$("#errorPassword").text("密码不能为空且只能为英文或者数字");
			} else {
				$("#errorPassword").text("");
			}
	
		});
		//密码确认
		$("#password1").blur(function(){//用户名文本框失去焦点触发验证事件
			if(!$(this).val() || $(this).val() != $("#password").val() ){//只处验证和上面一样
            	$("#errorPassword1").text("密码为空或者和上面的密码不一致");
   			}else{
   				$("#errorPassword1").text("");
        	}

     	});

		//绑定 提交按钮点击事件
		$("#accountAddBtn").click(function() {
			//验证手机
			//if (!checkMomile()) {return;}
			var returnVar=false;
			var mobile = $("#mobile").val();
			mobile = $.trim(mobile);
			if( null == mobile || "" == mobile ){
				lm.alert("账号不能为空");
				return;
			}
		 	if(mobile!=$('#mobileUpada').val()){
		 		var id = $("#id").val();
				lm.postSync("${contextPath }/account/checkMobile",{mobile:mobile,id:id},function(data){
					if(data == 1){
						$("#error").text("用户重复");
						returnVar=true; 
					}else{
						$("#error").text("");
					}
				});
		 	}else{
		 		$("#error").text("");
		 	}
		 	if(returnVar){
		 		return;
		 	}
			
			//验证密码输入
			if (!$("#password").val() || !$("#password").val().match(/([\w]){2,15}$/)){//只处验证和上面一样
				$("#errorPassword").text("密码不能为空且只能为英文或者数字");
				return ;
			} else {
				$("#errorPassword").text("");
			}
			//验证密码长度
			if ($("#password").val() != $('#passwordUpada').val()) {
				if ($("#password").val().length < 6	|| $("#password").val().length > 18) {
					lm.alert("密码长度为6-18");
					return;
				}
			}
			//二次密码输入一致性
			if(!$("#password1").val || $("#password1").val() != $("#password").val() ){//只处验证和上面一样
	            $("#errorPassword1").text("密码为空或者和上面的密码不一致");
	            return ;
	   		}else{
	   			$("#errorPassword1").text("");
	        }
			//验证姓名
			//if ($.trim($("#name").val()) == ""|| $.trim($("#name").val()) == null) {lm.alert("请输入姓名");	return;	}
			//验证邮箱
			if ($.trim($("#email").val()) != "" && $.trim($("#email").val()) != null) {
				if (!lm.isEmail($('#email').val())) {lm.alert("邮箱格式不正确");return;}
			}
			//验证地址
			//if ($.trim($("#address").val()) == ""|| $.trim($("#address").val()) == null) {	lm.alert("请输入地址");return;}
			//验证身份证号码
			if (!lm.isIdCard($('#idCard').val())) {	lm.alert("身份证号码输入不正确");return;}
			//角色校检
			var roleIds = $('input:checkbox[name="roleIds"]:checked').val();
			if (roleIds == null) {
				lm.alert("至少选择一个角色!");
				return;
			}
			//提交
			$("#accountAddForm").submit();
		});//提交按钮点击事件结束
		
	});//初始化事件结束
	
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong><i class='icon-plust'></i>${empty account ? '添加' : '修改' }账号  </strong>
		</div>
		<div class='panel-body'>
			<form id="accountAddForm" repeatSubmit='1' method='post' class='form-horizontal'
				action="${contextPath }/account/add">
				
				<input name="id" id="id" type="hidden" value="${account.id }" />
				<input id = "mobileUpada" name="mobileUpada" type="hidden" value="${account.mobile }" />
				<input  maxlength="18" type="hidden" id="passwordUpada" name="passwordUpada" value="${account.password }"  />
				<div class="form-group">
					<label class="col-md-1 control-label">账号</label>
					<div class="col-md-2">
						<input type='text' id="mobile" name="mobile" maxlength="11"
							value="${account.mobile }" class='form-control' />
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" id="error"></label>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">密码</label>
					<div class="col-md-2">
						<input  maxlength="18" type="password" id="password" name="password"
							value="${account.password }" class='form-control' />
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" id = "errorPassword" ></label>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">密码确认</label>
					<div class="col-md-2">
						<input  maxlength="18" type="password" id="password1" name="password1"
							value="${account.password }" class='form-control' />
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" id = "errorPassword1" ></label>
				</div>
				
				
				
				<div class="form-group">
					<label class="col-md-1 control-label">姓名</label>
					<div class="col-md-2">
						<input type='text' maxlength="20" id="name" name="name" value="${account.name }"
							class='form-control' />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">性别</label>
					<div class="col-md-2">
						男<input id="account_sex_0"  type='radio' value="0" name="sex"/>
						女<input id="account_sex_1"  type='radio' value="1" name="sex"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">邮箱</label>
					<div class="col-md-2">
						<input type='text' id="email" name="email" maxlength="100"	value="${account.email }" class='form-control' />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">地址</label>
					<div class="col-md-2">
						<input type='text' id="address" name="address" maxlength="150" value="${account.address }" class='form-control' />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">身份证号码</label>
					<div class="col-md-2">
						<input maxlength="20" type='text' id="idCard" name="idCard"
							value="${account.idCard }" class='form-control' />
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
				
				
				<div class="form-group">
					<label class="col-md-1 control-label">角色</label>
					<div class="col-md-4">
					<c:forEach items="${roles }" var="role">
					 	<label class="checkbox-inline"> <input type="checkbox" roleVal="${role.value }" name="roleIds" value="${role.id }"> ${role.name } </label>
		            </c:forEach>  
		            </div>
				</div>
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button" repeatSubmit='1'  id='accountAddBtn' class='btn btn-primary'
							value="${empty account ? '添加' : '修改' }" data-loading='稍候...' />
					</div>
				</div>
			</form>
		</div>
	</div>

</body>
</html>