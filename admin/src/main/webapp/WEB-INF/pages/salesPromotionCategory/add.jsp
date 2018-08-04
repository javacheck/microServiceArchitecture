<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty salesPromotionCategory ? '添加' : '修改' }促销分类</title> 
<script type="text/javascript">

$(document).ready(function(){
	
	// 列举此商家的支付方式(新增/修改)
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
	
	// 列举此商家的送货方式(新增/修改)
	var shipTypeGroup = $("#shipType_group").val();
	if(null != shipTypeGroup && "" != shipTypeGroup){
		var ship_type = shipTypeGroup.split(",");	
	
		for(var i=0;i<ship_type.length;i++){
			var group = ship_type[i].split("|");
    		$("#shipType_Show_Div").append("<input type='checkbox' name='shipTypeCheckBox' value='"+group[0]+"'>&nbsp;&nbsp;"+group[1]);
		}
	}
	
	var id = $("#salesPromotionCategoryId").val();
	// 修改时
	if( null != id && "" != id ){ 
		var type = '${salesPromotionCategory.amount}'; // 取统一价格字段数据
		if( null == type || "" == type ){
			$("#type").val(1);
			$("#discount_show").show();
			$("#amount_show").hide();
		} else {
			$("#type").val(0);
			$("#amount_show").show();
			$("#discount_show").hide();
		}
		
		$("#payType").val('${salesPromotionCategory.payType}');
		$("#shipType").val('${salesPromotionCategory.shipType}');
		$("#useBalance").val('${salesPromotionCategory.useBalance}');
		$("#useCashGift").val('${salesPromotionCategory.useCashGift}');
		$("#share").val('${salesPromotionCategory.share}');
		
		// 修改时判断此促销所支持的支付方式
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
		
		// 修改时判断此促销所支持的送货方式
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
});


function callback(obj){
	$("#salesPromotionCategory_storeName").val(obj.name);
	$("#storeId").val(obj.id);
	
	var storeId = obj.id;
	if( null == storeId || "" == storeId ){
		return ;
	}
	
	// 动态取此商家的支付方式
	lm.post("${contextPath}/salesPromotionCategory/list/ajax/storePayType",{storeId:storeId},function(data){
		$("#payType_Show_Div").empty();
		if(null != data && "" != data){
			if( data == 3 ){
				return ;
			}
			if(data == 2){
		    	$("#payType_Show_Div").append("<input type='checkbox' name='payTypeCheckBox' value='1'>&nbsp;&nbsp;货到付款<input type='checkbox' name='payTypeCheckBox' value='0'>&nbsp;&nbsp;在线支付");
			} else {
				if(data == 0 ){
					$("#payType_Show_Div").append("<input type='checkbox' name='payTypeCheckBox' value='0'>&nbsp;&nbsp;在线支付");
				} else {
					$("#payType_Show_Div").append("<input type='checkbox' name='payTypeCheckBox' value='1'>&nbsp;&nbsp;货到付款");
				}
			}
		}
	});
	
	// 动态取此商家的送货方式
	lm.post("${contextPath}/salesPromotionCategory/list/ajax/storeShipType",{storeId:storeId},function(data){
		$("#shipType_Show_Div").empty();
		if(null != data && "" != data){
			for(var i=0;i<data.length;i++){
				var group = data[i].split("|");
	    		$("#shipType_Show_Div").append("<input type='checkbox' name='shipTypeCheckBox' value='"+group[0]+"'>&nbsp;&nbsp;"+group[1]);
			}
		}
	});
}


$(document).ready(function(){
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
	
	var now = lm.Now;//当前日期
	$("#startTime").datetimepicker({
		minView: 0, 
		format: "yyyy-mm-dd hh:ii:ss", //选择日期后，文本框显示的日期格式 
		language: 'zh-CN', //汉化 
		autoclose:true , //选择日期后自动关闭
		startDate:now,
		todayBtn:true,//可选择当天按钮
		todayHighlight:false//高亮当前日期
	 });
	
	$("#endTime").datetimepicker({
		minView: 0, 
		format: "yyyy-mm-dd hh:ii:ss", //选择日期后，文本框显示的日期格式 
		language: 'zh-CN', //汉化 
		autoclose:true , //选择日期后自动关闭
		startDate:now,
		todayBtn:true,//可选择当天按钮
		todayHighlight:false//高亮当前日期
	 });
	
	//判断输入日期的合法性
	$('#startTime').datetimepicker().on('changeDate', function(ev){
		if((new Date(ev.date.valueOf())) > (new Date($('#endTime').val().replace(/-/g,"/")))){
			lm.alert("结束时间不能大于开始时间");
			$("#endTime").val("");
		}
	});
	$('#endTime').datetimepicker().on('changeDate', function(ev){
		if((new Date(ev.date.valueOf())) < (new Date($('#startTime').val().replace(/-/g,"/")))){
			lm.alert("结束时间不能大于开始时间");
			$("#endTime").val("");
		}
	});
	
	var salesPromotionCategory_status = '${salesPromotionCategory.status}';
	if( null != salesPromotionCategory_status && "" != salesPromotionCategory_status){ // 修改时
		if( 0 ==  salesPromotionCategory_status){ // 未开始
			$("#salesPromotionCategory_storeName").click(function (){
				$("#salesPromotionCategoryShowStore").modal();
			});			
		} else if( 1 == salesPromotionCategory_status ){ // 已开始
			$('#startTime').datetimepicker('remove'); // 不能修改促销开始时间
		} 
	} else { // 新增时
		$("#salesPromotionCategory_storeName").click(function (){
			$("#salesPromotionCategoryShowStore").modal();
		});	
	}
});


$(document).ready(function(){
	//保存/修改
	$("#salesPromotionCategoryBtn").click(function(){
		var storeId = $("#storeId").val();
		storeId = $.trim(storeId);  // 用jQuery的trim方法删除前后空格
		if( null == storeId || "" == storeId ){
			lm.alert("请先选择商家");
			return ;
		}
		var name = $("#name").val();
		name = $.trim(name);  // 用jQuery的trim方法删除前后空格
		if( null == name || "" == name ){
			lm.alert("促销名称不能为空");
			return;
		}
		
		var falg = false;
		var id = $("#salesPromotionCategoryId").val();
		lm.postSync("${contextPath}/salesPromotionCategory/list/ajax/checkName",{id:id,storeId:storeId,name:name},function(data){
			if(data == 0){
				falg = true;
			} else {
				lm.alert("促销名称存在重复");
			}
		});
		
		if(!falg){
			return ;
		}
	
		var type = $("#type").val();
		if(type == 0){
			var amount = $("#amount").val();
			amount = $.trim(amount);  // 用jQuery的trim方法删除前后空格
			if( null == amount || "" == amount){
				lm.alert("统一价格不能为空");
				return ;
			}
			if(!(lm.isFloat(amount))){
				lm.alert("价格输入错误!");
				return ;
			}
		} else {
			var discount = $("#discount").val();
			discount = $.trim(discount);  // 用jQuery的trim方法删除前后空格
			if( null == discount || "" == discount ){
				lm.alert("折扣不能为空");
				return;
			}
			if(!(lm.isFloat(discount) && (discount > 0 && discount < 100))){
				lm.alert("请输入正确的折扣");
				return;
			}
		}
		
		var salesNum = $("#salesNum").val();
		salesNum = $.trim(salesNum);  // 用jQuery的trim方法删除前后空格
		if( null != salesNum && "" != salesNum ){
			if( !(/^\+?[1-9][0-9]*$/.test(salesNum)) ){
				lm.alert("促销数量必须为正整数");
				return ;
			}
		}
		
		var buyNum = $("#buyNum").val();
		buyNum = $.trim(buyNum);  // 用jQuery的trim方法删除前后空格
		if( null != buyNum && "" != buyNum ){
			if( !(/^\+?[1-9][0-9]*$/.test(buyNum)) ){
				lm.alert("购买数量限制必须为正整数");
				return ;
			}
		}
		
		// 当促销数量不为无限数量而购买数量限制为无限时，弹出提示信息
		if( null != salesNum && "" != salesNum ){
			if(null == buyNum || "" == buyNum ){
				lm.alert("购买数量限制不能大于促销数量");
				return ;
			}
		}
		if( parseInt(buyNum) > parseInt(salesNum) ){
			lm.alert("购买数量限制不能大于促销数量");
			return ;
		}
			
		var payTypeCount = 0;
		 // 检测交易类型
		 $("[name = payTypeCheckBox]:checkbox").each(function () {
			 if(this.checked){
				 $("#payType").val($(this).val());
				 payTypeCount++;
	         }
		 });
		 
		 if( payTypeCount == 0 ){
	   	 	lm.alert("请选择至少一种支付类型");
	   	 	return false;
	     }
		 if( payTypeCount == 2 ){
		     $("#payType").val(2);
		 }
		 
		 var str="";
	     $("input[name='shipTypeCheckBox']").each(function(){ 
	         if(this.checked){
	             str += $(this).val()+","
	         }
	     });
	     if( "" == str ){
	    	 lm.alert("请选择至少一种送货方式");
	    	 return false;
	     }
	     $("#shipType").val(str.substring(0,str.length-1));
		 
		var startDate = $("#startTime").val();
		if( null == startDate || "" == startDate ){
			lm.alert("促销开始时间不能为空");
			return;
		}
		
		var endDate = $("#endTime").val();
		if( null == endDate || "" == endDate ){
			lm.alert("促销结束时间不能为空");
			return;
		}
		
		$("#salesPromotionCategoryForm").submit();
	}); 
});
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>${empty salesPromotionCategory ? '添加' : '修改' }促销分类
			</strong>
		</div>
		<div class='panel-body'>
			<form id="salesPromotionCategoryForm" method='post' repeatSubmit='1' class='form-horizontal' action="${contextPath }/salesPromotionCategory/save">
				<!-- 隐藏salesPromotionCategoryId,判断是否是修改操作 -->
				<input type="hidden" name="id" id="salesPromotionCategoryId" value="${salesPromotionCategory.id }">
				<input type="hidden" name="payType_group" id="payType_group" value="${payType_group }">
				<input type="hidden" name="shipType_group" id="shipType_group" value="${shipType_group }">
				
				<!-- 因为后期的修改问题,此字段只新增的时候使用,不再在页面输入 -->
				<input type="hidden" name="status" id="status" value="0">
				
				<div class="form-group">
					<label class="col-md-1 control-label">商家</label>
					<div class="col-md-2">
						<input type="text" name="storeName" readonly="readonly" class='form-control' id="salesPromotionCategory_storeName" value="${storeName }">							
						<input type="hidden" name="storeId" id="storeId"  value="${addOrUpdate_storeId }">
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">促销名称</label>
					<div class="col-md-2">
						<input type="text" name="name" class='form-control' id="name"  value="${salesPromotionCategory.name }" maxlength="190"  >
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">促销类型</label>
					<div class="col-md-2">
						<select id="type" name="type" class='form-control'> 
							<option id="0" value="0" selected="selected">统一价格</option>
							<option id="1" value="1">折扣</option>
						</select>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group" id="amount_show">
					<label class="col-md-1 control-label">统一价格(元)</label>
					<div class="col-md-2">
						<input type="text" name="amount" class='form-control' id="amount"  value="${salesPromotionCategory.amount }" maxlength="7"  >
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group" id="discount_show" style="display: none">
					<label class="col-md-1 control-label">折扣(%)</label>
					<div class="col-md-2">
						<input type="text" name="discount" class='form-control' id="discount"  value="${salesPromotionCategory.discount }" maxlength="3"  >
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
					
				<div class="form-group">
					<label class="col-md-1 control-label">促销数量</label>
					<div class="col-md-2">
						<input type="text" id="salesNum" name="salesNum" value="${salesPromotionCategory.salesNum == -1 ? '' : salesPromotionCategory.salesNum }" placeholder="不填则表示不限数量" class='form-control' maxlength="10"/>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
								
				<div class="form-group">
					<label class="col-md-1 control-label">购买数量限制</label>
					<div class="col-md-2">
						<input type="text" id="buyNum" name="buyNum" value="${salesPromotionCategory.buyNum == -1 ? '' : salesPromotionCategory.buyNum }" placeholder="不填则表示不限数量" class='form-control' maxlength="10"/>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">支付方式</label>
					<input type="hidden" name="payType" id="payType" value=""/>
					<div class="col-md-2" id="payType_Show_Div">

					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">配送方式</label>
					<input type='hidden' id="shipType" name="shipType" value=""/>
					<div class="col-md-2" id="shipType_Show_Div">
					
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">是否可以使用余额</label>
					<div class="col-md-2">
						<select id="useBalance" name="useBalance" class='form-control'> 
							<option id="1" value="1" selected="selected">可以使用</option>
							<option id="0" value="0">不可以使用</option>
						</select>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">是否可以使用优惠劵</label>
					<div class="col-md-2">
						<select id="useCashGift" name="useCashGift" class='form-control'> 
							<option id="1" value="1" selected="selected">可以使用</option>
							<option id="0" value="0">不可以使用</option>
						</select>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">是否单独促销</label>
					<div class="col-md-2">
						<select id="share" name="share" class='form-control'> 
							<option id="1" value="1" selected="selected">单独促销</option>
							<option id="0" value="0">可以同其他商品一起购买</option>
						</select>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">促销开始时间</label>
					<div class="col-md-2">
						<input type='text' readonly="readonly" id="startTime"  name="startTime" class='form-control' value="<fmt:formatDate value="${salesPromotionCategory.startDate }" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">促销结束时间</label>
					<div class="col-md-2">
						<input type='text' readonly="readonly" id="endTime"  name="endTime" class='form-control' value="<fmt:formatDate value="${salesPromotionCategory.endDate }" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
					
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button"  id='salesPromotionCategoryBtn' class='btn btn-primary' value="${empty salesPromotionCategory ? '添加' : '修改' }" data-loading='稍候...' />
					</div>
				</div>
			</form>
	  </div>
   </div>
   
<!-- 店铺弹窗标签 -->
<m:select_store path="${contextPath}/shop/showModle/list/list-data" modeId="salesPromotionCategoryShowStore" callback="callback"> </m:select_store>
</body>
</html>