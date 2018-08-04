<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>会员导入</title> 
<script type="text/javascript">

//初始化事件
$(function() {
	
	
	var isSys=${isSys};
	
 	
	$("[name='storeName']").bind("click", function () {
		$('#storeAddModal').modal();
	});
	//绑定 提交方法
	$("#fileUploadButton").click(function() {
		$("#tipDIV1").html("");
		$("#tipDIV").html("");
		var zipOrRarFile = $("#zipOrRarFile").val();
	    if(zipOrRarFile=='') {
	    	lm.alert("请选择需上传的文件!");
	    	return false;
	    }
	    if(zipOrRarFile.indexOf('.xls')==-1){
	    	lm.alert("文件类型不正确，请选择正确的xls文件(后缀名.xls)！");
	    	return false;
	    }
	    var storeId = $("#storeId").val();// 商家ID
	    if(storeId == "" || storeId == null){
			storeId="${isStoreId}";
		}
		if( storeId == "" || storeId == null || typeof(storeId)=="undefined"){
			lm.alert("商家不能为空!");
			return;
		}
		
		$("#fileUploadButton").prop("disabled","disabled");
		//lm.loading();
	    $("#fileUpload").submit();
	    startTimer();
	});
});
function callback(){
	
	$("#shopListDataId").find("tbody tr").click(function(){
		$("#storeId").val($(this).attr("val"));
		$("#storeName").val(($($(this).find("td")[0]).html()));
		$("#shopListModalBtn").click();
	});
	
}

function getExe() {
	var storeId = $("#storeId").val();// 商家ID
	if(storeId == "" || storeId == null){
		storeId="${isStoreId}";
	}
	if( storeId == "" || storeId == null || typeof(storeId)=="undefined"){
		lm.alert("商家不能为空!");
		return;
	}
	window.open("${contextPath}/user/list/ajax/list-by-search?storeId="+storeId);
}
var myVar;

function startTimer(){ 
    /*setInterval() 间隔指定的毫秒数不停地执行指定的代码*/
   myVar=setInterval(function(){
    lm.post("${contextPath}/user/importResult",{},function(data){
		if(data!=-2){
			if(data==1){
				stopTimer(myVar);
				lm.alert("导入成功!");
				$("#fileUploadButton").prop("disabled",false);
				$("#tipDIV1").html("导入成功!");
			}else if(data==-1){
				stopTimer(myVar);
				lm.alert("会员信息填写错误导入失败!");
				$("#fileUploadButton").prop("disabled",false);
				$("#tipDIV").html("会员信息填写错误导入失败!");
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
    },5000);
}



function stopTimer(){/* clearInterval() 方法用于停止 setInterval() 方法执行的函数代码*/
	clearInterval(myVar);
}

</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>会员导入</strong>
		</div>
		<div class='panel-body'>
			<div class='form-horizontal'>
			</div>
			<iframe id="iframform" name="iframform" style="display: none;"></iframe>
			<form id="fileUpload" target="iframform" method='post' class='form-horizontal' action="${contextPath }/user/save1" enctype="multipart/form-data">
				<c:if test="${isSys==true }">
					<div class="form-group" >
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商家</label>
						<div class="col-md-2">
							<input name="storeName" id="storeName" readonly="readonly" value="" class="form-control" isRequired="1"  />
							<input type="hidden" name="storeId" id="storeId" value="" />
						</div>
					</div>
				</c:if>
				<div  class="form-group">
					<label class="col-md-1 control-label">下载Excel模板</label>
					<div class="col-md-2">
						<a  onclick="getExe();" class="btn btn-small btn-danger">下载Excel模板</a>
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
	<!-- 模态窗 -->
	<div class="modal fade" id="storeAddModal">
		<div class="modal-dialog modal-lg" style="width: 1200px;">
		  <div class="modal-content">
			    <div class="modal-header">
			      <button type="button" class="close" data-dismiss="modal" id="shopListModalBtn"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
			      <h4 class="modal-title"></h4>
			    </div>
				<div class="modal-body">
					<m:list title="商家列表" id="productStockList" listUrl="${contextPath }/productStock/shopList/list-data" callback="callback" searchButtonId="cateogry_search_btn" >
					
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