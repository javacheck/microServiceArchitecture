<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品列表</title>
<script type="text/javascript">
function deleteProductStock(id){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/productStock/delete/delete-by-id",{id:id},function(data){
			if(data=="0"){
				lm.alert("删除成功！");
			} else {
				lm.alert("商品下已有库存，不能删除！");
				return;
			}
			window.location.href="${contextPath}/productStock/list";
		});
	});
}

function formReset(){
	$("#storeId").val("");
	$("#storeCategoryId").val("");
	$("#sysCategoryId").val("");
}

function callback(){
	$("[name='viewButton']").each(function(key,value){
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
	
    var productIds="";
    $("input[name='listProudctName']:checked").each(function(i){ 
    	productIds+=$(this).val()+","; 
    }); 
    productIds=productIds.substring( 0,productIds.length-1);
    if(productIds!=""){
	    lm.confirm("确定要删除吗？",function(){
			lm.post("${contextPath }/productStock/delete/deleteAll-by-productIds",{productIds:productIds},function(data){
				if (data != null && data!="") { 
					lm.alert("商品:"+data[0].productName+"下已有库存，不能删除！");
					return;
				}else{
					lm.alert("删除成功！");
					loadCurrentList_productStockList();
				}
			});
		});
    }else{
    	lm.alert("请选择要删除的商品！");
    }

}
</script>
<style>
  .input-group2{
  float:left;width:auto; background-color: #e5e5e5;border: 1px solid #ccc;border-radius: 4px; color: #222;font-size: 13px;font-weight: 400; padding: 5px 12px;text-align: center;
  }
  </style>
</head>
<body>
	<m:hasPermission permissions="productStockAdd" flagName="addFlag"/>
	<m:list title="商品列表" id="productStockList"
		listUrl="${contextPath }/productStock/list/list-data"  callback="callback"
		addUrl="${addFlag == true ? '/productStock/add' : '' }"
		searchButtonId="cateogry_search_btn" formReset="formReset" >

		<div class="input-group" style="max-width:1200px;">
			<c:choose>  
			   <c:when test="${isSys==true }">  
			   		<span class="input-group-addon">商家</span>
					<input name="storeName" id="storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="${store.name }" class="form-control" isRequired="1" tipName="商家" data-remote="${contextPath }/productStock/shopList/list" data-toggle="modal" />
					<input type="hidden" name="storeId" id="storeId" value="${store.storeId }" />
			   </c:when>  
			   <c:otherwise> 
			   		<m:hasPermission permissions="mainShopOrderStoreView">
			   			<c:if test="${isMainStore==true }">
							<span class="input-group-addon">商家</span>
							<input name="storeName" id="storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="${store.name }" class="form-control" isRequired="1" tipName="商家" data-remote="${contextPath }/productStock/shopList/list" data-toggle="modal" />
							<input type="hidden" name="storeId" id="storeId" value="-1" />
						</c:if>
       				</m:hasPermission>  
			   </c:otherwise>  
			</c:choose>	
            
            <span class="input-group2">商家分类</span>
           	<m:selectProductCategory onStoreChange="shopChange" isSys="false" inputName="storeCategoryId" inputId="storeCategoryId" path="${path}" auto="false"></m:selectProductCategory>
           		
            <span class="input-group2">商品名称</span> 
            	<input type="text" id="name" name="name" class="form-control" placeholder="商品名称" style="width: 200px;float:left;">
		</div>
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