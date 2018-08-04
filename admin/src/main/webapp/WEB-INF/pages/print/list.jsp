<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>打印机列表</title>
<script type="text/javascript">
function callback(obj){
	$("#print_storeName").val(obj.name);
	$("#print_storeId").val(obj.id);
}
function deletePrint(id){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/print/delete/delete-by-printId",{id:id},function(data){
			lm.alert("删除成功");
			loadCurrentList_printList();
		});
	});
}
function formReset(){
	$("#print_storeName").val("");
	$("#print_storeId").val("");
}
$(function(){
	// 点击弹出商家选择窗口
	$("#print_storeName").click(function (){
		$("#printShowStore").modal();
	});
});
//修改选择的店铺时触发
function byStoreAjaxRefreshList(){
	loadList_printList(); 
}
function typeChage(id,status){
	var temp=status=="1"?"开启":"关闭";
	lm.confirm("确定要"+temp+"打印机吗？",function(){
		lm.post("${contextPath }/print/typeChange/change-by-printId",{id:id,status:status},function(data){
			if(data==1){
				lm.alert("操作成功！");
				loadCurrentList_printList();
			} 
		});
	});
}
</script>
<style>
  .input-group2{
  float:left;width:auto; background-color: #e5e5e5;border: 1px solid #ccc;border-radius: 4px; color: #222;font-size: 13px;font-weight: 400; padding: 5px 12px;text-align: center;
  }
  </style>
</head>
<body>
	<m:hasPermission permissions="printAdd" flagName="addFlag"/>
	<m:list title="打印机列表" id="printList"
		listUrl="${contextPath }/print/list/list-data" 
		addUrl="${addFlag == true ? '/print/add' : '' }" 
		searchButtonId="cateogry_search_btn" >
		
		<div class="input-group" style="max-width:1500px;">
			<c:if test="${isSys==true }">
				<span class="input-group2">商家</span>
				<input name="storeName" id="print_storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="${store.name }" class="form-control" isRequired="1" tipName="商家"  />
				<input type="hidden" name="storeId" id="print_storeId" value="${store.storeId }" />
            </c:if>	
            
            <span class="input-group2">打印机编号</span> 
            <input type="text" id="printSn" name="printSn" class="form-control" placeholder="打印机编号" style="width: 200px;float:left;margin-right:40px;">
            
            <span class="input-group2">状态</span> 
            <select name="status" class="form-control" style="margin-right:40px;width: auto;" id="status">
           		<option  value ="">全部</option>
				<option  value ="1">已开启</option>
				<option  value ="0">已关闭</option>
           	</select>
            	
		</div>
	</m:list>
	<m:select_store path="${contextPath}/print/showModel/list/list-data" modeId="printShowStore" callback="callback"> </m:select_store>
</body>
</html>