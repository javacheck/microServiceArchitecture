<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty product ? '新增' : '修改' }商品</title> 
	<!-- 增加减少控件 -->
	<script type="text/javascript" src="${staticPath }/js/stepper/jquery.fs.stepper.js"></script>
	<!-- 增加减少样式 -->
	<link rel="stylesheet" href="${staticPath }/css/stepper/jquery.fs.stepper.css" type="text/css">
	<!-- ztree.core核心包 -->
	<script type="text/javascript" src="${staticPath }/js/ztree/jquery.ztree.core-3.5.js"></script>
	<!-- 升级树控件 -->
	<script type="text/javascript" src="${staticPath }/js/ztree/jquery.ztree.exhide-3.5.js"></script>
	<!-- 树形样式 -->
	<link rel="stylesheet" href="${staticPath }/css/ztree/zTreeStyle/zTreeStyle.css" type="text/css">
	<!-- 图片上传预览 -->
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
.bg-c{
	background-color: #ECECEC;
}
.text-c{
    text-align: center;
}
.text-label{
	margin-right:10px;
}
.text-td{
	line-height: 33px;
}
.text-input-fl{
	float:left;
	width: 40%;
	width:18px;
	height:18px;
	
}
input[type=checkbox] {
    margin: 8px 0 0;
}
.text-input- {
	margin-right:8px;
	float:left;
	display: block;
	width: 40%;
	height: 32px;
	padding: 5px 8px;
	font-size: 13px;
	line-height: 1.53846154;
	color: #222;
	vertical-align: middle;
	background-color: #fff;
	border: 1px solid #ccc;
	border-radius: 4px;
	-webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
	box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
	-webkit-transition: border-color ease-in-out .15s, 
	
	-webkit-box-shadow
		ease-in-out .15s;
	-o-transition: border-color ease-in-out .15s, box-shadow ease-in-out
		.15s;
	transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s


}
<%-- //======begin 新图片上传，syk:2016/10/13================================================================================== -->
#MyImageChange {
	display:none;
	position: fixed;
	top: 2%;
	left: 2%;
	width: 96%;
	height: 96%;
	padding: 16px;
	border: 10px solid blue;
	background-color: white;
	z-index: 9999;
	overflow: auto;
}

#clipArea {
	margin: 0 auto;
	height: 60%;
	width: 60%;
}
#imgTip {
	display:none;
}
#file,
#clipBtn {
	margin: 20px;
}
#view {
	margin: 0 auto;
	width: 330px;
	height: 220px;
}

#selectImg {
	display:none;
}
<-- //======end 新图片上传，syk:2016/10/13================================================================================= --%>
#clipArea {
	margin: 0 auto;
	height: 320px;
	width: 420px;
}
#imgTip {
	display:none;
}
#file {
	display:none;
}
#file,
#clipBtn {
	margin: 20px;
}
#view {
	margin: 0 auto;
	width: 330px;
	height: 220px;
}
#selectImg {
	display:none;
}
</style>
<script type="text/javascript">

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++设置图片上传预览 ---START--- ++++++++++++++++++++++++++++++++++++++++++++++++
$(function(){
	new uploadPreview({
		UpBtn : "pictureFile",
		DivShow : "pictureImagediv",
		ImgShow : "pictureImage"		
	});
	
	$("[name = pictureFile]").bind("change", function () {
		   var fileName = $("#pictureFile").val();
		   if( "" == fileName ){
			   $("#pictureFileCache").val("");
			   $("#pictureImagediv").hide();
		   } else {
			   $("#pictureFileCache").val(fileName);
			   $("#pictureImagediv").show();
		   }
	  });
	<%-- //======begin 新图片上传，syk:2016/10/13================================================================================== --%>
	/*
	$("[name = upPicture]").bind("click", function() {
		
		//原版方法
		//$("[name = pictureFile]").click();
		
		$("#MyImageChange").css("display","block");
		document.body.style.overflow="hidden";
		$("#pictureImage").show();
	});
	*/
	$("#originalPicture").click(function(){
		var picUrl = '${product.picUrl}';
		$("#pictureFileCache").val("original");
		$("#myImgUpload").attr("value","original");
		if (undefined != picUrl && null != picUrl && "" != picUrl) {
			$("#pictureImage").attr("src", picUrl);
			$("#pictureImage").show();
		} else {
			$("#pictureImage").attr("src", "");
			$("#pictureImage").hide();
		}
	});
	
	$("#delPicture").click(function(){
		$("#pictureImage").attr("src", "");
		$("#pictureImage").hide();
		$("#pictureFileCache").val("del");
		$("#myImgUpload").attr("value","del");
	});
	
	<%-- //======end 新图片上传，syk:2016/10/13================================================================================== --%>
});
function imageLoad(obj){
	$("#pictureImagediv").show();
	$("#divPicture").removeClass("col-md-2");
	
	if(obj.offsetHeight > 221 || obj.offsetWidth > 331 ){
		obj.src="";
		lm.alert("图片规格只能是330x220像素");
		$("#pictureImagediv").hide();
		$("#pictureFileCache").val("");
	} 
	$("#divPicture").addClass("col-md-2");
}
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++设置图片上传预览 ---END--- ++++++++++++++++++++++++++++++++++++++++++++++++

