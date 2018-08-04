<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty partner ? '添加' : '修改' }合作者</title> 
<script type="text/javascript">

$(document).ready(function(){
	// 保存商品库存信息
	$("#partnerAddBtn").click(function(){
		var storeId = $("#storeId").val();
		storeId = $.trim(storeId);
		if( null == storeId || "" == storeId ){
			lm.alert("请选择总部");
			return;
		}
		
		var name = $("#name").val(); // 
		name = $.trim(name);
		
		if( name == "" || name == null ){
			lm.alert("名称不能为空!");
			return;
		}
	
		var isRepeat = false;
		var id = $("#id").val();
		lm.postSync("${contextPath }/partner/list/ajax/checkPartnerName/", {id:id,name:name}, function(data) {
			if (data == 1) {
				lm.alert("名称存在重复,请重新输入!");
				$("#name").focus();
				isRepeat = true;
			} 
		});
		
		if (isRepeat) {
			return;
		}
		
		var appKey = $("#appKey").val();
		appKey = $.trim(appKey);
		if( null == appKey || "" == appKey ){
			lm.alert("合作者appKey不能为空");
			return;
		}
		
		var token = $("#token").val();
		token = $.trim(token);
		if( null == token || "" == token ){
			lm.alert("token不能为空");
			return;
		}
		
		isRepeat = false;
		lm.postSync("${contextPath }/partner/list/ajax/checkPartnerToken/", {id:id,token:token}, function(data) {
			if (data == 1) {
				lm.alert("token存在重复,请重新输入!");
				$("#token").focus();
				isRepeat = true;
			} 
		});
		
		if (isRepeat) {
			return;
		}
		
		$("#partnerAddForm").submit();
	}); 
	
});

</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>${empty partner ? '添加' : '修改' }合作者
			</strong>
		</div>
		<div class='panel-body'>
			<form id="partnerAddForm" method='post' repeatSubmit='1' class='form-horizontal' action="${contextPath }/partner/save">
			<input type="hidden" name="id" class='form-control' id="id"  value="${partner.id }" maxlength="100"  >
				
				<div class="form-group">
					<label class="col-md-1 control-label">总部</label>
					<div class="col-md-2">
						<input name="storeName" id="storeName"  readonly="readonly" value="${partner.storeName }" class="form-control" data-remote="${contextPath }/partner/mainShop/showMode" data-toggle="modal" />
						<input type="hidden" name="storeId" id="storeId" value="${partner.storeId }" />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">名称</label>
					<div class="col-md-2">
						<input type="text" name="name" class='form-control' id="name" value="${partner.name }" maxlength="200"  >
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">合作者appKey</label>
					<div class="col-md-2">
						<input type="text" name="appKey" class='form-control' id="appKey" value="${partner.appKey }" maxlength="50"  >
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
					
				<div class="form-group">
					<label class="col-md-1 control-label">token</label>
					<div class="col-md-2">
						<input type="text" name="token" class='form-control' id="token" value="${partner.token }" maxlength="50"  >
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
							
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button"  id='partnerAddBtn' class='btn btn-primary' value="${empty partner ? '添加' : '修改' }" data-loading='稍候...' />
					</div>
				</div>
			</form>
	  </div>
   </div>
</body>
</html>