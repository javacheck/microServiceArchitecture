<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>审核列表</title>
	
<script type="text/javascript">
	$(function(){
		var format = "yyyy-MM-dd";//默认格式
		var now = lm.Now;//当前日期
		var nowFormat=now.Format(format);
		$("#startTime").datetimepicker({
			minView: "month", //选择日期后，不会再跳转去选择时分秒 
			format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
			language: 'zh-CN', //汉化 
			autoclose:true , //选择日期后自动关闭
			todayBtn:true,//可选择当天按钮
			todayHighlight:false,//高亮当前日期
			endDate:""+nowFormat+""//默认最后可选择为当前日期
			
		 });
		$("#endTime").datetimepicker({
			minView: "month", //选择日期后，不会再跳转去选择时分秒 
			format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
			language: 'zh-CN', //汉化 
			autoclose:true, //选择日期后自动关闭 
			todayBtn:true,//可选择当天按钮
			todayHighlight:false,//高亮当前日期
			endDate:""+nowFormat+""//默认最后可选择为当前日期
		 });
		var endDateInput=nowFormat;
		//判断输入结束日期时候输入合法
		$('#endTime').datetimepicker().on('changeDate', function(ev){
			if((new Date(ev.date.valueOf()))<(new Date($('#startTime').val().replace(/-/g,"/")))){//开始时间大于结束时间
				lm.alert("结束时间不能大于开始时间");
				$("#endTime").val(endDateInput);//默认值上一次输入
				$('#endTime').datetimepicker('update');//更新
			}
			endDateInput= $("#endTime").val();
			$('#startTime').datetimepicker('setEndDate', $("#endTime").val());//设置开始时间最后可选结束时间
		});
	});
	
	function publishAuditShow(status){
		var id = $("#id").val();
		var reason = $("#reason").val();
		reason = $.trim(reason);
		if(status == 2){
			if( "" == reason){
				lm.alert("撤销理由不能为空!");
				return ;
			}
		}
		var userId = $("#userId").val();
		var title = $("#titleCache").val();
		lm.confirm("是否确认审核？",function(){
			lm.post("${contextPath }/publish/ajax/modifyStatus/", {id:id,status:status,reason:reason,userId:userId,title:title}, function(data) {
				if(data == 1){
					$("#auditClose").click();					
					loadCurrentList_publishList();
				} else {
					lm.alert("审核失败,请重试");
				}
			});
		});
	}
	function showModelWindow(id,flag){
		var nextAll = $("#publisImageTable #imageDown").nextAll();
		if (nextAll.length > 2){
			var index = 0;
			nextAll.each(function(k,v){
				if (index < nextAll.length - 2){
					$(v).remove();
				}
				index++;
			});
		}
				
		$("#id").val(id);
		lm.post("${contextPath }/publish/showMode/publishAuditWindow/", {id:id}, function(data) {
			var html = "";
			$("#userId").val(data.userId);
			$("#titleCache").val(data.title);
			if( "" != $.trim(data.publishImages) && undefined != data.publishImages){
				var isdesImages = 0;
				var iscerImages = 0;
				for(var i=0;i<data.publishImages.length;i++){
					if( data.publishImages[i].type == 1 ){
						if(isdesImages == 0 ){
							html += "<tr>";
							html += "<td colspan=3 align='center'>描述图片</td>";
							html += "</tr>";
						} 
						isdesImages ++;
						if(isdesImages == 1){
							html += "<tr>";
						}
						html += "<td id='isdesImages"+isdesImages+"'> ";
						html += "<img alt='' style='width:100%;cursor: pointer;' src= '"+ data.publishImages[i].picUrl +"?iopcmd=thumbnail&type=4&width=200'>";
						html += "</td>";
					}
				}
				if(isdesImages >= 1 ){
					if(isdesImages == 1){
						html += "<td></td><td></td>";
					} else if(isdesImages == 2) {
						html +="<td></td>";
					}
					html += "</tr>";
				}
				
				for(var i=0;i<data.publishImages.length;i++){
					if( data.publishImages[i].type == 2 ){
						if(iscerImages == 0 ){
							html += "<tr>";
							html += "<td colspan=3 align='center'>证书图片</td>";
							html += "</tr>";
						} 
						iscerImages ++;
						if(iscerImages == 1){
							html += "<tr>";
						}
						html += "<td id='iscerImages"+iscerImages+"'> ";
						html += "<img alt='' style='width:100%;cursor: pointer;' src= '"+ data.publishImages[i].picUrl +"?iopcmd=thumbnail&type=4&width=200'>";
						html += "</td>";
					} 
				}
				if(iscerImages >= 1 ){
					if(iscerImages == 1){
						html += "<td></td><td></td>";
					} else if(iscerImages == 2) {
						html +="<td></td>";
					}
					html += "</tr>";
				}
			} else {
				html += "<tr><td colspan=3 aligh='center'>未上传图片</td></tr>";
			}
			$("#publisImageTable #imageDown").after(html);
			
			if(data.type == '0'){ // 买买买不显示服务内容
				$("#serviceType").html('买买买');	
				$("#serviceAddress").html(data.address);
				$("#maimaimaiContent").hide();
			} else {
				$("#serviceType").html('卖卖卖');
				$("#serviceContent").html(data.content);
				$("#maimaimaiAddress").hide();
			}
			if( "" != $.trim(data.publishTimes) && undefined != data.publishTimes){
				var publishTime = "";
				for(var i=0;i<data.publishTimes.length;i++){
					if(data.publishTimes.length != 1){
						publishTime += "<br>";						
					}
					var date = new Date();
					date.setTime(data.publishTimes[i].startDate);
					var a =date.Format('yyyy年MM月dd日 hh时mm分');
					publishTime += a; 
					date.setTime(data.publishTimes[i].endDate);
					a =date.Format('yyyy年MM月dd日 hh时mm分');
					publishTime += "——" +  a;
				}
				$("#serviceTime").html(publishTime);
			}
			$("#showKeywords").html(data.keywords);
			$("#title").html(data.title);
			if (data.idAudit == 1){
				$("#idAudit").html('身份已验证');
			}else {
				$("#idAudit").html('身份未验证');
			}
			$("#price").html(data.price+"元");
			$("#userName").html(data.userName);
			$("#touchMoblie").html(data.mobile);
			$("#description").html(data.description);
			$("#range").html(data.range);
			var date = new Date();
			date.setTime(data.createdTime);
			var a =date.Format('yyyy年MM月dd日 hh时mm分ss秒');
			$("#createdTime").html(a);
			
			if(flag == 0 ){
				$("#revocationTR").hide();
				$("#publishRevocationBtn").hide();
				$("#publishNoBtn").show();
				$("#publishOkBtn").show();
			} else {
				$("#publishNoBtn").hide();
				$("#publishOkBtn").hide();
				$("#revocationTR").show();
				$("#publishRevocationBtn").show();
			}
			
			$("#publisAuditShowID").modal();
		});
		
		$("#publisAuditShowID").on('show.zui.modal', function() {
			$("#reason").val(""); // 初始弹出审核框框的时候,将审核理由清空
		});
	}
