<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty payChannelInfo ? '添加' : '修改' }支付渠道</title> 
<script type="text/javascript">
$(function(){
	// 修改时判断营业状态
	$("input:radio[name='type']").each(function(key,value){
		 if( $("#typeCache").val() == $(value).val() ){
			 $(value).attr("checked",true);
			 return false;
		 }			
	});
	$("[name='storeName']").bind("click", function () {
		$('#storeAddModal').modal();
	});
	
});
function savepayChannelInfo(){
	$("#payChannelInfoSave").submit();
	return;
	var id=$("#id").val();
	var storeId=$("#storeId").val();
	if(storeId==null || storeId==''){
		lm.alert("商店名称不能为空！");
		$("#storeId").focus();
		return;
	}
	var appId=$("#appId").val();
	if($.trim(appId)==null || $.trim(appId)==''){
		lm.alert("appId不能为空！");
		$("#appId").focus();
		return;
	}
	var appKey=$("#appKey").val();
	if($.trim(appKey)==null || $.trim(appKey)==''){
		lm.alert("appKey不能为空！");
		$("#appKey").focus();
		return;
	}
	var type=$("input:radio[name='type']:checked").val();
	var flag=true;
	lm.postSync("${contextPath}/payChannelInfo/list/ajax/exist",{id:id,storeId:storeId,appId:appId,appKey:appKey,type:type},function(data){
		if(data==1){
			lm.alert("支付渠道已存在！");
			flag=false;
			return false;
		}
	});
	if(flag){
		$("#payChannelInfoSave").submit();
		/*
		$("#payChannelInfoAddBtn").prop("disabled","disabled");
		lm.post("${contextPath}/payChannelInfo/list/ajax/save",$("#payChannelInfoSave").serialize(),function(data){
			if(data=='1'){
		    	lm.alert("操作成功！");
		    	 window.location.href="${contextPath}/payChannelInfo/list";
			}
		}); 
		
		*/
	}
		
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
			<i class='icon-plust'></i>${empty payChannelInfo ? '添加' : '修改' }支付渠道
			</strong>
		</div>
		<div class='panel-body'>
			<form action="${contextPath}/payChannelInfo/add" method='post' enctype="multipart/form-data" id="payChannelInfoSave" >
				<div  class='form-horizontal'>
					<input id="id" name="id" type="hidden" value="${payChannelInfo.id }" />
					<input id="typeCache" name="typeCache" type="hidden" value="${payChannelInfo.type }" />
					
					<c:choose>  
					   <c:when test="${isSys==true }">  
					   		<c:if test="${empty payChannelInfo }">
								<div class="form-group" >
									<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商家</label>
									<div class="col-md-2">
										<input name="storeName" id="storeName" readonly="readonly" value="" class="form-control" isRequired="1"  />
										<input type="hidden" name="storeId" id="storeId" value="${payChannelInfo.storeId }" />
									</div>
								</div>
							</c:if>
							<c:if test="${not empty payChannelInfo }">
								<div class="form-group" >
									<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商家</label>
									<div class="col-md-2">
										<input name="" id="" readonly="readonly" value="${payChannelInfo.storeName }" class="form-control" />
										<input type="hidden" name="storeId" id="storeId" value="${payChannelInfo.storeId }" />
									</div>
								</div>
							</c:if>
					   </c:when>  
					</c:choose>
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>appId</label>
						<div class="col-md-2">
							<input type='text' id="appId" name="appId" value="${payChannelInfo.appId }" class='form-control'   maxlength="10"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>appKey</label>
						<div class="col-md-2">
							<input type='text' id="appKey" name="appKey" value="${payChannelInfo.appKey }" class='form-control'   maxlength="512"/>
						</div>
					</div>
						<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商户号</label>
						<div class="col-md-2">
							<input type='text' id="mchID" name="mchID" value="${payChannelInfo.mchID }" class='form-control'   maxlength="20"/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>证书密码</label>
						<div class="col-md-2">
							<input type='text' id="certPassword" name="certPassword" value="${payChannelInfo.certPassword }" class='form-control'   maxlength="20"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>子商户号</label>
						<div class="col-md-2">
							<input type='text' id="subMchID" name="subMchID" value="${payChannelInfo.subMchID }" class='form-control'   maxlength="20"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>微信证书</label>
						<div class="col-md-2">
							<input type='file' id="cert" name="cert" class='form-control'  />
						</div>
					</div>
					
					
					<div class="form-group">
						<label class="col-md-1 control-label">支付渠道</label>
						<div class="col-md-2" style="margin-top: 3px;">
							<input id="type_0"  type='radio' value="0" name="type" checked="checked"/>支付宝
							<input id="type_1"  type='radio' value="1" name="type"/>微信
						</div>
					</div>
					
					<div class="form-group">
						<div class="col-md-offset-1 col-md-1 0" style="float:left">
							<input type="button"  id='payChannelInfoAddBtn' class='btn btn-primary' value="${empty payChannelInfo ? '添加' : '修改' }" onclick="savepayChannelInfo()" />
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