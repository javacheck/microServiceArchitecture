<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty productStock ? '添加' : '修改' }商品库存</title> 
<script type="text/javascript">
var attrLength="";
$(document).ready(function(){
	getProductAttributeValueList();
	
});

//根据商品ID所选获取商品属性
function getProductAttributeValueList() {
    var productId=$("#productId").val();//商品ID
    var attributeCode=$("#attributeCode").val();
    var arr;
    if(attributeCode!=""){
    	arr=attributeCode.split("-");
    }
    //根据商品ID所选获取商品属性
    lm.post("${contextPath}/productStock/list/ajax/productAttributeList",{productId:productId},function(data){
    	attrLength=data.length;
    	for(var i=0;i<data.length;i++){//属性列表
    		$("#productStock").append("<div class='form-group'>"+
											"<label class='col-md-2 control-label'>"+data[i].name+"<span style='color:red'>*</span></label>"+
											"<div class='col-md-1'>"+
													"<select name='productAttributeValue"+i+"' class='form-control'  style='width: auto;' id='productAttributeValue"+i+"' onchange='getproductId()' >"+
															"<option value=''>选择商品属性值</option>"+
													"</select>"+
											"</div>"+
									"</div>");
    		lm.postSync("${contextPath}/productAttributeValue/list/ajax/list-by-productAttributeId",{productAttributeId:data[i].id},function(data1){
    			for(var j=0;j<data1.length;j++){
    				$("#productAttributeValue"+i).append('<option id="' + data1[j].id + '" value="' + data1[j].id + '">' + data1[j].value + '</option>');
    				if(attributeCode!=""){
    					for(var k=0;k<arr.length;k++){
	    					$("#productAttributeValue"+i).find("option[value='"+arr[k]+"']").attr("selected",true);
	    		    		$("#productAttributeValue"+i).attr("disabled","disabled");
    					}
    				}
    			}
    		}); 
    		
		}
    	
		$("#productStock").append("<div class='form-group'>"+
	    		"<div class='col-md-offset-2 col-md-1 0'>"+
	    		"<input type='button'  id='productStockAddBtn' class='btn btn-primary' value='${empty productStock ? '添加' : '修改' }' onclick='saveproductStock()' />"+
	    		"</div></div>");
	});
   
}


//获取商品属性id
 function getproductAttributeId(){
	var productAttributeId=$("#productAttributeId").val();
	if(productAttributeId!=null){
   	 $("#attributeId").val(productAttributeId);
    }
} 
function saveproductStock(){
	var id=$("#id").val();//商品库存ID
	var storeId=$("#storeId").val();//店铺ID
	var productId=$("#productId").val();//商品ID
	var price=$("#price").val();//价格
	var stock=$("#stock").val();//库存
	var alarmValue=$("#alarmValue").val();//警报值
	var barCode=$("#barCode").val();//条形码
	var attributeCode="";//属性值合起来111－222－333
	if(price=="" ||price==null){
		lm.alert("价格不能为空!");
		return;
	}
	if(stock==""||stock==null){
		lm.alert("库存不能为空！");
		return;
	}
	if(alarmValue==""||alarmValue==null){
		lm.alert("警报值不能为空！");
		return;
	}
	if(barCode==""||barCode==null){
		lm.alert("条形码不能为空！");
		return;
	}
	for(var i=0;i<attrLength;i++){
		if($("#productAttributeValue"+i).children("option:selected").val()==""){
			lm.alert("属性值不能为空！");
			$("#productAttributeValue"+i).focus();
			return;
		}
	}
	for(var i=0;i<attrLength;i++){
		attributeCode+=$("#productAttributeValue"+i).children("option:selected").val()+'-';
	}
	if(attributeCode!=""){
		attributeCode=attributeCode.substring(0,attributeCode.length-1);
	}
	if(id==""){
		lm.post("${contextPath}/productStock/list/ajax/exist",{attributeCode:attributeCode},function(data){
			if(data==1){
				noty("已存在该商品属性值库存！");
			}else{
				lm.post("${contextPath}/productStock/list/ajax/save",{id:id,storeId:storeId,productId:productId,price:price,stock:stock,alarmValue:alarmValue,attributeCode:attributeCode,barCode:barCode},function(data){
					if(data=='1'){
				    	lm.alert("操作成功！");
				    	 window.location.href="${contextPath }/productStock/productStock/"+productId;
					}
				});
			}
		});
	}else{
		lm.post("${contextPath}/productStock/list/ajax/save",{id:id,storeId:storeId,productId:productId,price:price,stock:stock,alarmValue:alarmValue,attributeCode:attributeCode,barCode:barCode},function(data){
			if(data=='1'){
		    	lm.alert("操作成功！");
		    	 window.location.href="${contextPath }/productStock/productStock/"+productId;
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
			<i class='icon-plust'></i>${empty productStock ? '添加' : '修改' }商品库存
			</strong>
		</div>
		<div class='panel-body'>
			<div  class='form-horizontal' id="productStock">
				<input id="productId" name="productId" type="hidden" value="${product.id }" /><!-- 商品ID -->
				<input id="storeId" name="storeId" type="hidden" value="${store.id }" /><!-- 商品ID -->
				<input id="id" name="id" type="hidden" value="${productStock.id }" />
				<input id="attributeCode" name="attributeCode" type="hidden" value="${productStock.attributeCode }" />
				<div class="form-group">
					<label class="col-md-2 control-label">商品名称</label>
					<div class="col-md-2">
						<input type='text' id="productName" name="productName" value="${product.name }" class='form-control'   disabled="disabled"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">店铺</label>
					<div class="col-md-2">
						<input type='text' id="store" name="store" value="${store.name }" class='form-control'    disabled="disabled"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">价格<span style="color:red">*</span></label>
					<div class="col-md-2">
						<input type='text' id="price" name="price"  class='form-control'  value="${productStock.price }" maxlength="10"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">库存<span style="color:red">*</span></label>
					<div class="col-md-2">
						<input type='text' id="stock" name="stock" value="${productStock.stock }" class='form-control'   maxlength="10"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">警报值<span style="color:red">*</span></label>
					<div class="col-md-2">
						<input type='text' id="alarmValue" name="alarmValue" value="${productStock.alarmValue }" class='form-control'   maxlength="10"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">条形码<span style="color:red">*</span></label>
					<div class="col-md-2">
						<input type='text' id="barCode" name="barCode" value="${productStock.barCode }" class='form-control'   maxlength="10"/>
					</div>
				</div>
		</div>
		</div>
	</div>

</body>
</html>