<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty roomPackage ? '添加' : '修改' }套餐设置 </title>
<script type="text/javascript">
var cacheArray = new Array();

function checkNum(obj){
	if (!lm.isPositiveNum(obj.value)){
		lm.alert("请输入正整数");
		obj.value = 1;
	}
	totalPrice();
}

function checkboxSelectEvent(obj){
	if(obj.checked){
		$(obj).prop("checked",false);
	} else {
		$(obj).prop("checked",true);
	}
}

function trSelectEvent(obj){
	var child = $(obj).find("input:checkbox");
	if(child[0].checked){
		child.prop("checked",false);
		// 移除指定的元素
		cacheArray.splice($.inArray($(obj).attr("stockIdSign"),cacheArray),1); 
	} else {
		child.prop("checked",true);
		cacheArray.push($(obj).attr("stockIdSign"));	
	}
}

function checkPageALL(obj){
	if(obj.checked){
		$("#show_ProductStock_Model input:checkbox[id!='pageALLCheckBox']").each(function(key,value){
			if(-1 ==  $.inArray($(value).attr("stockIdSign") ,cacheArray) ){
				cacheArray.push($(value).attr("stockIdSign"));	
			}
		});
		$("#show_ProductStock_Model input:checkbox[id!='pageALLCheckBox']").prop("checked",true);
	} else {
		$("#show_ProductStock_Model input:checkbox[id!='pageALLCheckBox']").each(function(key,value){
			// 查询数组中是否有此数据，有则从数组中移除
			if(-1 !=  $.inArray($(value).attr("stockIdSign") ,cacheArray) ){
				// 移除指定的元素
				cacheArray.splice($.inArray($(value).attr("stockIdSign") ,cacheArray),1); 
			}
		});
		$("#show_ProductStock_Model input:checkbox[id!='pageALLCheckBox']").prop("checked",false);
	}
}

//---------------------------------------------------------------------------------------------------------------

	$(function(){
		var id = $("#roomPackageId").val();
		// 修改时
		if( null != id && "" != id ){ 
			var arrayCache = '${roomPackageProductList}'.split(",");
			for (var z = 0; z < arrayCache.length; z++) {
				if( "" != arrayCache[z]){
					cacheArray.push(arrayCache[z]);
				}
			}
			$("#roomPackageProductCache").val(cacheArray);
		}
	});
	$(function(){
		//绑定 提交按钮点击事件
		$("#roomPackageAddBtn").click(function() {
			var name = $("#name").val();
			name = $.trim(name);  // 用jQuery的trim方法删除前后空格
			if( null == name || "" == name ){
				lm.alert("套餐名称不能为空");
				return;
			}
			
			var falg = true;
			var id = $("#roomPackageId").val();
			lm.postSync("${contextPath}/roomPackage/list/ajax/checkName",{id:id,name:name},function(data){
				if(data == 0){
					falg = false;
				} else {
					lm.alert("套餐名称存在重复");
				}
			});
			
			if(falg){
				return ;
			}
			
			if( cacheArray.length <= 0 ){
				lm.alert("请选择套餐商品");
				return;
			}
			
			var price = $("#price").val();  
			price = $.trim(price);
			if( null == price || "" == price ){
				lm.alert("实际价格不能为空");
				return;
			}
			if(!(lm.isTwoPointFloat(price))){
				lm.alert("实际价格输入错误!");
				return ;
			}
			
			var duration = $("#duration_Cache").val();  
			duration = $.trim(duration);
			if( null == duration || "" == duration ){
				lm.alert("消费时长不能为空");
				return;
			}
			if( !(/^[0-9]+([.]\d{1})?$/.test(duration) && parseFloat(duration) > 0 ) ){
				lm.alert("消费时长输入错误！例如：0.5或者3 等");
				return ;
			}
			$("#roomPackageAddForm").submit();
		});//提交按钮点击事件结束
	});
	

	function delStock(obj){
		$(obj).parent().parent().remove();
		totalPrice();
		var id = $(obj).parent().parent().find("input").attr("idvalue");
		
		var cache = $("#roomPackageProductCache").val();
		var newCache = "";
		var cacheArr = cache.split(",");
		for (var i = 0; i < cacheArr.length; i++){
			if (cacheArr[i] != id){
				if (newCache == ""){
					newCache += cacheArr[i];
				}else {
					newCache += "," + cacheArr[i];
				}
			}
		}
		
		$("#roomPackageProductCache").val(newCache);
		
		// 查询数组中是否有此数据，有则从数组中移除
		if(-1 !=  $.inArray(id ,cacheArray) ){
			// 移除指定的元素
			cacheArray.splice($.inArray(id ,cacheArray),1); 
		}
	}

	function totalPrice(){
		var total = 0;
		var tb = $("#product_stock_table").find("tr[id!='product_stock_show']");
		var length = tb.length;
		$("#product_stock_table").find("tr[id!='product_stock_show']").each(function(k,v){
			if (length - 1 != k){
				$($(v).find("td")[0]).html(k+1);
				var amount = $(v).find("input").val();
				total += $($(v).find("td")[4]).html() * 100 * amount;
			}
		});
		
		$($(tb[length-1]).find("td")[4]).html(total/100);
	}
