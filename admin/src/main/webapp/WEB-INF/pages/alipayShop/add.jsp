<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty alipayShop ? '添加' : '修改' }门店</title> 
  <link rel="stylesheet" href="http://cache.amap.com/lbs/static/main1119.css"/>
  <script src="http://cache.amap.com/lbs/static/es5.min.js"></script>
  <script src="http://webapi.amap.com/maps?v=1.3&key=25b9397d98b0006d87cae813196a8a4e&plugin=AMap.Autocomplete""></script>
  <script type="text/javascript" src="http://cache.amap.com/lbs/static/addToolbar.js"></script>
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

$(function(){
	new uploadPreview({
		UpBtn : "brandLogoTemp",
		DivShow : "brandLogoImagediv",
		ImgShow : "brandLogoImage"		
	});
	
    $("[name = upBrandLogo]").bind("click", function () {
	      $("[name = brandLogoTemp]").click();
    });
  
  	new uploadPreview({
		UpBtn : "mainImageTemp",
		DivShow : "mainImagelogoImagediv",
		ImgShow : "mainImagelogoImage"		
  	});
	
 	$("[name = upMainLogo]").bind("click", function () {
	   	$("[name = mainImageTemp]").click();
 	});

 	new uploadPreview({
		UpBtn : "windowImage",
		DivShow : "windowImagelogoImagediv",
		ImgShow : "windowImagelogoImage"		
  	});
	
 	$("[name = upWindowLogo]").bind("click", function () {
	   	$("[name = windowImage]").click();
 	});

 	new uploadPreview({
		UpBtn : "oneInternal",
		DivShow : "oneInternallogoImagediv",
		ImgShow : "oneInternallogoImage"		
  	});
	
 	$("[name = upOneInternalLogo]").bind("click", function () {
	   	$("[name = oneInternal]").click();
 	});
 	
 	new uploadPreview({
		UpBtn : "twoInternal",
		DivShow : "twoInternallogoImagediv",
		ImgShow : "twoInternallogoImage"		
  	});
	
 	$("[name = upTwoInternalLogo]").bind("click", function () {
	   	$("[name = twoInternal]").click();
 	});
 	
 	new uploadPreview({
		UpBtn : "licenceTemp",
		DivShow : "licencelogoImagediv",
		ImgShow : "licencelogoImage"		
  	});
	
 	$("[name = upLicenceLogo]").bind("click", function () {
	   	$("[name = licenceTemp]").click();
 	});

 	new uploadPreview({
		UpBtn : "businessCertificateTemp",
		DivShow : "businessCertificatelogoImagediv",
		ImgShow : "businessCertificatelogoImage"		
  	});
	
 	$("[name = upBusinessCertificateLogo]").bind("click", function () {
	   	$("[name = businessCertificateTemp]").click();
 	});
});


$(function(){
	$("#provinceCode").change(function(key){
		var provinceID = $("#provinceCode").val();
		lm.post("${contextPath }/alipayShop/ajax/getCityByProvinceID/", {provinceID:provinceID}, function(data) {
			$("#cityCode option").remove();
			$("#districtCode option").remove();
			var cityOption = "";
			for (var i = 0; i < data.length; i++) {
				cityOption += "<option id="+data[i].cityID+" value="+data[i].cityID+">"+data[i].cityName+"</option>";
			}
			$("#cityCode").html(cityOption);
		});
	});	
	
	$("#cityCode").change(function(key){
		var cityID = $("#cityCode").val();
		lm.post("${contextPath }/alipayShop/ajax/getDistrictByCityID/", {cityID:cityID}, function(data) {
			$("#districtCode option").remove();
			var districtOption = "";
			for (var i = 0; i < data.length; i++) {
				districtOption += "<option id="+data[i].districtID+" value="+data[i].districtID+">"+data[i].districtName+"</option>";
			}
			$("#districtCode").html(districtOption);
		});
	});	
});

