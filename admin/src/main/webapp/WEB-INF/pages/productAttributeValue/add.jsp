<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty productAttributeValue ? '添加' : '修改' }商品属性值</title> 
<script type="text/javascript">
$(function(){
	var isSys=${isSys};
	var isMainStore=${isMainStore};
	var type="${empty productAttribute ? 0:1}";
	if(type!="0"){
		var categoryId="${productAttribute.categoryId}";
		var storeId="${store.id}";
		lm.post("${contextPath}/productAttribute/list/ajax/byStoreId",{storeId:storeId},function(data){
			for(var i=0;i<data.length;i++){
				$("#cacheCategoryId").append('<option  value="' + data[i].id + '">' + data[i].name + '</option>');
				$("#cacheCategoryId").find("option[value='"+categoryId+"']").attr("selected",true);
				$("#cacheCategoryId").attr("disabled","disabled");
			}
		});
		getCategoryId(categoryId);
	}else{
		if(isSys==false){
			var storeId="${isStoreId}";
			lm.post("${contextPath}/productAttribute/list/ajax/byStoreId",{storeId:storeId},function(data){
				for(var i=0;i<data.length;i++){
					$("#cacheCategoryId").append('<option  value="' + data[i].id + '">' + data[i].name + '</option>');
				}
			});
		}
	}
	$("[name='storeName']").bind("click", function () {
		$('#storeAddModal').modal();
	});
	
}); 
function getCategoryId(categoryId){
	var attributeIdCache=$("#attributeId").val();
    if(categoryId == ""){
       $("#childid").empty();
       $("#childid").append("<option value='' >请选择商品属性</option>");
       return ;
    }
    $("#childid").empty();
    lm.post("${contextPath}/productAttributeValue/list/ajax/list-by-categoryId",{categoryId:categoryId},function(data){
    	$("#childid").append("<option value='' >请选择商品属性</option>");
    	for(var i=0;i<data.length;i++){
    		 $("#childid").append('<option id="' + data[i].id + '" value="' + data[i].id + '">' + data[i].name + '</option>');
    		 if(attributeIdCache!=null && attributeIdCache!=""){
    				$("#childid").find("option[value='"+attributeIdCache+"']").attr("selected",true);//productAttributeValue表里的attributeId和(通过categoryid查询)商品属性表里的id相同，选中
    				$("#childid").attr("disabled","disabled");
    			}
		}
	});
}		

var addflag=0;
function valueAdd(){
	addflag=addflag+1;
	$("#nameAddDiv").append('<div class="form-group ">'+
									'<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商品属性值</label>'+
									'<div class="col-md-2">'+
										'<input type="text"  name="value" value="" class="form-control"  maxlength="120"/>'+
									'</div>'+
									'<input type="button" class="btn btn-small btn-warning"  value="删除" onclick="valueDel(this);" />'+
							'</div>');
}
function valueDel(divName){
	addflag=addflag-1;
	$(divName).parent().remove();
}
//获取商品属性id
function getChildrenList1(){
	var childid=$("#childid").val();
	if(childid!=null){
   	 $("#attributeId").val(childid);
    }
}
function saveproductAttributeValue(){
	
	var id=$("#id").val();//商品属性值ID
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
	var categoryId=$("#cacheCategoryId").find("option:selected").val();
	
	if(categoryId==null || categoryId==""){
		lm.alert("分类不能为空！");
		return;
	}
	var attributeId=$("#childid").find("option:selected").val();//商品属性ID
	
	if(attributeId==""||attributeId==null){
		lm.alert("商品属性不能为空！");
		return;
	}
	var flag=true;
	var arrValue = new Array();//创建一个数组
	$("input[name='value']").each(function(index,item){
		var value=$.trim($(this).val());
		if($.trim($(this).val())==""){
			lm.alert("商品属性值不能为空！");
			flag=false;
			return false;
		}
		lm.postSync("${contextPath}/productAttributeValue/list/ajax/exist",{id:id,value:$.trim($(this).val()),attributeId:attributeId},function(data){
			if(data==1){
				noty("商品属性值<span style='color:red;'>"+value+"</span>已存在！");
				flag=false;
				return false;
			}
		});
		arrValue.push($.trim($(this).val()));
	});
	if(isRepeat(arrValue)==true){
		lm.alert("商品属性值有重复，请检查！");
		flag=false;
	}
	if(flag==false){
 		return;
 	}
	$("#productAttributeValueAddBtn").prop("disabled","disabled");
	lm.post("${contextPath}/productAttributeValue/list/ajax/save",$("#attributeValueSave").serialize(),function(data){
		if(data=='1'){
	    	lm.alert("操作成功！");
	    	 window.location.href="${contextPath}/productAttributeValue/list";
		}
	}); 
}
function isRepeat(arr){
	var hash = {};   
	for(var i in arr) {    
		if(hash[arr[i]])
		return true;      
		hash[arr[i]] = true;  
		      
	}  
	return false;
}

