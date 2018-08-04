<%@tag import="cn.lastmiles.utils.SecurityUtils"%>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="modeId" required="true"%>
<%
	String mobile = SecurityUtils.getAccount().getMobile();
	this.getJspContext().setAttribute("user_Mobile", mobile);
%>
<script type="text/javascript">
$(function(){
	var InterValObj; //timer变量，控制时间
	var curCount = 120;//当前剩余秒数
	var isSend = false;
	$('#${modeId}').on('show.zui.modal', function() {
		isSend = false;
		curCount = 120;
		$("#verificationCode").val("");
		$("#forgetPayPassword").val("");
		$("#forgetSurePayPassword").val("");
		$("#tipInformation").html("");
		$("#commonTipInformation").html("");
	});
	
	$('#${modeId}').on('hidden.zui.modal', function(){
		 window.clearInterval(InterValObj);//停止计时器
         $("#payPasswordSendBtn").removeAttr("disabled");//启用按钮
         $("#payPasswordSendBtn").val("发送验证码");	
         
         $("#sureUpdatePayPasswordBtn").removeAttr("disabled");//启用按钮
	});
	
	$("#payPasswordSendBtn").click(function(){
		 var userMobile = $("#userMobile").val();
		 userMobile = $.trim(userMobile);
		 if( "" == userMobile ){
			return ; 
		 }
		 // 异步去发送验证码信息给此手机号,将isSend设置为true
		 lm.post("${contextPath }/payaccount/ajax/sendPayPassWordCode/",{},function(data){
			 if(data == 1){
				 isSend = true;
				 $("#payPasswordSendBtn").attr("disabled",true);
				 $("#payPasswordSendBtn").val("剩余"+ curCount + "秒");
				 InterValObj = window.setInterval(SetRemainTime, 1000); //启动计时器，1秒执行一次
			 }
		 });
	});
	
	//timer处理函数
	function SetRemainTime() {
          if (curCount == 0) {                
              window.clearInterval(InterValObj);//停止计时器
              $("#payPasswordSendBtn").removeAttr("disabled");//启用按钮
              $("#payPasswordSendBtn").val("重新发送验证码");
              isSend = false;
          } else {
              curCount--;
              $("#payPasswordSendBtn").val("剩余" + curCount + "秒");
          }
    }
	
	$("#verificationCode").blur(function(){
		var verificationCode = $("#verificationCode").val();
		verificationCode = $.trim(verificationCode);
		
		if( "" == verificationCode){
			return;
		}
		if(!isSend){ // 是否已经发送短信
			return;
		}
		// 异步验证验证码是否正确,显示tip信息
		lm.post("${contextPath }/payaccount/ajax/verifyPayPassWordCode/",{code:verificationCode},function(data){
			 if(data == 1){
				 $("#tipInformation").html("正确");
			 } else {
				 $("#tipInformation").html("错误");
			 }
		 });
	});
	
	$("#sureUpdatePayPasswordBtn").click(function(){
		var verificationCode = $("#verificationCode").val();
		verificationCode = $.trim(verificationCode);
		// 验证码不能为空
		if( null == verificationCode || "" == verificationCode ){
			$("#commonTipInformation").html("验证码不能为空");
			return ;
		}
		var forgetPayPassword = $("#forgetPayPassword").val();
		forgetPayPassword = $.trim(forgetPayPassword);
		// 支付密码不能为空
		if( null == forgetPayPassword || "" == forgetPayPassword ){
			$("#commonTipInformation").html("支付密码不能为空");
			return ;			
		}
		
		var result = isPassword(forgetPayPassword);
		if( result == 0 ){
			$("#commonTipInformation").html("支付密码长度不能小于6位且不能大于18位");
			return ;	
		}
		if( result == 1 ){
			$("#commonTipInformation").html("支付密码只能为英文或者数字");
			return ;
		}
		
		var forgetSurePayPassword = $("#forgetSurePayPassword").val();
		forgetSurePayPassword = $.trim(forgetSurePayPassword);
		// 确认支付密码不能为空
		if( null == forgetSurePayPassword || "" == forgetSurePayPassword ){
			$("#commonTipInformation").html("确认支付密码不能为空");
			return ;
		}
		
		if( forgetPayPassword != forgetSurePayPassword ){
			$("#commonTipInformation").html("两次密码不一致");
			return ;
		}
		if($("#tipInformation").html() !=  "正确"){
			$("#commonTipInformation").html("验证码错误或未发送验证码");
			return ;
		}
		
		// 支付密码不能和登录密码一致
		// 异步验证验证码是否正确,显示tip信息
		lm.post("${contextPath }/payaccount/ajax/sureUpdatePayPasswordCode/",{code:verificationCode,forgetPayPassword:forgetPayPassword},function(data){
			 if(data == 1){
				 $("#commonTipInformation").html("验证码错误或未发送验证码");
					return ;
			 } else if(data == 2) {
				 $("#commonTipInformation").html("支付密码不能和登录密码一致");
					return ;
			 }
			 //$("#commonTipInformation").html("修改成功");
			 $("#sureUpdatePayPasswordBtn").attr("disabled",true);
			 lm.alert("修改成功");
			 $("#close_payPassword").click();
		 });
	});
	
	isPassword = function(fieldValue){
		if( fieldValue.length < 6 || fieldValue.length > 19 ){
			return 0; // 长度限制
		}
		if( !(/([\w]){6,19}$/.test(fieldValue)) ){
			return 1; // 只能输入英文或者数字
		}
	}
});
</script>

