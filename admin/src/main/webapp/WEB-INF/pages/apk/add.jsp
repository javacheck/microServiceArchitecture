<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty apk ? '添加' : '修改' }APK</title> 
<script type="text/javascript">

$(document).ready(function(){
	var id = $("#id").val();
	if( null != id && "" != id){ // 修改
		$("#type").val('${apk.type}');
		// 修改时判断更新条件状态
		$("input:radio[name='needingUpdate']").each(function(key,value){
			 if( $("#needingUpdateCache").val() == $(value).val() ){
				 $(value).attr("checked",true);
				 return false;
			 }			
		});
	}
	// 保存商品库存信息
	$("#apkAddBtn").click(function(){
		var name = $("#name").val(); // 
		var version = $("#version").val(); // 
		name = $.trim(name);
		version = $.trim(version);
		
		if( name == "" || name == null ){
			lm.alert("APK名称不能为空!");
			return;
		}

		if( !/^(0|\+?[1-9][0-9]*)$/.test(version)){
			lm.alert("APK版本输入错误!");
			$("#version").focus();
			return;
		}
		
		if( version == "" || version == null ){
			lm.alert("APK版本不能为空!");
			return;
		}
		var isRepeat = false;
		var id = $("#id").val();
		lm.postSync("${contextPath }/apk/list/ajax/checkApkName/", {id:id,name:name}, function(data) {
			if (data == 1) {
				lm.alert("APK名称存在重复,请重新输入!");
				$("#name").focus();
				isRepeat = true;
			} 
		});
		
		if (isRepeat) {
			return;
		}
		
		$("#apkAddForm").submit();
	}); 
	
});

</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>${empty apk ? '添加' : '修改' }APK
			</strong>
		</div>
		<div class='panel-body'>
			<form id="apkAddForm" method='post' repeatSubmit='1' class='form-horizontal' action="${contextPath }/apk/save">
			<input type="hidden" name="id" class='form-control' id="id"  value="${apk.id }" maxlength="100"  >
			<input type="hidden" name="needingUpdateCache" class='form-control' id="needingUpdateCache"  value="${apk.needingUpdate }" maxlength="100"  >
				<div class="form-group">
					<label class="col-md-1 control-label">APK类型</label>
					<div class="col-md-2">
						<select id="type" name="type" class='form-control'> 
							<option id="0" value="0" selected="selected">随身社区APP</option>
							<option id="1" value="1">商户APP</option>
							<option id="2" value="2">POSAPP</option>
							<option id="3" value="3">应用中心</option>
						</select>
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">APK名称</label>
					<div class="col-md-2">
						<input type="text" name="name" class='form-control' id="name"  value="${apk.name }" maxlength="200"  >
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
								
				<div class="form-group">
					<label class="col-md-1 control-label">APK版本</label>
					<div class="col-md-2">
						<input type="text" id="version" name="version" value="${apk.version }"  class='form-control' maxlength="7"/>
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">强制更新</label>
					<div class="col-md-2">
						<input type="radio" name="needingUpdate" value="1"/>是
						<input type="radio" name="needingUpdate" value="0" checked="checked"/>否
					</div>
				</div>
								
				<div class="form-group">
						<label class="col-md-1 control-label">备注</label>
						<div class="col-md-2">
							<textarea id='memo'  name='memo' alt='最大输入字数为150个字符' cols=15 rows=5 class='form-control' maxlength='150'>${apk.memo }</textarea>
						</div>
					</div>
					
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button"  id='apkAddBtn' class='btn btn-primary'
							value="${empty apk ? '添加' : '修改' }" data-loading='稍候...' />
					</div>
				</div>
			</form>
	  </div>
   </div>
</body>
</html>