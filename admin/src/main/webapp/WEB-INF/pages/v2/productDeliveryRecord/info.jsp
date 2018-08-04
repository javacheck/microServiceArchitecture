<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品明细</title>
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
				<h4 class="modal-title">商品明细</h4>
			</div>
				<div>
					<table style="width: 100%;height: 100%" class='table table-hover table-striped table-bordered'>
						<thead>
							<tr>
								<th style="text-align: center;">序号</th>
								<th style="text-align: center;">商品名称</th>
								<th style="text-align: center;">商品条码</th>
								<th style="text-align: center;">规格</th>
								<th style="text-align: center;">单位</th>
								<th style="text-align: center;">进货价</th>
								<th style="text-align: center;">出库价</th>
								<th style="text-align: center;">出库数量</th>
								<th style="text-align: center;">库存数量</th>
								<th style="text-align: center;">商品备注</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${pdrs}" var="productDeliveryRecordStock" varStatus="v">
								<tr class='text-center'>
									<td>${v.count}</td>
									<td>${productDeliveryRecordStock.productName }</td>
									<td>${productDeliveryRecordStock.barCode}</td>
									<td>${productDeliveryRecordStock.attributeValues}</td>
									<td>${productDeliveryRecordStock.unitName}</td>
									<td>
										<c:if test="${productDeliveryRecordStock.costPrice==null || productDeliveryRecordStock.costPrice=='' }"></c:if>
										<c:if test="${productDeliveryRecordStock.costPrice!=null && productDeliveryRecordStock.costPrice!=''}">￥<fmt:formatNumber value="${productDeliveryRecordStock.costPrice }"  type="currency" pattern="0.00"/></c:if>
									</td>
									
									<td>
										<c:if test="${productDeliveryRecordStock.shipmentPrice !=null} ">
											￥<fmt:formatNumber value="${productDeliveryRecordStock.shipmentPrice }"  type="currency" pattern="0.00"/>
										</c:if>
									</td>
									<td><fmt:formatNumber value="${productDeliveryRecordStock.amount }"  type="currency" pattern="0.00"/></td>
									<td><fmt:formatNumber value="${productDeliveryRecordStock.stock }"  type="currency" pattern="0.00"/></td>
									<td>${productDeliveryRecordStock.memo}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
		</div>
	</div>
</body>
</html>