<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html >
<html>
<head>
    <meta charset="utf-8" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="white">
    <meta name="apple-touch-fullscreen" content="yes">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache, must-revalidate">
   
    <link rel="stylesheet" type="text/css" href="${staticPath }/css/api/style.css" >
	<script src="${staticPath }/js/jquery-1.11.2.min.js"></script>
    <script src="${staticPath }/js/api/zepto.min.js" ></script>
    <script src="${staticPath }/js/api/main.js" ></script>
    <script type="text/javascript" src="${staticPath }/js/common.js"></script>
<script type="text/javascript" src="${staticPath }/js/jquery.pager.js"></script>
	<script type="text/javascript">
		$(function (){
		    //设置默认选择
			var attributeCode = "${productStock.attributeCode}";
			var attributeCodes = new Array();
			attributeCodes= attributeCode.split('-')
			$("input[type=radio]").each(
				function() {
					var thisVal=$(this).val();
				    for (var i = 0; i < attributeCodes.length; i++) {
						if(thisVal==attributeCodes[i]){
							this.checked=true;
						}
					}
					//alert(thisVal);
				}
			)
			var attributeCodeCountent = "${attributeCodeCountent}";
			var storeId="${productStock.storeId}";
			var productId="${productStock.productId}";
			checkIsCanSelected(attributeCodeCountent);//初始化时候可以选择
			
			//添加点击事件
			$("input[type=radio]").click(function(){
				var papm = "";
	 			$("input[type=radio]").each(function() {
					if(this.checked==true){
						papm=papm+'-'+$(this).val();
					}
				})//循环事件结束
				papm=papm.substring(1);//获取当前选择的checkbox
				lm.post("${contextPath }/api/productAttribute/ajax/isonCheck",{'papm':papm,'storeId':storeId,'productId':productId},function(data){
				//alert(attributeCodeCountent);
				checkIsCanSelected(data.attributeCodeCountent);//重新渲染时候可以选择
				if(data.productStock!=null){
					$("#unitPrice").val(data.productStock.price);
					$("#stockId").val(data.productStock.id);
				}else{
					$("#unitPrice").val("0.0");
					$("#stockId").val("");
				}
				chagePrice();
				
				});//Post请求事件结束
				
			});//点击事件结束
			
			//添加提交点击事件
			$("#submit").click(function(){
				var papm="";
				$("input[type=radio]").each(function() {
					var ThisVal =$(this).val()
					if(this.checked==true){
						papm=papm+'-'+$("#"+ThisVal+"_l").text();
					}
				})//循环事件结束
				papm=papm.substring(1);
				var privce=$("#price_span_id").text();
				var stockId=$("#stockId").val();
				var productName =$(".productName").text();
				alert(papm+"-"+privce+"-"+stockId+"-"+productName);				
			});//添加提交点击事件结束
			
		});//初始化事件结束
		
	 
	 function upcount(){//加一
	   var count = $('.J_ItemAmount').val();
	   count++;
	   $('.J_ItemAmount').val(count);
	   chagePrice();
	 }
	 function downcount(){//减一
	   var count = $('.J_ItemAmount').val();
	   count--;
	   if(count<1){
		 count=1;
		 alert('购买数量不可小于1');
	   }
	   $('.J_ItemAmount').val(count)
	   //chagePrice();
	  }
	  
	 function checkIsCanSelected(attributeCodeCountent){//更改为可选中方法
	 	var attributeCodes = new Array();
		attributeCodes= attributeCodeCountent.split('-')
	 	$("input[type=radio]").each(
				function() {
					var thisVal=$(this).val();
					changeCanNotSelected(thisVal);//默认不可选择
				    for (var i = 0; i < attributeCodes.length; i++) {
						if(thisVal==attributeCodes[i]){
							changeCanSelected(thisVal);
						}
					}
				}
			)
	 }
	 
	 function changeCanSelected(obj){//修改为可选状态
	 	$("#"+obj).attr('disabled',false);
	 	$("#"+obj+"_l").removeClass("disabled");
	 }
	 function changeCanNotSelected(obj){//修改为不可选状态
	 	$("#"+obj).attr('disabled',true);
	 	$("#"+obj+"_l").addClass("disabled");
	 }
	 function chagePrice(){//更改价格
	 	$("#price_span_id").text($("#count_id").val()*$("#unitPrice").val());
	 }
	 
	function keyPress() {//锁定键盘只能输入数字
		var keyCode = event.keyCode;
		if ((keyCode >= 48 && keyCode <= 57)) {
			event.returnValue = true;
		} else {
			event.returnValue = false;
		}
	}
	//添加修改事件
	
	function changCount() {
		var thisVal = $("#count_id").val()
		if (!isNaN(thisVal)) {
			$("#changeUp").val(thisVal)
		} else {
			$("#count_id").val($("#changeUp").val());
			return;
		}
		chagePrice()
	}
	</script>
</head>
<body>
	<section class="main">
		<section class="productImg"><img src="${staticPath }/images/api/productImg.jpg" alt="productImg" width="260" height="260"></section>
		<section class="productTitle">
			<article class="productName">${productStock.productName}</article>
			<article class="pricePanel"><span class="priceSymbol">￥ <span id = "price_span_id" class="totalPrice">${productStock.price}</span></span></article>
		</section>
		<section class="productNum">
			<a href="javascript:void(0)" class="J_Minus minus" onClick="downcount();" style="background: url('${staticPath }/images/api/minus.png') no-repeat center center;">-</a>
			<select id="tz" name="productcountnow" class="J_ItemAmount">
				<option value="1">1</option>
				<option value="2">2</option>
				<option value="3">3</option>
				<option value="4">4</option>
			</select>
			<a href="javascript:void(0)" class="J_Plus plus" onClick="upcount();" style="background: url('${staticPath }/images/api/plus.png') no-repeat center center;">+</a>
		</section>
		
		<section>
			<form  id="loginform" action=" " method="post" name="form1">
				<input id = "unitPrice" type="hidden" name="" value="${productStock.price}" >
				<input id = "stockId" type="hidden" name="" value="${productStock.id}" >
				<input id = "changeUp" type="hidden" name="" value="1" >
				<table class="productContent">
					<c:forEach items="${ productAttributes}" var="productAttribute">
					   <tr>
					   <th class="productCate">${productAttribute.name}</th>
					   <td >
					   	  <c:forEach items="${ productAttribute.productAttributeValues}" var="productAttributeValue">
						  	<input type="radio" name="${productAttribute.id}" value="${productAttributeValue.id}" id="${ productAttributeValue.id}" disabled="disabled" ><label id = "${ productAttributeValue.id}_l" class="disabled" for="${ productAttributeValue.id}">${productAttributeValue.value }</label>
					   	  </c:forEach>
					   </td>
					   </tr>
					</c:forEach>
				
				 </table>
				<button type="button" value="提交" id="submit" class="submit">确定</button>
			</form>
		</section>
	</section>
</body>
</html>
