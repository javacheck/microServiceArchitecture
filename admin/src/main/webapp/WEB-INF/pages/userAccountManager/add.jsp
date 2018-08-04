<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加会员卡</title> 
<script type="text/javascript">
var isfun_Select_option = 0; // 全局设置会员卡功能
var cacheArray = new Array();

$(function(){
	var format = "yyyy-MM-dd";//默认格式
	var now = lm.Now;//当前日期
	var nowFormat=now.Format(format);
	$("#birthTime").datetimepicker({
		minView: "month", //选择日期后，不会再跳转去选择时分秒 
		format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
		language: 'zh-CN', //汉化 
		autoclose:true , //选择日期后自动关闭
		todayBtn:true,//可选择当天按钮
		todayHighlight:false,//高亮当前日期
		endDate:""+nowFormat+""//默认最后可选择为当前日期
	 });
	
	$('#birthTime').datetimepicker().on('hide', function(ev){
		getAge(this.value);  
	});
});

function getAge(selectDate){
	var sexDiv = $("#sexDiv");
	if( null == selectDate || "" == selectDate ){
		sexDiv.html("0岁");
		return;
	}
	var aDate = new Date();   
	var thisYear = aDate.getFullYear();
	selectDate=selectDate.substr(0,4);
	sexDiv.html((thisYear-selectDate)+"岁");
}

/**
 * 回调商家选择
 */
function callback(obj){
	$("#storeName").val(obj.name);
	$("#storeId").val(obj.id);
}

$(document).ready(function(){
	// 保存信息start
	$("#userCardAddBtn").click(function(){
		var storeId = $("#storeId").val();
		storeId = $.trim(storeId);
		if( null == storeId || "" == storeId ){
			lm.alert("商家不能为空");
			return;
		}
		
		var mobile = $("#mobile").val(); // 等级名称
		mobile = $.trim(mobile);
		
		if( mobile == "" || mobile == null ){
			lm.alert("会员手机号不能为空!");
			return;
		}
		
	   if(!lm.isMobile(mobile)){
		   lm.alert("请输入正确的手机号码");
		   return;
	   }
	   
	   /**
		 *  判断手机号在这个商家中是否唯一
		 */
		var isRepetitions = false;
		lm.postSync("${contextPath }/userAccountManager/list/ajax/checkMobileBystoreId/", {storeId:storeId,mobile:mobile}, function(data) {
			if (data != 0) {
				lm.alert("此手机号已经存在于此商家中");
				isRepetitions = true;
			} 
		});
		
		if (isRepetitions) {
			return;
		}
		
		var identity = $("#identity").val();
		identity = $.trim(identity);
		if( null != identity && "" != identity ){
			if( !(/([\d]){18}$/.test(identity)) ){
				lm.alert("证件号码必须为18位的数字!");
				return;
			}			
		}
		
	  var cardNum = $("#cardNum").val();
	  cardNum = $.trim(cardNum);
	  if( null == cardNum || "" == cardNum ){
		  lm.alert("卡号不能为空");
		  return ;
	  }
	  
	  if( !(/^[0-9]*$/.test(cardNum)) ){
		  lm.alert("卡号必须为数字");
		  return;
	  }
	  
	  if(isfun_Select_option == 0 ){ // 储值功能
		  
		  var actualAmount = $.trim($("#actualAmount").val());
	  	  if( null == actualAmount || "" == actualAmount ){
	  		  lm.alert("实收金额不能为空");
	  		  return;
	  	  }
	  	if( !(lm.isTwoPointFloat(actualAmount)) ){
	  		lm.alert("实收金额格式输入错误");
	  		return ;
	  	  }
	  	
		  var balance = $.trim($("#balance").val());
	  	  if( null == balance || "" == balance ){
	  		  lm.alert("可消费金额不能为空");
	  		  return;
	  	  }
	  	if( !(lm.isTwoPointFloat(balance)) ){
	  		lm.alert("可消费金额格式输入错误");
	  		return ;
	  	  }
	  } else if(isfun_Select_option == 1){ // 服务套餐
		  var storeServicePackageId = $.trim($("#storeServicePackageId").val());
	  	  if( null == storeServicePackageId || "" == storeServicePackageId ){
	  		  lm.alert("套餐内容不能为空");
	  		  return ;
	  	  }
		  var times = $.trim($("#times").val());
	  	  if( null == times || "" == times ){
	  		  lm.alert("服务次数不能为空");
	  		  return;
	  	  }
		  if( !(/^\+?[1-9][0-9]*$/.test(times)) ){
			  lm.alert("服务次数必须为非零的正整数");
			  return;
		  }
		  var realityPrice = $("#realityPrice").val();
		  realityPrice = $.trim(realityPrice);
		  if( null == realityPrice || "" == realityPrice ){
			  lm.alert("实收金额不能为空");
			  return ;
		  }
		  if( !(lm.isTwoPointFloat(realityPrice)) ){
		  		lm.alert("实收金额格式输入错误");
		  		return ;
		  	  }
		  
	  } else if(isfun_Select_option == 2){ // 折扣功能
		  var discountType = $("#discountType").val();
	  	  if(discountType == 2){ // 固定折扣
	  		  var discount = $.trim($("#discount").val());
	  	  	  if( null == discount || "" == discount ){
	  	  		  lm.alert("固定折扣不能为空");
	  	  		  return ;
	  	  	  }
	  	  	 if( !(lm.isFloat(discount) && discount >= 0 && discount <= 100) ) {
	  	  		 lm.alert("固定折扣格式输入错误");
	  	  		 return;
	  	  	 }
	  	  }
	  }
		/**
		 *  判断卡号在这个商家中是否唯一
		 */
		var isRepetition = false;
		lm.postSync("${contextPath }/userAccountManager/list/ajax/checkCardNumBystoreId/", {storeId:storeId,cardNum:cardNum}, function(data) {
			if (data != 0) {
				lm.alert("此卡号已经存在于此商家中");
				isRepetition = true;
			} 
		});
		
		if (isRepetition) {
			return;
		}
		
		$("#userCardAddForm").submit();
	}); 
	// 保存信息end
});

