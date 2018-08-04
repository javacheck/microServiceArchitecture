<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>支付宝门店列表</title>
<script type="text/javascript">

</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>支付宝口碑开店</strong>
			<a href="${contextPath }/alipayShop/add" class="btn btn-primary" name="alipay_addHref" style="margin-top: 5px;">
			<i class="icon-plus"></i>添加</a>
		</div>
		<div class='panel-body'>
			<div class='form-horizontal'>
			</div>
			<iframe id="iframform" name="iframform" style="display: none;"></iframe>
				<c:if test="${not empty errorMsg }">
					<div class="panel panel-warning" style="overflow:scroll;max-height: 400px">
						<div class="panel-heading">
		    				${errorMsg }
		  				</div>
	  				</div>
	  				<br/>
  				</c:if>
  					</div>
  						</div>
</body>
</html>