<div class="modal fade" id="${modeId}">
	<div class="modal-dialog modal-sm" style="width: 600px;" >
	  <div class="modal-content">
	    <div class="modal-header">
	      <button id = "close_payPassword" type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
	    </div>
			    <div class="modal-body">
		    <form id="payAccountFormSubmit" method='post' class='form-horizontal' action="" >
		    
					<div class="form-group">
						<label class="col-md-3 control-label">用户</label>
						<div class="col-md-6">
							<input type='text' id="userMobile" readonly="readonly"  name="userMobile" value="${user_Mobile }" class='form-control'  onpaste="return  false"  autocomplete="off"/>
						</div>
							&nbsp;&nbsp;&nbsp;&nbsp;<input type='button' id='payPasswordSendBtn' alt="系统将会给此手机号发送短信验证码,请确保手机开通" class='btn btn-small btn-primary' value="发送验证码" />
					</div>
					
					<div class="form-group">
						<label class="col-md-3 control-label">验证码</label>
						<div class="col-md-6">
							<input type='text' maxlength="10" id="verificationCode" name="verificationCode" value="" class='form-control' autocomplete="off"/>
						</div>
							<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
							<label class="col-md-0 control-label" style="color: red;font-size: 15px" id="tipInformation"></label>
					</div>
					
					<div class="form-group">
						<label class="col-md-3 control-label">支付密码</label>
						<div class="col-md-6">
							<input type='password' maxlength="19" id="forgetPayPassword" isRequired="1" tipName="输入支付密码"  fieldType="password" name="forgetPayPassword" value="" class='form-control'  onpaste="return false"  autocomplete="off"/>
						</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
					</div>
					
					<div class="form-group">
						<label class="col-md-3 control-label">确认支付密码</label>
						<div class="col-md-6">
							<input type='password' maxlength="19" id="forgetSurePayPassword" isRequired="1" tipName="确认支付密码"  fieldType="password" name="forgetSurePayPassword" value="" class='form-control'  onpaste="return false"  autocomplete="off"/>
						</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
					</div>
					
					<div class="form-group">
					<div class="col-md-offset-3 col-md-10">
						<input type="button" id='sureUpdatePayPasswordBtn' class='btn btn-primary' value="确认修改" data-loading='稍候...' />
						&nbsp;&nbsp;&nbsp;&nbsp;<label class="col-md-0 control-label" style="color: red;font-size: 15px" id="commonTipInformation"></label>
					</div>
			    </div>
		    
		   </form>
	   </div>
	  </div>
   </div>
</div>