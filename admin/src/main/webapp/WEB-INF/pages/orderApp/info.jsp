<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>线上订单详情</title>
<style type="text/css">
h4.hh{
    text-align: center;
font-size: 20px;
font-weight: 100;
}
.guanbi{
    font-size: 10px;
color: rgb(0, 0, 0);
line-height: 25px;
}
.modal-header{
    border-bottom: 1px solid #797979;
}

.bor-no{
    border:none;
    width:92%;
    margin:0 auto;
}
.bor-no td{
    border:none;
    background-color:#fff;
    font-family: 'Arial Normal', 'Arial';
}
.biaoti{
    color:#169BD5;
    font-size:16px;
}
.bg-co td{
    background-color:rgba(242, 242, 242, 1);
    padding-left:30px;
}
.table-t{
    padding:0;
    border:1px solid #797979;
}
.table-t th,.table-t td{
    background-color:#fff;
    text-align: center;
}
.first{
    width:305px;
    border-left:none;
}
.text1{
    font-size: 20px;
color: #0000FF;
font-weight: 400;
}
.text2{
    font-size: 22px;
color: #FF0000;
font-weight: 400;
}
.text3{
    font-size: 22px;
font-weight: 400;
}
.text4{
    font-weight: 600;
}

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
				<h4 class="modal-title">线上订单详情</h4>
			</div>
			<table class="table table-hover table-striped table-bordered" cellpadding="0" cellspacing="0">
				 <tbody>
				 	<tr class="biaoti">
                		<td colspan="3">基本信息</td>
            		</tr>
            		<tr class="bg-co" style="width: 100%; height: 30px;">
						<td colspan="2" width="100%">商家名称：${order.store.name }</td>
            		</tr>
            		<tr class="bg-co"  style="width: 100%;height: 30px">
                    	<td width="50%">订单编号：${order.id }</td>
                    	<td width="50%">订单时间：<fmt:formatDate value="${order.createdTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                	</tr>
                	<tr class="bg-co"  style="width: 100%;height: 30px">
                    	<td width="50%">会员帐号：${order.user.mobile }</td>
                    	<td width="50%">交易状态：${order.statusDes }</td>
                	</tr>
                	<tr class="bg-co"  style="width: 100%;height: 30px">
                    	<td width="50%">商品总价：￥<fmt:formatNumber value="${order.price }"  type="currency" pattern="0.00"/></td>
                    	<td width="50%">交易总额：￥<fmt:formatNumber value="${order.sumPrice }"  type="currency" pattern="0.00"/></td>
                	</tr>
                	<tr class="bg-co" style="width: 100%; height: 30px;">
						<td colspan="2" width="100%">获取积分：${order.user.point }</td>
            		</tr>
            		
                	<tr class="biaoti" style="width: 100%;height: 30px">
                		<td colspan="3">配送信息</td>
                	</tr>
                	<tr class="bg-co" style="width: 100%;height: 30px">
	                    <td width="50%">配送费：￥<fmt:formatNumber value="${order.shipAmount }"  type="currency" pattern="0.00"/></td>
	                    <td width="50%">配送方式：${order.shipType }</td>
                	</tr>
                	<tr class="bg-co" style="width: 100%; height: 30px;">
						<td colspan="2" width="100%">配送地址：${order.address.address}</td>
            		</tr>
            		<tr class="bg-co" style="width: 100%; height: 30px;">
						<td colspan="2" width="100%">留言：${order.message }</td>
            		</tr>
                	<tr class="biaoti" style="width: 100%;height: 30px">
                		<td colspan="3">优惠信息</td>
            		</tr>
                	<tr class="bg-co" style="width: 100%;height: 30px">
	                    <td width="50%">满减：</td>
	                    <td width="50%">首单：</td>
                	</tr>
                	<tr class="bg-co" style="width: 100%;height: 30px">
	                    <td width="50%">折扣：</td>
	                    <td width="50%">优惠券：${order.cashGiftDesc }</td>
                	</tr>
                	<tr class="bg-co" style="width: 100%; height: 30px;">
						<td colspan="2" width="100%">总优惠：￥<fmt:formatNumber value="${order.price-order.actualPrice-order.balance+order.shipAmount}"  type="currency" pattern="0.00"/></td>
            		</tr>
            		
            		<!--支付信息  -->
                	<tr class="biaoti" style="width: 100%;height: 30px">
                		<td colspan="3">支付信息</td>
                	</tr>
                	<tr class="bg-co" style="width: 100%; height: 30px;">
						<td colspan="2" width="100%">
							<c:if test="${order.actualPrice==null}">
								实付金额：
							</c:if>
							<c:if test="${order.actualPrice!=null}">
								实付金额：￥<fmt:formatNumber value="${order.actualPrice+order.balance }"  type="currency" pattern="0.00"/>
							</c:if>
						</td>
            		</tr>
                	<tr class="bg-co" style="width: 100%;height: 30px">
	                    <td width="50%">支付方式1：</td>
	                    <td width="50%">支付金额：</td>
                	</tr>
                	<tr class="bg-co" style="width: 100%;height: 30px">
	                    <td width="50%">支付方式2：</td>
	                    <td width="50%">支付金额：</td>
                	</tr>
                	
                	<!--商品信息  -->
                	<tr class="biaoti" style="width: 100%;height: 30px">
                		<td colspan="3">商品信息</td>
                	</tr>
                	<tr style="width: 100%">
                    	<td colspan="2" width="100%">
                        	<table style="width: 100%;height: 100%" class="table table-hover table-striped table-bordered table-t">
	                            <thead>
	                                <tr class="text-center">
	                                    <th class="first">商品</th>
	                                    <th>规格</th>
	                                    <th>单价</th>
	                                    <th>数量</th>
	                                    <th>小计</th>
	                                </tr>
	                            </thead>
                            <tbody>
                            	<c:set value="0" var="price"></c:set>
                            	<c:set value="0" var="amount"></c:set>
                            	<c:set value="0" var="total"></c:set>
                            	<c:forEach items="${order.orderItems}" var="orderItem">
                                    <tr class="text-center">
                                        <td class="first">${orderItem.productStock.productName}</td>
                                        <td>${orderItem.productStock.attributeValues}</td>
                                        <td>￥<fmt:formatNumber value="${orderItem.price }"  type="currency" pattern="0.00"/></td>
                                        <td>${orderItem.amount}</td>
                                        <td>￥<fmt:formatNumber value="${orderItem.price*orderItem.amount }"  type="currency" pattern="0.00"/></td>
                                    </tr>
                                    <c:set value="${price +  orderItem.price}" var="price"></c:set>
                                    <c:set value="${amount +  orderItem.amount}" var="amount"></c:set>
                                    <c:set value="${total +  orderItem.price*orderItem.amount}" var="total"></c:set>
                                </c:forEach>
                                <tr>
	                                <td class="first">合计：</td>
	                                <td></td>
	                                <td id="price">￥<fmt:formatNumber value="${price }"  type="currency" pattern="0.00"/></td>
	                                <td id="amount">${amount }</td>
	                                <td id="total">￥<fmt:formatNumber value="${total }"  type="currency" pattern="0.00"/></td>
                                </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
				 </tbody>
			</table>
		</div>
	</div>
</body>
</html>