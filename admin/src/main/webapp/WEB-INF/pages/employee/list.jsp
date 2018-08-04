<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>员工管理列表</title>
<script type="text/javascript">
function deleteById(id){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/employee/delete/deleteById",{id:id},function(data){
			if(data==1){
				lm.alert("删除成功！");
			} else {
				lm.alert("删除失败！");
			}
			loadCurrentList_employeeList();
		});
	});
}
</script>
</head>
<body>
	<m:hasPermission permissions="employeeAdd" flagName="addFlag"/>
	<m:list title="员工管理列表" id="employeeList"
		listUrl="${contextPath }/employee/list-data"
		addUrl="${addFlag == true ? '/employee/add' : '' }"
		searchButtonId="employee_search_btn">
		
		<div class="input-group" style="max-width: 800px;">
		 	<span class="input-group-addon">名称</span> 
            	<input type="text" id="name" name="name" class="form-control" placeholder="员工名称" style="width: 200px;">
        </div>
	</m:list>
</body>
</html>