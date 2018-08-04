<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>调拨商品明细</title>
<style type="text/css">
</style>
<script type="text/javascript">
</script>
</head>
<body>
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">×</span><span class="sr-only">关闭</span>
				</button>
				<h4 class="modal-title">商品明细</h4>
			</div>
				<table style="width: 100%;height: 100%" class='table table-hover table-striped table-bordered'>
					<thead>
						<tr class='text-center'>
							<th style="text-align: center;">商品名称</th>
							<th style="text-align: center;">商品条码</th>
							<th style="text-align: center;">商品规格(属性值)</th>
							<th style="text-align: center;">调拨数量</th>
							<th style="text-align: center;">实际到货数量</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${arss}" var="allocationRecordStock">
							<tr class='text-center'>
								<td>${allocationRecordStock.productName}</td>
								<td>${allocationRecordStock.barCode}</td>
								<td>${allocationRecordStock.attributeName}</td>
								<td>${allocationRecordStock.amount}</td>
								<c:if test="${allocationRecordStock.amount==allocationRecordStock.lastAmount}">
									<td>${allocationRecordStock.lastAmount}</td>
								</c:if>
								<c:if test="${allocationRecordStock.amount!=allocationRecordStock.lastAmount}">
									<td style="color:red;">${allocationRecordStock.lastAmount}</td>
								</c:if>
							</tr>
						</c:forEach>
					</tbody>
				</table>
		</div>
	</div>
</body>
</html>