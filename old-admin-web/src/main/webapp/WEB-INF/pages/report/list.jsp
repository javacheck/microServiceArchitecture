<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>会员管理</title>
<script type="text/javascript">
	$(function() {
		$("#beginTime").datetimepicker({
			minView : "month", //选择日期后，不会再跳转去选择时分秒 
			format : "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
			language : 'zh-CN', //汉化 
			autoclose : true
		//选择日期后自动关闭 
		});
		$("#endTime").datetimepicker({
			minView : "month", //选择日期后，不会再跳转去选择时分秒 
			format : "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
			language : 'zh-CN', //汉化 
			autoclose : true
		//选择日期后自动关闭 
		});
		var beginTime = "${beginTime}";
		var endTime = "${endTime}";
		
		var now = new Date();
		var _time = now.getFullYear() + '-'
				+ ('0' + (1 + now.getMonth())).slice(-2) + '-'
				+ ('0' + now.getDate()).slice(-2);
		if (beginTime == ""){
			$("#beginTime").val(_time);
		}
		if (endTime == ""){
			$("#endTime").val(_time);
		}
				
		var data = ${data};
		var category = "${category}";
		if (data.length > 0){
			$(".table .price").each(function(){
				var name = $(this).attr("name"); 
				var date = $(this).attr("date");
				var flag = true;
				for (var i = 0; i < data.length ; i++){
					var _name = "";
					if (category == 0){
						_name = data[i].productName;
					}else if (category == 1){
						_name = data[i].categoryName;
					}else if (category == 2){
						_name = data[i].storeName;
					}else if (category == 3){
						_name = data[i].accountName;
					}
					var _date = data[i].date;
					if (_name == name && _date == date){
						flag = false;
						$(this).html(data[i].sumPrice);
						break;
					}
				}
				if (flag == true){
					$(this).html(0);
				}
			});
			
			$(".totalprice").each(function(){
				var date = $(this).attr("date");
				var total = 0;
				
				$(".table .price").each(function(){
					var _date = $(this).attr("date");
					if (date == _date){
						total += new Number($(this).html());
					}
				});
				$(this).html(total);
			});
		}
	});
	function getByRoleId() {
		var roleId = $("#role").val();//获取角色Id

		$("#account").empty();
		lm.post("${contextPath}/report/list/ajax/list-by-roleId", {
			roleId : roleId
		}, function(data) {
			if (data.length == 0) {
				lm.alert("该角色下没有用户！");
				window.location.href = "${contextPath}/report/list";
			} else {
				for (var i = 0; i < data.length; i++) {
					$("#account").append(
							'<option id="' + data[i].id + '" value="' + data[i].id + '">'
									+ data[i].name + '</option>');
				}
			}
		});
	}
	function getExe() {
		var beginTime = $("#beginTime").val();//开始时间
		var endTime = $("#endTime").val();//结束时间
		var account = $("#account").val();//用户ID
		var tempNum = document.getElementsByName("category");//统计
		var category = "";
		for (var n = 0; n < tempNum.length; n++) {
			if (tempNum[n].checked) {
				category = tempNum[n].value;
				break;
			}
		}
		var tempNum1 = document.getElementsByName("date");//统计
		var date = "";
		for (var n = 0; n < tempNum1.length; n++) {
			if (tempNum1[n].checked) {
				date = tempNum1[n].value;
				break;
			}
		}
		lm.post("${contextPath}/report/list/ajax/list-by-search", {
			beginTime : beginTime,
			endTime : endTime,
			account : account,
			category : category,
			date : date
		}, function(data) {
			alert("成功！");
		});
	}
</script>
</head>
<body>
	<form action="${contextPath }/report/list">
		<input type="hidden" name="action" value="1">
		<div class="panel">
			<div class="panel-heading">
				<strong><i class="icon-list-ul"></i>统计报表</strong>
				<div class="input-group">
					<span class="input-group-addon">开始时间</span> <input id="beginTime" value="${beginTime }"
						type="text" name="beginTime" class="form-control form-date"
						placeholder="选择开始日期" readonly style="width: 300px;"> <span
						class="input-group-addon">结束时间</span> <input id="endTime" value="${endTime }"
						type="text" name="endTime" class="form-control form-date"
						placeholder="选择结束日期" readonly style="width: 300px;"> <span
						class="input-group-addon">店铺</span>
					<div class="col-md-0">
						<select name="store" class="form-control" style="width: 230px"
							id="store">
							<c:forEach items="${storeList }" var="s">
								<option <c:if test="${store == s.id }">selected="selected"</c:if> value="${s.id}">${s.name}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<br />
				<div class="input-group">
					<input id="category" type="radio" name="category" checked="checked"
						value="0" />统计产品 <input id="category" type="radio" <c:if test="${category == 1 }">checked="checked"</c:if>
						name="category" value="1" />统计分类 <input id="category"
						type="radio" name="category" value="2" <c:if test="${category == 2 }">checked="checked"</c:if>/>统计门店 <input
						id="category" type="radio" name="category" value="3" <c:if test="${category == 3 }">checked="checked"</c:if>/>统计账号
				</div>
				<br />
				<div class="input-group">
					统计日期：<input id="date" type="radio" name="date" checked="checked"
						value="0" />日 <input id="date" type="radio" name="date" value="1" <c:if test="${date == 1 }">checked="checked"</c:if>/>月
					<input id="date" type="radio" name="date" value="2" <c:if test="${date == 2 }">checked="checked"</c:if>/>年
				</div>
<br />
				<div class="input-group">
					<input type="submit" class="btn btn-info" value="查询">
				</div>
			</div>
			<c:if test="${not empty action }">
			<table class='table table-hover table-striped table-bordered'>
				<thead>
					<tr>
						<th></th>
						<c:forEach items="${dateList }" var="date">
							<th>${date }</th>
						</c:forEach>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${nameList }" var="name">
						<tr>
							<td>${name }</td>
							<c:forEach items="${dateList }" var="date">
								<td class="price" name="${name }" date="${date }"></td>
							</c:forEach>
						</tr>
					</c:forEach>
					<tr>
						<td>总计</td>
						<c:forEach items="${dateList }" var="date">
							<td class="totalprice" date="${date }"></td>
						</c:forEach>
					</tr>
				</tbody>
			</table>
			</c:if>
		</div>
	</form>
</body>
</html>