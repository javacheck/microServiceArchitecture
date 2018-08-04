<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="${contextPath}/static/js/uploadPreview.js" type="text/javascript"></script>
<title>新增售后</title>
<style type="text/css">
.bor{
       width:100%;
    }
    
.bor th,.bor tr,.bor td{
    border:none;
    text-align: center;
    line-height: 25px;
}
</style>
<script type="text/javascript">
var productNo = 1;
$(function(){
	$("#productSelect").hide();
	var format = "yyyy-MM-dd";//默认格式
	var now = lm.Now;//当前日期
	var nowFormat=now.Format(format);
	$("#beginTime").datetimepicker({
		minView: "month", //选择日期后，不会再跳转去选择时分秒 
		format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
		language: 'zh-CN', //汉化 
		autoclose:true , //选择日期后自动关闭
		todayBtn:true,//可选择当天按钮
		todayHighlight:false,//高亮当前日期
		endDate:""+nowFormat+""//默认最后可选择为当前日期
	 });
	$("#endTime").datetimepicker({
		minView: "month", //选择日期后，不会再跳转去选择时分秒 
		format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
		language: 'zh-CN', //汉化 
		autoclose:true, //选择日期后自动关闭 
		todayBtn:true,//可选择当天按钮
		todayHighlight:false,//高亮当前日期
		endDate:""+nowFormat+""//默认最后可选择为当前日期
	 });
	var endDateInput=nowFormat;
	//判断输入结束日期时候输入合法
	$('#endTime').datetimepicker().on('changeDate', function(ev){
		if((new Date(ev.date.valueOf()))<(new Date($('#beginTime').val().replace(/-/g,"/")))){//开始时间大于结束时间
			lm.alert("结束时间不能大于开始时间");
			$("#endTime").val(endDateInput);//默认值上一次输入
			$('#endTime').datetimepicker('update');//更新
		}
		endDateInput= $("#endTime").val();
		$('#beginTime').datetimepicker('setEndDate', $("#endTime").val());//设置开始时间最后可选结束时间
	});
	
	
	$("#productAfterSalesAddBtn").click(function(){
		$(this).prop("disabled",true);
		var list = new Array();
		var $storeId = "${store_session_key.id}";
		var $storeName = "${store_session_key.name}";
		var $orderId = $("#afterSales_orderId").val();
		if($(".finalOrderItem").length<1) {
			lm.alert("请选择要添加售后记录的订单商品！");
			$(this).prop("disabled",false);
			return;
		}else{
			var flag = true;
			$(".finalOrderItem").each(function(){
				var obj = new Object();
				var $productId = $(this).children("#productId").val();
				var $productName = $(this).children("#productName").val();
				var $barCode = $(this).children("#barCode").val();
				var $unitName = $(this).children("#unitName").val();
				var $productCategoryId = $(this).children("#productCategoryId").val();
				var $afterSalesTypeId = $(this).children("#afterSalesTypeId").children("#supplierId").children("option:selected").val();
				var $amount = $.trim($(this).find("#amount").val());
				var $remark = $(this).find("#remark").val();
				var $orderItemId = $(this).children("#orderItemId").val();
				var $price = $(this).children("#price").val();
				
				if($productId==null||$productId==""||$productName==null||$productName=="") {
					flag = false;
					lm.alert("页面出错，请刷新后再试或联系管理员。");
					return;
				}
				
				if($afterSalesTypeId==null||$afterSalesTypeId=="") {
					flag=false;
					lm.alert("商品:<span style='color:red;'>"+$productName+"</span>的售后类型不能为空！");
					return;
				}
				
				if($amount==null||$amount==""||$amount==0) {
					flag=false;
					lm.alert("商品:<span style='color:red;'>"+$productName+"</span>的售后数量不能为空！");
					return;
				}
				
				if(!(/^\d*(\.\d*)?$/.test($amount))) {
					flag=false;
					lm.alert("商品:<span style='color:red;'>"+$productName+"</span>的售后数量必须为数字！");
					return;
				}
				
				obj.productId = $productId;
				obj.productName = $productName;
				obj.barCode = $barCode;
				obj.unitName = $unitName;
				obj.productCategoryId = $productCategoryId;
				obj.afterSalesTypeId = $afterSalesTypeId;
				obj.amount = $amount;
				obj.remark = $remark;
				obj.orderItemId = $orderItemId;
				obj.price = $price;
				list.push(obj);
			});
			if(!flag){
				$(this).prop("disabled",false);
				return;
			}else{
				lm.post("${contextPath}/afterSales/save",{afterSalesJson:JSON.stringify(list),storeId:$storeId,storeName:$storeName,orderId:$orderId},function(data){
					if(data=='1'){
				    	lm.alert("操作成功！");
				    	window.location.href="${contextPath}/afterSales/list";
					}else{
						lm.alert("操作失败！请稍后再试或联系服务器管理员。");
						$("#productAfterSalesAddBtn").prop("disabled",false);
						return;
					}
				});
			}
		}
	});
})
function selectThisOrder(orderId) {
	$("#afterSales_orderId").val(orderId);
	$("#selectOrder").val(orderId);
	$("#productAbout").show();
	$("#productOrderId").val(orderId);
	$("#productChange [name='resetButton']")[0].click();
	$("#tbAmount").empty();
	productNo=1;
	$("#productSelect").hide();
}

