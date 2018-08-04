<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="${contextPath}/static/js/map/map.js"></script>  
<title>时间段设置</title>
<style type="text/css">
	.panel-head{
		border-bottom: 1px solid #ccc;
    	margin-bottom: 20px;
	}
	.panel-h{
		border: 1px solid #ccc; 
		margin-bottom:20px;
	}

</style>
<script type="text/javascript">
var record = 1000; // 为了避免重复,将值设置到很高
var record_Festivals = 2000;
$(document).ready(function(){
	$("a[name='dateSettingAdd_General']").removeAttr('href'); // 让A标签失效
	$("a[name='dateSettingAdd_Festivals']").removeAttr('href'); // 让A标签失效

	$("a[name='dateSettingAdd_General']").click(function(){
		var countNum = $("label[name='designation']").length;
		countNum++;
		var element = $("#dateSetting_General_show");
		 record = record+1;
		 element.append("<div class='form-group' id="+ record +"><label class='col-md-1 control-label' name='designation'>名称"+countNum+" </label> <div class='col-md-2'><input type='hidden' name='typeArray' value='3' class='form-control' maxlength='20'/><input type='hidden' name='idArray' value='-1000' class='form-control' maxlength='20'/><input type='text' placeholder='请输入该特殊时间段的名称' name='nameArray' class='form-control' maxlength='20'/></div><label class='' style='float: left;padding-top: 6px'>时间段</label> <div class='col-md-1'><select name='startTimeArray' class='form-control' onchange='checkData(this);return;'></select></div> <label class='' style='float: left;padding-top: 6px'>至</label> <div class='col-md-1'><select name='endTimeArray' class='form-control' onchange='checkData(this);return;'></select></div> <a onclick='dateSettingDelete(this,3);' name='deletebutton' class='btn btn-small btn-danger'>删除</a></div>");
		 buildOption($("#"+record).find("select"));
	});
	
	$("a[name='dateSettingAdd_Festivals']").click(function(){
		var countNum = $("label[name='designation_Festivals']").length;
		countNum++;
		var element = $("#dateSetting_Festivals_show");
		record_Festivals = record_Festivals+1;
		element.append("<div class='form-group' id="+ record_Festivals +"><label class='col-md-1 control-label' name='designation_Festivals'>名称"+countNum+" </label> <div class='col-md-2'><input type='hidden' name='typeArray' value='5' class='form-control' maxlength='20'/><input type='hidden' name='idArray' value='-1000' class='form-control' maxlength='20'/><input type='text' placeholder='请输入该特殊时间段的名称' name='nameArray' class='form-control' maxlength='20'/></div><label class='' style='float: left;padding-top: 6px'>时间段</label> <div class='col-md-1'><select name='startTimeArray' class='form-control' onchange='checkData(this);return;'></select></div> <label class='' style='float: left;padding-top: 6px'>至</label> <div class='col-md-1'><select name='endTimeArray' class='form-control' onchange='checkData(this);return;'></select></div> <a onclick='dateSettingDelete(this,5);' name='deletebutton' class='btn btn-small btn-danger'>删除</a></div>");
		buildOption($("#"+record_Festivals).find("select"));
	});
	
	$("#roomDateSettingAddBtn").click(function(){
		var flag = false;
		var distinctMap = new Map();
		var countNumber = 0;
		$("#dateSetting_General_show").find("input[name='nameArray']").each(function(key ,value){
			var dateSettingName = $(this).val();
			dateSettingName = $.trim(dateSettingName);  
			if( null == dateSettingName || "" == dateSettingName ){
				lm.alert("普通时间段名称不能为空");
				distinctMap.clear(); // 清除
				flag = true;
				return ;
			}
			countNumber++;
			if( distinctMap.isEmpty() ){
				distinctMap.put(dateSettingName,dateSettingName);
			} else {
				if( distinctMap.containsKey(dateSettingName) ){
					return;
				} else {					
					distinctMap.put(dateSettingName,dateSettingName);					
				}
			}
		});
		
		if(flag){
			return;
		}
		
		if( countNumber == 0 || distinctMap.isEmpty() || countNumber != distinctMap.size() ){
			lm.alert("普通时间段名称存在重复");
			return;	
		}
		
		flag = false;
		distinctMap.clear();
		countNumber = 0;
		
		$("#dateSetting_Festivals_show").find("input[name='nameArray']").each(function(key ,value){
			var dateSettingName = $(this).val();
			dateSettingName = $.trim(dateSettingName);  
			if( null == dateSettingName || "" == dateSettingName ){
				lm.alert("节假日时间段名称不能为空");
				distinctMap.clear(); // 清除
				flag = true;
				return ;
			}
			countNumber++;
			if( distinctMap.isEmpty() ){
				distinctMap.put(dateSettingName,dateSettingName);
			} else {
				if( distinctMap.containsKey(dateSettingName) ){
					return;
				} else {					
					distinctMap.put(dateSettingName,dateSettingName);					
				}
			}
		});
		
		if(flag){
			return;
		}
		
		if( countNumber == 0 || distinctMap.isEmpty() || countNumber != distinctMap.size() ){
			lm.alert("节假日时间段名称存在重复");
			return;	
		}
		
		var timeSetArray = new Array();
		var numberCout = 0;
		var stringLink = "";
		$("#dateSetting_General_show").find("select").each(function(key ,value){
			++numberCout;
			if( "" == stringLink ){
				stringLink = $(this).val();
			}
			if( numberCout % 2 == 0 ){ // 成双成对
				stringLink = stringLink +"_"+ $(this).val();
				timeSetArray.push(stringLink);
				stringLink = "";
			}
		});
				
		var sign = false;
		for(  var i = 0 ;i < timeSetArray.length ; i++ ){
			var source = timeSetArray[i].split("_");
			var sourceStart = source[0];
			var sourceEnd = source[1];
			
			for( var j = 0 ; j< timeSetArray.length ; j++ ){
				if( i == j ){
					continue ; // 实现continue功能
				}
				var dest = timeSetArray[j].split("_");
				var destStart = dest[0];
				var destEnd = dest[1];
				if( parseInt(destEnd) <= parseInt(sourceStart) || parseInt(destStart) >= parseInt(sourceEnd) ){
					
				} else {
					lm.alert("普通时间段存在重复");
					sign = true;
					break;
				}
			}
			if(sign){
				break;
			}
		}
		
		if(sign){
			return;
		}
	
		timeSetArray.length = 0;
		numberCout = 0;
		stringLink = "";
		$("#dateSetting_Festivals_show").find("select").each(function(key ,value){
			++numberCout;
			if( "" == stringLink ){
				stringLink = $(this).val();
			}
			if( numberCout % 2 == 0 ){ // 成双成对
				stringLink = stringLink +"_"+ $(this).val();
				timeSetArray.push(stringLink);
				stringLink = "";
			}
		});
				
		var sign = false;
		for(  var i = 0 ;i < timeSetArray.length ; i++ ){
			var source = timeSetArray[i].split("_");
			var sourceStart = source[0];
			var sourceEnd = source[1];
			
			for( var j = 0 ; j< timeSetArray.length ; j++ ){
				if( i == j ){
					continue ; // 实现continue功能
				}
				var dest = timeSetArray[j].split("_");
				var destStart = dest[0];
				var destEnd = dest[1];
				if( parseInt(destEnd) <= parseInt(sourceStart) || parseInt(destStart) >= parseInt(sourceEnd) ){
					
				} else {
					lm.alert("节假日时间段存在重复");
					sign = true;
					break;
				}
			}
			if(sign){
				break;
			}
		}
		
		if(sign){
			return;
		}
		$("#roomDateSettingAddForm").submit();
	});
});

