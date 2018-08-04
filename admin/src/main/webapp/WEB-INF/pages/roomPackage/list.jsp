<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>套餐设置列表</title>
<script type="text/javascript">
function deleteConfirm(id) {
	lm.confirm("确定要删除吗？", function() {
		lm.post("${contextPath }/roomPackage/delete/delete-by-Id", {id:id}, function(data) {
			if (data == 1) {
				noty("删除成功");
				loadCurrentList_roomPackageList();
			}
		});
	});
}
</script>
</head>
<body>
	<m:hasPermission permissions="roomPackageAdd" flagName="addFlag"/>
	<m:list title="套餐设置列表" id="roomPackageList"
		listUrl="${contextPath }/roomPackage/list/list-data"
		addUrl="${addFlag == true ? '/roomPackage/add' : '' }"
		searchShow="false" resetShow="false">
	</m:list>
</body>
</html>