function getProductList(){
	var productIds="";
	var productNames="";
    $("input[name='listProudctName']:checked").each(function(i){ 
    	productIds+=$(this).val()+","; 
    	productNames+=$(this).parent().parent().children()[1].innerHTML+",";
    });
    if (productIds==""){
    	lm.alert("请选择商品！");
    	return;
    } else {
    	var arrPss=productIds.split(",");
    	var arrPss2=productNames.split(",");
    	var flag=false;
    	$(".orderItemId").each(function(){
    		for (var k=0;k<arrPss.length;k++) {
    			if (arrPss[k]==$(this).val()) {
    				flag=true;
    				lm.alert("<span style='color:red;'>"+arrPss2[k]+"</span>已选择！");
    				return;
    			}
    		}
    	});
    	if (flag) {
    		return;
    	} else {
    		$("input[name='listProudctName']:checked").each(function(i){
    			$orderItemId = $(this).parent().children("#orderItemId").val();
    			$productId = $(this).parent().children("#productId").val();
    			$productName = $(this).parent().children("#productName").val();
    			$productBarCode = $(this).parent().children("#barCode").val();
    			$productUnit = $(this).parent().children("#unitName").val();
    			$productPrice = $(this).parent().children("#price").val();
    			$amount = $(this).parent().parent().children()[5].innerHTML;
    			$afterSalesType = $(this).parent().parent().children()[6].innerHTML;
    			$afterSalesAmount = $(this).parent().parent().children()[7].innerHTML;
    			$returnGoodsAmount = $(this).parent().parent().children()[8].innerHTML;
    			$productCategoryId = $(this).parent().children("#productCategoryId").val();
    			$max = $amount - $returnGoodsAmount;
    			$("#tbAmount").append("<tr style='height:45px;' class='finalOrderItem'>"+
    					"<input type='hidden' class='orderItemId' id='orderItemId' name='orderItemId' value='" + $orderItemId + "' />" +
    					"<input type='hidden' id='productId' name='productId' value='" + $productId + "' />" +
    					"<input type='hidden' id='productName' name='productName' value='" + $productName + "' />" +
    					"<input type='hidden' id='barCode' name='barCode' value='" + $productBarCode + "' />" +
    					"<input type='hidden' id='unitName' name='unitName' value='" + $productUnit + "' />" +
    					"<input type='hidden' id='productCategoryId' name='productCategoryId' value='" + $productCategoryId + "' />" +
    					"<input type='hidden' id='price' name='price' value='" + $productPrice + "' />" +
    	    			"<td>" + (productNo) + "</td>" +
    	    			"<td>" + $productName + "</td>" +
    	    			"<td>" + $productUnit + "</td>" +
    	    			"<td>" + $productPrice + "</td>" +
    	    			"<td id='afterSalesTypeId'>" + "<select style='width:200px;' name='supplierId"+i+"' class='form-control'  id='supplierId'><option  value =''>请选择售后类型</option><c:forEach items='${afterSalesTypeList }' var='types'><option value='${types.id }'>${types.name }</option></c:forEach></select>" + "</td>" +
    	    			"<td>" + "<input type='text' id='amount' name='amount'  value='' placeholder='最多可输" + $max + "' style='width:150px;height:30px;' maxlength='7' onblur='checkNum(this," + $max + ")'/>" + "</td>" +
    	    			"<td>" + "<input type='text' id='remark' name='remark'   value='' style='width:200px;' maxlength='100'/>" + "</td>" +
    	    			"<td>" + "<button type='button'  onclick='del(this)' class='btn btn-small btn-danger' style='width: auto;height:30px;'>删除</button>" + "</td>" +
    	    			"</th>");
    	    	productNo++;
    	    });
    		$("#productSelect").show();
    	}
    }
    $("#productListModalBtn").click();
}

function checkNum(obj,max){
	if($(obj).val() > max) {
		lm.alert("数量超出最大值");
		$(obj).val("");
	}
}

function del(divName){
	productNo=productNo-1;
	$(divName).parent().parent().remove();
	$("#tbAmount").find("tr").each(function(index,value){
		$(value).find("td").first().html(index+1);
	});
	if($.trim($("#tbAmount").html())==''){
		$("#productSelect").hide();
	}
}

