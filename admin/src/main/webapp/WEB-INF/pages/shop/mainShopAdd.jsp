<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty store ? '添加' : '修改' }总部</title> 
<script type="text/javascript">

$(document).ready(function(){
	// 保存商品库存信息
	$("#mainShopAddBtn").click(function(){
		var name = $("#name").val(); // 
		name = $.trim(name);
		
		if( name == "" || name == null ){
			lm.alert("名称不能为空!");
			return;
		}
		
		var isRepeat = false;
		var id = $("#id").val();
		lm.postSync("${contextPath }/shop/mainShop/list/ajax/checkName/", {id:id,name:name}, function(data) {
			if (data == 1) {
				lm.alert("名称存在重复,请重新输入!");
				$("#name").focus();
				isRepeat = true;
			} 
		});
		
		if (isRepeat) {
			return;
		}
		
		var agentId = $("#agentId").val();
		agentId = $.trim(agentId);
		if( agentId == "" || agentId == null ){
			lm.alert("代理商名称不能为空!");
			return;
		}
		
		var storeAcountName = $("#storeAcountName").val();
		storeAcountName = $.trim(storeAcountName);
		if( storeAcountName == null || "" == storeAcountName){
			lm.alert("登录账号不能为空");
			return ;
		}
		
		var flag = true;
		if( null != id && "" != id ){
			var storeAcountCache = $("#storeAcountCache").val();
			storeAcountCache = $.trim(storeAcountCache);
			if( storeAcountName == storeAcountCache ){
				flag = false;
			}
		}
		if(flag){
			lm.postSync("${contextPath }/shop/checkmobile/", {mobile:storeAcountName}, function(data) {
				if (data == 1) {
					lm.alert("登录账号存在重复！");
					flag = false;
				} 
			});
			
			if(!flag){
				 return false;
			 } 
		}
		
		
		
		var storeAcountPassWord = $("#storeAcountPassWord").val();
		storeAcountPassWord = $.trim(storeAcountPassWord);
		if( storeAcountPassWord == null || "" == storeAcountPassWord ){
			if(null == id || "" == id){
				lm.alert("登录密码不能为空");
				return ;				
			}
		} else {
			if( storeAcountPassWord.length < 6 || storeAcountPassWord.length > 19 ){
				lm.alert("登录密码长度不能小于6位且不能大于18位");
				return ; // 长度限制
			}
			if( !(/([\w]){6,19}$/.test(storeAcountPassWord)) ){
				lm.alert("登录密码只能为英文或者数字！");
				return ; // 只能输入英文或者数字
			}
		}
		
		var mobile = $("#mobile").val();
		mobile = $.trim(mobile);
		if( mobile == null || "" == mobile ){
			lm.alert("手机号码不能为空");
			return ;
		}
		
		if(!lm.isMobile(mobile)){
			lm.alert("手机号码输入错误");
			return ;
		}
		
		$("#mainShopAddForm").submit();
	}); 
	
});


$(document).ready(function(){
	var id = $("#id").val();
	if( null != id && "" != id ){ // 修改
		$("#isShareUser").val('${store.isShareUser }');
		var storeAcountPassWord = $("#storeAcountPassWord");
		storeAcountPassWord.val("");
		storeAcountPassWord.attr("placeholder","不修改则为原密码");
	}
});
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>${empty store ? '添加' : '修改' }总部
			</strong>
		</div>
		<div class='panel-body'>
			<form id="mainShopAddForm" method='post' repeatSubmit='1' class='form-horizontal' action="${contextPath }/shop/mainShopSave">
			<input type="hidden" name="id" class='form-control' id="id"  value="${store.id }" maxlength="100"  >
				<input type="hidden" name="status" class='form-control' id="status"  value="0" >
				<input type="hidden" name="storeAcountCache" class='form-control' id="storeAcountCache"  value="${store.storeAcountName }" >
				<div class="form-group">
					<label class="col-md-1 control-label">名称</label>
					<div class="col-md-2">
						<input type="text" name="name" class='form-control' id="name" value="${store.name }" maxlength="200"  >
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">代理商名称</label>
					<div class="col-md-2">
						<input name="agentName" id="agentName"  readonly="readonly" value="${store.agentName }" class="form-control" isRequired="1" tipName="代理商名称" ignoreNull data-remote="${contextPath }/shop/agentList" data-toggle="modal" />
						<input type="hidden" name="agentId" id="agentId" value="${store.agentId }" />
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">登录账号</label>
					<div class="col-md-2">
						<input type="text" name="storeAcountName" class='form-control' id="storeAcountName" value="${store.storeAcountName }" maxlength="20"  >
						
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">登录密码</label>
					<div class="col-md-2">
						<input type="password" style="display:none"> <!-- 为了屏蔽掉浏览器记住密码后自动填充到下面的密码框中,加入这个隐藏域 -->
						<input type="password" style="display:none">
						<input type="password" name="storeAcountPassWord" class='form-control' id="storeAcountPassWord" value="${store.storeAcountPassWord }" maxlength="20"  >
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
						
				<div class="form-group">
					<label class="col-md-1 control-label">手机号码</label>
					<div class="col-md-2">
						<input type="text" name="mobile" class='form-control' id="mobile" value="${store.mobile }" maxlength="20"  >
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">是否用户共享</label>
					<div class="col-md-2">
						<select id="isShareUser" name="isShareUser" class='form-control'> 
							<option id="0" value="0" selected="selected">不共享</option>
							<option id="1" value="1">共享</option>
						</select>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px">*</label>&nbsp;&nbsp;
				</div>
						
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button"  id='mainShopAddBtn' class='btn btn-primary' value="${empty store ? '添加' : '修改' }" data-loading='稍候...' />
					</div>
				</div>
			</form>
	  </div>
   </div>
</body>
</html>