<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>盘点表格导入</title>
<style type="text/css">
</style>
<script type="text/javascript">
//初始化事件
$(function() {
	//绑定上传文件按钮
	$("[name = fileBtn]").bind("click", function() {
		$("#stockCheckFile").click();
	});
	
	//绑定 提交方法
	$("#fileUploadButton").click(function() {
		$("#tipDIV").html("");
		$("#tipDIV1").html("");
		var fileId=$("#fileId").val();
		if(fileId == null || fileId == ""){
			lm.alert("上传文件不能为空！");
			$("#fileBtn").focus();
			return;
		}
		$("#cancelButton").prop("disabled","disabled");
		$("#fileUploadButton").prop("disabled","disabled");
		lm.loading();
		$("#tipDIV1").html("正在导入中，请耐心等待！")
	    $("#fileUpload").submit();
	});
});

function cancel(){
	$("#uploadModalBtn").click();
}
function uploadFile(obj) {
	if (obj.value != null && obj.value != '') {
		 if(obj.value.indexOf('.xls')==-1){
			lm.alert("上传应用格式不正确，请选择正确的excel文件(后缀名.xls)！");
			$("#fileId").val("");
	    	$("#fileBtn").focus();
    		return false;
		 }else{
			$("#fileId").val(obj.value);
			$("#fileBtn").show();
		 }
	}
}
</script>
</head>
<body>
	<div class="modal-dialog modal-me">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" id="uploadModalBtn"> 
					<!-- <span aria-hidden="true">×</span><span class="sr-only">关闭</span> -->
				</button>
				<h4 class="modal-title">盘点表格导入</h4>
			</div>
				<form id="fileUpload"  method="post" class="form-horizontal" action="${contextPath }/stockCheck/uploadStockCheck" enctype="multipart/form-data">
					<div class='form-horizontal'>
						<div class="form-group">
							<label class="col-md-1 control-label"></label>
						</div>
						<div class="form-group">
							<label class="col-md-1 control-label"></label>
						</div>
						<div class="form-group">
							<label class="col-md-1 control-label"></label>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">上传路径：</label>
							<div class="col-md-4">
								<input type="file" id="stockCheckFile" style="width:1px;height:1px" name="stockCheckFile" class="file" onchange="uploadFile(this)"/>
								<input type="text" name="fileId" id="fileId" readonly="readonly" value="" class="form-control"/>
							</div>
							<div class="col-md-2">
								<input type="button" id="fileBtn" class="btn btn-large btn-block btn-default" name="fileBtn" value="选择文件"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-1 control-label"></label>
						</div>
						<div class="form-group">
							<label class="col-md-1 control-label"></label>
							<div class="col-md-6">
							 	<span style="color:red" id="tipDIV"></span>
							 	<span style="color:green" id="tipDIV1"></span>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-1 control-label"></label>
						</div>
					</div>
				</form>
				<div class='text-center' >
					<button type="button"  name="cancelButton" onclick="cancel();" class="btn btn-primary ">取消</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<button type="button"  id="fileUploadButton" name="ileUploadButton" onclick="typeChage();" class="btn btn-warning">提交上传</button>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label"></label>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label"></label>
				</div>
		</div>
	</div>
</body>
</html>