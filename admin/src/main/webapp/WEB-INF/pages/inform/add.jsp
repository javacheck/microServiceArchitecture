<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty inform ? '添加' : '修改' }广播</title> 
<script type="text/javascript">

$(function(){
	var format = "yyyy-MM-dd";//默认格式
	var now = lm.Now;//当前日期
	var nowFormat=now.Format(format);
	$("#loseDate").datetimepicker({
		minView: "month", //选择日期后，不会再跳转去选择时分秒 
		format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
		language: 'zh-CN', //汉化 
		autoclose:true , //选择日期后自动关闭
		startDate:now,
		todayBtn:true,//可选择当天按钮
		todayHighlight:false//高亮当前日期
	 });
});
$(document).ready(function(){
	$("#informAddBtn").click(function(){
		var name = $("#name").val(); // 通知名称
		name = $.trim(name);
		if( name == "" || name == null ){
			lm.alert("通知名称不能为空!");
			return false;
		}
		
		var content = $("#content").val(); // 通知内容
		content = $.trim(content);
		
		if( content == "" || content == null ){
			lm.alert("通知内容不能为空!");
			return false;
		}

		var loseDate=$("#loseDate").val();//开始时间
		loseDate = $.trim(loseDate);
		if( loseDate =="" ||loseDate == null ){
			lm.alert("失效时间不能为空！");
			return false;
		}

		$("#informAddForm").submit();
	}); 
	
});

</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>${empty inform ? '添加' : '修改' }广播
			</strong>
		</div>
		<div class='panel-body'>
			<form id="informAddForm" method='post' class='form-horizontal' repeatSubmit='1' action="${contextPath }/inform/save">
								
				<div class="form-group">
					<label class="col-md-1 control-label">通知名称</label>
					<div class="col-md-2">
						<input type="text" id="name" name="name" value="${inform.name }"  class='form-control' maxlength="20"/>
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">通知范围</label>
					<div class="col-md-2">
						<select name="storeId" id="storeId" class="form-control" >
							<c:if test="${isMainStore == true }">
		    					<option value="-1">全部商家</option>
							</c:if>
	    					<c:forEach items="${scopeList}" var="shop">
	    						<option id="${shop.id }" value="${shop.id }">${shop.name }</option>
	    					</c:forEach>		
	    				</select>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">通知内容</label>
					<div class="col-md-2">
						<textarea id="content"  name="content" placeholder="200字以内" value="${inform.content }" cols=15 rows=5  class='form-control' maxlength="200">${inform.content }</textarea>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">失效时间</label>
					<div class="col-md-2">
						<input  readonly="readonly" type="text" id="loseDate" name="loseDate" value="<fmt:formatDate value="${inform.loseTime }" pattern="yyyy-MM-dd"/>"  class='form-control' />
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button"  id='informAddBtn' class='btn btn-primary' repeatSubmit='1' value="${empty inform ? '添加' : '修改' }" data-loading='稍候...' />
					</div>
				</div>
			</form>
	  </div>
   </div>
</body>
</html>