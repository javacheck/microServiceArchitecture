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
<title>${empty activity ? '添加' : '修改' }活动 </title>
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
	
	//手机号码重复验证
	$(function() {
		new uploadPreview({
			UpBtn : "activityImage",
			DivShow : "activityImagediv",
			ImgShow : "activityImageShow"
		});
		var isUpdate= "${empty activity ?'false':'true'}";//是否是修改状态
		if (isUpdate) {
			  $("#activityImagediv").show();
		}
		$("[name = activityImage]").bind("change", function () {
			   var fileName = $("#activityImage").val();
			   if( "" == fileName ){
				   $("#activityImagediv").hide();
			   } else {
				   $("#activityImagediv").show();
			   }
		  })
		  
		   $("[name = upActivityImage]").bind("click", function () {
			   $("[name = activityImage]").click();
		  })
	});

	//初始化事件
	$(function() {
		var format = "yyyy-MM-dd";//默认格式
		var now = lm.Now;//当前日期
		var nowFormat=now.Format(format);
		$("#beginTime").datetimepicker({
			minView: "month", //选择日期后，不会再跳转去选择时分秒 
			format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
			language: 'zh-CN', //汉化 
			autoclose:true , //选择日期后自动关闭
			todayBtn:true,//可选择当天按钮
			todayHighlight:false//高亮当前日期
			
		 });
		$("#endTime").datetimepicker({
			minView: "month", //选择日期后，不会再跳转去选择时分秒 
			format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
			language: 'zh-CN', //汉化 
			autoclose:true, //选择日期后自动关闭 
			todayBtn:true,//可选择当天按钮
			todayHighlight:false//高亮当前日期
		 });
		
	});//初始化事件结束
	
	$(function(){
		$("#activityAddBtn").bind("click", function () {
			
			//lm.post("${contextPath}/productCategory/manager/add",$("#activityAddForm").serialize(),function(data){
			//});
			
			$("#activityAddForm").submit();
		 })
	});
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong><i class='icon-plust'></i>${empty activity ? '添加' : '修改' }活动  </strong>
		</div>
		<div class='panel-body'>
			<form  id="activityAddForm" method='post' class='form-horizontal' repeatSubmit='1'
				action="${contextPath }/activity/add" enctype="multipart/form-data">
				
				<input name="id" type="hidden" value="${activity.id }" />
				<div class="form-group">
					<label class="col-md-1 control-label">活动标题</label>
					<div class="col-md-2">
						<input type='text' id="title" name="title" maxlength="30"	value="${activity.title }" class='form-control' />
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label">活动时间</label>
					<div class="col-md-2">
						<input  readonly="readonly" type="text" id="beginTime" name="beginTime" value="<fmt:formatDate value="${activity.startDate }" pattern="yyyy-MM-dd"/>"  class='form-control' />
					</div>
					<label class="" style="float: left;padding-top: 6px">至</label>
					<div class="col-md-2">
						<input  readonly="readonly" type="text" id="endTime" name="endTime" value="<fmt:formatDate value="${activity.endDate }" pattern="yyyy-MM-dd"/>"  class='form-control' />
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
				
				
				<div class="form-group">
					<label class="col-md-1 control-label">活动区域</label>
					<div class="col-md-2">
						<m:selectArea areaId="${activity.areaId}" inputName="areaId"></m:selectArea>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">相关图片</label>
					<div class="col-md-2">
						<input type="button" id="upActivityImage" class="btn " name="upActivityImage" value="上传详情图片"/>
						<input type="file" id="activityImage" name="activityImage" class="file"  value="上传图片" title="上传图片"/>
						<input type='hidden' id="imageId" name="imageId" class='form-control' value="${activity.imageId }" maxlength="25"/>
						<div id="activityImagediv">
							<img alt="" style="cursor: pointer;" id="activityImageShow" name="activityImageShow" src="${activity.imageUrl }">
						</div>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">活动备注</label>
					<div class="col-md-2">
						<input type='text' id="memo" name="memo" maxlength="150" value="${activity.memo }" class='form-control' />
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label">是否开启</label>
					<div class="col-md-2">
						<m:slider defaultStatus="${activity.status }"  width="75" height="30"  inputName="status"></m:slider>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
				
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button"  repeatSubmit='1' id='activityAddBtn' class='btn btn-primary'
							value="${empty activity ? '添加' : '修改' }" data-loading='稍候...' />
					</div>
				</div>
			</form>
		</div>
	</div>

</body>
</html>