function dateSettingDelete(obj,type){
	//countNum--;
	var idObj = $(obj).parent().find("input[name='idArray']");
	if( undefined == idObj || "" == idObj ){
		return false;
	}
	var idValue = idObj.val();
	var isFlag = true;
	if( idValue != -1000 ){
		// 去后台删除数据
		lm.postSync("${contextPath}/room/roomDateSetting/ajax/delete",{id:idValue},function(data){
			if( data == 0 ){
				lm.alert("删除失败");
				isFlag = false;
				return;
			} else if( data == 2 ) {
				lm.alert("与其他数据存在交互,不能删除");
				isFlag = false;
				return;
			}
		});
	} 
	if(isFlag){
		$(obj).parent().remove();	
		if( type == 3 ){
			$("label[name='designation']").each(function(key,value){
				$(value).html("名称"+(key+1));
			});			
		} else {
			$("label[name='designation_Festivals']").each(function(key,value){
				$(value).html("名称"+(key+1));
			});
		}
	}
}

function buildOption(_this){
	if( $(_this).find("option").length <= 0  ){
		if( $(_this).length >= 2 ){ // 两个select
			var first = $(_this)[0];
			var end = $(_this)[1];
			for(var i=0 ;i <= 24 ; i++){
				if( i== 0 ){
					$(first).append("<option selected='selected'>"+i+"</option>");
				} else {
					$(first).append("<option>"+i+"</option>");					
				}
			}
			for(var i=0 ;i <= 24 ; i++){
				if( i== 24 ){
					$(end).append("<option selected='selected'>"+i+"</option>");
				} else {
					$(end).append("<option>"+i+"</option>");					
				}
			}
			
		} else { // 单个select
			for(var i=0 ;i <= 24 ; i++){
				$(_this).append("<option>"+i+"</option>");
			}
			var firstOrend = $(_this).attr("name");
			if( firstOrend == "endTimeArray" ){
				$(_this).val(24);
			} else {
				$(_this).val(0);
			}
		}
	}
}
var GlobalSelectValue; // 全局保存选择的下拉选项值
function getSelectValue(_this){
	GlobalSelectValue = $(_this).val();
}
function checkData(_this){
	var nextOrPrev = $(_this).attr("name");
	var thisValue = $(_this).val();
	if( nextOrPrev == "endTimeArray" ){
		var prev = ($(_this).parent().prev().prev()).find("select").val();
		// 结束时间小于开始时间
		if( parseInt(thisValue) <= parseInt(prev) ){
			lm.alert("结束时间不能比开始时间小");
			if( undefined == GlobalSelectValue || parseInt(GlobalSelectValue) < parseInt(prev) ){
				if( prev == 24 ){
					($(_this).parent().prev().prev()).find("select").val(0);
				}
				GlobalSelectValue = 24;
			}
			$(_this).val(GlobalSelectValue);				
			return false;
		}
	} else {
		var next =  ($(_this).parent().next().next()).find("select").val();
		// 开始时间大于结束时间
		if( parseInt(thisValue) >= parseInt(next) ){
			lm.alert("开始时间不能大于结束时间");
			if( undefined == GlobalSelectValue || parseInt(GlobalSelectValue) > parseInt(next) ){
				if( next == 1 ){
					($(_this).parent().next().next()).find("select").val(24);
				}
				GlobalSelectValue = 0;
			}
			$(_this).val(GlobalSelectValue);				
			return false;
		}
	}
}
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong><i class='icon-plust'></i>时间段设置 </strong>
		</div>
		<div class='panel-body'>
			<form id="roomDateSettingAddForm" repeatSubmit='1' method='post' class='form-horizontal' action="${contextPath }/room/roomDateSetting/save">
				<div class='panel-h'>
					<div class='panel-heading panel-head'>
						<strong><i class='icon-plust'></i>普通时间段设置 </strong>
					</div>
					<!-- 重要标识 -->
					<input type="hidden" value="3" name="type"/>
					
					<div id="dateSetting_General_show">
						<c:if test="${not empty dateSettingGeneralList }">
							<c:forEach items="${dateSettingGeneralList }" varStatus="sortNo" var="dateSettingGeneralList">
								<c:set var="v" value="${ sortNo.index+1}"></c:set>
								<div class="form-group" id="${v}">
									<label class="col-md-1 control-label" name='designation'>名称${v}</label>
									<div class="col-md-2">
										<input type="hidden" name="typeArray" value="${dateSettingGeneralList.type}" class='form-control' maxlength="20"/>
										<input type="hidden" name="idArray" value="${dateSettingGeneralList.id}" class='form-control' maxlength="20"/>
										<input type="text" name="nameArray" placeholder="请输入该特殊时间段的名称" value="${dateSettingGeneralList.name}" class='form-control' maxlength="20"/>
									</div>
									<label class="" style="float: left;padding-top: 6px">时间段</label>
									<div class="col-md-1">
										<select class="form-control" name="startTimeArray" onclick="getSelectValue(this);return;" onchange="checkData(this);return;">
											<c:forEach begin="0" end="24" var="optionId">
												<option value="${optionId }" <c:if test="${dateSettingGeneralList.startTime == optionId }"> selected = 'selected'</c:if>	>							
													${optionId }
												</option>
											</c:forEach>
										</select>
									</div>
									<label class="" style="float: left;padding-top: 6px">至</label>
									<div class="col-md-1">
										<select class="form-control" name="endTimeArray" onclick="getSelectValue(this);return;" onchange="checkData(this);return;">
											<c:forEach begin="0" end="24" var="optionId">
												<option value="${optionId }" <c:if test="${dateSettingGeneralList.endTime == optionId }"> selected = 'selected'</c:if>	>							
													${optionId }
												</option>
											</c:forEach>
										</select>
									</div>
									<c:if test="${sortNo.first }">
										<a href="#" class="btn btn-primary" name="dateSettingAdd_General" style="margin-top: 5px;"><i class="icon-plus"></i></a>								
									</c:if>
									<c:if test="${v != 1}">
										<a onclick="dateSettingDelete(this,3);" name="deletebutton" class="btn btn-small btn-danger">删除</a>									
									</c:if>
								</div>
							</c:forEach>
						</c:if>
						<c:if test="${empty dateSettingGeneralList }">
							<div class="form-group" id="0">
								<label class="col-md-1 control-label" name='designation'>名称1</label>
								<div class="col-md-2">
									<input type="hidden" name="typeArray" value="3" class='form-control' maxlength="20"/>
									<input type="hidden" name="idArray" value="-1000" class='form-control' maxlength="20"/>
									<input type="text" name="nameArray"  placeholder="请输入该特殊时间段的名称" value="" class='form-control' maxlength="20"/>
								</div>
								<label class="" style="float: left;padding-top: 6px">时间段</label>
								<div class="col-md-1">
									<select class="form-control" name="startTimeArray" onchange="checkData(this);return;">
										<c:forEach begin="0" end="24" var="optionId">
												<option value="${optionId }">							
													${optionId }
												</option>
										</c:forEach>
									</select>
								</div>
								<label class="" style="float: left;padding-top: 6px">至</label>
								<div class="col-md-1">
									<select class="form-control" name="endTimeArray" onchange="checkData(this);return;">
										<c:forEach begin="0" end="24" var="optionId">
												<option value="${optionId }" <c:if test="${24 == optionId }"> selected = 'selected'</c:if>	>							
													${optionId }
												</option>
										</c:forEach>
									</select>
								</div>
								<a href="#" class="btn btn-primary" name="dateSettingAdd_General" style="margin-top: 5px;"><i class="icon-plus"></i></a>
							</div>
						</c:if>
					</div>
				</div>
				
				<div class='panel-h'>
					<div class='panel-heading panel-head'>
						<strong><i class='icon-plust'></i>节假日时间段设置 </strong>
					</div>
					
					<div id="dateSetting_Festivals_show">
						<c:if test="${not empty dateSettingFestivalsList }">
							<c:forEach items="${dateSettingFestivalsList }" varStatus="sortNo" var="dateSettingFestivalsList">
								<c:set var="v" value="${ sortNo.index+1}"></c:set>
								<div class="form-group" id="${v}">
									<label class="col-md-1 control-label" name='designation_Festivals'>名称${v}</label>
									<div class="col-md-2">
										<input type="hidden" name="typeArray" value="${dateSettingFestivalsList.type}" class='form-control' maxlength="20"/>
										<input type="hidden" name="idArray" value="${dateSettingFestivalsList.id}" class='form-control' maxlength="20"/>
										<input type="text" name="nameArray" placeholder="请输入该特殊时间段的名称" value="${dateSettingFestivalsList.name}" class='form-control' maxlength="20"/>
									</div>
									<label class="" style="float: left;padding-top: 6px">时间段</label>
									<div class="col-md-1">
										<select class="form-control" name="startTimeArray" onclick="getSelectValue(this);return;" onchange="checkData(this);return;">
											<c:forEach begin="0" end="24" var="optionId">
												<option value="${optionId }" <c:if test="${dateSettingFestivalsList.startTime == optionId }"> selected = 'selected'</c:if>	>							
													${optionId }
												</option>
											</c:forEach>
										</select>
									</div>
									<label class="" style="float: left;padding-top: 6px">至</label>
									<div class="col-md-1">
										<select class="form-control" name="endTimeArray" onclick="getSelectValue(this);return;" onchange="checkData(this);return;">
											<c:forEach begin="0" end="24" var="optionId">
												<option value="${optionId }" <c:if test="${dateSettingFestivalsList.endTime == optionId }"> selected = 'selected'</c:if>	>							
													${optionId }
												</option>
											</c:forEach>
										</select>
									</div>
									<c:if test="${sortNo.first }">
										<a href="#" class="btn btn-primary" name="dateSettingAdd_Festivals" style="margin-top: 5px;"><i class="icon-plus"></i></a>								
									</c:if>
									<c:if test="${v != 1}">
										<a onclick="dateSettingDelete(this,5);" name="deletebutton" class="btn btn-small btn-danger">删除</a>									
									</c:if>
								</div>
							</c:forEach>
						</c:if>
						<c:if test="${empty dateSettingFestivalsList }">
							<div class="form-group" id="0">
								<label class="col-md-1 control-label" name='designation_Festivals'>名称1</label>
								<div class="col-md-2">
									<input type="hidden" name="typeArray" value="5" class='form-control' maxlength="20"/>
									<input type="hidden" name="idArray" value="-1000" class='form-control' maxlength="20"/>
									<input type="text" name="nameArray"  placeholder="请输入该特殊时间段的名称" value="" class='form-control' maxlength="20"/>
								</div>
								<label class="" style="float: left;padding-top: 6px">时间段</label>
								<div class="col-md-1">
									<select class="form-control" name="startTimeArray" onchange="checkData(this);return;">
										<c:forEach begin="0" end="24" var="optionId">
												<option value="${optionId }">							
													${optionId }
												</option>
										</c:forEach>
									</select>
								</div>
								<label class="" style="float: left;padding-top: 6px">至</label>
								<div class="col-md-1">
									<select class="form-control" name="endTimeArray" onchange="checkData(this);return;">
										<c:forEach begin="0" end="24" var="optionId">
												<option value="${optionId }" <c:if test="${24 == optionId }"> selected = 'selected'</c:if>	>							
													${optionId }
												</option>
										</c:forEach>
									</select>
								</div>
								<a href="#" class="btn btn-primary" name="dateSettingAdd_Festivals" style="margin-top: 5px;"><i class="icon-plus"></i></a>
							</div>
						</c:if>
					</div>
					
				</div>
				
					 <div class="form-group">
						<div class="col-md-offset-1 col-md-10">
							<input type="button" repeatSubmit='1'  id='roomDateSettingAddBtn' class='btn btn-primary' value="保存" data-loading='稍候...' />
						</div>
					</div>
			</form>
		</div>
	</div>

</body>
</html>