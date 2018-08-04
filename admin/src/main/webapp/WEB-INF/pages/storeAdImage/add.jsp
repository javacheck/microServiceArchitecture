<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty storeAdImage ? '添加' : '修改' }商广图片</title> 
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

function callback(obj){
	$("#storeAdImage_storeName").val(obj.name);
	$("#storeAdImage_storeId").val(obj.id);
}

$(document).ready(function(){
	
	new uploadPreview({
		UpBtn : "storeAdImageImageId",
		DivShow : "storeAdImageImagediv",
		ImgShow : "showstoreAdImageImage"
	});
	
	if( null != $("#cacheStoreId").val() && "" != $("#cacheStoreId").val() ){ // 修改
		
		// 展示已经添加了的图片
		var imageId = $("#imageId");
		if( undefined != imageId && null != imageId.val() && "" != imageId.val() ){
			$("#storeAdImageImagediv").show();
			$("#showstoreAdImageImage").attr("src","${picUrl }"+imageId.val());
		}
	}
	
	// 点击弹出商家选择窗口
	$("#storeAdImage_storeName").click(function (){
		$("#storeAdImageShowStore").modal();
	});
	
	// 模拟点击
   $("[name = upstoreAdImageImage]").bind("click", function () {
	   $("[name = storeAdImageImageId]").click();
   });
   
	// 图片赋值
   $("[name = storeAdImageImageId]").bind("change", function () {
	   var fileName = $("#storeAdImageImageId").val();
	   if( "" == fileName ){
		   $("#imageIdCache").val("");
		   $("#storeAdImageImagediv").hide();
	   } else {
		   $("#imageIdCache").val(fileName);
		   $("#storeAdImageImagediv").show();
	   }
   })
      
   // 提交事件
   $("#storeAdImageAddBtn").click(function(){
	   var storeId = $("#storeAdImage_storeId").val(); // 店铺ID
	   storeId = $.trim(storeId);
	   if( null == storeId || "" == storeId){
		   lm.alert("商家不能为空");
		   return ;
	   }
	   	   
	   var imageId = $("#imageIdCache").val(); // 图片
	   imageId = $.trim(imageId);
	   if( null == imageId || "" == imageId){
		   lm.alert("图片不能为空");
		   return ;
	   }
	   
	   $("#storeAdImageAddForm").submit();
   });
	   
});

function imageLoad(obj){
	$("#storeAdImageImagediv").show();
	$("#divImageShow").removeClass("col-md-2");
	if(obj.offsetHeight < 350 || obj.offsetWidth < 370 || obj.offsetWidth*350 != obj.offsetHeight*370 ){
		obj.src="";
		lm.alert("图片规格错误");
		$("#storeAdImageImagediv").hide();
		$("#imageIdCache").val("");
	} 
	$("#divImageShow").addClass("col-md-2");
}
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>${empty storeAdImage ? '添加' : '修改' }商广图片
			</strong>
		</div>
		<div class='panel-body'>
			<form id="storeAdImageAddForm" method='post' class='form-horizontal' action="${contextPath }/storeAdImage/save" enctype="multipart/form-data">
			
				<!-- 修改时传过来的 -->
				<input id="cacheStoreId" name="cacheStoreId" type="hidden" value="${storeAdImage.storeId }" />

				<div class="form-group">
					<label class="col-md-1 control-label">商家</label>
					<div class="col-md-2">
						<input type="text" name = "storeName"  readonly="readonly" class='form-control' id = "storeAdImage_storeName"  value="${storeAdImage.storeName }"  >
						<input type="hidden" name = "storeId"  readonly="readonly" class='form-control' id = "storeAdImage_storeId"  value="${storeAdImage.storeId  }"  >
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
								
				<div class="form-group" style="display: none">
					<label class="col-md-1 control-label">广告位置</label>
					<div class="col-md-2">
						<select id="type" class="form-control"  name="type">
			           		<option id="first" value="0" selected="selected">暂时</option>
						</select>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">上传图片</label>
					<div class="col-md-2" id="divImageShow">
						<input type="button" id="upstoreAdImageImage" class="btn btn-large btn-block btn-default" name="upstoreAdImageImage" value="上传图片"/>&nbsp;&nbsp;
						<input type="file" id="storeAdImageImageId" style="width: 1px; height:1px"name="storeAdImageImageId" class="file"  value="上传图片" title="上传图片"/>
						<input type='hidden' id="imageId" name="imageId" class='form-control' value="${storeAdImage.imageId }"/>
						<input type="hidden" id="imageIdCache" name="imageIdCache" value="${storeAdImage.picURL }"/>
						<div id="storeAdImageImagediv">
							<img alt="图片展示" style="cursor: pointer;" id="showstoreAdImageImage" name="showstoreAdImageImage" src="" onload="imageLoad(this);">
						</div>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*(370*350)</label>
				</div>
			
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button"  id='storeAdImageAddBtn' class='btn btn-primary' value="${empty storeAdImage ? '添加' : '修改' }" data-loading='稍候...' />
					</div>
				</div>
			</form>
	  </div>
   </div>
   <!-- 店铺弹窗标签 -->
	<m:select_store path="${contextPath}/storeAdImage/showModel/list/list-data" modeId="storeAdImageShowStore" callback="callback"> </m:select_store>
</body>
</html>