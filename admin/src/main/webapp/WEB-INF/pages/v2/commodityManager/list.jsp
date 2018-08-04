<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>商品列表</title>
	
	<!-- ztree.core核心包 -->
	<script type="text/javascript" src="${staticPath }/js/ztree/jquery.ztree.core-3.5.js"></script>
	<!-- 升级树控件 -->
	<script type="text/javascript" src="${staticPath }/js/ztree/jquery.ztree.exhide-3.5.js"></script>
	<!-- 树形样式 -->
	<link rel="stylesheet" href="${staticPath }/css/ztree/zTreeStyle/zTreeStyle.css" type="text/css">	
	
<script type="text/javascript">

function deleteById(productId){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/commodityManager/delete/deleteById",{productId:productId},function(data){
			data = $.trim(data);
			if(data==1){
				lm.alert("删除成功！");
			}else if(data==2){
				lm.alert("商品已经销售，不能删除，只能下架处理");
			}else {
				lm.alert("删除失败！");
			}
			loadCurrentList_commodityManagerList(); // 刷新页面
		});
	});
}

var cacheArray = new Array();
function checkboxSelectEvent(obj){
	if(obj.checked){
		cacheArray.push($(obj).attr("sign"));
	} else {
		cacheArray.splice($.inArray($(obj).attr("sign"),cacheArray),1); 
	}
}

function checkPageALL(obj){
	if(obj.checked){
		$("#show_productManager_Model input:checkbox[id!='pageALLCheckBox']").each(function(key,value){
			if(-1 ==  $.inArray($(value).attr("sign") ,cacheArray) ){
				cacheArray.push($(value).attr("sign"));	
			}
		});
		$("#show_productManager_Model input:checkbox[id!='pageALLCheckBox']").prop("checked",true);
	} else {
		$("#show_productManager_Model input:checkbox[id!='pageALLCheckBox']").each(function(key,value){
			// 查询数组中是否有此数据，有则从数组中移除
			if(-1 !=  $.inArray($(value).attr("sign") ,cacheArray) ){
				// 移除指定的元素
				cacheArray.splice($.inArray($(value).attr("sign") ,cacheArray),1); 
			}
		});
		$("#show_productManager_Model input:checkbox[id!='pageALLCheckBox']").prop("checked",false);
	}
}

$(function(){
	$("#batchOperation").click(function(){
		if(cacheArray.length <= 0 ){
			lm.alert("请选择需要批量操作的数据!");
			return;
		}
		
		$("#batchOperationShowID").modal();
	});
	
	$("#batchOperationShowCancel").click(function(){
		$("#batchOperationShowClose").click();
	});
	
	$('#batchOperationShowID').on('hidden.zui.modal', function() {
		$("#showBrandId").val("-1");
		
		$("#showCategoryId").find("option").remove(); 
		
		$("input:checkbox[name='showShelves']").each(function(key,value){
			$(value).prop("checked",true);
		});
		$("input:radio[name='showWeighing']").each(function(key,value){
			if( $(value).val() == 0 ){
				$(value).prop("checked",true);				
			}
		});
		$("input:radio[name='showReturnGoods']").each(function(key,value){
			if( $(value).val() == 1 ){
				$(value).prop("checked",true);				
			}
		});
	});
	
	$("#batchOperationShowSure").click(function(){
		var showCategoryId = $("#showCategoryId").val(); // 商品分类
		var showBrandId = $("#showBrandId").val();
		var zuping = "";
		$("input[name='showShelves']").each(function(key,value){
			 if($(this).is(':checked')){
				 zuping += $(this).val();
			 }
		});
		var showShelves;
		if( zuping == "" ){ // 两个都没选中
			showShelves = 5;
		} else if( zuping.length == 1 ){
			showShelves = zuping;
		} else {
			showShelves = 4;
		}
		
		var showWeighing = $("input:radio[name='showWeighing']:checked").val();
		var showReturnGoods = $("input:radio[name='showReturnGoods']:checked").val();
		$("#productStockArray").val(cacheArray);
		
		var productStockArray = $("#productStockArray").val();
		
		var flag = false;
		
		lm.postSync("${contextPath }/commodityManager/ajax/batchOpertion/", {weighing:showWeighing,returnGoods:showReturnGoods,shelves:showShelves,categoryId:showCategoryId,brandId:showBrandId,productStockArray:productStockArray}, function(data) {
			if( data != 0 ){
				lm.alert("操作成功!");
				flag = true;				
			} 
		});
		
		if(flag){ // 修改成功
			loadCurrentList_commodityManagerList(); // 刷新页面
			$("#batchOperationShowClose").click();
			cacheArray.length = 0;
		}
	});
});

