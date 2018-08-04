<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty User ? '添加' : '修改' }会员</title>
<script type="text/javascript">
	
	
	$(function() {
		//商店
		var defaultSelect= "${User.storeId}";
		$("select[name='storeId'] option").each(function(){
			var val = $(this).val();
			if( val == defaultSelect) {
				$(this).prop("selected",true);
			}
		});
		
		var sex = "${user.sex}";
		if (sex == "" || sex == null) {
			$("#sex_0").attr("checked", true);
		} else {
			$("#sex_" + sex).attr("checked", true);
		}
		$("#mobile").change(function(){
			if(!checkMobile()){
				$("#errorMobile").text("不是正确的手机号码");
				$("#mobile").val('');
				return ;
			}
			if(isRepeatMobile()==true){
				$("#errorMobile").text("用户重复");
			}else{
				$("#errorMobile").text("");
			}
		});
		$("#selectId").change(function(){
			if(isRepeatMobile()){
				$("#errorMobile").text("用户重复");
			}else{
				$("#errorMobile").text("");
			}
		});
		$("#userAddbtn").click(function (){
			//验证姓名
			if ($.trim($("#name").val()) == ""|| $.trim($("#name").val()) == null) {
				alert("请输入姓名");
				return;
			}
			if(!checkMobile()){
				$("#errorMobile").text("不是正确的手机号码");
				$("#mobile").val('');
				return ;
			}
			if(isRepeatMobile()==true){
				$("#errorMobile").text("用户重复");
				return ;
			}else{
				$("#errorMobile").text("");
			}
			$("#userAddForm").submit();
		});
	});
	//验证手机号码输入是否合法
	function checkMobile(){
		var mobile = $("#mobile").val();
		if(/^1[3|4|5|8][0-9]\d{8}$/.test(mobile)){ 
	      	return true; 
	 	}
	 	return false; 
	 }
	 //验证手机号码和店铺是否同时重复
	 function isRepeatMobile(){
	 	var mobile = $("#mobile").val();
   		var storeId=$("#selectId").val();
   		var isRepeat =false;
   		if(mobile!=$('#decoraMobile').val()&& storeId!=$('#decoraStoreId').val()){
			lm.postSync("${contextPath }/user/checkMobile/",{'mobile':mobile,'storeId':storeId},function(data){
				if(data==1){isRepeat = true	;}
			});
   		 }
   		 return isRepeat;
	}
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong><i class='icon-plust'></i>${empty User ? '添加' : '修改' }会员</strong>
		</div>
		<div class='panel-body'>
			<form id ="userAddForm" method='post' class='form-horizontal'
				action="${contextPath }/user/add">
				<input name="id" type="hidden" value="${User.id }" />
				<input id="decoraStoreId" type="hidden" value="${User.storeId }" />
				<input id="decoraMobile" type="hidden" value="${User.mobile }" />
				
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
					<label class="col-md-2 control-label">手机</label>
					<div class="col-md-4">
						<input maxlength="11" type='text' id="mobile" name="mobile"
							value="${User.mobile }" class='form-control' />
					</div>
				<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				<label class="col-md-0 control-label" style="color: red;font-size: 15px" id = "errorMobile" ></label>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">姓名</label>
					<div class="col-md-4">
						<input maxlength="25" type='text' id="name" name="name"
							value="${User.name }" class='form-control' />
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>

				<div class="form-group">
					<label class="col-md-2 control-label">性别</label>
					<div class="col-md-4">
						男<input id="sex_0"  type='radio' value="0" name="sex"/>
						女<input id="sex_1"  type='radio' value="1" name="sex"/>
					</div>
				</div>
				
				
				<div class="form-group">
					<div class="col-md-offset-2 col-md-10">
						<input type="button" id='userAddbtn' class='btn btn-primary'
							value="${empty User ? '添加' : '修改' }" data-loading='稍候...' />
					</div>
				</div>
			</form>
		</div>
	</div>

</body>
</html>