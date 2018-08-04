<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>库存盘点记录</title>
<script type="text/javascript">
function callback(obj){
	$("#stockCheck_storeName").val(obj.name);
	$("#stockCheck_storeId").val(obj.id);
}

function formReset(){
	$("#stockCheck_storeName").val("");
	$("#stockCheck_storeId").val("");
}
$(function(){
	
	var format = "yyyy-MM-dd";//默认格式
	var now = lm.Now;//当前日期
	var nowFormat=now.Format(format);
	$("#createdTime").val(nowFormat);
	$("#createdTime").datetimepicker({
		minView: "month", //选择日期后，不会再跳转去选择时分秒 
		format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
		language: 'zh-CN', //汉化 
		autoclose:true , //选择日期后自动关闭
		todayBtn:true,//可选择当天按钮
		todayHighlight:false,//高亮当前日期
		endDate:""+nowFormat+""//默认最后可选择为当前日期
		
	 });
	
	// 点击弹出商家选择窗口
	$("#stockCheck_storeName").click(function (){
		$("#stockCheckShowStore").modal();
	});
	var action="${action}";
	if(action!=null && action!=''){
		if(action==1){
			lm.alert("上传成功!");
		}else{
			lm.alert(action);
		}
	}
});
//修改选择的店铺时触发
function byStoreAjaxRefreshList(){
	loadList_stockCheckList(); 
}
function getExe() {
	var storeId=$("#stockCheck_storeId").val();//店铺
	if( null == storeId || undefined == storeId ){
		storeId = "";
	} 
	var createdTime =$("#createdTime").val();//盘点时间
	var checkedName = $("#checkedName").val();//盘点人
	window.open("${contextPath}/stockCheck/list/ajax/list-by-search?createdTime="+createdTime
					+"&storeId="+storeId+"&checkedName="+checkedName);
}

</script>
<style>
  .input-group2{
  float:left;width:auto; background-color: #e5e5e5;border: 1px solid #ccc;border-radius: 4px; color: #222;font-size: 13px;font-weight: 400; padding: 5px 12px;text-align: center;
  }
  </style>
</head>
<body>
	<m:list title="库存盘点记录" id="stockCheckList"
		listUrl="${contextPath }/stockCheck/list/list-data" 
		searchButtonId="cateogry_search_btn" formReset="formReset">
		
		<div class="input-group" style="max-width:1500px;">
			   <c:if test="${isMainStore==true }">  
			   		<span class="input-group2">商家</span>
					<input name="storeName" id="stockCheck_storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="" class="form-control" isRequired="1" tipName="商家"  />
					<input type="hidden" name="storeId" id="stockCheck_storeId" value="" />
			   </c:if>  
			 
			 <span class="input-group2">导入日期</span> 
            <input id = "createdTime"  type="text"	name="createdTime" class="form-control form-date" placeholder="盘点时间" readonly style="width: 200px;float:left;margin-right:40px;"> 
            
            <span class="input-group2">盘点人</span> 
            <input type="text" id="checkedName" name="checkedName" class="form-control" placeholder="盘点人名称" style="width: 200px;float:left;margin-right:40px;">
            	
		</div>
		<c:if test="${isMainStore==false }"> 
			<button type="button" onclick="getExe();" class="btn btn-small btn-danger" style="width: auto;margin-top:5px;">导出盘点数据</button>
			<button type="button"  data-toggle="modal" name='infoButton' data-remote="${contextPath }/stockCheck/uploadPage/showMode" class="btn btn-small btn-danger" style="width: auto;margin-top:5px;">导入盘点数据</button>
		</c:if>
	</m:list>
	<m:select_store path="${contextPath}/stockCheck/showModel/list/list-data" modeId="stockCheckShowStore" callback="callback"> </m:select_store>
</body>
</html>