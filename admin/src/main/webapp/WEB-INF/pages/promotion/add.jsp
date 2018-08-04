<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- ztree.core核心包 -->
	<script type="text/javascript" src="${staticPath }/js/ztree/jquery.ztree.core-3.5.js"></script>
	<!-- 升级树控件 -->
	<script type="text/javascript" src="${staticPath }/js/ztree/jquery.ztree.exhide-3.5.js"></script>
	<!-- 树形样式 -->
	<link rel="stylesheet" href="${staticPath }/css/ztree/zTreeStyle/zTreeStyle.css" type="text/css">
	
<title>${empty promotion ? '添加' : '修改' }促销管理</title> 
<script src="${contextPath}/static/js/uploadPreview.js" type="text/javascript"></script>
<style type="text/css">
input.file{
    vertical-align:middle;
    position:relative;
    left:-218px;
    filter:alpha(opacity=0);
    opacity:0;
	z-index:1;
	*width:223px;
}
</style>
<script type="text/javascript">
//定义存储促销商品数据的缓存数组
var cacheArray = new Array();

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

// ----------------------------------------------------------------------------------------------------------

$(document).ready(function(){
	var promotion_type = $("#promotion_type").val();
	if( promotion_type == 1 ){ // 首单
		$("#amount_show").show(); // 显示减免金额
	} else if( promotion_type == 2 ){ // 满减
		$("#fullSubtract_show").show(); // 显示满减条件
		// $("#total_show").show(); // 显示促销数量
	} else if( promotion_type == 3 ){ // 折扣
		//$("#condition_show").show(); // 显示折扣条件
		$("#discount_show").show(); // 显示商品折扣
		$("#selectProduct").show(); // 显示选择促销商品选项
		//$("#total_show").show(); // 显示促销数量
	} else if( promotion_type == 4 ){ // 组合
		$("#groupProduct").show();
		$("#category_show").show();
		$("#barcode_show").show();
		$("#costPrice_show").show();
		$("#picture_show").show(); // 显示组合图片
		$("#price_show").show(); // 显示优惠价格
		$("#total_show").show(); // 显示促销数量
	}
});

$(document).ready(function(){
	
	var id = $("#promotionId").val();
	// 修改时
	if( null != id && "" != id ){ 
		var arrayCache = '${promotionProductList}'.split(",");
		for (var z = 0; z < arrayCache.length; z++) {
			if( "" != arrayCache[z]){
				cacheArray.push(arrayCache[z]);
			}
		}
		$("#promotionProductCache").val(cacheArray);
		$("#shared").val('${promotion.shared}');
		$("#promotionImagediv").show();
		$("#categoryId").val('${promotion.categoryId}');
	}
	$("#condition").focus(function(){
		layer.tips('此次活动享受折扣需要购买的商品数量', this, {
		    tips: [2, '#3595CC'], //还可配置颜色
		    time: 3000
		});
	});
});

$(document).ready(function(){
	new uploadPreview({
		UpBtn : "promotionImageId",
		DivShow : "promotionImagediv",
		ImgShow : "promotionImageShow"
	});

	// 模拟点击
	$("[name = upPromotionImage]").bind("click", function () {
	   $("[name = promotionImageId]").click();
	});
	
	$("[name = promotionImageId]").bind("change", function () {
		   var fileName = $("#promotionImageId").val();
		   if( "" == fileName ){
			   $("#imageIdCache").val("");
			   $("#promotionImagediv").hide();
		   } else {
			   $("#imageIdCache").val(fileName);
			   $("#promotionImagediv").show();
		   }
	  });
});

function imageLoad(obj){
	$("#promotionImagediv").show();
	$("#divImageShow").removeClass("col-md-2");
	
	if(obj.offsetHeight != 220 || obj.offsetWidth != 330 ){
		obj.src="";
		lm.alert("图片规格只能是330x220像素");
		$("#promotionImagediv").hide();
		$("#imageIdCache").val("");
	} 
	$("#divImageShow").addClass("col-md-2");
}