function productListDataIdCallback(){
	$("#all").click(function(){
	    if(this.checked){    
	        $("input:checkbox").prop("checked", true);   
	    }else{    
	    	$("input:checkbox").prop("checked", false); 
	    }    
	});
}

function changeOrderId() {
	$("productOrderId").val($("#afterSales_orderId").val());
}
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong><i class='icon-plust'></i>添加售后记录 </strong>
		</div>
		
		<div class='panel-body'>
			<div  class='form-horizontal'>
				<form id="afterSalesAddForm" method='post' class='form-horizontal' repeatSubmit='1'
				action="${contextPath }/afterSales/save">
					<div id="orderAbout" class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>订单编号</label>
						<div class="col-md-2">
							<input type="button" id="selectOrder" class="btn btn-large btn-block btn-default"  name="selectOrder"  value="请选择订单" data-toggle="modal" data-target="#orderChange"/>
							<input type="hidden" name="orderId" id="afterSales_orderId" value="" />
						</div>
					</div>
					
					<div id="productAbout" class="form-group" style="display:none;">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>选择商品</label>
						<div class="col-md-2">
							<input type="button" id="selectOrder" class="btn btn-large btn-block btn-default"  name="selectOrder"  value="请选择商品" data-toggle="modal" data-target="#productChange"/>
							<input type="hidden" name="orderId" id="afterSales_orderId" value="" />
						</div>
					</div>
					
					<div class="form-group">
					<label class="col-md-1 control-label">&nbsp;</label>
					<div id="productSelect" class="col-md-11" style="max-height:450px;overflow-y: auto;overflow-x:auto; border:1px solid #ccc;">
						<table  class="bor">
							<thead>
								<tr> 
									<th>序号</th>
									<th>商品名称</th>
									<th>商品规格</th>
									<th>销售单价</th>
									<th><span style="color: red;font-size: 15px">*</span>售后类型</th>
									<th><span style="color: red;font-size: 15px">*</span>数量</th>
									<th>备注</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody id="tbAmount">
									
							</tbody>
						</table>
					</div>
					</div>
					<div class="form-group">
						<div class="col-md-offset-1 col-md-10">
							<input type="button"  repeatSubmit='1' id='productAfterSalesAddBtn' class='btn btn-primary' 
								value="保存" data-loading='稍候...' />
								<a href="javascript:history.go(-1);" class="btn btn-small btn-danger">返回</a>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>

	<div class="modal fade" id="orderChange">
		<div class="modal-dialog modal-lg" style="width: 1200px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" id="orderListModalBtn"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
					<h4 class="modal-title"></h4>
				</div>
				<div class="modal-body">
					<m:list title="订单列表" id="orderList" listUrl="${contextPath }/afterSales/orderList/list-data" searchButtonId="orderList_search_btn" >
						<div class="input-group" style="max-width: 1500px;">
							<input type="hidden" name="storeId" id="storeId" value="${store_session_key.id}"/>
							<span class="input-group-addon">订单编号</span> 
			            		<input type="text" id="order_id" name="orderId" class="form-control" placeholder="请输入订单编号" style="width: 200px;">
			            	<span  class="input-group-addon">开始时间</span> 
								<input id = "beginTime" type="text" name="beginTime" class="form-control" placeholder="选择开始日期" readonly  style="width: 200px;margin-right:40px;">
							<span  class="input-group-addon">结束时间</span> 
								<input id = "endTime" type="text" name="endTime" class="form-control" placeholder="选择结束日期" readonly style="width: 200px;margin-right:40px;">
						</div>
					</m:list>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade" id="productChange">
		<div class="modal-dialog modal-lg" style="width: 1200px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" id="productListModalBtn"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
					<h4 class="modal-title"></h4>
				</div>
				<div class="modal-body">
					<m:list title="商品列表" id="productList" listUrl="${contextPath }/afterSales/productList/list-data" callback="productListDataIdCallback" searchButtonId="productList_search_btn" formReset="changeOrderId">
						<div class="input-group" style="max-width: 1500px;">
							<input type="hidden" name="orderId" id="productOrderId" value=""/>
							<span class="input-group-addon">商品名称</span>
			            		<input type="text" id="product_name" name="productName" class="form-control" placeholder="请输入商品名称" style="width: 200px;">
			            	<span class="input-group-addon">商品条码</span>
								<input type="text" id="product_barCode" name="barCode" class="form-control" placeholder="请输入商品条码" style="width: 200px;">
						</div>
						<button type="button" onclick="getProductList();" class="btn btn-small btn-danger" style="width: auto;margin-top:5px;">确认</button>
					</m:list>
				</div>
			</div>
		</div>
	</div>

</body>
</html>