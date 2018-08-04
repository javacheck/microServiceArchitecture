<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加组合商品</title> 
<script type="text/javascript">

$(function(){ 
	 $("#leftProduct").dblclick(function(){//双击左边数据移到右边
		  var categoryId = this.value;
		  var categoryName=$("#leftProduct").find("option:selected").text();
		  var priceSign = $("#leftProduct").children("option[value='"+categoryId+"']").attr("priceSign");
		  if(categoryId == '') return;
		  $("#leftProduct").children("option[value='"+categoryId+"']").remove();
		  $("#rightProduct").append('<option value="'+categoryId+'" priceSign="' + priceSign + '">'+categoryName+'</option>');
	});
	 
	 
	 $("#rightProduct").dblclick(function(){
		  var categoryId = this.value;
		  var categoryName=$("#rightProduct").find("option:selected").text();
		  var priceSign = $("#leftProduct").children("option[value='"+categoryId+"']").attr("priceSign");
		 
		  if(categoryId == '') return;
		  $("#rightProduct").children("option[value='"+categoryId+"']").remove();
		  $("#leftProduct").append('<option value="'+categoryId+'" priceSign="'+priceSign+'">'+categoryName+'</option>');
		  
	});
}); 

function add(){
	var arr=[];
	 var product=document.getElementById("leftProduct");
	 for(var i=0;i<product.length;i++){
		 if(product.options[i].selected){ 
			 arr.push(product.options[i].value);
			 $("#rightProduct").append('<option value="'+product.options[i].value+'" priceSign="'+ $(product.options[i]).attr('priceSign')+ '">'+product.options[i].text+'</option>');
		 }
	 }
	 for(var j=0;j<arr.length;j++){
		 $("#leftProduct").find("option[value='"+arr[j]+"']").remove();
	 }		
}

function del(){
	var arr=[];
	var product=document.getElementById("rightProduct");
	 for(var i=0;i<product.length;i++){
		 if(product.options[i].selected){ 
			 arr.push(product.options[i].value);
			 $("#leftProduct").append('<option value="'+product.options[i].value+'" priceSign="'+ $(product.options[i]).attr('priceSign')+'">'+product.options[i].text+'</option>');
		 }
	 }
	 for(var j=0;j<arr.length;j++){
		 $("#rightProduct").find("option[value='"+arr[j]+"']").remove();
	 }
}

$(document).ready(function(){
	$("#productCancelBtn").click(function(){ // 取消
		var myTrigger = $(".close");
		myTrigger.click();
	});
	$("#productSureBtn").click(function(){ // 确定
		var categoryIds= new Array();
		var productName = new Array();
		var price = new Array();
		$("#rightProduct").find("option").each(function(){
			categoryIds.push($(this).val());
			productName.push($(this).text());
			price.push($(this).attr("priceSign"));
		});
		
		if( categoryIds.length <= 0 ){
			lm.alert("请选择需要新增的组合商品");
			return;
		}
		$("#product_stock_table").find("tr[id!='product_stock_show']").remove();
		var html = "";
		for(var z=0;z<categoryIds.length;z++){
			html += "<tr> <td style='width:50px'>" +(z+1)+ "</td> <td style='width:150px'>"+ productName[z]+"</td><td>"+price[z]+"</td> </tr>";
		}
		$("#product_stock_table").append(html);
		
		cacheArray.length == 0;
		cacheArray = categoryIds;
		cacheArray.sort(); // 排序
		alert(cacheArray.length);
		$("#promotionProductCache").val(cacheArray);
		$("#promotionCache").val(cacheArray);
		$(".close").click();
	});
});
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-body'>
			<div  class='form-horizontal'>
				<div class="form-group">
					<div class="col-md-6" style="width:600px">
						<div class="col-md-2">
							<label class="col-md-1 control-label" style="width: 100px;height:10px;margin-left: 10px;margin-top: -20px;">现有商品</label>
	                        <select name="leftProduct" class='form-control' style="width: 200px;height:200px;margin-left: -10px; margin-top: 10px;"  id="leftProduct"  multiple="multiple" >
	                        		<c:forEach items="${productStockList }" var="productStockList" >
										<option value ="${productStockList.id}" priceSign="${productStockList.price }">${productStockList.productName} <c:if test="${productStockList.attributeValuesListJointValue != null }"> | ${productStockList.attributeValuesListJointValue }</c:if></option>
									</c:forEach>
	                        </select>
                        </div>
                        <div class="col-md-2" style="margin-left: 130px;margin-top: 50px;">
	                        <input type="button" id='AddBtn' class='btn btn-primary' value="增加" onclick="add()" /><br/>	<br/>
	                        <input type="button" id='DelBtn' class='btn btn-small btn-danger' value="删除" onclick="del()" /><br/>
	                    </div>
	                    <div class="col-md-2">
	                    	<label class="col-md-1 control-label" style="width: 100px;height:10px;margin-left: 10px;margin-top: -20px;">组合商品</label>
	                        <select name="rightProduct" class='form-control' style="width: 200px;height:200px;margin-left: -10px;margin-top: 10px;"  id="rightProduct"  multiple="multiple" >
                        		<c:forEach items="${productStockCacheList }" var="productStockCacheList" >
									<option value ="${productStockCacheList.id}" priceSign="${productStockCacheList.price }">${productStockCacheList.productName} <c:if test="${productStockCacheList.attributeValuesListJointValue != null }"> | ${productStockCacheList.attributeValuesListJointValue }</c:if></option>
								</c:forEach>
	                        </select>	
                        </div>
					</div>
				</div>
				
				<div class="form-group" id="tipDIV">
					<label class="col-md-1 control-label"></label>
					<div class="col-md-7">
					 	<span style="color:red">温馨提示：可按住CTRL键或者SHIFT键进行多选</span> 
					</div>
				</div>
				<div class="form-group">
					<div class="col-md-offset-3 col-md-1 0" style="float:left">
						<input type="button"  id='productCancelBtn' class='btn btn-primary' value="取消"/>
					</div>
					<div class="col-md-offset-3 col-md-1 0" style="float:left">
						<input type="button"  id='productSureBtn' class='btn btn-primary' value="确定"/>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
</html>