$(document).ready(function(){
	// 保存商品库存信息
	$("#commodityManagerAddBtn").click(function(){
		var productName = $("#name").val(); // 
		productName = $.trim(productName);
		
		if( productName == "" || productName == null ){
			lm.alert("商品名称不能为空!");
			return;
		}
		
		var storeId = $("#storeId").val();
		storeId = $.trim(storeId);
		var id = $("#id").val();
		id = $.trim(id);
		var nameFlag = false;
		lm.postSync("${contextPath}/commodityManager/list/ajax/existProductName",{name:productName,storeId:storeId,id:id},function(data){
			if(data >= 1){
				lm.alert("此商品名称已存在！");
				nameFlag = true;
				return ;				
			}
		});
		if(nameFlag){
			return;
		}
		
		var categoryId = $("#categoryId").val();
		categoryId = $.trim(categoryId);
		if( null == categoryId || "" == categoryId ){
			lm.alert("商品分类不能为空!");
			return;
		}
		
		//规格属性重复校验
		var productTr = $("#productStock_Table").find("tr:not([id='trHearder'])")
		var attrValArray = new Array();
		productTr.each(function(k,v){
			var attrVal = "";
			$(v).find("input[name='value']").each(function(key,value){
				attrVal += $.trim($(value).val());
			});
			attrValArray.push(attrVal);
		})
		
		for (var i = 0; i < attrValArray.length - 1; i++){
			var val1 = attrValArray[i];
			val1 = $.trim(val1);
			for (var j = i + 1; j < attrValArray.length; j++){
				var val2 = attrValArray[j];
				val2 = $.trim(val2);
				if (val1 == val2){
					lm.alert("商品规格不能重复");
					return;
				}
			}
		}
		
		var sort = $("#sort").val();
		sort = ($.trim(sort) == "" ? 0 : $.trim(sort) );
		if( null != sort && "" != sort ){
			if( !(/^[0-9]\d*$/.test(sort)) ){
				lm.alert("商品排序输入错误，商品排序只能为正整数！");
				return ;
			}
		}
		var weighing = $("input[name='weighing']:checked").val();
		var returnGoods = $("input[name='returnGoods']:checked").val();
		var remarks = $("#remarks").val();
		var zuping = "";
		$("input[name='shelves']").each(function(key,value){
			 if($(this).is(':checked')){
				 zuping += $(this).val();
			 }
		});
		var shelves;
		if( zuping == "" ){ // 两个都没选中
			shelves = 5;
		} else if( zuping.length == 1 ){
			shelves = zuping;
		} else {
			shelves = 4;
		}
		
		var count = 0;
		var value1Arr = new Array();
		var value2Arr = new Array();
		$("#productStock_Table tr").each(function(key,value) {
			if( key == 0 ){
				return;
			}
			count++;
			for( var z=0;z<2;z++){
				var tdInput = $($(this).children()[z]).children();
				var inputValue = tdInput.val();
				inputValue = $.trim(inputValue);
				if( tdInput.attr("id") == "value_1" ){
					if( null != inputValue  && "" != inputValue ){
						value1Arr.push(inputValue);
					}
				} else if( tdInput.attr("id") == "value_2" ){
					if( null != inputValue  && "" != inputValue ){
						value2Arr.push(inputValue);
					}
				}
			}
		});
		
		if( count != value1Arr.length && value1Arr.length  != 0 ){
			lm.alert($("#attribute_1 option:selected").text() + "必须都填写或者都不填写!");
			return;
		}
		
		if( count != value2Arr.length && value2Arr.length  != 0 ){
			lm.alert($("#attribute_2 option:selected").text() +"必须都填写或者都不填写!");
			return;
		}
		
		if( count > 1 && value1Arr.length == 0 && value2Arr.length == 0){
			lm.alert("无规格的商品不能拥有多种规格");
			return;
		}

		var list= new Array();
		var successFlag = true;
		$("#productStock_Table tr").each(function(key,value) {
			if( key == 0 ){ // 去掉第一行标头
				return;
			}
			var len = $(this).children().length;
				var obj =new Object();
				obj.categoryId = categoryId;
				obj.weighing = weighing;
				obj.returnGoods = returnGoods;
				obj.sort = sort;
				obj.shelves = shelves;
				obj.remarks = remarks;
				
				for( var z=0;z<len;z++){
					var tdInput = $($(this).children()[z]).children();
					var inputValue = tdInput.val();
					inputValue = $.trim(inputValue);
					if( tdInput.attr("id") == "value_1" ){
						obj.value_1 = inputValue;
					} else if( tdInput.attr("id") == "value_2" ){
						obj.value_2 = inputValue;						
					} else if( tdInput.attr("id") == "unitId" ){
						obj.unitId = inputValue;						
					} else if( tdInput.attr("id") == "barCode" ){
						if( null == inputValue || "" == inputValue ){
							lm.alert("商品条码不能为空!");
							successFlag = false;
							return;
						}
						if( !(/^[0-9]\d*$/.test(inputValue)) ){
							lm.alert("条码输入错误，条形码只能为正整数！");
							successFlag = false;
							return ;
						}
						
						if( inputValue.length > 17){
							lm.alert("条形码最大只能17位!");
							successFlag = false;
							return ;
						}
						
							var barcodeFlag = false;
							lm.postSync("${contextPath}/commodityManager/list/ajax/existBarCode",{name:productName,storeId:storeId,barCode:inputValue,id:id},function(data){
								if(data >= 1){
									lm.alert("此条形码已存在！");
									barcodeFlag = true;
									return ;				
								}
							});
							if(barcodeFlag){
								successFlag = false;
								return;
							}
						
						obj.id = $(tdInput[1]).val();
						obj.barCode = inputValue;
					} else if( tdInput.attr("id") == "price" ){
						if( null == inputValue || "" == inputValue ){
							lm.alert("销售价不能为空!");
							successFlag = false;
							return;
						}
						if( !(lm.isFloat(inputValue) && parseFloat(inputValue) >= 0) ){
							lm.alert("销售价格式输入错误");
							successFlag = false;
					  		return ;
						}
						if( !(lm.isTwoPointFloat(inputValue)) ){
					  		lm.alert("销售价格式输入错误");
					  		successFlag = false;
					  		return ;
					  	}
						if( -1 == inputValue.lastIndexOf(".")){
							if( inputValue.length > 7){
								lm.alert("销售价最大只支持整数位为7位!");
								successFlag = false;
								return ;
							}
						} else {
							if( inputValue.toString().split(".")[1].length > 1 ){
								if( inputValue.length > 10){
									lm.alert("销售价最大只支持整数位为7位!");
									successFlag = false;
									return ;
								}

							} else {
								if( inputValue.length > 9){
									lm.alert("销售价最大只支持整数位为7位!");
									successFlag = false;
									return ;
								}

							}
						}
						
						obj.price = inputValue;
					} else if( tdInput.attr("id") == "costPrice" ){
						if( null == inputValue || "" == inputValue ){
							lm.alert("进货价不能为空!");
							return;
						}
						if( !(lm.isFloat(inputValue) && inputValue >= 0) ){
							lm.alert("进货价格式输入错误");
							successFlag = false;
					  		return ;
						}
						if( !(lm.isTwoPointFloat(inputValue)) ){
					  		lm.alert("进货价格式输入错误");
					  		successFlag = false;
					  		return ;
					  	}
						
						if( -1 == inputValue.lastIndexOf(".")){
							if( inputValue.length > 7){
								lm.alert("进货价最大只支持整数位为7位!");
								successFlag = false;
								return ;
							}
						} else {
							if( inputValue.toString().split(".")[1].length > 1 ){
								if( inputValue.length > 10){
									lm.alert("进货价最大只支持整数位为7位!");
									successFlag = false;
									return ;
								}

							} else {
								if( inputValue.length > 9){
									lm.alert("进货价最大只支持整数位为7位!");
									successFlag = false;
									return ;
								}

							}
						}
						
						obj.costPrice = inputValue;
					} else if( tdInput.attr("id") == "marketPrice" ){
						if( null == inputValue || "" == inputValue ){
							lm.alert("市场价不能为空!");
							successFlag = false;
							return;
						}
						if( !(lm.isFloat(inputValue) && inputValue >= 0) ){
							lm.alert("市场价格式输入错误");
							successFlag = false;
					  		return ;
						}
						if( !(lm.isTwoPointFloat(inputValue)) ){
					  		lm.alert("市场价只支持两位小数");
					  		successFlag = false;
					  		return ;
					  	}
						
						if( -1 == inputValue.lastIndexOf(".")){
							if( inputValue.length > 7){
								lm.alert("市场价最大只支持整数位为7位!");
								successFlag = false;
								return ;
							}
						} else {
							if( inputValue.toString().split(".")[1].length > 1 ){
								if( inputValue.length > 10){
									lm.alert("市场价最大只支持整数位为7位!");
									successFlag = false;
									return ;
								}

							} else {
								if( inputValue.length > 9){
									lm.alert("市场价最大只支持整数位为7位!");
									successFlag = false;
									return ;
								}

							}
						}
						
						obj.marketPrice = inputValue;
					} else if( tdInput.attr("id") == "memberPrice" ){
						inputValue = (inputValue == "" ? "" : inputValue);
						
						if( null != inputValue && "" != inputValue ){
							if( !(lm.isFloat(inputValue) && parseFloat(inputValue) >= 0) ){
								lm.alert("会员价格式输入错误");
								successFlag = false;
						  		return ;
							}
							if( !(lm.isTwoPointFloat(inputValue)) ){
						  		lm.alert("会员价格式输入错误");
						  		successFlag = false;
						  		return ;
						  	}
							
							if( -1 == inputValue.lastIndexOf(".")){
								if( inputValue.length > 7){
									lm.alert("会员价最大只支持整数位为7位!");
									successFlag = false;
									return ;
								}
							} else {
								if( inputValue.toString().split(".")[1].length > 1 ){
									if( inputValue.length > 10){
										lm.alert("会员价最大只支持整数位为7位!");
										successFlag = false;
										return ;
									}

								} else {
									if( inputValue.length > 9){
										lm.alert("会员价最大只支持整数位为7位!");
										successFlag = false;
										return ;
									}

								}
							}
						}
						
						obj.memberPrice = inputValue;
					} else if( tdInput.attr("id") == "stock" ){
						// 勾选了无限库存
						if($(tdInput[1]).is(':checked')){ 
							obj.stock = $(tdInput[1]).val(); 
						} else {
							inputValue = (inputValue == "" ? "0" : inputValue);
							if( weighing == 1 ){
								if( !(lm.isTwoPointFloat(inputValue)) ){
							  		lm.alert("库存数量只支持两位小数");
							  		successFlag = false;
							  		return ;
							  	}
								if( -1 == inputValue.lastIndexOf(".")){
									if( inputValue.length > 7){
										lm.alert("库存数量最大只支持整数位为7位!");
										successFlag = false;
										return ;
									}
								} else {
									if( inputValue.toString().split(".")[1].length > 1 ){
										if( inputValue.length > 10){
											lm.alert("库存数量最大只支持整数位为7位!");
											successFlag = false;
											return ;
										}

									} else {
										if( inputValue.length > 9){
											lm.alert("库存数量最大只支持整数位为7位!");
											successFlag = false;
											return ;
										}

									}
								}
							} else {
								if( -1 == inputValue.lastIndexOf(".")){
									if( !(/^[0-9]\d*$/.test(inputValue)) ){
										lm.alert("库存数量输入错误，不可称重情况下库存数量只能为正整数！");
										successFlag = false;
										return ;
									}
									if( inputValue.length > 7){
										lm.alert("库存数量最大只能7位!");
										successFlag = false;
										return ;
									}
								} else {
									if( inputValue.toString().split(".")[1].length > 1 ){
										if( !(/^[0-9]\d*$/.test(inputValue)) ){
											lm.alert("库存数量输入错误，不可称重情况下库存数量只能为正整数！");
											successFlag = false;
											return ;
										}
										if( inputValue.length > 7){
											lm.alert("库存数量最大只能7位!");
											successFlag = false;
											return ;
										}
									} else {
										var a = inputValue.toString().split(".")[1];
										if( a != 0 ){
											if( !(/^[0-9]\d*$/.test(inputValue)) ){
												lm.alert("库存数量输入错误，不可称重情况下库存数量只能为正整数！");
												successFlag = false;
												return ;
											}
											if( inputValue.length > 7){
												lm.alert("库存数量最大只能7位!");
												successFlag = false;
												return ;
											}
										}
									}
								}
							}
							obj.stock = inputValue;
						}
					} else if( tdInput.attr("id") == "alarmValue" ){
						inputValue = (inputValue == "" ? "0" : inputValue);
						
						if( weighing == 1 ){
							if( !(lm.isTwoPointFloat(inputValue)) ){
						  		lm.alert("缺货提醒只支持两位小数");
						  		successFlag = false;
						  		return ;
						  	}
							if( -1 == inputValue.lastIndexOf(".")){
								if( inputValue.length > 7){
									lm.alert("缺货提醒最大只支持整数位为7位!");
									successFlag = false;
									return ;
								}
							} else {
								if( inputValue.toString().split(".")[1].length > 1 ){
									if( inputValue.length > 10){
										lm.alert("缺货提醒最大只支持整数位为7位!");
										successFlag = false;
										return ;
									}

								} else {
									if( inputValue.length > 9){
										lm.alert("缺货提醒最大只支持整数位为7位!");
										successFlag = false;
										return ;
									}

								}
							}
						} else {
							if( -1 == inputValue.lastIndexOf(".")){
								if( !(/^[0-9]\d*$/.test(inputValue)) ){
									lm.alert("缺货提醒输入错误，不可称重情况下缺货提醒只能为正整数！");
									successFlag = false;
									return ;
								}
								if( inputValue.length > 7){
									lm.alert("缺货提醒最大只能7位!");
									successFlag = false;
									return ;
								}
							} else {
								if( inputValue.toString().split(".")[1].length > 1 ){
									if( !(/^[0-9]\d*$/.test(inputValue)) ){
										lm.alert("缺货提醒输入错误，不可称重情况下缺货提醒只能为正整数！");
										successFlag = false;
										return ;
									}
									if( inputValue.length > 7){
										lm.alert("缺货提醒最大只能7位!");
										successFlag = false;
										return ;
									}
								} else {
									var a = inputValue.toString().split(".")[1];
									if( a != 0 ){
										if( !(/^[0-9]\d*$/.test(inputValue)) ){
											lm.alert("缺货提醒输入错误，不可称重情况下缺货提醒只能为正整数！");
											successFlag = false;
											return ;
										}
										if( inputValue.length > 7){
											lm.alert("缺货提醒最大只能7位!");
											successFlag = false;
											return ;
										}
									}
								}
							}
						}
						
						obj.alarmValue = inputValue;
					} 
				}				
				list.push(obj);
		});
		
		if( list.length <= 0 ){
			successFlag = false;
		}
		
		if(successFlag){
			$("#productStockJson").val(JSON.stringify(list));
			$("#commodityManagerAddForm").submit();			
		}
	}); 
	
});

