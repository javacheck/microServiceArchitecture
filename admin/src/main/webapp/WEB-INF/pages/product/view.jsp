<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>订单管理</title>
<style type="text/css">
</style>
<script type="text/javascript">
$(document).ready(function(){
var action="${action}";
if(action==1){
	lm.alert("请先添加图片！");
	$("#closeImagePre").click();
}
});
</script>
</head>
<body>
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button id="closeImagePre" type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">×</span><span class="sr-only">关闭</span>
				</button>
				<h4 class="modal-title">图片预览</h4>
			</div>
			<table class="table table-hover table-striped table-bordered"cellpadding="0" cellspacing="0">

				<c:forEach items="${productStock.picUrlList }" var="p">
					<tr style="width: 100%;height: 30px">
						<td width="30%" align="center"><img alt="" src="${p}" style="width:200px;heigth:300px"></td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>
</body>
</html>