<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>库存调拨列表</title>
<script type="text/javascript">
function fromCallback(obj){
	$("#allocation_fromStoreName").val(obj.name);
	$("#allocation_fromStoreId").val(obj.id);
}
function toCallback(obj){
	$("#allocation_toStoreName").val(obj.name);
	$("#allocation_toStoreId").val(obj.id);
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
		$("#allocation_fromStoreName").click(function (){
			$("#allocationShowFromStore").modal();
		});
		// 点击弹出商家选择窗口
		$("#allocation_toStoreName").click(function (){
			$("#allocationShowToStore").modal();
		});
	});
	
	
	function typeChage(id,status,typeName){
		
		if(status==1){
			lm.confirm("确定要审核通过吗？",function(){
				$("#auditModalBtn").click();
				lm.post("${contextPath }/allocation/typeChange/change-by-allocationId",{id:id,status:status},function(data){
					if(data==1){
						lm.alert("操作成功！");
						loadCurrentList_allocationList();
					} 
				});
			});
		}else if(status==3){
			lm.confirm("确定要拒绝"+typeName+"吗？",function(){
				$("#auditModalBtn").click();
				lm.post("${contextPath }/allocation/typeChange/change-by-allocationId",{id:id,status:status},function(data){
					if(data==1){
						lm.alert("操作成功！");
						loadCurrentList_allocationList();
					} 
				});
			});
		}else if(status==2){
			lm.confirm("确定要发货吗？",function(){
				$("#auditModalBtn").click();
				lm.post("${contextPath }/allocation/typeChange/change-by-allocationId",{id:id,status:status},function(data){
					if(data==1){
						lm.alert("操作成功！");
						loadCurrentList_allocationList();
					} 
				});
			});
		}
	}
	function formReset(){
		$("#allocation_fromStoreName").val("");
		$("#allocation_fromStoreId").val("");
		$("#allocation_toStoreName").val("");
		$("#allocation_toStoreId").val("");
	}
	function getExe() {
		var fromStoreId=$("#allocation_fromStoreId").val();//调出仓库
		var toStoreId=$("#allocation_toStoreId").val();//调入仓库
		if( null == fromStoreId || undefined == fromStoreId ){
			fromStoreId = "";
		}
		if( null == toStoreId || undefined == toStoreId ){
			toStoreId = "";
		}
		var allocationNumber=$("#allocationNumber").val();//调拨单号
		var status=$("#status").val();//支付方式
		var beginTime =$("#beginTime").val();//开始时间
		var endTime = $("#endTime").val();//结束时间
		
		
		window.open("${contextPath}/allocation/list/ajax/list-by-search?beginTime="+beginTime+"&endTime="+endTime
						+"&fromStoreId="+fromStoreId+"&toStoreId="+toStoreId+"&allocationNumber="+allocationNumber+"&status="+status);
	}
</script>
<style>
  .input-group2{
  float:left;width:auto; background-color: #e5e5e5;ballocation: 1px solid #ccc;ballocation-radius: 4px; color: #222;font-size: 13px;font-weight: 400; padding: 5px 12px;text-align: center;
  }
</style>
</head>
<body>
	<c:set value="/allocation/add" var="addUrl"></c:set>
	<c:if test="${configFlag == false}">
		<c:set value="" var="addUrl"></c:set>
	</c:if>
	<m:list addName = "新增库存调拨" title="库存调拨列表" id="allocationList"
		listUrl="${contextPath }/allocation/list/list-data" 
		addUrl="${addUrl }" 
		 searchButtonId="user_search_btn" formReset="formReset"	>
		<div class="input-group" style="max-width: 1500px;">
             
	   		<span class="input-group2">调出仓库</span>
			<input name="fromSName" id="allocation_fromStoreName" style="width: 200px;margin-right:40px;" readonly="readonly" value="" class="form-control" isRequired="1" tipName="选择调出店铺"  />
			<input type="hidden" name="fromStoreId" id="allocation_fromStoreId" value="" />
			
			<span class="input-group2">调入仓库</span>
			<input name="toStoreName" id="allocation_toStoreName" style="width: 200px;margin-right:40px;" readonly="readonly" value="" class="form-control" isRequired="1" tipName="选择调入店铺"  />
			<input type="hidden" name="toStoreId" id="allocation_toStoreId" value="" />
			 
			<span class="input-group2">调拨单号</span> 
			<input type="text"	id="allocationNumber" name="allocationNumber" class="form-control" placeholder="请输入调拨单号" style="width: 200px;margin-right:40px;">  
			
			<span class="input-group2">调拨状态</span>
           	<select name="status" class="form-control" style="width: auto;" id="status">
           		<option  value ="">全部</option>
				<option  value ="0">待审核</option>
				<option  value ="1">待发货</option>
				<option  value ="2">待收货</option>
				<option  value ="3">已拒绝</option>
				<option  value ="4">已完成</option>
           	</select>
           	
           	</div>
			<br />
			<div class="input-group" style="max-width: 1500px;">
			<span  class="input-group2">开始时间</span> 
			<input id = "beginTime"  type="text"    name="beginTime" class="form-control form-date" placeholder="选择开始日期" readonly  style="width: 100px;margin-right:40px;">
			<span  class="input-group2">结束时间</span> 
			<input id = "endTime"  type="text"	name="endTime" class="form-control form-date" placeholder="选择结束日期" readonly style="width: 100px;margin-right:40px;">
           	</div>
			<button type="button" onclick="getExe()" class="btn btn-small btn-danger" style="width: auto;margin-top:5px;">批量导出</button>
	</m:list>
	<m:select_store path="${contextPath}/allocation/showModel/list/list-data" modeId="allocationShowFromStore" callback="fromCallback"> </m:select_store>
	<m:select_store path="${contextPath}/allocation/showModel/listStore/list-data" modeId="allocationShowToStore" callback="toCallback"> </m:select_store>
</body>
</html>