<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>会员账号管理列表</title>
<script type="text/javascript">
function callback(obj){
	$("#userAccountManager_storeName").val(obj.name);
	$("#userAccountManager_storeId").val(obj.id);
}
function formReset(){
	$("#userAccountManager_storeName").val("");
	$("#userAccountManager_storeId").val("");
}
$(function(){
	// 点击弹出商家选择窗口
	$("#userAccountManager_storeName").click(function (){
		$("#userAccountManagerShowStore").modal();
	});
});

$(function(){
	$("#reportUserAccountManager").click(function(){
		var storeId = $("#userAccountManager_storeId").val();
		if( undefined == storeId ){
			storeId = "";
		} 
		var reportMobile = $("#mobile").val(); // 会员号
		reportMobile = $.trim(reportMobile);  // 用jQuery的trim方法删除前后空格
		
		var reportCardNum = $("#cardNum").val(); // 会员卡号
		reportCardNum = $.trim(reportCardNum);  // 用jQuery的trim方法删除前后空格
		location.href = "${contextPath}/userAccountManager/ajax/reportUserAccountManagerToExcel?reportCardNum="+reportCardNum+"&storeId="+storeId +"&reportMobile="+reportMobile;
	});
});

var cacheArray = new Array();
function checkboxSelectEvent(obj){
	if(obj.checked){
		cacheArray.push($(obj).attr("sign"));
	} else {
		cacheArray.splice($.inArray($(obj).attr("sign"),cacheArray),1); 
	}
}

function checkPageALL(obj){
	if(obj.checked){
		$("#show_userCard_Model input:checkbox[id!='pageALLCheckBox']").each(function(key,value){
			if(-1 ==  $.inArray($(value).attr("sign") ,cacheArray) ){
				cacheArray.push($(value).attr("sign"));	
			}
		});
		$("#show_userCard_Model input:checkbox[id!='pageALLCheckBox']").prop("checked",true);
	} else {
		$("#show_userCard_Model input:checkbox[id!='pageALLCheckBox']").each(function(key,value){
			// 查询数组中是否有此数据，有则从数组中移除
			if(-1 !=  $.inArray($(value).attr("sign") ,cacheArray) ){
				// 移除指定的元素
				cacheArray.splice($.inArray($(value).attr("sign") ,cacheArray),1); 
			}
		});
		$("#show_userCard_Model input:checkbox[id!='pageALLCheckBox']").prop("checked",false);
	}
}

