<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="${contextPath}/static/js/checkSubmitParameters.js" type="text/javascript"></script>
<title>设置支付密码</title>
<script type="text/javascript">
	$(function(){
		$("#setPasswordBtn").click(function(){
			checkParameters("payAccountFormSubmit");
		});
		
		// 重写额外的判断方法
		extraFunction = function(){ 
			var password = $("#password").val();
			var surePassword = $("#surePassword").val();
			if( password != surePassword ){
				lm.alert("输入的两个密码不一致!");
				return false;
			}
			return true;
		}
		
		$("#password").keypress(function (event) {
			  var e = event || window.event,
            $tip =
	          $('#capslock'),
	                  kc = e.keyCode || e.which, // 按键的keyCode
	                  isShift = e.shiftKey || (kc == 16 ) || false; // shift键是否按住
	          if (((kc >= 65 && kc <= 90) && !isShift) || ((kc >= 97 && kc <= 122) && isShift)) {
	        	  $('#noEqual').hide();
	              $tip.show();
	          } else {
	               $tip.hide();
	          }
        });
		  
		$("#password").blur(function () {
			  $('#capslock').hide();
			  var payPassWord = $(this).val();
			  payPassWord = $.trim(payPassWord);  // 用jQuery的trim方法删除前后空格
			  if("" != payPassWord){
				  lm.post("${contextPath }/payaccount/checkPasswordIsSure/", {payPassWord:payPassWord}, function(data) {
						if(data == 1){
						   $("#password").val("");
						   $('#noEqual').show();				  
						} else {
						   $('#noEqual').hide();
						}
				 });
			  } else {
				  $('#noEqual').hide();
			  }
        });
	});
	
</script>
<style type="text/css">
#capslock {
  display:none ;
  position: relative;
  top: 0px;
  left: 150px;
  z-index: 6;
  width: 248px;
  background-color: #fffdee;
  height: 30px;
  line-height: 30px;
  border: 1px solid #edd288;
  color: #f60;
  padding: 0 10px;
}
#noEqual {
  display:none ;
  position: relative;
  top: 0px;
  left: 65px;
  z-index: 6;
  width: 370px;
  background-color: #fffdee;
  height: 30px;
  line-height: 30px;
  border: 1px solid #edd288;
  color: #f60;
  padding: 0 10px;
}
</style>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong><i class='icon-plust'></i>设置支付密码</strong>
		</div>
		<div class='panel-body'>
			<form id="payAccountFormSubmit" method='post' class='form-horizontal' action="${contextPath }/payaccount/setPassword" >
				
				<div class="form-group">
					<div id="capslock"><i></i><s></s>键盘大写锁定已打开，请注意大小写</div>
					<div id="noEqual">为了您的支付安全,支付密码不能和登录密码一致,请重新输入</div>
					<label class="col-md-1 control-label">支付密码</label>
					<div class="col-md-2">
						<input type='password' maxlength="19" isRequired="1"  tipName="支付密码" fieldType="password" id="password" name="password" value="" class='form-control'  onpaste="return  false"  autocomplete="off"/>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>

				<div class="form-group">
					<label class="col-md-1 control-label">确认支付密码</label>
					<div class="col-md-2">
						<input type='password' maxlength="19"  id="surePassword" isRequired="1" tipName="确认支付密码"  fieldType="password" name="surePassword" value="" class='form-control'  onpaste="return  false"  autocomplete="off"/>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
				
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type='button' id='setPasswordBtn' class='btn btn-small btn-primary' value="设置" />
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>