$(function(){
	// 点击弹出商家选择窗口
	$("#commodityManager_storeName").click(function (){
		$("#commodityManagerShowStore").modal();
	});
	
	$("#cancelSelect").click(function(){
		$("#commodityManager_storeName").val("");
		$("#commodityManager_storeId").val("");
	});
});

function formReset(){
	$("#commodityManager_storeName").val("");
	$("#commodityManager_storeId").val("");
	$("#categoryId").find("option").val("");
	$("#categoryId").find("option").text("全部");
}

function callback(obj){
	$("#commodityManager_storeName").val(obj.name);
	$("#commodityManager_storeId").val(obj.id);
}

$(function(){
	$("#reportcommodityManager").click(function(){
		var reportStoreId = $("#commodityManager_storeId"); // 开始时间
		if( undefined == reportStoreId || "" == reportStoreId || null == reportStoreId ){
			reportStoreId = null;
		} else {
			reportStoreId = $.trim(reportStoreId.val());  // 用jQuery的trim方法删除前后空格			
		}
		
		var reportName = $("#name").val(); // 商品名称
		reportName = $.trim(reportName);  // 用jQuery的trim方法删除前后空格
		
		var reportBarCode = $("#barCode").val(); // 商品条码
		reportBarCode = $.trim(reportBarCode);  // 用jQuery的trim方法删除前后空格
		
		var reportCategoryId = $("#categoryId").val(); // 分类名称
		reportCategoryId = $.trim(reportCategoryId);  // 用jQuery的trim方法删除前后空格
		
		var reportShelves = $("#shelves").val(); // 商品状态
		reportShelves = $.trim(reportShelves);  // 用jQuery的trim方法删除前后空格
		
		var reportBrandId = $("#brandId").val(); // 商品状态
		reportBrandId = $.trim(reportBrandId);  // 用jQuery的trim方法删除前后空格
		
		location.href = "${contextPath}/commodityManager/ajax/reportProductToExcel?reportStoreId="+reportStoreId+"&reportName="+reportName +"&reportBarCode="+reportBarCode+"&reportCategoryId="+reportCategoryId +"&reportShelves="+reportShelves+"&reportBrandId="+reportBrandId;
	});
});
	
</script>

</head>

<body>

<!-- 商品列表DIV   start --> 				            							
<m:hasPermission permissions="commodityManagerAdd" flagName="addFlag"/>
	<m:list title="商品列表" id="commodityManagerList"
		listUrl="${contextPath }/commodityManager/list-data"
		addUrl="${addFlag == true ? '/commodityManager/add' : '' }"
		searchButtonId="commodityManager_search_btn" formReset="formReset">
		
		<input type="hidden" name="productStockArray" id="productStockArray" value=""/>
		
		<div class="input-group" style="max-width:1500px;">
			<c:if test="${loginIdentityMarking == 0 || loginIdentityMarking == 1 }">
				<span class="input-group-addon">商家</span>
				<input <c:if test="${loginIdentityMarking ==  1 }"> value='${store_session_key.name}' </c:if> name="storeName" id="commodityManager_storeName" readOnly="readOnly" placeholder="请选择商家" style="width: 200px;" class="form-control" />
				<input type="hidden" name="storeId" id="commodityManager_storeId" value="" />
				<!-- 
					<input id="cancelSelect" name="cancelSelect" class="btn btn-info" type="button" style="width: auto;margin-top:5px;" value="清空选择">
				 -->	
			</c:if>
					
		 	<span class="input-group-addon">商品名称</span> 
            	<input type="text" id="name" name="name" class="form-control" placeholder="输入商品名称或拼音码" style="width: 200px;">
            	
            <span class="input-group-addon">商品条码</span> 
            	<input type="text" id="barCode" name="barCode" class="form-control" style="width: 200px;">
            	
            <span class="input-group-addon">分类名称</span>
	        	<m:treeSelect treeRoot="treeRoot" showAllOption="1" selectID="categoryId" linkURL="${contextPath }/commodityCategory/ajax/loadZtreeListNoSubordinate"></m:treeSelect>            
          <span class="input-group-addon">商品状态</span>
	            <select class="form-control" id="shelves" style="width: auto" name="shelves"> 
	            	<option value="">全部</option>
		            <option value="0">仅收银端上架</option>
		            <option value="2">仅APP端上架</option>
		            <option value="-2">收银端、APP端均上架</option>
		            <option value="-4">全部下架</option>
	            </select>
	       <span class="input-group-addon">品牌名称</span>
	            	<select class="form-control" id="brandId" name="brandId" style="width: 215px">
						<option value="">请选择品牌</option>
						<c:forEach items="${brandList }" var="brand">
							<option id="${brand.id }" value="${brand.id }">${brand.name }</option>
						</c:forEach>
					</select>
        </div>
        <a id="reportcommodityManager" class="btn btn-small btn-danger" style="width: auto;margin-top:5px;">导出</a>
        <input id="batchOperation" name="batchOperation" class="btn btn-info" type="button" style="width: auto;margin-top:5px;" value="批量操作">
	</m:list> 
