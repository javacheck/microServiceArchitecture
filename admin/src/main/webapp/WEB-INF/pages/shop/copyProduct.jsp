<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>复制商品</title> 
<script src="${contextPath}/static/js/uploadPreview.js" type="text/javascript"></script>
<script src="${contextPath}/static/js/checkSubmitParameters.js" type="text/javascript"></script>
<style type="text/css">
</style>
<script type="text/javascript">
	var isSubmit = false;
	$(function(){
		$("#copyOfproduct").click(function(){
			var url = "${contextPath }/shop/ajax/copyProduct";
			var parameterData =$("#shopSignFormSubmit").serialize();
			if (isSubmit) {
				//alert("禁止提交多次");
				return ;
			}
			isSubmit=true;
			lm.post(url,parameterData,function(data){
				alert(data);
			});
		});
	});
	function callbackfrom(store){
		storeId=store.id;
	}
	function callbackto(store){
		storeId=store.id;
	}
	function showStoreFrom(){
		alert(4);
		$("#copyOfproductShowStoreFrom").modal();
	}
	function showStoreTo(){
		alert(5);
		$("#copyOfproductShowStoreTo").modal();
	}
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>复制商品
			</strong>
		</div>
		<div class='panel-body'>
			<form id="shopSignFormSubmit" method='post' class='form-horizontal'  action="${contextPath }/shop/save" enctype="multipart/form-data" >
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>FROM</label>
					<div class="col-md-2">
						<input type="text" name="oldShopId"  value=""  />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>to</label>
					<div class="col-md-2">
						<input type="text"  name="newShopId"  value=""/>
					</div>
				</div>
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button"  id='copyOfproduct' class='btn btn-primary' value="复制" data-loading='稍候...' />
					</div>
				</div>
			</form>
	  </div>
   </div>
   <m:select_store path="${contextPath}/shop/showModle/list/list-data"   modeId="copyOfproductShowStoreFrom" callback="callbackfrom"> </m:select_store>
   <m:select_store path="${contextPath}/shop/showModle/list/list-data"   modeId="copyOfproductShowStoreTo" callback="callbackto"> </m:select_store>
</body>
</html>