$(function(){
	var format = "yyyy-MM-dd";//默认格式
	var now = lm.Now;//当前日期
	var nowFormat=now.Format(format);
	$("#businessCertificteExpires").datetimepicker({
		minView: "month", //选择日期后，不会再跳转去选择时分秒 
		format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
		language: 'zh-CN', //汉化 
		autoclose:true , //选择日期后自动关闭
		todayBtn:true,//可选择当天按钮
		todayHighlight:false//高亮当前日期
	 });
});

function imageLoad(obj){
	/**
	$("#logoImagediv").show();
	$("#divLogo").removeClass("col-md-2");
	
	if(obj.offsetHeight != 220 || obj.offsetWidth != 330 ){
		obj.src="";
		lm.alert("图片规格只能是330x220像素");
		$("#logoImagediv").hide();
		$("#logoFileCache").val("");
	} 
	$("#divLogo").addClass("col-md-2");
	*/
}

$(document).ready(function(){
	// 保存商品库存信息
	$("#alipayShopAddBtn").click(function(){
		var storeId = $("#storeId").val();
		storeId = $.trim(storeId);
		if( null == storeId || "" == storeId ){
			lm.alert("商家不能为空！");
			return;
		}
		 var brandName = $("#brandName").val(); // 品牌名称
		 brandName = $.trim(brandName);
		 if( null == brandName || "" == brandName ){
			 lm.alert("品牌名称不能为空!");
			 return;
		 }
		 
		 var mainShopName = $("#mainShopName").val(); // 门店名称
		 mainShopName = $.trim(mainShopName);
		 if( null == mainShopName || "" == mainShopName ){
			 lm.alert("门店名称不能为空!");
			 return;
		 }
		 
		 var provinceCode = $("#provinceCode option:selected").text(); // 省
		 provinceCode = $.trim(provinceCode);
		 if( null == provinceCode || "" == provinceCode ){
			 lm.alert("省份不能为空!");
			 return;
		 }
		 
		 var cityCode = $("#cityCode option:selected").text(); //  市
		 cityCode = $.trim(cityCode);
		 if( null == cityCode || "" == cityCode ){
			 lm.alert("市不能为空!");
			 return;
		 }
		 
		 var districtCode = $("#districtCode option:selected").text(); //  区
		 districtCode = $.trim(districtCode);
		 if( null == districtCode || "" == districtCode ){
			 lm.alert("区不能为空!");
			 return;
		 }
		 
		 var address = $("#address").val(); // 门店地址
		 address = $.trim(address);
		 if( null == address || "" == address ){
			 lm.alert("门店地址不能为空!");
			 return;
		 }
		 
		 var longitude = $("#longitude").val(); // 经度
		 longitude = $.trim(longitude);
		 if( null == longitude || "" == longitude ){
			 lm.alert("经度不能为空!");
			 return;
		 }
		 
		 var latitude = $("#latitude").val(); // 纬度
		 latitude = $.trim(latitude);
		 if( null == latitude || "" == latitude ){
			 lm.alert("纬度不能为空!");
			 return;
		 }
		 
		 var categoryId = $("#categoryId option:selected").text(); // 品类
		 categoryId = $.trim(categoryId);
		 if( null == categoryId || "" == categoryId ){
			 lm.alert("品类不能为空!");
			 return;
		 }
		 
		 var contactNumber = $("#contactNumber").val(); // 门店电话
		 contactNumber = $.trim(contactNumber);
		 if( null == contactNumber || "" == contactNumber ){
			 lm.alert("门店电话不能为空!");
			 return;
		 }
		 
		 var brandLogoTemp = $("#brandLogoTemp").val(); // 品牌LOGO
		 brandLogoTemp = $.trim(brandLogoTemp);
		 if( null == brandLogoTemp || "" == brandLogoTemp ){
			 lm.alert("品牌LOGO不能为空！");
			 return ;
		 }
		 var mainImageTemp = $("#mainImageTemp").val(); // 门店首图
		 mainImageTemp = $.trim(mainImageTemp);
		 if( null == mainImageTemp || "" == mainImageTemp ){
			 lm.alert("门店首图不能为空！");
			 return ;
		 }
		 
		 var windowImage = $("#windowImage").val(); // 门头照
		 windowImage = $.trim(windowImage);
		 if( null == windowImage || "" == windowImage ){
			 lm.alert("门头照不能为空！");
			 return ;
		 }
		 
		 var oneInternal = $("#oneInternal").val(); // 内景照一
		 oneInternal = $.trim(oneInternal);
		 if( null == oneInternal || "" == oneInternal ){
			 lm.alert("内景照一不能为空！");
			 return ;
		 }
		 var twoInternal = $("#twoInternal").val(); // 内景照二
		 twoInternal = $.trim(twoInternal);
		 if( null == twoInternal || "" == twoInternal ){
			 lm.alert("内景照二不能为空！");
			 return ;
		 }
		 var licenceTemp = $("#licenceTemp").val(); // 门店营业执照
		 licenceTemp = $.trim(licenceTemp);
		 if( null == licenceTemp || "" == licenceTemp ){
			 lm.alert("门店营业执照不能为空！");
			 return ;
		 }
		 
		 var licenceCode = $("#licenceCode").val(); // 门店营业执照编号
		 licenceCode = $.trim(licenceCode);
		 if( null == licenceCode || "" == licenceCode ){
			 lm.alert("门店营业执照编号不能为空!");
			 return;
		 }
		 
		 var licenceName = $("#licenceName").val(); // 门店营业执照名称
		 licenceName = $.trim(licenceName);
		 if( null == licenceName || "" == licenceName ){
			 lm.alert("门店营业执照名称不能为空!");
			 return;
		 }
		 
		$("#alipayShopAddForm").submit();
	}); 
});