<!-- 商品列表DIV   end -->  
   
   <!-- 信息详情展示start -->
		  <div class="modal fade" id="batchOperationShowID">
			 <div class="modal-dialog modal-sale-show">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" id="batchOperationShowClose" name="batchOperationShowClose" class="close" data-dismiss="modal">
							<span aria-hidden="true">×</span><span class="sr-only">关闭</span>
						</button>
						<h4 class="modal-title" style="text-align: center;">批量操作</h4>
					</div>
					   <table class="table table-hover table-striped table-bordered" cellpadding="0" cellspacing="0">
						 <tbody>
		            		<tr class="bg-co"  style="width: 100%;height: 30px">
		                    	<td width="50%"><span style="color:#176AE8">商品分类：</span><span style="right:-80px;color:red">修改商品分类为--></span></td>
		                    	<td>
		                    		 <m:treeSelect treeRoot="showTreeRoot" selectID="showCategoryId" ></m:treeSelect>
		                    	</td>
		                	</tr>
		                	<tr class="bg-co" style="width: 100%; height: 30px;">
			                	<td width="50%"><span style="color:#176AE8">商品品牌：</span><span style="right:-80px;color:red">修改商品品牌为--></span></td>
			                	<td>
				                	<select class="form-control" id="showBrandId" name="showBrandId" style="width: 215px">
										<option value="-1">请选择品牌</option>
										<option value="">无</option>
										<c:forEach items="${brandList }" var="brand">
											<option id="${brand.id }" value="${brand.id }">${brand.name }</option>
										</c:forEach>
									</select>
								</td>
							</tr>
						
		                	<tr class="bg-co" style="width: 100%; height: 30px;">
								<td width="50%"><span style="color:#176AE8">上架管理：</span></td>
								<td>
									<input type="checkbox" name="showShelves" value="0" checked="checked">&nbsp;&nbsp;收银端上架
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="checkbox" name="showShelves" value="2" checked="checked">&nbsp;&nbsp;APP端上架
								</td>
		            		</tr>
		            		
		            		<tr class="bg-co" style="width: 100%; height: 30px;">
								<td width="50%"><span style="color:#176AE8">是否称重：</span></td>
								<td>
									<input type="radio" name="showWeighing" value="1" >&nbsp;&nbsp;是
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="radio" name="showWeighing" value="0" checked="checked" >&nbsp;&nbsp;否
								</td>
		            		</tr>
		            		
		            		<tr class="bg-co" style="width: 100%; height: 30px;">
								<td width="50%"><span style="color:#176AE8">是否支持退货：</span></td>
								<td>
									<input type="radio" name="showReturnGoods" value="1" checked="checked" >&nbsp;&nbsp;是
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="radio" name="showReturnGoods" value="0" >&nbsp;&nbsp;否
								</td>
		            		</tr>
		            		
		            		<tr class="bg-co" style="width: 100%; height: 30px;">
								<td width="50%" style="text-align: center;">
									<button id="batchOperationShowCancel"  name="batchOperationShowCancel" class='btn btn-small btn-warning'>取消</button>
								</td>
								<td width="50%" style="text-align: center;">
									<button id="batchOperationShowSure"  name="batchOperationShowSure" class='btn btn-small btn-warning'>确定</button>
								</td>
		            		</tr>
		            		
            		   </tbody>
					</table>
			   </div>
			</div>
		</div>
	  <!-- 信息详情展示end -->
	  
	  <m:select_store path="${contextPath}/commodityManager/showModel/list/list-data" modeId="commodityManagerShowStore" callback="callback"> </m:select_store>
</body>

</html>