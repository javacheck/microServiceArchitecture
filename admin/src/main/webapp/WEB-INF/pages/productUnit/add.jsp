<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty productUnit ? '添加' : '修改' }商品单位</title> 
<script type="text/javascript">

$(function(){
	var isSys=${isSys};
	$("[name='storeName']").bind("click", function () {
		$('#storeAddModal').modal();
	});
	
}); 

var addflag=0;
function nameAdd(){
	addflag=addflag+1;
	$("#nameAddDiv").append('<div class="form-group " id="nameAdd'+addflag+'" >'+
									'<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商品单位名称</label>'+
									'<div class="col-md-2">'+
										'<input type="text"  name="unitName" value="" class="form-control"  maxlength="20"/>'+
									'</div>'+
									'<input type="button" class="btn btn-small btn-warning"  value="删除" onclick="nameDel(this);" />'+
								'</div>');
}
function nameDel(divName){
	addflag=addflag-1;
	$(divName).parent().remove();
}
function saveproductUnit(){
	var id=$("#id").val();
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
	
	var flag=true;
	var arrName = new Array();//创建一个数组
	$("input[name='unitName']").each(function(index,item){
		var nameVal=$.trim($(this).val());
		if($.trim($(this).val())==""){
			lm.alert("单位名称不能为空！");
			flag=false;
			return false;
		}
		lm.postSync("${contextPath}/productUnit/list/ajax/exist",{id:id,name:$.trim($(this).val()),storeId:storeId},function(data){
			if(data==1){
				lm.alert("单位名称<span style='color:red'>"+nameVal+"</span>已存在！");
				flag=false;
				return false;
			}
		});
		arrName.push($.trim($(this).val()));
	});
	if(isRepeat(arrName)==true){
		lm.alert("商品单位有重复，请检查！");
		return;
	}
	if(flag==false){
 		return;
 	}
	$("#productUnitAddBtn").prop("disabled","disabled");
	lm.post("${contextPath}/productUnit/list/ajax/save",$("#unitSave").serialize(),function(data){
		
		if(data=='1'){
	    	lm.alert("操作成功！");
	    	 window.location.href="${contextPath}/productUnit/list";
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
		$("#shopListModalBtn").click();
	});
	
}
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
			<i class='icon-plust'></i>${empty productUnit ? '添加' : '修改' }商品单位
			</strong>
		</div>
		<div class='panel-body'>
			<form method='post'  id="unitSave" >
				<div  class='form-horizontal'>
					<input id="id" name="id" type="hidden" value="${productUnit.id }" />
					   <c:if test="${isSys==true }">  
					   		<c:if test="${empty productUnit }">
								<div class="form-group" >
									<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商家</label>
									<div class="col-md-2">
										<input name="storeName" id="storeName" readonly="readonly" value="" class="form-control" isRequired="1"  />
										<input type="hidden" name="storeId" id="storeId" value="" />
									</div>
								</div>
							</c:if>
							<c:if test="${not empty productUnit }">
								<div class="form-group" >
									<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商家</label>
									<div class="col-md-2">
										<input name="" id="" readonly="readonly" value="${store.name }" class="form-control" />
									</div>
								</div>
							</c:if>
					   </c:if>  
					
					<c:if test="${empty productUnit }">
						<div id="nameAddDiv">
							<div class="form-group " id="nameAdd0">
								<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商品单位名称</label>
								<div class="col-md-2">
									<input type='text' name="unitName" value="" class='form-control'   maxlength="20"/>
								</div>
								<input type="button"  id="nameAddBtn" class="btn btn-small btn-warning"  value="添加单位名称" onclick="nameAdd();" />
							</div>
						</div>
					</c:if>
					<c:if test="${not empty productUnit }">
						<div class="form-group">
							<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商品单位名称</label>
							<div class="col-md-2">
								<input type='text' name="unitName" value="${productUnit.name }" class='form-control'   maxlength="20"/>
							</div>
						</div>
					</c:if>
					
					<div class="form-group">
						<div class="col-md-offset-1 col-md-1 0" style="float:left">
							<input type="button"  id='productUnitAddBtn' class='btn btn-primary' value="${empty productUnit ? '添加' : '修改' }" onclick="saveproductUnit()" />
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