$(function(){
	$("[name='storeName']").bind("click", function () {
		$('#storeAddModal').modal();
	});
});

function callback(){
	
	$("#shopListDataId").find("tbody tr").click(function(){
		$("#storeId").val($(this).attr("val"));
		$("#storeName").val(($($(this).find("td")[0]).html()));
		
		$("#shopListModalBtn").click();
	});
	
}
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>${empty alipayShop ? '添加' : '修改' }门店
			</strong>
		</div>
		<div class='panel-body'>
			<form id="alipayShopAddForm" method='post' class='form-horizontal' repeatSubmit='1' action="${contextPath }/alipayShop/save" enctype="multipart/form-data">
			
				<!-- 修改时传过来的设备ID -->
				<input id="id" name="id" type="hidden" value="${alipayShop.id }" />

				<div class="form-group" >
					<label class="col-md-1 control-label">商家<span style="color: red;font-size: 15px">*</span></label>
					<div class="col-md-2">
						<input name="storeName" id="storeName" readonly="readonly" value="" class="form-control" />
						<input type="hidden" name="storeId" id="storeId" value="${alipayShop.storeId }" />
					</div>
				</div>
								
				<div class="form-group">
					<label class="col-md-1 control-label">品牌名称<span style="color: red;font-size: 15px">*</span></label>
					<div class="col-md-2">
						<input type="text" name = "brandName" class='form-control' id = "brandName"  value="${alipayShop.brandName }" >
					</div>
				</div>
								
				<div class="form-group">
					<label class="col-md-1 control-label">门店名称<span style="color: red;font-size: 15px">*</span></label>
					<div class="col-md-2">
						<input type="text" id=mainShopName name="mainShopName" value="${alipayShop.mainShopName }"  class='form-control' maxlength="20"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">门店地址<span style="color: red;font-size: 15px">*</span></label>
					<div class="col-md-2">
						<select style="width: 82px" id="provinceCode" name="provinceCode" >
							<c:forEach items="${province }" var="pro">
								<option id="${pro.provinceID }" value="${pro.provinceID }">${pro.provinceName }</option>
							</c:forEach>
						</select>
						<select style="width: 82px" id="cityCode" name="cityCode">
						</select>
						<select style="width: 82px" id="districtCode" name="districtCode">
						</select>
						<input type='text' id="address" name="address" class='form-control' value="${alipayShop.address }" maxlength="50"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">点击地图确认经纬度<span style="color: red;font-size: 15px">*</span></label>
						<div id="container" style="width: 1000px;height: 500px;position:relative;">
						<div id="myPageTop" style="z-index:1000;">
						    <table>
						        <tr>
						            <td>
						                <label>按关键字搜索：</label>
						            </td>
						            <td class="column2">
						                <label>经度----纬度<span style="color: red;font-size: 15px">*</span></label>
						            </td>
						        </tr>
						        <tr>
						            <td>
						                <input type="text" placeholder="请输入关键字进行搜索" id="tipinput">
						            </td>
						            <td class="column2">
						                <input type="text" width="80px" readonly="true" id="longitude" name="longitude">
						                <input type="text" width="80px" readonly="true" id="latitude" name="latitude">
						            </td>
						        </tr>
						    </table>
					  </div>
					</div>
				</div>

				<div class="form-group">
					<label class="col-md-1 control-label">品类<span style="color: red;font-size: 15px">*</span></label>
					<div class="col-md-2">
						<select id="categoryId" name="categoryId" style="width: auto" class="form-control">
							<c:forEach items="${category }" var="cat">
								<option id="${cat.id }" value="${cat.id }">${cat.name }</option>
							</c:forEach>
						</select>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">门店电话<span style="color: red;font-size: 15px">*</span></label>
					<div class="col-md-2">
						<input type='text' id="contactNumber" name="contactNumber" class='form-control' value="${alipayShop.contactNumber }" maxlength="50"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">品牌LOGO<span style="color: red;font-size: 15px">*</span></label>
					<div id="divbrandLogo" class="col-md-2">
						<input type="button" id="upBrandLogo" class="btn btn-large btn-block btn-default"  name="upBrandLogo" data-toggle="tooltip" title="仅支持上传一张，LOGO将在支付宝-口碑页面展示。" value="上传品牌LOGO"/>&nbsp;&nbsp;
						<input type="file" id="brandLogoTemp" style="width: 1px;height:1px"name="brandLogoTemp" class="file"  value="上传照片" title="上传照片"/>
						<input type='hidden' id="logo" name="logo" class='form-control' value="${store.logo }"/>
						<input type="hidden" id="brandLogoFileCache" name="brandLogoFileCache" isRequired="0" tipName="品牌LOGO" value="${alipayShop.brandLogo }"/>
						<div id="brandLogoImagediv">
							<img alt="品牌LOGO" style="cursor: pointer;" id="brandLogoImage" name="brandLogoImage" src="" onload="imageLoad(this);">
						</div>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">门店首图<span style="color: red;font-size: 15px">*</span></label>
					<div id="divmainImageLogo" class="col-md-2">
						<input type="button" id="upMainLogo" class="btn btn-large btn-block btn-default"  name="upMainLogo" data-toggle="tooltip" title="仅支持上传一张，首图在支付宝-口碑页面重点展示" value="上传门店首图"/>&nbsp;&nbsp;
						<input type="file" id="mainImageTemp" style="width: 1px;height:1px"name="mainImageTemp" class="file"  value="上传照片" title="上传照片"/>
						<input type='hidden' id="mainImagelogo" name="mainImagelogo" class='form-control' value="${alipayShop.mainImage }"/>
						<input type="hidden" id="mainImagelogoFileCache" name="mainImagelogoFileCache" value="${alipayShop.mainImage }"/>
						<div id="mainImagelogoImagediv">
							<img alt="门店首图" style="cursor: pointer;" id="mainImagelogoImage" name="mainImagelogoImage" src="" onload="imageLoad(this);">
						</div>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">门头照<span style="color: red;font-size: 15px">*</span></label>
					<div id="divwindowImageLogo" class="col-md-2">
						<input type="button" id="upWindowLogo" class="btn btn-large btn-block btn-default"  name="upWindowLogo" data-toggle="tooltip" title="仅支持上传一张，首图在支付宝-口碑页面重点展示" value="上传门头照"/>&nbsp;&nbsp;
						<input type="file" id="windowImage" style="width: 1px;height:1px"name="windowImage" class="file"  value="上传照片" title="上传照片"/>
						<input type='hidden' id="windowImagelogo" name="windowImagelogo" class='form-control' value="${alipayShop.windowImage }"/>
						<input type="hidden" id="windowImagelogoFileCache" name="windowImagelogoFileCache" value="${alipayShop.windowImage }"/>
						<div id="windowImagelogoImagediv">
							<img alt="门头照" style="cursor: pointer;" id="windowImagelogoImage" name="windowImagelogoImage" src="" onload="imageLoad(this);">
						</div>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">内景照一<span style="color: red;font-size: 15px">*</span></label>
					<div id="divoneInternalLogo" class="col-md-2">
						<input type="button" id="upOneInternalLogo" class="btn btn-large btn-block btn-default"  name="upOneInternalLogo" data-toggle="tooltip" title="门头照上须有店名，且店名需与填写的门店名称一致。" value="上传内景照一"/>&nbsp;&nbsp;
						<input type="file" id="oneInternal" style="width: 1px;height:1px"name="oneInternal" class="file"  value="上传照片" title="上传照片"/>
						<input type='hidden' id="oneInternallogo" name="oneInternallogo" class='form-control' value="${alipayShop.oneInternal }"/>
						<input type="hidden" id="oneInternallogoFileCache" name="oneInternallogoFileCache" value="${alipayShop.oneInternal }"/>
						<div id="oneInternallogoImagediv">
							<img alt="内景照一" style="cursor: pointer;" id="oneInternallogoImage" name="oneInternallogoImage" src="" onload="imageLoad(this);">
						</div>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">内景照二<span style="color: red;font-size: 15px">*</span></label>
					<div id="divtwoInternalLogo" class="col-md-2">
						<input type="button" id="upTwoInternalLogo" class="btn btn-large btn-block btn-default"  name="upTwoInternalLogo" data-toggle="tooltip" title="门头照上须有店名，且店名需与填写的门店名称一致。" value="上传内景照二"/>&nbsp;&nbsp;
						<input type="file" id="twoInternal" style="width: 1px;height:1px"name="twoInternal" class="file"  value="上传照片" title="上传照片"/>
						<input type='hidden' id="twoInternallogo" name="twoInternallogo" class='form-control' value="${alipayShop.twoInternal }"/>
						<input type="hidden" id="twoInternallogoFileCache" name="twoInternallogoFileCache" value="${alipayShop.twoInternal }"/>
						<div id="twoInternallogoImagediv">
							<img alt="内景照二" style="cursor: pointer;" id="twoInternallogoImage" name="twoInternallogoImage" src="" onload="imageLoad(this);">
						</div>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">门店营业执照图片<span style="color: red;font-size: 15px">*</span></label>
					<div id="divlicenceLogo" class="col-md-2">
						<input type="button" id="upLicenceLogo" class="btn btn-large btn-block btn-default"  name="upLicenceLogo" data-toggle="tooltip" title="上传营业执照的照片，营业执照的经营者姓名需要与用户真实姓名一致" value="上传门店营业执照"/>&nbsp;&nbsp;
						<input type="file" id="licenceTemp" style="width: 1px;height:1px"name="licenceTemp" class="file"  value="上传照片" title="上传照片"/>
						<input type='hidden' id="licencelogo" name="licencelogo" class='form-control' value="${alipayShop.licence }"/>
						<input type="hidden" id="licencelogoFileCache" name="licencelogoFileCache" value="${alipayShop.licence }"/>
						<div id="licencelogoImagediv">
							<img alt="门店营业执照" style="cursor: pointer;" id="licencelogoImage" name="licencelogoImage" src="" onload="imageLoad(this);">
						</div>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">门店营业执照编号<span style="color: red;font-size: 15px">*</span></label>
					<div class="col-md-2">
						<input type='text' id="licenceCode" name="licenceCode" class='form-control' value="${alipayShop.licenceCode }" maxlength="20"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">门店营业执照名称<span style="color: red;font-size: 15px">*</span></label>
					<div class="col-md-2">
						<input type='text' id="licenceName" name="licenceName" class='form-control' value="${alipayShop.licenceName }" maxlength="20"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">行业许可证<span style="color: red;font-size: 15px">*</span></label>
					<div id="divbusinessCertificateLogo" class="col-md-2">
						<input type="button" id="upBusinessCertificateLogo" class="btn btn-large btn-block btn-default"  name="upBusinessCertificateLogo" data-toggle="tooltip" title="上传对应的许可证" value="上传行业许可证"/>&nbsp;&nbsp;
						<input type="file" id="businessCertificateTemp" style="width: 1px;height:1px"name="businessCertificateTemp" class="file"  value="上传照片" title="上传照片"/>
						<input type='hidden' id="businessCertificatelogo" name="businessCertificatelogo" class='form-control' value="${alipayShop.businessCertificate }"/>
						<input type="hidden" id="businessCertificatelogoFileCache" name="businessCertificatelogoFileCache" value="${alipayShop.businessCertificate }"/>
						<div id="businessCertificatelogoImagediv">
							<img alt="行业许可证" style="cursor: pointer;" id="businessCertificatelogoImage" name="businessCertificatelogoImage" src="" onload="imageLoad(this);">
						</div>
					</div>
				</div>
				
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button"  id='alipayShopAddBtn' class='btn btn-primary' repeatSubmit='1'
							value="${empty alipayShop ? '添加' : '修改' }" data-loading='稍候...' />
					</div>
				</div>
				</form>
	  </div>
   </div>
