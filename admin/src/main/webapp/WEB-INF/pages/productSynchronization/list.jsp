<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品同步</title> 
<script type="text/javascript">

//初始化事件
$(function() {
	// 点击弹出商家选择窗口
	$("#toStoreName").click(function (){
		$("#productSynchronizationShowToStore").modal();
	});
	
});
function toCallback(obj){
	$("#toStoreName").val(obj.name);
	$("#toStoreId").val(obj.id);
}
function productSynchronization(){
	var toStoreId=$("#toStoreId").val();
	if(toStoreId==null || toStoreId==""){
		lm.alert("请选择分店！");
		return;
	}
	var flag=true;
	lm.postSync("${contextPath}/productSynchronization/findStockExist",{toStoreId:toStoreId},function(data){
		if(data!=null && data!=''){
			lm.alert("该商店已存在商品，不能同步！");
			flag=false;
			return;
		}
	});
	if(flag){
		lm.confirm("确定要同步商品吗？",function(){
			lm.post("${contextPath }/productSynchronization/edit",{toStoreId:toStoreId},function(data){
				if(data==1){
					lm.alert("操作成功！");
				} 
			});
		});
	}
	return;
}
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong><i class='icon-plust'></i>商品同步 </strong>
		</div>
		<div class='panel-body'>
			<div  class='form-horizontal'>
				<form  class='form-horizontal'>
					<div class="form-group">
						
						<%-- <label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商品调出商店</label>
						<div class="col-md-2">
							<input name="fromStoreName" id="fromStoreName" readonly="readonly" value="${store.name }" class="form-control" isRequired="1"  />
							<input type="hidden" name="fromStoreId" id="fromStoreId" value="${store.id }" />
						</div> --%>
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>同步商品信息到</label>
						<div class="col-md-2">
							<input name="toStoreName" id="toStoreName" readonly="readonly" value="请选择分店" class="form-control" isRequired="1"  />
							<input type="hidden" name="toStoreId" id="toStoreId" value="" />
						</div>
					</div>
					
					<div class="form-group">
						<div class="col-md-offset-1 col-md-10">
							<input type="button"  repeatSubmit='1' id='productSynchronizationAddBtn' class='btn btn-primary' onclick="productSynchronization();" value="确认" data-loading='稍候...' />
						</div>
					</div>
				</form>
				<m:select_store path="${contextPath}/productSynchronization/showModel/listStore/list-data" modeId="productSynchronizationShowToStore" callback="toCallback"> </m:select_store>
				
			</div>
		</div>
	</div>


</body>
</html>