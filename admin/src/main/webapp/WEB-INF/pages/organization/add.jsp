<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty organization ? '添加' : '修改' }组织架构管理</title> 
	<link rel="stylesheet" href="${staticPath }/css/autocomplete/jquery.bigautocomplete.css" type="text/css" />
	<script type="text/javascript" src="${staticPath }/js/autocomplete/jquery.bigautocomplete.js"></script>
	<script type="text/javascript" src="${staticPath }/js/autocomplete/syntaxHighlighter/shCore.js"></script>
	
<script type="text/javascript">
	$(function(){
		$("#parentName").bigAutocomplete({url:"${contextPath }/organization/ajax/searchHigherLevelList"}); 
		
		$("#organizationAddBtn").click(function(){
			
			var parentName = $("#parentName").val(); // 
			parentName = $.trim(parentName);
				var id = $("#id").val();
			
			if( null != parentName && "" != parentName ){
				var isCheck = false;
				lm.postSync("${contextPath }/organization/checkParentName",{id:id,parentName:parentName},function(data){
					if( null == data || "" == data ){
						alert("上级名称不存在或与此组织同名")
						isCheck = true;
					} else {
						if( data.level == 5 ){
							alert("此级别不能作为父级");
							isCheck = true;
						} else {
							$("#parentId").val(data.id);							
						}
					} 
				});
				if(isCheck){
					return;
				}
			} else {
				var hasPermission = false;
				var id = $("#id").val();
				lm.postSync("${contextPath }/organization/checkHasPermission",{id:id},function(data){
					if( null != data && "" != data ){
						alert("此权限受限,其总部只能拥有一个")
						hasPermission = true;
					}
				});
				if(hasPermission){
					return;
				}
			}
			
			var name = $("#name").val(); // 
			name = $.trim(name);
			
			if( name == "" || name == null ){
				alert("组织名称不能为空!");
				return;
			}
			
			var isRepeat = false;
			var parentId = $("#parentId").val();
			parentId = $.trim(parentId);
			lm.postSync("${contextPath }/organization/ajax/checkNameRepetition", {parentId:parentId,id:id,name:name}, function(data) {
				if (data == 1) {
					alert("组织名称存在重复,请重新输入!");
					$("#name").focus();
					isRepeat = true;
				} 
			});
			
			if (isRepeat) {
				return;
			}
			
			var discount = $("#discount").val();
			discount = $.trim(discount);
			if( null != discount && "" != discount ){
				if ( !(lm.isFloat(discount) && (discount > 0 && discount <= 1)) ){
					alert("请输入正确的进货折扣");
					return ;					
				}				
			}
			
			var agentId = $("#agentId").val();
			agentId = $.trim(agentId);
			if( agentId == "" || agentId == null ){
				alert("代理商名称不能为空!");
				return;
			}
			
			var storeAcountName = $("#storeAcountName").val();
			storeAcountName = $.trim(storeAcountName);
			if( storeAcountName == null || "" == storeAcountName){
				alert("登录账号不能为空");
				return ;
			}
			var storeId = $("#storeId").val();
			var flag = true;
			if( null != storeId && "" != storeId ){
				var storeAcountCache = $("#storeAcountCache").val();
				storeAcountCache = $.trim(storeAcountCache);
				if( storeAcountName == storeAcountCache ){
					flag = false;
				}
			}
			if(flag){
				lm.postSync("${contextPath }/shop/checkmobile/", {mobile:storeAcountName}, function(data) {
					if (data == 1) {
						alert("登录账号存在重复！");
						flag = false;
					} 
				});
				
				if(!flag){
					 return false;
				 } 
			}
			
			var storeAcountPassWord = $("#storeAcountPassWord").val();
			storeAcountPassWord = $.trim(storeAcountPassWord);
			if( storeAcountPassWord == null || "" == storeAcountPassWord ){
				if(null == storeId || "" == storeId){
					alert("登录密码不能为空");
					return ;				
				}
			} else {
				if( storeAcountPassWord.length < 6 || storeAcountPassWord.length > 19 ){
					alert("登录密码长度不能小于6位且不能大于18位");
					return ; // 长度限制
				}
				if( !(/([\w]){6,19}$/.test(storeAcountPassWord)) ){
					alert("登录密码只能为英文或者数字！");
					return ; // 只能输入英文或者数字
				}
			}
			
			var mobile = $("#mobile").val();
			mobile = $.trim(mobile);
			if( mobile == null || "" == mobile ){
				alert("手机号码不能为空");
				return ;
			}
			
			if(!lm.isMobile(mobile)){
				alert("手机号码输入错误");
				return ;
			}
			
			$("#organizationAddForm").submit();
		});
	});
	
	$(document).ready(function(){
		var storeId = $("#storeId").val();
		if( null != storeId && "" != storeId ){ // 修改
			$("#isShareUser").val('${store.isShareUser }');
			var storeAcountPassWord = $("#storeAcountPassWord");
			storeAcountPassWord.val("");
			storeAcountPassWord.attr("placeholder","不修改则为原密码");
		}
	});
