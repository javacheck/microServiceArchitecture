<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加优惠劵</title> 
<script type="text/javascript">

function callback(obj){
	$("#cashGift_storeId").val(obj.name);
	$("#storeId").val(obj.id);
}

$(document).ready(function(){
	$("#cashGift_storeId").click(function (){
		$("#cashGiftShowStore").modal();
	});
	
	var format = "yyyy-MM-dd";//默认格式
	var now = lm.Now;//当前日期
	var nowFormat=now.Format(format);
	$("#validTimeReplace").datetimepicker({
		minView: "month", //选择日期后，不会再跳转去选择时分秒 
		format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
		language: 'zh-CN', //汉化 
		autoclose:true , //选择日期后自动关闭
		startDate:now,
		todayBtn:true,//可选择当天按钮
		todayHighlight:false//高亮当前日期
	 });
		
	$("#amountTipID").html("折扣");
	
	$("input:radio[name='type']").click(function(){
		if($(this).val() == 0 ){
			$("#amountTipID").html("折扣");
		} else {
			$("#amountTipID").html("金额");
		}
	});
	
	$("#mobile").bind("blur", function () {
		var mobile = $("#mobile").val();
		mobile = $.trim(mobile);
		if( mobile == "" || mobile == null ){
			return;
		}
		if(!lm.isMobile(mobile)){
			lm.alert("请输入正确的手机号码");
			return ;
		}
		
		lm.post("${contextPath}/cashGift/list/ajax/checkMobile",{mobile:mobile},function(data){
			if(data==null || data == 'null' || data == ""){
				lm.alert("此手机号不是会员");
				$("#userId").val("");
				return ;
			} else {
				$("#userId").val(data);
			}
		});
	});
	
	$("#cashGiftrebackBtn").click(function(){
		window.location.href="${contextPath }/cashGift/list";		
	});
	
	// 保存商品库存信息
	$("#cashGiftAddBtn").click(function(){
		var mobile = $("#mobile").val(); // 手机号码
		mobile = $.trim(mobile);
		if( mobile == "" || mobile == null ){
			lm.alert("手机号码不能为空!");
			return;
		}
		
		if(!lm.isMobile(mobile)){
			lm.alert("请输入正确的手机号码");
			return ;
		}
		
		lm.postSync("${contextPath}/cashGift/list/ajax/checkMobile",{mobile:mobile},function(data){
			if(data == null || data=='null' || data == ""){
				lm.alert("此手机号不是会员");
				$("#userId").val("");
				return ;
			} else {
				$("#userId").val(data);
			}
		});
		
		if($("#userId").val() == ""){
			lm.alert("此手机号不是会员");
			return ;
		}
		
		var amount = $("#amount").val(); // 折扣/金额
		amount = $.trim(amount);
		if( amount == "" || amount == null ){
			lm.alert($("#amountTipID").html() + "不能为空");
			return ;
		}
		
		if( !(lm.isFloat(amount) && amount > 0) ){
			lm.alert($("#amountTipID").html() + "输入错误！");
			return ;
		}
		
		if( !(/^[0-9]*(\.[0-9]{1,2})?$/.test(amount)) ){
			lm.alert($("#amountTipID").html() + "输入错误！");
			return ;
		}

		if($("#amountTipID").html() == "折扣"){
			if( parseFloat(amount) > 10 ){
				lm.alert("折扣不能大于10");
				return ;
			}
		}
		
		var validTimeReplace = $("#validTimeReplace").val(); // 失效日期
		
		if( validTimeReplace == "" || validTimeReplace == null ){
			lm.alert("请选择失效日期");
			return ;
		}
		
		lm.post("${contextPath}/cashGift/list/ajax/save",$("#cashGiftSaveForm").serialize(),function(data){
			
			if(data==1){
		    	lm.alert("操作成功！");
		    	$("#userId").val("");
		    	$("#mobile").val("");
			}
		}); 
	}); 
	
});

</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>添加优惠劵
			</strong>
		</div>
		<div class='panel-body'>
			<form method='post' class='form-horizontal' repeatSubmit='1' action="" id="cashGiftSaveForm">
			
				<div class="form-group">
					<label class="col-md-1 control-label">商家</label>
					<div class="col-md-2">
						<input type="text" name="storeName" readonly="readonly" class='form-control' id = "cashGift_storeId" value="${cashGift.storeName }">
						<input type="hidden" name="storeId" class='form-control' id="storeId"  value="${cashGift.storeId }">
					</div>
				</div>
								
				<div class="form-group">
					<label class="col-md-1 control-label">手机</label>
					<div class="col-md-2">
						<input type="text" id="mobile" name="mobile" value="${cashGift.mobile }"  class='form-control' maxlength="19"/>
						<input type="hidden" id="userId" name="userId" value="${cashGift.userId }"  class='form-control'/>
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">类型</label>
					<div class="col-md-2">
						<input type="radio" name="type" id="type" checked="checked" value="0"/>&nbsp;&nbsp;折扣劵
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="type" id="type" value="1"/>&nbsp;&nbsp;现金劵
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
								
				<div class="form-group">
					<label class="col-md-1 control-label" id="amountTipID"></label>
					<div class="col-md-2">
						<input type='text' id="amount" name="amount" placeholder="仅保留两位小数" class='form-control' value="<fmt:formatNumber value="${cashGift.amount }" type="currency" pattern="0.00"/>" maxlength="7"/>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">失效日期</label>
					<div class="col-md-2">
						<input  readonly="readonly" type="text" id="validTimeReplace" name="validTimeReplace" value="<fmt:formatDate value="${cashGift.validTime }" pattern="yyyy-MM-dd"/>"  class='form-control' />
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">说明</label>
					<div class="col-md-2">
						<textarea id="memo"  name="memo" value="${cashGift.memo }" cols=15 rows=5  class='form-control' maxlength="150">${cashGift.memo }</textarea>
					</div>
				</div>
				
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button"  id='cashGiftAddBtn' class='btn btn-primary' repeatSubmit='1' value="添加" data-loading='稍候...' />
						<input type="button"  id='cashGiftrebackBtn' class='btn btn-primary' value="返回列表页面" />
					</div>
				</div>
				</form>
	  </div>
   </div>
   <!-- 店铺弹窗标签 -->
	<m:select_store path="${contextPath}/shop/showModle/list/list-data" modeId="cashGiftShowStore" callback="callback"> </m:select_store>
</body>
</html>