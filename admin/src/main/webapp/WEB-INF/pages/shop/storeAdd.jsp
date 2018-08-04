<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty store ? '添加' : '修改' }商家</title> 
<script src="${contextPath}/static/js/uploadPreview.js" type="text/javascript"></script>
<style type="text/css">
input.file{
    vertical-align:middle;
    position:relative;
    left:-218px;
    filter:alpha(opacity=0);
    opacity:0;
	z-index:1;
	*width:223px;
}
</style>
<script type="text/javascript">
$(document).ready(function(){
	new uploadPreview({
		UpBtn : "logoFile",
		DivShow : "logoImagediv",
		ImgShow : "logoImage"		
	});
		
	if( null != $("#id").val() && "" != $("#id").val() ){ // 修改
		
		var posAdminPassword = $("#posAdminPassword");
		posAdminPassword.val("");
		posAdminPassword.attr("placeholder","不修改则为原密码");
		$("#unifiedPointRule").val('${store.unifiedPointRule}');
		// 送货上门方式
		var shipType = $("#shipTypeCache").val();
		var shipTypeArray = shipType.split(",");
		$("input:checkBox[name='shipTypeCheckBox']").each(function(key,value){
			for (var i = 0; i < shipTypeArray.length; i++) {
				if( shipTypeArray[i] == $(value).val() ){
					$(value).attr("checked",true);
					return ; // 匹配到了就不找了
				}
			}
		});
								
		// 展示已经添加了的商家LOLO
		var logo = $("#logo");
		if( undefined != logo && null != logo.val() && "" != logo.val() ){
			$("#logoImagediv").show();
			$("#logoImage").attr("src","${picUrl }"+logo.val() );
		}
		
		var posCartAuthority="${store.posCartAuthority}";
		 if(posCartAuthority=="1"){
			 $("#posCartAuthority_1").eq(1).prop("checked",true);
		 }
		 var receiptReprint="${store.receiptReprint}";
		 if(receiptReprint=="0"){
			 $("#posCartAuthority_0").eq(0).prop("checked",true);
		 }
		 var receiptPrintAmount="${store.receiptPrintAmount}";
		 if(receiptPrintAmount=="1"){
			 $("#receiptPrintAmount_0").eq(1).prop("checked",true);
		 }
	}
	
	   $("[name = logoFile]").bind("change", function () {
		   var fileName = $("#logoFile").val();
		   if( "" == fileName ){
			   $("#logoFileCache").val("");
			   $("#logoImagediv").hide();
		   } else {
			   $("#logoFileCache").val(fileName);
			   $("#logoImagediv").show();
		   }
	  });
	  
	  $("[name = upLogo]").bind("click", function () {
		   $("[name = logoFile]").click();
	  });
});

$(document).ready(function(){
	// 商家保存商家信息
	$("#byStoreAddBtn").click(function(){	
		 var description = $("#description").val();
		 description = $.trim(description);
		 if( "" == description || null == description ){
			 lm.alert("商家简介不能为空");
			 return ;
		 }
		 
		 var startBusinessString = $("#startBusinessString").val();
		 startBusinessString = $.trim(startBusinessString);
		 if( "" == startBusinessString || null == startBusinessString ){
			 lm.alert("配送时间不能为空");
			 return ;
		 }
		 
		 var endBusinessString = $("#endBusinessString").val();
		 endBusinessString = $.trim(endBusinessString);
		 if( "" == endBusinessString || null == endBusinessString ){
			 lm.alert("配送时间不能为空");
			 return ;
		 }
		 
		 var minAmount = $("#minAmount").val(); // 起送金额
		 minAmount = $.trim(minAmount);
		 if( "" != minAmount && null != minAmount ){
			 if(!(lm.isFloat(minAmount) && minAmount >= 0)){
				 lm.alert("起送金额输入错误");
				return; 
			 }
		 }
		 
		 var shipRange = $("#shipRange").val(); // 配送范围
		 shipRange = $.trim(shipRange);
		 if( "" != shipRange && null != shipRange ){
			 if(!(lm.isFloat(shipRange) && shipRange >= 0)){
				 lm.alert("配送范围输入错误");
				return; 
			 }
		 }
		 
		 var shipAmount = $("#shipAmount").val(); // 送货费用为空或者为0，则不再判断免费配送限定额
		 shipAmount = $.trim(shipAmount);
		 if( "" != shipAmount && null != shipAmount ){
			 if(!(lm.isFloat(shipAmount) && shipAmount >= 0)){
				 lm.alert("送货费用输入错误");
				return; 
			 }
		 }
		 
		 var freeShipAmount = $("#freeShipAmount").val(); //免费配送限定额
		 freeShipAmount = $.trim(freeShipAmount);
		 if( "" != freeShipAmount && null != freeShipAmount ){
			 if(!(lm.isFloat(freeShipAmount) && freeShipAmount >= 0)){
				 lm.alert("免配送费限定额输入错误");
				return; 
			 }
		 }
		 
		 if( "" != shipAmount && shipAmount > 0){
			 var minAmount = $("#minAmount").val(); // 起送金额
			 minAmount = $.trim(minAmount);
			 if( "" != minAmount && minAmount > 0){
				 if( parseFloat(freeShipAmount) < parseFloat(minAmount) ){
					 lm.alert("免配送费限定额不能小于起送金额");
					 return false;
				 }
			 } 
		 } else {
			 $("#freeShipAmount").val(""); // 送货费用为空时，设置免配送费限定额为空
		 }
			 
		 var str="";
         $("input[name='shipTypeCheckBox']").each(function(){ 
             if(this.checked){
                 str += $(this).val()+","
             }
         });
         if( "" == str ){
        	 lm.alert("请选择至少一种送货方式");
        	 return false;
         }
         $("#shipType").val(str.substring(0,str.length-1));
         
         var phone = $("#phone").val(); // 电话
         phone = $.trim(phone);
		 if( "" != phone && null != phone ){
			 if( (!(/^(\d{3,4}-)\d{7,8}$/.test(phone)) && (!(/^\+?[1-9][0-9]*$/.test(phone)))) ){
				 lm.alert("电话输入错误");
				return; 
			 }
		 }
		 
		 var posAdminPassword = $("#posAdminPassword").val();
		 posAdminPassword = $.trim(posAdminPassword);
			if( posAdminPassword == null || "" == posAdminPassword ){
				var id = $("#id").val();
				if(null == id || "" == id){
					lm.alert("授权密码不能为空");
					return ;				
				}
			} else {
				if( posAdminPassword.length < 6 || posAdminPassword.length > 19 ){
					lm.alert("授权密码长度不能小于6位且不能大于18位");
					return ; // 长度限制
				}
				if( !(/([\w]){6,19}$/.test(posAdminPassword)) ){
					lm.alert("授权密码只能为英文或者数字！");
					return ; // 只能输入英文或者数字
				}
			}
		 $("#byShopSignFormSubmit").submit();
	}); 
});

