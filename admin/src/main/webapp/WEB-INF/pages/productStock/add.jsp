<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty product ? '添加' : '修改' }商品</title> 
<script type="text/javascript">

$(function(){
	var isSys=${isSys};
	var isMainStore=${isMainStore};
	$("[name='storeName']").bind("click", function () {
		$('#storeAddModal').modal();
	});
	
	var product="${empty product ? 0:1}";
	if(product!="0"){
		var categoryId="${product.categoryId}";
		var storeId="${product.storeId}";
		var brandId="${product.brandId}";
		lm.post("${contextPath}/productAttribute/list/ajax/byStoreId",{storeId:storeId},function(data){
			for(var i=0;i<data.length;i++){
				$("#cacheCategoryId").append('<option  value="' + data[i].id + '">' + data[i].name + '</option>');
				$("#cacheCategoryId").find("option[value='"+categoryId+"']").attr("selected",true);
				$("#cacheCategoryId").attr("disabled","disabled");
			}
		});
		lm.post("${contextPath}/productBrand/list/ajax/byStoreId",{storeId:storeId},function(data){
			for(var i=0;i<data.length;i++){
				$("#cacheBrandId").append('<option  value="' + data[i].id + '">' + data[i].name + '</option>');
				$("#cacheBrandId").find("option[value='"+brandId+"']").attr("selected",true);
				//$("#cacheBrandId").attr("disabled","disabled");
			}
		});
	}else{
		if(isSys==false){
			var storeId="${isStoreId}";
			lm.post("${contextPath}/productAttribute/list/ajax/byStoreId",{storeId:storeId},function(data){
				for(var i=0;i<data.length;i++){
					$("#cacheCategoryId").append('<option  value="' + data[i].id + '">' + data[i].name + '</option>');
				}
			});
			lm.post("${contextPath}/productBrand/list/ajax/byStoreId",{storeId:storeId},function(data){
				for(var i=0;i<data.length;i++){
					$("#cacheBrandId").append('<option  value="' + data[i].id + '">' + data[i].name + '</option>');
				}
			});
		}
	}
	var typeCache="${product.type}";
	if(typeCache=="0"){
		$("input[name='type']").eq(0).prop("checked",true);//有属性
	}
	var exitStockType="${exitStockType}";
	
	$("[name = 'type']").bind("click", function () {
		if(exitStockType=="" || exitStockType==null){
			if($("[name=sysCategoryId]").val()=="" && $("[name='storeCategoryId']").val()==""){
				lm.alert("请选择分类！");
				$("input[name='type']").eq(1).prop("checked",true);
				return;
			}else{
				var categoryId=$("#cacheCategoryId").find("option:selected").val();
				lm.post("${contextPath}/productStock/list/ajax/productAttributeList",{categoryId:categoryId},function(data){
					if(data.length==0){
						lm.alert("该分类下没有属性！");
						$("input[name='type']").eq(1).prop("checked",true);
						return;
					}
				}); 
			}
		}else if(exitStockType=="0"){//表示该商品已有库存
			if(typeCache=="0"){//有属性
				lm.alert("商品下已有库存，不能修改成无属性！");
				$("input[name='type']").eq(0).prop("checked",true);//有属性
				return;
			}else{//无属性
				if($('input:radio[name="type"]:checked').val()=="0"){
					lm.confirm("商品下已存在无属性的库存，修改成有属性商品，无属性库存将会下架,是否要修改成有属性？",function(){
						$("input[name='type']").eq(0).prop("checked",true);
					},function(){
						$("input[name='type']").eq(1).prop("checked",true);//无属性
					});
				}
			}
		}else{
			if($("#cacheCategoryId option:selected").val()=="" && $("#cacheCategoryId option:selected").val()==null){
				lm.alert("请选择分类！");
				$("input[name='type']").eq(1).prop("checked",true);
				return;
			}else{
				var categoryId=$("#cacheCategoryId option:selected").val();
				lm.post("${contextPath}/productStock/list/ajax/productAttributeList",{categoryId:categoryId},function(data){
					if(data.length==0){
						lm.alert("该分类下没有属性！");
						$("input[name='type']").eq(1).prop("checked",true);
						return;
					}
				}); 
			}
		}
	  }); 
}); 



