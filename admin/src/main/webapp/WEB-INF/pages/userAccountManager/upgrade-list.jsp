<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>可升级会员列表</title>
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
	$("#upgrade").click(function(){
		
		if( cacheArray.length <= 0 ){
			lm.alert("请先勾选需要升级的会员");
			return;
		}
		lm.confirm("确定要升级吗？",function(){
			$("#cacheArray").val(cacheArray);
			var cache = $("#cacheArray").val();
			var storeId = $("#userAccountManager_storeId").val();
			var mobile = $("#mobile").val();
			mobile = $.trim(mobile);
			var cardNum = $("#cardNum").val();
			cardNum = $.trim(cardNum);
			
			lm.post("${contextPath }/userAccountManager/ajax/upgrade-update/",{storeId:storeId,mobile:mobile,cardNum:cardNum,cacheArray:cache},function(data){
				if(data==1){
					lm.alert("升级成功！");
				} else if( data ==2){
					lm.alert("没有可以升级的会员！");
				} else {
					lm.alert("升级失败！");					
				}
					loadCurrentList_userAccountManagerList();
					cacheArray.length = 0;
					$("#cacheArray").val("");
			});			
		});
	});
	
	$("#oneMoreTime").click(function(){
		lm.confirm("确定要一键全部升级吗？",function(){
			var cache = []; // 一键全部升级
			var storeId = $("#userAccountManager_storeId").val();
			var mobile = $("#mobile").val();
			mobile = $.trim(mobile);
			var cardNum = $("#cardNum").val();
			cardNum = $.trim(cardNum);
			
			lm.post("${contextPath }/userAccountManager/ajax/upgrade-update/", {storeId:storeId,mobile:mobile,cardNum:cardNum,cacheArray:cache}, function(data) {
				if(data==1){
					lm.alert("升级成功！");
				} else if( data ==2){
					lm.alert("没有可以升级的会员！");
				} else {
					lm.alert("升级失败！");
				}
					loadCurrentList_userAccountManagerList();
					cacheArray.length = 0;
					$("#cacheArray").val("");
			});
		});
	});
	
});
</script>
</head>
<body>
	<m:hasPermission permissions="userAccountManagerAdd" flagName="addFlag"/>
	<m:list title="可升级会员列表" id="userAccountManagerList"
		listUrl="${contextPath }/userAccountManager/upgrade/list-data"
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
         <input id="upgrade" name="upgrade" class="btn btn-small btn-danger" type="button" style="width: auto;margin-top:5px;" value="升级">
        <input id="oneMoreTime" name="oneMoreTime" class="btn btn-info" type="button" style="width: auto;margin-top:5px;" value="一键全部升级">
	</m:list>
	<input type="hidden" name="cacheArray" id="cacheArray" value="" />
		  
	<m:select_store path="${contextPath}/userAccountManager/showModel/list/list-data" modeId="userAccountManagerShowStore" callback="callback"> </m:select_store>
</body>
</html>