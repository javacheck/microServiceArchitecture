<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品列表</title>
<script type="text/javascript">
$(function(){
	if(cacheArray.length > 0){
		$("#show_Store_Model input[type='checkbox']").each(function(key,value){
			for (var i = 0; i < cacheArray.length; i++) {
				if(cacheArray[i] == ($(value).attr("shopIdSign")) ){
					$(value).attr("checked",true);
					return; //在缓存数组中找到了当前的值就重新再在外围循环
				}
			}
		});
	}
});
</script>
</head>
<body>
	<div class="panel" id="show_Store_Model">
		<m:table pageNo="${page.pageNo }" pages="${page.pages }" pageSize="${page.pageSize }" total="${page.total }">
			<thead>
				<tr class='text-center'>
					<th>
						<input type="checkbox" id="pageALLCheckBox" onclick="checkPageALL(this);"/>
					</th>
					<th>商家名称</th>
					<th>商家手机号码</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${page.data}" var="shop">
					<tr style="cursor: pointer;" id="${shop.id }" shopIdSign="${shop.id }" onclick="trSelectEvent(this);">
						<td><input type="checkbox" id="${shop.id }" shopIdSign="${shop.id }" onclick="checkboxSelectEvent(this);" /></td>
						<td>${shop.name }</td>
						<td>${shop.mobile }</td>
					</tr>
				</c:forEach>
			</tbody>
		</m:table>
	</div>
</body>
</html>