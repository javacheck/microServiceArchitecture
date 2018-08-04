<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>总部列表</title>
<script type="text/javascript">
function deleteById(id){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/shop/delete/delete-byId",{id:id},function(data){
			if(data==1){
				lm.alert("删除成功！");
			} else {
				lm.alert("删除失败！");
			}
			loadCurrentList_mainShopList();
		});
	});
}
</script>
</head>
<body>
	<m:hasPermission permissions="mainShopAdd" flagName="addFlag"/>
	<m:list title="总部列表" id="mainShopList"
		listUrl="${contextPath }/shop/mainShop/list-data"
		searchButtonId="mainShop_cateogry_search_btn" >
		
		<div class="input-group" style="max-width: 1500px;">
		
		 <span class="input-group-addon">总部名称</span> 
            	<input type="text" id="name" name="name" class="form-control" placeholder="总部名称" style="width: 200px;">
		</div>
	</m:list>
</body>
</html>