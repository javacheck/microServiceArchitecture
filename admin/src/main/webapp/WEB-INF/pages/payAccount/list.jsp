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
	
	function beforeSearch(){
		return true;
	}
</script>
</head>
<body>


	<m:list title="账号列表" id="accountList" beforeSearch="beforeSearch"
		listUrl="/payaccount/list-data"
		searchButtonId="user_search_btn">
		<div class="input-group" style="max-width: 600px;" >
			<span class="input-group-addon">手机号码</span> 
			<input type="text"	name="mobile" class="form-control" placeholder="请输入手机号码" style="width: 200px;">
			<span class="input-group-addon">状态</span> 
			<select name="status"   class="form-control">
				<option value="-1">所有</option>
				<option value="0">未激活</option>
				<option value="1">正常</option>
				<option value="2">冻结</option>
				<option value="3">销户</option>
				<option value="4">挂失</option>
				<option value="5">锁定</option>
		    </select>
		    <span class="input-group-addon">类型</span> 
			<select name="type"  class="form-control">
				<option value="-2">所有</option>
				<option value="-1">平台</option>
				<option value="0">商家</option>
				<option value="1">代理商</option>
				<option value="2">用户</option>
		    </select>
		</div>
	</m:list>
</body>
</html>