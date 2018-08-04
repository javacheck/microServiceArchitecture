<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty productStock ? '添加' : '修改' }库存</title> 
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
var sysAttrLength=0; // 商品属性个数的一个全局标识
var delImageLength=0;
$(document).ready(function(){
	var isSys=${isSys};
	var isMainStore=${isMainStore};
	if(isSys){
		$("#storeDiv").show();
	}
	delImageLength="${cerUrlMap.size()}";
	var shelves="${productStock.shelves}";//上下架
	if(shelves==1){
		 $("input[name='shelves']").eq(1).prop("checked",true);
	}
	new uploadPreview({
		UpBtn : "image",
		DivShow : "logoImagediv",
		ImgShow : "logoImage"
	});
	var type="${product.type}";
	var categoryId="${product.categoryId}";
	var categoryName="${product.categoryName}";
	if(type=="0"){//有属性
		$("#addAdminCategory").show();//管理员分类下拉显示
		$("#sysCategoryId").append('<option  value="' + categoryId + '">' + categoryName + '</option>');
		$("#sysCategoryId").attr("disabled",true);
		getSysCategoryId(categoryId);
	}else if(type=="1"){
		$("#addAdminCategory").show();//管理员分类下拉显示
		$("#sysCategoryId").append('<option  value="' + categoryId + '">' + categoryName + '</option>');
		$("#sysCategoryId").attr("disabled",true);
	}
	
	
	$("[name = upLogo]").bind("click", function () {
		   $("#image").click();
	  });
	$("[name = 'name']").bind("click", function () {
		if(isSys){
			if($("#storeId").val()!=null && $("#storeId").val()!=""){
				var storeId=$("#storeId").val();
				$("#storeModelId").val(storeId);
				$('#productNameAddModal').modal();
				$("#productStockList_search_btn").click();
			  }else{
				lm.alert("请先选择商家！");
				return;
			  }
		}else{
			var storeId="${isStoreId}";
			$("#storeModelId").val(storeId);
			$('#productNameAddModal').modal();
		}
	  }); 
	//绑定 提交方法
	 $("#productStockAddBtn").click(function() {
		 	getEditorContent();
		 	
		 	var id = $("#id").val(); // 库存ID
		 	var storeId=$("#storeId").val();
			var isSys=${isSys};
			var isMainStore=${isMainStore};
			if(isSys){
				if(id==null || id==""){
					if(storeId==null || storeId=="" || storeId==-1){
						lm.alert("商店不能为空！");
						return;
					}
				}
			}else{
				storeId="${isStoreId}";
			}
			var productName="";
			if(id==null || id==""){
				productName = $("#productName").val(); // 商品名称
			}else{
				productName="${product.name}";
			}
			if(id==null || id==""){
				if( productName == "" || productName == null ){
					lm.alert("商品名称不能为空!");
					return;
				}
			}
			var barCode = $("#barCode").val(); // 条形码
			if( barCode == "" || barCode == null ){
				lm.alert("商品条码不能为空!");
				return;
			}
			if( !(/^[0-9]\d*$/.test(barCode)) ){
				lm.alert("条码输入错误，条形码只能为正整数！");
				return ;
			}
			var remarks = $("#remarks").val(); // 备注
			
			var shelves=$("input:radio[name='shelves']:checked").val();
			var categoryId = $("#topid option:selected").val(); // 商品分类ID
			var productId = $("#productId").val(); // 商品ID
			
			
			var price = $("#price").val(); // 销售单价
			if( price == "" || price == null ){
				lm.alert("销售单价不能为空!");
				return;
			}
			if( !(lm.isFloat(price) && price >= 0) ){
				lm.alert("请输入正确的销售单价！");
				return ;
			}else{
				if(price.indexOf(".")!=-1){
					if(price.substring(price.indexOf(".")+1,price.length).length>2){
						lm.alert("销售单价小数位数不能超过两位!");
						return;
					}
				}
			}
			var memberPrice = $("#memberPrice").val(); // 会员价
			if( memberPrice != "" && memberPrice != null ){
			
				if( !(lm.isFloat(memberPrice) && memberPrice >= 0) ){
					lm.alert("请输入正确的会员价！");
					return ;
				}else{
					if(memberPrice.indexOf(".")!=-1){
						if(memberPrice.substring(memberPrice.indexOf(".")+1,memberPrice.length).length>2){
							lm.alert("会员价小数位数不能超过两位!");
							return;
						}
					}
				}
			}
			var marketPrice = $("#marketPrice").val(); // 市场价格
			if( marketPrice == "" || marketPrice == null ){
				lm.alert("市场价格不能为空!");
				return;
			}
			if( !(lm.isFloat(marketPrice) && marketPrice >= 0) ){
				lm.alert("请输入正确的市场价格！");
				return ;
			}else{
				if(marketPrice.indexOf(".")!=-1){
					if(marketPrice.substring(marketPrice.indexOf(".")+1,marketPrice.length).length>2){
						lm.alert("市场价格小数位数不能超过两位!");
						return;
					}
				}
			}
			var costPrice = $("#costPrice").val(); // 成本价格
			if(costPrice == "" || costPrice == null ){
				lm.alert("成本价格不能为空!");
				return;
			}
			if( !(lm.isFloat(costPrice) && costPrice >= 0) ){
				lm.alert("请输入正确的成本价格！");
				return ;
			}else{
				if(costPrice.indexOf(".")!=-1){
					if(costPrice.substring(costPrice.indexOf(".")+1,costPrice.length).length>2){
						lm.alert("成本价格小数位数不能超过两位!");
						return;
					}
				}
			}
			
			var stock = $("#stock").val(); // 库存数量
			if( stock != "" && stock != null ){
				if( !(/^[0-9]\d*$/.test(stock)) ){
					lm.alert("库存输入错误，库存只能为正整数！");
					return ;
				}
			}
			var alarmValue = $("#alarmValue").val(); // 缺货提醒
			if( alarmValue == "" || alarmValue == null ){
				lm.alert("警报值不能为空！");
				return;
			}
			if( !(/^[0-9]\d*$/.test(alarmValue)) ){
				lm.alert("警报值输入错误，警报值只能为正整数！");
				return ;
			}
			/* if(getEditorContent()==""){
			 	lm.alert("商品详情不能为空！");
				return;
			} */
			
			var flag = true;
			var attributeCode = ""; // 属性ID
			var attributeName="";
			// 有属性时进行属性值判断
			if(sysAttrLength>0){
				for(var i=0;i<sysAttrLength;i++){
					if($("#productAttributeValue"+i).children("option:selected").val()==""){
						lm.alert("属性值不能为空！");
						$("#productAttributeValue"+i).focus();
						flag = false;
						return;
					}
					attributeCode += $("#productAttributeValue"+i).children("option:selected").val()+'-';
					attributeName += "|"+$("#productAttributeValue"+i).children("option:selected").text();
				}
			}
			
			if(attributeCode!="" ){
				attributeCode=attributeCode.substring( 0,attributeCode.length-1);
				if(attributeCode.indexOf("undefined")==-1){
					$("#attributeCode").val(attributeCode);
					$("#attributeName").val(productName+attributeName);
				}
			}
			var num=0;
			$("input[name='image']").each(function(index,item){
				 var imageFile=$(this).val();
				 if(imageFile!=null && imageFile!=''){
					 var type = imageFile.substring(imageFile.lastIndexOf(".") + 1, imageFile.length).toLowerCase(); 
					 if (type != "jpg" && type != "bmp" && type != "gif" && type != "png") {  
						 lm.alert("请上传正确格式的图片");  
						 flag=false;
						 return false;
					 }else{
						 	num++; 
						}/* else{
						var img = new Image();
						img.src =$(this).next().find("img").attr("src") ;
						var w = img.width;
						var h = img.height;
						if(parseInt(h)<220 || parseInt(w)<330  || parseInt(h)*330 != parseInt(w)*220){
							num=num+1;
							lm.alert("第"+num+"图片比例不对!");  
							 flag=false;
							 return false;
						}else{
						 	num++; 
						}
					 } */
				 }
			});
			num=Number(num)+Number(delImageLength);
			if(num==0){
				lm.alert("至少要上传一张商品图片！");
				return;
			}
			if(num>6){
				lm.alert("至多能上传6商品图片！");
				return;
			}
			if($.trim($("#sort").val())!=null && $.trim($("#sort").val())!=""){
				if( !(/^[0-9]\d*$/.test($.trim($("#sort").val()))) ){
					lm.alert("排序输入错误，排序只能为正整数！");
					return ;
				}
			}
			if($("#id").val() != "" ){ // 修改时
				var barCodeCache = $("#barCodeCache").val();
				var nameCache = $("#nameCache").val();
				var attributeCodeCache = $("#attributeCodeCache").val();
				if( nameCache != productName || barCodeCache != barCode){
					lm.postSync("${contextPath}/product/list/ajax/existBarCode",{name:productName,storeId:storeId,barCode:barCode},function(data){
						if(data == 1){
							lm.alert("此条形码已存在！");
							flag = false;
							return ;				
						}
					});	
				}
				
				if(!flag){
					return;
				}
				if( nameCache != productName || attributeCode != attributeCodeCache ){
					lm.postSync("${contextPath}/product/list/ajax/existProductStock",{id:id,name:productName,storeId:storeId,attributeCode:attributeCode},function(data){
						if(data == 1){
							lm.alert("已存在此属性的商品库存！");
							flag = false;
							return ;				
						}
					});	
				}
			} else { // 新增时
				lm.postSync("${contextPath}/product/list/ajax/existBarCode",{name:productName,storeId:storeId,barCode:barCode},function(data){
					if(data == 1){
						lm.alert("此条形码已存在！");
						flag = false;
						return ;				
					}
				});	
				if(!flag){
					return;
				}
				
				lm.postSync("${contextPath}/product/list/ajax/existProductStock",{id:id,name:productName,storeId:storeId,attributeCode:attributeCode},function(data){
					if(data == 1){
						lm.alert("已存在此属性的商品库存！");
						flag = false;
						return ;				
					}
				});	
			}
			if(!flag){
				return;
			}
			 
			if(flag){
				$("#productStockAddBtn").prop("disabled",true);
			 	$("#productStockSave").submit();
			}
	 });
});
function getSysCategoryId(categoryId){
	var arr;
    // 得到属性值连接字段
    var attributeCode = $("#attributeCode").val();
    if(attributeCode!=""){
    	arr = attributeCode.split("-");
    }
    $("#addAdminCategory").find("div[class='form-group c']").remove();
    if(categoryId == ""){
       return;
    }else{
	    	//根据商品分类ID获取商品属性
	        lm.post("${contextPath}/product/list/ajax/productAttributeList",{categoryId:categoryId},function(data){
	        	sysAttrLength = data.length; // 赋值查询到的属性字段总个数
	        	
	        	for(var i=0;i<data.length;i++){ 
	        		//加属性列表
	        		$("#addAdminCategory").append("<div class='form-group c'>"+
	    				"<label class='col-md-1 control-label'><span style='color: red;font-size: 15px;'>*</span>"+data[i].name+"</label>"+
	    				"<div class='col-md-1'>"+
	    					"<select name='productAttributeValue"+i+"' id='productAttributeValue"+i+"' class='form-control' style='width: auto;'>"+
	    							"<option value=''>选择商品属性值</option>"+
	    					"</select>"+
	    				"</div>"+
	    				"</div>");
	        		// 加载属性列表的属性值
	        		lm.postSync("${contextPath}/productAttributeValue/list/ajax/list-by-productAttributeId",{productAttributeId:data[i].id},function(data1){
	        			for(var j=0;j<data1.length;j++){
	        				$("#addAdminCategory").find("#productAttributeValue"+i).append('<option id="' + data1[j].id + '" value="' + data1[j].id + '">' + data1[j].value + '</option>');
	        				// 属性值连接字段不为空，则匹配属性值使其被选中
	        				if(attributeCode!=""){
	        					for(var k=0;k<arr.length;k++){
	        						$("#addAdminCategory").find("#productAttributeValue"+i).find("option[value='"+arr[k]+"']").attr("selected",true);
	        					}
	        				}
	        			}
	        		}); 
	    		}
	        });
    }
}

