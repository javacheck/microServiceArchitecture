<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>供应商列表</title>
<script type="text/javascript">

function deletesupplier(id){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/supplier/delete/delete-by-supplierId",{id:id},function(data){
			lm.alert("删除成功");
			window.location.href="${contextPath}/supplier/list";
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
	<m:hasPermission permissions="supplierAdd" flagName="addFlag"/>
	<m:list title="供应商列表" id="supplierList"
		listUrl="${contextPath }/supplier/list/list-data" 
		addUrl="${addFlag == true ? '/supplier/add' : '' }" 
		searchButtonId="cateogry_search_btn" >
		
		<div class="input-group" style="max-width:1500px;">
            
            <span class="input-group2">供应商名称</span> 
            <input type="text" id="name" name="name" class="form-control" placeholder="供应商名称" style="width: 200px;float:left;margin-right:40px;">
            
            <span class="input-group2">电话</span> 
            <input type="text" id="phone" name="phone" class="form-control" placeholder="电话" style="width: 200px;float:left;margin-right:40px;">
            	
		</div>
	</m:list>
	
</body>
</html>