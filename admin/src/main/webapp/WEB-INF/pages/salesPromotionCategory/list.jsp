<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>促销分类列表</title>
<script type="text/javascript">
// 定义存储促销商品数据的缓存数组
var cacheArray = new Array();
var stock_salesNum; // 用于存储选中促销分类所设定的促销数量
function checkboxSelectEvent(obj){
	if(obj.checked){
		$(obj).prop("checked",false);
	} else {
		var stockId = $(obj).attr("id");
		var flag = false;
		if(null != stock_salesNum && "" != stock_salesNum){
			lm.postSync("${contextPath}/salesPromotionCategory/list/ajax/checkProductStock",{stockId:stockId,stock:stock_salesNum},function(data){
				if(data > 0){
					flag = true;
				} 
			});
		} else { // 促销分类设定的促销数量为无限数量
			flag = true;
		}
		if(flag){
			$(obj).prop("checked",true);
		}else {
			lm.alert("库存不足");
		}
	}
}

function trSelectEvent(obj){
	var child = $(obj).find("input:checkbox");
	if(child[0].checked){
		child.prop("checked",false);
		// 移除指定的元素
		cacheArray.splice($.inArray($(obj).attr("stockIdSign") + "_" + $(obj).attr("originalPriceSign") + "_" + $(obj).attr("shelvesSign"),cacheArray),1); 
	} else {
		var stockId = $(obj).attr("id");
		var flag = false;
		if(null != stock_salesNum && "" != stock_salesNum){
			lm.postSync("${contextPath}/salesPromotionCategory/list/ajax/checkProductStock",{stockId:stockId,stock:stock_salesNum},function(data){
				if(data > 0){
					flag = true;
				} 
			});
		} else {
			flag = true;
		}
		
		if(flag){
			child.prop("checked",true);
			cacheArray.push($(obj).attr("stockIdSign") + "_" + $(obj).attr("originalPriceSign") + "_" + $(obj).attr("shelvesSign"));	
		}else {
			lm.alert("库存不足");
		}
	}
}

function checkPageALL(obj){
	if(obj.checked){
		var link_Stock_limit = "";
		$("#show_ProductStock_Model input:checkbox[id!='pageALLCheckBox']").each(function(key,value){
			
			var stockId = $(this).attr("id");
			var flag = false;
			if(null != stock_salesNum && "" != stock_salesNum){
				lm.postSync("${contextPath}/salesPromotionCategory/list/ajax/checkProductStock",{stockId:stockId,stock:stock_salesNum},function(data){
					if(data > 0){
						flag = true;
					} 
				});
			} else {
				flag = true;
			}
			
			if(flag){
				if(-1 ==  $.inArray($(value).attr("stockIdSign") + "_" + $(value).attr("originalPriceSign") + "_" + $(value).attr("shelvesSign"),cacheArray) ){
					cacheArray.push($(value).attr("stockIdSign") + "_" + $(value).attr("originalPriceSign") + "_" + $(value).attr("shelvesSign"));	
				}
				$(this).prop("checked",true);
			} else {
				link_Stock_limit += stockId;
				link_Stock_limit += "_";
			}
			
		});
		if("" != link_Stock_limit){
			link_Stock_limit = link_Stock_limit.slice(0,link_Stock_limit.length-1);
			lm.alert("商品ID："+link_Stock_limit + ",库存不足!");			
		}
	} else {
		$("#show_ProductStock_Model input:checkbox[id!='pageALLCheckBox']").each(function(key,value){
			// 查询数组中是否有此数据，有则从数组中移除
			if(-1 !=  $.inArray($(value).attr("stockIdSign") + "_" + $(value).attr("originalPriceSign") + "_" + $(value).attr("shelvesSign"),cacheArray) ){
				// 移除指定的元素
				cacheArray.splice($.inArray($(value).attr("stockIdSign") + "_" + $(value).attr("originalPriceSign") + "_" + $(value).attr("shelvesSign"),cacheArray),1); 
			}
		});
		$("#show_ProductStock_Model input:checkbox[id!='pageALLCheckBox']").prop("checked",false);
	}
}

//-------------------------------------------------------------------------------------------------------------------------
function deleteById(storeId,id){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/salesPromotionCategory/delete/delete-by-Id",{storeId:storeId,id:id},function(data){
			if(data==1){
				lm.alert("删除成功！");
			} else {
				lm.alert("删除失败！");
			}
			loadCurrentList_salesPromotionCategoryList();
		});
	});
}

function againById(storeId,id){
	lm.confirm("确定要重新开始吗？",function(){
		lm.post("${contextPath }/salesPromotionCategory/again/again-by-Id",{storeId:storeId,id:id},function(data){
			if(data==1){
				lm.alert("重新开始成功！");
			} else {
				lm.alert("重新开始失败！");
			}
			loadCurrentList_salesPromotionCategoryList();
		});
	});
}

function viewById(storeId,id){
	location.href = "${contextPath }/salesPromotion/list?storeId=" + storeId + "&salesPromotionCategoryId="+id;
}

