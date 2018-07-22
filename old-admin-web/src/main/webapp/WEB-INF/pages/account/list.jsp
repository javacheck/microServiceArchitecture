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
	function deleteConfirm(id) {
		lm.confirm("确定要删除吗？", function() {
			lm.postSync("${contextPath }/account/delete/" + id, {}, function(data) {
				if (data == 1) {
					noty("删除成功");
					loadCurrentList_userList();
				}
			});
		});
	}
</script>
</head>
<body>
	<m:hasPermission permissions="accountAdd" flagName="addFlag"/>

	<m:list title="会员列表" id="userList"
		listUrl="/account/list-data"
		addUrl="${addFlag == true ? '/account/add' : '' }" searchButtonId="user_search_btn">
		<div class="input-group">
			<span class="input-group-addon">手机号码</span> <input type="text"
				name="mobile" class="form-control" placeholder="请输入手机号码"
				style="width: 200px;">
		</div>
	</m:list>
</body>
</html>