function saveProduct(){
		
	    var id= $("#id").val();// 商品ID
	    var storeId=$("#storeId").val();
		var isSys=${isSys};
		var isMainStore=${isMainStore};
		if(isSys){
			if(id==null || id==""){
				if(storeId==null || storeId=="" || storeId==-1){
					lm.alert("商店不能为空！");
					return;
				}
			}
		}else{
			storeId="${isStoreId}";
		}
		var name = $("#name").val(); // 商品名称
		var oldName= $("#oldName").val();// 旧商品名称
		if( name == "" || name == null ){
			lm.alert("商品名称不能为空!");
			return;
		}
		
		var categoryId=$("#cacheCategoryId").find("option:selected").val();
		if(categoryId==null || categoryId==""){
			lm.alert("分类不能为空！");
			return;
		}
		var brandId=$("#cacheBrandId").find("option:selected").val();
		var type=$('input:radio[name="type"]:checked').val();
		var flag = true;
		if($('input:radio[name="type"]:checked').val()==0){
			lm.postSync("${contextPath}/productStock/list/ajax/productAttributeList",{categoryId:categoryId},function(data){
				if(data.length==0){
					lm.alert("该分类下没有属性！");
					$("input[name='type']").eq(1).prop("checked",true);
					flag = false;
					return;
				}
			});
		}
		
		if($("#id").val() != ""){ // 修改时
			lm.postSync("${contextPath}/productStock/list/ajax/existProduct",{id:id,name:name,storeId:storeId,categoryId:categoryId},function(data){
				if(data == 1){
					lm.alert("已存在此商品！");
					flag = false;
					return ;				
				}
			});	
		} else { // 新增时
			lm.postSync("${contextPath}/productStock/list/ajax/existProduct",{id:id,name:name,type:type,storeId:storeId,categoryId:categoryId},function(data){
				if(data == 1){
					lm.alert("已存在此商品！");
					flag = false;
					return ;				
				}
			});	
		}
		if(!flag){
			return;
		}
		$("#productAddBtn").prop("disabled","disabled");
		if(flag){
			lm.post("${contextPath}/productStock/save",{id:id,storeId:storeId,name:name,type:type,categoryId:categoryId,oldName:oldName,brandId:brandId},function(data){
				if(data=='1'){
			    	lm.alert("操作成功！");
			    	 window.location.href="${contextPath}/productStock/list";
				}
			}); 
		}
}
function callback(){
	
	$("#shopListDataId").find("tbody tr").click(function(){
		$("#storeId").val($(this).attr("val"));
		$("#storeName").val(($($(this).find("td")[0]).html()));
		lm.post("${contextPath}/productAttribute/list/ajax/byStoreId",{storeId:$("#storeId").val()},function(data){
			if(data.length>0){
				for(var i=0;i<data.length;i++){
					$("#cacheCategoryId").append('<option  value="' + data[i].id + '">' + data[i].name + '</option>');
				}
			}else{
				lm.alert("商家分类为空！");
			}
		});
		lm.post("${contextPath}/productBrand/list/ajax/byStoreId",{storeId:$("#storeId").val()},function(data){
			if(data.length>0){
				for(var i=0;i<data.length;i++){
					$("#cacheBrandId").append('<option  value="' + data[i].id + '">' + data[i].name + '</option>');
				}
			}
		});
		$("#shopListModalBtn").click();
	});
	
}
</script>
</head>
<body>
	<div class='panel' callback="infoButtonFun">
		<div class='panel-heading'>
			<strong>
			<i class='icon-plust'></i>${empty product ? '添加' : '修改' }商品
			</strong>
		</div>
		<div class='panel-body'>
			<form method='post'  id="productSave" action="${contextPath}/productStock/save">
				<div  class='form-horizontal'>
					<input id="id" name="id" type="hidden" value="${product.id }" />
					<input id="oldName" name="oldName" type="hidden" value="${product.name }" />
					<input id="categoryIdCache" name="categoryIdCache" type="hidden" value="${product.categoryId }" />
					   <c:if test="${isSys==true }">  
					   		<c:if test="${empty product }">
								<div class="form-group" >
									<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商家</label>
									<div class="col-md-2">
										<input name="storeName" id="storeName" readonly="readonly" value="" class="form-control" isRequired="1"  />
										<input type="hidden" name="storeId" id="storeId" value="" />
									</div>
								</div>
							</c:if>
							<c:if test="${not empty product }">
								<div class="form-group" >
									<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商家</label>
									<div class="col-md-2">
										<input name="" id="" readonly="readonly" value="${product.storeName }" class="form-control" />
									</div>
								</div>
							</c:if>
					   </c:if>  
					
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商品名称</label>
						<div class="col-md-2">
							<input type="text" id="name" name="name" value="${product.name }" class='form-control' maxlength="120"/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>所属分类</label>
						<div class="col-md-2">
							<select  class="form-control"  id="cacheCategoryId" name="cacheCategoryId">
								<option value="">请选择分类</option>	
							</select>
						</div>
					</div>	
					
					<div class="form-group">
						<label class="col-md-1 control-label">所属品牌</label>
						<div class="col-md-2">
							<select  class="form-control"  id="cacheBrandId" name="cacheBrandId">
								<option value="">请选择品牌</option>	
							</select>
						</div>
					</div>
							
					<div class="form-group">
						<label class="col-md-1 control-label">是否有属性</label>
						<div class="col-md-2">
							<input id="account_type_0"  type='radio' value="0" name="type" />有属性
							<input id="account_type_1"  type='radio' value="1" name="type" checked="checked"/>无属性
						</div>
					</div>
					
					
					<div class="form-group">
						<div class="col-md-offset-1 col-md-1 0" style="float:left">
							<input type="button"  id='productAddBtn' class='btn btn-primary' value="${empty product ? '添加' : '修改' }" onclick="saveProduct();" />
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
	<!-- 模态窗 -->
	<div class="modal fade" id="storeAddModal">
		<div class="modal-dialog modal-lg" style="width: 1200px;">
		  <div class="modal-content">
			    <div class="modal-header">
			      <button type="button" class="close" data-dismiss="modal" id="shopListModalBtn"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
			      <h4 class="modal-title"></h4>
			    </div>
				<div class="modal-body">
					<m:list title="商家列表" id="productStockList" listUrl="${contextPath }/productStock/shopList/list-data" callback="callback" searchButtonId="cateogry_search_btn" >
					
					<div class="input-group" style="max-width: 1500px;">
						<c:if test="${isSys==true }">
							 <span class="input-group-addon">商家名称</span> 
			            	 <input type="text" id="name" name="name" class="form-control" placeholder="商家名称" style="width: 200px;">
				     	</c:if>       	
				         <span class="input-group-addon">手机号码</span> 
		            	 <input type="text" id="mobile" name="mobile" class="form-control" placeholder="手机号码" style="width: 200px;">
					</div>
				</m:list>
				</div>
			</div>
		</div>
	</div>
	<!-- 模态窗 -->
</body>
</html>