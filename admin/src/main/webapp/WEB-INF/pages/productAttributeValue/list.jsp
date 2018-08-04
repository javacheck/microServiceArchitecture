<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>属性值列表</title>
<script type="text/javascript">
function callback(obj){
	$("#productAttributeValue_storeName").val(obj.name);
	$("#productAttributeValue_storeId").val(obj.id);
}
function deleteProductAttributeValue(id){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/productAttributeValue/delete/delete-by-productAttributeValueId",{id:id},function(data){
			if(data==1){
				lm.alert("删除成功");
			} else if(data==0){
				lm.alert("此属性值关联到了商品，不能删除！");
				return ;
			}
			 window.location.href="${contextPath}/productAttributeValue/list";
		});
	});
}
function formReset(){
	$("#productAttributeValue_storeName").val("");
	$("#productAttributeValue_storeId").val("");
}

$(function(){
	// 点击弹出商家选择窗口
	$("#productAttributeValue_storeName").click(function (){
		$("#productAttributeValueShowStore").modal();
	});
});
//修改选择的店铺时触发
function byStoreAjaxRefreshList(){
	loadList_productAttributeValueList(); 
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
    var productAttributeValueIds="";
    $("input[name='listProductAttributeValueName']:checked").each(function(i){ 
    	productAttributeValueIds+=$(this).val()+","; 
    }); 
    productAttributeValueIds=productAttributeValueIds.substring( 0,productAttributeValueIds.length-1);
    if(productAttributeValueIds!=""){
	    lm.confirm("确定要删除吗？",function(){
			lm.post("${contextPath }/productAttributeValue/delete/deleteAll-by-productAttributeValueIds",{productAttributeValueIds:productAttributeValueIds},function(data){
				if (data != null && data!="") { 
					lm.alert("属性值:"+data[0].value+"关联到了商品，不能删除！");
					return;
				}else{
					lm.alert("删除成功！");
					loadCurrentList_productAttributeValueList();
				}
			});
		});
    }else{
    	lm.alert("请选择要删除的属性值！");
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
	<m:hasPermission permissions="productAttributeValueAdd" flagName="addFlag"/>
	<m:list title="属性值列表" id="productAttributeValueList"
		listUrl="${contextPath }/productAttributeValue/list/list-data" callback="callback1"
		addUrl="${addFlag == true ? '/productAttributeValue/add' : '' }"
		searchButtonId="cateogry_search_btn" formReset="formReset">
		<div class="input-group" style="max-width:1500px;">
			   <c:if test="${isSys==true }">  
			   		<span class="input-group2">商家</span>
					<input name="storeName" id="productAttributeValue_storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="${store.name }" class="form-control" isRequired="1" tipName="商家" />
					<input type="hidden" name="storeId" id="productAttributeValue_storeId" value="${store.storeId }" />
			   </c:if>  
			   
            <span class="input-group2">属性值名称</span> 
            <input type="text" id="name" name="name" class="form-control" placeholder="名称" style="width: 200px;float:left;margin-right:40px;">
            	
		</div>
	</m:list>
	<m:select_store path="${contextPath}/productAttributeValue/showModel/list/list-data" modeId="productAttributeValueShowStore" callback="callback"> </m:select_store>
</body>
</html>