<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty agent ? '添加' : '修改' }代理商</title> 
<script type="text/javascript">
$(document).ready(function(){
	var type="${agent.type}";
	if(type!=""){
		$("#type option[value=" + type + "]").attr("selected",true);
	}
	
	if($("#type").find("option:selected").val()==1){
		$("[name=parendIdButton]").attr("disabled",true);
	}
	/* var mobile="${agent.mobile}";
	if(mobile!=null && mobile!=""){
		$("[name=mobile]").attr("disabled",true);
	} */
	$("#cancelParentBtn").click(function(){
		$("#parentId").val('');
		$("#parentName").val('');
	});
});

function getType(){
	if($("#type").find("option:selected").val()==1){
		$("[name=parendIdButton]").attr("disabled",true);
		$("#span").hide();
		$("#parentId").val('');
		$("#parentName").val('');
	}else{
		$("[name=parendIdButton]").attr("disabled",false);
		$("#span").show();
	}
}
//保存代理商信息
function saveAgent(){
	var id=$("#id").val();
	 var mobileCache = $("#mobileCache").val();
	var name=$.trim($("#name").val());
	if($.trim($("#name").val())==""){
		lm.alert("代理商名称不能为空！");
		$("#name").focus();
		return; 
	}
	var type=$("#type").find("option:selected").val();
	var parentId=$("#parentId").val();
	if(type!=1 ){
		if(parentId=="" || parentId==null){
			lm.alert("上级代理商不能为空！");
			return; 
		}
	}
	var contactName=$.trim($("#contactName").val());
	var mobile=$("#mobile").val();
	if(mobile==""){
		lm.alert("手机号码不能为空！"); 
		$("#mobile").focus();
		return; 
	}
	if(!lm.isMobile(mobile)){ 
		lm.alert("请输入正确的手机号码"); 
		$("#mobile").focus();
		return; 
	 }
	
	var areaId=$("[name=areaId]").val();
	if($("[name=areaId]").val()=="" || $("[name=areaId]").val()==null || typeof($("[name=areaId]").val())=="undefined"){
		lm.alert("省市区不能为空！"); 
		$("[name=areaId]").focus();
		return; 
	}
	var address=$.trim($("#address").val());
	if($.trim($("#address").val())==""){
		lm.alert("地址不能为空！"); 
		$("#address").focus();
		return; 
	}
	if($("#parentName").val()!=null && $("#parentName").val()!=""){
		if(name==$("#parentName").val()){
			lm.alert("填写的代理商名称和上级代理商一样！");
			$("#name").focus();
			return; 
		}
	}
	var flag=true;
	
	 if(id==null || id==""){
		 lm.postSync("${contextPath}/agent/checkmobile/",{mobile:mobile},function(data){
				if(data=="1"){
					lm.alert("手机号码已注册！");
					flag = false;
				}
		 });
	 }else{
		 
		
		 if( mobileCache == mobile ){
			 flag = true;
		 }else{
			 lm.postSync("${contextPath}/agent/checkmobile/",{mobile:mobile},function(data){
					if(data=="1"){
						lm.alert("手机号码已注册！");
						flag = false;
					}
			 });
		 }
	 }
	 
	if(flag){
		if( mobileCache == mobile ){
			lm.confirm("温馨提示：修改手机号码会将账号也同步进行修改,是否要修改？",function(){
				$("#addAgentBtn").prop("disabled",true);
				lm.post("${contextPath}/agent/list/ajax/editAgent",{id:id,name:name,type:type,parentId:parentId,contactName:contactName,mobile:mobile,areaId:areaId,address:address},function(data){
					if(data=="0"){
						lm.alert("已存在该代理商！");
						return;
					}if(data=="1"){
						lm.alert("保存成功！");
						window.location.href="${contextPath}/agent/list";
					}
				});
			});
		}else{
			$("#addAgentBtn").prop("disabled",true);
			lm.post("${contextPath}/agent/list/ajax/editAgent",{id:id,name:name,type:type,parentId:parentId,contactName:contactName,mobile:mobile,areaId:areaId,address:address},function(data){
				if(data=="0"){
					lm.alert("已存在该代理商！");
					return;
				}if(data=="1"){
					lm.alert("保存成功！");
					window.location.href="${contextPath}/agent/list";
				}
			});
		}
		
		
	}
}
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>${empty agent ? '添加' : '修改' }代理商
			</strong>
		</div>
		<div class='panel-body'>
			<div  class='form-horizontal' id="agent">
			
				<!-- 修改时传过来的代理商ID -->
				<input id="id" name="id" type="hidden" value="${agent.id }" />
				<input id="mobileCache" name="mobileCache" type="hidden" value="${agent.mobile }" />
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>代理商名称</label>
					<div class="col-md-2">
						<input type="text" id="name" style="width: 250px;" name="name" value="${agent.name }" class='form-control' maxlength="120"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>代理商类型</label>
					<div class="col-md-2">
		            	<select name="type" class="form-control" style="width: 250px;" id="type" onChange="getType()">
							<option  value ="1" >总代理商</option>
							<option  value ="2" >运营商</option>
							<option  value ="3" >子公司</option>
		            	</select>
	            	</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span id="span" style="color: red;font-size: 15px;display: none;">*</span>上级代理商</label>
					<div class="col-md-6">
						<div class="input-group">
						<c:if test="${empty agent }">
							<button type="button" name='parendIdButton'  data-remote="${contextPath }/agent/agentList" data-toggle="modal" class="btn btn-primary">请选择上级代理商</button>
						</c:if>
						<c:if test="${not empty agent }">
							<button type="button" name='parendIdButton'  data-remote="${contextPath }/agent/agentList/${agent.id }" data-toggle="modal" class="btn btn-primary">请选择上级代理商</button>
						</c:if>
						<button type='button' class="btn btn-warning" id="cancelParentBtn">取消上级代理商</button>
						</div>
						<input type="hidden" id="parentId" name="parentId" value="${agent.parentId }" />
						
						<input id="parentName" class='form-control' readonly="readonly" style="margin-top: 5px;width: 250px;" value="${agent.parentName }"/>
					</div>
					
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">联系方式</label>
					<div class="col-md-2">
						<input type='text' id="contactName" name="contactName" style="width: 250px;" class='form-control' value="${agent.contactName }" maxlength="20"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>手机号码</label>
					<div class="col-md-2">
						<input type='text' id="mobile" name="mobile" style="width: 250px;" class='form-control' value="${agent.mobile }"  maxlength="11"/>
						<input id="mobileCache" name="mobileCache" type="hidden" value="${agent.mobile }" />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>省/市/区</label>
					<div class="col-md-3">
						<m:selectArea inputName="areaId" path="${agent.areaPath }"></m:selectArea>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>联系地址</label>
					<div class="col-md-2">
						<input type="text" id="address" style="width: 250px;" name="address" class='form-control' value="${agent.address }" maxlength="50"/>
					</div>
				</div>
				
				<div class="form-group">
					<div class="col-md-offset-1 col-md-1 0">
						<input type='button' id='addAgentBtn' class='btn btn-primary' value="${empty agent ? '添加' : '修改' }" onclick="saveAgent();"/>
					</div>
				</div>
		 </div>
	  </div>
   </div>
</body>
</html>