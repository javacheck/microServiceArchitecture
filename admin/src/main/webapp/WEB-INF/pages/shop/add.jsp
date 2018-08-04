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
<script src="${contextPath}/static/js/checkSubmitParameters.js" type="text/javascript"></script>
<script type="text/javascript" src="${contextPath}/static/js/ZeroClipboard/ZeroClipboard.js"></script>

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
<style type="text/css">
			body { font-family:arial,sans-serif; font-size:9pt; }
			#clip_button { width:250px; text-align:center; border:1px solid black; background-color:#ccc; margin:0px 0px; padding:5px 0px; cursor:default; font-size:9pt; }
			#clip_button.hover { background-color:#eee; }
			#clip_button.active { background-color:#aaa; }
		</style>
		
<script type="text/javascript">
var countNum = 1;
$(document).ready(function(){
	new uploadPreview({
		UpBtn : "logoFile",
		DivShow : "logoImagediv",
		ImgShow : "logoImage"		
	});
	
	//keyupParameters("shopSignFormSubmit");
	
	blurParameters("shopSignFormSubmit");
		
	var isUpdate = false;
	if( null != $("#id").val() && "" != $("#id").val() ){ // 修改
		isUpdate = true;
		$("#unifiedPointRule").val('${store.unifiedPointRule}');
		var posAdminPassword = $("#posAdminPassword");
		posAdminPassword.val("");
		posAdminPassword.attr("placeholder","不修改则为原密码");
	
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
		
		$("#mobile").change(function(){
			var mobileCache = $("#mobileCache").val();
			if(mobileCache != $(this).val() ){
				$("#mobile").parent().next().next().html("温馨提示：修改手机号码会将账号也同步进行修改");												
			} else {
				$("#mobile").parent().next().next().html("");
			}
		});
		
		// 修改时判断交易类型是否是两种交易类型均可
		var payTypeValue =  $("#payType").val();
		if( payTypeValue == "2" ){
			$("[name = payTypeCheckBox]:checkbox").attr("checked",true);
		} else {
			
			$("[name = payTypeCheckBox]:checkbox").each(function(key,value){
				if( payTypeValue == $(value).val() ){
					$(value).attr("checked",true);
					return ; // 匹配到了就不找了
				}
			});
		}
		
		// 修改时判断营业状态
		$("input:radio[name='status']").each(function(key,value){
			 if( $("#statusCache").val() == $(value).val() ){
				 $(value).attr("checked",true);
				 return false;
			 }			
		});
		
		// 展示已经添加了的商家LOLO
		var logo = $("#logo");
		if( undefined != logo && null != logo.val() && "" != logo.val() ){
			$("#logoImagediv").show();
			$("#logoImage").attr("src","${picUrl }"+logo.val() );
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
	   
	// 保存商家信息(每个填写的input框均需要isRequired字段，0为非必填字段。1为必填字段)
	$("#storeAddBtn").click(function(){	
		checkParameters("shopSignFormSubmit"); 
	}); 
	$("a[name='terminalIdAdd']").removeAttr('href'); // 让A标签失效

	$("a[name='terminalIdAdd']").click(function(){
		var element = $("#cacheTerminalIdDiv");
		 element.append("<div class='form-group' id='"+countNum+"'><label class='col-md-1 control-label'>终端号</label> <div class='col-md-2'><input type='text' id='terminalId"+ countNum+"' onblur='checkTerminalId(this);'  name='terminalIdArray' isRequired='0' tipName='终端号' class='form-control' maxlength='30'/></div> <a onclick='terminalIdDelete(this);' id='deletebutton"+ countNum+"' name='deletebutton' class='btn btn-small btn-danger'>删除</a></div>");
		 countNum++ ;
	});
	 // 唯一性验证
	checkUniqueness = function(obj){
		 var value = $(obj).attr("name");
		 if( value == "name" ){
			 var ok = true;
			 if(isUpdate){
				var nameCache = $("#nameCache").val();
				if( nameCache == $(obj).val() ){
					ok = false;
				}
			 } 
			 if(ok){
				 lm.postSync("${contextPath }/shop/checkName/", {name:$(obj).val()}, function(data) {
					if (data == 1) {
						lm.alert($(obj).attr("tipName") + "存在重复！");
						$(obj).focus();
						ok = false;
					} 
				});
				 if(!ok){
					 return false;
				 }				 
			 }
		 } 
		 /* else if( value == "companyName" ){
			 var ok = true;
			 if(isUpdate){
				var companyNameCache = $("#companyNameCache").val();
				if( companyNameCache == $(obj).val() ){
					ok = false;
				}
			}
			 if(ok){
				 lm.postSync("${contextPath }/shop/checkCompanyName/"+ $(obj).val(), {}, function(data) {
					if (data == 1) {
						lm.alert($(obj).attr("tipName") + "存在重复！");
						$(obj).focus();
						ok = false;
					} 
				});
				 if(!ok){
					 return false;
				 } 
			}
		 } */
		 else if( value == "merchantNo" ){
			 var ok = true;
			 if(isUpdate){
				var merchantNoCache = $("#merchantNoCache").val();
				if( merchantNoCache == $(obj).val() ){
					ok = false;
				}
			} 
			 if(ok){
				 lm.postSync("${contextPath }/shop/checkMerchantNo/", {merchantNo:$(obj).val()}, function(data) {
					if (data == 1) {
						lm.alert($(obj).attr("tipName") + "存在重复！");
						$(obj).focus();
						ok = false;
					} 
				});
				 if(!ok){
					 return false;
				 } 
			 }
		 } else if( value == "merchantName" ){
			 var ok = true;
			 if(isUpdate){
				var merchantNameCache = $("#merchantNameCache").val();
				if( merchantNameCache == $(obj).val() ){
					ok = false;
				}
			 }
			 if(ok){
				 lm.postSync("${contextPath }/shop/checkMerchantName/", {merchantName:$(obj).val()}, function(data) {
					if (data == 1) {
						lm.alert($(obj).attr("tipName") + "存在重复！");
						$(obj).focus();
						ok = false;
					} 
				});
				 if(!ok){
					 return false;
				 } 
			 }
		 } else if( value == "mobile" ){
			 var ok = true;
			 	if(isUpdate){
					var mobileCache = $("#mobileCache").val();
					if( mobileCache == $(obj).val() ){
						ok = false;
					}
				 }
				 if(ok){
					 lm.postSync("${contextPath }/shop/checkmobile/", {mobile:$(obj).val()}, function(data) {
						if (data == 1) {
							lm.alert($(obj).attr("tipName") + "存在重复！");
							$(obj).focus();
							ok = false;
						} 
					});
					 if(!ok){
						 return false;
					 } 
				 }
		 } else if( value == "freeShipAmount"){
			 var shipAmount = $("#shipAmount").val(); // 送货费用为空或者为0，则不再判断免费配送限定额
			 shipAmount = $.trim(shipAmount);
			 if( "" != shipAmount && shipAmount > 0){
				 var minAmount = $("#minAmount").val(); // 起送金额
				 minAmount = $.trim(minAmount);
				 if( "" != minAmount && minAmount > 0){
					 if( parseFloat($(obj).val()) < parseFloat(minAmount) ){
						 lm.alert("免配送费限定额不能小于起送金额");
						 return false;
					 }
				 } 
			 } else {
				 if($(obj).val() > 0 ){
					 lm.alert("送货费用为空时，免配送费限定额也必须为空");
					 $(obj).val(""); // 送货费用为空时，设置免配送费限定额为空
				 }
			 }
		 } else if( value == "posAdminPassword" ){
			 var posAdminPassword = $("#posAdminPassword").val();
			 posAdminPassword = $.trim(posAdminPassword);
			 if( posAdminPassword == null || "" == posAdminPassword ){
				 var id = $("#id").val();
					if(null == id || "" == id){
						lm.alert("授权密码不能为空");
						return ;				
					}
				}
		 }
		 return true;
	 }	
	 var array1 = '${terminalIdList }'.split(",");
	 var ss = new Array();
	for (var z = 0; z < array1.length; z++) {
		if( "" != array1[z]){
			ss.push(array1[z]);
		}
	}
	 
	extraFunction = function(){
		 var payTypeCount = 0;
		 // 检测交易类型
		 $("[name = payTypeCheckBox]:checkbox").each(function () {
			 if(this.checked){
				 $("#payType").val($(this).val());
				 payTypeCount++;
             }
		  });
		 
		 if( payTypeCount == 0 ){
        	 lm.alert("请选择至少一种支付类型");
        	 return false;
         }
		 if( payTypeCount == 2 ){
		     $("#payType").val(2);
		 }
		 
		 // 2015-11-17 当POS商户号和终端号不为空时，则下面的收单开户行、收单银行账户、收单账户名、收单银行行号 设置为必填
		 var merchantNo = $("#merchantNo").val();
		 merchantNo = $.trim(merchantNo);
		 
		var notNullSign = "";
		var ok = true;
		$("input[name='terminalIdArray']").each(function(key,value){
			var terValue = $.trim($(value).val());
			if( "" == terValue ){
				return ;//实现continue功能
			}
			notNullSign = terValue;
			if(-1 != $.inArray(terValue,ss)){
				return ;
			}
			lm.postSync("${contextPath }/shop/checkTerminalId/", {terValue:terValue}, function(data) {
				if (data == 1) {
					lm.alert("终端号" + terValue + "已经存在");
					$(obj).focus();
					ok = false;
					return false;
				} 
			});
		});
		
		if( "" != notNullSign ){
			if( null == merchantNo || "" == merchantNo ){
				lm.alert("POS商户号不能为空");
				return;
			}
		}
		
		if( null != merchantNo && "" != merchantNo ){
			if( "" == notNullSign ){
				lm.alert("终端号不能为空");
				return;
			}
		}
		
		if( (null != merchantNo && "" != merchantNo) 
				|| (null != notNullSign && "" != notNullSign ) ){
			 var posBankDeposit = $("#posBankDeposit").val();
			 posBankDeposit = $.trim(posBankDeposit);
			 if( null == posBankDeposit || "" == posBankDeposit ){
				 lm.alert("收单开户行不能为空");
				 return;
			 }
			 
			 var posBankAccount = $("#posBankAccount").val();
			 posBankAccount = $.trim(posBankAccount);
			 if( null == posBankAccount || "" == posBankAccount ){
				 lm.alert("收单银行账户不能为空");
				 return;
			 }
			 
			 var posAccountName = $("#posAccountName").val();
			 posAccountName = $.trim(posAccountName);
			 if( null == posAccountName || "" == posAccountName ){
				 lm.alert("收单账户名不能为空");
				 return;
			 }
			 
			 var posBankLine = $("#posBankLine").val();
			 posBankLine = $.trim(posBankLine);
			 if( null == posBankLine || "" == posBankLine ){
				 lm.alert("收单银行行号不能为空");
				 return;
			 }
		 } 
		
		if(ok){
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
	         return true;
		}
	};
});

function terminalIdDelete(obj){
	 $(obj).parent().remove();
}

function checkTerminalId(obj){
	var terminalIdValue = $(obj).val();
	terminalIdValue = $.trim(terminalIdValue);
	if("" == terminalIdValue){
		return ;
	}
	var ids = $(obj).attr("id");
	$("input[name='terminalIdArray'][id != "+ids+"]").each(function(key,value){
		var terValue = $.trim($(value).val());
		if( "" == terValue ){
			return ;//实现continue功能
		}
		if(terminalIdValue == terValue){
			lm.alert("您填写的终端号中存在重复");
			$(obj).val("");
			return false;
		}
	});
}

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
$(function() {
	if( null != $("#id").val() && "" != $("#id").val() ){ // 修改
		var store_onLineRateType = '${store.onLineRateType}';
		$("#onLineRateType").val(store_onLineRateType);
		var onLineRateType = $("#onLineRate");
			
		if( store_onLineRateType == 0 ){
			onLineRateType.attr("fieldType","pRate"); // 按比率计算([0-100])
		} else {
			onLineRateType.attr("fieldType","pDouble"); // 按金额计算(只能输入两位小数的正实数)
		}
	}
	
	$("#onLineRateType").change(function(){
		var onLineRate = $("#onLineRate");
		if( $(this).val() == 0 ){ // 按比例来计算
			onLineRate.attr("fieldType","pRate"); // 按比率计算([0-100])
		} else { // 按固定金额来计算
			onLineRate.attr("fieldType","pDouble"); // 按金额计算(只能输入两位小数的正实数)
		}
	});
	
});

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

$(function(){
	if( null != $("#id").val() && "" != $("#id").val() ){ // 修改
		$("#authURLDIV").css('display','block');
	}
});
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
			<form id="shopSignFormSubmit" method='post' repeatSubmit='1' class='form-horizontal' autocomplete="off" action="${contextPath }/shop/save" enctype="multipart/form-data" >
			
				<!-- 修改时传过来的商家ID -->
				<input id="id" name="id" type="hidden" value="${store.id }" />
				<input id="statusCache" name="statusCache" type="hidden" value="${store.status }" />
				<input id="shipTypeCache" name="shipTypeCache" type="hidden" value="${store.shipType }" />
				
				<!-- 用于验证唯一性的参数 -->
				<input id="nameCache" name="nameCache" type="hidden" value="${store.name }" />
				<input id="companyNameCache" name="companyNameCache" type="hidden" value="${store.companyName }" />
				<input id="merchantNoCache" name="merchantNoCache" type="hidden" value="${store.merchantNo }" />
				<input id="merchantNameCache" name="merchantNameCache" type="hidden" value="${store.merchantName }" />
				<input id="mobileCache" name="mobileCache" type="hidden" value="${store.mobile }" />

				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商家名称</label>
					<div class="col-md-2">
						<input type="text" id="name"  name="name" isRequired="1" tipName="商家名称" value="${store.name }" class='form-control' maxlength="50"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商家简介</label>
					<div class="col-md-2">
						<textarea id="description"  name="description" isRequired="1" tipName="商家简介" value="${store.description }" cols=15 rows=5  class='form-control' maxlength="150">${store.description }</textarea>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>公司名称</label>
					<div class="col-md-2">
						<input type="text" id="companyName"  name="companyName" isRequired="1" tipName="公司名称" value="${store.companyName }" class='form-control' maxlength="50"/>
					</div>
				</div>
						
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商家类型</label>
					<div class="col-md-2">
						<input name="shopTypeName"  class="form-control" readonly="readonly" value="${store.shopTypeName }" isRequired="1" tipName="商家类型" ignoreNull id="shopTypeName" data-remote="${contextPath }/shop/shopTypeList" data-toggle="modal" />
						<input type="hidden" name="shopTypeId" id="shopTypeId" value="${store.shopTypeId }"  />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>支付类型</label>
					<div class="col-md-2">
						<input type="hidden" name="payType" id="payType" value="${store.payType }"/>
						<input type="checkbox" name="payTypeCheckBox" id="payTypeCheckBox" value="1"/>&nbsp;&nbsp;货到付款
						<input type="checkbox" name="payTypeCheckBox" id="payTypeCheckBox" value="0"/>&nbsp;&nbsp;在线支付
					</div>
				</div>
				
				<div class="form-group" id="authURLDIV" style="display: none">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px"></span>支付宝授权链接</label>
					<div class="col-md-2">
						<input name="alipayAuthURL" disabled="disabled" id="alipayAuthURL" alt="https://openauth.alipay.com/oauth2/appToAppAuth.htm?app_id=2016082201782495&redirect_uri=${alipayRedirectUri}${contextPath }/alipayDirectUrl/auth&storeId=${store.id }" value="https://openauth.alipay.com/oauth2/appToAppAuth.htm?app_id=2016082201782495&redirect_uri=${alipayRedirectUri}${contextPath }/alipayDirectUrl/auth&storeId=${store.id }" class="form-control" />
						<button id="clip_button">复制链接到剪贴板</button>
					</div>
					<c:if test="${ alipayAuth != true }">
						<font color="red">已授过权限</font>
					</c:if>
				</div>
				
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px"></span>支付宝商家(shopID)</label>
					<div class="col-md-2">
						<input name="alipayStoreId" id="alipayStoreId" value="${store.alipayStoreId }" class="form-control" tipName="支付宝商家shopID" ignoreNull />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>代理商名称</label>
					<div class="col-md-2">
						<input name="agentName" id="agentName"  readonly="readonly" value="${store.agentName }" class="form-control" isRequired="1" tipName="代理商名称" ignoreNull data-remote="${contextPath }/shop/agentList" data-toggle="modal" />
						<input type="hidden" name="agentId" id="agentId" value="${store.agentId }" />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">总部</label>
					<div class="col-md-2">
						<input name="mainShopName" id="mainShopName"  readonly="readonly" value="${store.mainShopName }" class="form-control" isRequired="0" tipName="总部名称" ignoreNull data-remote="${contextPath }/shop/mainShop/ajax/showOrganizationTreeMode" data-toggle="modal" />
						<!-- <input type="hidden" name="parentId" id="parentId" value="${store.parentId }" /> -->
						<input type="hidden" name="organizationId" id="organizationId" value="${store.organizationId }" />
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
					<label class="col-md-1 control-label">POS商户号</label>
					<div class="col-md-2">
						<input type="text" id="merchantNo"  name="merchantNo" isRequired="0" tipName="POS商户号" value="${store.merchantNo }" class='form-control' maxlength="50"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">POS商户名称</label>
					<div class="col-md-2">
						<input type='text' id="merchantName"  name="merchantName" isRequired="0" tipName="POS商户名称" class='form-control' value="${store.merchantName }" maxlength="50"/>
					</div>
				</div>
				
				<div id="cacheTerminalIdDiv">
					<c:if test="${empty store }">
						<div class="form-group" id="0">
							<label class="col-md-1 control-label">终端号</label>
							<div class="col-md-2">
								<input type="text" id="terminalId0" onblur="checkTerminalId(this);" name="terminalIdArray" isRequired="0" tipName="终端号" value="" class='form-control' maxlength="50"/>
							</div>
								<a href="#" class="btn btn-primary" name="terminalIdAdd" style="margin-top: 5px;"><i class="icon-plus"></i></a>
						</div>					
					</c:if>
					<c:if test="${not empty store }">
						<c:if test="${not empty terminalIdArray }">
							<c:forEach items="${terminalIdArray }" varStatus="sortNo" var="terminalIdArray">
								<c:set var="v" value="${ sortNo.index}"></c:set>
									<div class="form-group" id="${v}">
									<label class="col-md-1 control-label">终端号</label>
									<div class="col-md-2">
										<input type="text" id="terminalId_${v}" onblur="checkTerminalId(this);" name="terminalIdArray" isRequired="0" tipName="终端号" value="${terminalIdArray.terminalId }" class='form-control' maxlength="50"/>
									</div>
										<c:if test="${sortNo.first }">
											<a href="#" class="btn btn-primary" name="terminalIdAdd" style="margin-top: 5px;"><i class="icon-plus"></i></a>								
										</c:if>
										<c:if test="${v != 0}">
											<a onclick="terminalIdDelete(this);" id="deletebutton_${v}" name="deletebutton" class="btn btn-small btn-danger">删除</a>									
										</c:if>
									</div>
							</c:forEach>
						</c:if>
						<c:if test="${empty terminalIdArray }">
							<div class="form-group" id="0">
								<label class="col-md-1 control-label">终端号</label>
								<div class="col-md-2">
									<input type="text" id="terminalId0" onblur="checkTerminalId(this);" name="terminalIdArray" isRequired="0" tipName="终端号" value="" class='form-control' maxlength="50"/>
								</div>
									<a href="#" class="btn btn-primary" name="terminalIdAdd" style="margin-top: 5px;"><i class="icon-plus"></i></a>
							</div>	
						</c:if>
					</c:if>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>标准签约费率( % )</label>
					<div class="col-md-2">
						<input type='text' id="mcc"  name="mcc" isRequired="1" tipName="标准签约费率" fieldType="pRate" class='form-control' value="<fmt:formatNumber value="${store.mcc }" type="currency" pattern="0.00"/>" maxlength="9"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>签约费率( % )</label>
					<div class="col-md-2">
						<input type='text' id="mccSign"  name="mccSign" isRequired="1" tipName="签约费率" fieldType="pRate" class='form-control' value="<fmt:formatNumber value="${store.mccSign }" type="currency" pattern="0.00"/>" maxlength="9"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>收单成本费率( % )</label>
					<div class="col-md-2">
						<input type='text' id="mccCost"  name="mccCost" isRequired="1" tipName="收单成本费率" fieldType="pRate" class='form-control' value="<fmt:formatNumber value="${store.mccCost }" type="currency" pattern="0.00"/>" maxlength="9"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>线上手续费率计算类型</label>
					<div class="col-md-2">
						<select id="onLineRateType" name="onLineRateType" class='form-control'> 
							<option id="0" value="0" selected="selected">比例计算</option>
							<option id="1" value="1">固定金额计算</option>
						</select>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>线上交易手续费率</label>
					<div class="col-md-2">
						<input type='text' id="onLineRate"  name="onLineRate" isRequired="1" tipName="线上交易手续费率" fieldType="pRate" class='form-control' value="<fmt:formatNumber value="${store.onLineRate }" type="currency" pattern="0.00"/>" maxlength="9"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">收单开户行</label>
					<div class="col-md-2">
						<input type='text' id="posBankDeposit"  name="posBankDeposit" isRequired="0" tipName="收单开户行" class='form-control' value="${store.posBankDeposit }" maxlength="50"/>
					</div>
				</div>
				
				
				<div class="form-group">
					<label class="col-md-1 control-label">收单银行账户</label>
					<div class="col-md-2">
						<input type='text' id="posBankAccount"  name="posBankAccount" isRequired="0" tipName="收单银行账户" fieldType="pInteger" class='form-control' value="${store.posBankAccount }" maxlength="19"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">收单账户名</label>
					<div class="col-md-2">
						<input type='text' id="posAccountName"  name="posAccountName" isRequired="0" tipName="收单账户名" class='form-control' value="${store.posAccountName }" maxlength="50"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">收单银行行号</label>
					<div class="col-md-2">
						<input type='text' id="posBankLine"  name="posBankLine" isRequired="0" tipName="收单银行行号" fieldType="pInteger" class='form-control' value="${store.posBankLine }" maxlength="19"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label">使用的打印机</label>
					<div class='col-md-1'>
    					<select name="printer" id="printer" class="form-control" >
    							<option value="0" selected="selected">默认打印机</option>
    							<option value="1">映美打印机</option>
    					</select>
    				</div>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>地址</label>
					<div class="col-md-2">
						<input type='text' id="address"  name="address" isRequired="1" tipName="地址" class='form-control' value="${store.address }" maxlength="200"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>联系人</label>
					<div class="col-md-2">
						<input type='text' id="contact"  name="contact" isRequired="1" tipName="联系人" class='form-control' value="${store.contact }" maxlength="50"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>配送时间</label>
						<div class="col-md-1">
							<input type='text' readonly="readonly" id="startBusinessString"  name="startBusinessString" class='form-control' isRequired="1" tipName="配送时间" ignoreNull value="<fmt:formatDate value="${store.startBusinessDate }" pattern="HH:mm"/>"/>
						</div>
						<label class="" style="float: left;padding-top: 6px">至</label>
						<div class="col-md-1">
							<input type='text' readonly="readonly" id="endBusinessString"  name="endBusinessString" class='form-control' isRequired="1" tipName="配送时间" ignoreNull value="<fmt:formatDate value="${store.endBusinessDate }" pattern="HH:mm"/>"/>
						</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>营业状态</label>
					<div class="col-md-2">
						<input type="radio" name="status" id="status" checked="checked" value="1"/>&nbsp;&nbsp;营业
						<input type="radio" name="status" id="status" value="0"/>&nbsp;&nbsp;结业
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">起送金额( 元 )</label>
					<div class="col-md-2">
						<input type='text' id="minAmount"  name="minAmount" isRequired="0" tipName="起送金额" fieldType="pDouble" class='form-control' value="<fmt:formatNumber value="${store.minAmount }" type="currency" pattern="0.00"/>" maxlength="7"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">配送范围( 公里 )</label>
					<div class="col-md-2">
						<input type='text' id="shipRange"  name="shipRange" isRequired="0" tipName="配送范围" fieldType="pDouble" class='form-control' value="${store.shipRange }" maxlength="50"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">送货费用( 元 )</label>
					<div class="col-md-2">
						<input type='text' id="shipAmount"  name="shipAmount" isRequired="0" tipName="送货费用" fieldType="pDouble" class='form-control' value="<fmt:formatNumber value="${store.shipAmount }" type="currency" pattern="0.00"/>" maxlength="7"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">免配送费限定额( 元 )</label>
					<div class="col-md-2">
						<input type='text' id="freeShipAmount"  name="freeShipAmount" isRequired="0" tipName="免配送费限定额" fieldType="pDouble" class='form-control' value="<fmt:formatNumber value="${store.freeShipAmount }" type="currency" pattern="0.00"/>" maxlength="7"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">服务</label>
					<div class="col-md-2">
						<input type='text' id="service"  name="service" isRequired="0" tipName="服务" class='form-control' value="${store.service }" maxlength="200"/>
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
						<input type='text' id="shipTime"  name="shipTime" isRequired="0" tipName="送达时间" class='form-control' value="${store.shipTime }" maxlength="50"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">联系电话</label>
					<div class="col-md-2">
						<input type='text' id="phone"  name="phone" isRequired="0" tipName="联系电话" fieldType="phone" class='form-control' value="${store.phone }" maxlength="15"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>手机号码</label>
					<div class="col-md-2">
						<input type='text' id="mobile"  name="mobile" isRequired="1" tipName="手机号码" fieldType="mobile" class='form-control' value="${store.mobile }" maxlength="50"/>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" id="warningTip" ></label>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>授权密码</label>
					<div class="col-md-2">
						<input type="password" style="display:none"> <!-- 为了屏蔽掉浏览器记住密码后自动填充到下面的密码框中,加入这个隐藏域 -->
						<input type="password" style="display:none">
						<input type="password" name="posAdminPassword" class='form-control' fieldType="password" id="posAdminPassword" isRequired="0" tipName="授权密码" value="${store.posAdminPassword }" maxlength="20"  >
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">商家LOGO</label>
					<div id="divLogo" class="col-md-2">
						<input type="button" id="upLogo" class="btn btn-large btn-block btn-default"  name="upLogo" data-toggle="tooltip" title="仅支持330*220的格式" value="上传商家LOGO"/>&nbsp;&nbsp;
						<input type="file" id="logoFile" style="width: 1px;height:1px"name="logoFile" class="file"  value="上传图片" title="上传图片"/>
						<input type='hidden' id="logo" name="logo" class='form-control' value="${store.logo }"/>
						<input type="hidden" id="logoFileCache" name="logoFileCache" isRequired="0" tipName="商家LOGO" value="${store.logo }"/>
						<div id="logoImagediv">
							<img alt="商家LOGO" style="cursor: pointer;" id="logoImage" name="logoImage" src="" onload="imageLoad(this);">
						</div>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">地图</label>
					<div class="col-md-2">
						<input id="mapsearchInput"  class='form-control' placeholder="输入地名或者地址进行搜索"/>
					</div>
					<div class="col-md-2">
						<button type="button" class='btn btn-primary' id="mapsearchBtn">搜索</button>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"></label>
					<div id="container" style="width: 900px;height: 500px">
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>经度</label>
					<div class="col-md-2">
						<input type='text' id="longitude"  name="longitude" isRequired="1" tipName="经度" readonly="readonly" class='form-control' value="${store.longitude }" maxlength="15"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>纬度</label>
					<div class="col-md-2">
						<input type='text' id="latitude"  name="latitude" isRequired="1" tipName="纬度" readonly="readonly" class='form-control' value="${store.latitude }" maxlength="15"/>
					</div>
				</div>
				
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button"  id='storeAddBtn' class='btn btn-primary' value="${empty store ? '添加' : '修改' }" data-loading='稍候...' />
					</div>
				</div>
			</form>
	  </div>
   </div>
   <script type="text/javascript">
   		$(function(){
   			$("#mapsearchBtn").click(function(){
	   			 var address = $("#mapsearchInput").val();
	   			 if( null == address || "" == address){
	   				 return ;
	   			 }
	   			 baidumapSearch(address);
	   		 });
   		});
   </script>
   <m:baimap mapContainerId="container" latitudeInputId="latitude" longitudeInputId="longitude"></m:baimap>
<script type="text/javascript">
	var zeroClipboard = null;
	$(function() {
	    ZeroClipboard.setMoviePath("${contextPath}/static/js/ZeroClipboard/ZeroClipboard.swf");
		zeroClipboard = new ZeroClipboard.Client();
		zeroClipboard.setHandCursor( true );
		
		zeroClipboard.addEventListener("load", function (client) {
			console.log("Flash文件加载完毕。");
		});
		
		zeroClipboard.addEventListener("mouseOver", function (client) {// update the text on mouse over
			zeroClipboard.setText($("#alipayAuthURL").val());
		});
		
		zeroClipboard.addEventListener("complete", function (client, text) {
			alert("已将链接复制到剪贴板: " + text );
		});
		
		zeroClipboard.glue( "clip_button" );
	});
</script>
</body>

</html>