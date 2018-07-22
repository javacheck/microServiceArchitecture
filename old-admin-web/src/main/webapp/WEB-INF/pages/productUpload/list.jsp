<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
		var zipOrRarFile = $("#zipOrRarFile").val();
	    if(zipOrRarFile=='') {
	    	lm.alert("请选择需上传的文件!");
	    	return false;
	    }
	    /* if(zipOrRarFile.indexOf('.zip')==-1 || zipOrRarFile.indexOf('.rar')==-1){
	    	lm.alert("文件格式不正确，请选择正确的zip文件(后缀名.zip)或rar文件(后缀名.rar)！");
	    	return false;
	    } */
	    $("#fileUpload").submit();
	});
});
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>商品导入</strong>
		</div>
		<div class='panel-body'>
			<div class='form-horizontal'>
			<div class="form-group">
				<label class="col-md-2 control-label">下载模板</label>
				<div class="col-md-4">
					<a href="${staticPath }/exel/modle.rar" class='btn btn-primary'>下载</a>
				</div>
			</div>
			</div>
			<form id="fileUpload" method='post' class='form-horizontal' action="${contextPath }/productUpload/save1" enctype="multipart/form-data">
				<div class="form-group">
					<label class="col-md-2 control-label">上传文件</label>
					<div class="col-md-4">
						<input type='file' id="zipOrRarFile" name="zipOrRarFile" value="" class='form-control' />
					</div>
				</div>
		
				<div class="form-group">
					<div class="col-md-offset-2 col-md-1 0">
							<input type='button' id='fileUploadButton' class='btn btn-primary' value="上传"  onclick="submitExcel()"/>
					</div>
				</div>
			</form>
		</div>
	</div>

</body>
</html>