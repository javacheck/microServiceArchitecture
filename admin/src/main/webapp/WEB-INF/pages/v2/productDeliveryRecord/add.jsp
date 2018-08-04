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
<title>商品出库</title>
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
var sortLength=1;
var supplierData;
$(function(){
	$("#stockSelect").hide();
	var format = "yyyy-MM-dd";//默认格式
	var now = lm.Now;//当前日期
	var nowFormat=now.Format(format);
	$("#deliveryTime").val(nowFormat);
	$("#deliveryTime").datetimepicker({
		minView: "month", //选择日期后，不会再跳转去选择时分秒 
		format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
		language: 'zh-CN', //汉化 
		autoclose:true , //选择日期后自动关闭
		todayBtn:true,//可选择当天按钮
		todayHighlight:false,//高亮当前日期
		startDate:""+nowFormat+""//默认最后可选择为当前日期
		
	 });
	
	$("[name = 'stockName']").bind("click", function () {
		$('#stockNameAddModal').modal();
		$("#productStockList_search_btn").click();
	}); 
	$("#productDeliveryRecordAddBtn").bind("click", function () {
		var list= new Array();
		var amounts="";
		var stockIds="";
		var costPrices="";
		var supplierNames="";
		var flag=true;
		if($("input[name='amount']").length<1){
			lm.alert("请添加要出库的商品！");
			return;
		}else{
			$("input[name='amount']").each(function(index,item){
				var obj =new Object();
				var stockId=$(this).parent().attr("value");
				var weighing=$(this).parent().parent().find("td").first().attr("value");
				var productName=$(this).parent().parent().find("td[valueName='a']").html();
				var barCode=$(this).parent().parent().find("td[valueName='d']").html();
				var attributeValues=$(this).parent().parent().find("td[valueName='b']").html();
				var unitName=$(this).parent().parent().find("td[valueName='c']").html();
				var costPrice=$(this).parent().parent().find("td[valueName='f']").html();
				var shipmentPrice=$.trim($(this).parent().prev().find("input").val());
				var amount=$.trim($(this).val());
				var stock=$(this).parent().next().html();
				var memo=$(this).parent().next().next().find("input").val();
				if(shipmentPrice!=null && shipmentPrice!=''){
					if( isDouble(shipmentPrice)==false){
						flag=false;
						lm.alert("<span style='color:red;'>"+productName+"</span>出库价必须为整数或是两位小数的数字！");
						return;
					}else if(shipmentPrice<=0){
						flag=false;
						lm.alert("<span style='color:red;'>"+productName+"</span>出库价必须为大于0！");
						return;
					}
				}else if(amount==null || amount==''){
					flag=false;
					lm.alert("商品:<span style='color:red;'>"+productName+"</span>出库数量不能为空！");
					return;
				}
				if(weighing==0){
					if(!(/^[0-9]\d*$/.test(amount))){
						flag=false;
						lm.alert("<span style='color:red;'>"+productName+"</span>出库数量必须为大于0的整数！");
						return;
					}else if(amount==0){
						flag=false;
						lm.alert("<span style='color:red;'>"+productName+"</span>出库数量必须为大于0的整数！");
						return;
					}else{
						if(amount-stock<=0){
							obj.stockId= stockId;
							obj.productName =productName;
							obj.barCode=barCode;
							obj.attributeValues= attributeValues;
							obj.unitName=unitName;
							obj.costPrice=costPrice;
							obj.shipmentPrice=shipmentPrice;
							obj.amount=amount;
							obj.stock=stock;
							obj.memo=memo;
							list.push(obj);
						}else{
							flag=false;
							lm.alert("库存不足！");
							return;
						}
					}
				}else if(weighing==1){
					if(!isDouble(amount)){
						flag=false;
						lm.alert("<span style='color:red;'>"+productName+"</span>出库数量必须小于两位小数的数字！");
						return;
					}else if(amount<0){
						flag=false;
						lm.alert("<span style='color:red;'>"+productName+"</span>出库数量必须为大于0！");
						return;
					}else{
						if(amount-stock<=0){
							obj.stockId= stockId;
							obj.productName =productName;
							obj.barCode=barCode;
							obj.attributeValues= attributeValues;
							obj.unitName=unitName;
							obj.costPrice=costPrice;
							obj.shipmentPrice=shipmentPrice;
							obj.amount=amount;
							obj.stock=stock;
							obj.memo=memo;
							list.push(obj);
						}else{
							flag=false;
							lm.alert("库存不足！");
							return;
						}
					}
				}
			});
		}
		var deliveryTime=$("#deliveryTime").val();
		var memo=$("#deliveryMemo").val();
		if(flag){
			$("#productDeliveryRecordAddBtn").prop("disabled",true);
			lm.post("${contextPath}/productDeliveryRecord/save",{dateProductStockSJson:JSON.stringify(list),deliveryTime:deliveryTime,memo:memo},function(data){
				if(data=='1'){
			    	lm.alert("操作成功！");
			    	 window.location.href="${contextPath}/productDeliveryRecord/list";
				}
			}); 
		}
		
	 })
});