$(document).ready(function(){
	var now = lm.Now;//当前日期
	$("#startTime").datetimepicker({
		minView: 0, 
		format: "yyyy-mm-dd hh:ii:ss", //选择日期后，文本框显示的日期格式 
		language: 'zh-CN', //汉化 
		autoclose:true , //选择日期后自动关闭
		startDate:now,
		todayBtn:true,//可选择当天按钮
		todayHighlight:false//高亮当前日期
	 });
	
	$("#endTime").datetimepicker({
		minView: 0, 
		format: "yyyy-mm-dd hh:ii:ss", //选择日期后，文本框显示的日期格式 
		language: 'zh-CN', //汉化 
		autoclose:true , //选择日期后自动关闭
		startDate:now,
		todayBtn:true,//可选择当天按钮
		todayHighlight:false//高亮当前日期
	 });
	
	//判断输入日期的合法性
	$('#startTime').datetimepicker().on('changeDate', function(ev){
		if((new Date(ev.date.valueOf())) > (new Date($('#endTime').val().replace(/-/g,"/")))){
			lm.alert("结束时间不能大于开始时间");
			$("#endTime").val("");
		}
	});
	$('#endTime').datetimepicker().on('changeDate', function(ev){
		if((new Date(ev.date.valueOf())) < (new Date($('#startTime').val().replace(/-/g,"/")))){
			lm.alert("结束时间不能大于开始时间");
			$("#endTime").val("");
		}
	});
});

$(document).ready(function(){
	$("#selectBtn").click(function(){
		// 查询此商家的商品分类
		lm.post("${contextPath}/promotion/list/ajax/productSystemCategory",function(data){
			/*
			$("#productCategoryId option[id!='productCategorySelect']").remove();
			loadCurrentList_promotionList_modelShow(); // 刷新数据
			if(null != data && "" != data){
				for(var i=0;i<data.length;i++){
		    		 $("#productCategoryId").append('<option id="' + data[i].id + '" value="' + data[i].id + '">' + data[i].name + '</option>');
				}
			}
			*/
			
		});
		$("#promotionProductShowID").modal();		
	});
	
	$("#infoButton").click(function(){
		// 查询此商家的商品分类
		lm.post("${contextPath}/promotion/list/ajax/productSystemCategory",function(data){
			/*
			$("#productCategoryId option[id!='productCategorySelect']").remove();
			loadCurrentList_promotionList_modelShow(); // 刷新数据
			if(null != data && "" != data){
				for(var i=0;i<data.length;i++){
		    		 $("#productCategoryId").append('<option id="' + data[i].id + '" value="' + data[i].id + '">' + data[i].name + '</option>');
				}
			}
			*/
		});
		$("#promotionProductShowID").modal();		
	});
	
});

var countNum = 2; // 避免编号重复
$(document).ready(function(){
	$("a[name='fullSubtractAdd']").removeAttr('href'); // 让A标签失效

	$("a[name='fullSubtractAdd']").click(function(){
		var element = $("#fullSubtract_show");
		 element.append("<div class='form-group' id='"+ countNum++ +"'><label class='col-md-1 control-label'>满</label> <div class='col-md-1'><input type='text' id='fullSubtract"+ countNum+"' name='fullSubtractKey' class='form-control' maxlength='7'/></div><label class='' style='float: left;padding-top: 6px'>减</label> <div class='col-md-1'><input type='text' id='fullSubtract"+ countNum+"' name='fullSubtractValue' class='form-control' maxlength='7'/></div> <a onclick='fullSubtractDelete(this);' id='deletebutton"+ countNum+"' name='deletebutton' class='btn btn-small btn-danger'>删除</a></div>");
	});
});

function fullSubtractDelete(obj){
	 $(obj).parent().remove();
}

