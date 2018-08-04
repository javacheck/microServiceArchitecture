<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品详情(仅供查看)</title> 
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
	
<style type="text/css">
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
</style>
<script type="text/javascript">

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
		
		var categoryId = $("#categoryId").val();;
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
			lm.alert("第一列必须都填写或者都不填写!");
			return;
		}
		
		if( count != value2Arr.length && value2Arr.length  != 0 ){
			lm.alert("第二列必须都填写或者都不填写!");
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
						
						if( inputValue.length > 25){
							lm.alert("条形码最大只能25位!");
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
				"<c:forEach items='${unitList }' var='unit'>"+
					"<option value='${unit.id}'>${unit.name}</option>"+
				"</c:forEach>"+
			"</select>"+
		"</td>"+
		"<td>"+
			// 商品条码  
			"<input name='barCode' id='barCode' value='${productStock.barCode }' maxlength='25' class='form-control'/>"+
			"<input type='hidden' name='productStockId' id='productStockId' value='-90000' class='form-control'/>"+
		"</td>"+
		"<td>"+
			// 销售价 
			"<input type='number' name='price' class='form-control' id='price' value='' min='0' step='0.1'>"+
		"</td>"+
		"<td>"+
			// 进货价   
			"<input type='number' name='costPrice' class='form-control' id='costPrice' value='' min='0' step='0.1'>"+
		"</td>"+
		"<td>"+
			// 市场价  
			"<input type='number' name='marketPrice' class='form-control' id='marketPrice' value='' min='0' step='0.1'>"+
		"</td>"+
		"<td>"+
			// 会员价  
			"<input type='number' name='memberPrice' class='form-control' id='memberPrice' value='' min='0' step='0.1'>"+
		"</td>"+
		"<td class='text-td'>"+
			// 库存数量   
			"<input type='number' name='stock' placeholder='不填默认为0' class='text-input-' id='stock' value='0' min='0' step='0.1'>"+
			"<input type='checkbox' onclick='operationStock(this);' name='infinite' id='infinite' value='-99' class='form-control text-input-fl'/>设为无限库存"+
		"</td>"+
		"<td>"+
			// 缺货提醒 
			"<input type='number' name='alarmValue' class='form-control' id='alarmValue' value='0' min='0' step='0.1'>"+
		"</td>"+
		"<td colspan='2'><input type='button' onclick='deleteProductStockFunction(this)' id='deleteProductStock' name='deleteProductStock' value='删除'/></td>"+
	"</tr>";
	
		productStock_Table.append(html);
	});
	
	
	
	
	$("#copyAndAddProductStock").click(function(){
		
		var html = "<tr class='bg-c'>";
		$("#productStock_Table tr").each(function(key,value) {
			if( key == 0 ){
				return;
			}
			if( key >= 2 ){
				return false;
			}
			
			html += $(this).html();			
		});
		html += "</tr>";
		
		html = html.replace($(html).find("input[id='productStockId']").val(),"-90000");
		
		html = html.replace($(html).find("input[id='addProductStock']").parent().html(),"");
		html = html.replace($(html).find("input[id='copyAndAddProductStock']").parent().html(),"");
		
		var lastTd = "<td colspan='2'><input type='button' onclick='deleteProductStockFunction(this)' id='deleteProductStock' name='deleteProductStock' value='删除'/></td>";
		
		html = html.replace("<td></td>","");
		html = html.replace("<td></td>",lastTd);
		
		console.log("create tr --->> "+html);
		$("#productStock_Table").append(html);
	});
	
});

function deleteProductStockFunction(obj){
	var productStockId = $(obj).parent().parent().find("input[name='productStockId']").val();
	if( undefined != productStockId && "-90000" != productStockId && "" != productStockId ){
		lm.post("${contextPath }/commodityManager/ajax/deleteProductStock", {productStockId:productStockId}, function(data) {
			if( null != data && "" != data ){
				Console.log("delete productStockId is "+data);
			}
		});		
	}
	$(obj).parent().parent().remove();
}

