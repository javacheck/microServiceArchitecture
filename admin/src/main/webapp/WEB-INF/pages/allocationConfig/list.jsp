<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="${contextPath}/static/js/uploadPreview.js" type="text/javascript"></script>
<title>库存调拨设置</title>
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
	$("#status_0").click(function () {
		 if($(this).prop("checked")){
			 $("#status_1").prop("checked",false);
			 $("#statusNo_1").prop("disabled",true);
			 $("#statusNo_0").prop("disabled",true);
		 }
	 });
	
	$("#status_1").click(function () {
		 if($(this).prop("checked")){
			 $("#status_0").prop("checked",false);
			 $("#statusNo_1").prop("disabled",false);
			 $("#statusNo_0").prop("disabled",false);
		 }
	 });
	 var status="${allocationConfig.status}";
	 if(status!=''){
		 if(status=='2'){
			 $("#statusNo_1").prop("checked",true);
			 $("#statusNo_0").prop("checked",false);
			 $("#statusNo_1").prop("checked",true);
		 }else if(status=='0'){
			 $("#status_0").prop("checked",true);
			 $("#statusNo_1").prop("disabled",true);
			 $("#statusNo_0").prop("disabled",true);
		 }else{
			 $("#statusNo_1").prop("checked",true);
			 $("#statusNo_0").prop("checked",true);
			 $("#statusNo_1").prop("checked",false);
		 }
	 }
	$("#allocationConfigAddBtn").bind("click", function () {
		
		if($("#status_0").prop("checked")==true){
			$("#status").val(0);
		}else if($("#status_1").prop("checked")==true && $("#statusNo_0").prop("checked")==true){
			$("#status").val(1);
		}else{
			$("#status").val(2);
		}
		$("#allocationConfigAddForm").submit();
		lm.alert("操作成功！");
	 })
});

</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong><i class='icon-plust'></i>库存调拨设置 </strong>
		</div>
		<div class='panel-body'>
			<div  class='form-horizontal'>
				<form id="allocationConfigAddForm" method='post' class='form-horizontal' repeatSubmit='1'
				action="${contextPath }/allocationConfig/save">
				<input id="status" name="status" type="hidden" value="${allocationConfig.status }" />
				<input id="storeId" name="storeId" type="hidden" value="${allocationConfig.storeId }" />
					<div class="form-group">
						<label class="col-md-1 control-label">&nbsp;</label>
						<div class="col-md-1" style="width:150px;margin-top: 5px;">
							<input  type="radio"    id="status_1" name="status" value="1" checked="checked"/>允许分店之间调拨
						</div>
						<label class="col-md-1 control-label" style="width:280px;">(勾选后，分店之间能相互调拨商品)</label>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label">&nbsp;</label>
						<div style="padding-left:170px;">
							<input  type="radio"    id="statusNo_1" name="statusNo"/>需总部审核<br />
							<input  type="radio"    id="statusNo_0" name="statusNo"  checked="checked"/>无需总部审核
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label">&nbsp;</label>
						<div class="col-md-1" style="width:150px;margin-top: 5px;">
							<input  type="radio"    id="status_0" name="status" value="0"/>不允许分店之间调拨
						</div>
						<label class="col-md-1 control-label" style="width:293px;">(勾选后，分店之间不能相互调拨商品)</label>
					</div>
					<div class="form-group">
						<div class="col-md-offset-1 col-md-10">
							<input type="button"  repeatSubmit='1' id='allocationConfigAddBtn' class='btn btn-primary' 
								value="保存" data-loading='稍候...' />
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>

</body>
</html>