<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>消费记录</title> 
<style>
	.inlin{
		display:inline-block;
		width:31%;
	}
	.inlin label{
	 	width:28%;
	 }
	 .inlin .col-md-0{
	 	width:2%;
	 }
	 .inlin .col-md-2{
	 	width:54%;
	 }
	 .inlin2{
	 	
		display:inline-block;
		width:69%;
	 }
	 .inlin2 .col-md-2{
	 	width:26%;
	 }
	 .text-c,.text-c input{
	 	text-align: center;
	 }
	 .text-c input{
	 	border:none;
	 }
	 .mag-bt{
	 	margin-bottom: 0;
	 }
	 .bg-c{
	 	background-color:#ECECEC;
	 }
	 .padd-t{
	 	margin-top: 6px;
	 	display: inline-block;
	 }
	 .bg-co td{
	 	color:red;
	 }
	 
</style>
<script type="text/javascript">

$(document).ready(function(){
	// 保存信息start
	$("#roomOpenRecordDetailsAddBtn").click(function(){
		$("#roomOpenRecordDetailsAddForm").submit();
	}); 
	// 保存信息end
});

</script>
</head>
<body>
	<div class='panel'>
	
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>消费记录
			</strong>
		</div>
		
		<div class='panel-body'>
			<form id="roomOpenRecordDetailsAddForm" method='post' autocomplete="off" class='form-horizontal' action="${contextPath }/roomOpenRecord/list">
				
					<div class="form-group inlin">
						<label class="col-md-1 control-label">开台时间</label>
						<div class="col-md-2">
							<input type="text" id="startTime" name="startTime" readonly="readonly" class='form-control' value="<fmt:formatDate value="${roomOpen.startTime }" pattern="yyyy-MM-dd HH:mm:ss" />" maxlength="20"  >
						</div>
					</div>
						
					<div class="form-group inlin2">
						<label class="col-md-1 control-label">订单编号</label>
						<div class="col-md-2">
							<input type="text" id="id" name="id" readonly="readonly" class='form-control' value="${roomOpen.id }" maxlength="20"  >
						</div>
					</div>	
				
					<div class="form-group inlin">
						<label class="col-md-1 control-label">包间名称</label>
						<div class="col-md-2">
							<input type="text" id="name" name="name" readonly="readonly" class='form-control' value="${roomOpen.room.name }" maxlength="20"  >
						</div>
					</div>
				
					<div class="form-group inlin2">
						<label class="col-md-1 control-label">包间号码</label>
						<div class="col-md-2">
							<input type="text" id="number" name="number" readonly="readonly" class='form-control' value="${roomOpen.room.number }" maxlength="20"  >
						</div>
					</div>
					
					<div class="form-group inlin">
						<label class="col-md-1 control-label">包间类型</label>
						<div class="col-md-2">
							<input type="text" id="categoryName" name="categoryName" readonly="readonly" class='form-control' value="${roomOpen.room.categoryName }" maxlength="50"  >
						</div>
					</div>
				
					<div class="form-group inlin2">
						<label class="col-md-1 control-label">额定人数</label>
						<div class="col-md-2">
							<input type="text" id="persons" name="persons" readonly="readonly" class='form-control' value="${roomOpen.room.persons }" maxlength="20"  >
						</div>
					</div>
				
					<div class="form-group inlin">
						<label class="col-md-1 control-label">消费时长</label>
						<div class="col-md-2">
							<input type="text" id="duration" name="duration" readonly="readonly" class='form-control' value="${roomOpen.duration } 分钟" maxlength="50"  >
						</div>
					</div>
				
					<div class="form-group inlin2">
						<label class="col-md-1 control-label">消费总额</label>
						<div class="col-md-2">
							<input type="text" id="totalAmount" name="totalAmount" readonly="readonly" class='form-control' value="￥<fmt:formatNumber value="${roomOpen.totalAmount }" type="currency" pattern="0.00"/>" maxlength="50"  >
						</div>
					</div>
					
					<label class="col-md-1 control-label">包间消费明细：</label>
					<c:if test="${not empty roomOpenDetailArray }">
							<table border="1" class="text-c">
									<tr class="bg-c">
										<td width="30%">名称</td>
										<td>收费单价(元)</td>
										<td>数量</td>
										<td>状态</td>
										<td>小计</td>
									</tr>
									<c:set value="0" var="total"></c:set>
									<c:forEach items="${roomOpenDetailArray }" varStatus="sortNo" var="roomOpenDetail">
										<c:set var="v" value="${ sortNo.index}"></c:set>
											<tr>
												<td>
													<div class="form-group mag-bt" id="name_div_${v}">
														<div class="col-md-12">
															<input type="text" id="name_${v}" readonly="readonly" value="${roomOpenDetail.name }" maxlength="50"/>
														</div>
													</div>
												</td>
												<td>
													<div class="form-group mag-bt" id="price_${v}">
														<div class="col-md-12">
															<c:if test="${roomOpenDetail.type==0 }">
															 <c:set var="itemDuration" value="${roomOpenDetail.number }"></c:set>
															</c:if>
															<c:if test="${roomOpenDetail.type!=0 }">
															<input type="text" id="price_${v}" readonly="readonly" value="￥<fmt:formatNumber value="${roomOpenDetail.price }" type="currency" pattern="0.00"/>" maxlength="50"/>
															</c:if>
														</div>
													</div>
												</td>
												<td>
													<div class="form-group mag-bt" id="number_div_${v}">
														<div class="col-md-2">
															<input type="text" id="number_${v}" readonly="readonly" value="${roomOpenDetail.number  }" maxlength="50"/>
														</div>
													</div>
												</td>
												<td>
													<div class="form-group mag-bt" id="number_div_${v}">
														<div class="col-md-2">
															<c:if test="${roomOpenDetail.status==1 }">
															<input type="text" id="number_${v}" readonly="readonly" value="已付" maxlength="50"/>
															</c:if>
															<c:if test="${roomOpenDetail.status==2 }">
															<input type="text" id="number_${v}" readonly="readonly" value="未付" maxlength="50"/>
															</c:if>
														</div>
													</div>
												</td>
													<td>
													<div class="form-group mag-bt" id="total_div_${v}">
														<div class="col-md-2">
															<input type="text" id="total_${v}" readonly="readonly" value="￥<fmt:formatNumber value="${ roomOpenDetail.total }" type="currency" pattern="0.00"/>" maxlength="50"/>
															<c:set value="${total +  roomOpenDetail.total  * 100 }" var="total"></c:set>
														</div>
													</div>
												</td>
											</tr>
											<c:set var="refundPrice" value="${roomOpenDetail.refundPrice}"></c:set>
	                       					<c:set var="duration" value="${roomOpenDetail.duration}"></c:set>
									</c:forEach>
									  <c:if test="${duration !=itemDuration*60}">
				                    	<tr class="bg-co">
				                    		<td class="first">房间退款</td>
				                    		<td></td>
				                    		<td><fmt:formatNumber value="${duration/60-itemDuration }" type="currency" pattern="0.0"/></td>
				                    		<td>已退</td>
				                    		<td>￥<fmt:formatNumber value="-${refundPrice  }" type="currency" pattern="0.00"/></td>
				                    	</tr>
				                    	<c:set var="total" value="${total -  refundPrice * 100 }"></c:set>
				                     </c:if>
									<tr >
										<td>合计</td>
										<td></td>
										<td></td>
										<td></td>
										<td>￥${total / 100 }</td>
									</tr>
							</table>
					</c:if>
					<c:if test="${empty roomOpenDetailArray }">
						  <spam class="padd-t">无</spam>
					</c:if>		
					
						<div class="form-group">
						<label class="col-md-1 control-label"></label>
						<div class="col-md-2" style="margin-top: 3px;">
						</div>
					</div>
					
					<div class="form-group">
						<div class="col-md-offset-1 col-md-10">
							<input type="button" id='roomOpenRecordDetailsAddBtn' class='btn btn-primary' value="确定" data-loading='稍候...' />
						</div>
					</div>
				
			</form>
	  </div>
   </div>
</body>
</html>