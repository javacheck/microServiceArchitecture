<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty htmlAd ? '添加' : '修改' }广告</title> 
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
	$("#htmlAd_storeName").val(obj.name);
	$("#htmlAd_storeId").val(obj.id);
}

$(document).ready(function(){
	$("#cancelStoreBtn").click(function(){
		$("[name='storeId']").val('');
		$("[name='storeName']").val('');
	});
	new uploadPreview({
		UpBtn : "htmlImageId",
		DivShow : "htmlAdImagediv",
		ImgShow : "showhtmlAdImage",
		Width : 330,
		height : 220
	});
	
	if( null != $("#id").val() && "" != $("#id").val() ){ // 修改
		var share="${htmlAd.share}";
		if(share==0){
			$("#share").attr("checked",false);
		}
		var valid="${htmlAd.valid}";
		if(valid==1){
			 $("input[name='valid']").eq(1).prop("checked",true);
		}
		// 展示已经添加了的图片
		var imageId = $("#imageId");
		if( undefined != imageId && null != imageId.val() && "" != imageId.val() ){
			$("#htmlAdImagediv").show();
			$("#showhtmlAdImage").attr("src","${picUrl }"+imageId.val() );
		}
	}
	
	// 点击弹出商家选择窗口
	$("#htmlAd_storeName").click(function (){
		$("#htmlAdShowStore").modal();
	});
	
	// 模拟点击
   $("[name = uphtmlAdImage]").bind("click", function () {
	   $("[name = htmlImageId]").click();
   });
   
// 图片赋值
   $("[name = htmlImageId]").bind("change", function () {
	   var fileName = $("#htmlImageId").val();
	   if( "" == fileName ){
		   $("#imageIdCache").val("");
		   $("#htmlAdImagediv").hide();
	   } else {
		   $("#imageIdCache").val(fileName);
		   $("#htmlAdImagediv").show();
	   }
   })
      
   // 提交事件
   $("#htmlAdAddBtn").click(function(){
	   var title=$("[name='title']").val();
	   title = $.trim(title);
	   if( null == title || "" == title){
		   lm.alert("广告标题不能为空！");
		   $("[name='title']").focus();
		   return ;
	   }
	   var imageId = $("#imageIdCache").val(); // 图片
	   if( null == imageId || "" == imageId){
		   lm.alert("LOGO不能为空！");
		   return ;
	   }
	   $("#htmlAdAddBtn").prop("disabled",true);
	   $("#htmlAdAddForm").submit();
   });
	   
});
function IsURL(str_url){ 
	var strRegex = '^((https|http|ftp|rtsp|mms)?://)' 
	+ '?(([0-9a-z_!~*\'().&=+$%-]+: )?[0-9a-z_!~*\'().&=+$%-]+@)?' //ftp的user@ 
	+ '(([0-9]{1,3}.){3}[0-9]{1,3}' // IP形式的URL- 199.194.52.184 
	+ '|' // 允许IP和DOMAIN（域名） 
	+ '([0-9a-z_!~*\'()-]+.)*' // 域名- www. 
	+ '([0-9a-z][0-9a-z-]{0,61})?[0-9a-z].' // 二级域名 
	+ '[a-z]{2,6})' // first level domain- .com or .museum 
	+ '(:[0-9]{1,4})?' // 端口- :80 
	+ '((/?)|' // a slash isn't required if there is no file name 
	+ '(/[0-9a-z_!~*\'().;?:@&=+$,%#-]+)+/?)$'; 
	var re=new RegExp(strRegex); 
	//re.test() 
	if (re.test(str_url)){ 
		return true; 
	}else{ 
		return false; 
	} 
} 
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>${empty htmlAd ? '添加' : '修改' }广告
			</strong>
		</div>
		<div class='panel-body'>
			<form id="htmlAdAddForm" method='post' class='form-horizontal' action="${contextPath }/htmlAd/edit" enctype="multipart/form-data">
			
				<!-- 修改时传过来的设备ID -->
				<input id="id" name="id" type="hidden" value="${htmlAd.id }" />

				<div class="form-group">
					<label class="col-md-1 control-label">商家</label>
					<div class="col-md-2">
						<input type="text" name = "storeName"  readonly="readonly" class='form-control' id = "htmlAd_storeName"  value="${htmlAd.storeName }"  >
						<input type="hidden" name = "storeId"  readonly="readonly" class='form-control' id = "htmlAd_storeId"  value="${htmlAd.storeId  }"  >
					</div>
					<button type='button' class="btn btn-warning" id="cancelStoreBtn">取消商家</button>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>广告标题</label>
					<div class="col-md-2">
						<input type='text' name="title" value="${htmlAd.title }" class='form-control'   maxlength="120"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label">广告URL</label>
					<div class="col-md-2">
						<input type='text' name="url" value="${htmlAd.url }" class='form-control'   maxlength="120"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label">城市地址</label>
					<div class="col-md-3">
						<m:selectArea inputName="areaId" path="${htmlAd.areaPath }"></m:selectArea>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">是否分享</label>
					<div class="col-md-2">
						<input id="share" type="checkbox" name="share" checked="checked" value="1" style="margin-top: 8px;"/>分享
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label">是否有效</label>
					<div class="col-md-2" style="margin-top: 3px;">
						<input id="account_type_0"  type='radio' value="0" name="valid" checked="checked"/>有效
						<input id="account_type_1"  type='radio' value="1" name="valid"/>无效
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>上传LOGO</label>
					<div class="col-md-2">
						<input type="button" id="uphtmlAdImage" class="btn btn-large btn-block btn-default" name="uphtmlAdImage" value="上传LOGO"/>&nbsp;&nbsp;
						<input type="file" id="htmlImageId" style="width: 1px; height:1px"name="htmlImageId" class="file"  value="上传LOGO" title="上传LOGO"/>
						<input type='hidden' id="imageId" name="imageId" class='form-control' value="${htmlAd.imageId }"/>
						<input type="hidden" id="imageIdCache" name="imageIdCache" value="${htmlAd.picUrl }"/>
						<div id="htmlAdImagediv">
							<img alt="图片展示"  id="showhtmlAdImage" name="showhtmlAdImage" src="" >
						</div>
					</div>
				</div>
			
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button"  id='htmlAdAddBtn' class='btn btn-primary' value="${empty htmlAd ? '添加' : '修改' }" data-loading='稍候...' />
					</div>
				</div>
			</form>
	  </div>
   </div>
   <!-- 店铺弹窗标签 -->
	<m:select_store path="${contextPath}/htmlAd/showModel/list/list-data" modeId="htmlAdShowStore" callback="callback"> </m:select_store>
</body>
</html>