function callback(){
	
	$("#shopListDataId").find("tbody tr").click(function(){
		$("#storeId").val($(this).attr("val"));
		$("#storeName").val(($($(this).find("td")[0]).html()));
		lm.post("${contextPath}/productAttribute/list/ajax/byStoreId",{storeId:$("#storeId").val()},function(data){
			if(data.length>0){
				$("#cacheCategoryId").empty();
				$("#cacheCategoryId").append("<option value='' >请选择分类</option>");
				for(var i=0;i<data.length;i++){
					$("#cacheCategoryId").append('<option  value="' + data[i].id + '">' + data[i].name + '</option>');
				}
			}else{
				lm.alert("商家分类为空！");
			}
		});
		$("#shopListModalBtn").click();
	});
	
}
function getChildrenList1(){
	getCategoryId($("#cacheCategoryId").find("option:selected").val());
}
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
			<i class='icon-plust'></i>${empty productAttributeValue ? '添加' : '修改' }商品属性值
			</strong>
		</div>
		<div class='panel-body'>
			<form method='post'  id="attributeValueSave" >
				<div  class='form-horizontal'>
					<input id="id" name="id" type="hidden" value="${productAttributeValue.id }" />
					<input id="attributeId" name="attributeId" type="hidden" value="${productAttributeValue.attributeId }" />
					   <c:if test="${isSys==true }">  
					   		<c:if test="${empty productAttributeValue }">
								<div class="form-group" >
									<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商家</label>
									<div class="col-md-2">
										<input name="storeName" id="storeName" readonly="readonly" value="" class="form-control" isRequired="1"  />
										<input type="hidden" name="storeId" id="storeId" value="" />
									</div>
								</div>
							</c:if>
							<c:if test="${not empty productAttributeValue }">
								<div class="form-group" >
									<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商家</label>
									<div class="col-md-2">
										<input name="" id="" readonly="readonly" value="${store.name }" class="form-control" />
									</div>
								</div>
							</c:if>
					   </c:if>  
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>所属分类</label>
						<div class="col-md-2">
							<select  class="form-control"  id="cacheCategoryId" name="cacheCategoryId" onchange="getChildrenList1();" >
								<option value="">请选择分类</option>	
							</select>
						</div>
					</div>
					<div class="form-group">
		                <label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商品属性</label>
		                    <div class="col-md-1">
								<select name="childid" class="form-control" style="width: 130px;" id="childid">
									<option>请选择商品属性</option>
								</select>
							 </div>
					</div>
					<c:if test="${empty productAttributeValue }">
							<div id="nameAddDiv">
								<div class="form-group ">
									<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商品属性值</label>
									<div class="col-md-2">
										<input type='text' name="value" value="" class='form-control'   maxlength="120"/>
									</div>
									<input type="button"  id="nameAddBtn" class="btn btn-small btn-warning"  value="添加属性值" onclick="valueAdd();" />
								</div>
							</div>
						</c:if>
						<c:if test="${not empty productAttributeValue }">
							<div class="form-group">
								<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商品属性值</label>
								<div class="col-md-2">
									<input type='text' name="value" value="${productAttributeValue.value }" class='form-control'   maxlength="120"/>
								</div>
							</div>
						</c:if>
					<div class="form-group">
						<div class="col-md-offset-2 col-md-1 0">
							<input type="button"  id='productAttributeValueAddBtn' class='btn btn-primary' value="${empty productAttributeValue ? '添加' : '修改' }" onclick="saveproductAttributeValue()" />
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