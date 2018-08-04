<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>会员卡信息</title> 
<style>
	.inlin{
		display:inline-block;
		width:31%;
	}
	.inlin label{
	 	width:28%;
	 }
	 .inlin .col-md-0{
	 	width:2%;
	 }
	 .inlin .col-md-2{
	 	width:54%;
	 }
	 .inlin2{
	 	
		display:inline-block;
		width:69%;
	 }
	 .inlin2 .col-md-2{
	 	width:26%;
	 }
	 .text-c,.text-c input{
	 	text-align: center;
	 }
	 .text-c input{
	 	border:none;
	 }
	 .mag-bt{
	 	margin-bottom: 0;
	 }
	 .bg-c{
	 	background-color:#ECECEC;
	 }
	 .padd-t{
	 	margin-top: 6px;
	 	display: inline-block;
	 }
	 
</style>
<script type="text/javascript">
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

$(document).ready(function(){
	$("#userCardDetailsSureBtn").click(function(){
		window.location.href="${contextPath}/userAccountManager/list";
	}); 
});

$(function(){
	// 修改时判断营业状态
	$("#status").val('${userCard.status}');
	$("#sex").val('${user.sex}');
});

$(function(){
	var globalName = "";
	var globalSex = "";
	var globalBirthDay = "";
	var globalIdentity = "";
	var globalStatus = "";
	var globalMobile = "";
	var globalMemo = "";
	$("#edit-info").click(function(){
		var text = $(this).val();
		if(text == "编辑"){
			globalName = $("#name").val();
			globalSex = $("#sex").val();
			globalBirthDay = $("#birthTime").val();
			globalIdentity = $("#identity").val();
			globalStatus = $("#status").val();
			globalMobile = $("#mobile").val();
			globalMemo = $("#memo").val();
			
			$("#name").attr("readOnly",false); // 会员姓名
			$("#sex").attr("disabled",false); // 性别
			
			$("#birthTime").attr("disabled",false); //生日
			$("#birthTime").attr("readOnly",true); //生日
			$("#birthTime").attr("style","background-color:#fff;");
			
			$("#mobile").attr("readOnly",false); // 手机号码
			
			$("#identity").attr("readOnly",false); // 证件号码
			$("#memo").attr("readOnly",false); // 会员备注
			$("#status").attr("disabled",false); // 会员卡状态
			$(this).val("取消编辑");
			$("#edit_font_tip").html("可编辑(会员姓名、手机号、性别、生日、证件号码、会员卡状态、会员备注)");
			$("#sure_show").css("display","none"); // 确定按钮(隐藏)
			$("#cancel_show").css("display",""); // 取消按钮(显示)
			$("#save_show").css("display",""); // 保存按钮(显示)
		} else {
			$("#name").val(globalName);
			$("#sex").val(globalSex);
			$("#birthTime").val(globalBirthDay);
			$("#identity").val(globalIdentity);
			$("#status").val(globalStatus);
			$("#mobile").val(globalMobile);
			$("#memo").val(globalMemo);
			
			$("#name").attr("readOnly",true); // 会员姓名
			$("#sex").attr("disabled",true); // 性别

			$("#birthTime").attr("disabled",true); //生日
			$("#birthTime").removeAttr("style");
			$("#mobile").attr("readOnly",true); // 手机号码
			$("#memo").attr("readOnly",true); // 会员备注
			$("#identity").attr("readOnly",true); // 证件号码
			$("#status").attr("disabled",true); // 会员卡状态
			$(this).val("编辑");
			$("#edit_font_tip").html("");
			$("#sure_show").css("display",""); // 确定按钮(显示)
			$("#cancel_show").css("display","none"); // 取消按钮(隐藏)
			$("#save_show").css("display","none"); // 保存按钮(隐藏)
		}
	});
	
	
	$("#userCardDetailsCancelBtn").click(function(){
		$("#name").val(globalName);
		$("#sex").val(globalSex);
		$("#birthTime").val(globalBirthDay);
		$("#identity").val(globalIdentity);
		$("#status").val(globalStatus);
		$("#mobile").val(globalMobile);
		$("#memo").val(globalMemo);
		
		$("#name").attr("readOnly",true); // 会员姓名
		$("#sex").attr("disabled",true); // 性别

		$("#birthTime").attr("disabled",true); //生日
		$("#birthTime").removeAttr("style");
		$("#mobile").attr("readOnly",true); // 手机号码
		$("#memo").attr("readOly",true); // 会员备注
		
		$("#identity").attr("readOnly",true); // 证件号码
		$("#status").attr("disabled",true); // 会员卡状态
		
		$("#edit-info").val("编辑");
		$("#edit_font_tip").html("");
		
		$("#sure_show").css("display",""); // 确定按钮(显示)
		$("#cancel_show").css("display","none"); // 取消按钮(隐藏)
		$("#save_show").css("display","none"); // 保存按钮(隐藏)
	});
	
	
	$("#userCardDetailsSaveBtn").click(function(){
		var mobile = $("#mobile").val();
		mobile = $.trim(mobile);
		var storeId=$("#storeId").val();
		if( null == mobile || "" == mobile ){
			lm.alert("会员手机号不能为空!");
			return;
		}
		
		if(!lm.isMobile(mobile)){
			lm.alert("会员手机号码格式错误!");
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
		
		
		var cacheMobile = $("#cacheMobile").val();
		var flag = true;
		if( cacheMobile != mobile ){ // 有修改,则进行比对
			lm.postSync("${contextPath }/userAccountManager/ajax/checkConnectedMobile/", {storeId:storeId,mobile:mobile}, function(data) {
				if (data == 1) {
					lm.alert("会员卡手机号码已存在，请检查!");
					flag = false;
				} 
			});
		}
		if (!flag) {
			return;
		}

		var discount = $("#discount").val();
		if (discount == '--'){
			$("#discount").val("");
		}
		$("#userCardDetailsAddForm").submit();
	});
});
</script>
</head>
<body>
	<div class='panel'>
	
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>会员卡信息
				<input id="edit-info" name="edit-info" class="btn btn-info" type="button" style="width: auto;margin-top:5px;" value="编辑">
				<label><font color="red" id="edit_font_tip"></font></label>
			</strong>
		</div>
		
		<div class='panel-body'>
			<form id="userCardDetailsAddForm" method='post' repeatSubmit='1' autocomplete="off" class='form-horizontal' action="${contextPath }/userAccountManager/updateDetail">
				
				<input type="hidden" id="id" name="id" class='form-control' value="${userCard.id }" maxlength="100"  >
				<input type="hidden" id="userId" name="userId" class='form-control' value="${user.id }" maxlength="100"  >
				
					<div  class="form-group inlin">
						<label class="col-md-1 control-label">所属商家</label>
						<div class="col-md-2">
							<input type="text" name="storeName" readonly="readonly" class='form-control' id = "storeName" value="${userCard.storeName }">
							<input type="hidden" name="storeId" class='form-control' id="storeId"  value="${userCard.storeId }">
						</div>
					</div>
				
					<div class="form-group inlin2">
					</div>
					
					<div class="form-group inlin">
						<label class="col-md-1 control-label">会员手机号</label>
						<div class="col-md-2">
							<input type="text" id="mobile" name="mobile" readonly="readonly" class='form-control' value="${userCard.mobile }" maxlength="20"  >
							<input type="hidden" id="cacheMobile" name="cacheMobile" readonly="readonly" class='form-control' value="${userCard.mobile }" maxlength="20"  >
						</div>
					</div>
					
					<div class="form-group inlin2">
						<label class="col-md-1 control-label">会员姓名</label>
						<div class="col-md-2">
							<input type="text" id="name" name="name" readonly="readonly" class='form-control' value="${user.name }" maxlength="20"  >
						</div>
					</div>
				
					<div class="form-group inlin">
						<label class="col-md-1 control-label">性别</label>
						<div class="col-md-2">
							<select id="sex" name="sex" disabled="disabled" class='form-control'>
								<option value="0">男</option>
								<option value="1">女</option>
								<option value="2">保密</option>
							</select>
						</div>
					</div>	
				
					<div class="form-group inlin2">
						<label class="col-md-1 control-label">生日</label>
						<div class="col-md-2">
							<input type="text" id="birthTime" name="birthTime" disabled="disabled" class='form-control' value="<fmt:formatDate value="${user.birthDay }" pattern="yyyy-MM-dd" />" maxlength="20"  >
						</div>
					</div>
					
					<div class="form-group inlin">
						<label class="col-md-1 control-label">身份证号码</label>
						<div class="col-md-2">
							<input type="text" id="identity" name="identity" readonly="readonly" class='form-control' value="${user.identity }" maxlength="18"  >
						</div>
					</div>	
					
					<div class="form-group inlin2">
						<label class="col-md-1 control-label">年龄</label>
						<div class="col-md-2" id="sexDiv">
							
						</div>
					</div>	
					
					<div class="form-group inlin">
						<label class="col-md-1 control-label">会员卡号</label>
						<div class="col-md-2">
							<input type="text" id="cardNum" name="cardNum" readonly="readonly" class='form-control' value="${userCard.cardNum }" maxlength="50"  >
						</div>
					</div>
					
					<div class="form-group inlin2">
						<label class="col-md-1 control-label">会员等级</label>
						<div class="col-md-2">
							<input type="text" id="level" name="level" readonly="readonly" class='form-control' value="${userCard.userLevelName == null ? '--' : userCard.userLevelName }" maxlength="20"  >
						</div>
					</div>
					
					<div class="form-group inlin">
						<label class="col-md-1 control-label">累计消费</label>
						<div class="col-md-2">
							<input type="text" id="totalConsumption" name="totalConsumption" readonly="readonly" class='form-control' value="<fmt:formatNumber value="${userCard.totalConsumption}" type="currency" pattern="0.00"/>" maxlength="50"  >
						</div>
					</div>
				
					<div class="form-group inlin2">
						<label class="col-md-1 control-label">享受折扣</label>
						<div class="col-md-2">
							<input type="text" id="discount" name="discount" readonly="readonly" class='form-control' value="${userCard.userLevelDiscount == null ? '--' : userCard.userLevelDiscount }" maxlength="20"  >
						</div>
					</div>
				
					<div class="form-group inlin">
						<label class="col-md-1 control-label">累计积分</label>
						<div class="col-md-2">
							<input type="text" id="totalPoint" name="totalPoint" readonly="readonly" class='form-control' value="<fmt:formatNumber value="${userCard.totalPoint }" type="currency" pattern="0"/>" maxlength="50"  >
						</div>
					</div>
					
					<div class="form-group inlin2">
						<label class="col-md-1 control-label">账户余额</label>
						<div class="col-md-2">
							<input type="text" id="balance" name="balance" readonly="readonly" class='form-control' value="<fmt:formatNumber value="${userCard.balance }" type="currency" pattern="0.00"/>" maxlength="50"  >
						</div>
					</div>

					<div class="form-group inlin">
						<label class="col-md-1 control-label">账户积分</label>
						<div class="col-md-2">
							<input type="text" id="point" name="point" readonly="readonly" class='form-control' value="<fmt:formatNumber value="${userCard.point }" type="currency" pattern="0"/>" maxlength="50"  >
						</div>
					</div>
				
					<div class="form-group inlin2">
						<label class="col-md-1 control-label">创建日期</label>
						<div class="col-md-2">
							<input type="text" id="createdDate" name="createdDate" readonly="readonly" class='form-control' value="<fmt:formatDate value="${userCard.createdTime }" pattern="yyyy-MM-dd" />" maxlength="20"  >
						</div>
					</div>	
					
					<div class="form-group">
						<label class="col-md-1 control-label">会员卡状态</label>
						<div class="col-md-2" style="margin-top: 3px;">
							<select id="status" name="status" disabled="disabled" class='form-control'>
								<option value="1">启用</option>
								<option value="2">禁用</option>
							</select>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-1 control-label">会员备注</label>
						<div class="col-md-2">
							<textarea id='memo'  name='memo' readonly="readonly" alt='最大输入字数为150个字符' cols=15 rows=5 class='form-control' maxlength='150'>${userCard.memo }</textarea>
						</div>
				   </div>
					
					<label class="col-md-1 control-label">服务套餐：</label>
					<c:if test="${not empty userStoreServicePackageArray }">
							<table border="1" class="text-c">
									<tr class="bg-c">
										<td width="50%">名称</td>
										<td>累计消费次数</td>
										<td>剩余次数</td>
									</tr>
									<c:forEach items="${userStoreServicePackageArray }" varStatus="sortNo" var="userStoreServicePackage">
										<c:set var="v" value="${ sortNo.index}"></c:set>
											<tr>
												<td>
													<div class="form-group mag-bt" id="${v}">
														<div class="">
															<input type="text" id="servicePackage_${v}" readonly="readonly" value="${userStoreServicePackage.storeServicePackage.name }" maxlength="50"/>
														</div>
													</div>
												</td>
												<td>
													<div class="form-group mag-bt" id="${v}">
														<div class="col-md-2">
															<input type="text" id="times_${v}" readonly="readonly" value="${userStoreServicePackage.totalSaleTimes }" maxlength="50"/>
														</div>
													</div>
												</td>
												<td>
													<div class="form-group mag-bt" id="${v}">
														<div class="col-md-2">
															<input type="text" id="remain_times_${v}" readonly="readonly" value="${userStoreServicePackage.times }" maxlength="50"/>
														</div>
													</div>
												</td>
											</tr>
									</c:forEach>
							</table>
					</c:if>
					<c:if test="${empty userStoreServicePackageArray }">
						  <spam class="padd-t">无</spam>
					</c:if>		
					
					<div class="form-group">
						<label class="col-md-1 control-label"></label>
						<div class="col-md-2" style="margin-top: 3px;">
						</div>
					</div>
					
					<div class="col-md-offset-1" id="cancel_show" style="display: none;float: left;">
						<div class="col-md-offset-1 col-md-10">
							<input type="button" id='userCardDetailsCancelBtn' class='btn btn-primary' value="取消" data-loading='稍候...' />
						</div>
						
					</div>
				
					<div class="form-group" id="save_show" style="display: none;float: left;">
						<div class="col-md-offset-1 col-md-10">
							<input type="button" id='userCardDetailsSaveBtn' class='btn btn-primary' value="保存" data-loading='稍候...' />
						</div>
					</div>	
					
					<div class="form-group" id="sure_show">
						<div class="col-md-offset-1 col-md-10">
							<input type="button" id='userCardDetailsSureBtn' class='btn btn-primary' value="确定" data-loading='稍候...' />
						</div>
					</div>
				
			</form>
	  </div>
   </div>
<script type="text/javascript">
	$(function(){
		var birthTime = $("#birthTime").val();
		getAge(birthTime);
	});
</script>
</body>
</html>