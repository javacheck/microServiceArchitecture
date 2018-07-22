<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>上传商品图片</title> 
<script type="text/javascript">

//初始化事件
$(function() {
	//绑定 提交方法
	$("#imageUploadButton").click(function() {
		var productStockId=$("#productStockId").val();
		var imageFile=$("#imageFile1").val();
		var type = imageFile.substring(imageFile.lastIndexOf(".") + 1, imageFile.length).toLowerCase();  
		if (type != "jpg" && type != "bmp" && type != "gif" && type != "png") {  
			 lm.alert("请上传正确的图片格式");            
			 return false;            
		}
	    $("#imageUpload").submit(); 
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
			<form id="imageUpload" method='post' class='form-horizontal' action="${contextPath }/productStock/saveImage" enctype="multipart/form-data">
				<input type='hidden' id="productStockId" name="productStockId" value="${id}" class='form-control' />
				<div class="form-group">
					<label class="col-md-2 control-label">上传第一张图片</label>
					<div class="col-md-4">
						<input type='file' id="imageFile1" name="imageFile" value="" class='form-control' />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">上传第二张图片</label>
					<div class="col-md-4">
						<input type='file' id="imageFile2" name="imageFile" value="" class='form-control' />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">上传第三张图片</label>
					<div class="col-md-4">
						<input type='file' id="imageFile3" name="imageFile" value="" class='form-control' />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">上传第四张图片</label>
					<div class="col-md-4">
						<input type='file' id="imageFile4" name="imageFile" value="" class='form-control' />
					</div>
				</div>
				<div class="form-group">
					<div class="col-md-offset-2 col-md-1 0">
							<input type='button' id='imageUploadButton' class='btn btn-primary' value="上传"  onclick="submitExcel()"/>
					</div>
				</div>
			</form>
		</div>
	</div>

</body>
</html>