//-------------------------------------------------------将中文转换成首字母大写的英文字母 ---START--- --------------------------------------------
$(function(){
	$("#name").blur(function(){
		var name = $(this).val();
	   	name = $.trim(name);
	   	if( null != name && "" != name ){
	   		var cacheName = $("#cacheName").val();
	   		if( name != cacheName){
	   			lm.postSync("${contextPath }/commodityManager/ajax/analysisChineseSrc", {src:name}, function(data) {
					if( null != data && "" != data ){
						$("#cacheName").val(name);
						$("#shortName").val(data.returnSrc);
					}
				});
	   		} 
	   	}
	});
});
//-------------------------------------------------------将中文转换成首字母大写的英文字母 ---END--- --------------------------------------------

//------------------------------------------------------------------------商品规格新增  START----------------------------------------------
$(function(){
	$("#addProductStock").click(function(){
		var productStock_Table = $("#productStock_Table");
		var tr = productStock_Table.find("tr");
		if( (tr.length-1) <= 1 ){
			var firstFlag = false;
			$("#productStock_Table tr").each(function(key,value) {
				if( key == 0 ){
					return;
				}
				for( var z=0;z<2;z++){
					var tdInput = $($(this).children()[z]).children();
					var inputValue = tdInput.val();
					inputValue = $.trim(inputValue);
					if( tdInput.attr("id") == "value_1" ){
						if( null == inputValue  || "" == inputValue ){
							firstFlag = true;
						}
					} else if( tdInput.attr("id") == "value_2" ){
						if( null != inputValue  && "" != inputValue ){
							if(firstFlag){
								firstFlag = false;
							}
						} else {
							if(firstFlag){
								firstFlag = true;
							}
						}
					}
				}
			});
			if(firstFlag){
				lm.alert("无规格的商品不能新增更多规格！");
				return;
			}
		}

		var html = "<tr class='bg-c'>"+
		"<td><input name='value' id='value_1' value='' maxlength='50' class='form-control'/></td>"+
		"<td><input name='value' id='value_2' value='' maxlength='50' class='form-control'/></td>"+
		"<td>"+
			// 单位 
			"<select class='form-control' id='unitId' name='unitId' style='width: auto'>"+
				"<option value=''>无</option>"+ 
				"<c:forEach items='${unitList }' var='unit'>"+
					"<option value='${unit.id}'>${unit.name}</option>"+
				"</c:forEach>"+
			"</select>"+
		"</td>"+
		"<td>"+
			// 商品条码  
			"<input name='barCode' id='barCode' value='${productStock.barCode }' maxlength='17' class='form-control'/>"+
			"<input type='hidden' name='productStockId' id='productStockId' value='-90000' class='form-control'/>"+
		"</td>"+
		"<td>"+
			// 销售价 
			"<input type='text' name='price' class='form-control' id='price' value=''>"+
		"</td>"+
		"<td>"+
			// 进货价   
			"<input type='text' name='costPrice' class='form-control' id='costPrice' value='' >"+
		"</td>"+
		"<td>"+
			// 市场价  
			"<input type='text' name='marketPrice' class='form-control' id='marketPrice' value=''>"+
		"</td>"+
		"<td>"+
			// 会员价  
			"<input type='text' name='memberPrice' class='form-control' id='memberPrice' value=''>"+
		"</td>"+
		"<td class='text-td'>"+
			// 库存数量   
			"<input type='text' name='stock' placeholder='不填默认为0' class='text-input-' id='stock' value='0'>"+
			"<input type='checkbox' onclick='operationStock(this);' name='infinite' id='infinite' value='-99' class='form-control text-input-fl'/>设为无限库存"+
		"</td>"+
		"<td>"+
			// 缺货提醒 
			"<input type='text' name='alarmValue' class='form-control' id='alarmValue' value='0' >"+
		"</td>"+
		"<td colspan='2'><input type='button' onclick='deleteProductStockFunction(this)' id='deleteProductStock' name='deleteProductStock' value='删除'/></td>"+
	"</tr>";
	
		productStock_Table.append(html);
	});
	
	
	
	
	$("#copyAndAddProductStock").click(function(){
		
		var productStock_Table = $("#productStock_Table");
		var tr = productStock_Table.find("tr");
		if( (tr.length-1) <= 1 ){
			var firstFlag = false;
			$("#productStock_Table tr").each(function(key,value) {
				if( key == 0 ){
					return;
				}
				for( var z=0;z<2;z++){
					var tdInput = $($(this).children()[z]).children();
					var inputValue = tdInput.val();
					inputValue = $.trim(inputValue);
					if( tdInput.attr("id") == "value_1" ){
						if( null == inputValue  || "" == inputValue ){
							firstFlag = true;
						}
					} else if( tdInput.attr("id") == "value_2" ){
						if( null != inputValue  && "" != inputValue ){
							if(firstFlag){
								firstFlag = false;
							}
						} else {
							if(firstFlag){
								firstFlag = true;
							}
						}
					}
				}
			});
			if(firstFlag){
				lm.alert("无规格的商品不能新增更多规格！");
				return;
			}
		}
		
		var obj =new Object();
		$("#productStock_Table tr").each(function(key,value) {
			if( key == 0 ){
				return;
			}
			if( key == 1 ){
				var len = $(this).children().length;
				
				for( var z=0;z<len;z++){
					var tdInput = $($(this).children()[z]).children();
					var inputValue = tdInput.val();
					inputValue = $.trim(inputValue);
					if( tdInput.attr("id") == "value_1" ){
						obj.value_1 = inputValue;
					} else if( tdInput.attr("id") == "value_2" ){
						obj.value_2 = inputValue;
					} else if( tdInput.attr("id") == "unitId" ){
						obj.unitId = inputValue;
					} else if( tdInput.attr("id") == "barCode" ){
						obj.barCode = inputValue;
					} else if( tdInput.attr("id") == "price" ){
						obj.price = inputValue;
					} else if( tdInput.attr("id") == "costPrice" ){
						obj.costPrice = inputValue;
					} else if( tdInput.attr("id") == "marketPrice" ){
						obj.marketPrice = inputValue;
					} else if( tdInput.attr("id") == "memberPrice" ){
						obj.memberPrice = inputValue;
					} else if( tdInput.attr("id") == "stock" ){
						// 勾选了无限库存
						if($(tdInput[1]).is(':checked')){ 
							obj.stock = ""; 
						}  else {
							obj.stock = inputValue;							
						}
					} else if( tdInput.attr("id") == "alarmValue" ){
						obj.alarmValue = inputValue;
					} 
				}
			} else {
				return;
			}
		});
		
		var html = "<tr class='bg-c'>"+
		"<td><input name='value' id='value_1' value='"+obj.value_1+"' maxlength='50' class='form-control'/></td>"+
		"<td><input name='value' id='value_2' value='"+obj.value_2+"' maxlength='50' class='form-control'/></td>"+
		"<td>"+
			// 单位 
			"<select class='form-control' id='unitId' name='unitId' style='width: auto'>"+
				"<option value=''>无</option>"+ 
				"<c:forEach items='${unitList }' var='unit'>"+
					"<option value='${unit.id}'>${unit.name}</option>"+
				"</c:forEach>"+
			"</select>"+
		"</td>"+
		"<td>"+
			// 商品条码  
			"<input name='barCode' id='barCode' value='"+obj.barCode+"' maxlength='17' class='form-control'/>"+
			"<input type='hidden' name='productStockId' id='productStockId' value='-90000' class='form-control'/>"+
		"</td>"+
		"<td>"+
			// 销售价 
			"<input type='text' name='price' class='form-control' id='price' value='"+obj.price+"'>"+
		"</td>"+
		"<td>"+
			// 进货价   
			"<input type='text' name='costPrice' class='form-control' id='costPrice' value='"+obj.costPrice+"'>"+
		"</td>"+
		"<td>"+
			// 市场价  
			"<input type='text' name='marketPrice' class='form-control' id='marketPrice' value='"+obj.marketPrice+"' >"+
		"</td>"+
		"<td>"+
			// 会员价  
			"<input type='text' name='memberPrice' class='form-control' id='memberPrice' value='"+obj.memberPrice+"' >"+
		"</td>"+
		"<td class='text-td'>"+
			// 库存数量   
			"<input type='text' name='stock' placeholder='不填默认为0' class='text-input-' id='stock' value='"+obj.stock+"'>"+
			"<input type='checkbox' onclick='operationStock(this);' name='infinite' id='infinite' value='-99' class='form-control text-input-fl'/>设为无限库存"+
		"</td>"+
		"<td>"+
			// 缺货提醒 
			"<input type='text' name='alarmValue' class='form-control' id='alarmValue' value='"+obj.alarmValue+"'>"+
		"</td>"+
		"<td colspan='2'><input type='button' onclick='deleteProductStockFunction(this)' id='deleteProductStock' name='deleteProductStock' value='删除'/></td>"+
	"</tr>";
		$("#productStock_Table").append(html);
		
		// 给单位赋值
		var unitd = $("#productStock_Table").find("tr:not([id='trHearder']):first").find(".form-control[id='unitId'] option:selected").val();
		$("#productStock_Table").find("tr:not([id='trHearder']):last").find(".form-control[id='unitId']").val(unitd);
		var checkbox = $("#productStock_Table").find("tr:not([id='trHearder']):first").find(".form-control[id='infinite']").is(':checked');
		if(checkbox){
			$("#productStock_Table").find("tr:not([id='trHearder']):last").find(".text-input-[id='stock']").attr("placeholder","无限库存");
			$("#productStock_Table").find("tr:not([id='trHearder']):last").find(".text-input-[id='stock']").attr("disabled","disabled");
			$("#productStock_Table").find("tr:not([id='trHearder']):last").find(".form-control[id='infinite']").attr("checked",true);
		}
	});
	
});

