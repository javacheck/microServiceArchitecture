<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>代理商列表</title>
<script type="text/javascript">
	function callback(){
		$("#agentListDataId").find("tbody tr").click(function(){
			$("#agentId").val($(this).attr("val"));
			$("#agentName").val(($($(this).find("td")[0]).html())); 
			$("#agentModalBtn").click();
		});
	}
	
</script>
</head>
<body>
	<div class="modal-dialog modal-lg" style="width: 1200px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" id="agentModalBtn">
					<span aria-hidden="true">×</span><span class="sr-only">关闭</span>
				</button>
			</div>
			<m:list title="代理商列表" id="agentList"
				listUrl="${contextPath }/shop/agentList/agentList-data" callback="callback"
				searchButtonId="cateogry_search_btn">
				<div class="input-group" style="max-width: auto">
					<span class="input-group-addon">代理商名称</span>
						<input type="text" id="name" name="name" class="form-control" placeholder="代理商名称" style="width: 200px;"> 
					<span class="input-group-addon">手机号码</span>
						<input type="text" id="mobile" name="mobile" class="form-control" placeholder="手机号码" style="width: 200px;">
				</div>
			</m:list>
		</div>
	</div>
</body>
</html>