<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty productAttributeValue ? '添加' : '修改' }商品属性值</title> 
<script type="text/javascript">
$(document).ready(function(){
	getTopattributeList();
	var attributeId=$("#attributeId").val();//页面从list跳到add页面拿到productAttributeValue对象，从而拿到productAttributeValue.attributeId(商品属性)
	
	if(attributeId!=""){//商品属性不为空时，即为更改操作
    	lm.post("${contextPath}/productAttributeValue/list/ajax/list-by-id",{id:attributeId},function(data){//通过商品属性值表里的商品属性ID查找商品属性表里的productid
        	lm.post("${contextPath}/productAttributeValue/list/ajax/list-by-productId",{productId:data.productId},function(data){//通过商品属性表里的productid，把商品属性列表查出来
            	for(var i=0;i<data.length;i++){
            		 $("#childid").append('<option id="' + data[i].id + '" value="' + data[i].id + '">' + data[i].name + '</option>');
        		}
            	$("#childid").find("option[value='"+attributeId+"']").attr("selected",true);//productAttributeValue表里的attributeId和(通过productid查询)商品属性表里的id相同，选中
            	$("#childid").attr("disabled","disabled");
        	});
            $("#productid").find("option[value='"+data.productId+"']").attr("selected",true);//通过商品属性ID查找商品属性表里的productid选中商品ID
            $("#productid").attr("disabled","disabled");
    	 });
	} 
	//checkproductAttributeValue();
});
function checkproductAttributeValue(){
	$("#value").focus(function(){//名称焦点验证
		if($.trim(document.getElementById("value").value) == "" || $.trim(document.getElementById("value").value)==null){
			$("#emptype").show();
			$("#error").hide();
			return false;
		}else{
			$("#emptype").hide();
			$("#error").hide();
		}
	});
	$("#value").blur(function(){
		if($.trim(document.getElementById("value").value) == "" || $.trim(document.getElementById("value").value)==null){
			$("#error").show();
			$("#emptype").hide();
			return false;
		}else{
			$("#emptype").hide();
			$("#error").hide();
		}
	});
}
//获取商品
function getTopattributeList() {
	lm.post("${contextPath}/productAttributeValue/list/ajax/list-by-accountId",function(data){
    	for(var i=0;i<data.length;i++){
    		 $("#productid").append('<option id="' + data[i].id + '" value="' + data[i].id + '">' + data[i].name + '</option>');
		}
	});
}
//根据商品所选获取商品属性
function getChildrenList() {
    var parentId=$("#productid").val();
    if(parentId == ""){
       $("#childid").empty();
       $("#childid").append("<option value='' >请选择商品属性</option>");
       return ;
    }
    $("#childid").empty();
    lm.post("${contextPath}/productAttributeValue/list/ajax/list-by-productId",{productId:parentId},function(data){
    	$("#childid").append("<option value='' >请选择商品属性</option>");
    	for(var i=0;i<data.length;i++){
    		 $("#childid").append('<option id="' + data[i].id + '" value="' + data[i].id + '">' + data[i].name + '</option>');
		}
	});
}

//获取商品属性id
function getChildrenList1(){
	var childid=$("#childid").val();
	if(childid!=null){
   	 $("#attributeId").val(childid);
    }
}
function saveproductAttributeValue(){
	var id=$("#id").val();//商品属性值ID
	var value=$("#value").val();//商品属性值
	var attributeId=$("#attributeId").val();//商品属性ID
	if(value=="" ||value==null){
		lm.alert("商品属性值不能为空!");
		return;
	}
	if($("#attributeId").val()==""||$("#attributeId").val()==null){
		lm.alert("商品属性不能为空！");
		return;
	}
	lm.post("${contextPath}/productAttributeValue/list/ajax/exist",{value:value,attributeId:attributeId},function(data){
		if(data==1){
			noty(name+"已存在该商品属性值！");
		}else{
			var url="${contextPath}/productAttributeValue/list/ajax/save";
			lm.post(url,{id:id,value:value,attributeId:attributeId},function(data){
				if(data=='1'){
			    	lm.alert("操作成功！");
			    	 window.location.href="${contextPath}/productAttributeValue/list";
				}
			});
		}
	});
	
}
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
			<i class='icon-plust'></i>${empty productAttributeValue ? '添加' : '修改' }商品属性值
			</strong>
		</div>
		<div class='panel-body'>
			<div  class='form-horizontal'>
				<input id="id" name="id" type="hidden" value="${productAttributeValue.id }" />
				<%-- <input id="productId" name="productId" type="text" value="${productAttributeValue.productId }" /><!--商品ID  -->
				<input id="productAttributeId" name="productAttributeId" type="text" value="${productAttributeValue.productAttributeId }" /><!--商品属性ID  --> --%>
				<input id="attributeId" name="attributeId" type="hidden" value="${productAttributeValue.attributeId }" />
				<div class="form-group">
					<label class="col-md-2 control-label">商品属性值<span style="color:red">*</span></label>
					<div class="col-md-2">
						<input type='text' id="value" name="value" value="${productAttributeValue.value}" class='form-control' onBlur="checkproductAttributeValue();"  maxlength="10"/>
					</div>
					<label class="col-md-0 control-label" style="color: red;display:none;" id="error">商品属性值不能为空</label>
					<label class="col-md-0 control-label" style="color: blue;display:none;" id="emptype">请输入商品属性值</label>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">商品属性<span style="color:red">*</span></label>
						<div class="col-md-1">
	                        	 <select name="productid" class="form-control"  style="width: auto;"  id="productid" onChange="getChildrenList()">
	                        	 	<option value="">选择商品</option>
	                        	</select>
	                    </div>
	                    <div class="col-md-1">
								<select name="childid" class="form-control"  style="width: auto;" id="childid" onchange="getChildrenList1();" >
									<option value="">
										选择商品属性
									</option>
								</select>
						 </div>
				</div>
				<div class="form-group">
					<div class="col-md-offset-2 col-md-1 0">
						<input type="button"  id='productAttributeValueAddBtn' class='btn btn-primary' value="${empty productAttributeValue ? '添加' : '修改' }" onclick="saveproductAttributeValue()" />
					</div>
				</div>
		</div>
		</div>
	</div>

</body>
</html>