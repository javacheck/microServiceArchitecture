<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty purchaseAcceptance ? '添加' : '修改' }验收单</title> 
<script src="${contextPath}/static/js/uploadPreview.js" type="text/javascript"></script>
<style type="text/css">
input.file{
    vertical-align:middle;
    position:relative;
    left:-218px;
    filter:alpha(opacity=0);
    opacity:0;
	z-index:1;
	*width:223px;
}
</style>
<script type="text/javascript">

$(function(){
	$("#status_0").click(function () {
		 if($(this).prop("checked")){
			 $("#_storageNumberDiv").hide();
		 }
	 });
	
	$("#status_1").click(function () {
		 if($(this).prop("checked")){
			 $("#_storageNumberDiv").show();
		 }
	 });
	
	new uploadPreview({
		UpBtn : "paImageId",
		DivShow : "purchaseAcceptanceImagediv",
		ImgShow : "showpurchaseAcceptanceImage",
		Width : 330,
		height : 220
	});
	
	if( null != $("#id").val() && "" != $("#id").val() ){ // 修改
		// 展示已经添加了的图片
		var imageId = $("#imageId");
		if( undefined != imageId && null != imageId.val() && "" != imageId.val() ){
			$("#purchaseAcceptanceImagediv").show();
			$("#showpurchaseAcceptanceImage").attr("src","${picUrl }"+imageId.val() );
		}
		$("#status_0").prop("checked",true);
		$("#_storageNumberDiv").hide();
	}
	
	
	
	// 模拟点击
   $("[name = uppurchaseAcceptanceImage]").bind("click", function () {
	   $("[name = paImageId]").click();
   });
   
// 图片赋值
   $("[name = paImageId]").bind("change", function () {
	   var fileName = $("#paImageId").val();
	   if( "" == fileName ){
		   $("#imageIdCache").val("");
		   $("#purchaseAcceptanceImagediv").hide();
	   } else {
		   $("#imageIdCache").val(fileName);
		   $("#purchaseAcceptanceImagediv").show();
	   }
   })
   $("[name = '_storageNumber']").bind("click", function () {
		$('#productStorageRecordModal').modal();
	});    
   // 提交事件
   $("#purchaseAcceptanceAddBtn").click(function(){
	   var id=$("#id").val();
	   var purchaseNumber=$("[name='purchaseNumber']").val();
	   purchaseNumber = $.trim(purchaseNumber);
	   if( null == purchaseNumber || "" == purchaseNumber){
		   lm.alert("采购单号不能为空！");
		   $("[name='purchaseNumber']").focus();
		   return ;
	   }
	   var imageId = $("#imageIdCache").val(); // 图片
	   if( null == imageId || "" == imageId){
		   lm.alert("上传采购单图片不能为空！");
		   return ;
	   }
	   var _storageNumber=$("#_storageNumber").val();
	  var status= $("input:radio[name='status']:checked").val();
	   if(status==1){
		   if( null == _storageNumber || "" == _storageNumber || _storageNumber=='请选择入库单号'){
			   lm.alert("入库单号不能为空！");
			   return ;
		   }
	   }else{
		   $("#_storageNumber").val(""); 
	   }
	   var flag=true;
		lm.postSync("${contextPath}/purchaseAcceptance/list/ajax/exist",{id:id,purchaseNumber:purchaseNumber},function(data){
			if(data == 1){
				lm.alert("该采购单号已存在！");
				flag = false;
				return ;				
			}
		});	
		if(flag){
		   $("#purchaseAcceptanceAddBtn").prop("disabled",true);
		   $("#purchaseAcceptanceAddForm").submit();
		}
   });
	   
});
function storageListDataIdCallback(){
	$("#storageListDataId").find("tbody tr td[valueName='a']").click(function(){
		$("#_storageNumber").val(($($(this).parent().find("td")[1]).html()));
		$("#storageListModalBtn").click();
	});
	
} 
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>${empty purchaseAcceptance ? '添加' : '修改' }验收单
			</strong>
		</div>
		<div class='panel-body'>
			<form id="purchaseAcceptanceAddForm" method='post' class='form-horizontal' action="${contextPath }/purchaseAcceptance/edit" enctype="multipart/form-data">
			
				<input id="id" name="id" type="hidden" value="${purchaseAcceptance.id }" />
				<c:if test="${empty purchaseAcceptance}">
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>采购单号</label>
						<div class="col-md-2">
							<input type='text' name="purchaseNumber" value="" class='form-control'   maxlength="120"/>
						</div>
					</div>
				</c:if>
				<c:if test="${not empty purchaseAcceptance}">
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>采购单号</label>
						<div class="col-md-2">
							<input type='text' name="purchaseNumber" value="${purchaseAcceptance.purchaseNumber }" class='form-control'  disabled="disabled" maxlength="120"/>
						</div>
					</div>
				</c:if>
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>上传采购单图片</label>
					<div class="col-md-2">
						<input type="button" id="uppurchaseAcceptanceImage" class="btn btn-large btn-block btn-default" name="uppurchaseAcceptanceImage" value="上传采购单图片"/>&nbsp;&nbsp;
						<input type="file" id="paImageId" style="width: 1px; height:1px"name="paImageId" class="file"  value="上传采购单图片" title="上传采购单图片"/>
						<input type='hidden' id="imageId" name="imageId" class='form-control' value="${purchaseAcceptance.imageId }"/>
						<input type="hidden" id="imageIdCache" name="imageIdCache" value="${purchaseAcceptance.picUrl }"/>
						<div id="purchaseAcceptanceImagediv">
							<img alt="图片展示"  id="showpurchaseAcceptanceImage" name="showpurchaseAcceptanceImage" src="" >
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label">状态</label>
					<div class="col-md-2" style="margin-top: 3px;">
						<input id="status_1"  type='radio' value="1" name="status" checked="checked"/>已入库&nbsp;&nbsp;&nbsp;
						<input id="status_0"  type='radio' value="0" name="status"/>未入库
					</div>
				</div>
				<div class="form-group" id="_storageNumberDiv">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>入库单号</label>
					<div class="col-md-1">
						<input name="_storageNumber" id="_storageNumber" style="width: 200px;text-align: center;margin-right:40px;"  readonly="readonly" value="请选择入库单号" class="form-control" isRequired="1" tipName="请选择入库单号"  />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label">备注</label>
					<div class="col-md-2">
						<textarea id='memo' name='memo' value='' alt='最大输入字数为200个字符' cols=15 rows=5 class='form-control' maxlength='200'>${purchaseAcceptance.memo }</textarea>
					</div>
				</div>
			
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button"  id='purchaseAcceptanceAddBtn' class='btn btn-primary' value="${empty purchaseAcceptance ? '添加' : '修改' }" data-loading='稍候...' />
					</div>
				</div>
			</form>
			<!-- 模态窗 -->
			<div class="modal fade" id="productStorageRecordModal">
				<div class="modal-dialog modal-lg" style="width: 1200px;">
				  <div class="modal-content">
					    <div class="modal-header">
					      <button type="button" class="close" data-dismiss="modal" id="storageListModalBtn"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
					      <h4 class="modal-title"></h4>
					    </div>
						<div class="modal-body">
							<m:list title="入库记录" id="storageStockList" listUrl="${contextPath }/purchaseAcceptance/storageList/list-data" callback="storageListDataIdCallback"  searchButtonId="productStorageRecord_search_btn" >
								<div class="input-group" style="max-width: 1500px;">
									 <span class="input-group-addon">入库单号</span> 
					            	 <input type="text" id="storageNumber" name="storageNumber" class="form-control" placeholder="入库单号" style="width: 200px;">
					            	 <span class="input-group-addon">操作人</span> 
					            	 <input type="text" id="mobile" name="mobile" class="form-control" placeholder="操作人" style="width: 200px;">
								</div>
							</m:list>
						</div>
					</div>
				</div>
			</div>
			<!-- 模态窗 -->
	  </div>
   </div>
</body>
</html>