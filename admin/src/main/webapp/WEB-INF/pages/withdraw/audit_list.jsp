<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理员提现审核列表</title>
<script type="text/javascript">
$(function(){
	var format = "yyyy-MM-dd";//默认格式
	var now = lm.Now;//当前日期
	var nowFormat=now.Format(format);
	$("#startTime").datetimepicker({
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
		if((new Date(ev.date.valueOf()))<(new Date($('#startTime').val().replace(/-/g,"/")))){//开始时间大于结束时间
			lm.alert("结束时间不能大于开始时间");
			$("#endTime").val(endDateInput);//默认值上一次输入
			$('#endTime').datetimepicker('update');//更新
		}
		endDateInput= $("#endTime").val();
		$('#startTime').datetimepicker('setEndDate', $("#endTime").val());//设置开始时间最后可选结束时间
	});
});

//点击搜索时先判断价格参数的正确性
function checkParameter(){
	var Id = $("input[name='Id']").val();
	Id = $.trim(Id); // 用jQuery的trim方法删除前后空格
	if( null != Id && Id != "" ){
		if( !(/^[0-9]*$/.test(Id)) ){
			lm.alert("请输入正确的申请ID！");
			return ;
		}
	}
	
	return true; 
}
function deleteConfirm(id,status,ownerId,type) {
	var message = status == 3 ? '通过':'不通过';
	lm.confirm("确定提现记录" + message + "审核？", function() { 
		lm.post("${contextPath }/withdraw/ajax/modifyStatus", {id:id,status:status,ownerId:ownerId,type:type}, function(data) {
			if (data == 1) {
				noty("审核"+message);
				loadCurrentList_auditList();
			}
		});
	});
}
</script>
</head>
<body>
	<m:list title="管理员提现审核列表" id="auditList"
		listUrl="${contextPath }/withdraw/audit/list-data"
		searchButtonId="cateogry_search_btn" beforeSearch="checkParameter" >
		 <div class="input-group" style="min-width: 1000px;"> 	
            <span class="input-group-addon">申请ID</span> 
            	<input type="text" id="Id" name="Id" maxlength="15" value="" class="form-control" placeholder="申请ID" style="width: 200px;">
            
            <span class="input-group-addon">户主类型</span>
           	<select name="type" class="form-control" style="width: auto;" id="type">
           		<option id="all" value="10" selected="selected">全部</option>
           		<option id="first" value="0">商家</option>
           		<option id="last" value="1">代理商</option>
           	</select>
            
           	<span class="input-group-addon">户主名称</span> 
            	<input type="text" id="name" name="name" value="" class="form-control" placeholder="户主名称" style="width: 200px;">
           	
           	<span  class="input-group-addon">开始时间</span> 
				<input id = "startTime"  type="text"  name="startTime" class="form-control form-date" placeholder="开始时间" readonly  style="width: 200px;">
			
			<span  class="input-group-addon" >结束时间</span> 
				<input id = "endTime"  type="text"	name="endTime" class="form-control form-date" placeholder="结束时间" readonly style="width: 200px;">
		</div>
	</m:list>
</body>
</html>