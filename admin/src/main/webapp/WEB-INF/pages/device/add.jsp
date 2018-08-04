<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty device ? '添加' : '修改' }终端</title> 
<script type="text/javascript">

function callback(obj){
	$("#device_storeId").val(obj.name);
	$("#storeId").val(obj.id);
}

$(document).ready(function(){
	$("#device_storeId").click(function (){
		$("#deviceShowStore").modal();
	});
	
	
	// 保存商品库存信息
	$("#deviceAddBtn").click(function(){
		var storeId = $("#storeId").val(); // 店铺ID
		var deviceSn = $("#deviceSn").val(); // 设备编号
		var deviceName = $("#deviceName").val(); // 设备名称
		deviceSn = $.trim(deviceSn);
		deviceName = $.trim(deviceName);
		if( storeId == "" || storeId == null ){
			lm.alert("商家不能为空!");
			return;
		}
		
		if( deviceSn == "" || deviceSn == null ){
			lm.alert("终端编号不能为空!");
			return;
		}

		if( deviceName == "" || deviceName == null ){
			lm.alert("终端名称不能为空！");
			return;
		}
		
		var id  = $("#id").val(); // 记录ID
		
		var isRepeat = false;
		lm.postSync("${contextPath }/device/checkDeviceId/", {id:id,deviceSn:deviceSn}, function(data) {
			if (data == 1) {
				$("#error").text("终端编号重复");
				isRepeat = true;
			} 
		});
		
		if (isRepeat) {
			return;
		}

		var serialId = $("#serialId").val(); // 终端系统的唯一标识
		serialId = $.trim(serialId);
		var flag = false;
		lm.postSync("${contextPath }/device/checkSerialId/", {id:id,serialId:serialId}, function(data) {
			if (data == 1) {
				$("#serialIdError").text("系统序列号重复");
				flag = true;
			} 
		});
		if(!flag && !isRepeat){
			$("#deviceAddForm").submit();
		}
	}); 
	
});

</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>${empty device ? '添加' : '修改' }终端
			</strong>
		</div>
		<div class='panel-body'>
			<form id="deviceAddForm" method='post' class='form-horizontal' repeatSubmit='1'
				action="${contextPath }/device/save">
			
				<!-- 修改时传过来的设备ID -->
				<input id="id" name="id" type="hidden" value="${device.id }" />
				<input id="storeId" name="storeId" type="hidden" value="${device.storeId }" />	
				<input id="deviceIdBack" name="deviceIdBack" type="hidden" value="${device.deviceSn }" />	 

				<div class="form-group">
					<label class="col-md-1 control-label">商家</label>
					<div class="col-md-2">
						<input type="text" name = "storeName"  readonly="readonly" class='form-control' id = "device_storeId"  value="${device.storeName }"  >
					</div>
				</div>
								
				<div class="form-group">
					<label class="col-md-1 control-label">SN编号</label>
					<div class="col-md-2">
						<input type="text" id="deviceSn" name="deviceSn" value="${device.deviceSn }"  class='form-control' maxlength="18"/>
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" id="error"></label>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">终端名称</label>
					<div class="col-md-2">
						<input type='text' id="deviceName" name="deviceName" class='form-control' value="${device.deviceName }" maxlength="10"/>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">终端厂商</label>
					<div class="col-md-2">
						<input type='text' id="factory" name="factory" class='form-control' value="${device.factory }" maxlength="10"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">系统序列号</label>
					<div class="col-md-2">
						<input type='text' id="serialId" name="serialId" class='form-control' value="${device.serialId }" maxlength="20"/>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" id="serialIdError"></label>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">是否启用</label>
					<div class="col-md-2">
						<m:slider inputName="status" defaultStatus="${device.status }"></m:slider>
					</div>
				</div>
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button"  id='deviceAddBtn' class='btn btn-primary' repeatSubmit='1'
							value="${empty device ? '添加' : '修改' }" data-loading='稍候...' />
					</div>
				</div>
				</form>
	  </div>
   </div>
   <!-- 店铺弹窗标签 -->
	<m:select_store path="${contextPath}/shop/showModle/list/list-data" modeId="deviceShowStore" callback="callback"> </m:select_store>
</body>
</html>