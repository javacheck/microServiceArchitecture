<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- ztree.core核心包 -->
	<script type="text/javascript" src="${staticPath }/js/ztree/jquery.ztree.core-3.5.js"></script>
	<!-- 升级树控件 -->
	<script type="text/javascript" src="${staticPath }/js/ztree/jquery.ztree.exhide-3.5.js"></script>
	<!-- 树形样式 -->
	<link rel="stylesheet" href="${staticPath }/css/ztree/zTreeStyle/zTreeStyle.css" type="text/css">	

<title>退货商品统计</title>
<style type="text/css">
	.input-group2{
	float:left;width:auto; background-color: #e5e5e5;border: 1px solid #ccc;border-radius: 4px; color: #222;font-size: 13px;font-weight: 400; padding: 5px 12px;text-align: center;
	}
	.table{
	   	background-color:#F7F7F7;
	}
	.mal{
	  border-left:none !important;
	}
	.lines td{
	  line-height: 15px; 
	  background-color:#F7F7F7;
	  order-top:none;
	  border-bottom:none;
	  border-right:none;
	  padding-left:20px;
	  float:left;
	}
	.lines-tr{
	  border:none;
	  font-weight: 1500px; 
	  background-color:#F7F7F7;
	  color: #ff3333;
	  font-size: 20px;
	  font-weight: 700;
	}
	.lines-tr td{
	  margin-bottom:20px;
	  margin-top:0;
	  border-bottom:none;
	}
	.mag{
		border:none;
	}
	.mag td{
	    margin-top:20px;
	    border-bottom:none;
	    border-right:none;
	    
	    border-top:none;
	}
	
    .one td{
        width:11%;
        box-sizing: border-box;
        display:inline-block;
        padding-left: 20px;
       line-height: 35px;
       border-bottom:none;
       border-top:none;
       
       
       
    }
   .two{
    	position: relative;
/*     	display: block; */
    }
     

		.arrows{
		            width: 0;
		            height: 0;
		            border-left: 7px solid transparent;
		            border-right: 7px solid transparent;
		            border-bottom: 8px solid  #5B5B5B;
		            position: absolute;
		            right: 8px;
		            top: 9px;
		            cursor: pointer;
		        }
	        .boult{
	            right: 8px;
	            top: 18px;
	            border-bottom:none;
              	border-top: 8px solid  #5B5B5B;
              	cursor: pointer;
	              
	        }
</style>
<script type="text/javascript">
var _isMain = false;
<c:if test="${isMainStore==true }">
var _mainStoreId = "${account_session_key.storeId}";
_isMain = true;
</c:if>

function callback(obj){
	$("#order_storeName").val(obj.name);
	$("#order_storeId").val(obj.id);
	$("#treeStoreId").val(obj.id);
	
	if (_mainStoreId == obj.id){
		$("#categoryId").find("option").val("");
		$("#categoryId").find("option").text("全部");
		$("#categoryId").prop("disabled",true);
	}else {
		$("#categoryId").prop("disabled",false);
	}
}
function deleteId(id){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/returnGoodsRecordStatistical/delete/delete-by-Id",{id:id},function(data){
			if(data==1){
				lm.alert("删除成功！");
			} else {
				lm.alert("删除失败！");
			}
			loadCurrentList_returnGoodsRecordStatisticalList();
		});
	});
}

$(document).ready(function(){
	// 点击弹出商家选择窗口
	$("#order_storeName").click(function (){
		$("#orderShowStore").modal();
	});
	
	if (_isMain){
		$("#categoryId").prop("disabled",true);
	}
});

$(function(){
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
});

function callback1(){
	$("[name='numAsc']").click(function (){
		$('.boult').css('border-top', '8px solid #5B5B5B');
        $('.boult').css('border-bottom', '8px solid #5B5B5B');
        var sortValue=$(this).attr("value");
        $("#sort").val(sortValue);
        loadCurrentList_returnGoodsRecordStatisticalList();
       
		
	});
	$("[name='numAsc']").each(function(index,item){
		if($(this).attr("value")==$("#sort").val()){
			if($(this).attr("value")%2==0){
				$(this).css('border-top', '8px solid #0080FF');
			}else{
				$(this).css('border-bottom', '8px solid #0080FF');
			}
		}
	});
}

