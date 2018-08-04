<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>统计报表</title>
<script type="text/javascript">
	$(function() {
		$(".t").each(function(){ 
			var maxwidth=10;
			if($(this).text().length>maxwidth){
				 $(this).text(
					 $(this).text().substring(0,maxwidth)); $(this).html($(this).html()+'...'
				 ); 
			 } 
		 }); 
		

		
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

		$(".category").find('input').change(function(){
			if ($(this).val() == "2"){
				
				$("#store").attr("disabled",true);
			}else {
				$("#store").attr("disabled",false);
			}
		});
		
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
		
		$('#beginTime').datetimepicker('setEndDate', _time);
		$('#endTime').datetimepicker('setEndDate', _time);
		
		var tempNum = document.getElementsByName("category");//统计
		var category = "";
		for (var n = 0; n < tempNum.length; n++) {
			if (tempNum[n].checked) {
				category = tempNum[n].value;
				break;
			}
		}
		if(category!=2){
			if($("#store").val()=="" || $("#store").val()==null || $("#store").val()==undefined){
				$("#submit").hide();
				lm.alert("店铺为空，请先添加店铺！");
				return;
			}else{
				$("#submit").show();
			}
		}
		var action="${action}";
		
		var data = ${data};
		var category = "${category}";//统计分类(0,1,2,3)
		var date1 = "${date}";//统计日期(0,1,2)
		if(action=="1" && category==2){
			$("#store").attr("disabled","disabled");
		}
		if (data.length > 0){//查询是否有数据
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
					var _date='';
					if(date1==0){//按日统计
						_date = data[i].date;
					}else if(date1==1){//按月统计
						_date = (data[i].date).substr(0,data[i].date.lastIndexOf('-'));
					}else{//按年统计
						_date = (data[i].date).substr(0,data[i].date.indexOf('-'));
					}
					//var minPrice="";
					if (_name == name && _date == date){//如果循环（以dateList.length）时，和dataList的数据中的名称和天数相同，把当天总金额打印出来，不同给0
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
			
			$(".minPrice").each(function(){
				var total = 0;
				var tds = $(this).parent().find("td");
				
				for (var i = 1; i < tds.length; i++){
					total += new Number($(tds[i]).html());
				}
				
				$(this).html(total);
			});
		}
		
	});
	
	function getExe() {
		var beginTime = $("#beginTime").val();//开始时间
		var endTime = $("#endTime").val();//结束时间
		var account = $("#account").val();//用户ID
		var store=$("#store").val();//店铺
		var tempNum = document.getElementsByName("category");//统计
		var category = "";
		for (var n = 0; n < tempNum.length; n++) {
			if (tempNum[n].checked) {
				category = tempNum[n].value;
				break;
			}
		}
		if(category==2){
			store="";
		}
		var tempNum1 = document.getElementsByName("date");//统计
		var date = "";
		for (var n = 0; n < tempNum1.length; n++) {
			if (tempNum1[n].checked) {
				date = tempNum1[n].value;
				break;
			}
		}
		
		window.open("${contextPath}/report/list/ajax/list-by-search?beginTime="+beginTime+"&endTime="+endTime+"&category="+category+"&date="+date+"&store="+store);
	}
	function getbeginDate(){
		var tempNum1 = document.getElementsByName("date");//按时间统计
		var date = "";
		for (var n = 0; n < tempNum1.length; n++) {
			if (tempNum1[n].checked) {
				date = tempNum1[n].value;
				break;
			}
		}
		var now = new Date();
		var _time = now.getFullYear() + '-'
				+ ('0' + (1 + now.getMonth())).slice(-2) + '-'
				+ ('0' + now.getDate()).slice(-2);
		if(date==0){//按日统计
			$('#endTime').datetimepicker('setStartDate',$('#beginTime').val());
			var a=new Date($('#beginTime').val());
			var d=new Date(a.getTime()+24*60*60*1000*30);//最多只能统计31天
			var c=d.getFullYear() + '-'+ ('0' + (1 + d.getMonth())).slice(-2) + '-'+ ('0' + d.getDate()).slice(-2);
			if(now>d){
				$('#endTime').datetimepicker('setEndDate',c);
				$('#endTime').val(c);
			}else{
				$('#endTime').datetimepicker('setEndDate',_time);
				
			}
		}else if(date==1){//按月统计
			$('#endTime').datetimepicker('setStartDate',$('#beginTime').val());
			var a=new Date($('#beginTime').val());
			var d=new Date(a.getTime()+24*60*60*1000*30*12);//最多只能统计12个月
			var c=d.getFullYear() + '-'+ ('0' + (1 + d.getMonth())).slice(-2) + '-'+ ('0' + d.getDate()).slice(-2);
			if(now>d){
				$('#endTime').datetimepicker('setEndDate',c);
				$('#endTime').val(c);
			}else{
				$('#endTime').datetimepicker('setEndDate',_time);
				
			}
		}else{
			$('#endTime').val(_time);
			$('#endTime').datetimepicker('setEndDate',_time);
		}
	}
	function getendDate(){
		
		var a=new Date($('#beginTime').val());
		var b=new Date($('#endTime').val());
		if(a>b){
			lm.alert("开始时间不能大于结束时间！");
			$('#beginTime').val($('#endTime').val());
		}
		$('#beginTime').datetimepicker('setEndDate',$('#endTime').val());
		
	}
</script>
</head>
<body>
	<form action="${contextPath }/report/list" id="reportForm">
		<input type="hidden" name="action" value="1">
		<div class="panel">
			<div class="panel-heading">
				<strong><i class="icon-list-ul"></i>统计报表</strong>
				<div class="input-group" style="max-width: 1000px;">
					<span class="input-group-addon">开始时间</span> <input id="beginTime" value="${beginTime }"
						type="text" name="beginTime" class="form-control form-date"
						placeholder="选择开始日期" readonly style="width: 200px;" onchange="getbeginDate();"> <span
						class="input-group-addon">结束时间</span> <input id="endTime" value="${endTime }"
						type="text" name="endTime" class="form-control form-date"
						placeholder="选择结束日期" readonly style="width: 200px;" onchange="getendDate();"> <span
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
				<div class="input-group category">
					<input id="category" type="radio" name="category" checked="checked" value="0" />统计商品 
					<input id="category" type="radio" name="category" value="1" <c:if test="${category == 1 }">checked="checked"</c:if>/>统计分类 
					<input id="category" type="radio" name="category" value="2" <c:if test="${category == 2 }">checked="checked"</c:if>/>统计门店
					<input id="category" type="radio" name="category" value="3" <c:if test="${category == 3 }">checked="checked"</c:if>/>统计账号
				</div>
				<br />
				<div class="input-group">
					统计日期：<input id="date" type="radio" name="date" checked="checked"value="0" />日 
							<input id="date" type="radio" name="date" value="1" <c:if test="${date == 1 }">checked="checked"</c:if>/>月
							<input id="date" type="radio" name="date" value="2" <c:if test="${date == 2 }">checked="checked"</c:if>/>年
				</div>
				<br />
				<div class="input-group">
					<input type="submit" class="btn btn-info" value="搜索" id="submit">
					<c:if test="${nameList.size()>0}">
					<a href="" onclick="getExe()" class="btn btn-small btn-danger">导出报表</a>
					</c:if>
				</div>
			</div>
			<div class="panel-body" style="overflow-x:auto;">
			<c:if test="${not empty action }">
			<table class='table table-hover table-striped table-bordered'  >
				<thead>
					<tr>
						<c:if test="${dateList.size()>0}">
							<c:if test="${category == 0 }"><th style="text-overflow:ellipsis;overflow:hidden;white-space: nowrap;">商品名称</th></c:if>
							<c:if test="${category == 1 }"><th>分类名称</th></c:if>
							<c:if test="${category == 2 }"><th>店铺名称</th></c:if>
							<c:if test="${category == 3 }"><th>账号名称</th></c:if>
						</c:if> 
						<c:forEach items="${dateList }" var="date"><!-- 天数列表 -->
							<th>${date }</th>
						</c:forEach>
						<th>小计</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${nameList }" var="name"><!-- 去重后的名称列表 -->
						<tr name="${name }" >
							<td class="t" style="text-overflow:ellipsis;overflow:hidden;white-space: nowrap;padding:2px">${name }</td>
							<c:forEach items="${dateList }" var="date"><!-- 天数列表 -->
								<td class="price" name="${name }" date="${date }"></td>
							</c:forEach>
							<td class="minPrice">0</td>
						</tr>
					</c:forEach>
					<tr>
						<td>总计</td>
						<c:forEach items="${dateList }" var="date">
							<td class="totalprice" date="${date }"></td>
						</c:forEach>
						<td class="minPrice">0</td>
					</tr>
				</tbody>
			</table>
			</c:if>
			</div>
		</div>
	</form>
</body>
</html>