/**
 * select选择
 */
$(function(){
	
	$("#storeServicePackageId").change(function(){
		var selectID = $(this).val();
		$("#times").val("");
		if( cacheArray.length > 0 ){
			for( var k = 0 ; k < cacheArray.length ; k++){
				var getMap = cacheArray[k].split(",");
				if( selectID == getMap[0] ){
					$("#storeServicePackageId").val(getMap[0]);
					$("#times").val(getMap[1]);
				}
			}
		}
	});
	
	$("#fun_Select").change(function(){
		userCard_select();
	});
	
	$("#discountType").change(function(){
		discountType_select();
	});
	
	// 商家选择
	$("#storeName").click(function (){
		$("#userCardStore").modal();
	});
	
	$("#servicePackage_AddBtn").click(function(){
		// 必须有商家
		var storeId = $("#storeId").val();
		storeId = $.trim(storeId);
		if( null == storeId || "" == storeId ){
			lm.alert("请先选择商家");
			return;
		}
		
		$("#servicePackage_storeId").val(storeId); // 将商家赋值
		$("#servicePackageShowID").modal();
	});
});

/**
 * 初始化
 */
$(function(){
	userCard_select(); // 初始化就判断用户卡功能的选择
	isfun_Select_option = 0;
});

/**
 * 折扣方式选择判断
 */
function discountType_select(){
	var type = $("#discountType").val();
	if(type == 2){ // 固定折扣
		$("#fixedDiscountShow").show(); // 固定折扣显示		
	} else {
		$("#fixedDiscountShow").hide(); // 固定折扣隐藏			
	}
}

/**
 * 用户卡功能选择——判断
 */
function userCard_select(){
	var funOption = $("#fun_Select").val();
	if( funOption == 0 ){ // 储值功能
		$("#balanceShow").show(); // 实收金额显示
		$("#balance_Show").show(); // 实收金额显示
		$("#storeServicePackageId option").remove();
		
		$("#storeServicePackageIdShow").hide(); // 套餐内容隐藏
		$("#serviceNumShow").hide(); // 服务次数隐藏
		$("#realityPriceShow").hide(); // 服务套餐实收金额隐藏
		
		$("#discountTypeShow").hide(); // 折扣方式隐藏
		$("#fixedDiscountShow").hide(); // 固定折扣隐藏
	} else if(funOption == 1){ // 服务套餐
		var storeId = $.trim($("#storeId").val());
		if( null == storeId || "" == storeId ){
			lm.alert("请先选择商家");
			$("#fun_Select").val(isfun_Select_option);
			return ;
		}
		$("#storeServicePackageIdShow").show();// 套餐内容显示
		$("#serviceNumShow").show(); // 服务次数显示
		$("#realityPriceShow").show(); // 服务套餐实收金额显示
		getServicePackageSelect();
		$("#balanceShow").hide(); // 实收金额隐藏
		$("#balance_Show").hide(); // 实收金额隐藏
		
		$("#discountTypeShow").hide(); // 折扣方式隐藏
		$("#fixedDiscountShow").hide(); // 固定折扣隐藏
	} else { // 折扣功能
		$("#discountTypeShow").show(); // 折扣方式显示
		 discountType_select();
		 $("#storeServicePackageId option").remove();
		
		$("#balanceShow").hide(); // 实收金额隐藏
		$("#balance_Show").hide(); // 实收金额隐藏
		
		$("#storeServicePackageIdShow").hide(); // 套餐内容隐藏
		$("#serviceNumShow").hide(); // 服务次数隐藏
		$("#realityPriceShow").hide(); // 服务套餐实收金额隐藏
	}
	isfun_Select_option = funOption;
}