</script>
</head>
<body>
	<m:list title="审核列表" id="publishList"
		listUrl="${contextPath }/publish/list-data" 
		searchButtonId="user_search_btn">
		<div class="input-group" style="max-width: 1200px;">
			<span  class="input-group-addon">开始时间</span> 
			<input id = "startTime"  type="text"    name="startTime" class="form-control form-date" placeholder="选择开始日期" readonly  style="width: 200px;">
			
			<span  class="input-group-addon">结束时间</span> 
			<input id = "endTime"  type="text"	name="endTime" class="form-control form-date" placeholder="选择结束日期" readonly style="width: 200px;">
			
			<span class="input-group-addon">关键字</span> 
			<input type="text"	name="keywords" id="keywords" class="form-control" placeholder="关键字" style="width: 200px;">
				
			<span class="input-group-addon">服务类型</span>
           	<select name="type" class="form-control" style="width: 200px;" id="type">
           		<option id="all" value="-10" selected="selected">全部</option>
           		<option id="first" value="0">买买买</option>
           		<option id="middle" value="1">卖卖卖</option>
           	</select>
			
			<span class="input-group-addon">审核状态</span>
           	<select name="status" class="form-control" style="width: 200px;" id="status">
           		<option id="first" value="-10">全部</option>
           		<option id="second" value="0">审核中</option>
           		<option id="middle" value="1">审核通过</option>
           		<option id="middle" value="2">撤销</option>
           		<option id="three" value="3">审核不通过</option>
           	</select>
           
		</div>
	</m:list>
	
	<div class="modal fade" id="publisAuditShowID" >
	 <div class="modal-dialog modal-lg">
	  <div class="modal-content">
		    <div class="modal-header">
		      <button type="button" id="auditClose" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
		      <h4 class="modal-title">发布审核(图片)</h4>
		    </div>
		    <div class="modal-body">
		<div class='panel-body'>
				<input type="hidden" id="id" name="id"/>
				<input type="hidden" id="userId" name="userId"/>
				<input type="hidden" id="titleCache" name="titleCache"/>
				<table class="table table-hover table-striped table-bordered" id="publisImageTable" cellpadding="0" cellspacing="0">
				<tr>
					<td colspan="3">服务类型：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="serviceType"></span></td>
				</tr>
	
				<tr>
					<td colspan="3" align="right"><span id="idAudit"></span></td>
				</tr>
				
				<tr>
					<td colspan="3">创建时间：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="createdTime"></span></td>
				</tr>

				<tr>
					<td colspan="3">发布金额：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="price"></span></td>
				</tr>
				
				<tr>
					<td colspan="3">关键字：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="showKeywords"></span></td>
				</tr>
				
				<tr>
					<td colspan="3">标题：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="title"></span></td>
				</tr>
				
				<tr>
					<td colspan="3">发布人：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="userName"></span></td>
				</tr>

				<tr>
					<td colspan="3">联系电话：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="touchMoblie"></span></td>
				</tr>
								
				<tr>
					<td colspan="3">发布范围：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="range"></span></td>
				</tr>
				
				<tr>
					<td colspan="3">服务时间：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="serviceTime"></span></td>
				</tr>
				
				<tr id="maimaimaiContent">
					<td colspan="3">服务内容：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="serviceContent"></span></td>
				</tr>
				
				<tr id="maimaimaiAddress">
					<td colspan="3">服务地址：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="serviceAddress"></span></td>
				</tr>
				
				<tr id="imageDown">
					<td colspan="3">描述信息：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="description"></span></td>
				</tr>
				
				<tr id="revocationTR">
					<td colspan="3">撤销理由：<textarea id="reason" rows="4" cols="120" maxlength="50"></textarea></td>
				</tr>
				
				<tr>
					<td align="center" style="width:33%" id="noButton"><input type="button" onclick="publishAuditShow(3);"  id='publishNoBtn' class='btn btn-primary' value="不通过"  /></td>
					<td align="center" style="width:33%" id="revocationButton"><input type="button" onclick="publishAuditShow(2);" id='publishRevocationBtn' class='btn btn-primary' value="撤销审核状态" /></td>
					<td align="center" style="width:33%" id="yesButton"><input type="button" onclick="publishAuditShow(1);" id='publishOkBtn' class='btn btn-primary' value="通过" /></td>
				</tr>			
			</table>
		</div>
		    </div>
	  </div>
	</div>
</div>
</body>
</html>