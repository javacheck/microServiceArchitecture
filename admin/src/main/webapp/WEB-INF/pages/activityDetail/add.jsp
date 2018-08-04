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
<title>${empty activityDetail ? '添加' : '修改' }活动详情 </title>
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
			UpBtn : "activityDetailImage",
			DivShow : "activityDetailImagediv",
			ImgShow : "activityDetailImageShow"
		});
		var isUpdate= "${empty activityDetail ?'false':'true'}";//是否是修改状态
		if (isUpdate) {
			  $("#activityDetailImagediv").show();
		}
		$("[name = activityDetailImage]").bind("change", function () {
			   var fileName = $("#activityDetailImage").val();
			   if( "" == fileName ){
				   $("#activityDetailImagediv").hide();
			   } else {
				   $("#activityDetailImagediv").show();
			   }
		  })
		  
		   $("[name = upActivityDetailImage]").bind("click", function () {
			   $("[name = activityDetailImage]").click();
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
	
	var addNodeId = 100;
	function addDiv(){
		$("#treasure").append(
			"<div id = 'treasureDivId_"+addNodeId+"' class='form-group'>"
				+"<label class='col-md-1 control-label'>宝藏点</label>"
				+"<div class='col-md-2'>"
				+"	<input type='text' id='treasureName' name='treasureName' maxlength='50' value='' placeholder='' class='form-control' />"
				+"</div>"
				+"<div align='right' class='col-md-1'>"
				+"	<label class='' style='float: right;padding-top: 10px;text-align: right;' >宝藏名称</label>"
				+"</div>"
				+"<div class='col-md-2'>"
				+"	<input type='text' id='shopName' name='shopName' maxlength='50' value='' placeholder='输入藏宝店铺名称' class='form-control' />"
				+"</div>"
				+"<label class='col-md-0 control-label' style='color: red;font-size: 15px' ><input onclick='deleteDiv("+addNodeId+")' type='button' class='btn btn-mini btn-primary' value='删除' ></label>"
			+"</div>");
		addNodeId++;
	}
	function deleteDiv(nodeId){
		$("#treasureDivId_"+nodeId).remove();
	}
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong><i class='icon-plust'></i>${empty activityDetail ? '添加' : '修改' }活动详情 </strong>
		</div>
		<div class='panel-body'>
			<form id="activityAddForm" method='post' class='form-horizontal' repeatSubmit='1'
				action="${contextPath }/activityDetail/add" enctype="multipart/form-data">
				
				<input name="activityId" type="hidden" value="${activity.id }" />
				<div class="form-group">
					<label class="col-md-1 control-label">活动标题</label>
					<div class="col-md-2">
						${activity.title }
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label">开始时间</label>
					<div class="col-md-2">
						<input  readonly="readonly" type="text" id="beginTime" name="beginTime" value="<fmt:formatDate value="${activityDetail.startDate }" pattern="yyyy-MM-dd"/>"  class='form-control' />
					</div>
					<div class="col-md-1">
						<label class="" style="float: right;padding-top: 10px">结束时间</label>
					</div>
					<div class="col-md-2">
						<input  readonly="readonly" type="text" id="endTime" name="endTime" value="<fmt:formatDate value="${activityDetail.endDate }" pattern="yyyy-MM-dd"/>"  class='form-control' />
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
				
				
				<div class="form-group">
					<label class="col-md-1 control-label">活动区域</label>
					<div class="col-md-2">
						<m:selectArea areaId="${activityDetail.areaId}" inputName="areaId"></m:selectArea>
					</div>
					<div class="col-md-1">
						<label class="" style="float: right;padding-top: 10px">小区地址</label>
					</div>
					<div class="col-md-2">
						<input type='text' id="placeName" name="placeName" maxlength="150" value="${activityDetail.placeName }" placeholder="输入小区地址" class='form-control' />
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
				<div id = 'treasure'>
				<c:if test="${ empty activityDetail}"><!-- 添加 -->
					<div id = 'treasureDivId_${itemStatus.index}' class="form-group">
					<label class="col-md-1 control-label">宝藏点</label>
					<div class="col-md-2">
						<input type='text' id="placeName" name="treasureName" maxlength="50" value="" placeholder="" class='form-control' />
					</div>
					<div align="right" class="col-md-1">
						<label class="" style="float: right;padding-top: 10px;text-align: right;" >宝藏名称</label>
					</div>
					<div class="col-md-2">
						<input type='text' id="placeName" name="shopName" maxlength="50" value="" placeholder="输入藏宝店铺名称" class='form-control' />
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >
							<input onclick="addDiv()" type="button" class="btn btn-mini btn-info" value="添加" >
					</label>
				</div>
				</c:if>
				<c:forEach items="${ activityDetailLocations}" varStatus="itemStatus" var="activityDetailLocation">
				
				<div id = 'treasureDivId_${itemStatus.index}' class="form-group">
					<label class="col-md-1 control-label">宝藏点</label>
					<div class="col-md-2">
						<input type='text' id="placeName" name="treasureName" maxlength="50" value="${activityDetailLocation.name }" placeholder="" class='form-control' />
					</div>
					<div align="right" class="col-md-1">
						<label class="" style="float: right;padding-top: 10px;text-align: right;" >宝藏名称</label>
					</div>
					<div class="col-md-2">
						<input type='text' id="placeName" name="shopName" maxlength="50" value="${activityDetailLocation.shopName }" placeholder="输入藏宝店铺名称" class='form-control' />
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >
						<c:if test="${itemStatus.first }">
							<input onclick="addDiv()" type="button" class="btn btn-mini btn-info" value="添加" >
						</c:if>
						<c:if test="${!itemStatus.first }">
							<input onclick="deleteDiv(${itemStatus.index})" type="button" class="btn btn-mini btn-primary" value="删除" >
						</c:if>
					</label>
				</div>
				
				</c:forEach>
				</div>
				
				
				<div class="form-group">
					<label class="col-md-1 control-label">相关图片</label>
					<div class="col-md-2">
						<input type="button" id="upActivityDetailImage" class="btn " name="upActivityDetailImage" value="上传详情图片"/>
						<input type="file" id="activityDetailImage" name="activityDetailImage" class="file"  value="上传图片" title="上传图片"/>
						<input type='hidden' id="imageId" name="imageId" class='form-control' value="${activityDetail.imageId }" maxlength="25"/>
						<div id="activityDetailImagediv">
							<img alt="" style="cursor: pointer;" id="activityDetailImageShow" name="activityDetailImageShow" src="${activityDetail.imageUrl }">
						</div>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">是否开启</label>
					<div class="col-md-2">
						<m:slider defaultStatus="${activityDetail.status }"  width="75" height="30"  inputName="status"></m:slider>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
				
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button"  repeatSubmit='1' id='activityAddBtn' class='btn btn-primary'	value="${empty activity ? '发布' : '修改' }" data-loading='稍候...' />
					</div>
				</div>
			</form>
		</div>
	</div>

</body>
</html>