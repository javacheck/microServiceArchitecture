<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品品牌列表</title>
<script type="text/javascript">
function callback(obj){
	$("#productBrand_storeName").val(obj.name);
	$("#productBrand_storeId").val(obj.id);
}
function deleteproductBrand(id){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/productBrand/delete/delete-by-productBrandId",{id:id},function(data){
			if( data==2 ){
				lm.alert("此品牌关联到了商品，不能删除！");
				return;
			}else{
				lm.alert("删除成功");
			}
			loadCurrentList_productBrandList();
		});
	});
}
function formReset(){
	$("#productBrand_storeName").val("");
	$("#productBrand_storeId").val("");
}
$(function(){
	// 点击弹出商家选择窗口
	$("#productBrand_storeName").click(function (){
		$("#productBrandShowStore").modal();
	});
});
//修改选择的店铺时触发
function byStoreAjaxRefreshList(){
	loadList_productBrandList(); 
}

function callback1(){
	$("#all").click(function(){ 
	    if(this.checked){    
	        $("input:checkbox").prop("checked", true);   
	    }else{    
	    	$("input:checkbox").prop("checked", false); 
	    }    
	});
}
function delAll(){
    var productBrandIds="";
    $("input[name='listproductBrandName']:checked").each(function(i){ 
    	productBrandIds+=$(this).val()+","; 
    }); 
    productBrandIds=productBrandIds.substring( 0,productBrandIds.length-1);
    if(productBrandIds!=""){
	    lm.confirm("确定要删除吗？",function(){
			lm.post("${contextPath }/productBrand/delete/deleteAll-by-productBrandIds",{productBrandIds:productBrandIds},function(data){
				if (data != null && data!="") {
					if(data[0].split(",")[1]=="0"){
						lm.alert("品牌:<span style='color:red;'>"+data[0].split(",")[0]+"</span>关联到了商品，不能删除！");
						return;
					}
				}else{
					lm.alert("删除成功！");
					loadCurrentList_productBrandList();
				}
			});
		});
    }else{
    	lm.alert("请选择要删除的品牌！");
    	return;
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
	<m:hasPermission permissions="productBrandAdd" flagName="addFlag"/>
	<m:list title="商品品牌列表" id="productBrandList"
		listUrl="${contextPath }/productBrand/list/list-data" callback="callback1"
		addUrl="${addFlag == true ? '/productBrand/add' : '' }" 
		searchButtonId="cateogry_search_btn" formReset="formReset">
		
		<div class="input-group" style="max-width:1500px;">
            <c:choose>  
			   <c:when test="${isSys==true }">  
			   		<span class="input-group2">商家</span>
					<input name="storeName" id="productBrand_storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="" class="form-control" isRequired="1" tipName="商家"  />
					<input type="hidden" name="storeId" id="productBrand_storeId" value="" />
			   </c:when>  
			   <c:otherwise> 
			   		<m:hasPermission permissions="mainShopOrderStoreView">
			   			<c:if test="${isMainStore==true }">
							<span class="input-group2">商家</span>
							<input value='${store_session_key.name}' name="storeName" id="productBrand_storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="" class="form-control" isRequired="1" tipName="商家"  />
							<input type="hidden" name="storeId" id="productBrand_storeId" value="-1" />
						</c:if>
       				</m:hasPermission>  
			   </c:otherwise>  
			</c:choose>
            <span class="input-group2">品牌名称</span> 
            <input type="text" id="name" name="name" class="form-control" placeholder="名称" style="width: 200px;float:left;margin-right:40px;">
            	
		</div>
	</m:list>
	<m:select_store path="${contextPath}/productBrand/showModel/list/list-data" modeId="productBrandShowStore" callback="callback"> </m:select_store>
</body>
</html>