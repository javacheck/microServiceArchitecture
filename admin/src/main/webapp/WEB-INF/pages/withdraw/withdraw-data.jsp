<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>提现</title>
<script type="text/javascript">
	$(function(){
		if( "" == $("#bankAccountNumber").val()){
			lm.alert("对不起,请先绑定银行卡");
			$("#priceSubmitBtn").attr("disabled",true);
		}
		
		if( undefined == $("#passwordShow") || "" == $("#passwordShow").val() ){
			$("#warningLabel").html("对不起,为了您的支付安全,请先设置支付密码");
			$("#priceSubmitBtn").attr("disabled",true);
		}
		
		if( "" == $("#balanceShow").val() || parseFloat($("#balanceShow").val()) <= 0 ){
			$("#balanceShow").val("0.0");
			lm.alert("对不起,您的余额不足,请先充值");
			$("#priceSubmitBtn").attr("disabled",true);
		}
		
		$("#priceSubmitBtn").click(function(){
			if( undefined == $("#passwordShow") || "" == $("#passwordShow").val() ){
				lm.alert("对不起,为了您的支付安全,请先设置支付密码");
				$("#priceSubmitBtn").attr("disabled",true);
				return false;
			}
			
			var amount = $("#amount").val();
			if( "" == amount ){
				lm.alert("提取金额不能为空!");
				$("#amount").focus();
				return false;
			}
			/*
				if( !(lm.isFloat(amount) && amount > 0) ){
					lm.alert("提取金额输入错误！");
					$("#amount").val("");
					$("#amount").focus();
					return false;
				}
			*/
			if(!(/^(([1-9]\d{0,9})|0)(\.\d{1,2})?$/).test(amount)){
				lm.alert("提取金额输入错误！");
				$("#amount").focus();
				return false;
			}
			
			//if( (lm.isFloat(amount) && amount > 0) ){
				if( parseFloat(amount) < 200){
					lm.alert("单次提取金额不能小于200元！");
					$("#amount").focus();
					return false;
				}
			//}
			var balanceShow = $("#balanceShow").val();
			
			if( parseFloat(amount) > parseFloat(balanceShow) ){
				lm.alert("提取金额不能大于您的余额！");
				$("#amount").focus();
				return false;
			}
			
			var payPassword = $("#payPassword").val();
			payPassword = $.trim(payPassword); // 用jQuery的trim方法删除前后空格
			var isCheck = false;
			if( null != payPassword && "" != payPassword ){
				lm.postSync("${contextPath }/payaccount/checkPassword/", {originalPassword:payPassword}, function(data) {
					var data = data.split("_");
					if (data[0] == 0) { // 不存在或者密码不匹配
						if( undefined == data[1] || "0" == data[1] ){
							$("#countNumber").css("width",280).html("您输入支付密码错误次数超过5次,今日已锁定");
							$("#priceSubmitBtn").attr("disabled",true);
						} else {
							lm.alert("支付密码错误,请重新输入!");
							$("#payPassword").val("");
							$("#payPassword").focus();
							$("#countTime").html(data[1]);
						}
						$("#countNumber").show();
						isCheck = true;
						return false;
					} 
				});
			} else {
				lm.alert("支付密码不能为空!");
				$("#payPassword").focus();
				return false;
			}
			if(isCheck){
				return false;
			}
			
			$("#withdrawForm").submit();
		});
		
		$("#id").change(function(){
			var id = $("#id option:selected").val();
			lm.post("${contextPath}/withdraw/list/ajax/getBankInformation",{id:id},function(data){
				if(null != data && "" != data){
					$("#bankAccountNumber").val(data.accountNumber);
					$("#bankAccountName").val(data.accountName);
				}
			});
		});
		
		  $("#payPassword").keypress(function (event) {
			  var e = event || window.event,
              $tip =
	          $('#capslock'),
	                  kc = e.keyCode || e.which, // 按键的keyCode
	                  isShift = e.shiftKey || (kc == 16 ) || false; // shift键是否按住
	          if (((kc >= 65 && kc <= 90) && !isShift) || ((kc >= 97 && kc <= 122) && isShift)) {
	               $tip.show();
	          } else {
	               $tip.hide();
	          }
          });
		  
		  $("#payPassword").blur(function () {
			  $('#capslock').hide();
          });
	});
	
	function switchEle(obj){
	    if(window.event.keyCode == 9){
	    	if(obj == 1){
	    		$("#id").focus();
	    	} else {
		        $("#bankAccountName").focus();	    		
	    	}
	    }
	}
	
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
#countNumber {
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
</style>
</head>
<body>
	<m:forgetPayPassword modeId="openPayPasswordModel"></m:forgetPayPassword>
	<div class='panel'>
		<div class='panel-heading'>
			<strong><i class='icon-plust'></i>提现</strong>
		</div>
		<div class='panel-body'>
			<form id="withdrawForm" method='post' class='form-horizontal' action="${contextPath }/withdraw/saveWithdraw" >
				
				<div class="form-group">
					<label class="col-md-1 control-label">冻结金额为：</label>
					<div class="col-md-2">
						<input type='text' readonly="readonly" maxlength="15" onfocus="return false;" onkeyup="switchEle(1);" onmousedown="return false;"  id="frozenAmount" name="frozenAmount" value="${payAccount.frozenAmountString }" class='form-control' />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">您的可提现金额为：</label>
					<div class="col-md-2">
						<input type='text' readonly="readonly" maxlength="15" onfocus="return false;" onkeyup="switchEle(1);" onmousedown="return false;"  id="balanceShow" name="balanceShow" value="${payAccount.balanceString }" class='form-control' />
						<input type='hidden' readonly="readonly" maxlength="5"  id="passwordShow" name="passwordShow" value="${payAccount.password }" class='form-control' />
					</div>
				</div>

				<div class="form-group">
					<label class="col-md-1 control-label">提现银行</label>
					<div class="col-md-2">
						<select name="id" class="form-control" onkeydown="switchEle(2);"  id="id" >
		            		<c:forEach items="${businessBankList }" var="businessBank" >
								<option value ="${businessBank.id}">${businessBank.bankName}</option>
							</c:forEach>
		            	</select>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">提取银行卡号：</label>
					<div class="col-md-2">
						<input type='text' readonly="readonly" onmousedown="return false;" maxlength="15" id="bankAccountNumber" name="bankAccountNumber"  value="${accountNumber }" class='form-control' />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">提取银行用户名：</label>
					<div class="col-md-2">
						<input type='text' readonly="readonly" onmousedown="return false;" maxlength="15" id="bankAccountName" name="bankAccountName"  value="${accountName }" class='form-control' />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">提取金额为：</label>
					<div class="col-md-2">
						<input type='text' maxlength="15" id="amount" name="amount" placeholder="单次提取金额不能小于200元" value="" class='form-control' />
					</div>
				</div>
				
				<div class="form-group">
					<div id="capslock"><i></i><s></s>键盘大写锁定已打开，请注意大小写</div>
					<div id="countNumber">为了您的安全，您今日还剩下<span id="countTime"></span>次机会</div>
					<label class="col-md-1 control-label">支付密码：</label>
					<a href='#openPayPasswordModel' data-toggle='modal'>忘记支付密码？猛戳这里</a>
					<div class="col-md-2">
						<input type="password" style="display:none"> <!-- 为了屏蔽掉浏览器记住密码后自动填充到下面的密码框中,加入这个隐藏域 -->
						<input type="password" style="display:none">
						<input type="password" maxlength="19" id="payPassword" name="payPassword"   value="" class='form-control' onpaste="return  false" autocomplete="off"/>
					</div>
				</div>
				
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type='button' id='priceSubmitBtn' class='btn btn-small btn-primary' value="确认提现" />
						<label class="col-md-0 control-label" id="warningLabel" style="color: red;font-size: 15px" ></label>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>