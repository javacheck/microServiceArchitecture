<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>上传商品图片</title> 
<script type="text/javascript">
function isIE() {
	if (!!window.ActiveXObject || "ActiveXObject" in window){
		return true;
	} else{
		return false;
	}
}
function is360(){
	if(window.external&&window.external.twGetRunPath&&window.external.twGetRunPath().toLowerCase().indexOf("360se")>-1){ 
		return true;
	}else{
		return false;
	}
}
//初始化事件
$(function() {
	var num=0;
	var action="${action}";
	if(action!=null && action!=''){
		if(action=='success'){
			lm.alert("图片上传成功!");
		}else{
			lm.alert("图片<span style='color:red;'>"+action+"</span>已存在！");
		}
	}
	//绑定 提交方法
	$("#imageUploadButton").click(function() {
		var productStockId=$("#productStockId").val();
		var flag=true;
	 	$("input[name='imageFile']").each(function(index,item){
			 var imageFile=$(this).val();
			 if(imageFile!=""){
				 num++;
			 }
			 if(imageFile!=null && imageFile!=''){
				 var type = imageFile.substring(imageFile.lastIndexOf(".") + 1, imageFile.length).toLowerCase(); 
				 if (type != "jpg" && type != "bmp" && type != "gif" && type != "png") {  
					 lm.alert("请上传正确格式的图片");  
					 flag=false;
					 return false;
				 }
				 if (imageFile.indexOf('\\')>0){
					 var name=imageFile.substring(imageFile.lastIndexOf("\\")+1,imageFile.lastIndexOf("."));
					 if (!/^(\+|-)?\d+($|\.\d+$)/.test(name)){
						 lm.alert("图片名称必须为数字"); 
						 flag=false;
						 return false;
					 }   
				 }else{
					 var name=imageFile.substring(0,imageFile.lastIndexOf("."));
					 if (!/^(\+|-)?\d+($|\.\d+$)/.test(name)){
						 lm.alert("图片名称必须为数字"); 
						 flag=false;
						 return false;
					 }   
				 }
			 }
		});
	 	if(flag==false){
	 		return;
	 	}
	 	if(num==0){
	 		lm.alert("最少要上传一张图片！");
	 		return;
	 	}
	    $("#imageUpload").submit(); 
	});
});
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>上传商品图片</strong>
		</div>
		<input type='hidden' id="flag" name="flag" value="${flag}" class='form-control' />
		<div class='panel-body'>
			<form id="imageUpload" method='post' class='form-horizontal' action="${contextPath }/productUpload/saveImage" enctype="multipart/form-data">
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
				<div class="form-group" id="tipDIV">
					<label class="col-md-1 control-label"></label>
					<div class="col-md-6">
					 	<span style="color:red">温馨提示：上传图片必须是jpg，bmp，gif，png，而且图片名称必须为数字</span> 
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