function deleteProductStockFunction(obj){
	var productStockId = $(obj).parent().parent().find("input[name='productStockId']").val();
	if( undefined != productStockId && "-90000" != productStockId && "" != productStockId ){
		lm.confirm("确定要删除此库存吗？",function(){
			var id = $("#id").val();
			lm.post("${contextPath }/commodityManager/ajax/deleteProductStock", {productStockId:productStockId,productId:id}, function(data) {
				if( null != data && "" != data ){
					if( data == 0 ){
						$(obj).parent().parent().remove();						
					} else {
						lm.alert("商品已经销售，不能删除，只能下架处理");						
					}
				}
			});
		});
	} else {
		$(obj).parent().parent().remove();		
	}
}

function operationStock(obj){
	if( $(obj).is(":checked") ){ // 选中
		if( $(obj).prev().attr("id") == "stock" ){
			$(obj).prev().val("");
			$(obj).prev().attr("placeholder","无限库存");
			$(obj).prev().attr("disabled",true);				
		}
	} else {
		if( $(obj).prev().attr("id") == "stock" ){
			$(obj).prev().attr("placeholder","不填默认为0");
			$(obj).prev().attr("disabled",false);				
		}
	}
}
//------------------------------------------------------------------------商品规格新增  END----------------------------------------------