$(function(){
		
		$("#infoButton").click(function(){
			// 查询此商家的商品分类
			lm.post("${contextPath}/roomPackage/list/ajax/productSystemCategory",function(data){
				$("#productCategoryId option[id!='productCategorySelect']").remove();
				loadCurrentList_roomPackageList_modelShow(); // 刷新数据
				if(null != data && "" != data){
					for(var i=0;i<data.length;i++){
			    		 $("#productCategoryId").append('<option id="' + data[i].id + '" value="' + data[i].id + '">' + data[i].name + '</option>');
					}
				}
			});
			$("#roomPackageProductShowID").modal();		
		});
		
	// 商品添加
	$("#roomPackageInfoAddBtn").click(function(){
		if( cacheArray.length <= 0 ){
			lm.alert("请选择套餐商品");
			//$("#product_stock_table").find("tr[id!='product_stock_show']").remove();
			return;
		}
		
		var idvalues = $("[name='roomPackageProductCacheAmount']");
		var objArray = new Array();
		if (idvalues.length > 0){
			idvalues.each(function(k,v){
				var amount = $(this).val();
				var obj = new Object();
				obj.id = $(this).attr("idvalue");
				obj.amount = amount;
				objArray.push(obj);
			});
		}
		
		cacheArray.sort(); // 排序
		var html = "";
		var cacheString = "";
		for( var i =0 ; i < cacheArray.length; i++){
			cacheString += cacheArray[i];
			if( i != (cacheArray.length-1) ){
				cacheString += ",";
			}
		}
		var countNumber = 0;
		lm.post("${contextPath}/roomPackage/list/ajax/getProductStock",{cacheString:cacheString},function(data){
			$("#product_stock_table").find("tr[id!='product_stock_show']").remove();
			if(null != data && "" != data){
				for(var z=0;z<data.length;z++){
					var attribute = "";
					if(null != data[z].attributeValuesListJointValue && "" != data[z].attributeValuesListJointValue ){
						attribute = data[z].attributeValuesListJointValue;
					}
					html += "<tr> <td name='idvalue' value='"+data[z].id+"' style='width:50px'>" +(z+1)+ "</td> <td style='width:230px'>"+ data[z].productName +"</td><td style='width:230px'>"+ attribute 
						+"</td><td style='width:200px'> <input onkeyup='checkNum(this);' style='width:50px' idvalue='"+data[z].id
						+"'  name='roomPackageProductCacheAmount' value='1' maxlength='4'/> "+"</td> <td style='width:200px'>"+data[z].price
						+"</td><td> <button class='btn btn-warn' onclick='delStock(this);'>删除</button> </td> </tr>";
					countNumber += data[z].price * 100;
				}
				html +="<tr> <td style='width:50px'>总计</td> <td style='width:230px'></td><td style='width:230px'></td><td></td><td>"+(countNumber/100)+"</td> </tr>";
				$("#product_stock_table").append(html);
				
				if (objArray.length > 0){
					for (var i = 0; i < objArray.length; i++){
						var obj = objArray[i];
						$("[name='roomPackageProductCacheAmount']").each(function(k,v){
							var id = $(this).attr("idvalue");
							if (id == obj.id ){
								$(this).val(obj.amount);
							}
						});
					}
				}
				totalPrice();
			}
		});
		$("#roomPackageProductCache").val(cacheArray);
		$("#roomPackageClose").click();
	});
		
});

