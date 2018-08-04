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
<title>商品出库</title>
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
.bor{
       width:100%;
    }
    
.bor th,.bor tr,.bor td{
    border:none;
    text-align: center;
    line-height: 25px;
}
</style>
<script type="text/javascript">

</script>
</head>
<body>

	<div class='panel'>
		<div class='panel-heading'>
			<strong><i class='icon-plust'></i>门店申请结果 </strong>
		</div>
		<div class='panel-body'>
			<div  class='form-horizontal'>
				<form id="productDeliveryRecordAddForm" method='post' class='form-horizontal' repeatSubmit='1' action="">
					
					<div class="form-group">
					<div id="stockSelect" class="col-md-12" style="max-height:450px;overflow-y: auto;overflow-x:auto; border:1px solid #ccc;">
						<table  class="bor">
							<thead>
								<tr> 
									<th>外部请求ID</th>
									<th>事件类型</th>
									<th>本条消息的ID</th>
									<th>通知发送时间</th>
									<th>通知类型</th>
									<th>支付宝申请流水ID</th>
									<th>门店审核状态</th>
									<th>门店是否上架</th>
									<th>门店是否在客户端显示</th>
									<th>接口版本</th>
									<th>签名方式</th>
									<th>签名</th>
									<th>支付宝门店ID</th>
									<th>结果码</th>
									<th>流水申请结果描述</th>
								</tr>
							</thead>
							<tbody id="tbAmount">
									<tr>
										<td>${asyncResult.request_id }</td>
										<td>${asyncResult.biz_type }</td>
										<td>${asyncResult.notify_id }</td>
										<td>${asyncResult.notify_time }</td>
										<td>${asyncResult.notify_type }</td>
										<td>${asyncResult.apply_id }</td>
										<td>${asyncResult.audit_status }</td>
										<td>${asyncResult.is_online }</td>
										<td>${asyncResult.is_show }</td>
										<td>${asyncResult.version }</td>
										<td>${asyncResult.sign_type }</td>
										<td>${asyncResult.sign }</td>
										<td>${asyncResult.shop_id }</td>
										<td>${asyncResult.result_code }</td>
										<td>${asyncResult.result_desc }</td>
									</tr>
							</tbody>
						</table>
					</div>
					</div>
				</form>
			</div>
		</div>
	</div>

</body>
</html>