var addflag=0;
function imageAdd(){
	addflag=addflag+1;
	$("#imageAddDiv").append('<div class="form-group ">'+
	'<label class="col-md-1 control-label">商品图片(330*220)</label>'+
	'<div class="col-md-2" >'+
		'<input type="button" id="upLogo'+addflag+'" name="upLogo'+addflag+'" value="上传图片(推荐330*220)"/>'+
		'<input type="file" id="image'+addflag+'" name="image" class="file"  value="上传图片" title="上传图片"/>'+
		'<div id="logoImagediv'+addflag+'">'+
			'<img alt="商家LOGO" style="cursor: pointer;" id="logoImage'+addflag+'" name="logoImage'+addflag+'" width="330px" height="220px">'+
		'</div>'+
	'</div>'+
	'<input type="button" class="btn btn-small btn-warning"   value="删除" onclick="imageDel(this);" />'+ 
'</div>');
	new uploadPreview({
		UpBtn : "image"+addflag,
		DivShow : "logoImagediv"+addflag,
		ImgShow : "logoImage"+addflag
	});
	$("#upLogo"+addflag).bind("click", function () {
		   $("#image"+addflag).click();
	  });
}
function imageDel(divName){
	addflag=addflag-1;
	$(divName).parent().remove();
}
function imageDel1(divName){
	delImageLength=delImageLength-1;
	$(divName).parent().remove();
}
function productListDataIdCallback(){
	$("#productListDataId").find("tbody tr").click(function(){
		$("#productId").val($(this).attr("val"));
		$("#productName").val(($($(this).find("td")[0]).html()));
		var type=$($(this).find("td")[3]).html();
		var categoryStoreId=$($(this).find("td")[4]).html();
		var categoryId=$($(this).find("td")[5]).html();
		var categoryName=$($(this).find("td")[6]).html();
		
		if(type=="有"){
			$("#addAdminCategory").show();
			$("#sysCategoryId option").remove();
			$("#sysCategoryId").append('<option  value="' + categoryId + '">' + categoryName + '</option>');
			$("#sysCategoryId").attr("disabled",true);
			getSysCategoryId(categoryId);
		}else{
			$("#addAdminCategory").show();
			$("#sysCategoryId option").remove();
			$("#sysCategoryId").append('<option  value="' + categoryId + '">' + categoryName + '</option>');
			$("#sysCategoryId").attr("disabled",true);
			$("#addAdminCategory").find("div[class='form-group c']").remove();
			//getSysCategoryId(categoryId);
		}
		$("#productListModalBtn").click();
	});
	
}
/* function imageLoad(obj){
	var img = new Image();
	img.src =$(obj).attr("src") ;
	var w = img.width;
	var h = img.height;
	if(h < 220 || w < 330  || h*330 != w*220){
		obj.src="";
		lm.alert("图片比例不对!");
		if(addflag==0){
			$("#logoImagediv").hide();
			$("#image").val("");
		}else{
			addflag=addflag-1;
			$(obj).parent().parent().parent().remove();
		}
	} 
} */
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>${empty productStock ? '添加' : '修改' }库存
			</strong>
		</div>
		<div class='panel-body'>
			<form method='post'  id="productStockSave" action="${contextPath}/product/save" enctype="multipart/form-data">
				<div  class='form-horizontal' id="product">
					<!-- 修改时传过来的库存ID -->
					<input id="id" name="id" type="hidden" value="${productStock.id }" />
					<input id="pageNo" name="pageNo" type="hidden" value="${pageNo }" />	 
					<!-- 修改时传过来的库存对象的属性值连接字段 --> 
					<input id="attributeCode" name="attributeCode" type="hidden" value="${productStock.attributeCode }" />
					<input id="attributeName" name="attributeName" type="hidden" value="${productStock.attributeName }" />
					
					<input id="attributeCodeCache" name="attributeCodeCache" type="hidden" value="${productStock.attributeCode }" />
					
					<input id="nameCache" name="nameCache" type="hidden" value="${product.name }" />
					
					<input id="barCodeCache" name="barCodeCache" type="hidden" value="${productStock.barCode }" />
					
					
					<div  id="storeDiv" style="display: none;">
						<div class="form-group" >
						   <c:if test="${isSys==true }">  
						   		<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商家</label>
								<div class="col-md-2">
									<input name="storeName" id="storeName" readonly="readonly" value="${store.name }" class="form-control" isRequired="1" tipName="商家" data-remote="${contextPath }/product/shopList/list" data-toggle="modal" />
									<input type="hidden" name="storeId" id="storeId" value="${productStock.storeId }" />
								</div>
						   </c:if>  
						</div>
					</div>
					<c:if test="${empty productStock}">
						<div class="form-group" >
							<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商品名称</label>
							<div class="col-md-2">
								<input type="text" id="productName" name="name" readonly="readonly" value="${product.name }" class='form-control' isRequired="1" />
								<input type="hidden" name="productId" id="productId" value="${productStock.productId }" />
							</div>
						</div>
					</c:if>
					<c:if test="${not empty productStock}">
						<div class="form-group" >
							<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商品名称</label>
							<div class="col-md-2">
								<input type="text" id="productName" name="productName" readonly="readonly" value="${product.name }" class='form-control' isRequired="1" />
								
							</div>
						</div>
					</c:if>
					
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商品条码</label>
						<div class="col-md-2">
							<input type='text' id="barCode" name="barCode" value="${productStock.barCode }" class='form-control' maxlength="15"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label">商品简介</label>
						<div class="col-md-2">
							<textarea id='remarks' name='remarks' value='${productStock.remarks }' alt='最大输入字数为150个字符' cols=15 rows=5 class='form-control' maxlength='150'>${productStock.remarks }</textarea>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label">是否上架</label>
						<div class="col-md-2" style="margin-top: 3px;">
							<input id="account_type_0"  type='radio' value="0" name="shelves" checked="checked"/>是
							<input id="account_type_1"  type='radio' value="1" name="shelves"/>否
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>销售单价</label>
						<div class="col-md-2">
							<input type='text' id="price" name="price" class='form-control' value="<fmt:formatNumber value="${productStock.price }" type="currency" pattern="0.00"/>" maxlength="7"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label">会员价</label>
						<div class="col-md-2">
							<input type='text' id="memberPrice" name="memberPrice" class='form-control' value="<fmt:formatNumber value="${productStock.memberPrice }" type="currency" pattern="0.00"/>" maxlength="7"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>市场价格</label>
						<div class="col-md-2">
							<input type='text' id="marketPrice" name="marketPrice" class='form-control' value="<fmt:formatNumber value="${productStock.marketPrice }" type="currency" pattern="0.00"/>" maxlength="7"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>成本单价</label>
						<div class="col-md-2">
							<input type='text' id="costPrice" name="costPrice" class='form-control' value="<fmt:formatNumber value="${productStock.costPrice }" type="currency" pattern="0.00"/>" maxlength="7"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>库存数量</label>
						<div class="col-md-2">
							<!-- 因为库存的后台字段为Integer，而它支持的最大值为2147483647，故将输入长度限制为9位，同理。上面的价格是Double类型的。故将输入长度限制为7位  2015-05-27 -->
							<input type='text' id="stock" placeholder="无限" name="stock" value="${productStock.stock == -99 ? '':productStock.stock }" class='form-control' maxlength="9"/>
						</div>
						<label class="col-md-0 control-label" style="color: gray;font-size: 10px" > 库存默认为无限</label>
					</div>
					
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>缺货提醒</label>
						<div class="col-md-2">
							<input type='text' id="alarmValue" name="alarmValue" value="${productStock.alarmValue }" class='form-control' maxlength="9"/>
						</div>
					</div>
					<div id="imageAddDiv">
						<div class="form-group ">
							<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商品图片(330*220)</label>
							<div class="col-md-2" >
								<input type="button" id="upLogo" name="upLogo" value="上传图片(推荐330*220)"/>
								<input type="file" id="image" name="image" class="file"  value="上传图片" title="上传图片(推荐使用330*220)"/>
								<div id="logoImagediv">
									<img alt="商品图片" style="cursor: pointer;" id="logoImage" name="logoImage" width="330px;" height="220px;"  src="" >
								</div>
							</div>
							<input type="button"  id="imageAddBtn" class="btn btn-small btn-warning"  value="添加图片" onclick="imageAdd();" /> 
						</div>
					</div>
					<div>
						<c:forEach items="${cerUrlMap}" var="cer">
								<div class="form-group ">
									<label class="col-md-1 control-label">商品图片</label>
									<div class="col-md-2">
										<img alt="商品图片" style="cursor: pointer;" id="logoImage" name="logoImage" width="330" height="220" src="${picUrl }${cer.key}"/>
										<input type="hidden"  name="delImage" value="${cer.key}"/>
									</div>
										<input type="button" style="margin-top: 100px;" class="btn btn-small btn-warning"  value="删除" onclick="imageDel1(this);" />
									
								</div>
							</c:forEach>
					</div>
					
						
					<div id="addAdminCategory" style="display: none;">
						<div class="form-group">
							<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>所属分类</label>
							<div class="col-md-2">
								<select  class="form-control"  id="sysCategoryId">
											
								</select>
							</div>
						</div>
					</div>
					<div id="" style="display: none;">
						<m:selectProductCategory onStoreChange="shopChange" isSys="false" inputName="storeCategoryId" path=""  onchageCallback=""></m:selectProductCategory>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label">排序</label>
						<div class="col-md-2">
							<input type='text' id="sort" name="sort" class='form-control' value="${productStock.sort }" maxlength="7"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label">单位名称</label>
						<div class="col-md-2">
							<input type='text' id="unitName" name="unitName" class='form-control' value="${productStock.unitName }" maxlength="80"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label">商品详情</label>
						<div class="col-md-2">
							<m:editor textAreaName="textArea" getEditorContent="getEditorContent" content="${productStock.details}"></m:editor>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-2">
							<input type="button" id="productStockAddBtn" class="btn btn-primary"  value="${empty productStock ? '添加' : '修改' }"/>
							<a href="javascript:history.go(-1);" class="btn btn-small btn-warning">返回</a>
						</div>
					</div>
					
					
			 </div>
		 </form>
	  </div>
   </div>
   
<!-- 模态窗 -->
	<div class="modal fade" id="productNameAddModal">
		<div class="modal-dialog modal-lg" style="width: 1200px;">
		  <div class="modal-content">
			    <div class="modal-header">
			      <button type="button" class="close" data-dismiss="modal" id="productListModalBtn"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
			      <h4 class="modal-title"></h4>
			    </div>
				<div class="modal-body">
					<m:list title="商品列表" id="productStockList" listUrl="${contextPath }/productStock/productList/list-data" callback="productListDataIdCallback"  searchButtonId="productStockList_search_btn" >
						<div class="input-group" style="max-width: 1500px;">
							 <input type="hidden" id="storeModelId" name="storeModelId" value=""/> 
							 <span class="input-group-addon">商品名称</span> 
			            	 <input type="text" id="name" name="name" class="form-control" placeholder="商品名称" style="width: 200px;">
						</div>
					</m:list>
				</div>
			</div>
		</div>
	</div>
	<!-- 模态窗 -->
</body>
</html>