<script type="text/javascript">
    var map = new AMap.Map("container", {
        resizeEnable: true
    });
    //为地图注册click事件获取鼠标点击出的经纬度坐标
    var clickEventListener = map.on('click', function(e) {
    	$("#longitude").val(e.lnglat.getLng());
    	$("#latitude").val(e.lnglat.getLat());
    });
    var auto = new AMap.Autocomplete({
        input: "tipinput"
    });
    AMap.event.addListener(auto, "select", select);//注册监听，当选中某条记录时会触发
    function select(e) {
        if (e.poi && e.poi.location) {
            map.setZoom(15);
            map.setCenter(e.poi.location);
        }
    }
</script>
<!-- 模态窗 -->
	<div class="modal fade" id="storeAddModal">
		<div class="modal-dialog modal-lg" style="width: 1200px;">
		  <div class="modal-content">
			    <div class="modal-header">
			      <button type="button" class="close" data-dismiss="modal" id="shopListModalBtn"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
			      <h4 class="modal-title"></h4>
			    </div>
				<div class="modal-body">
					<m:list title="商家列表" id="productStockList" listUrl="${contextPath }/alipayShop/shopList/list-data" callback="callback" searchButtonId="cateogry_search_btn" >
					
					<div class="input-group" style="max-width: 1500px;">
						<c:if test="${isSys==true }">
							 <span class="input-group-addon">商家名称</span> 
			            	 <input type="text" id="name" name="name" class="form-control" placeholder="商家名称" style="width: 200px;">
					     </c:if>       	
				         <span class="input-group-addon">手机号码</span> 
		            	 <input type="text" id="mobile" name="mobile" class="form-control" placeholder="手机号码" style="width: 200px;">
					</div>
				</m:list>
				</div>
			</div>
		</div>
	</div>
	<!-- 模态窗 -->
</body>
</html>