<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品单位列表</title>
<script type="text/javascript">
function callback(obj){
	$("#productUnit_storeName").val(obj.name);
	$("#productUnit_storeId").val(obj.id);
}
function deleteproductUnit(id){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/productUnit/delete/delete-by-productUnitId",{id:id},function(data){
			if(data==0){
				lm.alert("该单位已关联商品，不能删除！");
				return;
			}else{
				lm.alert("删除成功");
				loadCurrentList_productUnitList();
			}
		});
	});
}
function formReset(){
	$("#productUnit_storeName").val("");
	$("#productUnit_storeId").val("");
}
$(function(){
	// 点击弹出商家选择窗口
	$("#productUnit_storeName").click(function (){
		$("#productUnitShowStore").modal();
	});
});
//修改选择的店铺时触发
function byStoreAjaxRefreshList(){
	loadList_productUnitList(); 
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
    var productUnitIds="";
    $("input[name='listproductUnitName']:checked").each(function(i){ 
    	productUnitIds+=$(this).val()+","; 
    }); 
    productUnitIds=productUnitIds.substring( 0,productUnitIds.length-1);
    if(productUnitIds!=""){
	    lm.confirm("确定要删除吗？",function(){
			lm.post("${contextPath }/productUnit/delete/deleteAll-by-productUnitIds",{productUnitIds:productUnitIds},function(data){
				if(data==0){
					lm.alert("该单位已关联商品，不能删除！");
					return;
				}else{
					lm.alert("删除成功");
					loadCurrentList_productUnitList();
				}
			});
		});
    }else{
    	lm.alert("请选择要删除的单位！");
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
	
	<m:hasPermission permissions="productUnitAdd" flagName="addFlag"/>
	<m:list addName = "新增单位" title="商品单位列表" id="productUnitList"
		listUrl="${contextPath }/productUnit/list/list-data" callback="callback1"
		addUrl="${addFlag == true ? '/productUnit/add' : '' }" 
		searchButtonId="cateogry_search_btn" formReset="formReset">
		
		<div class="input-group" style="max-width:1500px;">
			   <c:if test="${isSys==true }">  
			   		<span class="input-group2">商家</span>
					<input name="storeName" id="productUnit_storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="" class="form-control" isRequired="1" tipName="商家"  />
					<input type="hidden" name="storeId" id="productUnit_storeId" value="" />
			   </c:if>  
			   
			   <m:isMainStore>
			   		<span class="input-group2">商家</span>
					<input value='${store_session_key.name}' name="storeName" id="productUnit_storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="" class="form-control" isRequired="1" tipName="商家"  />
					<input type="hidden" name="storeId" id="productUnit_storeId" value="" />
			   </m:isMainStore>
			  
            <span class="input-group2">单位名称</span> 
            <input type="text" id="name" name="name" class="form-control" placeholder="单位名称" style="width: 200px;float:left;margin-right:40px;">
            	
		</div>
	</m:list>
	<m:select_store path="${contextPath}/productUnit/showModel/list/list-data" modeId="productUnitShowStore" callback="callback"> </m:select_store>
</body>
</html>