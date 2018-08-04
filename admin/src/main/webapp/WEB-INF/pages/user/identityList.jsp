<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>证件列表</title>
	
<script type="text/javascript">

	function updateIdAuditConfirm(id,idAudit) {
		var message = idAudit==1?'通过':'不通过';
		lm.confirm("确定要"+message+"证件审核吗？", function() {
			lm.post("${contextPath }/user/ajax/modifyIdAudit/" + id, {idAudit:idAudit}, function(data) {
				noty("审核"+message+"成功！");
				loadCurrentList_identityList();
			});
		});
	}
</script>
</head>
<body>
	<m:list title="证件列表" id="identityList"
		listUrl="${contextPath }/user/identityList/list-data" 
		
		searchButtonId="user_search_btn">
		<div class="input-group" style="max-width: 1200px;">
			 <span class="input-group-addon">证件类型</span>
            	<select name="idAudit" class="form-control" style="width: 200px;" id="idAudit" >
            		<option  value ="">全部</option>
					<option  value ="0">审核中</option>
					<option  value ="1">审核成功</option>
					<option  value ="2">审核失败</option>
            	</select>
			<span class="input-group-addon">手机号码</span> 
			<input type="text"	name="mobile" class="form-control" placeholder="请输入会员手机号码" style="width: 200px;">
		</div>
	</m:list>
</body>
</html>