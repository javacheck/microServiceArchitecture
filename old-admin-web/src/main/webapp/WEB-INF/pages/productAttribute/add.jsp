<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty productAttribute ? '添加' : '修改' }商品属性</title> 
<script type="text/javascript">

$(function(){
	 //var productId=$("#productId").val();
	/*  $("#productId1").find("option").each(function(){
			if($(this).val()==productId){
				productName=$(this).text();
				
			}
		}); */
	/*  if(productId !=null && productId !="" && productId !=undefined){
		 var productName=$("#productId1").children("option[value='"+productId+"']").text();
		 $("#productId1").children("option[value='"+productId+"']").remove();	
		 $("#productId2").append('<option value="'+productId+'">'+productName+'</option>');
	 } */
	 
	 $("#productId1").dblclick(function(){//双击左边数据移到右边
	  var productId = this.value;
	  var productName=$("#productId1").find("option:selected").text();
	 
	  if(productId == '') return;
	  var name=$("#name").val();//商品属性名称
		lm.post("${contextPath}/productAttribute/list/ajax/exist",{name:name,productId:productId},function(data){
			if(data==1){
				noty(productName+"已存在该属性！");
			}else{
				$("#productId1").children("option[value='"+productId+"']").remove();
			  $("#productId2").append('<option value="'+productId+'">'+productName+'</option>');
			}
		});
	  
	});
	 $("#productId2").dblclick(function(){
		  var productId = this.value;
		  var productName=$("#productId2").find("option:selected").text();
		 
		  if(productId == '') return;
		  $("#productId2").children("option[value='"+productId+"']").remove();
		  $("#productId1").append('<option value="'+productId+'">'+productName+'</option>');
		  
	});
	
	 //checkProduct();
}) 
function checkProduct(){
	
	$("#name").focus(function(){//用户名焦点验证
		if($.trim(document.getElementById("name").value) == "" || $.trim(document.getElementById("name").value)==null){
			$("#emptype").show();
			$("#error").hide();
			return false;
		}else{
			$("#emptype").hide();
			$("#error").hide();
		}
	});
	$("#name").blur(function(){
		if($.trim(document.getElementById("name").value) == "" || $.trim(document.getElementById("name").value)==null){
			$("#error").show();
			$("#emptype").hide();
			return false;
		}else{
			$("#emptype").hide();
			$("#error").hide();
		}
	});
}
function saveProductAttribute(){
	var id=$("#id").val();//商品属性ID
	var name=$("#name").val();//商品属性名称
	var productId=$("#productId").val();//商品ID
	if(name==""){
		lm.alert("商品属性名称不能为空！");
		return;
	}
	var productIds='';
	$("#productId2").find("option").each(function(){
		productIds+=$(this).val()+',';
	});
	
	productIds=productIds.substring(0,productIds.length-1);//商品数组
	if(id==""){
		if(productIds==""){
			lm.alert("选择所属商品不能为空！");
			return;
		}
	}
	if(id!=""){
		lm.post("${contextPath}/productAttribute/list/ajax/exist",{name:name,productId:productId},function(data){
			if(data==1){
				noty("该商品已存在相同属性！");
			}else{
				lm.post("${contextPath}/productAttribute/list/ajax/save",{id:id,name:name},function(data){
					if(data=='1'){
				    	lm.alert("操作成功！");
				    	 window.location.href="${contextPath}/productAttribute/list";
					}
				});
			}
		});
	}else{
		var flag=true;
		$("#productId2").find("option").each(function(){//循环
			productId=$(this).val();
			lm.post("${contextPath}/productAttribute/list/ajax/exist",{name:name,productId:productId},function(data){
				if(data==1){
					noty("该商品已存在相同属性！");
					flag=false;
				}
			});
		});
		if(flag==false){
			return;
		}else{
			lm.post("${contextPath}/productAttribute/list/ajax/save",{id:id,name:name,productIds:productIds},function(data){
				if(data=='1'){
			    	lm.alert("操作成功！");
			    	 window.location.href="${contextPath}/productAttribute/list";
				}
			}); 
		}
	}
}
function addProduct(){
	var arr=[];
	 var product=document.getElementById("productId1");
	 for(var i=0;i<product.length;i++){
		 if(product.options[i].selected){ 
			 arr.push(product.options[i].value);
			 $("#productId2").append('<option value="'+product.options[i].value+'">'+product.options[i].text+'</option>');
		 }
	 }
	 for(var j=0;j<arr.length;j++){
		 $("#productId1").find("option[value='"+arr[j]+"']").remove();
	 }
	   /* $("#productId1").find("option").each(function(){
		if($(this).find("option:selected")=="checked"){
			alert($(this).val());
		} 
	  });   */
		
}
function delProduct(){
	var arr=[];
	var product=document.getElementById("productId2");
	 for(var i=0;i<product.length;i++){
		 if(product.options[i].selected){ 
			 arr.push(product.options[i].value);
			 $("#productId1").append('<option value="'+product.options[i].value+'">'+product.options[i].text+'</option>');
		 }
	 }
	 for(var j=0;j<arr.length;j++){
		 $("#productId2").find("option[value='"+arr[j]+"']").remove();
	 }
}
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
			<i class='icon-plust'></i>${empty productAttribute ? '添加' : '修改' }商品属性
			</strong>
		</div>
		<div class='panel-body'>
			<%-- <form method='post'  action="${contextPath }/productAttribute/add"> --%>
			<div  class='form-horizontal'>
				<input id="id" name="id" type="hidden" value="${productAttribute.id }" />
				<input id="productId" name="productId" type="hidden" value="${productAttribute.productId }" />
				<div class="form-group">
					<label class="col-md-1 control-label">商品属性名称<span style="color:red">*</span></label>
					<div class="col-md-2">
						<input type='text' id="name" name="name" value="${productAttribute.name}" class='form-control' onBlur="checkProduct();"  maxlength="10"/>
					</div>
					<label class="col-md-0 control-label" style="color: red;display:none;" id="error">商品属性名不能为空</label>
					<label class="col-md-0 control-label" style="color: blue;display:none;" id="emptype">请输入商品属性名</label>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label">所属商品<span style="color:red">*</span></label>
					<div class="col-md-6">
						<c:if test="${empty productAttribute}">
						<div class="col-md-2">
                        <select name="productId1" class='form-control' style="width: 150px;height:200px;margin-left: -10px;"  id="productId1" onChange="getProuctId()" multiple="multiple" >
                        		<c:forEach items="${list }" var="product" >
									<option  value ="${product.id}">${product.name}</option>
								</c:forEach>
                        </select>
                        </div>
                        <div class="col-md-2" style="margin-left: 50px;margin-top: 50px;">
	                        <input type="button"  id='productIdAddBtn' class='btn btn-primary' value="增加" onclick="addProduct()" /><br/>	<br/>
	                        <input type="button"  id='productIdDelBtn' class='btn btn-small btn-danger' value="删除" onclick="delProduct()" /><br/>
	                    </div>
	                    <div class="col-md-2">
                        <select name="productId2" class='form-control' style="width: 150px;height:200px;margin-left: -30px;"  id="productId2"  multiple="multiple" >
                        		
                        </select>	
                        </div>
                        </c:if>
                        <c:if test="${not empty productAttribute}">
                        	<input type='text' id="productName" name="productName" value="${productAttribute.productName}" class='form-control' disabled="disabled"/>
                        </c:if>
					</div>
				</div>
				<div class="form-group">
					<div class="col-md-offset-1 col-md-1 0" style="float:left">
						<input type="button"  id='productAddBtn' class='btn btn-primary' value="${empty productAttribute ? '添加' : '修改' }" onclick="saveProductAttribute()" />
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
</html>