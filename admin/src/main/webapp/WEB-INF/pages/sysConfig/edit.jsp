<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="${contextPath}/static/js/uploadPreview.js" type="text/javascript"></script>
<title>用户推荐管理</title>
<style type="text/css">
input.file{
    vertical-align:middle;
    position:relative;
    left:-218px;
    filter:alpha(opacity=0);
    opacity:0;
	z-index:1;
	*width:223px;
}

</style>
<script type="text/javascript">
$(function(){
	$("#sysConfigAddBtn").bind("click", function () {
		$("#sysConfigAddForm").submit();
	 })
});
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong><i class='icon-plust'></i>用户推荐管理 </strong>
		</div>
		<div class='panel-body'>
			<div  class='form-horizontal'>
				<form id="sysConfigAddForm" method='post' class='form-horizontal' repeatSubmit='1'
				action="${contextPath }/sysConfig/save">
					<input name="name" type="hidden" value="${sysConfig.name }" />
						<div class="form-group">
							<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>是否启用</label>
							<div class="col-md-2">
								<m:slider defaultStatus="${sysConfig.value=='RECOMMENDED'?'1':'0' }"  width="75" height="30"  inputName="value" on="开" off="关"></m:slider>
							</div>
						</div>
					
					<div class="form-group">
						<div class="col-md-offset-1 col-md-10">
							<input type="button"  repeatSubmit='1' id='sysConfigAddBtn' class='btn btn-primary' 
								value="修改" data-loading='稍候...' />
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>

</body>
</html>