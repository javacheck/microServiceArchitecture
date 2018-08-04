<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>活动结果审核列表</title>
<script type="text/javascript">
	function showModelWindow(id,userId){
		$("#id").val(id);
		$("#userId").val(userId);
		lm.post("${contextPath }/activityResultAudit/showMode/auditWindow/", {id:id,userId:userId}, function(data) {
			$("#dateTime").html();
			var date = new Date();
			date.setTime(data.dateTime);
			var a =date.Format('yyyy年MM月dd日');
			$("#dateTime").html(a);
			$("#placeName").html(data.placeName);
			$("#name").html(data.name);
			$("#shopName").html(data.shopName);
			$("#activityResultImageShow").attr("src",data.imageId);
			$("#activityResultAuditShowID").modal();
		});
	}
	
	function activityAuditShow(status){
		var id = $("#id").val();
		var userId = $("#userId").val();
		lm.confirm("是否确认审核？",function(){
			lm.post("${contextPath }/activityResultAudit/list/ajax/auditOperation/", {id:id,userId:userId,status:status}, function(data) {
				if(data == 1){
					$("#auditClose").click();					
					loadCurrentList_activityResultAudit();
				} else {
					lm.alert("审核失败,请重试");
				}
			});
		});
	}
	
</script>
</head>

<body>
	<m:list title="活动结果审核列表" id="activityResultAudit"
		listUrl="${contextPath }/activityResultAudit/list-data"
		searchButtonId="activityResultAudit_search_btn">

		<div class="input-group" style="max-width: 600px;">
			<span class="input-group-addon">手机号码</span> 
			<input type="text" id="mobile" name="mobile" class="form-control" placeholder="手机号码" style="width: 200px;"> 
		
			<span class="input-group-addon">审核状态</span>
           	<select name="status" class="form-control" style="width: 200px;" id="status">
           		<option id="first" value="0" selected="selected">审核中</option>
           		<option id="second" value="1">审核通过</option>
           		<option id="last" value="2">审核不通过</option>
           	</select>
		</div>
	</m:list>
	
	<div class="modal fade" id="activityResultAuditShowID" >
	 <div class="modal-dialog modal-lg">
	  <div class="modal-content">
		    <div class="modal-header">
		      <button type="button" id="auditClose" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
		      <h4 class="modal-title">宝藏审核(图片)</h4>
		    </div>
		    <div class="modal-body">
		<div class='panel-body'>
				<input type="hidden" id="id" name="id"/>
				<input type="hidden" id="userId" name="userId"/>
				<table class="table table-hover table-striped table-bordered" cellpadding="0" cellspacing="0">
				<tr>
					<td colspan="2" style="width:20%">日期：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="dateTime"></span></td>
				</tr>
				
				<tr>
					<td colspan="2" style="width:20%">区域：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="placeName"></span></td>
				</tr>
				
				<tr>
					<td style="width:50%">宝藏点：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="name"></span></td>
					<td style="width:50%">店铺名称：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="shopName"></span></td>
				</tr>
				
				<tr>
					<td colspan="2" style="line-height:500px;height:500;no-repeat center;"><img alt="" style="width:100%;cursor: pointer;" id="activityResultImageShow" name="activityResultImageShow" src=""></td>
				</tr>
	
				<tr>
					<td align="center"><input type="button" onclick="activityAuditShow(2);"  id='activityNoBtn' class='btn btn-primary' value="不通过" data-loading='稍候...' /></td>
					<td align="center"><input type="button" onclick="activityAuditShow(1);" id='activityOkBtn' class='btn btn-primary' value="通过" data-loading='稍候...' /></td>
				</tr>			
			</table>
		</div>
		    </div>
	  </div>
	</div>
</div>
</body>
</html>