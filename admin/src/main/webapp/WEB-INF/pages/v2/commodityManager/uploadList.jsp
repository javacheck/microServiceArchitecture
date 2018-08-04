<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品导入</title> 
<script type="text/javascript">

//初始化事件
$(function() {
	
	//绑定 提交方法
	$("#fileUploadButton").click(function() {
		$("#tipDIV1").html("");
		$("#tipDIV").html("");
		
		var userExcelFile = $("#userExcelFile").val();
		userExcelFile = $.trim(userExcelFile);
	    if( null == userExcelFile || "" == userExcelFile ) {
	    	lm.alert("请选择需上传的文件!");
	    	return false;
	    }
	    if( userExcelFile.indexOf('.xls') == -1 ){
	    	lm.alert("文件类型不正确，请选择正确的xls文件(后缀名.xls)！");
	    	return false;
	    }
	    	
		$("#fileUploadButton").prop("disabled","disabled");
		//lm.loading();
	    $("#fileUpload").submit();
	});
});

function getExe() {
	window.open("${contextPath}/commodityManager/list/ajax/exportModelExcel");
}
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>商品导入</strong>
		</div>
		<div class='panel-body'>
			<div class='form-horizontal'>
			</div>
			<iframe id="iframform" name="iframform" style="display: none;"></iframe>
				<c:if test="${not empty uploadResult }">
					<div class="panel panel-warning" style="overflow:scroll;max-height: 400px">
						<div class="panel-heading">
		    				${uploadResult }
		  				</div>
	  				</div>
	  				<br/>
  				</c:if>
			<form id="fileUpload" method='post' class='form-horizontal' action="${contextPath }/commodityManager/uploadProductExcel" enctype="multipart/form-data">
				<div  class="form-group">
					<label class="col-md-1 control-label">下载Excel模板</label>
					<div class="col-md-2">
						<a  onclick="getExe();" class="btn btn-small btn-danger">下载Excel模板</a>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label">导入</label>
					<div class="col-md-2">
						<input type='file' id="userExcelFile" name="userExcelFile" value="" class='form-control' />
					</div>
					<div class="col-md-2">
							<input type='button' id='fileUploadButton' class='btn btn-primary' value="上传" />
					</div>
				</div>
				<div class="form-group" >
					<label class="col-md-1 control-label"></label>
					<div class="col-md-6">
					 	<span style="color:red" id="tipDIV"></span>
					 	<span style="color:green" id="tipDIV1"></span>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>