$(document).ready(function(){
	// 商品添加
	$("#promotionInfoAddBtn").click(function(){
		var promotion_type = $("#promotion_type").val();
		if( promotion_type == 4 ){
			if( cacheArray.length <= 0 ){
				lm.alert("请选择需要新增的组合商品");
				$("#product_stock_table").find("tr[id!='product_stock_show']").remove();
				return;
			}
		}
		
		cacheArray.sort(); // 排序
		if( promotion_type == 4 ){ // 组合
			var html = "";
			var cacheString = "";
			for( var i =0 ; i < cacheArray.length; i++){
				cacheString += cacheArray[i];
				if( i != (cacheArray.length-1) ){
					cacheString += ",";					
				}
			}
			lm.post("${contextPath}/promotion/list/ajax/getProductStock",{cacheString:cacheString},function(data){
				$("#product_stock_table").find("tr[id!='product_stock_show']").remove();
				if(null != data && "" != data){
					for(var z=0;z<data.length;z++){
						var attribute = "";
						if(null != data[z].attributeValuesListJointValue && "" != data[z].attributeValuesListJointValue ){
							attribute = "|" + data[z].attributeValuesListJointValue;
						}
						html += "<tr> <td style='width:50px'>" +(z+1)+ "</td> <td style='width:150px'>"+ data[z].productName + attribute  +"</td><td>"+data[z].price+"</td> </tr>";
					}
					$("#product_stock_table").append(html);
				}
			});
		}
		$("#promotionProductCache").val(cacheArray);
		$("#promotionClose").click();
	});
	
	//保存/修改
	$("#promotionBtn").click(function(){
		var name = $("#name").val();
		name = $.trim(name);  // 用jQuery的trim方法删除前后空格
		if( null == name || "" == name ){
			lm.alert("促销名称不能为空");
			return;
		}
		
		var falg = true;
		var id = $("#promotionId").val();
		lm.postSync("${contextPath}/promotion/list/ajax/checkName",{id:id,name:name},function(data){
			if(data == 0){
				falg = false;
			} else {
				lm.alert("促销名称存在重复");
			}
		});
		
		if(falg){
			return ;
		}
		
		var promotion_type = $("#promotion_type").val();
		if( promotion_type == 1 ){ // 首单
			var amount = $("#amount").val();
			amount = $.trim(amount);  // 用jQuery的trim方法删除前后空格
			if( null == amount || "" == amount ){
				lm.alert("减免金额不能为空");
				return ;
			}
			if(!(lm.isFloat(amount))){
				lm.alert("减免金额输入错误!");
				return ;
			}
		} else if( promotion_type == 2 ){ // 满减
			var flag = false;
			$("#fullSubtract_show").find("input").each(function(key ,value){
				var fullName = $(this).val();
				fullName = $.trim(fullName);  // 用jQuery的trim方法删除前后空格
				if( null == fullName || "" == fullName ){
					lm.alert("满减条件不能为空");
					flag = true;
					return ;
				}
				if(!(/^\d*$/.test(fullName))){
					lm.alert("满减条件输入错误!");
					flag = true;
					return ;
				}
				
				if( fullName.length > 6 ){
					lm.alert("满减条件长度不能大于6位数!");
					flag = true;
					return ;
				}
			});
			
			if(flag){
				return;
			}
		
			var total = $("#total").val();
			total = $.trim(total);
			if( null != total && "" != total ){
				if( !(/^\+?[1-9][0-9]*$/.test(total)) ){
					lm.alert("促销数量必须为正整数");
					return ;
				}
			}
		} else if( promotion_type == 3 ){ // 折扣
			var condition = $("#condition").val();
			condition = $.trim(condition);
			if( null != condition && "" != condition ){
				if( !(/^\+?[1-9][0-9]*$/.test(condition)) ){
					lm.alert("折扣条件必须为正整数");
					return ;
				}
			}
			var discount = $("#discount").val();
			discount = $.trim(discount);  // 用jQuery的trim方法删除前后空格
			if( null == discount || "" == discount ){
				lm.alert("商品折扣不能为空");
				return;
			}
			if(!(lm.isFloat(discount) && (discount > 0 && discount < 10))){
				lm.alert("请输入正确的商品折扣");
				return;
			}
			var total = $("#total").val();
			total = $.trim(total);
			if( null != total && "" != total ){
				if( !(/^\+?[1-9][0-9]*$/.test(total)) ){
					lm.alert("促销数量必须为正整数");
					return ;
				}
			}
		} else if( promotion_type == 4 ){ // 组合
			// 图片不能为空
		   var imageIdCache = $("#imageIdCache").val(); // 图片
		   imageIdCache = $.trim(imageIdCache);
		   if( null == imageIdCache || "" == imageIdCache){
			   lm.alert("图片不能为空");
			   return ;
		   }
			   
			if( cacheArray.length <= 0 ){
				lm.alert("请选择需要新增的组合商品");
				return;
			}
			
			var barCode = $("#barCode").val(); // 条形码
			barCode = $.trim(barCode);
			if( barCode == "" || barCode == null ){
				lm.alert("商品条码不能为空!");
				return;
			}
			if( !(/^[0-9]\d*$/.test(barCode)) ){
				lm.alert("条码输入错误，条形码只能为正整数！");
				return ;
			}
			
			var isSign = false;
			lm.postSync("${contextPath}/promotion/list/ajax/existBarCode",{id:id,barCode:barCode},function(data){
				if(data == 1){
					lm.alert("此条形码已存在！");
					isSign = true;
				}
			});	
			
			if(isSign){
				return;
			}
			
			var price = $("#price").val();  
			price = $.trim(price);
			if( null == price || "" == price ){
				lm.alert("优惠价格不能为空");
				return;
			}
			if(!(lm.isFloat(price))){
				lm.alert("优惠价格输入错误!");
				return ;
			}
			var total = $("#total").val();
			total = $.trim(total);
			if( null != total && "" != total ){
				if( !(/^\+?[1-9][0-9]*$/.test(total)) ){
					lm.alert("促销数量必须为正整数");
					return ;
				}
			}
		}
	
		var startDate = $("#startTime").val();
		if( null == startDate || "" == startDate ){
			lm.alert("促销开始时间不能为空");
			return;
		}
		
		var endDate = $("#endTime").val();
		if( null == endDate || "" == endDate ){
			lm.alert("促销结束时间不能为空");
			return;
		}
		
		//--------------------检测了活动时间段再提交---------------
		var status = $("#status").val();
		if( promotion_type == 1 || promotion_type == 2){
			var sign = promotion_type == 1 ? "首单" : "满减";
			var first = false;
			lm.postSync("${contextPath}/promotion/list/ajax/checkFirstOrFull",{id:id,promotion_type:promotion_type,status:status},function(data){
				if( data == 1 ){
					lm.alert("此商家已经添加了"+sign+"促销");
					first = true;
				}
			});
			if(first){
				return;
			}
		}
		if( promotion_type == 4 || promotion_type == 3 ){
			var submit_flag = false;
			var promotionProductCache = $("#promotionProductCache").val();
			
			lm.postSync("${contextPath}/promotion/list/ajax/checkTimeInterleaving",{id:id,promotion_type:promotion_type,status:status,startTime:startDate,endTime:endDate,promotionProductCache:promotionProductCache},function(data){
				if(data == 1){
					lm.alert("促销商品与其他活动商品存在重复");
					submit_flag = true;
				}
			});
			
			if(submit_flag){
				return;
			}
		} 
		//----------------------------------------
		
		$("#promotionForm").submit();
	}); 
});
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>${empty promotion ? '添加' : '修改' }
				${promotion_type ==1 ?'首单':promotion_type == 2 ?'满减': promotion_type == 3 ? '折扣' : promotion_type == 4 ? '组合' : '' }
				促销
			</strong>
		</div>
		<div class='panel-body'>
			<form id="promotionForm" method='post' repeatSubmit='1' class='form-horizontal' autocomplete="off" action="${contextPath }/promotion/save" enctype="multipart/form-data">
				<!-- 隐藏promotionId,判断是否是修改操作 -->
				<input type="hidden" name="id" id="promotionId" value="${promotion.id }">
				<!-- 隐藏促销类型，判断是首单/满减/折扣/组合 -->
				<input type="hidden" name="promotion_type" id="promotion_type" value="${promotion_type }">
				<!-- 隐藏存储的促销商品 -->
				<input type="hidden" name="promotionProductCache" id="promotionProductCache" value="">
				
				<div class="form-group">
					<label class="col-md-1 control-label">促销名称</label>
					<div class="col-md-2">
						<input type="text" name="name" class='form-control' id="name" value="${promotion.name }" maxlength="190">
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group" id="category_show" style="display: none">
					<label class="col-md-1 control-label">选择分类</label>
					<div class="col-md-2">
						<m:treeSelect treeRoot="treeRoot" selectID="categoryId"></m:treeSelect>
			        	<input type="hidden" name="treeStoreId" id="treeStoreId" value="${treeStoreId }">
	            	</div>
	            	<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
				
				<div class="form-group" id="picture_show" style="display: none">
					<label class="col-md-1 control-label">相关图片</label>
					<div class="col-md-2" id="divImageShow">
						<input type="button" id="upPromotionImage" class="btn " name="upPromotionImage" value="上传促销图片"/>
						<input type="file" id="promotionImageId" style="width: 1px; height:1px" name="promotionImageId" class="file" value="上传图片" title="上传图片"/>
						<input type='hidden' id="imageId" name="imageId" class='form-control' value="${promotion.imageId }" />
						<input type='hidden' id="imageIdCache" name="imageIdCache" class='form-control' value="${promotion.imageId }"/>
						<div id="promotionImagediv">
							<img alt="图片展示" style="cursor: pointer;" id="promotionImageShow" name="promotionImageShow" src="${promotion.imagePIC }" onload="imageLoad(this);">
						</div>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
				
				<div class="form-group" id="selectProduct" style="display: none">
					<label class="col-md-1 control-label">选择促销商品</label>
					<div class="col-md-2">
						<input type="button" name="selectBtn" class="btn btn-primary" id="selectBtn" value="选择商品或者分类" >
							不选默认全部商品
					</div>
				</div>
				
				<div class="form-group" id="groupProduct" style="display: none">
					<label class="col-md-1 control-label">选择组合商品</label>
					<div class="col-md-2">
						<button type="button" id="infoButton" name='infoButton' class="btn btn-small btn-primary">选择组合商品</button>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
						<table class='form-control' border=0 style="width: 300px;height:auto;margin-left:120px; margin-top: 10px;"  id="product_stock_table">
							<tr id="product_stock_show"><td style="width:50px">序号</td><td style="width:150px">商品名称</td><td>市场价格</td></tr>
							<c:forEach items="${productStockList }" varStatus="sortNumber" var="productStockList" >
								<c:set var="index" value="${ sortNumber.index}"></c:set>
								<tr>
									<td style="width:50px">${index+1}</td>
									<td style="width:150px">${productStockList.productName}<c:if test="${productStockList.attributeValuesListJointValue != null }"> | ${productStockList.attributeValuesListJointValue }</c:if></td>
									<td>${productStockList.price }</td>
								</tr>
							</c:forEach>
						</table>
				</div>
				
				<div class="form-group" id="barcode_show" style="display: none">
					<label class="col-md-1 control-label">商品条码</label>
					<div class="col-md-2">
						<input type='text' id="barCode" name="barCode" value="${promotion.barCode }" class='form-control' maxlength="15"/>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group" id="costPrice_show" style="display : none">
					<label class="col-md-1 control-label">成本单价</label>
					<div class="col-md-2">
						<input type='text' id="costPrice" name="costPrice" class='form-control' value="<fmt:formatNumber value="${promotion.costPrice }" type="currency" pattern="0.00"/>" maxlength="7"/>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group" id="amount_show" style="display: none">
					<label class="col-md-1 control-label">减免金额(元)</label>
					<div class="col-md-2">
						<input type="text" name="amount" class='form-control' id="amount" value="${promotion.amount }" maxlength="7">
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group" id="condition_show" style="display: none">
					<label class="col-md-1 control-label">折扣条件</label>
					<div class="col-md-2">
						<input type="text" name="condition" placeholder="不填则表示不限数量" data-toggle="tooltip" title="此次活动享受折扣需要购买的商品数量" class='form-control' id="condition" value="${promotion.condition == -1 ? '' : promotion.condition }" maxlength="10">
					</div>
				</div>
				
				<div class="form-group" id="discount_show" style="display: none">
					<label class="col-md-1 control-label">商品折扣</label>
					<div class="col-md-2">
						<input type="text" name="discount" class='form-control' id="discount" value="${promotion.discount }" maxlength="3">
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;比如：1表示1折
				</div>
				
				<div class="form-group" id="price_show" style="display: none">
					<label class="col-md-1 control-label">优惠价格</label>
					<div class="col-md-2">
						<input type="text" name="price" class='form-control' id="price" value="${promotion.price }" maxlength="7">
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div id="fullSubtract_show" style="display:none">
					<c:if test="${not empty fullSubtractMap }">
						<c:forEach items="${fullSubtractMap }" varStatus="sortNo" var="fullSubtractHM">
							<c:set var="v" value="${ sortNo.index}"></c:set>
							<div class="form-group" id="${v}">
								<label class="col-md-1 control-label">满</label>
								<div class="col-md-1">
									<input type="text" id="fullSubtract${v}" name="fullSubtractKey" value="${fullSubtractHM.key}" class='form-control' maxlength="7"/>
								</div>
								<label class="" style="float: left;padding-top: 6px">减</label>
								<div class="col-md-1">
									<input type="text" id="fullSubtract${v}_${v+1}" name="fullSubtractValue" value="${fullSubtractHM.value}" class='form-control' maxlength="7"/>
								</div>
								<c:if test="${sortNo.first }">
									<a href="#" class="btn btn-primary" name="fullSubtractAdd" style="margin-top: 5px;"><i class="icon-plus"></i></a>								
								</c:if>
								<c:if test="${v != 0}">
									<a onclick="fullSubtractDelete(this);" id="deletebutton_${v}" name="deletebutton" class="btn btn-small btn-danger">删除</a>									
								</c:if>
							</div>
						</c:forEach>
					</c:if>
					<c:if test="${empty fullSubtractMap }">
						<div class="form-group" id="0">
							<label class="col-md-1 control-label">满</label>
							<div class="col-md-1">
								<input type="text" id="fullSubtract0" name="fullSubtractKey" value="" class='form-control' maxlength="7"/>
							</div>
							<label class="" style="float: left;padding-top: 6px">减</label>
							<div class="col-md-1">
								<input type="text" id="fullSubtract1" name="fullSubtractValue" value="" class='form-control' maxlength="7"/>
							</div>
							<a href="#" class="btn btn-primary" name="fullSubtractAdd" style="margin-top: 5px;"><i class="icon-plus"></i></a>
						</div>
					</c:if>
				</div>		
								
				<div class="form-group" id="total_show" style="display: none">
					<label class="col-md-1 control-label">促销数量</label>
					<div class="col-md-2">
						<input type="text" id="total" name="total" value="${promotion.total == -1 ? '' : promotion.total }" placeholder="不填则表示不限数量" class='form-control' maxlength="10"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">开始时间</label>
					<div class="col-md-2">
						<input type='text' readonly="readonly" id="startTime" name="startTime" class='form-control' value="<fmt:formatDate value="${promotion.startDate }" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">结束时间</label>
					<div class="col-md-2">
						<input type='text' readonly="readonly" id="endTime" name="endTime" class='form-control' value="<fmt:formatDate value="${promotion.endDate }" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group" style="display: none">
					<label class="col-md-1 control-label">是否共享促销</label>
					<div class="col-md-2">
						<select id="shared" name="shared" class='form-control'> 
							<option id="0" value="0" selected="selected">不共享</option>
							<option id="1" value="1">共享</option>
						</select>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px">*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">促销状态</label>
					<div class="col-md-2">
						<m:slider defaultStatus="${promotion.status }" width="75" height="30" inputId="status" inputName="status"></m:slider>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
				
				<c:choose>
					<c:when test="${promotion_type == 3 || promotion_type == 2}">
						<div class="form-group">
							<label class="col-md-1 control-label">适用范围</label>
							<div class="col-md-2">
								<input type="radio" name="scope" value="0" checked="checked" <c:if test="${promotion.scope == 0  }">checked="checked"</c:if> />全部
								<input type="radio" name="scope" value="1" <c:if test="${promotion.scope == 1  }">checked="checked"</c:if>/>app端使用
								<input type="radio" name="scope" value="2" <c:if test="${promotion.scope == 2  }">checked="checked"</c:if>/>pos端使用
							</div>
						</div>
					</c:when>
					<c:otherwise>
						<input type="hidden" name="scope" value="1"/>
					</c:otherwise>
				</c:choose>
				
					
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button"  id='promotionBtn' class='btn btn-primary' value="${empty promotion ? '添加' : '修改' }" data-loading='稍候...'/>
					</div>
				</div>
			</form>
	  </div>
	  	<!-- 促销商品展示start -->
		  <div class="modal fade" id="promotionProductShowID">
			 <div class="modal-dialog modal-lg" style="width:1200px">
				  <div class="modal-content">
					    <div class="modal-header">
					      <button type="button" id="promotionClose" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
					    </div>
					    <div class="modal-body">
							 <m:list title="商品列表" id="promotionList_modelShow"
								listUrl="/promotion/showProductStockModel/list/list-data"
								 searchButtonId="promotion_mode_search_btn">
								<div class="input-group" style="max-width: 800px;">
									<span class="input-group-addon">商品分类</span>
										<m:treeSelect treeRoot="productTreeRoot" showAllOption="1" selectID="productCategoryId"></m:treeSelect>
										<!-- 隐藏促销类型，判断是首单/满减/折扣/组合 -->
									<input type="hidden" name="promotionType" id="promotionType" value="${promotion_type }">
									<span class="input-group-addon">商品名称</span> 
									<input type="text"	name="productName" id="productName" class="form-control" placeholder="商品名称" style="width: 200px;">
								</div>
									<input style="margin-top: 5px;" type="button" id='promotionInfoAddBtn' class='btn btn-primary' value="确定"/>		
							</m:list>
					    </div>
				  </div>
			</div>
		</div>
	  <!-- 促销商品展示end -->
   </div>
</body>
</html>