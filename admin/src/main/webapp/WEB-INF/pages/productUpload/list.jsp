<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品导入</title> 
<script type="text/javascript">

//初始化事件
$(function() {
	
	var isSys=${isSys};
	var isMainStore=${isMainStore};
	
	var action="${action}";
	if(action!=null && action!=''){
		if(action==1){
			lm.alert("导入成功!");
			$("#fileUploadButton").prop("disabled",false);
			$("#tipDIV1").html("导入成功!");
		}else if(action==-1){
			lm.alert("商品信息填写错误导入失败!");
			$("#fileUploadButton").prop("disabled",false);
			$("#tipDIV").html("商品信息填写错误导入失败!");
		}else{
			$("#fileUploadButton").prop("disabled",false);
			$("#tipDIV").html(action);
		}
	}
	
	//绑定 提交方法
	$("#fileUploadButton").click(function() {
		$("#tipDIV").html("");
		$("#tipDIV1").html("");
		var zipOrRarFile = $("#zipOrRarFile").val();
	    if(zipOrRarFile=='') {
	    	lm.alert("请选择需上传的文件!");
	    	return false;
	    }
	    if(zipOrRarFile.indexOf('.rar')==-1){
	    	lm.alert("文件格式不正确，请选择正确的rar文件(后缀名.rar)！");
	    	return false;
	    }
	    var storeId=$("#storeId").val();
		var isSys=${isSys};
		var isMainStore=${isMainStore};
		if(isSys){
			if(storeId==null || storeId=="" || storeId==-1){
				lm.alert("商店不能为空！");
				return;
			}
		}else{
			storeId="${isStoreId}";
		}
		var categoryId=$("[name='storeCategoryId']").val();
		
		if(categoryId==null || categoryId==""){
			lm.alert("分类不能为空！");
			return;
		}
		$("#fileUploadButton").prop("disabled","disabled");
		//lm.loading();
	    $("#fileUpload").submit();
	    startTimer();
	});
});
function getExe() {
	var storeId=$("#storeId").val();
	var isSys=${isSys};
	var isMainStore=${isMainStore};
	if(isSys){
		if(storeId==null || storeId=="" || storeId==-1){
			lm.alert("商店不能为空！");
			return;
		}
	}else{
		storeId="${isStoreId}";
	}
	var categoryId=$("[name='storeCategoryId']").val();
	
	if(categoryId==null || categoryId==""){
		lm.alert("分类不能为空！");
		return;
	}
	var flag=true;
	lm.postSync("${contextPath }/productUpload/checkAttributeByCagetoryId/", {categoryId:categoryId}, function(data) {
		if (data =="1") { 
			lm.alert("请先为该分类添加属性！");
			flag=false;
		}
	});
	lm.postSync("${contextPath }/productUpload/checkCagetoryId/", {categoryId:categoryId}, function(data) {
		if (data != null && data!="") { 
			$("#tipDIV").html("分类:"+data[0]+"下的属性:"+data[1]+"的属性值为空，请先添加属性值！");
			flag=false;
		}
	});
	if(flag){
		window.open("${contextPath}/productUpload/list/ajax/list-by-search?storeId="+storeId+"&categoryId="+categoryId);
	}
}
function startTimer(){ 
    /*setInterval() 间隔指定的毫秒数不停地执行指定的代码*/
   myVar=setInterval(function(){
    lm.post("${contextPath}/productUpload/importResult",{},function(data){
		if(data!=-2){
			if(data==1){
				stopTimer(myVar);
				$("#fileUploadButton").prop("disabled",false);
				$("#tipDIV1").html("导入成功!");
			}else if(data==-1){
				stopTimer(myVar);
				lm.alert("商品信息填写错误导入失败!");
				$("#fileUploadButton").prop("disabled",false);
				$("#tipDIV").html("商品信息填写错误导入失败!");
			}else{
				stopTimer(myVar);
				$("#fileUploadButton").prop("disabled",false);
				$("#tipDIV1").html("");
				$("#tipDIV").html(data);
			} 
		}else{
			$("#tipDIV1").html("正在导入中，请耐心等待！");
		}
	}); 
    },2000);
}
function stopTimer(){/* clearInterval() 方法用于停止 setInterval() 方法执行的函数代码*/
	clearInterval(myVar);
}
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>商品导入</strong>
		</div>
		<div class='panel-body'>
			<div class='form-horizontal'>
			</div>
			<iframe id="iframform" name="iframform" style="display: none;"></iframe>
			<form id="fileUpload" target="iframform" method='post' class='form-horizontal' action="${contextPath }/productUpload/save1" enctype="multipart/form-data">
				<div  class="form-group">
					<label class="col-md-1 control-label">下载说明文件</label>
					<div class="col-md-2">
						<a href="${staticPath }/exel/readme.docx" class="btn btn-small btn-danger">下载说明文件</a>
					</div>
				</div>
			   <c:if test="${isSys==true }">  
			   		<div class="form-group" >
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商家</label>
						<div class="col-md-2">
							<input name="storeName" id="storeName" readonly="readonly" value="" class="form-control" isRequired="1" tipName="商家" data-remote="${contextPath }/productStock/shopList/list" data-toggle="modal" />
							<input type="hidden" name="storeId" id="storeId" value="" />
						</div>
					</div>
			   </c:if>  
				   
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>所属分类</label>
					<div class="col-md-2">
						<m:selectProductCategory onStoreChange="shopChange" isSys="false" inputName="storeCategoryId" path="${path}" ></m:selectProductCategory>
					</div>
				</div>
				<div  class="form-group">
					<label class="col-md-1 control-label">下载Excel文件</label>
					<div class="col-md-2">
						<button  onclick="getExe();" class="btn btn-small btn-danger">下载Excel文件</button>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label">导入</label>
					<div class="col-md-2">
						<input type='file' id="zipOrRarFile" name="zipOrRarFile" value="" class='form-control' />
					</div>
					<div class="col-md-2">
							<input type='button' id='fileUploadButton' class='btn btn-primary' value="上传" />
					</div>
				</div>
				<div class="form-group" >
					<label class="col-md-1 control-label"></label>
					<div class="col-md-6">
					 	<span style="color:red" id="tipDIV"></span>
					 	<span style="color:green" id="tipDIV1"></span>  
					</div>
				</div>
			</form>
		</div>
	</div>

</body>
</html>