//--------------------------------------------------规格选择 START---------------------------------------------------------
$(function(){
	$("#attribute_1").change(function(){
		var attribute_2 = $("#attribute_2").val();
		var attribute_1 = $(this).val();
		if( attribute_1 == attribute_2){
			$(this).val(1);
		}
	});
	
	$("#attribute_2").change(function(){
		var attribute_1 = $("#attribute_1").val();
		var attribute_2 = $(this).val();
		if( attribute_1 == attribute_2){
			$(this).val(2);
		}
	});
});
//--------------------------------------------------规格选择 END---------------------------------------------------------


$(function(){
	// 修改时判断营业状态
	$("input:radio[name='weighing']").each(function(key,value){
		 if( $("#weighingCache").val() == $(value).val() ){
			 $(value).attr("checked",true);
			 return false;
		 }			
	});
	
	
	
	var attribute_1 = '${product.attribute_1}';
	var attribute_2 = '${product.attribute_2}';
	
	$("#attribute_1").val(attribute_1 == "" ? 1 : attribute_1);
	$("#attribute_2").val(attribute_2 == "" ? 2 : attribute_2);

	$("input:radio[name='returnGoods']").each(function(key,value){
		 if( $("#returnGoodsCache").val() == $(value).val() ){
			 $(value).attr("checked",true);
			 return false;
		 }			
	});

	/*
	var weighing = $("input[name='weighing']:checked").val();
	if( weighing == 0 ){ // 否
		$("input[name='stock']").attr("step","1");
		$("input[name='alarmValue']").attr("step","1");
	} else {
		$("input[name='stock']").attr("step","0.1");			
		$("input[name='alarmValue']").attr("step","0.1");			
	}
	*/
	var brandId = '${product.brandId}';
	$("#brandId").val(brandId);
	
	var shelvesCache = '${product.shelves}';
	if( shelvesCache == 4 ){ // 全部上架
		$("input:checkbox[name='shelves']").each(function(key,value){
			$(value).attr("checked",true);
		});
	} else {
		$("input:checkbox[name='shelves']").each(function(key,value){
			 if( shelvesCache == $(value).val() ){
				 $(value).attr("checked",true);
				 return false;
			 }			
		});
	}
	
	var picUrl = '${product.picUrl}';
	if( undefined != picUrl && null != picUrl && "" != picUrl ){
		$("#pictureImagediv").show();
		$("#pictureImage").attr("src",picUrl);
	}
	
});

