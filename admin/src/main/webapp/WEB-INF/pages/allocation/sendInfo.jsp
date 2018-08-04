<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发货</title>
<style type="text/css">
</style>
<script type="text/javascript">
</script>
</head>
<body>
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" id="auditModalBtn"> 
					<span aria-hidden="true">×</span><span class="sr-only">关闭</span>
				</button>
				<h4 class="modal-title">发货</h4>
			</div>
				<div>
					<table style="width: 100%;height: 100%" class='table table-hover table-striped table-bordered'>
						<thead>
							<tr>
								<th style="text-align: center;">序号</th>
								<th style="text-align: center;">商品名称</th>
								<th style="text-align: center;">规格</th>
								<th style="text-align: center;">调拨数量</th>
								<th style="text-align: center;">我的库存</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${arss}" var="allocationRecordStock" varStatus="v">
								<tr class='text-center'>
									<td>${v.index+1}</td>
									<td>
										${allocationRecordStock.productName}
									</td>
									<td>${allocationRecordStock.attributeName}</td>
									<td>${allocationRecordStock.amount}</td>
									<td>
										<c:if test="${allocationRecordStock.stock<0}">无限</c:if>
										<c:if test="${allocationRecordStock.stock>=0}">${allocationRecordStock.stock}</c:if>
									</td>
									
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div class='text-center' style="margin-top: -10px;">
					<button type="button"  name='typeChangeButton' onclick="typeChage(${arss[0].allocationRecordId },2);" class="btn btn-primary ">发货</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<button type="button"  name='typeChangeButton' onclick="typeChage(${arss[0].allocationRecordId },3,'发货');" class="btn btn-warning ">拒绝</button>
				</div>
		</div>
	</div>
</body>
</html>