$(function(){
	$("#report-for-loss").click(function(){
		
		if( cacheArray.length <= 0 ){
			lm.alert("请先勾选需要挂失的会员卡");
			return;
		}
		lm.confirm("确定要挂失吗？",function(){
			$("#cacheArray").val(cacheArray);
			var cache = $("#cacheArray").val();
			lm.post("${contextPath }/userAccountManager/ajax/updateStatus-by-Id",{status:3,cacheArray:cache},function(data){
				if(data==1){
					lm.alert("挂失成功！");
				} else {
					lm.alert("挂失失败！");
				}
					loadCurrentList_userAccountManagerList();
					cacheArray.length = 0;
					$("#cacheArray").val("");
			});			
		});
	});
	
	$("#undo-report-for-loss").click(function(){
		
		if( cacheArray.length <= 0 ){
			lm.alert("请先勾选需要取消挂失的会员卡");
			return;
		}
		lm.confirm("确定要取消挂失吗？",function(){
			$("#cacheArray").val(cacheArray);
			var cache = $("#cacheArray").val();
			lm.post("${contextPath }/userAccountManager/ajax/updateStatus-by-Id",{status:1,cacheArray:cache},function(data){
				if(data==1){
					lm.alert("取消挂失成功！");
				} else {
					lm.alert("取消挂失失败！");
				}
					loadCurrentList_userAccountManagerList();
					cacheArray.length = 0;
					$("#cacheArray").val("");
			});			
		});
	});
	
	$("#changeCard").click(function(){
		if( cacheArray.length <= 0 ){
			lm.alert("请先勾选需要换卡的记录");
			return;
		} else if( cacheArray.length > 1 ){
			lm.alert("换卡操作只能单个卡号的进行，暂不支持多卡同时操作");
			return;
		} else {
			$("#cardID").val(cacheArray[0]);
			var cardID = $("#cardID").val();
			lm.post("${contextPath }/userAccountManager/list/ajax/getChangeCardInfo/", {cardID:cardID}, function(data) {
				if( null != data && "" != data && undefined != data ){
					$("#changeShowMobile").html(data.mobile);
					$("#changeShowCard").html(data.cardNum);
				}
			});
		}
		$("#userAccountManagerShowID").modal();
	});
	
	$("#userAccountManagerShowSure").click(function(){
		var changeCardNumber = $("#changeCardNumber").val();
		changeCardNumber = $.trim(changeCardNumber);
		if( null == changeCardNumber || "" == changeCardNumber ){
			lm.alert("更换卡号不能为空!");
			return;
		}
		
		if( !(/^[0-9]*$/.test(changeCardNumber)) ){
			 lm.alert("卡号必须为数字");
			 return;
		}
		
		var cardID = $("#cardID").val();
		var flag = false;
		lm.postSync("${contextPath }/userAccountManager/list/ajax/saveRandomCardNumber/", {cardID:cardID,changeCardNumber:changeCardNumber}, function(data) {
			if( data == 1 ){
				lm.alert("换卡成功!");
				flag = true;				
			} else if( data == 2 ) {
				lm.alert("卡号存在重复!");
				flag = true;
			}
		});
		
		if(flag){ // 修改成功
			loadCurrentList_userAccountManagerList(); // 刷新页面
			$("#userAccountManagerShowClose").click();
			cacheArray.length = 0;
		}
	});
	
	$("#userAccountManagerShowCancel").click(function(){
		$("#userAccountManagerShowClose").click();
	});
	
	var globalChangeCardNumber = "";
	
	$('#userAccountManagerShowID').on('hidden.zui.modal', function() {
		$("#changeCardNumber").val("");
		$("#changeType").val(0);
		$("#changeShowMobile").html("");
		$("#changeShowCard").html("");
		globalChangeCardNumber = "";
	});
	
	$("#changeType").change(function(){
		var changeType = $(this).val();
		if( 0 == changeType ){ // 手动输入
			$("#changeCardNumber").val("");
		} else { // 自动输入
			if( "" != globalChangeCardNumber ){
				$("#changeCardNumber").val(globalChangeCardNumber);
			} else {
				lm.postSync("${contextPath }/userAccountManager/list/ajax/getRandomCardNumber/", {}, function(data) {
					globalChangeCardNumber = data;
					$("#changeCardNumber").val(data);
				});
			}
		}
	});
});
</script>
</head>
<body>
	<m:hasPermission permissions="userAccountManagerAdd" flagName="addFlag"/>
	<m:list title="会员账号管理列表" id="userAccountManagerList"
		listUrl="${contextPath }/userAccountManager/list-data"
		addUrl="${addFlag == true ? '/userAccountManager/add' : '' }"
		searchButtonId="userAccountManager_search_btn" formReset="formReset">
		
		<div class="input-group" style="max-width: 800px;">
			<c:if test="${isMainStore}">
				<span class="input-group-addon">商家</span>
				<input name="storeName" id="userAccountManager_storeName" readOnly="readOnly" placeholder="请选择商家" style="width: 200px;" class="form-control" />
				<input type="hidden" name="storeId" id="userAccountManager_storeId" value="" />
			</c:if>
					
		 	<span class="input-group-addon">卡号</span> 
            	<input type="text" id="cardNum" name="cardNum" class="form-control" placeholder="卡号" style="width: 200px;">
            	
            	<span class="input-group-addon">会员手机号</span> 
            	<input type="text" id="mobile" name="mobile" class="form-control" placeholder="会员手机号" style="width: 200px;">
        </div>
        <a id="reportUserAccountManager" class="btn btn-small btn-danger" style="width: auto;margin-top:5px;">导出</a>
        <input id="report-for-loss" name="report-for-loss" class="btn btn-info" type="button" style="width: auto;margin-top:5px;" value="挂失">
        <input id="undo-report-for-loss" name="undo-report-for-loss" class="btn btn-info" type="button" style="width: auto;margin-top:5px;" value="取消挂失">
        <input id="changeCard" name="changeCard" class="btn btn-info" type="button" style="width: auto;margin-top:5px;" value="换卡">
	</m:list>
	<input type="hidden" name="cacheArray" id="cacheArray" value="" />
	
	
	<!-- 信息详情展示start -->
		  <div class="modal fade" id="userAccountManagerShowID">
			 <div class="modal-dialog modal-sale-show">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" id="userAccountManagerShowClose" name="userAccountManagerShowClose" class="close" data-dismiss="modal">
							<span aria-hidden="true">×</span><span class="sr-only">关闭</span>
						</button>
						<h4 class="modal-title" style="text-align: center;">更换会员卡</h4>
					</div>
					   <table class="table table-hover table-striped table-bordered" cellpadding="0" cellspacing="0">
						 <tbody>
						 	
		            		<tr class="bg-co"  style="width: 100%;height: 30px">
		                    	<td width="50%"><span style="color:#176AE8">会员手机号：</span></td>
		                    	<td><input type="hidden" id="cardID" name="cardID" value=""/><span id="changeShowMobile" name="changeShowMobile"></span></td>
		                	</tr>
		                	<tr class="bg-co"  style="width: 100%;height: 30px">
		                    	<td width="50%"><span style="color:#176AE8">会员卡号：</span></td>
		                    	<td><span id="changeShowCard" name="changeShowCard"></span></td>
		                	</tr>
		                	<tr class="bg-co" style="width: 100%; height: 30px;">
								<td width="50%"><span style="color:#176AE8">更换方式：</span></td>
								<td>
									<select id="changeType" name="changeType">
										<option id="shoudong" value="0">手动输入</option>
										<option id="zidong" value="1">自动输入</option>
									</select>
								</td>
		            		</tr>
		            		<tr class="bg-co" style="width: 100%; height: 30px;">
								<td width="50%"><span style="color:#176AE8">更换卡号：</span></td>
								<td><input type="text" class="form-control" id="changeCardNumber" name="changeCardNumber" maxlength="20" value=""/></td>
		            		</tr>
		            		<tr class="bg-co" style="width: 100%; height: 30px;">
								<td width="50%" style="text-align: center;">
									<button id="userAccountManagerShowCancel"  name="userAccountManagerShowCancel" class='btn btn-small btn-warning'>取消</button>
								</td>
								<td width="50%" style="text-align: center;">
									<button id="userAccountManagerShowSure"  name="userAccountManagerShowSure" class='btn btn-small btn-warning'>确定</button>
								</td>
		            		</tr>
		            		
            		   </tbody>
					</table>
			   </div>
			</div>
		</div>
	  <!-- 信息详情展示end -->
	  
	<m:select_store path="${contextPath}/userAccountManager/showModel/list/list-data" modeId="userAccountManagerShowStore" callback="callback"> </m:select_store>
</body>
</html>