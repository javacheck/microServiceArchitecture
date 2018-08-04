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
<title>库存调拨</title>
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
        max-width: 1000px;
        min-width: 500px;
        

    }
    
.bor th,.bor tr,.bor td{
    border:none;
    text-align: center;
    line-height: 25px;
}
</style>
<script type="text/javascript">
var sortLength=1;
$(function(){
	var isMainStore=${isMainStore};
	$("#stockSelect").hide();
	var format = "yyyy-MM-dd";//默认格式
	var now = lm.Now;//当前日期
	var nowFormat=now.Format(format);
	$("#allocationTime").val(nowFormat);
	$("#allocationTime").datetimepicker({
		minView: "month", //选择日期后，不会再跳转去选择时分秒 
		format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
		language: 'zh-CN', //汉化 
		autoclose:true , //选择日期后自动关闭
		todayBtn:true,//可选择当天按钮
		todayHighlight:false,//高亮当前日期
		startDate:""+nowFormat+""//默认最后可选择为当前日期
		
	 });
	
	// 点击弹出商家选择窗口
	$("#allocation_fromStoreName").click(function (){
		$("#allocationShowFromStore").modal();
	});
	// 点击弹出商家选择窗口
	$("#allocation_toStoreName").click(function (){
		$("#allocationShowToStore").modal();
	});
	
	$("[name = 'stockName']").bind("click", function () {
		
		if($("#allocation_fromStoreId").val()==null || $("#allocation_fromStoreId").val()=="" || $("#allocation_fromStoreId").val()==-1){
			lm.alert("请先选择调出仓库！");
			return;
		  }else{
			    var storeId=$("#allocation_fromStoreId").val();
				$("#product_storeId").val(storeId);
				$('#stockNameAddModal').modal();
				$("#productStockList_search_btn").click();
		  }
		
		
	  }); 
	$("#allocationAddBtn").bind("click", function () {
		
		var fromStoreId=$("#allocation_fromStoreId").val();
		if($("#allocation_fromStoreId").val()==null || $("#allocation_fromStoreId").val()=="" || $("#allocation_fromStoreId").val()==-1){
			lm.alert("请先选择调出仓库！");
			return;
		 }
		var toStoreId;
		var toStoreName;
		if(isMainStore){
			toStoreId=$("#allocation_toStoreId").val();
			toStoreName=$("#allocation_toStoreName").val();
		}else{
			toStoreId=$("#storeId").val();
			toStoreName=$("#storeName").val();
		}
		if(toStoreId==null || toStoreId=="" || toStoreId==-1){
			lm.alert("请先选择调入仓库！");
			return;
		 }
		if(fromStoreId!=null && toStoreId!=null){
			if(fromStoreId==toStoreId){
				lm.alert("调出仓库和调入仓库不能相同！");
				return;
			}
		}
		
		var amounts="";
		var stockIds="";
		var flag=true;
		if($("input[name='amount']").length<1){
			lm.alert("请添加要调入仓库的商品！");
		}else{
			$("input[name='amount']").each(function(index,item){
				var amount=$.trim($(this).val());
				var sName=$(this).parent().prev().html();
				var stockAmount=$(this).parent().next().html();
				
				if(amount==null || amount==''){
					flag=false;
					lm.alert("商品:<span style='color:red;'>"+sName+"</span>调入数量不能为空！");
					return;
				}else if(isDouble(amount)==false ){
					flag=false;
					lm.alert("<span style='color:red;'>"+sName+"</span>调入仓库的商品数量必须为整数或是两位小数的数字！");
					return;
				}else if(amount<=0){
					flag=false;
					lm.alert("<span style='color:red;'>"+sName+"</span>调入仓库的商品数量必须为大于0！");
					return;
				}else{
					if(stockAmount!='无限'){
						if(Number(amount)>Number(stockAmount)){
							lm.alert("<span style='color:red;'>"+sName+"</span>库存的数量不够！");
							flag=false;
							return;
						}else{
							amounts += $(this).val()+",";
							stockIds +=$(this).parent().attr("value")+",";
						}
					}else{
						amounts += $(this).val()+",";
						stockIds +=$(this).parent().attr("value")+",";
					}
				}
			});
		}
		if(flag){
			amounts=amounts.substring( 0,amounts.length-1);
			stockIds=stockIds.substring( 0,stockIds.length-1);
		}else{
			return;
		}
		if(stockIds!=''){
			lm.postSync("${contextPath}/allocation/findStockExist",{toStoreId:toStoreId,stockIds:stockIds},function(data){
				if(data!=null && data!=''){
					var _flag=data[0].split(",")[1];
					if(_flag=="0"){
						lm.alert("商品"+data[0].split(",")[0]+"在商店"+toStoreName+"中不存在！请检查！");
						flag = false;
						return;
					}else if(_flag=="1"){
						lm.alert("商品"+data[0].split(",")[0]+"在"+toStoreName+"的库存是无限，不需要调拨！请检查！");
						flag = false;
						return;
					}
					
				}
			}); 
		}
		var allocationTime=$("#allocationTime").val();
		var memo=$("#memo").val();
		if(amounts!='' && flag==true){
			$("#allocationAddBtn").prop("disabled",true);
			lm.post("${contextPath}/allocation/save",{fromStoreId:fromStoreId,toStoreId:toStoreId,stockIds:stockIds,amounts:amounts,allocationTime:allocationTime,memo:memo},function(data){
				if(data=='1'){
			    	lm.alert("操作成功！");
			    	 window.location.href="${contextPath}/allocation/list";
				}
			}); 
		}
		
	 })
});
function fromCallback(obj){
	if ($("#allocation_fromStoreId").val() != obj.id){
		$("#tbAmount").html("");
		$("#stockSelect").hide();
	}
	if($("#allocation_toStoreId").val()!=null && $("#allocation_toStoreId").val()!=""){
		if($("#allocation_toStoreId").val()==obj.id){
			lm.alert("调出仓库和调入仓库不能一样！");
			return;
		}else{
			$("#allocation_fromStoreName").val(obj.name);
			$("#allocation_fromStoreId").val(obj.id);
		}
	}else{
		$("#allocation_fromStoreName").val(obj.name);
		$("#allocation_fromStoreId").val(obj.id);
	}
}
function toCallback(obj){
	if($("#allocation_fromStoreId").val()!=null && $("#allocation_fromStoreId").val()!=""){
		if($("#allocation_fromStoreId").val()==obj.id){
			lm.alert("调出仓库和调入仓库不能一样！");
			return;
		}else{
			$("#allocation_toStoreName").val(obj.name);
			$("#allocation_toStoreId").val(obj.id);
		}
	}else{
		$("#allocation_toStoreName").val(obj.name);
		$("#allocation_toStoreId").val(obj.id);
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
	/* $("#productListDataId").find("tbody tr").click(function(){
		$("#allocation_stockId").val($(this).attr("val"));
		$("#allocation_stockName").val(($.trim($($(this).find("td")[0]).html())));
		$("#productstockListModalBtn").click();
	}); */
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
	    		var sName=$(this).parent().prev().html();
				if(arrPss[i]==$(this).parent().attr("value")){
					lm.alert("<span style='color:red;'>"+sName+"</span>已调入！");
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
    	lm.post("${contextPath }/allocation/findProductStockList-by-productStockIds",{productStockIds:productStockIds},function(data){
			for(var i=0;i<data.length;i++){
				if(data[i].stock>=0){
					var s=data[i].stock-data[i].alarmValue;
					if(Number(s)<0){
						s=0;
					}
					$("#tbAmount").append("<tr>"+
										"<td>"+(sortLength)+"</td>"+
										"<td>"+data[i].productName+"</td>"+
										"<td>"+data[i].attributeValues+"</td>"+
										"<td>"+data[i].barCode+"</td>"+
										"<td value='"+data[i].id+"'><input type='text' id='amount' name='amount'   style='width:50px;height:30px;' maxlength='7'/></td>"+
										"<td>"+s+"</td>"+
										"<td><button type='button'  onclick='del(this)' class='btn btn-small btn-danger' style='width: auto;height:30px;'>删除</button></td>"+
										"</tr>");
				}else{
					$("#tbAmount").append("<tr>"+
							"<td>"+(sortLength)+"</td>"+
							"<td>"+data[i].productName+"</td>"+
							"<td>"+data[i].attributeValues+"</td>"+
							"<td>"+data[i].barCode+"</td>"+
							"<td value='"+data[i].id+"'><input type='text' id='amount' name='amount'   style='width:50px;height:30px;' maxlength='7'/></td>"+
							"<td>无限</td>"+
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
			<strong><i class='icon-plust'></i>库存调拨 </strong>
		</div>
		<div class='panel-body'>
			<div  class='form-horizontal'>
				<form id="allocationAddForm" method='post' class='form-horizontal' repeatSubmit='1'
				action="${contextPath }/allocation/save">
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>调出仓库</label>
						<div class="col-md-2">
							<input name="fromSName" id="allocation_fromStoreName" readonly="readonly" value="选择调出仓库" class="form-control" isRequired="1"  />
							<input type="hidden" name="fromStoreId" id="allocation_fromStoreId" value="" />
						</div>
						<c:if test="${isMainStore==true}">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>调入仓库</label>
						<div class="col-md-2">
							<input name="toSName" id="allocation_toStoreName" readonly="readonly" value="选择调入仓库" class="form-control" isRequired="1"  />
							<input type="hidden" name="toStoreId" id="allocation_toStoreId" value="" />
						</div>
						</c:if>
						<c:if test="${isMainStore==false}">
							<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>调入仓库</label>
							<div class="col-md-2">
							<input name="toSName" id="storeName" readonly="readonly" value="${store.name }" class="form-control" isRequired="1"  />
							<input type="hidden" name="toStoreId" id="storeId" value="${store.id }" />
						</div>
						</c:if>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>商品</label>
						<div class="col-md-2">
							<input name="stockName" id="allocation_stockName" style="width: 200px;margin-right:40px;" readonly="readonly" value="选择商品" class="form-control" isRequired="1" tipName="商家"  />
							<input type="hidden" name="stockId" id="allocation_stockId" value="" />
						</div>
					</div>
					<div class="form-group">
					<label class="col-md-1 control-label">&nbsp;</label>
					<div id="stockSelect" style="width: 800px;max-height:450px;overflow-y: auto;overflow-x:auto; border:1px solid #ccc;">
						<table  class="bor">
							<thead>
								<tr> 
									<th>序号</th>
									<th>商品名称</th>
									<th>规格</th>
									<th>商品条码</th>
									<th>调拨数量</th>
									<th>调出方可调库存</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody id="tbAmount">
									
							</tbody>
						</table>
					</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>调拨时间</label>
						<div class="col-md-2">
							<input id="allocationTime" value="" type="text" name="allocationTime" class="form-control" readonly style="width: 200px;">
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label">备注</label>
						<div class="col-md-2">
							<textarea id='memo' name='memo' value='' alt='最大输入字数为150个字符' cols=15 rows=5 class='form-control' maxlength='150'>${productStock.remarks }</textarea>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-offset-1 col-md-10">
							<input type="button"  repeatSubmit='1' id='allocationAddBtn' class='btn btn-primary' 
								value="${empty allocation ? '保存' : '修改' }" data-loading='稍候...' />
								<a href="javascript:history.go(-1);" class="btn btn-small btn-danger">返回</a>
						</div>
					</div>
				</form>
				<m:select_store path="${contextPath}/allocation/showModel/list/listFrom-data" modeId="allocationShowFromStore" callback="fromCallback"> </m:select_store>
				<m:select_store path="${contextPath}/allocation/showModel/listStore/list-data" modeId="allocationShowToStore" callback="toCallback"> </m:select_store>
				<!-- 模态窗 -->
				<div class="modal fade" id="stockNameAddModal">
					<div class="modal-dialog modal-lg" style="width: 1200px;">
					  <div class="modal-content">
						    <div class="modal-header">
						      <button type="button" class="close" data-dismiss="modal" id="productstockListModalBtn"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
						      <h4 class="modal-title"></h4>
						    </div>
							<div class="modal-body">
								<m:list title="商品列表" id="productStockList" listUrl="${contextPath }/allocation/stockList/list-data" callback="productListDataIdCallback"  searchButtonId="productStockList_search_btn" >
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