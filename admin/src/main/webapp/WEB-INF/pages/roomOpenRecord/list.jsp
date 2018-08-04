<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>包间交易记录列表</title>
<script type="text/javascript">

</script>
</head>
<body>
	<m:list title="包间交易记录列表" id="roomOpenRecordList"
		listUrl="${contextPath }/roomOpenRecord/list/list-data"
		searchButtonId="roomOpenRecord_search_btn">
		<div class="input-group" style="max-width: 500px;">
						
			<span class="input-group-addon">包间号码</span> 
			<input type="text" name="number" class="form-control" placeholder="包间号码">
			
			<span class="input-group-addon">包间类型</span>
				<select id="categoryId" class='form-control' style="width: auto;float:left;margin-right:40px;" name="categoryId">
					<option value="">全部</option>
					<c:forEach items="${categoryList }" var="category" >
						<option value ="${category.id}">${category.name}</option>
					</c:forEach>
				</select>
		</div>
	</m:list>
</body>
</html>