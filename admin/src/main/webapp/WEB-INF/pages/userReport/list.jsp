<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>举报列表</title>
<script type="text/javascript">
$(function(){
	var format = "yyyy-MM-dd";//默认格式
	var now = lm.Now;//当前日期
	var nowFormat=now.Format(format);
	//$("#beginTime").val(nowFormat);//默认开始日期为当天
	//$("#endTime").val(nowFormat);//默认结束日期为当天
	$("#beginTime").datetimepicker({
		minView: "month", //选择日期后，不会再跳转去选择时分秒 
		format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
		language: 'zh-CN', //汉化 
		autoclose:true , //选择日期后自动关闭
		todayBtn:true,//可选择当天按钮
		todayHighlight:false,//高亮当前日期
		endDate:""+nowFormat+""//默认最后可选择为当前日期
		
	 });
	$("#endTime").datetimepicker({
		minView: "month", //选择日期后，不会再跳转去选择时分秒 
		format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
		language: 'zh-CN', //汉化 
		autoclose:true, //选择日期后自动关闭 
		todayBtn:true,//可选择当天按钮
		todayHighlight:false,//高亮当前日期
		endDate:""+nowFormat+""//默认最后可选择为当前日期
	 });
	var endDateInput=nowFormat;
	//判断输入结束日期时候输入合法
	$('#endTime').datetimepicker().on('changeDate', function(ev){
		if((new Date(ev.date.valueOf()))<(new Date($('#beginTime').val().replace(/-/g,"/")))){//开始时间大于结束时间
			lm.alert("结束时间不能大于开始时间");
			$("#endTime").val(endDateInput);//默认值上一次输入
			$('#endTime').datetimepicker('update');//更新
		}
		endDateInput= $("#endTime").val();
		$('#beginTime').datetimepicker('setEndDate', $("#endTime").val());//设置开始时间最后可选结束时间
	});
});
function deleteUserReport(id){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/userReport/delete/delete-by-id",{id:id},function(data){
			if(data==1){
				lm.alert("删除成功！");
				window.location.href="${contextPath}/userReport/list";
			} else {
				lm.alert("删除失败！");
			}
		});
	});
}
</script>
</head>
<body>
	<m:list title="举报列表" id="reportList"
		listUrl="${contextPath }/userReport/list/list-data"  
		searchButtonId="cateogry_search_btn" >
		<div class="input-group" style="max-width: 1500px;">
			<span class="input-group-addon">开始时间</span>
			<input id="beginTime" value="" type="text" name="beginTime" class="form-control form-date" placeholder="选择开始日期" readonly style="width: 200px;"> 
			<span class="input-group-addon">结束时间</span> 
			<input id="endTime" value="" type="text" name="endTime" class="form-control form-date" placeholder="选择结束日期" readonly style="width: 200px;"> 
			<span class="input-group-addon">举报类型</span> 
            	<select name="typeId" class="form-control" style="width: auto;" id="typeId">
					<option  value ="">全部</option>
					<c:forEach items="${reportTypeList }" var="reprotType" >
						<option  value ="${reprotType.id}">${reprotType.name}</option>
					</c:forEach>
            	</select>
           
            <span class="input-group-addon">举报内容</span>
            	<input type="text" id="content" name="content" class="form-control" placeholder="举报内容" style="width: 200px;">
            
            <span class="input-group-addon">联系方式</span>
            	<input type="text" id="contact" name="contact" class="form-control" placeholder="联系方式" style="width: 200px;">
		</div>
		
	</m:list>
</body>
</html>