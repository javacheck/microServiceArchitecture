<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>经营汇总表</title>
<script type="text/javascript">

	$(function(){
		var isSys=${isSys};
		var isMainStore=${isMainStore};
		var storeIdCashe="${storeIdCashe}";
		var storeNameCashe="${storeNameCashe}";
		var sourceCashe="${sourceCashe}";
		if(storeIdCashe!='' && storeIdCashe!=null){
			$("#operationSummary_storeId").val(parseInt(storeIdCashe));
			$("#operationSummary_storeName").val(storeNameCashe);
		}
		if(sourceCashe!='' && sourceCashe!=null){
		$("#source").val(parseInt(sourceCashe));
		}
		// 点击弹出商家选择窗口
		$("#operationSummary_storeName").click(function (){
			$("#operationSummary_stockId").val("");
			$("#operationSummary_stockName").val("");
			$("#operationSummaryShowStore").modal();
		});
		$("#submitButton").click(function() {
			$("#operationSummaryId").submit();
		});
	});
	function callback(obj){
		$("#operationSummary_storeName").val(obj.name);
		$("#operationSummary_storeId").val(obj.id);
	}

	function formReset(){
		$("#operationSummary_storeName").val("");
		$("#operationSummary_storeId").val("");
		$("#source").val("");
	}
	
	
	
	function goReportSalesList(){
		var isMainStore=${isMainStore};
		if(isMainStore){
			window.location.href="${contextPath }/reportSales/list";
		}else{
			window.location.href="${contextPath }/reportSales/storeList";
		}
	}
	function goReportUserList(){
		var isMainStore=${isMainStore};
		if(isMainStore){
			window.location.href="${contextPath }/reportUser/list";
		}else{
			window.location.href="${contextPath }/reportUser/storeList";
		}
	}
	function goProductStockList(){
		window.location.href="${contextPath }/reportStock/list";
	}
	function getExe() {
		var flag=${isSys};
		var storeId="";
		if(flag){
			storeId=$("#operationSummary_storeId").val();//店铺
		}
		var source = $("#source option:selected").val(); //交易类型
		window.open("${contextPath}/operationSummary/list/ajax/list-by-search?storeId="+storeId+"&source="+source);
	}
</script>
<style>
	.table{
       width:1150px;
       padding-bottom: 10px;
       background-color:#F7F7F7;

    }
    .table-th{
        display:block !important;
        border-bottom:1px solid #ccc;border-left:none;margin:0;padding:0;margin-bottom:20px;
        width:100% !important;
    }
    .one td{
       background-color:#F7F7F7 !important; 
    }
    .one td{
        width:25%;
        float:left;
        box-sizing: border-box;
        display:inline-block;
       line-height: 35px;
       padding-left:20px;
       border-top:none;
       border-bottom:none;
       border-right:none;
       line-height: 20px;
       color: #333333;
        font-size: 13px;
        font-weight: 400;
    }
    .mal{
        border-left:none !important;
    }
    .exothecium{
        padding:0 20px;
    }
    
    .headline th{
        margin-bottom:20px;
        padding-left: 20px;
        padding-right: 20px;
        font-size:16px;
        border-top:none;
        border-right:none;
        border-left:none;
    }
    .headline th a{
        font-size: 12px;
    }
    .ma-bt td{
        margin-bottom:20px;
    }
    .text td{
        color: #ff6633;
        font-size: 20px;
        font-weight: 700;
    }
</style>
<style>
  .input-group2{
  float:left;width:auto; background-color: #e5e5e5;border: 1px solid #ccc;border-radius: 4px; color: #222;font-size: 13px;font-weight: 400; padding: 5px 12px;text-align: center;
  }
  </style>