function imageLoad(obj){
	$("#logoImagediv").show();
	$("#divLogo").removeClass("col-md-2");
	
	if(obj.offsetHeight != 220 || obj.offsetWidth != 330 ){
		obj.src="";
		lm.alert("图片规格只能是330x220像素");
		$("#logoImagediv").hide();
		$("#logoFileCache").val("");
	} 
	$("#divLogo").addClass("col-md-2");
}

//初始化事件
$(function() {
	var format = "yyyy-MM-dd";//默认格式
	var now = lm.Now;//当前日期
	var nowFormat=now.Format(format);
	$("#startBusinessString").datetimepicker({
		  minView:0,
		  maxView:1,
		  format: 'hh:ii',
		  language: 'zh-CN', //汉化 
		  autoclose:true , //选择日期后自动关闭
          startView:1
	 });
	$("#endBusinessString").datetimepicker({
		  minView:0,
		  maxView:1,
		  format: 'hh:ii',
		  language: 'zh-CN', //汉化 
		  autoclose:true , //选择日期后自动关闭
          startView:1
	 });
	
});//初始化事件结束
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>${empty store ? '添加' : '修改' }商家
			</strong>
		</div>
		<div class='panel-body'>
			<form id="byShopSignFormSubmit" method='post' class='form-horizontal' autocomplete="off" action="${contextPath }/shop/storeSave" enctype="multipart/form-data" >
			
				<!-- 修改时传过来的商家ID -->
				<input id="id" name="id" type="hidden" value="${store.id }" />
				<input id="myStore" name="myStore" type="hidden" value="${myStore }" />
				<input id="shipTypeCache" name="shipTypeCache" type="hidden" value="${store.shipType }" />

				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商家名称</label>
					<div class="col-md-2">
						<input type="text" id="name" readonly="readonly" name="name" value="${store.name }" class='form-control' maxlength="50"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商家简介</label>
					<div class="col-md-2">
						<textarea id="description"  name="description" value="${store.description }" cols=15 rows=5  class='form-control' maxlength="150">${store.description }</textarea>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">总部统一积分规则</label>
					<div class='col-md-1'>
    					<select name="unifiedPointRule" id="unifiedPointRule" class="form-control" >
    							<option value="0" selected="selected">同意使用总部积分规则</option>
    							<option value="1">不使用总部积分规则</option>
    					</select>
    				</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">购物车商品删除要权限</label>
					<div class="col-md-2" style="margin-top: 3px;">
							<input id="posCartAuthority_1"  type='radio' value="1" name="posCartAuthority" <c:if test="${store.posCartAuthority == 1 }">checked="checked"</c:if>/>是&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input id="posCartAuthority_0"  type='radio' value="0" name="posCartAuthority" <c:if test="${store.posCartAuthority == 0 }">checked="checked"</c:if> />否
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label">小票重打印</label>
					<div class="col-md-2" style="margin-top: 3px;">
							<input id="receiptReprint_1"  type='radio' value="1" name="receiptReprint" <c:if test="${store.receiptReprint == 1 }">checked="checked"</c:if>/>开启&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input id="receiptReprint_0"  type='radio' value="0" name="receiptReprint" <c:if test="${store.receiptReprint == 0 }">checked="checked"</c:if> />关闭
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label">小票打印数</label>
					<div class="col-md-2" style="margin-top: 3px;">
							<input id="receiptPrintAmount_0"  type='radio' value="1" name="receiptPrintAmount" <c:if test="${store.receiptPrintAmount == 1 }">checked="checked"</c:if> />1联&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input id="receiptPrintAmount_1"  type='radio' value="2" name="receiptPrintAmount" <c:if test="${store.receiptPrintAmount == 2 }">checked="checked"</c:if>/>2联
					</div>
				</div>	
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>配送时间</label>
						<div class="col-md-1">
							<input type='text' readonly="readonly" id="startBusinessString"  name="startBusinessString" class='form-control' value="<fmt:formatDate value="${store.startBusinessDate }" pattern="HH:mm"/>"/>
						</div>
						<label class="" style="float: left;padding-top: 6px">至</label>
						<div class="col-md-1">
							<input type='text' readonly="readonly" id="endBusinessString"  name="endBusinessString" class='form-control' value="<fmt:formatDate value="${store.endBusinessDate }" pattern="HH:mm"/>"/>
						</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">起送金额( 元 )</label>
					<div class="col-md-2">
						<input type='text' id="minAmount"  name="minAmount" class='form-control' value="<fmt:formatNumber value="${store.minAmount }" type="currency" pattern="0.00"/>" maxlength="7"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">配送范围( 公里 )</label>
					<div class="col-md-2">
						<input type='text' id="shipRange"  name="shipRange" class='form-control' value="${store.shipRange }" maxlength="50"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">送货费用( 元 )</label>
					<div class="col-md-2">
						<input type='text' id="shipAmount"  name="shipAmount" class='form-control' value="<fmt:formatNumber value="${store.shipAmount }" type="currency" pattern="0.00"/>" maxlength="7"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">免配送费限定额( 元 )</label>
					<div class="col-md-2">
						<input type='text' id="freeShipAmount"  name="freeShipAmount" class='form-control' value="<fmt:formatNumber value="${store.freeShipAmount }" type="currency" pattern="0.00"/>" maxlength="7"/>
					</div>
				</div>
				
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>送货方式</label>
					<div class="col-md-2">
						<input type="checkbox" name="shipTypeCheckBox" value="0"/>&nbsp;&nbsp;店铺送货上门&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="checkbox" name="shipTypeCheckBox" value="1"/>&nbsp;&nbsp;到店自提&nbsp;&nbsp;&nbsp;&nbsp;
						<br>
						<input type="checkbox" name="shipTypeCheckBox" value="2"/>&nbsp;&nbsp;快递&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="checkbox" name="shipTypeCheckBox" value="3"/>&nbsp;&nbsp;第三方配送&nbsp;&nbsp;&nbsp;&nbsp;
						<input type='hidden' id="shipType" name="shipType" class='form-control' value=""/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">送达时间</label>
					<div class="col-md-2">
						<input type='text' id="shipTime"  name="shipTime" class='form-control' value="${store.shipTime }" maxlength="50"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">联系电话</label>
					<div class="col-md-2">
						<input type='text' id="phone"  name="phone" class='form-control' value="${store.phone }" maxlength="15"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>授权密码</label>
					<div class="col-md-2">
						<input type="password" style="display:none"> <!-- 为了屏蔽掉浏览器记住密码后自动填充到下面的密码框中,加入这个隐藏域 -->
						<input type="password" style="display:none">
						<input type="password" name="posAdminPassword" class='form-control' id="posAdminPassword" value="${store.posAdminPassword }" maxlength="20"  >
					</div>
				</div>
								
				<div class="form-group">
					<label class="col-md-1 control-label">商家LOGO</label>
					<div id="divLogo" class="col-md-2">
						<input type="button" id="upLogo" class="btn btn-large btn-block btn-default"  name="upLogo" value="上传商家LOGO"/>&nbsp;&nbsp;
						<input type="file" id="logoFile" style="width: 1px;height:1px"name="logoFile" class="file"  value="上传图片" title="上传图片"/>
						<input type='hidden' id="logo" name="logo" class='form-control' value="${store.logo }"/>
						<input type="hidden" id="logoFileCache" name="logoFileCache" value="${store.logo }"/>
						<div id="logoImagediv">
							<img alt="商家LOGO" style="cursor: pointer;" id="logoImage" name="logoImage" src="" onload="imageLoad(this);">
						</div>
					</div>
				</div>

				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button"  id='byStoreAddBtn' class='btn btn-primary' value="${empty store ? '添加' : '修改' }" data-loading='稍候...' />
					</div>
				</div>
			</form>
	  </div>
   </div>
 
</body>
</html>