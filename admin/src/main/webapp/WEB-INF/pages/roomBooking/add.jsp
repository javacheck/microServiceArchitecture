<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>包间预订 </title>
<script type="text/javascript">
	var contextPath = "${contextPath}";
	//初始化事件
	$(function() {
		
		// 不可修改
		var status ="${roomBooking.status}";
		if (status!=0) {
			$("input").each(function() {
				$(this).attr("disabled","disabled");
				$("#dateSettingId").attr("disabled","disabled");
			});
		}
		
		var dateSettingId  = "${roomBooking.dateSettingId}";
		if(dateSettingId != ''){
			$("#dateSettingId").val(dateSettingId);
		}
		
		var format = "yyyy-MM-dd hh:mm";//默认格式
		var now = lm.Now;//当前日期
		var nowFormat=now.Format(format);
		
		$("#reserveDate").datetimepicker({
			minView: "hour", //选择日期后，不会再跳转去选择时分秒 
			format: "yyyy-mm-dd HH:ii", //选择日期后，文本框显示的日期格式 
			language: 'zh-CN', //汉化 
			autoclose:true , //选择日期后自动关闭
			todayBtn:true,//可选择当天按钮
			todayHighlight:false,//高亮当前日期
			startDate: nowFormat
		 });
		
		//绑定 提交按钮点击事件
		$("#roomBookingAddBtn").click(function() {
			$("#roomBookingAddForm").submit();
		});//提交按钮点击事件结束
		
		
		//绑定 提交按钮点击事件
		$("#roomBookingCancelBtn").click(function() {
			lm.confirm("确定要取消预订吗？", function() {
			lm.post("${contextPath }/roomBooking/changeStatus/",{status:3,id:"${roomBooking.id}"},function(data){
				if(data == 1){
					noty("取消预约成功");
					window.location.replace("${contextPath }/roomBooking/list/");
				}
			});
		});
		
	});//提交按钮点击事件结束
	
		$("#roomBookingAddForm").validator({
			timely :2,
		    rules: {
		    	TwoPointFloat:
				function(element, param, field) {
		    		// [/^[1-9]\d*([.]\d{1})?$/,'请输入正确的预定时长,例如:2或者1.5']
		    		if( !(/^[0-9]+([.]\d{1})?$/.test(element.value) && parseFloat(element.value) > 0 ) ){
		    			return '请输入正确的预定时长,例如:2或者1.5';
		    		}
		            return true;
		    	}    
		    },
		    fields: {
		    	reserveDuration :{
		    		rule:"required;TwoPointFloat;remote["+contextPath+"/roomBooking/checkBookingTime, id, roomId, reserveDate]"
		    		,must :true
		    	}
		    }
		});
	
});//初始化事件结束

</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong><i class='icon-plust'></i>包间预订  </strong>
		</div>
		<div class='panel-body'>
			<form id="roomBookingAddForm" repeatSubmit='1' method='post' class='form-horizontal'
				action="${contextPath }/roomBooking/add">
				
				<input name="roomId" id="roomId" type="hidden" value="${room.id }" />
				<input name="id" id="id" type="hidden" value="${roomBooking.id }" />
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px" class="">*</span>预订人</label>
					<div class="col-md-2">
						<input type='text' id="contacts" data-rule="required" name="contacts" maxlength="20"	value="${roomBooking.contacts }" class='form-control' />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px" class="">*</span>联系电话</label>
					<div class="col-md-2">
						<input type='text' id="phone" name="phone" maxlength="11"
							value="${roomBooking.phone }"  data-rule="required; mobile" data-rule-mobile="[/^1[3458]\d{9}$/, '请检查手机号格式']" class='form-control' />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label">房间号码</label>
					<div class="col-md-2 control-label" style="text-align: left;">
					${room.number }
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label">额定人数</label>
					<div class="col-md-2 control-label" style="text-align: left;">
					${room.persons }
					</div>
				</div>
				
				<div class="form-group">	
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px" class="">*</span>预订时间</label>
					<div class="col-md-2">
						<input type='text' id="reserveDate" name="reserveDate" readonly value="<fmt:formatDate value="${roomBooking.reserveDate }"
							pattern="yyyy-MM-dd HH:mm" />" class='form-control' />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px" class="">*</span>预定时长(小时)</label>
					<div class="col-md-2">
						<input type='text' id="reserveDuration" name="reserveDuration" maxlength="7"
							value="${roomBooking.reserveDuration }" class='form-control' />
					</div>
				</div>
				
				<div class="form-group">	
					<label class="col-md-1 control-label">预订备注</label>
					<div class="col-md-2">
						<input type='text' id="memo" name="memo" maxlength="100"
							value="${roomBooking.memo }" class='form-control' />
					</div>
				</div>
				
				<div class="form-group">
				
					<div class="col-md-offset-1 col-md-10">
						<c:if test="${not empty roomBooking && roomBooking.status ==0 }">
						<input type="button"  id='roomBookingCancelBtn' class='btn btn-danger'	value="取消预订" data-loading='稍候...' />
						</c:if>
						
						<c:if test="${empty roomBooking ||roomBooking.status ==0 }">
						<input type="button"  id='roomBookingAddBtn' class='btn btn-primary'value="${empty roomBooking ? '添加' : '修改' }" data-loading='稍候...' />
						</c:if>
					</div>
				</div>
			</form>
		</div>
	</div>

</body>
</html>