/**
 * 获得套餐内容列表
 */
function getServicePackageSelect(){
	var storeId = $.trim($("#storeId").val());
	if( null == storeId || "" == storeId ){
		return;
	}
	cacheArray.length = 0 ; //清空缓存数组
	lm.postSync("${contextPath }/userAccountManager/list/ajax/getServicePackageSelect/",{storeId:storeId},function(data){
		if( undefined != data && null != data && "" != data){
			for( var i=0 ;i < data.length ;i++ ){
				$("#storeServicePackageId").append('<option value="' + data[i].id + '">' + data[i].name + '</option>');
				cacheArray.push(data[i].id + "," + data[i].times);
			}
			$("#storeServicePackageId").val(data[0].id);
			$("#times").val(data[0].times);
		}
	});
}

/**
 * 点击新增套餐内容保存
 */
$(function(){
	$("#servicePackage_OkBtn").click(function(){
		var servicePackage_name = $("#servicePackage_name").val();
		servicePackage_name = $.trim(servicePackage_name);
		if( null == servicePackage_name || "" == servicePackage_name){
			lm.alert("套餐内容不能为空");
			return;
		}
		var storeId = $.trim($("#servicePackage_storeId").val());
		var isRepetition = false;
		lm.postSync("${contextPath }/userAccountManager/list/ajax/checkServicePackageName/",{id:null,storeId:storeId,name:servicePackage_name},function(data){
			if( data != 0 ){
				isRepetition = true;
			}
		});
		if(isRepetition){
			var storeName = $("#storeName").val();
			lm.alert(storeName + "商家已存在此套餐内容");
			return;
		}
		var servicePackage_times = $("#servicePackage_times").val();
		servicePackage_times = $.trim(servicePackage_times);
		if( null == servicePackage_times || "" == servicePackage_times){
			lm.alert("服务次数不能为空");
			return;
		}
		
		if( !(/^\+?[1-9][0-9]*$/.test(servicePackage_times)) ){
			lm.alert("服务次数必须为非零的正整数");
			return;
		}
		
		// 保存套餐
		lm.postSync("${contextPath }/userAccountManager/list/ajax/saveServicePackage/",$("#servicePackageSave").serialize(), function(data) {
			$("#storeServicePackageId").append('<option value="' + data.id + '">' + data.name + '</option>');
			$("#servicePackageClose").click();
			$("#storeServicePackageId").val(data.id);
			$("#times").val(data.times);
			cacheArray.push(data.id + "," + data.times);
			$("#servicePackage_name").val("");
			$("#servicePackage_times").val("");
			
		});
	});
});

$(function(){
	$("#actualAmount").blur(function(){
		var actualAmount = $.trim($(this).val());
	    if( null != actualAmount && "" != actualAmount ){
		  if( (lm.isFloat(actualAmount) && actualAmount >= 0) ){
		  	  $("#balance").val(actualAmount); // 失焦后将实收金额填充入可消费金额输入框中
		  }
  	    }
	});
});

$(function(){
	$("#mobile").blur(function(){
		var storeId = $("#storeId").val();
		if( null == storeId || "" == storeId ){
			lm.alert("请先选择商家!");
			$(this).val("");
			return;
		}
		var mobile = $.trim($(this).val());
		if( null != mobile && "" != mobile  ){
			if(!lm.isMobile(mobile)){
				return;
			}
			lm.postSync("${contextPath }/userAccountManager/list/ajax/checkUserMobile/",{storeId:storeId,mobile:mobile}, function(data) {
				 if( null != data && undefined != data && "" != data ){
					 $("#sex").val(data.sex);
					 var a = replaceTimeStamp(data.birthDay);
					 $("#birthTime").val(a);
					 getAge(a);
					 $("#identity").val(data.identity);
					 $("#name").val(data.name);
				 } else {
					 clearParameter();
				 }			
			});
		
			lm.post("${contextPath }/userAccountManager/list/ajax/getLevel/",{storeId:storeId,mobile:mobile}, function(data) {
				 $("#levelId option[value != '']").remove();
				 if( null != data && undefined != data && "" != data ){
					 var option = "";
					 for( var i=0;i<data.length;i++){
						 option += "<option value="+data[i].id+" id="+data[i].id+">"+data[i].name+"</option>";
					 }
					 $("#levelId").append(option);
				 } else {
					 $("#levelId option[value != '']").remove(); 
				 }		
			});
			
		}
	});
});

