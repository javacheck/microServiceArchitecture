<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品入库</title>
<script type="text/javascript">
	function callback(obj){
		$("#productStorageRecord_storeName").val(obj.name);
		$("#productStorageRecord_storeId").val(obj.id);
	}
	function formReset(){
		$("#productStorageRecord_storeName").val("");
		$("#productStorageRecord_storeId").val("");
	}
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
			todayHighlight:false //高亮当前日期
			//endDate:""+nowFormat+""//默认最后可选择为当前日期
			
		 });
		$("#endTime").datetimepicker({
			minView: "month", //选择日期后，不会再跳转去选择时分秒 
			format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
			language: 'zh-CN', //汉化 
			autoclose:true, //选择日期后自动关闭 
			todayBtn:true,//可选择当天按钮
			todayHighlight:false //高亮当前日期
			//endDate:""+nowFormat+""//默认最后可选择为当前日期
		 });
		var endDateInput=nowFormat;
		//判断输入结束日期时候输入合法
		$('#endTime').datetimepicker().on('changeDate', function(ev){
			if((new Date(ev.date.valueOf()))<(new Date($('#beginTime').val().replace(/-/g,"/")))){//开始时间大于结束时间
				lm.alert("结束时间不能大于开始时间");
				$("#endTime").val("");//默认值上一次输入
				$('#endTime').datetimepicker('update');//更新
			}
			endDateInput= $("#endTime").val();
			$('#beginTime').datetimepicker('setEndDate', $("#endTime").val());//设置开始时间最后可选结束时间
		});
		// 点击弹出商家选择窗口
		$("#productStorageRecord_storeName").click(function (){
			$("#productStorageRecordShowStore").modal();
		});
	});
	//修改选择的店铺时触发
	function byStoreAjaxRefreshList(){
		loadList_productStorageRecordList(); 
	}
	
	function getExe() {
		var storageNumber=$("#storageNumber").val();//入库单号
		var mobile=$("#mobile").val();//操作人
		var beginTime =$("#beginTime").val();//开始时间
		var endTime = $("#endTime").val();//结束时间
		
		
		window.open("${contextPath}/productStorageRecord/list/ajax/list-by-search?beginTime="+beginTime+"&endTime="+endTime
						+"&storageNumber="+storageNumber+"&mobile="+mobile);
	}
</script>
<style>
  .input-group2{
  float:left;width:auto; background-color: #e5e5e5;bproductStorageRecord: 1px solid #ccc;bproductStorageRecord-radius: 4px; color: #222;font-size: 13px;font-weight: 400; padding: 5px 12px;text-align: center;
  }
</style>
</head>
<body>
	<m:hasPermission permissions="productStorageRecordAdd" flagName="addFlag"/>
	<m:list addName = "新增入库" title="商品入库" id="productStorageRecordList"
		listUrl="${contextPath }/productStorageRecord/list/list-data" 
		addUrl="${addFlag == true ? '/productStorageRecord/add' : '' }" 
		 searchButtonId="productStorageRecord_search_btn">
		<div class="input-group" style="max-width: 1500px;">
			<c:choose>  
			   <c:when test="${isSys==true }">  
			   		<span class="input-group2">商家</span>
					<input name="storeName" id="productStorageRecord_storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="" class="form-control" isRequired="1" tipName="商家"  />
					<input type="hidden" name="storeId" id="productStorageRecord_storeId" value="" />
			   </c:when>  
			   <c:otherwise> 
			   		<m:hasPermission permissions="mainShopOrderStoreView">
			   			<c:if test="${isMainStore==true }">
							<span class="input-group2">商家</span>
							<input name="storeName" id="productStorageRecord_storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="" class="form-control" isRequired="1" tipName="商家"  />
							<input type="hidden" name="storeId" id="productStorageRecord_storeId" value="-1" />
						</c:if>
       				</m:hasPermission>  
			   </c:otherwise>  
			</c:choose> 
			
			<span class="input-group2">入库单号</span> 
			<input type="text"	id="storageNumber" name="storageNumber" class="form-control" placeholder="入库单号" style="width: 200px;margin-right:40px;">  
			
			<span class="input-group2">操作人</span> 
			<input type="text"	id="mobile" name="mobile" class="form-control" placeholder="操作人" style="width: 200px;margin-right:40px;">  
           	
			<span  class="input-group2">操作时间</span> 
			<input id = "beginTime"  type="text"    name="beginTime" class="form-control form-date" placeholder="选择开始日期" readonly  style="width: 100px;margin-right:40px;">
			<span  class="input-group2">至</span> 
			<input id = "endTime"  type="text"	name="endTime" class="form-control form-date" placeholder="选择结束日期" readonly style="width: 100px;margin-right:40px;">
           	</div>
			<button type="button" onclick="getExe()" class="btn btn-small btn-danger" style="width: auto;margin-top:5px;">导出记录</button>
	</m:list>
	<m:select_store path="${contextPath}/productStorageRecord/showModel/list/list-data" modeId="productStorageRecordShowStore" callback="callback"> </m:select_store>
</body>
</html>