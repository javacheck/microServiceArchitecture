<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>库存列表</title>
<script type="text/javascript">
function deleteProductAndProductStock(productStockId){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/product/delete/delete-by-productStockId",{productStockId:productStockId},function(data){
			if(data==1){
				lm.alert("删除成功！");
				loadCurrentList_productList();
			} else if(data==0){
				lm.alert("该商品已出售，只能下架！");
			}
			
		});
	});
}

$(function(){
	var data="${data }";
	if(data!=null && data!="" ){
		if(data==0){
			lm.alert("操作成功！");
			window.location.href="${contextPath }/product/list";
		}
	}
	 
});

function formReset(){
	$("#storeId").val("");
	$("#storeCategoryId").val("");
	$("#sysCategoryId").val("");
}

function callback(){
	$("[name='shopListModalBtn']").each(function(key,value){
		var dataRemote = $(value).attr("data-remote");
		$(value).attr("data-remote",dataRemote + "?date="+new Date());
	});
	$("#all").click(function(){
		
	    if(this.checked){    
	        $("input:checkbox").prop("checked", true);   
	    }else{    
	    	$("input:checkbox").prop("checked", false); 
	    }    
	});
}
function delAll(){
	
    var productStockIds="";
    $("input[name='listProudctStockName']:checked").each(function(i){ 
    	productStockIds+=$(this).val()+","; 
    }); 
    productStockIds=productStockIds.substring( 0,productStockIds.length-1);
    if(productStockIds!=""){
	    lm.confirm("确定要删除吗？",function(){
			lm.post("${contextPath }/product/delete/deleteAll-by-productStockIds",{productStockIds:productStockIds},function(data){
				if(data!=null && data!=''){
					lm.alert("商品"+data[0]+"已出售，只能下架！");
				}else{
					lm.alert("删除成功！");
					loadCurrentList_productList();
				}
			});
		});
    }else{
    	lm.alert("请选择要删除的库存！");
    	return;
    }

}
function getExe() {
	var flag=${isSys};
	var storeId="";
	if(flag){
		var storeId=$("#storeId").val();//店铺
	}
	var storeCategoryId=$("#storeCategoryId").val();//商品分类ID
	var name=$("#name").val();//商品名称
	var alarmValueType= $("#alarmValueType option:selected").val(); // 是否缺货
	var shelves=$("#shelves option:selected").val(); //商品状态
	var barCode=$("#barCode").val();
	window.open("${contextPath}/product/list/ajax/list-by-search?storeId="+storeId+"&categoryId="+storeCategoryId
					+"&name="+name+"&alarmValueType="+alarmValueType
					+"&shelves="+shelves+"&barCode="+barCode);
}
</script>
<style>
  .input-group2{
  float:left;width:auto; background-color: #e5e5e5;border: 1px solid #ccc;border-radius: 4px; color: #222;font-size: 13px;font-weight: 400; padding: 5px 12px;text-align: center;
  }
  </style>
</head>
<body>
	<m:hasPermission permissions="productAdd" flagName="addFlag"/>
	<m:list title="库存列表" id="productList" pageNo="${pageNo }"
		listUrl="${contextPath }/product/list/list-data"  callback="callback"
		addUrl="${addFlag == true ? '/product/add' : '' }"
		searchButtonId="cateogry_search_btn" formReset="formReset" >

		<div class="input-group" style="max-width:1800px;">
			
            <c:choose>  
			   <c:when test="${isSys==true }">  
			   		<span class="input-group-addon">商家</span>
					<input name="storeName" id="storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="${store.name }" class="form-control" isRequired="1" tipName="商家" data-remote="${contextPath }/product/shopList/list" data-toggle="modal" />
					<input type="hidden" name="storeId" id="storeId" value="${store.storeId }" />
			   </c:when>  
			   <c:otherwise> 
			   		<m:hasPermission permissions="mainShopOrderStoreView">
			   			<c:if test="${isMainStore==true }">
							<span class="input-group-addon">商家</span>
							<input name="storeName" id="storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="${store.name }" class="form-control" isRequired="1" tipName="商家" data-remote="${contextPath }/product/shopList/list" data-toggle="modal" />
							<input type="hidden" name="storeId" id="storeId" value="-1" />
						</c:if>
       				</m:hasPermission>  
			   </c:otherwise>  
			</c:choose>
            
            <span class="input-group2">商家分类</span>
           	<m:selectProductCategory onStoreChange="shopChange" isSys="false" inputName="storeCategoryId" inputId="storeCategoryId" path="${path}" auto="false"></m:selectProductCategory>
           		
            <span class="input-group2">商品名称</span> 
            	<input type="text" id="name" name="name" class="form-control" placeholder="商品名称" style="width: 200px;float:left;margin-right:40px;">
            	
             <span class="input-group2">商品条码</span> 
            	<input type="text" id="barCode" name="barCode" class="form-control" placeholder="商品条码" style="width: 200px;float:left;margin-right:40px;">	
            	
            <span class="input-group2">是否缺货</span>
           	<select name="alarmValueType" class="form-control" style="width: auto;float:left;margin-right:40px;" id="alarmValueType" >
				<option  value ="">全部</option>
				<option  value ="1">缺货</option>
           	</select>
           	<span class="input-group2">商品状态</span>
           	<select name="shelves" class="form-control" style="width: auto;float:left;margin-right:40px;" id="shelves" >
				<option  value ="">全部</option>
				<option  value ="0">上架</option>
				<option  value ="1">下架</option>
           	</select>
		</div>
		<a href="" onclick="getExe()" class="btn btn-small btn-danger" style="width: auto;margin-top:5px;">导出库存</a>
	</m:list>
<script type="text/javascript">
$(function(){
	var isSys=${isSys};
	var isMainStore=${isMainStore};
	var storeIdCache="${storeIdCache}";
	if(isSys==false && isMainStore==false){
		shopChange(storeIdCache);
	}
});
</script>
</body>
</html>