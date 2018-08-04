<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>支付渠道列表</title>
<script type="text/javascript">

function callback(obj){
	$("#payChannelInfo_storeName").val(obj.name);
	$("#payChannelInfo_storeId").val(obj.id);
}
function deletepayChannelInfo(id){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/payChannelInfo/delete/delete-by-payChannelInfoId",{id:id},function(data){
			lm.alert("删除成功");
			 window.location.href="${contextPath}/payChannelInfo/list";
		});
	});
}
function formReset(){
	$("#payChannelInfo_storeName").val("");
	$("#payChannelInfo_storeId").val("");
}
$(function(){
	// 点击弹出商家选择窗口
	$("#payChannelInfo_storeName").click(function (){
		$("#payChannelInfoShowStore").modal();
	});
});
//修改选择的店铺时触发
function byStoreAjaxRefreshList(){
	loadList_payChannelInfoList(); 
}


</script>

<style>
  .input-group2{
  float:left;width:auto; background-color: #e5e5e5;border: 1px solid #ccc;border-radius: 4px; color: #222;font-size: 13px;font-weight: 400; padding: 5px 12px;text-align: center;
  }
  </style>
</head>
<body>
	<m:list title="支付渠道列表" id="payChannelInfoList"
		listUrl="${contextPath }/payChannelInfo/list/list-data" 
		addUrl="/payChannelInfo/add" 
		searchButtonId="cateogry_search_btn" formReset="formReset">
		
		<div class="input-group" style="max-width:1500px;">
        	    
           <c:choose>  
			   <c:when test="${isSys==true }">  
			   		<span class="input-group2">商家</span>
					<input name="storeName" id="payChannelInfo_storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="" class="form-control" isRequired="1" tipName="商家"  />
					<input type="hidden" name="storeId" id="payChannelInfo_storeId" value="" />
			   </c:when>  
			   <c:otherwise> 
			   		<m:hasPermission permissions="mainShopOrderStoreView">
			   			<c:if test="${isMainStore==true }">
							<span class="input-group2">商家</span>
							<input name="storeName" id="payChannelInfo_storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="" class="form-control" isRequired="1" tipName="商家"  />
							<input type="hidden" name="storeId" id="payChannelInfo_storeId" value="-1" />
						</c:if>
       				</m:hasPermission>  
			   </c:otherwise>  
			</c:choose>
           <span class="input-group2">支付渠道</span>
           	<select name="type" class="form-control" style="width: auto;margin-right:40px;" id="type">
           		<option  value ="">全部</option>
				<option  value ="0">支付宝</option>
				<option  value ="1">微信</option>
           	</select>
            	
		</div>
	</m:list>
	<m:select_store path="${contextPath}/payChannelInfo/showModel/list/list-data" modeId="payChannelInfoShowStore" callback="callback"> </m:select_store>
            	
		
	
</body>
</html>