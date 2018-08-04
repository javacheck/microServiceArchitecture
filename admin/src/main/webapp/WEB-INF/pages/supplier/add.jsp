<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty supplier ? '添加' : '修改' }供应商</title> 
<script type="text/javascript">
function savesupplier(){
	var id=$("#id").val();
	var name=$("#name").val();
	if($.trim(name)==null || $.trim(name)==''){
		lm.alert("供应商名称不能为空！");
		$("#name").focus();
		return;
	}
 	var phone=$.trim($("#phone").val());//电话
 	if(phone==null || phone==''){
 		lm.alert("电话不能为空！");
 		$("#phone").focus();
		return;
 	}
	var flag=true;
	lm.postSync("${contextPath}/supplier/list/ajax/exist",{id:id,name:name},function(data){
		if(data==1){
			lm.alert("供应商名称已存在！");
			$("#name").focus();
			flag=false;
			return false;
		}
	});
	if(flag){
		$("#supplierAddBtn").prop("disabled","disabled");
		lm.post("${contextPath}/supplier/list/ajax/save",$("#supplierSave").serialize(),function(data){
			
			if(data=='1'){
		    	lm.alert("操作成功！");
		    	 window.location.href="${contextPath}/supplier/list";
			}
		}); 
	}
		
}

</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
			<i class='icon-plust'></i>${empty supplier ? '添加' : '修改' }供应商
			</strong>
		</div>
		<div class='panel-body'>
			<form method='post'  id="supplierSave" >
				<div  class='form-horizontal'>
					<input id="id" name="id" type="hidden" value="${supplier.id }" />
					
					
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>供应商名称</label>
						<div class="col-md-2">
							<input type='text' id="name" name="name" value="${supplier.name }" class='form-control'   maxlength="50"/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-1 control-label">联系人</label>
						<div class="col-md-2">
							<input type='text' id="contacts" name="contacts" value="${supplier.contacts }" class='form-control'   maxlength="50"/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>电话</label>
						<div class="col-md-2">
							<input type='text' id="phone" name="phone" value="${supplier.phone }" class='form-control'   maxlength="50"/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-1 control-label">地址</label>
						<div class="col-md-2">
							<input type='text' id="address" name="address" value="${supplier.address }" class='form-control'   maxlength="100"/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-1 control-label">备注</label>
						<div class="col-md-2">
							<textarea id='memo' name='memo' value='' alt='最大输入字数为200个字符' cols=15 rows=5 class='form-control' maxlength='200'>${supplier.memo }</textarea>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-offset-1 col-md-1 0" style="float:left">
							<input type="button"  id='supplierAddBtn' class='btn btn-primary' value="${empty supplier ? '添加' : '修改' }" onclick="savesupplier()" />
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>