</head>
<body>
	
		<div class="panel">
			<form id="operationSummaryId" method='post' action="${contextPath}/operationSummary/list">
			<div class="panel-heading">
				<strong><i class="icon-list-ul"></i>经营汇总表</strong>
				<div class="input-group" style="max-width: 1000px;">
					<c:choose>  
					   <c:when test="${isSys==true }">  
					   		<span class="input-group-addon">商家</span>
							<input name="storeName" id="operationSummary_storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="" class="form-control" isRequired="1" tipName="商家"  />
							<input type="hidden" name="storeId" id="operationSummary_storeId" value="" />
					   </c:when>  
					   <c:otherwise> 
					   		<m:hasPermission permissions="mainShopOrderStoreView">
					   			<c:if test="${isMainStore==true }">
									<span class="input-group-addon">商家</span>
									<input name="storeName" id="operationSummary_storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="" class="form-control" isRequired="1" tipName="商家"  />
									<input type="hidden" name="storeId" id="operationSummary_storeId" value="-1" />
								</c:if>
		       				</m:hasPermission>  
					   </c:otherwise>  
					</c:choose>
           			<span class="input-group-addon">交易类型</span>
		            <select name="source" class="form-control" style="width: 200px;margin-right:40px;float:left;" id="source" >
		           		<option  value ="">全部</option>
						<option  value ="0">APP</option>
						<option  value ="1">终端</option>
		           	</select>
		           	
				</div>
				<br />
				<div class="input-group">
					<button id="submitButton" class="btn btn-info"  name="submitButton" type="button">搜索</button>
					<button id="fresetButton" class="btn btn-success"  name="resetButton" type="button" onclick="formReset();">重置</button>
					<button id="userButton" class="btn btn-small btn-danger"  name="userButton" type="button" onclick="getExe();">导出报表</button>
				</div>
			</div>
			</form>
			<br />
			<div  class="exothecium" >
				<m:hasPermission permissions="reportSalesList">
				<table class='table table-hover table-striped table-bordered one' style="font-weight: 600;"  cellpading=0 cellspaceing=0>
				  <tr class="headline">
				  	<th class="table-th">销售统计概览<a  onclick="goReportSalesList();" style="float: right;cursor:pointer;">查看详情</a></th>
				  </tr>
				  <tr>
				    <td class="mal">今日销售额</td>
				    <td>本月销售额</td>
				    <td>上月销售额</td>
				    <td>今年销售额</td>
				  </tr>
				  <tr class="text">  
				    <td class="mal">￥<fmt:formatNumber value="${reportDateSales.salesNumSum }"  type="currency" pattern="0.00"/></td>
				    <td>￥<fmt:formatNumber value="${reportThisMonthSales.salesNumSum }"  type="currency" pattern="0.00"/></td>
				    <td>￥<fmt:formatNumber value="${reportLastMonthSales.salesNumSum }"  type="currency" pattern="0.00"/></td>
				    <td>￥<fmt:formatNumber value="${reportYearSales.salesNumSum }"  type="currency" pattern="0.00"/></td>
				  </tr>
				  <tr>  
				    <td class="mal">今日实销毛利</td>
				    <td>本月实销毛利</td>
				    <td>上月实销毛利</td>
				    <td>今年实销毛利</td>
				  </tr>
				  <tr class="text">  
				    <td class="mal">￥<fmt:formatNumber value="${reportDateSales.grossProfit }"  type="currency" pattern="0.00"/></td>
				    <td>￥<fmt:formatNumber value="${reportThisMonthSales.grossProfit }"  type="currency" pattern="0.00"/></td>
				    <td>￥<fmt:formatNumber value="${reportLastMonthSales.grossProfit }"  type="currency" pattern="0.00"/></td>
				    <td>￥<fmt:formatNumber value="${reportYearSales.grossProfit }"  type="currency" pattern="0.00"/></td>
				  </tr>
				  <tr> 
				    <td class="mal">今日订单数</td>
				    <td>本月订单数</td>
				    <td>上月订单数</td>
				    <td>今年订单数</td>
				  </tr>
				  <tr class="ma-bt text"> 
				    <td class="mal">${reportDateSales.orderNumSum }</td>
				    <td>${reportThisMonthSales.orderNumSum }</td>
				    <td>${reportLastMonthSales.orderNumSum }</td>
				    <td>${reportYearSales.orderNumSum }</td>
				  </tr>
				</table>
				</m:hasPermission>
				
				<m:hasPermission permissions="reportUserList">
				<table class='table table-hover table-striped table-bordered one' style="font-weight: 600;"  cellpading=0 cellspaceing=0>
				  <tr class="headline">
				  	<th class="table-th">会员统计概览<a  onclick="goReportUserList();" style="float: right;cursor:pointer;">查看详情</a></th>
				  </tr>
				  <tr>
				    <td class="mal">今日新增会员</td>
				    <td>本月新增会员</td>
				    <td>上月新增会员</td>
				    <td>今年新增会员</td>
				  </tr>
				  <tr class="text">
					    <td class="mal">${reportDateUser.totalUserNumSum==null?'0': reportDateUser.totalUserNumSum}</td>
					    <td>${reportThisMonthUser.totalUserNumSum==null?'0': reportThisMonthUser.totalUserNumSum }</td>
					    <td>${reportLastMonthUser.totalUserNumSum==null?'0': reportLastMonthUser.totalUserNumSum }</td>
					    <td>${reportYearUser.totalUserNumSum==null?'0': reportYearUser.totalUserNumSum }</td>
				   </tr>
				   <tr> 
					    <td class="mal">今日会员储值</td>
					    <td>本月会员储值</td>
					    <td>上月会员储值</td>
					    <td>今年会员储值</td>
				   </tr>
				   <tr class="text"> 
					    <td class="mal">￥<fmt:formatNumber value="${reportDateUser.rechargeSum==null?'0.00': reportDateUser.rechargeSum }"  type="currency" pattern="0.00"/></td>
					    <td>￥<fmt:formatNumber value="${reportThisMonthUser.rechargeSum==null?'0.00': reportThisMonthUser.rechargeSum }"  type="currency" pattern="0.00"/></td>
					    <td>￥<fmt:formatNumber value="${reportLastMonthUser.rechargeSum==null?'0.00': reportLastMonthUser.rechargeSum }"  type="currency" pattern="0.00"/></td>
					    <td>￥<fmt:formatNumber value="${reportYearUser.rechargeSum==null?'0.00': reportYearUser.rechargeSum }"  type="currency" pattern="0.00"/></td>
				   </tr>
				   <tr> 
					    <td class="mal">今日会员消费</td>
					    <td>本月会员消费</td>
					    <td>上月会员消费</td>
					    <td>今年会员消费</td>
				   </tr>
				   <tr class="text"> 
					    <td>￥<fmt:formatNumber value="${reportDateUser.consumptionSum==null?'0.00': reportDateUser.consumptionSum }"  type="currency" pattern="0.00"/></td>
					    <td>￥<fmt:formatNumber value="${reportThisMonthUser.consumptionSum==null?'0.00': reportThisMonthUser.consumptionSum }"  type="currency" pattern="0.00"/></td>
					    <td>￥<fmt:formatNumber value="${reportLastMonthUser.consumptionSum==null?'0.00': reportLastMonthUser.consumptionSum }"  type="currency" pattern="0.00"/></td>
					    <td>￥<fmt:formatNumber value="${reportYearUser.consumptionSum==null?'0.00': reportYearUser.consumptionSum }"  type="currency" pattern="0.00"/></td>
				  </tr>
				</table>
				</m:hasPermission>
				
				<m:hasPermission permissions="reportStockList">
				<table class='table table-hover table-striped table-bordered one' style="font-weight: 600;"  cellpading=0 cellspaceing=0>
				  <tr class="headline">
				  	<th class="table-th">库存统计概览<a  onclick="goProductStockList();" style="float: right;cursor:pointer;">查看详情</a></th>
				  </tr>
				  <tr>
				    <td class="mal">商品总数</td>
				    <td>商品总成本</td>
				    <td>预计销售额</td>
				    <td>预计毛利</td>
				  </tr>
				  <tr class="text"> 
				    <td class="mal">${reportProductStock.stockSum==null?'0':reportProductStock.stockSum }</td>
				    <td>￥<fmt:formatNumber value="${reportProductStock.costPriceSum==null?'0.00':reportProductStock.costPriceSum }"  type="currency" pattern="0.00"/></td>
				    <td>￥<fmt:formatNumber value="${reportProductStock.priceSum==null?'0.00':reportProductStock.priceSum }"  type="currency" pattern="0.00"/></td>
				    <td>￥<fmt:formatNumber value="${reportProductStock.grossProfit==null?'0.00':reportProductStock.grossProfit }"  type="currency" pattern="0.00"/></td>
				  </tr>
				</table>
				</m:hasPermission>
			</div>
		</div>
	
	<m:select_store path="${contextPath}/operationSummary/showModel/list/list-data" modeId="operationSummaryShowStore" callback="callback"> </m:select_store>
	
</body>
</html>