$(function(){
	if( "" != $("#roomPackageId").val() ){
		totalPrice(); // 修改进来之前就先计算		
	}
});
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong><i class='icon-plust'></i>${empty roomPackage ? '添加' : '修改' }套餐设置  </strong>
		</div>
		<div class='panel-body'>
			<form id="roomPackageAddForm" repeatSubmit='1' method='post' class='form-horizontal' action="${contextPath }/roomPackage/save">
				
				<!-- 隐藏存储的促销商品 -->
				<input type="hidden" name="roomPackageProductCache" id="roomPackageProductCache" value="">
				
				<input name="id" id="roomPackageId" type="hidden" value="${roomPackage.id }" />
				
				<div class="form-group">
					<label class="col-md-1 control-label">套餐名称</label>
					<div class="col-md-2">
						<input type='text' id="name" name="name" maxlength="21" value="${roomPackage.name }" class='form-control' />
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">选择套餐内容</label>
					<div class="col-md-2">
						<button type="button" id="infoButton" name='infoButton' class="btn btn-small btn-primary">选择套餐内容</button>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
						<table class='form-control' border=0 style="width: 600px;height:auto;margin-left:120px; margin-top: 10px;"  id="product_stock_table">
							<tr id="product_stock_show"><td style="width:50px">序号</td><td style="width:230px">服务项目或商品</td><td style="width:230px">规格</td> 
							<td style="width:200px">数量</td>  <td style="width:200px">价格</td><td style="width:230px"></td></tr>
							<c:set var="countNumber" value="0"></c:set>
							<c:forEach items="${productStockList }" varStatus="sortNumber" var="productStockList" >
								<c:set var="index" value="${ sortNumber.index}"></c:set>
								<tr>
									<td style="width:50px" name="idvalue" value="${productStockList.id}" >${index+1}</td>
									<td style="width:230px">${productStockList.productName}</td>
									<td style="width:230px"><c:if test="${productStockList.attributeValuesListJointValue != null }"> ${productStockList.attributeValuesListJointValue }</c:if></td>
									<td style="width:200px"> <input onkeyup='checkNum(this);' maxlength="4" style="width:50px" name="roomPackageProductCacheAmount" idvalue="${productStockList.id }" value="${productStockList.stock }"/> </td>
									<td style="width:200px">${productStockList.price }</td>
									<td> <button class='btn btn-warn' onclick='delStock(this);'>删除</button> </td>
								</tr>
								<c:set var="countNumber" value="${countNumber + productStockList.price  * 100 * productStockList.stock}"></c:set>
							</c:forEach>
								<tr>
									<td style="width:50px">合计</td>
									<td style="width:230px"></td>
									<td style="width:200px"></td>
									<td style="width:200px"></td>
									<td>${countNumber / 100}</td>
									<td></td>
								</tr>
						</table>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">实际价格</label>
					<div class="col-md-2">
						<input type='text' id="price" name="price" maxlength="10" value="${roomPackage.price }" class='form-control' />
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >元*</label>
				</div>
				
					<div class="form-group">
					<label class="col-md-1 control-label">消费时长</label>
					<div class="col-md-2">
						<input type='text' id="duration_Cache" name="duration_Cache" maxlength="10" value="${null != roomPackage ? roomPackage.duration / 60 : 1 }" class='form-control' />
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >小时*</label>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">备注说明</label>
					<div class="col-md-2">
						<textarea id="memo"  name="memo" value="${roomPackage.memo }" placeholder="上限100字" cols=15 rows=5  class='form-control' maxlength="100">${roomPackage.memo }</textarea>
					</div>
				</div>
				
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button"   id='roomPackageAddBtn' class='btn btn-primary' value="${empty roomPackage ? '添加' : '修改' }" data-loading='稍候...' />
					</div>
				</div>
			</form>
		</div>
	</div>
	
	<!-- 促销商品展示start -->
		  <div class="modal fade" id="roomPackageProductShowID">
			 <div class="modal-dialog modal-lg" style="width:1200px">
				  <div class="modal-content">
					    <div class="modal-header">
					      <button type="button" id="roomPackageClose" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
					    </div>
					    <div class="modal-body">
							 <m:list title="添加商品列表" id="roomPackageList_modelShow"
								listUrl="/roomPackage/showProductStockModel/list/list-data"
								 searchButtonId="roomPackage_mode_search_btn">
								<div class="input-group" style="max-width: 800px;">
									<span class="input-group-addon">商品分类</span>
									<select id="productCategoryId" class='form-control' style="width: auto;float:left;margin-right:40px;" name="productCategoryId">
										<option id="productCategorySelect" value="-1">请选择</option>
									</select>
									<span class="input-group-addon">商品名称</span> 
									<input type="text"	name="productName" id="productName" class="form-control" placeholder="商品名称" style="width: 200px;">
								</div>
									<input style="margin-top: 5px;" type="button" id='roomPackageInfoAddBtn' class='btn btn-primary' value="确定"/>		
							</m:list>
					    </div>
				  </div>
			</div>
		</div>
	  <!-- 促销商品展示end -->
</body>
</html>