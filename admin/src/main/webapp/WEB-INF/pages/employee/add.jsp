<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty employee ? '添加' : '修改' }员工管理</title> 
<script type="text/javascript">
$(document).ready(function(){
	
	//性别
	var aa = "${empty employee.sex ? '0':employee.sex}";
	$("#employee_sex_" + aa).attr("checked", true);
	
	//性别
	var bb = "${empty employee.type ? '0':employee.type}";
	$("#employee_type_" + bb).attr("checked", true);
	
	// 保存信息start
	$("#employeeAddBtn").click(function(){
		var name = $("#name").val(); // 等级名称
		name = $.trim(name);
		
		if( name == "" || name == null ){
			lm.alert("员工名称不能为空!");
			return;
		}
		
		var isRepeat = false;
		var id = $("#id").val();
		lm.postSync("${contextPath }/employee/list/ajax/checkEmployeeName/", {id:id,name:name}, function(data) {
			if (data == 1) {
				lm.alert("员工名称存在重复,请重新输入!");
				isRepeat = true;
			} 
		});
		
		if (isRepeat) {
			return;
		}
		
		$("#employeeAddForm").submit();
	}); 
	// 保存信息end
});
</script>
</head>
<body>
	<div class='panel'>
	
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>${empty employee ? '添加' : '修改' }员工管理
			</strong>
		</div>
		
		<div class='panel-body'>
			<form id="employeeAddForm" method='post' repeatSubmit='1' class='form-horizontal' action="${contextPath }/employee/save">
				
				<input type="hidden" id="id" name="id" class='form-control' value="${employee.id }" maxlength="100"  >
				
				<div class="form-group">
					<label class="col-md-1 control-label">员工名称</label>
					<div class="col-md-2">
						<input type="text" id="name" name="name" class='form-control' value="${employee.name }" maxlength="100"  >
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">性别</label>
					<div class="col-md-2">
						男<input id="employee_sex_1"  type='radio' value="1" name="sex"/>
						女<input id="employee_sex_0"  type='radio' value="0" name="sex"/>
					</div>
				</div>
					
				<div class="form-group">
					<label class="col-md-1 control-label">类型</label>
					<div class="col-md-2" style="margin-top: 3px;">
						<input id="employee_type_0"  type='radio' value="0" name="type" checked="checked"/>导购员
						<input id="employee_type_1"  type='radio' value="1" name="type"/>收银员
					</div>
				</div>
						
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button" id='employeeAddBtn' class='btn btn-primary' value="${empty employee ? '添加' : '修改' }" data-loading='稍候...' />
					</div>
				</div>
				
			</form>
	  </div>
   </div>
</body>
</html>