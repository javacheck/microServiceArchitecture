<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看促销分类详细信息</title> 
<script type="text/javascript">

$(document).ready(function(){
	
	// 列举此商家的支付方式
	var payTypeGroup = $("#payType_group").val();
	if(null != payTypeGroup && "" != payTypeGroup){
		if(payTypeGroup == 2){
	    	$("#payType_Show_Div").append("<input type='checkbox' name='payTypeCheckBox' value='1'>&nbsp;&nbsp;货到付款<input type='checkbox' name='payTypeCheckBox' value='0'>&nbsp;&nbsp;在线支付");
		} else {
			if(payTypeGroup == 0 ){
				$("#payType_Show_Div").append("<input type='checkbox' name='payTypeCheckBox' value='0'>&nbsp;&nbsp;在线支付");
			} else {
				$("#payType_Show_Div").append("<input type='checkbox' name='payTypeCheckBox' value='1'>&nbsp;&nbsp;货到付款");
			}
		}
	}
	
	// 列举此商家的送货方式
	var shipTypeGroup = $("#shipType_group").val();
	if(null != shipTypeGroup && "" != shipTypeGroup){
		var ship_type = shipTypeGroup.split(",");	
	
		for(var i=0;i<ship_type.length;i++){
			var group = ship_type[i].split("|");
    		$("#shipType_Show_Div").append("<input type='checkbox' name='shipTypeCheckBox' value='"+group[0]+"'>&nbsp;&nbsp;"+group[1]);
		}
	}
	
	var id = $("#salesPromotionCategoryId").val();
	// 查看时
	if( null != id && "" != id ){ 
		var type = '${salesPromotionCategory.amount}';
		if( null == type || "" == type ){
			$("#type").val(1);
			$("#discount_show").show();
			$("#amount_show").hide();
		} else {
			$("#type").val(0);
			$("#amount_show").show();
			$("#discount_show").hide();
		}
		$("#shipType").val('${salesPromotionCategory.shipType}');
		$("#payType").val('${salesPromotionCategory.payType}');
		$("#useBalance").val('${salesPromotionCategory.useBalance}');
		$("#useCashGift").val('${salesPromotionCategory.useCashGift}');
		$("#share").val('${salesPromotionCategory.share}');
		
		$("#type").attr("disabled",true);
		$("#useBalance").attr("disabled",true);
		$("#useCashGift").attr("disabled",true);
		$("#share").attr("disabled",true);
		
		// 查看时判断此促销所支持的支付方式
		var payTypeValue =  $("#payType").val();
		if(null != payTypeValue && "" != payTypeValue){
			if( payTypeValue == 2 ){
				$("[name = payTypeCheckBox]:checkbox").attr("checked",true);
			} else {
				$("[name = payTypeCheckBox]:checkbox").each(function(key,value){
					if( payTypeValue == $(value).val() ){
						$(value).attr("checked",true);
						return ; // 匹配到了就不找了
					}
				});
			}
		}
		
		// 查看时判断此促销所支持的送货方式
		var shipTypeValue = $("#shipType").val();
		if(null != shipTypeValue && "" != shipTypeValue){
			var shipTypeArray = shipTypeValue.split(",");
			$("input:checkBox[name='shipTypeCheckBox']").each(function(key,value){
				for (var i = 0; i < shipTypeArray.length; i++) {
					if( shipTypeArray[i] == $(value).val() ){
						$(value).attr("checked",true);
						return ; // 匹配到了就不找了
					}
				}
			});
		}
	}
			
	$("#type").change(function(){
		if(0 == $(this).val()){ // 统一价格
			$("#amount_show").show();
			$("#discount_show").hide();
			$("#discount").val("");
		} else {
			$("#discount_show").show();
			$("#amount_show").hide();
			$("#amount").val("");
		}
	});
	
	$("#salesPromotionCategoryBackBtn").click(function(){
		history.back();
	});
	
});
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>查看促销分类详细信息
			</strong>
		</div>
		<div class='panel-body'>
			<form id="salesPromotionCategoryForm" method='post' class='form-horizontal'>
				<input type="hidden" name="id" id="salesPromotionCategoryId" value="${salesPromotionCategory.id }">
				<input type="hidden" name="payType_group" id="payType_group" value="${payType_group }">
				<input type="hidden" name="shipType_group" id="shipType_group" value="${shipType_group }">
				
				<div class="form-group">
					<label class="col-md-1 control-label">商家</label>
					<div class="col-md-2">
						<input type="text" name="storeName" readonly="readonly" class='form-control' id="salesPromotionCategory_storeName" value="${storeName }">							
						<input type="hidden" name="storeId" id="storeId"  value="${addOrUpdate_storeId }">
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">促销名称</label>
					<div class="col-md-2">
						<input type="text" name="name" class='form-control' id="name"  value="${salesPromotionCategory.name }" maxlength="190"  >
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">促销类型</label>
					<div class="col-md-2">
						<select id="type" name="type" class='form-control'> 
							<option id="0" value="0" selected="selected">统一价格</option>
							<option id="1" value="1">折扣</option>
						</select>
					</div>
				</div>
				
				<div class="form-group" id="amount_show">
					<label class="col-md-1 control-label">统一价格</label>
					<div class="col-md-2">
						<input type="text" name="amount" class='form-control' id="amount"  value="${salesPromotionCategory.amount }" maxlength="7">
					</div>
				</div>
				
				<div class="form-group" id="discount_show" style="display: none">
					<label class="col-md-1 control-label">折扣</label>
					<div class="col-md-2">
						<input type="text" name="discount" class='form-control' id="discount"  value="${salesPromotionCategory.discount }" maxlength="3">
					</div>
				</div>
					
				<div class="form-group">
					<label class="col-md-1 control-label">促销数量</label>
					<div class="col-md-2">
						<input type="text" id="salesNum" name="salesNum" value="${salesPromotionCategory.salesNum == -1 ? '' : salesPromotionCategory.salesNum }" placeholder="不填则表示不限数量" class='form-control' maxlength="10"/>
					</div>
				</div>
								
				<div class="form-group">
					<label class="col-md-1 control-label">购买数量限制</label>
					<div class="col-md-2">
						<input type="text" id="buyNum" name="buyNum" value="${salesPromotionCategory.buyNum == -1 ? '' : salesPromotionCategory.buyNum }" placeholder="不填则表示不限数量" class='form-control' maxlength="10"/>
					</div>
				</div>
						
				<div class="form-group">
					<label class="col-md-1 control-label">支付方式</label>
					<input type="hidden" name="payType" id="payType" value=""/>
					<div class="col-md-2" id="payType_Show_Div">

					</div>
				</div>
						
				<div class="form-group">
					<label class="col-md-1 control-label">配送方式</label>
					<input type='hidden' id="shipType" name="shipType" class='form-control' value=""/>
					<div class="col-md-2" id="shipType_Show_Div">
					
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">是否可以使用余额</label>
					<div class="col-md-2">
						<select id="useBalance" name="useBalance" class='form-control'> 
							<option id="1" value="1" selected="selected">可以使用</option>
							<option id="0" value="0">不可以使用</option>
						</select>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">是否可以使用优惠劵</label>
					<div class="col-md-2">
						<select id="useCashGift" name="useCashGift" class='form-control'> 
							<option id="1" value="1" selected="selected">可以使用</option>
							<option id="0" value="0">不可以使用</option>
						</select>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">是否单独促销</label>
					<div class="col-md-2">
						<select id="share" name="share" class='form-control'> 
							<option id="1" value="1" selected="selected">单独促销</option>
							<option id="0" value="0">可以同其他商品一起购买</option>
						</select>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">促销开始时间</label>
					<div class="col-md-2">
						<input type='text' readonly="readonly" id="startTime"  name="startTime" class='form-control' value="<fmt:formatDate value="${salesPromotionCategory.startDate }" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">促销结束时间</label>
					<div class="col-md-2">
						<input type='text' readonly="readonly" id="endTime"  name="endTime" class='form-control' value="<fmt:formatDate value="${salesPromotionCategory.endDate }" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					</div>
				</div>
					
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button" id='salesPromotionCategoryBackBtn' class='btn btn-primary' value="返回" data-loading='稍候...' />
					</div>
				</div>
			</form>
	  </div>
   </div>
</body>
</html>