function showById(storeId,id){
	$("#model_query_storeId").val(storeId);
	$("#salesPromotionCategoryId").val(id);
	
	$("#salesPromotionCategoryIdCache").val(id);
	
	cacheArray.length = 0; // 清空缓存数组
	
	if( null == storeId || "" == storeId ){
		return ;
	}
	// 查询此商家的商品分类
	lm.post("${contextPath}/salesPromotionCategory/list/ajax/productSystemCategory",{type:1,storeId:storeId},function(data){
		$("#productCategoryId option[id!='listPleaseSelect']").remove();
		if(null != data && "" != data){
			for(var i=0;i<data.length;i++){
	    		 $("#productCategoryId").append('<option id="' + data[i].id + '" value="' + data[i].id + '">' + data[i].name + '</option>');
			}
		}
	});
	
	loadCurrentList_salesPromotionCategoryList_modelShow(); // 刷新数据
	$("#salesPromotionCategoryProductShowID").modal();
}


$(function(){
	$("#salesPromotionInfoAddBtn").click(function(){
		if( cacheArray.length <= 0 ){
			lm.alert("请选择需要新增的促销商品");
			return;
		}
		cacheArray.sort(); // 排序
		$("#salesPromotionInfoCache").val(cacheArray);
		$("#salesPromotionInfoFormSubmit").submit();
	});
});

function formReset(){
	$("#salesPromotionCategory_storeName").val("");
	$("#salesPromotionCategory_storeId").val("");
}

$(function(){
	// 点击弹出商家选择窗口
	$("#salesPromotionCategory_storeName").click(function (){
		$("#salesPromotionCategoryShowStore").modal();
	});
});

function callback(obj){
	$("#salesPromotionCategory_storeName").val(obj.name);
	$("#salesPromotionCategory_storeId").val(obj.id);
}
</script>
</head>
<body>
	<m:hasPermission permissions="salesPromotionCategoryAdd" flagName="addFlag"/>
	<m:list title="促销分类列表" id="salesPromotionCategoryList"
		listUrl="/salesPromotionCategory/list-data"
		addUrl="${addFlag == true ? '/salesPromotionCategory/add' : '' }"
		searchButtonId="salesPromotionCateogry_search_btn" formReset="formReset">
		
		<div class="input-group" style="max-width: 800px;">
			<c:if test="${salesPromotionCategoryIsStore == null}">
				<span class="input-group-addon">商家</span>
				<input type="text" name="storeName"  readonly="readonly" class='form-control' id="salesPromotionCategory_storeName" value="" >
				<input type="hidden" name="storeId" class='form-control' id = "salesPromotionCategory_storeId" value="" >
			</c:if>
			
			<span class="input-group-addon">促销类型</span>
           	<select name="salesPromotionCategoryType" class="form-control" id="salesPromotionCategoryType">
           		<option id="all" value="-10" selected="selected">全部</option>
           		<option id="first" value="1">统一价格</option>
           		<option id="last" value="0">折扣</option>
           	</select>
           	
			<span class="input-group-addon">促销名称</span>
			<input type="text" name="salesPromotionCategoryName" class='form-control' id="salesPromotionCategoryName" value="" >
		</div>
	</m:list>
<m:select_store path="${contextPath}/salesPromotionCategory/showShopModel/list/list-data" modeId="salesPromotionCategoryShowStore" callback="callback"> </m:select_store>



<div class="modal fade" id="salesPromotionCategoryProductShowID">
	 <div class="modal-dialog modal-lg" style="width:1200px">
	  <div class="modal-content">
		    <div class="modal-header">
		      <button type="button" id="salesPromotionCategoryClose" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
		    </div>
		    <div class="modal-body">
				 <m:list title="商品列表" id="salesPromotionCategoryList_modelShow"
					listUrl="/salesPromotionCategory/showProductStockModel/list/list-data"
					 searchButtonId="salesPromotionCategory_mode_search_btn">
					<div class="input-group" style="max-width: 800px;">
						<input type="hidden" id="model_query_storeId" name="model_query_storeId">
						<input type="hidden" id="salesPromotionCategoryId" name="salesPromotionCategoryId">
					
						<span class="input-group-addon">商品分类</span>
						<select id="productCategoryId" class='form-control' style="width: auto;float:left;margin-right:40px;" name="productCategoryId">
							<option id="listPleaseSelect" value="-1">请选择</option>
							
						</select>
						
						<span class="input-group-addon">商品名称</span> 
						<input type="text"	name="productName" class="form-control" placeholder="商品名称" style="width: 200px;">
					</div>
						<input style="margin-top: 5px;" type="button" id='salesPromotionInfoAddBtn' class='btn btn-primary' value="确定" />		
				</m:list>
		    </div>
	  </div>
	</div>
</div>

<form id="salesPromotionInfoFormSubmit" method='post' class='form-horizontal' action="${contextPath }/salesPromotionCategory/saveSalesPromotionInfo" >
	<!-- 存储促销商品的缓存信息 -->
	<input id="salesPromotionInfoCache" name="salesPromotionInfoCache" type="hidden" value="" />
	<!-- 存储促销商品的分类ID -->
	<input id="salesPromotionCategoryIdCache" name="salesPromotionCategoryIdCache" type="hidden" value="" />
</form>
</body>
</html>