function clearParameter(){
	 $("#sex").val(2);
	 $("#birthTime").val("");
	 $("#identity").val("");
	 getAge("");
	 $("#name").val("");
}

function replaceTimeStamp(timestamp){
	if( null == timestamp || undefined == timestamp || "" == timestamp ){
		return null;
	}
	var d = new Date(timestamp);    //根据时间戳生成的时间对象
	var date = (d.getFullYear()) + "-" + 
			   ( (d.getMonth()+1) < 10 ? ('0'+(d.getMonth()+1)) : (d.getMonth()+1) ) + '-' +
			   ( (d.getDate()) < 10 ? ('0'+(d.getDate())) : (d.getDate()) );
	return date;
}
</script>
</head>
<body>
	<div class='panel'>
	
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>添加会员卡
			</strong>
		</div>
		
		<div class='panel-body'>
			<form id="userCardAddForm" method='post' repeatSubmit='1' autocomplete="off" class='form-horizontal' action="${contextPath }/userAccountManager/saveOrUpdateUserCard">
				<input type="hidden" name="isStore" class='form-control' id="isStore"  value="${isStore}">
				<c:if test="${isStore == false }">
					<div class="form-group">
						<label class="col-md-1 control-label">商家</label>
						<div class="col-md-2">
							<input type="text" name="storeName" readonly="readonly" class='form-control' id = "storeName" value="${userCard.storeName }">
							<input type="hidden" name="storeId" class='form-control' id="storeId"  value="${userCard.storeId }">
						</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
					</div>
				</c:if>
				<c:if test="${isStore == true }">
					<input type="hidden" name="storeId" class='form-control' id="storeId"  value="${userCard.storeId }">
				</c:if>				
				<div class="form-group">
					<label class="col-md-1 control-label">会员手机号</label>
					<div class="col-md-2">
						<input type="text" id="mobile" name="mobile" class='form-control' value="${userCard.mobile }" maxlength="20"  >
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
					
				<div class="form-group">
					<label class="col-md-1 control-label">会员姓名</label>
					<div class="col-md-2">
						<input type="text" id="name" name="name" class='form-control' value="${userCard.name }" maxlength="20"  >
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">会员性别</label>
					<div class="col-md-2">
						<select id="sex" name="sex" class='form-control'>
							<option value="0">男</option>
							<option value="1">女</option>
							<option value="2" selected="selected">保密</option>
						</select>
					</div>
				</div>
				
				<div class="form-group">
						<label class="col-md-1 control-label">会员生日</label>
						<div class="col-md-2">
							<input type="text" id="birthTime" name="birthTime" readonly="readonly" class='form-control' value="<fmt:formatDate value="${user.birthDay }" pattern="yyyy-MM-dd" />" maxlength="20"  >
						</div>
				</div>
				
				<div class="form-group">
						<label class="col-md-1 control-label">会员年龄</label>
						<div class="col-md-2" id="sexDiv">
							<!-- 
							<input type="text" id="sex" name="sex" readonly="readonly" class='form-control' value="" maxlength="20"  >
							 -->
						</div>
				</div>
					
				<div class="form-group">
						<label class="col-md-1 control-label">身份证号码</label>
						<div class="col-md-2">
							<input type="text" id="identity" name="identity" class='form-control' value="${user.identity }" maxlength="18"  >
						</div>
					</div>
				
				<div class="form-group">
						<label class="col-md-1 control-label">会员等级</label>
						<div class="col-md-2">
							<select class="form-control" id="levelId" name="levelId">
								<option value="">请选择会员等级</option>
							</select>
						</div>
					</div>
						
				<div class="form-group">
					<label class="col-md-1 control-label">会员卡功能</label>
					<div class="col-md-2">
						<select id="fun_Select" name="fun_Select" class='form-control'> 
							<option id="0" value="0" selected="selected">储值功能</option>
							<option id="1" value="1">服务套餐</option>
							<!--  <option id="2" value="2">折扣功能</option> 2016.03.18因需求变更，将折扣去掉-->
						</select>
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
					<div class="form-group">
					<label class="col-md-1 control-label">卡号</label>
					<div class="col-md-2">
						<input type="text" id="cardNum" name="cardNum" class='form-control' value="${userCard.cardNum }" maxlength="50"  >
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group" id="balanceShow" style="display: none">
					<label class="col-md-1 control-label">实收金额</label>
					<div class="col-md-2">
						<input type="text" id="actualAmount" name="actualAmount" class='form-control'  maxlength="7"  >
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group" id="balance_Show" style="display: none">
					<label class="col-md-1 control-label">可消费金额</label>
					<div class="col-md-2">
						<input type="text" id="balance" name="balance" placeholder="默认等于实收金额" class='form-control' maxlength="7"  >
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group" id="storeServicePackageIdShow" style="display: none">
					<label class="col-md-1 control-label">套餐内容</label>
					<div class="col-md-2">
						<select id="storeServicePackageId" name="userStoreServicePackage.storeServicePackageId" class='form-control'> 
						</select>
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
					<input type="button" id='servicePackage_AddBtn' class='btn btn-primary' value="新增套餐" />
				</div>
				
				<div class="form-group" id="serviceNumShow" style="display: none">
					<label class="col-md-1 control-label">服务次数</label>
					<div class="col-md-2">
						<input type="text" id="times" name="userStoreServicePackage.times" class='form-control' value="${userCard.userStoreServicePackage.times }" maxlength="7"  >
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group" id="realityPriceShow" style="display: none">
					<label class="col-md-1 control-label">实收金额</label>
					<div class="col-md-2">
						<input type="text" id="realityPrice" name="realityPrice" class='form-control'  maxlength="7"  >
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group" id="discountTypeShow" style="display: none">
					<label class="col-md-1 control-label">折扣方式</label>
					<div class="col-md-2">
						<select id="discountType" name="discountType" class='form-control'> 
							<option id="1" value="1" selected="selected">按会员等级折扣</option>
							<option id="2" value="2">输入固定折扣</option>
							<option id="3" value="3">无折扣</option>
						</select>
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group" id="fixedDiscountShow" style="display: none">
					<label class="col-md-1 control-label">固定折扣</label>
					<div class="col-md-2">
						<input type="text" id="discount" name="discount" class='form-control' value="${userCard.discount }" maxlength="100"  >
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">会员备注</label>
					<div class="col-md-2">
						<textarea id='memo'  name='memo' alt='最大输入字数为150个字符' cols=15 rows=5 class='form-control' maxlength='150'>${userCard.memo }</textarea>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"></label>
					<div class="col-md-2" style="margin-top: 3px;">
						<input id="status"  type='radio' value="1" name="status" checked="checked"/>启用
						<input id="status"  type='radio' value="2" name="status"/>禁用
					</div>
				</div>
						
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button" id='userCardAddBtn' class='btn btn-primary' value="添加" data-loading='稍候...' />
					</div>
				</div>
				
			</form>
	  </div>
   </div>
   
   <m:select_store path="${contextPath}/shop/showModle/list/list-data" modeId="userCardStore" callback="callback"> </m:select_store>
   
   <div class="modal fade" id="servicePackageShowID" >
	 <div class="modal-dialog modal-sm" style="width:600px">
	  <div class="modal-content">
		    <div class="modal-header">
		      <button type="button" id="servicePackageClose" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
		    </div>
		    <div class="modal-body">
		<div class='panel-body'>
			<form action="" id="servicePackageSave" autocomplete="off" >
				<input type="hidden" name="storeId" class='form-control' id="servicePackage_storeId" >
				<table class="table table-hover table-striped table-bordered" cellpadding="0" cellspacing="0">
					<tr>
						<td style="width:40%">套餐内容</td>
						<td>
							<input type="text" id="servicePackage_name" name="name" class='form-control' maxlength="50"  >
						</td>
					</tr>
					
					<tr>
						<td style="width:40%">服务次数</td>
						<td>
							<input type="text" id="servicePackage_times" name="times" class='form-control' maxlength="7"  >
						</td>
					</tr>
		
					<tr>
						<td colspan="2" align="center"><input type="button" id='servicePackage_OkBtn' class='btn btn-primary' value="确定" /></td>
					</tr>			
				</table>
			</form>
		</div>
		    </div>
	  </div>
	</div>
</div>
</body>
</html>