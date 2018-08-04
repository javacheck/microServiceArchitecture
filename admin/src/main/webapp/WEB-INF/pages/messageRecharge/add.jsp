<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty MessageRecharge ? '添加' : '修改' }短信充值</title> 
<script type="text/javascript">
//定义存储促销商品数据的缓存数组
var cacheArray = new Array();
var isRealityCacheArray = new Array();

function checkboxSelectEvent(obj){
	if(obj.checked){
		$(obj).prop("checked",false);
	} else {
		$(obj).prop("checked",true);
	}
}

function trSelectEvent(obj){
	var child = $(obj).find("input:checkbox");
	if(child[0].checked){
		child.prop("checked",false);
		// 移除指定的元素
		cacheArray.splice($.inArray($(obj).attr("shopIdSign"),cacheArray),1); 
	} else {
		child.prop("checked",true);
		cacheArray.push($(obj).attr("shopIdSign"));	
	}
}

function checkPageALL(obj){
	if(obj.checked){
		$("#show_Store_Model input:checkbox[id!='pageALLCheckBox']").each(function(key,value){
			if(-1 ==  $.inArray($(value).attr("shopIdSign") ,cacheArray) ){
				cacheArray.push($(value).attr("shopIdSign"));	
			}
		});
		$("#show_Store_Model input:checkbox[id!='pageALLCheckBox']").prop("checked",true);
	} else {
		$("#show_Store_Model input:checkbox[id!='pageALLCheckBox']").each(function(key,value){
			// 查询数组中是否有此数据，有则从数组中移除
			if(-1 !=  $.inArray($(value).attr("shopIdSign") ,cacheArray) ){
				// 移除指定的元素
				cacheArray.splice($.inArray($(value).attr("shopIdSign") ,cacheArray),1); 
			}
		});
		$("#show_Store_Model input:checkbox[id!='pageALLCheckBox']").prop("checked",false);
	}
}

// ----------------------------------------------------------------------------------------------------------

$(document).ready(function(){

	// 保存商品库存信息
	$("#messageRechargeAddBtn").click(function(){
		
		if( isRealityCacheArray.length <= 0 ){
			lm.alert("请先选择需要短信充值的商家!");
			return ;
		}
		
		var price = $("#price").val();
		price = $.trim(price);
		if( null == price || "" == price ){
			lm.alert("充值金额不能为空");
			return;
		}
		
		if( !(lm.isTwoPointFloat(price)) ){
			lm.alert("充值金额格式错误!");
			return;
		}
		
		var number = $("#number").val();
		number = $.trim(number);
		if( null == number || "" == number ){
			lm.alert("充值数量不能为空!");
			return;
		}
		
		if( !(/^(0|[1-9][0-9]*)$/.test(number)) ){
			lm.alert("充值数量格式错误!");
			return;
		}
		
		var operationPassword = $("#operationPassword").val();
		operationPassword = $.trim(operationPassword);
		if( null == operationPassword || "" == operationPassword ){
			lm.alert("请输入操作密码!");
			return ;
		}
		
		var isRepeat = false;
		lm.postSync("${contextPath }/messageRecharge/ajax/checkOperationPassword/", {operationPassword:operationPassword}, function(data) {
			if (data == 1) {
				lm.alert("操作密码错误,请重新输入!");
				isRepeat = true;
			} 
		});
		if(isRepeat){
			$("#operationPassword").val("");
			return;
		}
		$("#storeIdArray").val(isRealityCacheArray);
		
		$("#messageRechargeAddForm").submit();
	}); 
	
});

$(function(){
	$("#infoButton").click(function(){
		cacheArray.length = 0; // 先清空缓存数组数据
		
		cacheArray = isRealityCacheArray.concat(cacheArray); // 将真实保存的数组数据复制到缓存数组中(以真实保存数组为准)
		
		loadCurrentList_messageRechargeList_modelShow();
		$("#messageRechargeStoreShowID").modal();		
	});
	
	$("#messageRechargeInfoAddBtn").click(function(){
		isRealityCacheArray.length = 0; // 清空真实保存数组数据
		isRealityCacheArray = isRealityCacheArray.concat(cacheArray); // 将缓存数组数据复制到真实保存数组中(以缓存数组为准)
		$("#messageRechargeClose").click();
	});
});
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>${empty MessageRecharge ? '添加' : '修改' }短信充值
			</strong>
		</div>
		<div class='panel-body'>
			<form id="messageRechargeAddForm" method='post' repeatSubmit='1' class='form-horizontal' action="${contextPath }/messageRecharge/save">
				<div class="form-group">
					<label class="col-md-1 control-label">商家</label>
					<div class="col-md-2">
						<button type="button" id="infoButton" name='infoButton' class="btn btn-small btn-primary">选择商家</button>
						<input type="hidden" id="storeIdArray" name="storeIdArray" value=""/>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">充值金额(元)</label>
					<div class="col-md-2">
						<input type="text" name="price" class='form-control' id="price"  value="${messageRecharge.price }" maxlength="7"  >
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
								
				<div class="form-group">
					<label class="col-md-1 control-label">充值数量</label>
					<div class="col-md-2">
						<input type="text" id="number" name="number" value="${MessageRecharge.number }"  class='form-control' maxlength="7"/>
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">操作密码</label>
					<div class="col-md-2">
						<input type="password" style="display:none">  <!-- 为了屏蔽掉浏览器记住密码后自动填充到下面的密码框中,加入这个隐藏域 -->
						<input type="password" style="display:none">
						<input type="password" id="operationPassword" name="operationPassword" value=""  class='form-control' maxlength="20"/>
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
					
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button"  id='messageRechargeAddBtn' class='btn btn-primary' value="${empty MessageRecharge ? '添加' : '修改' }" data-loading='稍候...' />
					</div>
				</div>
			</form>
	  </div>
	  
	  	<!-- 商家展示start -->
		  <div class="modal fade" id="messageRechargeStoreShowID">
			 <div class="modal-dialog modal-lg" style="width:1200px">
				  <div class="modal-content">
					    <div class="modal-header">
					      <button type="button" id="messageRechargeClose" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
					    </div>
					    <div class="modal-body">
							 <m:list title="商家列表" id="messageRechargeList_modelShow"
								listUrl="/messageRecharge/showStoreModel/list/list-data"
								 searchButtonId="messageRecharge_mode_search_btn">
								<div class="input-group" style="max-width: 800px;">
									<span class="input-group-addon">商家名称</span> 
									<input type="text"	name="modelStoreName" id="modelStoreName" class="form-control" placeholder="商家名称" style="width: 200px;">
								</div>
									<input style="margin-top: 5px;" type="button" id='messageRechargeInfoAddBtn' class='btn btn-primary' value="确定"/>		
							</m:list>
					    </div>
				  </div>
			</div>
		</div>
	  <!-- 商家展示end -->
	  
   </div>
</body>
</html>