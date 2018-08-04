<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>POS收银详情</title>
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
	$(function(){
		$("td[name='attributeName']").each(function(){
			var html = $.trim($(this).html()).split("|");
			if (html.length > 1){
				var content = "";
				for (var i = 1; i < html.length ; i++){
					if (content == ""){
						content = html[i];
					}else {
						content = content + "|" + html[i];
					}
				}
				$(this).html(content);
			}
		});
		$("td[name='productName']").each(function(){
			var html = $.trim($(this).html()).split("|");
			if (html.length > 1){
				$(this).html(html[0]);
			}
		});
		
	});
</script>
</head>
<body>
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">×</span><span class="sr-only">关闭</span>
				</button>
				<h4 class="modal-title">POS收银详情</h4>
			</div>
			<table class="table table-hover table-striped table-bordered" cellpadding="0" cellspacing="0">
				 <tbody>
				 	<tr class="biaoti">
                		<td colspan="3">收银员信息</td>
            		</tr>
            		<tr class="bg-co"  style="width: 100%;height: 30px">
                    	<td width="50%">收银员：${order.account.name }</td>
                    	<td width="50%">手机号码：${order.account.mobile }</td>
                	</tr>
                	<tr class="bg-co" style="width: 100%; height: 30px;">
						<td colspan="2" width="100%">性别：${order.account.sex==0?'男':'女' }</td>
            		</tr>
            		<c:if test="${order.user!=null && order.user!='' }">
            		<tr class="biaoti">
                		<td colspan="3">会员信息</td>
            		</tr>
            		<tr class="bg-co"  style="width: 100%;height: 30px">
                    	<td width="50%">会员姓名：${order.userCard.name }</td>
                    	<td width="50%">会员手机：${order.userCard.mobile }</td>
                	</tr>
                	<tr class="bg-co" style="width: 100%; height: 30px;">
                		<td width="50%">会员卡号：${order.userCard.cardNum }</td>
                		<c:if test="${order.user.sex==0}">
						<td width="50%">会员性别：男</td>
						</c:if>
						<c:if test="${order.user.sex==1}">
						<td width="50%">会员性别：女</td>
						</c:if>
						<c:if test="${order.user.sex==2}">
						<td width="50%">会员性别：</td>
						</c:if>
            		</tr>
            		<tr class="bg-co"  style="width: 100%;height: 30px">
                    	<td colspan="2" width="100%">会员备注：${order.userCard.memo }</td>
                	</tr>
            		</c:if>
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
                    	<td width="50%">获取积分：<fmt:formatNumber value="${order.getPoint }"  type="currency" pattern="0"/></td>
                    	<td width="50%">交易状态：${order.statusDes }</td>
                	</tr>
                	<tr class="bg-co"  style="width: 100%;height: 30px">
                    	<td width="50%">商品总价：￥<fmt:formatNumber value="${order.price }"  type="currency" pattern="0.00"/></td>
                    	<td width="50%">交易总额：￥<fmt:formatNumber value="${order.actualPrice }"  type="currency" pattern="0.00"/></td>
                	</tr>
            		
                	<!--优惠信息  -->
                	<tr class="biaoti" style="width: 100%;height: 30px">
                		<td colspan="3">优惠信息</td>
            		</tr>
                	<tr class="bg-co" style="width: 100%;height: 30px">
	                    <td width="50%">满减：</td>
	                    <td width="50%">首单：</td>
                	</tr>
                	<tr class="bg-co" style="width: 100%;height: 30px">
	                    <td width="50%">折扣：<c:if test="${order.discount != 10}">${order.discount }折</c:if></td>
	                    <td width="50%">优惠券：${order.cashGift.desc }</td>
                	</tr>
                	<tr class="bg-co" style="width: 100%; height: 30px;">
						<td colspan="2" width="100%">总优惠：￥<fmt:formatNumber value="${order.price-order.actualPrice}"  type="currency" pattern="0.00"/></td>
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
								实付金额：￥<fmt:formatNumber value="${order.actualPrice }"  type="currency" pattern="0.00"/>
							</c:if>
						</td>
            		</tr>
                	<tr class="bg-co" style="width: 100%;height: 30px">
	                    <td width="50%">支付方式1：
	                    	<c:if test="${order.paymentMode == 0}">支付宝</c:if>
	                    	<c:if test="${order.paymentMode == 1}">微信</c:if>
	                    	<c:if test="${order.paymentMode == 2}">刷卡</c:if>
	                    	<c:if test="${order.paymentMode == 3}">现金</c:if>
	                    	<c:if test="${order.paymentMode == 9}">会员卡</c:if>
	                    </td>
	                    <td width="50%">支付金额：
	                    	<c:choose>
		                    	<c:when test="${order.paymentMode == 3 }">
		                    		￥<fmt:formatNumber value="${order.actualPrice }"  type="currency" pattern="0.00"/>
		                    	</c:when>
		                    	<c:when test="${order.paymentMode == 9 && order.cashPrice - order.change - order.actualPrice == 0}">
		                    		￥<fmt:formatNumber value="0"  type="currency" pattern="0.00"/>
		                    	</c:when>
		                    	<c:when test="${order.paymentMode == 9 && order.cashPrice - order.change - order.actualPrice < 0}">
		                    		￥<fmt:formatNumber value="${order.actualPrice  - order.cashPrice  + order.change}"  type="currency" pattern="0.00"/>
		                    	</c:when>
		                    	<c:otherwise>
		                    		￥<fmt:formatNumber value="${order.actualPrice - order.cashPrice  }"  type="currency" pattern="0.00"/>
		                    	</c:otherwise>
	                    	</c:choose>
	                    </td>
                	</tr>
                	<c:if test="${order.paymentMode != 3 && order.cashPrice > 0 }">
	                	<tr class="bg-co" style="width: 100%;height: 30px">
		                    <td width="50%">支付方式2：现金</td>
		                    <td width="50%">支付金额：￥<fmt:formatNumber value="${order.cashPrice - order.change }"  type="currency" pattern="0.00"/></td>
	                	</tr>
                	</c:if>
                	<tr class="bg-co" style="width: 100%;height: 30px">
	                    <td width="50%">收取现金：￥<fmt:formatNumber value="${order.cashPrice }"  type="currency" pattern="0.00"/></td>
	                    <td width="50%">找零：
	                    	<c:choose>
	                    		<c:when test="${order.paymentMode  == 9 }">
	                    			￥<fmt:formatNumber value="${order.change }"  type="currency" pattern="0.00"/>
	                    		</c:when>
		                    	<c:when test="${order.cashPrice > order.actualPrice }">
	                    			￥<fmt:formatNumber value="${order.cashPrice - order.actualPrice }"  type="currency" pattern="0.00"/>
	                    		</c:when>
	                    		<c:otherwise>
	                    			￥0.00
	                    		</c:otherwise>
	                    	</c:choose>
	                    </td>
                	</tr>
                	
                	<!-- 售后  -->
                	<c:if test="${ not empty order.orderReturnGoods}">
                			<tr class="biaoti" style="width: 100%;height: 30px">
		                		<td colspan="3">售后</td>
		                	</tr>
		                	<tr style="width: 100%">
		                    	<td colspan="2" width="100%">
		                        	<table style="width: 100%;height: 100%" class="table table-hover table-striped table-bordered table-t">
			                            <thead>
			                                <tr class="text-center">
			                                    <th>退款时间</th>
			                                    <th>退货方式</th>
			                                    <th>退货数量</th>
			                                    <th>退款金额</th>
			                                    <th>减扣积分</th>
			                                    <th width="30%">退款原因</th>
			                                </tr>
			                            </thead>
		                            <tbody>
		                            	<c:set value="0" var="number"></c:set>
		                            	<c:set value="0" var="price"></c:set>
		                            	<c:set value="0" var="point"></c:set>
		                            	<c:forEach items="${order.orderReturnGoods}" var="orderReturnGoods">
		                                    <tr class="text-center">
		                                        <td><fmt:formatDate value="${orderReturnGoods.createdTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
		                                        <td>${orderReturnGoods.returnType == 0 ? '退储值' : '退现金' }</td>
		                                        <td>${orderReturnGoods.returnNumber}</td>
		                                        <td>￥<fmt:formatNumber value="${orderReturnGoods.returnPrice }"  type="currency" pattern="0.00"/></td>
		                                        <td>${orderReturnGoods.returnPoint}</td>
		                                        <td>${orderReturnGoods.returnReason}</td>
		                                    </tr>
		                                    <c:set value="${number +  orderReturnGoods.returnNumber}" var="number"></c:set>
		                                    <c:set value="${price +  orderReturnGoods.returnPrice}" var="price"></c:set>
		                                    <c:set value="${point +  orderReturnGoods.returnPoint}" var="point"></c:set>
		                                </c:forEach>
		                                <tr>
			                                <td>合计：</td>
			                                <td></td>
			                                <td id="number">${number }</td>
			                                <td id="price">￥<fmt:formatNumber value="${price }"  type="currency" pattern="0.00"/></td>
			                                <td id="point">${point }</td>
			                                <td></td>
		                                </tr>
		                            </tbody>
		                        </table>
		                    </td>
		                </tr>
                	</c:if>
                
                
                
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
	                                    <th>售后状态</th>
	                                    <th>退货数量</th>
	                                    <th>退款金额</th>
	                                </tr>
	                            </thead>
                            <tbody>
                            	<c:set value="0" var="price"></c:set>
                            	<c:set value="0" var="amount"></c:set>
                            	<c:set value="0" var="total"></c:set>
                            	<c:set value="0" var="returnPriceTotal"></c:set>
                            	<c:set value="0" var="returnNumberTotal"></c:set>
                            	<c:forEach items="${order.orderItems}" var="orderItem">
                                    <tr class="text-center">
                                        <td class="first" name="productName">${orderItem.productStock.attributeName}</td>
                                        <td name="attributeName">${orderItem.productStock.attributeName}</td>
                                        <td>￥<fmt:formatNumber value="${orderItem.price }"  type="currency" pattern="0.00"/></td>
                                        <td>${orderItem.amount}</td>
                                        <td>￥<fmt:formatNumber value="${orderItem.price*orderItem.amount }"  type="currency" pattern="0.00"/></td>
                                        <td>${orderItem.returnNumber == 0 ? '无' : '已退货' }</td>
                                        <td>${orderItem.returnNumber}</td>
                                        <td><fmt:formatNumber value="${orderItem.returnPrice}"  type="currency" pattern="0.00"/></td>
                                    </tr>
                                    <c:set value="${price +  orderItem.price}" var="price"></c:set>
                                    <c:set value="${amount +  orderItem.amount}" var="amount"></c:set>
                                    <c:set value="${total +  orderItem.price*orderItem.amount}" var="total"></c:set>
                                    <c:set value="${returnPriceTotal +  orderItem.returnPrice}" var="returnPriceTotal"></c:set>
                                    <c:set value="${returnNumberTotal +  orderItem.returnNumber}" var="returnNumberTotal"></c:set>
                                </c:forEach>
                                <tr>
	                                <td class="first">合计：</td>
	                                <td></td>
	                                <td id="price"></td>
	                                <td id="amount">${amount }</td>
	                                <td id="total">￥<fmt:formatNumber value="${total }"  type="currency" pattern="0.00"/></td>
	                                <td></td>
	                                <td id="returnNumberTotal">${returnNumberTotal }</td>
	                                <td id="returnPriceTotal">￥<fmt:formatNumber value="${returnPriceTotal }"  type="currency" pattern="0.00"/></td>
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