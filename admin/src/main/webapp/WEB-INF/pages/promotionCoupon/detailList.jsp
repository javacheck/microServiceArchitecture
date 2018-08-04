<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>优惠列表</title>
<script type="text/javascript">
function callback(obj){
	$("#cashGift_storeName").val(obj.name);
	$("#cashGift_storeId").val(obj.id);
}
function formReset(){
	$("#cashGift_storeName").val("");
	$("#cashGift_storeId").val("");
}
$(function(){
	// 点击弹出商家选择窗口
	$("#cashGift_storeName").click(function (){
		$("#cashGiftShowStore").modal();
	});
});
//修改选择的店铺时触发
function byStoreAjaxRefreshList(){
	loadList_cashGiftList(); 
}




</script>
<style>
  .input-group2{
  float:left;width:auto; background-color: #e5e5e5;border: 1px solid #ccc;border-radius: 4px; color: #222;font-size: 13px;font-weight: 400; padding: 5px 12px;text-align: center;
  }
  </style>
</head>
<body>
	<m:list title="优惠列表" id="promotionCouponList"
		listUrl="${contextPath }/promotionCoupon/detailList-data" 
		searchButtonId="cateogry_search_btn" formReset="formReset" >
		
		<div class="input-group" style="max-width:1500px;">
			<input type="hidden" id="couponId" name="couponId" value="${couponId}"/>
            <span class="input-group2">手机号码</span> 
            <input type="text" id="mobile" name="mobile" class="form-control"  style="width: 200px;float:left;margin-right:40px;">
            
            <span class="input-group2">使用情况</span> 
            <select name="status" class="form-control" style="margin-right:40px;width: auto;" id="status">
           		<option  value ="">全部</option>
				<option  value ="0">未使用</option>
				<option  value ="1">已使用</option>
				<option  value ="2">已过期</option>
           	</select>
            
		</div>
	</m:list>
	<m:select_store path="${contextPath}/promotionCoupon/showModel/list/list-data" modeId="cashGiftShowStore" callback="callback"> </m:select_store>
</body>
</html>