<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>账号列表</title>
<script type="text/javascript">
	$(function(){
	});
	function deleteConfirm(id) {
		lm.confirm("确定要删除吗？", function() {
			lm.post("${contextPath }/roomCategory/delete/" + id, {}, function(data) {
				if (data == 1) {
					noty("删除成功");
					loadCurrentList_roomCategoryList();
				}else{
					noty("此分类已使用不可删除");
				}
			});
		});
	}
	
	function beforeSearch(){
		return true;
	}
</script>
</head>
<body>
	<m:hasPermission permissions="accountAdd" flagName="addFlag"/>


	<m:list title="类型列表" id="roomCategoryList" beforeSearch="beforeSearch"
		listUrl="/roomCategory/list-data"
		addUrl="${addFlag == true ? '/roomCategory/add' : '' }" >
	</m:list>
</body>
</html>