<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
	
	//绑定 提交方法
	$("#fileUploadButton").click(function() {
		$("#tipDIV1").html("");
		$("#tipDIV").html("");
		
		var userExcelFile = $("#userExcelFile").val();
		userExcelFile = $.trim(userExcelFile);
	    if( null == userExcelFile || "" == userExcelFile ) {
	    	lm.alert("请选择需上传的文件!");
	    	return false;
	    }
	    if( userExcelFile.indexOf('.xls') == -1 ){
	    	lm.alert("文件类型不正确，请选择正确的xls文件(后缀名.xls)！");
	    	return false;
	    }
	    	
		$("#fileUploadButton").prop("disabled","disabled");
		//lm.loading();
	    $("#fileUpload").submit();
	   // startTimer();
	});
});

$(function(){
	var uploadResult = $("#uploadResult").val();
	uploadResult = $.trim(uploadResult);
	if( null != uploadResult && "" != uploadResult){
		var tip = "导入成功!";
		if( 1 == uploadResult ){
			tip = "第一列数据中存在空数据,请填写完整!";
		} else if( 2 == uploadResult ){
			tip = "第一列数据中数据格式不正确,请填写正确!";
		} else if( 3 == uploadResult ){
			tip = "第一列数据中存在重复的数据,请重新填写!";
		} else if( 4 == uploadResult ){
			tip = "第三列数据中数据格式不正确,请填写正确!";
		}
		lm.alert(tip);		
	}
});
function getExe() {
	window.open("${contextPath}/userAccountManager/list/ajax/exportModelExcel");
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
				<c:if test="${not empty uploadResult }">
				<div class="panel panel-warning"><div class="panel-heading">
    				${uploadResult }
  				</div></div>
  				<br/>
  				</c:if>
			<form id="fileUpload" method='post' class='form-horizontal' action="${contextPath }/userAccountManager/uploadUserExcel" enctype="multipart/form-data">
				<div  class="form-group">
					<label class="col-md-1 control-label">下载Excel模板</label>
					<div class="col-md-2">
						<a  onclick="getExe();" class="btn btn-small btn-danger">下载Excel模板</a>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label">导入</label>
					<div class="col-md-2">
						<input type='file' id="userExcelFile" name="userExcelFile" value="" class='form-control' />
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