function formRest(){
	$("#treeStoreId").val("");
	$("#categoryId").find("option").val("");
	$("#categoryId").find("option").text("全部");
}


$(function(){
	$("select[name='dateType']").change(function(){
		var date = $(this).val();
		if (date == "0"){
			$("#beginTime").val("${dayStart}");
			$("#endTime").val("${dayStart}");
		}else if (date == "1"){
			$("#beginTime").val("${monthStart}");
			$("#endTime").val("${monthEnd}");
		}else if (date == "2"){
			$("#beginTime").val("${preMonthStart}");
			$("#endTime").val("${preMonthEnd}");
		}else if (date == "3"){
			$("#beginTime").val("${yearStart}");
			$("#endTime").val("${yearEnd}");
		}
	});
	
	$("#beginTime").blur(function(){
		$("select[name='dateType']").val("");
	});
	
	$("#endTime").blur(function(){
		$("select[name='dateType']").val("");
	});
});
function getExe() {
	var storeId=$("#storeId").val();//店铺
	if( null == storeId || undefined == storeId ){
		storeId = "";
	} 
	var dateType=$("#dateType").val();//订单编号
	var productName=$("#productName").val();//商品名称
	var barcode=$("#barcode").val();//条码
	var categoryId=$("#categoryId").val();//分类
	var beginTime =$("#beginTime").val();//开始时间
	var endTime = $("#endTime").val();//结束时间
	var sort=$("#sort").val();//排序
	window.open("${contextPath}/returnGoodsRecordStatistical/list/ajax/list-by-search?beginTime="+beginTime+"&endTime="+endTime
					+"&storeId="+storeId+"&productName="+productName+"&barcode="+barcode
					+"&dateType="+dateType+"&categoryId="+categoryId+"&sort="+sort);
}
</script>
</head>
<body>
	<m:list title="退货商品统计" id="returnGoodsRecordStatisticalList" callback="callback1" formReset="formRest"
		listUrl="${contextPath }/returnGoodsRecordStatistical/list-data"
		searchButtonId="returnGoodsRecordStatistical_cateogry_search_btn">
		
		<div class="input-group" style="max-width: 600px;">
		<input type="hidden" name="sort" id="sort" value="0" />
   			<c:if test="${isMainStore==true }">
				<span class="input-group-addon">商家</span>
				<input name="storeName" id="order_storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="" class="form-control" isRequired="1" tipName="商家"  />
				<input type="hidden" name="storeId" id="order_storeId" value="" />
			</c:if>
           
            <span class="input-group-addon">商品名称</span> 
            <input type="text" id="productName" name="productName" class="form-control" placeholder="商品名称" style="width: 200px;margin-right:40px;">
            
            <span class="input-group-addon">商品条码</span> 
            <input type="text" id="barcode" name="barcode" class="form-control" placeholder="商品条码" style="width: 200px;margin-right:40px;">
            
            <span class="input-group-addon">快速查看</span> 
            <select name="dateType" class="form-control" style="width: auto;margin-right:40px;" id="dateType">
				<option  value ="">请选择</option>
           		<option  value ="0">今日</option>
				<option  value ="1" selected="selected">本月</option>
				<option  value ="2">上月</option>
				<option  value ="3">本年</option>
           	</select>
            
	        
	        </div>
			<br/>
			<div class="input-group" style="max-width: 1000px;">	  
            <span class="input-group-addon">商品分类</span>
	        	<m:treeSelect treeRoot="treeRoot" showAllOption="1" selectID="categoryId"></m:treeSelect>
	        	<input type="hidden" name="treeStoreId" id="treeStoreId" value="">
				
	        <span  class="input-group-addon">开始时间</span> 
			<input value="${monthStart }" id = "beginTime" type="text" name="beginTime" class="form-control" placeholder="选择开始日期" readonly  style="width: 200px;margin-right:40px;">
			
			<span  class="input-group-addon">结束时间</span> 
			<input value="${monthEnd }" id = "endTime" type="text" name="endTime" class="form-control" placeholder="选择结束日期" readonly style="width: 200px;margin-right:40px;">
		</div>
		<button type="button" onclick="getExe()" class="btn btn-small btn-danger" style="width: auto;margin-top:5px;">导出</button>
	</m:list>
	<m:select_store path="${contextPath}/order/showModel/list/list-data" modeId="orderShowStore" callback="callback"> </m:select_store>
</body>
</html>