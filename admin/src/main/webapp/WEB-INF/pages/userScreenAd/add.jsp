<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加客屏广告</title> 
<script src="${contextPath}/static/js/uploadPreview.js" type="text/javascript"></script>
<style type="text/css">
input.file{
    vertical-align:middle;
    position:relative;
    left:-218px;
    filter:alpha(opacity=0);
    opacity:0;
	z-index:1;
	*width:223px;
}
</style>
<script type="text/javascript">


$(document).ready(function(){
	new uploadPreview({
		UpBtn : "htmlImageId",
		DivShow : "userScreenAdImagediv",
		ImgShow : "showUserScreenAdImage",
		Width : 330,
		height : 220
	});
	
	// 模拟点击
   $("[name = upuserScreenAdImage]").bind("click", function () {
	   $("[name = htmlImageId]").click();
   });
   
// 图片赋值
   $("[name = htmlImageId]").bind("change", function () {
	   var fileName = $("#htmlImageId").val();
	   if( "" == fileName ){
		   $("#imageIdCache").val("");
		   $("#userScreenAdImagediv").hide();
	   } else {
		   $("#imageIdCache").val(fileName);
		   $("#userScreenAdImagediv").show();
	   }
   })
      
   // 提交事件
   $("#userScreenAdAddBtn").click(function(){
	   var imageId = $("#imageIdCache").val(); // 客屏广告
	   if( null == imageId || "" == imageId){
		   lm.alert("客屏广告图片不能为空！");
		   return ;
	   }
	   $("#userScreenAdAddBtn").prop("disabled",true);
	   $("#userScreenAdAddForm").submit();
   });
	   
});

</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>添加客屏广告
			</strong>
		</div>
		<div class='panel-body'>
			<form id="userScreenAdAddForm" method='post' class='form-horizontal' action="${contextPath }/userScreenAd/edit" enctype="multipart/form-data">
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>上传客屏广告图片</label>
					<div class="col-md-2">
						<input type="button" id="upuserScreenAdImage" class="btn btn-large btn-block btn-default" name="upuserScreenAdImage" value="上传客屏广告图片"/>&nbsp;&nbsp;
						<input type="file" id="htmlImageId" style="width: 1px; height:1px"name="htmlImageId" class="file"  value="上传客屏广告图片" title="上传客屏广告图片"/>
						<input type='hidden' id="imageId" name="imageId" class='form-control' value="${userScreenAd.imageId }"/>
						<input type="hidden" id="imageIdCache" name="imageIdCache" value="${userScreenAd.picUrl }"/>
						<div id="userScreenAdImagediv">
							<img alt="图片展示"  id="showUserScreenAdImage" name="showUserScreenAdImage" src="" >
						</div>
					</div>
				</div>
			
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button"  id='userScreenAdAddBtn' class='btn btn-primary' value="添加" data-loading='稍候...' />
					</div>
				</div>
			</form>
	  </div>
   </div>
</body>
</html>