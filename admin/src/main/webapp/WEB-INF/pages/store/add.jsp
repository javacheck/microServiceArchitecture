<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty store ? '添加' : '修改' }店铺</title>
<script type="text/javascript">
	$(function(){
		$("#addStoreBtn").click(function(){
			var name = $.trim($("#name").val());
			if (name == ""){
				lm.alert("请输入名称");
			}else if(name.length > 100){
				lm.alert("请输入长度不超过100个字符的名称");
			}else {
				$("#storeForm").submit();
			}
		});
	});
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong> <i class='icon-plust'></i>${empty store ? '添加' : '修改' }店铺
			</strong>
		</div>
		<div class='panel-body'>
			<form method='post' class='form-horizontal' id="storeForm"
				action="${contextPath }/store/add">
				<input name="id" type="hidden" value="${store.id }" />
				<div class="form-group">
					<label class="col-md-2 control-label">店铺名称</label>
					<div class="col-md-4">
						<input type='text' id="name" name="name" value="${store.name}"
							class='form-control' maxlength="50"/>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>

				<div class="form-group">
					<div class="col-md-offset-2 col-md-1 0">
						<input type='button' id='addStoreBtn' class='btn btn-primary' value="${empty store ? '添加' : '修改' }" />
					</div>
				</div>
			</form>
		</div>
	</div>

</body>
</html>