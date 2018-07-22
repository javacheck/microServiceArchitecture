<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty account ? '添加' : '修改' }帐号 </title>
<script type="text/javascript">
	function changeText(id){
		$("#parentId").text($("#"+id).text());
		if(id==-1){
			$("#parentIdInput").val("");
		}else{
			$("#parentIdInput").val(id);
		}
	}
	
	//手机号码重复验证
	$(function() {
		$("#mobile").change(function(){
			var mobile = $(this).val();
			if(!(/^1[3|4|5|8][0-9]\d{4,8}$/.test(mobile))){ 
        		alert("不是完整的11位手机号或者正确的手机号前七位"); 
        		return ; 
   		 	}
			lm.post("${contextPath }/account/checkMobile/"+mobile,{},function(data){
				if(data == 1){
					$("#error").text("用户重复");
				}else{
					$("#error").text("");
				}
			});
  			//$(this).css("background-color","#FFFFCC");
		});
	});
	

	//初始化事件
	$(function() {
	
		//性别
		var aa = "${account.sex}";
		if (aa == "" || aa == null) {
			$("#sex_0").attr("checked", true);
		} else {
			$("#sex_" + aa).attr("checked", true);
		}
		
		//角色
		var nowRoles = "${nowRoleIds}";
		$("input[name='roleIds']").each(
			function() {
				var thisVal=$(this).val();
				for (var i=0;i<nowRoles.length;i++){
					if(thisVal==nowRoles[i]){
						this.checked=true;
					}
				}
			}
		)
		
		//商店
		var defaultSelect= ${defaultSelect};
		$("select[name='storeId'] option").each(function(){
			var val = $(this).val();
			if( val == defaultSelect) {
				$(this).prop("selected",true)
			}
		});
		
		
		
		

		//绑定 提交方法
		$("#accountAddBtn").click(function() {
			
			//验证手机
			if(!(/^1[3|4|5|8][0-9]\d{4,8}$/.test($("#mobile").val()))){ 
				alert("不是完整的11位手机号或者正确的手机号前七位"); 
				return ; 
				}
				//验证密码
				if($("#password").val()==""||$("#password").val()==null){
					alert("请输入密码");
					return;
				}
				//验证姓名
				if($("#name").val()==""||$("#name").val()==null){
					alert("请输入姓名");
					return;
				}
				//验证邮箱
				if(!(/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/.test($("#email").val()))){
					alert("邮箱格式不正确");
					return ;
				}
				//验证地址
				if($("#address").val()==""||$("#address").val()==null){
					alert("请输入地址");
					return;
				}
				//验证身份证号码
				if(!(/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test($("#idCard").val()))){
					alert("身份证号码输入不正确");
					return ;
				}
				//角色校检
				var roleIds= $('input:checkbox[name="roleIds"]:checked').val();
				if(roleIds==null){
			    	alert("至少选择一个角色!");
			   		return ;
				}
			 
			//验证上级管理
			//if($("#parentIdInput").val()==""||$("#parentIdInput").val()==null){
			//	alert("请选择上级管理");
			//	return;
			//}
			//提交
			$("#accountAddForm").submit();
		});
	});
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong><i class='icon-plust'></i>${empty account ? '添加' : '修改' }账号  </strong>
		</div>
		<div class='panel-body'>
			<form id="accountAddForm" method='post' class='form-horizontal'
				action="${contextPath }/account/add">
				
				<input name="id" type="hidden" value="${account.id }" />

				<div class="form-group">
					<label class="col-md-2 control-label">手机</label>
					<div class="col-md-4">
						<input type='text' id="mobile" name="mobile"
							value="${account.mobile }" class='form-control' />
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" id="error">*</label>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" id="error"></label>
				</div>
				
				<div class="form-group">
					<label class="col-md-2 control-label">密码</label>
					<div class="col-md-4">
						<input type="password" id="password" name="password"
							value="${account.password }" class='form-control' />
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" id="error">*</label>
				</div>
				
				
				
				<div class="form-group">
					<label class="col-md-2 control-label">姓名</label>
					<div class="col-md-4">
						<input type='text' id="name" name="name" value="${account.name }"
							class='form-control' />
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" id="error">*</label>
				</div>
				
				<div class="form-group">
					<label class="col-md-2 control-label">性别</label>
					<div class="col-md-4">
						男<input id="sex_0"  type='radio' value="0" name="sex"/>
						女<input id="sex_1"  type='radio' value="1" name="sex"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-2 control-label">邮箱</label>
					<div class="col-md-4">
						<input type='text' id="email" name="email"
							value="${account.email }" class='form-control' />
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" id="error">*</label>
				</div>
				
				<div class="form-group">
					<label class="col-md-2 control-label">地址</label>
					<div class="col-md-4">
						<input type='text' id="address" name="address"
							value="${account.address }" class='form-control' />
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" id="error">*</label>
				</div>
				
				<div class="form-group">
					<label class="col-md-2 control-label">身份证号码</label>
					<div class="col-md-4">
						<input type='text' id="idCard" name="idCard"
							value="${account.idCard }" class='form-control' />
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" id="error">*</label>
				</div>
				
				<div class="form-group">
					<label class="col-md-2 control-label">上级</label>
					
					<input type="hidden" id = "parentIdInput" name ="parentId" value="${account.parentId}" >
					
					<div class="col-md-4">
							<div class="btn-group">
								<button class="btn dropdown-toggle" type="button" id="dropdownMenu1"
									data-toggle="dropdown">	 
									<c:if test="${not empty account}">
										<c:if test="${not empty parentAccount}">
											<span id = "parentId"> ${parentAccount.name}</span> 
										</c:if>
										<c:if test="${empty parentAccount}">
											<span id = "parentId">选择上级领导</span> 
										</c:if>
									</c:if>
									<c:if test="${empty account}">
										<span id = "parentId">选择上级领导</span> 
									</c:if>
								</button>
								<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
									<li><a id="-1"  onclick="changeText('-1')" >选择上级领导</a></li>
									<c:forEach items="${list }" var="accountl">
									<li><a id="${accountl.id }"  onclick="changeText('${accountl.id }')" >${accountl.name }</a></li>
									</c:forEach>
								</ul>
							</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">角色</label>
					<div class="col-md-10">
					<c:forEach items="${roles }" var="role">
					 	<label class="checkbox-inline"> <input type="checkbox" name="roleIds" value="${role.id }"> ${role.name } </label>
		            </c:forEach>  
		            </div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">商店</label>
					<div class="col-md-10">
						<select id = "selectId" name ="storeId" class='form-control' style="width: auto">
							<c:forEach items="${stores }" var="store">
								<option id="${store.id}"  value="${store.id}">${store.name}</option>
		           			 </c:forEach>  
						</select>
		            </div>
				</div>
				<div class="form-group">
					<div class="col-md-offset-2 col-md-10">
						<input type="button"  id='accountAddBtn' class='btn btn-primary'
							value="${empty account ? '添加' : '修改' }" data-loading='稍候...' />
					</div>
				</div>
			</form>
		</div>
	</div>

</body>
</html>