function productListDataIdCallback(){
	$("#all").click(function(){
	    if(this.checked){    
	        $("input:checkbox").prop("checked", true);   
	    }else{    
	    	$("input:checkbox").prop("checked", false); 
	    }    
	});
}
function getStockListByBarCode(){
	var barCode=$("#barCode").val();
	if(barCode!=null && barCode!=""){
		lm.post("${contextPath }/productDeliveryRecord/findProductStockList-by-barCode",{barCode:barCode},function(data){
			if(data.length>0){
				
			    if($("input[name='amount']").length>0){
					var flag=true;
			    	for(var i=0;i<data.length;i++){
				    	$("input[name='amount']").each(function(index,item){
							if(data[i].id==$(this).parent().attr("value")){
								if(data[i].id == $(this).parent().attr("value")){
					    			flag=false;
					    			return;
					    		}
							}
						});
				    	if(flag){
				    		var attributeValues=data[i].attributeValues==null?"":data[i].attributeValues;
							var unitName=data[i].unitName==null?"":data[i].unitName;
							var costPrice=data[i].costPrice==null?"":data[i].costPrice;
							$("#tbAmount").append("<tr>"+
												"<td value='"+data[i].weighing+"'>"+(sortLength)+"</td>"+
												"<td valueName='a'>"+data[i].productName+"</td>"+
												"<td valueName='d'>"+data[i].barCode+"</td>"+
												"<td valueName='b'>"+attributeValues+"</td>"+
												"<td valueName='c'>"+unitName+"</td>"+
												"<td valueName='f'>"+costPrice+"</td>"+
												"<td><input type='text' id='shipmentPrice' name='shipmentPrice'  value='' style='width:50px;height:30px;' maxlength='7'/></td>"+
												"<td value='"+data[i].id+"'><input type='text' id='amount' name='amount'   style='width:50px;height:30px;' maxlength='7'/></td>"+
												"<td>"+data[i].stock+"</td>"+
												"<td><input type='text' id='memo' name='memo'   value=''  maxlength='100'/></td>"+
												"<td><button type='button'  onclick='del(this)' class='btn btn-small btn-danger' style='width: auto;height:30px;'>删除</button></td>"+
												"</tr>");
							sortLength=sortLength+1;
				    	}else{
				    		flag=true;
				    	}
			    	}
			    }else{
			    	for(var i=0;i<data.length;i++){
						var attributeValues=data[i].attributeValues==null?"":data[i].attributeValues;
						var unitName=data[i].unitName==null?"":data[i].unitName;
						var costPrice=data[i].costPrice==null?"":data[i].costPrice;
						$("#tbAmount").append("<tr>"+
											"<td value='"+data[i].weighing+"'>"+(sortLength)+"</td>"+
											"<td valueName='a'>"+data[i].productName+"</td>"+
											"<td valueName='d'>"+data[i].barCode+"</td>"+
											"<td valueName='b'>"+attributeValues+"</td>"+
											"<td valueName='c'>"+unitName+"</td>"+
											"<td valueName='f'>"+costPrice+"</td>"+
											"<td><input type='text' id='shipmentPrice' name='shipmentPrice'  value='' style='width:50px;height:30px;' maxlength='7'/></td>"+
											"<td value='"+data[i].id+"'><input type='text' id='amount' name='amount'   style='width:50px;height:30px;' maxlength='7'/></td>"+
											"<td>"+data[i].stock+"</td>"+
											"<td><input type='text' id='memo' name='memo'   value=''  maxlength='100'/></td>"+
											"<td><button type='button'  onclick='del(this)' class='btn btn-small btn-danger' style='width: auto;height:30px;'>删除</button></td>"+
											"</tr>");
						sortLength=sortLength+1;
				    }
				    $("#stockSelect").show();
			    }
			}else{
				lm.alert("该条码没有对应的商品！");
			}
		});
	}else{
		lm.alert("请输入条码！");
	}

}
function getStockList(){
	
    var productStockIds="";
    $("input[name='listProudctStockName']:checked").each(function(i){ 
    	productStockIds+=$(this).val()+","; 
    });
    var arrPss=productStockIds.split(",");
    var flag=true;
    if($("input[name='amount']").length>0){
    	for(var i=0;i<arrPss.length;i++){
	    	$("input[name='amount']").each(function(index,item){
	    		var sName=$(this).parent().parent().find("td[valueName='a']").html();
				if(arrPss[i]==$(this).parent().attr("value")){
					lm.alert("<span style='color:red;'>"+sName+"</span>已选择！");
					flag=false;
					return;
				}
			});
    	}
    }
    if(!flag){
    	return;
    }
    productStockIds=productStockIds.substring( 0,productStockIds.length-1);
    if(productStockIds!=""){
    	lm.post("${contextPath }/productDeliveryRecord/findProductStockList-by-productStockIds",{productStockIds:productStockIds},function(data){
			for(var i=0;i<data.length;i++){
				if(data[i].stock>=0){
					var attributeValues=data[i].attributeValues==null?"":data[i].attributeValues;
					var unitName=data[i].unitName==null?"":data[i].unitName;
					var costPrice=data[i].costPrice==null?"":data[i].costPrice;
					$("#tbAmount").append("<tr>"+
							"<td value='"+data[i].weighing+"'>"+(sortLength)+"</td>"+
							"<td valueName='a'>"+data[i].productName+"</td>"+
							"<td valueName='d'>"+data[i].barCode+"</td>"+
							"<td valueName='b'>"+attributeValues+"</td>"+
							"<td valueName='c'>"+unitName+"</td>"+
							"<td valueName='f'>"+costPrice+"</td>"+
							"<td><input type='text' id='shipmentPrice' name='shipmentPrice'  value='' style='width:50px;height:30px;' maxlength='7'/></td>"+
							"<td value='"+data[i].id+"'><input type='text' id='amount' name='amount'   style='width:50px;height:30px;' maxlength='7'/></td>"+
							"<td>"+data[i].stock+"</td>"+
							"<td><input type='text' id='memo' name='memo'   value=''  maxlength='100'/></td>"+
							"<td><button type='button'  onclick='del(this)' class='btn btn-small btn-danger' style='width: auto;height:30px;'>删除</button></td>"+
							"</tr>");
				}
				sortLength=sortLength+1;
			}
			$("#stockSelect").show();
		});
    	$("#productstockListModalBtn").click();
    }else{
    	lm.alert("请选择商品！");
    	return;
    }

}
function del(divName){
	sortLength=sortLength-1;
	$(divName).parent().parent().remove();
	$("#tbAmount").find("tr").each(function(index,value){
		$(value).find("td").first().html(index+1);
	});
	if($.trim($("#tbAmount").html())==''){
		$("#stockSelect").hide();
	}
}
function isDouble(doubleName){
	var length = doubleName.length;
    if(doubleName.indexOf(".")>0){
	        var first = doubleName.indexOf(".");//判断第一个小数点所在位置
	        var last = doubleName.lastIndexOf(".");//判断最后一个小数点所在的位置
	        var temp_length = doubleName.split(".").length - 1;//含有.的个数
	        if(!isNaN(doubleName) && (temp_length == 1) && (first==last) && (length - last <=3) ){
	            return true;
	        }else{
	        	return false;
	        }
    }else{
    	return true;
    }
}
</script>
</head>
<body>

	<div class='panel'>
		<div class='panel-heading'>
			<strong><i class='icon-plust'></i>商品出库 </strong>
		</div>
		<div class='panel-body'>
			<div  class='form-horizontal'>
				<form id="productDeliveryRecordAddForm" method='post' class='form-horizontal' repeatSubmit='1'
				action="${contextPath }/productDeliveryRecord/save">
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>出库商品</label>
						<div class="col-md-2">
							<input type="text" id="barCode" name="barCode" value="" class='form-control' maxlength="120"  placeholder="请输入商品条码搜索"/>
						</div>
						<div class="col-md-1">
							<button type="button" onclick="getStockListByBarCode();" class="btn btn-primary" style="width: auto;">确认</button>
						</div>
						<div class="col-md-1">
							<input name="stockName" id="productDeliveryRecord_stockName" style="width: 200px;text-align: center;margin-right:40px;" readonly="readonly" value="批量选择商品" class="form-control" isRequired="1" tipName="商家"  />
							<input type="hidden" name="stockId" id="productDeliveryRecord_stockId" value="" />
						</div>
					</div>
					
					<div class="form-group">
					<label class="col-md-1 control-label">&nbsp;</label>
					<div id="stockSelect" class="col-md-11" style="max-height:450px;overflow-y: auto;overflow-x:auto; border:1px solid #ccc;">
						<table  class="bor">
							<thead>
								<tr> 
									<th>序号</th>
									<th>商品名称</th>
									<th>商品条码</th>
									<th>规格</th>
									<th>单位</th>
									<th>进货价</th>
									<th>出库价</th>
									<th><span style="color: red;font-size: 15px">*</span>出库数量</th>
									<th>库存数量</th>
									<th>商品备注</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody id="tbAmount">
									
							</tbody>
						</table>
					</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>出库时间</label>
						<div class="col-md-2">
							<input id="deliveryTime" value="" type="text" name="deliveryTime" class="form-control" readonly style="width: 200px;">
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label">出库备注</label>
						<div class="col-md-2">
							<textarea id='deliveryMemo' name='deliveryMemo' value='' alt='最大输入字数为150个字符' cols=15 rows=5 class='form-control' maxlength='150'></textarea>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-offset-1 col-md-10">
							<input type="button"  repeatSubmit='1' id='productDeliveryRecordAddBtn' class='btn btn-primary' 
								value="${empty productDeliveryRecord ? '确定' : '修改' }" data-loading='稍候...' />
								<a href="javascript:history.go(-1);" class="btn btn-small btn-danger">返回</a>
						</div>
					</div>
				</form>
				<!-- 模态窗 -->
				<div class="modal fade" id="stockNameAddModal">
					<div class="modal-dialog modal-lg" style="width: 1200px;">
					  <div class="modal-content">
						    <div class="modal-header">
						      <button type="button" class="close" data-dismiss="modal" id="productstockListModalBtn"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
						      <h4 class="modal-title"></h4>
						    </div>
							<div class="modal-body">
								<m:list title="商品列表" id="productStockList" listUrl="${contextPath }/productDeliveryRecord/stockList/list-data" callback="productListDataIdCallback"  searchButtonId="productStockList_search_btn" >
									<div class="input-group" style="max-width: 1500px;">
										 <input type="hidden" id="product_storeId" name="product_storeId" value=""/> 
										 <span class="input-group-addon">商品名称</span> 
						            	 <input type="text" id="product_name" name="product_name" class="form-control" placeholder="商品名称" style="width: 200px;">
									</div>
								<button type="button" onclick="getStockList();" class="btn btn-small btn-danger" style="width: auto;margin-top:5px;">确认</button>
								</m:list>
							</div>
						</div>
					</div>
				</div>
				<!-- 模态窗 -->
			</div>
		</div>
	</div>

</body>
</html>