function operationStock(obj){
	if( $(obj).is(":checked") ){ // 选中
		if( $(obj).prev().attr("id") == "stock" ){
			$(obj).prev().val("");
			$(obj).prev().attr("placeholder","无限库存");
			$(obj).prev().attr("readOnly",true);				
		}
	} else {
		if( $(obj).prev().attr("id") == "stock" ){
			$(obj).prev().attr("placeholder","不填默认为0");
			$(obj).prev().attr("readOnly",false);				
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

	var weighing = $("input[name='weighing']:checked").val();
	if( weighing == 0 ){ // 否
		$("input[name='stock']").attr("step","1");
		$("input[name='alarmValue']").attr("step","1");
	} else {
		$("input[name='stock']").attr("step","0.1");			
		$("input[name='alarmValue']").attr("step","0.1");			
	}
	
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


$(document).ready(function(){
	$("#commodityManagerReturnBtn").click(function(){
		 window.location.href = "${contextPath }/commodityManager/list";
	}); 
	
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
});
//*******************************************************将输入框设置为增加减少的数字框 ---END--- ********************************************

</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>商品详情(仅供查看)
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
						<input type="text" name="name" disabled="disabled" class='form-control' id="name" value="${product.name }" maxlength="48"  >
						<input type="hidden" name="cacheName" class='form-control' id="cacheName" value="${product.name }" maxlength="48"  >
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">拼音码</label>
					<div class="col-md-2">
						<input name="shortName" id="shortName"  disabled="disabled" value="${product.shortName }" class="form-control"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>&nbsp;&nbsp;商品分类</label>
					<div class="col-md-2">
						<m:treeSelect treeRoot="updateTreeRoot" ban="true" selectID="categoryId" showId="${product.categoryId }" showValue="${product.categoryName }"></m:treeSelect>
					</div>
				</div>
				
				<div class="form-group">								
				<label class="col-md-1 control-label text-label"><span style="color: red;font-size: 15px">*</span>&nbsp;&nbsp;商品规格</label>
				<table border="1" id="productStock_Table" class="text-c col-md-9">
					<tr class="bg-c" id="trHearder">
						<td width="10%">
							<select class="form-control" disabled="disabled" name="attribute_1" id="attribute_1" style="width: 100px"> 
								<c:forEach items="${attributeList }" var="attribute">
									<c:if test="${attribute.id == product.attribute_1 }">
										<option value="${attribute.id }"  selected="selected">${attribute.name }</option>									
									</c:if>
									<c:if test="${attribute.id != product.attribute_1 }">
										<option value="${attribute.id }">${attribute.name }</option>									
									</c:if>
								</c:forEach>
							</select> 
						</td>
						<td>
							<select class="form-control" disabled="disabled" name="attribute_2" id="attribute_2" style="width: 100px"> 
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
						<td width="13%"><span style="color: red;font-size: 15px">*</span>&nbsp;&nbsp;商品条码</td>
						<td><span style="color: red;font-size: 15px">*</span>&nbsp;&nbsp;销售价</td>
						<td><span style="color: red;font-size: 15px">*</span>&nbsp;&nbsp;进货价</td>
						<td><span style="color: red;font-size: 15px">*</span>&nbsp;&nbsp;市场价</td>
						<td>会员价</td>
						<td width="22%">库存数量</td>
						<td>缺货提醒</td>
					</tr>
					
					<c:if test="${not empty productStockList }">
						<c:forEach items="${productStockList }" varStatus="sortNo"  var="productStock">
							<c:set var="v" value="${ sortNo.index+1}"></c:set>
							<tr class="bg-c">
								<td><input name="value" id="value_1" disabled="disabled" value="${productStock.value_1 }" maxlength='50' class="form-control"/></td>
								<td><input name="value" id="value_2" disabled="disabled" value="${productStock.value_2 }" maxlength='50' class="form-control"/></td>
								<td>
									<!-- 单位  -->
									<select class="form-control" disabled="disabled" id="unitId" name="unitId" style="width: auto"> 
										<option value="">无</option>  
											<c:forEach items="${unitList }" var="unit">
												<option value="${unit.id }">${unit.name }</option>
											</c:forEach>
									</select>
								
								</td>
								<td>
									<!-- 商品条码  -->
									<input name="barCode" id="barCode" disabled="disabled" value="${productStock.barCode }" maxlength='25' class="form-control"/>
									<input type="hidden" name="productStockId" id="productStockId" value="${productStock.id }" class="form-control"/>
								</td>
								<td>
									<!-- 销售价  -->
									<input type="number" name="price" disabled="disabled" class='form-control' id="price" value="${productStock.price }" min="0" step="0.1">
								</td>
								<td>
									<!-- 进货价  -->
									<input type="number" name="costPrice" disabled="disabled" class='form-control' id="costPrice" value="${productStock.costPrice }" min="0" step="0.1">
								</td>
								<td>
									<!-- 市场价  -->
									<input type="number" name="marketPrice" disabled="disabled" class='form-control' id="marketPrice" value="${productStock.marketPrice }" min="0" step="0.1">
								</td>
								<td>
									<!-- 会员价  -->
									<input type="number" name="memberPrice" disabled="disabled" class='form-control' id="memberPrice" value="${productStock.memberPrice == '' ? '' : productStock.memberPrice  }" min="0" step="0.1">
								</td>
								<td class="text-td">
									<!-- 库存数量  -->
									<c:if test="${productStock.stock == -99 }">
										<input type="text" name="stock" readonly="readonly" placeholder="不填默认为0" class="text-input-" id="stock" value="${productStock.stock == -99 ? '' : productStock.stock }">
										<input type="checkbox" checked="checked" disabled="disabled" name="infinite" onclick="operationStock(this);" id="infinite" value="-99" class="form-control text-input-fl"/>设为无限库存									
									</c:if>
									<c:if test="${productStock.stock != -99 }">
										<input type="text" style="background-color: #e5e5e5;" name="stock" readonly="readonly" placeholder="不填默认为0" class="text-input-" id="stock" value="${productStock.stock == -99 ? '' : productStock.stock }">
										<input type="checkbox" name="infinite" disabled="disabled" onclick="operationStock(this);" id="infinite" value="-99" class="form-control text-input-fl"/>设为无限库存									
									</c:if>
								</td>
								<td>
									<!-- 缺货提醒  -->
									<input type="number" name="alarmValue" disabled="disabled" class='form-control' id="alarmValue" value="${productStock.alarmValue }" min="0" step="0.1">
								</td>
							</tr>
						</c:forEach>
					</c:if>
				
				</table>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">商品排序</label>
					<div class="col-md-2">
						<input type="text" name="sort" disabled="disabled" class='form-control' id="sort" value="${product.sort}" maxlength="7">
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">品牌</label>
					<div class="col-md-2">
						<select class="form-control" disabled="disabled" id="brandId" name="brandId" style="width: 215px">
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
						<input type="radio" name="weighing" disabled="disabled" value="1" >&nbsp;&nbsp;是
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="weighing" disabled="disabled" value="0" >&nbsp;&nbsp;否
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">上架管理</label>
					<div class="col-md-2">
						<input type="checkbox" name="shelves" disabled="disabled" value="0" >&nbsp;&nbsp;收银端上架
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="checkbox" name="shelves" disabled="disabled" value="2" >&nbsp;&nbsp;APP端上架
					</div>
						<label class="col-md-0 control-label" style="color: gray;font-size: 15px" ></label>&nbsp;&nbsp;
				</div>
				
			    <div class="form-group">
					<label class="col-md-1 control-label">是否支持退货</label>
					<div class="col-md-2">
						<input type="radio" name="returnGoods" disabled="disabled" value="1" >&nbsp;&nbsp;是
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="returnGoods" disabled="disabled" value="0" >&nbsp;&nbsp;否
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">商品图片</label>
					<div id="divPicture" class="col-md-2">
						<input type='hidden' id="picture" name="picture" class='form-control' value="${product.picture }"/>
						<input type="hidden" id="pictureFileCache" name="pictureFileCache" value="${product.picUrl }"/>
						<div id="pictureImagediv">
							<img alt="商品图片" style="cursor: pointer;" id="pictureImage" name="pictureImage" src="" onload="imageLoad(this);">
						</div>
					</div>
				</div>
						
				<div class="form-group">
					<label class="col-md-1 control-label">商品备注</label>
					<div class="col-md-2">
						<textarea id="remarks"  name="remarks" disabled="disabled" value="${product.remarks }" placeholder="上限150字" cols=15 rows=5  class='form-control' maxlength="150">${product.remarks }</textarea>
					</div>
				</div>
							
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button"  id='commodityManagerReturnBtn' class='btn btn-primary' value="返回" data-loading='稍候...' />
					</div>
				</div>
			</form>
	  </div>
   </div>
</body>
</html>