//*******************************************************将输入框设置为增加减少的数字框 ---START--- ********************************************
$(function(){
	/*
	$("#price").stepper(); // 销售价
	$("#costPrice").stepper(); // 进货价
	$("#marketPrice").stepper(); // 市场价
	$("#memberPrice").stepper(); // 会员价
	$("#stock").stepper(); // 库存数量
	$("#alarmValue").stepper(); // 缺货提醒
	$("#sort").stepper(); // 排序
	*/
	/*
	$("input[name='weighing']").change(function(){
		var weighing = $(this).val();
		if( weighing == 0 ){ // 否
			$("input[name='stock']").attr("step","1");
			$("input[name='alarmValue']").attr("step","1");
		} else {
			$("input[name='stock']").attr("step","0.1");			
			$("input[name='alarmValue']").attr("step","0.1");			
		}
	});
	*/
});
//*******************************************************将输入框设置为增加减少的数字框 ---END--- ********************************************

</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>${empty product ? '新增' : '修改' }商品
			</strong>
		</div>
		<div class='panel-body'>
			<form id="commodityManagerAddForm" method='post' repeatSubmit='1' autocomplete="off" enctype="multipart/form-data" class='form-horizontal' action="${contextPath }/commodityManager/save">
				<input type="hidden" name="id" class='form-control' id="id"  value="${product.id }" maxlength="100"  >
				<input type="hidden" name="productStockJson" class='form-control' id="productStockJson"  value="" >
				<input type="hidden" name="weighingCache" class='form-control' id="weighingCache"  value="${product.weighing }" >
				<input type="hidden" name="returnGoodsCache" class='form-control' id="returnGoodsCache"  value="${product.returnGoods }" >
				<input type="hidden" name="storeId" class='form-control' id="storeId"  value="${storeId }" >
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>&nbsp;&nbsp;商品名称</label>
					<div class="col-md-2">
						<input type="text" name="name" class='form-control' id="name" value="${product.name }" maxlength="48"  >
						<input type="hidden" name="cacheName" class='form-control' id="cacheName" value="${product.name }" maxlength="48"  >
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">拼音码</label>
					<div class="col-md-2">
						<input name="shortName" id="shortName"  readonly="readonly" value="${product.shortName }" class="form-control"/>
					</div>
					<label class="col-md-0 control-label" style="color: gray;font-size: 15px" >拼音码为商品名称拼音简写（如：莱麦面包拼音码： LMMB）</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>&nbsp;&nbsp;商品分类</label>
					<div class="col-md-2">
						<m:treeSelect treeRoot="updateTreeRoot" selectID="categoryId" showId="${product.categoryId }" showValue="${product.categoryName }"></m:treeSelect>
					</div>
				</div>
				
				<div class="form-group">								
				<label class="col-md-1 control-label text-label"><span style="color: red;font-size: 15px">*</span>&nbsp;&nbsp;商品规格</label>
				<table border="1" id="productStock_Table" class="text-c col-md-10">
					<tr class="bg-c" id="trHearder">
						<td width="10%">
							<select class="form-control" name="attribute_1" id="attribute_1" style="width: 120px"> 
								<c:forEach items="${attributeList }" var="attribute">
									<c:if test="${attribute.id == product.attribute_1 }">
										<option value="${attribute.id }" selected="selected">${attribute.name }</option>									
									</c:if>
									<c:if test="${attribute.id != product.attribute_1 }">
										<option value="${attribute.id }">${attribute.name }</option>									
									</c:if>
								</c:forEach>
							</select> 
						</td>
						<td width="10%">
							<select class="form-control" name="attribute_2" id="attribute_2" style="width: 120px"> 
								<c:forEach items="${attributeList }" var="attribute">
									<c:if test="${attribute.id == product.attribute_2 }">
										<option value="${attribute.id }" selected="selected">${attribute.name }</option>									
									</c:if>
									<c:if test="${attribute.id != product.attribute_2 }">
										<option value="${attribute.id }">${attribute.name }</option>									
									</c:if>
								</c:forEach>
							</select> 
						</td>
						<td>单位</td>
						<td width="11%"><span style="color: red;font-size: 15px">*</span>&nbsp;&nbsp;商品条码</td>
						<td><span style="color: red;font-size: 15px">*</span>&nbsp;&nbsp;销售价</td>
						<td><span style="color: red;font-size: 15px">*</span>&nbsp;&nbsp;进货价</td>
						<td><span style="color: red;font-size: 15px">*</span>&nbsp;&nbsp;市场价</td>
						<td>会员价</td>
						<td width="20%">库存数量</td>
						<td>缺货提醒</td>
						<td colspan="2"></td>
					</tr>
					
					<c:if test="${not empty productStockList }">
						<c:forEach items="${productStockList }" varStatus="sortNo"  var="productStock">
							<c:set var="v" value="${ sortNo.index+1}"></c:set>
							<tr class="bg-c">
								<td><input name="value" id="value_1" value="${productStock.value_1 }" maxlength='50' class="form-control"/></td>
								<td><input name="value" id="value_2" value="${productStock.value_2 }" maxlength='50' class="form-control"/></td>
								<td>
									<!-- 单位  -->
									<select class="form-control" id="unitId" name="unitId" style="width: auto">
										<option value="">无</option> 
										<c:forEach items="${unitList }" var="unit">
											<c:if test="${productStock.unitId == unit.id }">
												<option value="${unit.id }" selected="selected">${unit.name }</option>	
											</c:if>
											<c:if test="${productStock.unitId != unit.id }">
												<option value="${unit.id }">${unit.name }</option>
											</c:if>
										</c:forEach>
									</select>
								</td>
								<td>
									<!-- 商品条码  -->
									<input name="barCode" id="barCode" value="${productStock.barCode }" maxlength='17' class="form-control"/>
									<input type="hidden" name="productStockId" id="productStockId" value="${productStock.id }" class="form-control"/>
								</td>
								<td>
									<!-- 销售价  -->
									<input type="text" name="price" class='form-control' id="price" value="${productStock.price }" >
								</td>
								<td>
									<!-- 进货价  -->
									<input type="text" name="costPrice" readonly="readonly" class='form-control' id="costPrice" value="${productStock.costPrice }" >
								</td>
								<td>
									<!-- 市场价  -->
									<input type="text" name="marketPrice" class='form-control' id="marketPrice" value="${productStock.marketPrice }" >
								</td>
								<td>
									<!-- 会员价  -->
									<input type="text" name="memberPrice" class='form-control' id="memberPrice" value="${productStock.memberPrice == '' ? '' : productStock.memberPrice  }" min="0" step="0.1">
								</td>
								<td class="text-td">
									<!-- 库存数量  -->
									<c:if test="${productStock.stock == -99 }">
										<input type="text" name="stock" disabled="disabled" placeholder="不填默认为0" class="text-input-" id="stock" value="${productStock.stock == -99 ? '' : productStock.stock }" >
										<input type="checkbox" checked="checked" disabled="disabled" name="infinite" onclick="operationStock(this);" id="infinite" value="-99" class="form-control text-input-fl"/>设为无限库存									
									</c:if>
									<c:if test="${productStock.stock != -99 }">
										<input type="text" name="stock" disabled="disabled" placeholder="不填默认为0" class="text-input-" id="stock" value="${productStock.stock == -99 ? '' : productStock.stock }">
										<input type="checkbox" name="infinite" disabled="disabled" onclick="operationStock(this);" id="infinite" value="-99" class="form-control text-input-fl"/>设为无限库存									
									</c:if>
								</td>
								<td>
									<!-- 缺货提醒  -->
									<input type="text" name="alarmValue" class='form-control' id="alarmValue" value="${productStock.alarmValue }" >
								</td>
								<c:if test="${sortNo.first }">
									<td><input type="button" id="addProductStock" name="addProductStock" value="新增"/></td>
									<td><input type="button" id="copyAndAddProductStock" value="复制并新增"/></td>									
								</c:if>
								<c:if test="${v != 1}">
									<td colspan='2'><input type='button' onclick='deleteProductStockFunction(this)' id='deleteProductStock' name='deleteProductStock' value='删除'/></td>
								</c:if>
								
							</tr>
						</c:forEach>
					</c:if>
				
				</table>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">商品排序</label>
					<div class="col-md-2">
						<input type="text" name="sort" class='form-control' id="sort" value="${product.sort}" maxlength="7">
					</div>
						<label class="col-md-0 control-label" style="color: gray;font-size: 15px" >（在收银端、APP端的商品列表按照该数值排序，数值越大越在前面）</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">品牌</label>
					<div class="col-md-2">
						<select class="form-control" id="brandId" name="brandId" style="width: 215px">
							<option value="">请选择品牌</option>
							<c:forEach items="${brandList }" var="brand">
								<option id="${brand.id }" value="${brand.id }">${brand.name }</option>
							</c:forEach>
						</select>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">是否称重</label>
					<div class="col-md-2">
						<input type="radio" name="weighing" value="1" >&nbsp;&nbsp;是
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="weighing" value="0" >&nbsp;&nbsp;否
					</div>
						<label class="col-md-0 control-label" style="color: gray;font-size: 15px" >（若可以称重，则商品数量可以保留两位小数）</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">上架管理</label>
					<div class="col-md-2">
						<input type="checkbox" name="shelves" value="0" >&nbsp;&nbsp;收银端上架
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="checkbox" name="shelves" value="2" >&nbsp;&nbsp;APP端上架
					</div>
						<label class="col-md-0 control-label" style="color: gray;font-size: 15px" ></label>&nbsp;&nbsp;
				</div>
				
			    <div class="form-group">
					<label class="col-md-1 control-label">是否支持退货</label>
					<div class="col-md-2">
						<input type="radio" name="returnGoods" value="1" >&nbsp;&nbsp;是
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="returnGoods" value="0" >&nbsp;&nbsp;否
					</div>
						<label class="col-md-0 control-label" style="color: gray;font-size: 15px" >（若不支持退货，则该商品不能在收银机上进行退款）</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">商品图片</label>
					<div id="divPicture" class="col-md-2">
						<input type="button" id="upPicture" class="btn btn-large btn-block btn-default"  name="upPicture" title="仅支持330*220的格式" value="上传商品图片" data-toggle="modal" data-target="#MyImageChange"/>&nbsp;&nbsp;
						<input type="file" id="pictureFile" style="width: 1px;height:1px"name="pictureFile" class="file"  value="上传图片" title="上传图片"/>
						<input type='hidden' id="picture" name="picture" class='form-control' value="${product.picture }"/>
						<input type="hidden" id="pictureFileCache" name="pictureFileCache" value="${product.picUrl }"/>
						<div id="pictureImagediv">
							<img alt="商品图片" style="cursor: pointer;" id="pictureImage" name="pictureImage" src="" onload="imageLoad(this);">
						</div>
						<input type="button" id="originalPicture" class="btn btn-large btn-block btn-default"  name="originalPicture" data-toggle="tooltip" value="恢复原始商品图片"/>&nbsp;&nbsp;
						<input type="button" id="delPicture" class="btn btn-large btn-block btn-default"  name="delPicture" data-toggle="tooltip" value="删除商品图片"/>&nbsp;&nbsp;
					</div>
					<label class="col-md-0 control-label" style="color: gray;font-size: 15px" >（推荐尺寸330*220）</label>&nbsp;&nbsp;
				</div>
						
				<div class="form-group">
					<label class="col-md-1 control-label">商品备注</label>
					<div class="col-md-2">
						<textarea id="remarks"  name="remarks" value="${product.remarks }" placeholder="上限150字" cols=15 rows=5  class='form-control' maxlength="150">${product.remarks }</textarea>
					</div>
				</div>
							
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button"  id='commodityManagerAddBtn' class='btn btn-primary' value="${empty product ? '添加' : '修改' }" data-loading='稍候...' />
					</div>
				</div>
				<input type="hidden" id="myImgUpload" name="imgUpLoad" value="original" />
			</form>
	  </div>
   </div>
   
   <%-- //======begin 新图片上传，syk:2016/10/13================================================================================== --%>
   <div id="MyImageChange" class="modal fade">
	<div class="modal-dialog modal-lg">
	 <div class="modal-content">
	  <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
        <h4 class="modal-title">选择图片</h4>
      </div>
      <div class="modal-body">
      	<button id="selectFile" class="btn btn-primary">选择图片文件</button>
		<input type="file" id="file">
		<div id="clipArea"></div>
		<!-- <button id="selectImg">选择图片</button>  -->
		<label id="imgTip">选择合适的区域截取图片。鼠标滚轮可缩放图片，双击鼠标可旋转图片。</label>
		<button id="clipBtn" class="btn btn-primary">截取图片</button>
		<div id="view"></div>
	  </div>
	  <div class="modal-footer">
	  	<button id="selectImg" type="button" class="btn btn-primary" data-dismiss="modal">保存</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      </div>

		<script src="${contextPath}/static/js/syk/iscroll-zoom.js"></script>
		<script src="${contextPath}/static/js/syk/hammer.js"></script>
		<script src="${contextPath}/static/js/syk/lrz.all.bundle.js"></script>
		<script src="${contextPath}/static/js/syk/jquery.photoClip.js"></script>
		<script>
			$("#selectFile").click(function(){
				$("#file").click();
			});
			var clipArea = new bjj.PhotoClip("#clipArea", {
				size : [ 330, 220 ],
				outputSize : [ 330, 220 ],
				file : "#file",
				view : "#view",
				ok : "#clipBtn",
				loadStart : function() {
					console.log("照片读取中");
				},
				loadComplete : function(src) {
					console.log("照片读取完成" + src);
					$("#imgTip").css("display","block");
				},
				loadError: function(event) {
					console.log("照片读取异常" + event);
				},
				clipFinish : function(dataURL) {
					console.log(dataURL);
					$("#selectImg").css("display","inline");
					$("#selectImg").click(function(){
						$("#pictureImage").attr("src",dataURL);
						$("#pictureImage").show();
						//document.body.style.overflow="auto";
						//$("#MyImageChange").css("display","none");
						//var blob = dataURLToBlob(dataURL);
						//$("#commodityManagerAddForm").append('pictureFile',blob);
						//alert(blob);
						$("#myImgUpload").attr("value",dataURL);
						//$("#pictureFile").val($("#file").val());
						//$("#pictureFile")[0].files[0]=blob;
						//var fileName = $("#file").val();
						$("#pictureFileCache").val("change");
					});
				}
			});
			//clipArea.destroy();
		</script>
	  </div>
	 </div>
	</div>
	<%-- //======end 新图片上传，syk:2016/10/13================================================================================= --%>
	
</body>
</html>