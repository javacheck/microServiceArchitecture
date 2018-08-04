<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty role ? '添加' : '修改' }角色 </title>
<script type="text/javascript">
	
	var v="";
	//初始化事件
	$(function() {
		//绑定 提交方法
		$("#accountAddBtn").click(function() {
   		 	//验证姓名
   		 	var name = $.trim($("#name").val());
   		 	if(name==null||name==""){
   		 		lm.alert("请输入角色名称");
   		 		return;
   		 	}
   		 	var isHave=false;
   		 	if($("#name").val()!=$("#decoraName").val()){
	   		 	lm.postSync("${contextPath }/role/ajax/checkName/"+name,{},function(data){
					if(data==1){
						lm.alert("角色名称已存在");
						isHave =true;
					}else{
					}
				});
   		 	}
   		 	if (isHave) {
				return ;
			}
	   		$("#roleAddForm").submit();
   		 	$("#name").val($.trim(name));
   		 	//提交
		});
		
		$("#name").keyup(function () {
			var name = $.trim($("#name").val());
			$("#name").val(name);
		});
		
		
	});
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong><i class='icon-plust'></i>${empty role ? '添加' : '修改' }角色  </strong>
		</div>
		<div class='panel-body'>
			<form id="roleAddForm" method='post' class='form-horizontal' repeatSubmit='1'
				action="${contextPath }/role/add">
				
				<input name="id" type="hidden" value="${role.id }" />
				<input id="decoraName" type="hidden" value="${role.name }" />
				
				<div class="form-group">
					<label class="col-md-2 control-label">角色</label>
					<div class="col-md-4">
						<input type='text' maxlength="12" id="name" name="name" value="${role.name }"
							class='form-control' />
					</div>
				</div>
				
				<div class="form-group">
					<div class="col-md-offset-2 col-md-10">
						<input type="button"  id='accountAddBtn' class='btn btn-primary' repeatSubmit='1'
							value="${empty role ? '添加' : '修改' }" data-loading='稍候...' />
					</div>
				</div>
			</form>
		</div>
	</div>

</body>
</html>