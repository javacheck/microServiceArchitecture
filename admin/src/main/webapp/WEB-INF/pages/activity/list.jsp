<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>活动列表</title>
<script type="text/javascript">
	function deleteConfirm(id) {
		lm.confirm("确定要删除吗？", function() {
			lm.postSync("${contextPath }/activity/delete/" + id, {}, function(data) {
				if (data == 1) {
					noty("删除成功");
					loadCurrentList_activityList();
				}
			});
		});
	}
</script>
</head>
<body>
	<m:hasPermission permissions="activityAdd" flagName="addFlag" />
	<m:list title="活动列表" id="activityList"
		listUrl="${contextPath }/activity/list-data"
		addUrl="${addFlag == true ? '/activity/add' : '' }"
		searchButtonId="activity_search_btn">

		<div class="input-group" style="max-width: 600px;">
			<span class="input-group-addon">活动标题</span> <input type="text"
				id="activityname" name="name" class="form-control"
				placeholder="活动标题" style="width: 200px;"> 

		</div>
	</m:list>
</body>
</html>