</script>	
</head>
<body>

<!-- 内层DIV   start -->
    <div class='panel'>
	    <div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>${empty organization ? '添加' : '修改' }组织架构管理
			</strong>
		</div>
		
	    <div class='panel-body'>
	      	<form id="organizationAddForm" method='post' class='form-horizontal' repeatSubmit='1' autocomplete="off" action="${contextPath }/organization/save">
	      		  <input id="parentId" name="parentId" type="hidden" value="" />
	      		  <input id="id" name="id" type="hidden" value="${organization.id }" />
	      		  <input maxlength="50" type="hidden" name="organizationName" id="organizationName" value="${organizationName }" class="form-control">
	      		  <input type="hidden" name="storeId" id="storeId" value="${store.id }" />
				  <input type="hidden" name="storeAcountCache" class='form-control' id="storeAcountCache"  value="${store.storeAcountName }" >
				
		          <div class="form-group">
			            <label class="col-md-1 control-label">上级名称</label>
			            <div class="col-md-2">
			               <input maxlength="50" type="text" name="parentName" id="parentName" placeholder="不选择时则添加的组织为最高级别" value="${organization.parentName }" class="form-control">
			            </div>
			            	
	
			            <label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
		          </div>
		          
		          <div class="form-group">
			            <label class="col-md-1 control-label">组织名称</label>
			            <div class="col-md-2">
			               <input maxlength="50" type="text" name="name" id="name" value="${organization.name }" class="form-control">
			            </div>
			            <label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
		          </div>
		          
		          <div class="form-group">
			            <label class="col-md-1 control-label">进货折扣</label>
			            <div class="col-md-2">
			               <input maxlength="5" type="text" name="discount" id="discount" value="${organization.discount }" class="form-control">
			            </div>
			            <label class="col-md-0 control-label" style="color: red;font-size: 15px" >(选填，只有被铺货组织有效)</label>
		          </div>

					<div class="form-group">
						<label class="col-md-1 control-label">代理商名称</label>
						<div class="col-md-2">
							<input name="agentName" id="agentName"  readonly="readonly" value="${store.agentName }" class="form-control" isRequired="1" tipName="代理商名称" ignoreNull data-remote="${contextPath }/shop/agentList" data-toggle="modal" />
							<input type="hidden" name="agentId" id="agentId" value="${store.agentId }" />
						</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
					</div>
					
					<div class="form-group">
						<label class="col-md-1 control-label">登录账号</label>
						<div class="col-md-2">
							<input type="text" name="storeAcountName" class='form-control' id="storeAcountName" value="${store.storeAcountName }" maxlength="20"  >
							<input type="hidden" name="storeAcountNameCache" class='form-control' id="storeAcountNameCache" value="${store.storeAcountNameCache }" maxlength="20"  >
						</div>
							<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
					</div>
					
					<div class="form-group">
						<label class="col-md-1 control-label">登录密码</label>
						<div class="col-md-2">
							<input type="password" style="display:none"> <!-- 为了屏蔽掉浏览器记住密码后自动填充到下面的密码框中,加入这个隐藏域 -->
							<input type="password" style="display:none">
							<input type="password" name="storeAcountPassWord" class='form-control' id="storeAcountPassWord" value="${store.storeAcountPassWord }" maxlength="20"  >
						</div>
							<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
					</div>
							
					<div class="form-group">
						<label class="col-md-1 control-label">手机号码</label>
						<div class="col-md-2">
							<input type="text" name="mobile" class='form-control' id="mobile" value="${store.mobile }" maxlength="20"  >
						</div>
							<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
					</div>
					
					<div class="form-group" style="display: none">
						<label class="col-md-1 control-label">是否用户共享</label>
						<div class="col-md-2">
							<select id="isShareUser" name="isShareUser" class='form-control'> 
								<option id="0" value="0" >不共享</option>
								<option id="1" value="1" selected="selected">共享</option>
							</select>
						</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px">*</label>&nbsp;&nbsp;
					</div>
					
					 <div class="form-group">
						<div class="col-md-offset-1 col-md-10">
							<input type="button"  id='organizationAddBtn' class='btn btn-primary' value="${empty organization ? '添加' : '修改' }" />
						</div>
				  	</div>
	        </form>
	    </div>
  </div>
<!-- 内层DIV   end -->

</body>
</html>