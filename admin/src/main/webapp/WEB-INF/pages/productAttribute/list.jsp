<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>属性列表</title>
<script type="text/javascript">
function callback(obj){
	$("#productAttribute_storeName").val(obj.name);
	$("#productAttribute_storeId").val(obj.id);
}
function deleteProductAttribute(id){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/productAttribute/delete/delete-by-productAttributeId",{id:id},function(data){
			if(data==0){
				lm.alert("该属性已设属性值，不能删除！");
				return;
			} else if( data==2 ){
				lm.alert("此属性关联到了多属性商品，不能删除！");
				return;
			}else{
				lm.alert("删除成功");
			}
			 window.location.href="${contextPath}/productAttribute/list";
		});
	});
}
function formReset(){
	$("#productAttribute_storeName").val("");
	$("#productAttribute_storeId").val("");
}
$(function(){
	// 点击弹出商家选择窗口
	$("#productAttribute_storeName").click(function (){
		$("#productAttributeShowStore").modal();
	});
});
//修改选择的店铺时触发
function byStoreAjaxRefreshList(){
	loadList_productAttributeList(); 
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
    var productAttributeIds="";
    $("input[name='listProductAttributeName']:checked").each(function(i){ 
    	productAttributeIds+=$(this).val()+","; 
    }); 
    productAttributeIds=productAttributeIds.substring( 0,productAttributeIds.length-1);
    if(productAttributeIds!=""){
	    lm.confirm("确定要删除吗？",function(){
			lm.post("${contextPath }/productAttribute/delete/deleteAll-by-productAttributeIds",{productAttributeIds:productAttributeIds},function(data){
				if (data != null && data!="") {
					if(data[0].split(",")[1]=="0"){
						lm.alert("属性"+data[0].split(",")[0]+"关联到了商品，不能删除！");
						return;
					}else{
						lm.alert("属性"+data[0].split(",")[0]+"已设属性值，不能删除！");
						return;
					}
				}else{
					lm.alert("删除成功！");
					loadCurrentList_productAttributeList();
				}
			});
		});
    }else{
    	lm.alert("请选择要删除的属性！");
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
	<m:hasPermission permissions="productAttributeAdd" flagName="addFlag"/>
	<m:list title="属性列表" id="productAttributeList"
		listUrl="${contextPath }/productAttribute/list/list-data" callback="callback1"
		addUrl="${addFlag == true ? '/productAttribute/add' : '' }" 
		searchButtonId="cateogry_search_btn" formReset="formReset">
		
		<div class="input-group" style="max-width:1500px;">
			   <c:if test="${isSys==true }">  
			   		<span class="input-group2">商家</span>
					<input name="storeName" id="productAttribute_storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="" class="form-control" isRequired="1" tipName="商家"  />
					<input type="hidden" name="storeId" id="productAttribute_storeId" value="" />
			   </c:if>  
			  
            <span class="input-group2">属性名称</span> 
            <input type="text" id="name" name="name" class="form-control" placeholder="名称" style="width: 200px;float:left;margin-right:40px;">
            	
		</div>
	</m:list>
	<m:select_store path="${contextPath}/productAttribute/showModel/list/list-data" modeId="productAttributeShowStore" callback="callback"> </m:select_store>
</body>
</html>