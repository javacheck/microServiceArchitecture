<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty supermarketAd ? '添加' : '修改' }商超广告</title> 
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
	$("#supermarketAd_storeName").val(obj.name);
	$("#supermarketAd_storeId").val(obj.id);
	var storeId = $("#supermarketAd_storeId").val();
	if( null == storeId || "" == storeId ){
		return ;
	}
	lm.post("${contextPath}/supermarketAd/list/ajax/productSystemCategory",{type:1,storeId:storeId},function(data){
		$("#productCategoryId option").remove();
		if(null != data && "" != data){
			for(var i=0;i<data.length;i++){
	    		 $("#productCategoryId").append('<option id="' + data[i].id + '" value="' + data[i].id + '">' + data[i].name + '</option>');
			}
		}
	});
}

$(document).ready(function(){
	
	new uploadPreview({
		UpBtn : "supermarketImageId",
		DivShow : "superMarketAdImagediv",
		ImgShow : "showSuperMarketAdImage",
		Width : 330,
		height : 220
	});
	
	if( null != $("#id").val() && "" != $("#id").val() ){ // 修改
		
		// 展示已经添加了的图片
		var imageId = $("#imageId");
		if( undefined != imageId && null != imageId.val() && "" != imageId.val() ){
			$("#superMarketAdImagediv").show();
			$("#showSuperMarketAdImage").attr("src","${picUrl }"+imageId.val());
		}
		$("#productCategoryId").val('${supermarketAd.productCategoryId}'); // 展示商品分类
		$("#position").val('${supermarketAd.position}'); // 展示广告位置
	}
	
	// 点击弹出商家选择窗口
	$("#supermarketAd_storeName").click(function (){
		$("#supermarketAdShowStore").modal();
	});
	
	// 模拟点击
   $("[name = upSuperMarketAdImage]").bind("click", function () {
	   $("[name = supermarketImageId]").click();
   });
   
	// 图片赋值
   $("[name = supermarketImageId]").bind("change", function () {
	   var fileName = $("#supermarketImageId").val();
	   if( "" == fileName ){
		   $("#imageIdCache").val("");
		   $("#superMarketAdImagediv").hide();
	   } else {
		   $("#imageIdCache").val(fileName);
		   $("#superMarketAdImagediv").show();
	   }
   })
      
   // 提交事件
   $("#supermarketAdAddBtn").click(function(){
	   var storeId = $("#supermarketAd_storeId").val(); // 店铺ID
	   storeId = $.trim(storeId);
	   if( null == storeId || "" == storeId){
		   lm.alert("商家不能为空");
		   return ;
	   }
	   
	   var productCategoryId = $("#productCategoryId").val();
	   if( null == productCategoryId || "" == productCategoryId ){
		   lm.alert("商品分类不能为空");
		   return ;
	   }
	   var flag = false;
	   var id = $("#id").val();
	   lm.postSync("${contextPath}/supermarketAd/list/ajax/checkDataHave",{id:id,position:0,storeId:storeId,productCategoryId:productCategoryId},function(data){
			if(data > 0 ){
				lm.alert("同一个商品类别只能添加一个广告");
				flag = true;
				return ;
			}
		});
	   
	   if(flag){
		  return ; 
	   }
	   
	   var imageId = $("#imageIdCache").val(); // 图片
	   imageId = $.trim(imageId);
	   if( null == imageId || "" == imageId){
		   lm.alert("图片不能为空");
		   return ;
	   }
	   
	   $("#supermarketAdAddForm").submit();
   });
	   
});

function imageLoad(obj){
	$("#superMarketAdImagediv").show();
	$("#divImageShow").removeClass("col-md-2");
	if(obj.offsetHeight < 174 || obj.offsetWidth < 174  || obj.offsetHeight*174 != obj.offsetWidth*174){
		obj.src="";
		lm.alert("图片规格错误");
		$("#superMarketAdImagediv").hide();
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
				<i class='icon-plust'></i>${empty supermarketAd ? '添加' : '修改' }商超广告
			</strong>
		</div>
		<div class='panel-body'>
			<form id="supermarketAdAddForm" method='post' class='form-horizontal' action="${contextPath }/supermarketAd/save" enctype="multipart/form-data">
			
				<!-- 修改时传过来的设备ID -->
				<input id="id" name="id" type="hidden" value="${supermarketAd.id }" />
				<input id="type" name="type" type="hidden" value="1" />

				<div class="form-group">
					<label class="col-md-1 control-label">商家</label>
					<div class="col-md-2">
						<input type="text" name = "storeName"  readonly="readonly" class='form-control' id = "supermarketAd_storeName"  value="${supermarketAd.storeName }"  >
						<input type="hidden" name = "storeId"  readonly="readonly" class='form-control' id = "supermarketAd_storeId"  value="${supermarketAd.storeId  }"  >
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">商品分类</label>
					<div class="col-md-2">
						<select id="productCategoryId" class="form-control"  name="productCategoryId">
							<c:forEach items="${categoryList }" var="category" >
								<option value ="${category.id}">${category.name}</option>
							</c:forEach>
						</select>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
				
				<div class="form-group" style="display: none">
					<label class="col-md-1 control-label">广告位置</label>
					<div class="col-md-2">
						<select id="position" class="form-control"  name="position">
			           		<option id="first" value="0" selected="selected">主广告位</option>
			           		<option id="last" value="1">次广告位</option>
						</select>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">上传图片</label>
					<div class="col-md-2" id="divImageShow">
						<input type="button" id="upSuperMarketAdImage" class="btn btn-large btn-block btn-default" name="upSuperMarketAdImage" value="上传图片"/>&nbsp;&nbsp;
						<input type="file" id="supermarketImageId" style="width: 1px; height:1px"name="supermarketImageId" class="file"  value="上传图片" title="上传图片"/>
						<input type='hidden' id="imageId" name="imageId" class='form-control' value="${supermarketAd.imageId }"/>
						<input type="hidden" id="imageIdCache" name="imageIdCache" value="${supermarketAd.picURL }"/>
						<div id="superMarketAdImagediv">
							<img alt="图片展示" style="cursor: pointer;" id="showSuperMarketAdImage" name="showSuperMarketAdImage" src="" onload="imageLoad(this);">
						</div>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*(174*174)</label>
				</div>
			
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button"  id='supermarketAdAddBtn' class='btn btn-primary' value="${empty supermarketAd ? '添加' : '修改' }" data-loading='稍候...' />
					</div>
				</div>
			</form>
	  </div>
   </div>
   <!-- 店铺弹窗标签 -->
	<m:select_store path="${contextPath}/supermarketAd/showModel/list/list-data" modeId="supermarketAdShowStore" callback="callback"> </m:select_store>
</body>
</html>