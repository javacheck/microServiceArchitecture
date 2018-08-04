<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty print ? '添加' : '修改' }打印机</title> 
<script type="text/javascript">

$(function(){
	var isSys=${isSys};
	$("[name='storeName']").bind("click", function () {
		$('#storeAddModal').modal();
	});
	var storeIdCache="${print.storeId}";
	var categoryIdsCache="${print.categoryIds}";
	if(storeIdCache!=null){//
		var categoryIdsArr=categoryIdsCache.split(",");
		lm.post("${contextPath}/print/findCategoryList",{"storeId":storeIdCache},function(data){
			var flag=true;
			if(data.length>0){
				for(var i=0;i<data.length;i++){ 
					for(var j=0;j<categoryIdsArr.length;j++){
						if(categoryIdsArr[j]==data[i].id){
							$("#categoryCheckBox").append("<input type='checkbox'  checked='checked' name='categoryIds' valueName= '"+data[i].name+"' value='"+data[i].id+"' > "+data[i].name+"    ");
							flag=true;
							break;
						}else{
							flag=false;
						}		
					}
					if(flag==false){
						$("#categoryCheckBox").append("<input type='checkbox'  name='categoryIds' valueName= '"+data[i].name+"' value='"+data[i].id+"' > "+data[i].name+"    ");
						flag=true;
					}
				}
				$("#printCategory").show();
			}
		});
	}//
}); 

function savePrint(){
	
	var id=$("#id").val();
	var storeId=$("#storeId").val();
	if(storeId==null || storeId==""){
		lm.alert("商店不能为空！");
		return;
	}
	var printSn=$.trim($("[name='printSn']").val());
	var printKey=$.trim($("[name='printKey']").val());
	if($.trim(printSn)==null || $.trim(printSn)==""){
		lm.alert("打印机编号不能为空！");
		return;
	}
	if($.trim(printKey)==null || $.trim(printKey)==""){
		lm.alert("打印机KEY不能为空！");
		return;
	}
	var printName=$.trim($("#printName").val());
	var memo=$.trim($("#memo").val());
	var categoryIds="";
	var categoryNames="";
	 $("input[name='categoryIds']").each(function(){
	    if ($(this).prop("checked")) {
	    	categoryIds+=$(this).attr('value') +",";
	    	categoryNames+=$(this).attr('valueName')+",";
	    }
	 });
	 categoryIds=categoryIds.substring( 0,categoryIds.length-1);
	 categoryNames=categoryNames.substring( 0,categoryNames.length-1);
	if(categoryIds==""){
		lm.alert("至少选择一个分类！");
		return;
	}
	
	
	var flag=true;
	lm.postSync("${contextPath}/print/list/ajax/exist",{id:id,printSn:printSn,printKey:printKey},function(data){
		if(data==1){
			lm.alert("该编号和KEY的打印机已存在！");
			flag=false;
			return false;
		}
	});
	
	if(flag==false){
 		return;
 	}
	$("#printAddBtn").prop("disabled","disabled");
	lm.post("${contextPath}/print/list/ajax/save",{id:id,storeId:storeId,printSn:printSn,
		printKey:printKey,categoryIds:categoryIds,categoryNames:categoryNames,printName:printName,memo:memo},function(data){
		
		if(data=='1'){
	    	lm.alert("操作成功！");
	    	 window.location.href="${contextPath}/print/list";
		}
	}); 
		
}



function callback(){
	
	$("#shopListDataId").find("tbody tr").click(function(){
		$("#categoryCheckBox").empty();
		$("#storeId").val($(this).attr("val"));
		$("#storeName").val(($($(this).find("td")[0]).html()));
		$("#shopListModalBtn").click();
		lm.post("${contextPath}/print/findCategoryList",{"storeId":$("#storeId").val()},function(data){
			if(data.length>0){
				for(var i=0;i<data.length;i++){ 
					$("#categoryCheckBox").append("<input type='checkbox'  name='categoryIds' valueName= '"+data[i].name+"' value='"+data[i].id+"' > "+data[i].name+"    ");
				}
				$("#printCategory").show();
			}else{
				lm.alert("该商家下没有分类，请先添加分类！");
				$("#printCategory").hide();
				$("#storeId").val("");
				$("#storeName").val("");
			}
		});
	});
	
}
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
			<i class='icon-plust'></i>${empty print ? '添加' : '修改' }打印机
			</strong>
		</div>
		<div class='panel-body'>
			<form method='post' id="printSave">
				<div  class='form-horizontal'>
					<input id="id" name="id" type="hidden" value="${print.id }" />
					<c:if test="${isSys==true }">
						<div class="form-group" >
							<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商家</label>
							<div class="col-md-2">
								<input name="storeName" id="storeName" readonly="readonly" value="${print.storeName }" class="form-control" isRequired="1"  />
								<input type="hidden" name="storeId" id="storeId" value="${print.storeId }" />
							</div>
						</div>
					</c:if>
					<div class="form-group">
						<label class="col-md-1 control-label">打印机名称</label>
						<div class="col-md-2">
							<input type='text'  id="printName" name="printName" value="${print.printName }" class='form-control'   maxlength="100"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>打印机编号</label>
						<div class="col-md-2">
							<input type='text'  name="printSn" value="${print.printSn }" class='form-control'   maxlength="10"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>打印机KEY</label>
						<div class="col-md-2">
							<input type='text' name="printKey" value="${print.printKey }" class='form-control'   maxlength="10"/>
						</div>
					</div>
					<div class="form-group" style="display: none;" id="printCategory">
						<label class="col-md-1 control-label">打印分类</label>
						<div class="col-md-4">
						 	<div  id="categoryCheckBox"></div>
			            </div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label">备注</label>
						<div class="col-md-2">
							<textarea id='memo' name='memo' value='' alt='最大输入字数为200个字符' cols=15 rows=5 class='form-control' maxlength='200'>${print.memo }</textarea>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-offset-1 col-md-1 0" style="float:left">
							<input type="button"  id='printAddBtn' class='btn btn-primary' value="${empty print ? '添加' : '修改' }" onclick="savePrint()" />
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