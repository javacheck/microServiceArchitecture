<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商家代理商账号列表</title>
<script type="text/javascript">

	function deleteConfirm(id) {
		lm.confirm("确定要删除吗？", function() {
			lm.postSync("${contextPath }/account/delete/" + id, {}, function(data) {
				if (data == 1) {
					noty("删除成功");
					loadCurrentList_accountList();
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


	<m:list title="商家代理商账号列表" id="shopAgentList" beforeSearch="beforeSearch"
		listUrl="/shopAgent/list-data"
		 searchButtonId="user_search_btn">
		<div class="input-group" style="max-width: 1300px;">
			<span class="input-group-addon">用户账号</span> 
			<input type="text"	name="mobile" class="form-control" placeholder="请输入用户账号" style="width: 200px;">
		</div>
	</m:list>
</body>
</html>