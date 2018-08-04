<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }" >
	<thead>
		<tr> 
			
			<th>优惠券活动名称</th>
			<th>发放方式</th>
			<th>类型</th>
			<th>金额/折扣</th>
			<th>数量</th>
			<th>发放用户</th>
			<th>活动开始时间</th>
			<th>活动结束时间</th>
			<th>适用商家</th>
			<th>订单金额满</th>
			<th>状态</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="promotionCoupon">
			<tr>
				<td>${promotionCoupon.name }</td>
				
				<td>
					<c:if test="${promotionCoupon.issueType==0 }">自动发放</c:if>
					<c:if test="${promotionCoupon.issueType==1 }">手动领取</c:if>
					<c:if test="${promotionCoupon.issueType==2 }">线下发放</c:if>
				</td>
				
				<td>
					<c:if test="${promotionCoupon.type==0 }">折扣券</c:if>
					<c:if test="${promotionCoupon.type==1 }">现金券</c:if>
				</td>
				
				<td>
				<c:if test="${promotionCoupon.type==0 }">
					<c:if test="${promotionCoupon.minAmount==promotionCoupon.maxAmount }">
						${promotionCoupon.minAmount }
					</c:if>
					<c:if test="${promotionCoupon.minAmount!=promotionCoupon.maxAmount }">
						${promotionCoupon.minAmount }~${promotionCoupon.maxAmount }
					</c:if>
				</c:if>
				<c:if test="${promotionCoupon.type==1 }">
					<c:if test="${promotionCoupon.minAmount==promotionCoupon.maxAmount }">
						${promotionCoupon.minAmount }
					</c:if>
					<c:if test="${promotionCoupon.minAmount!=promotionCoupon.maxAmount }">
						${promotionCoupon.minAmount }~${promotionCoupon.maxAmount }
					</c:if>
				</c:if>
				
				</td>
				<td>${promotionCoupon.totalNum==0?'无限制':promotionCoupon.totalNum }</td>
				<td>
					<c:if test="${promotionCoupon.range==0 }">所有用户</c:if>
					<c:if test="${promotionCoupon.range==1 }">新注册用户</c:if>
					<c:if test="${promotionCoupon.range==2 }">已注册用户</c:if>
				</td>
				<td><fmt:formatDate value="${promotionCoupon.startDate }" pattern="yyyy-MM-dd" /></td>
				<td><fmt:formatDate value="${promotionCoupon.endDate }" pattern="yyyy-MM-dd" /></td>
				<td>
					<c:if test="${promotionCoupon.storeId == null }">全部商家</c:if>
					<c:if test="${promotionCoupon.storeId!=null }">${promotionCoupon.storeName}</c:if>
				</td>
				<td>${promotionCoupon.orderAmount }</td>
				<td>
					<c:if test="${promotionCoupon.promotionStatus==0 }">已停止</c:if>
					<c:if test="${promotionCoupon.promotionStatus==1 }">进行中</c:if>
					<c:if test="${promotionCoupon.promotionStatus==2 }">即将开始</c:if>
				</td>
				<td>
					<m:hasPermission permissions="promotionCouponEdit">
						<c:if test="${isSys==true || promotionCoupon.storeId !=null }">
							<a href="${contextPath }/promotionCoupon/update/${promotionCoupon.id}" class="btn btn-small btn-warning">修改</a> 
						</c:if>
					</m:hasPermission>
					<m:hasPermission permissions="promotionCouponDetail">
						<c:if test="${isSys==true || promotionCoupon.storeId !=null }">
							<a href="${contextPath }/promotionCoupon/detailList/${promotionCoupon.id}" class="btn btn-small btn-success">发放详情</a> 
						</c:if>
					</m:hasPermission>
						<c:if test="${promotionCoupon.issueType == 2 && promotionCoupon.promotionStatus != 0 }">
							<c:if test="${promotionCoupon.reportNum != promotionCoupon.totalNum }">
								<a onclick='reportCashGiftBarcode(${promotionCoupon.id });' id="reportCashGiftBarcode" class="btn btn-small btn-danger">导出</a>
							</c:if>
							 
							<c:if test="${promotionCoupon.reportNum == promotionCoupon.totalNum }">
								<a href="${contextPath}/promotionCoupon/ajax/reportCashGiftBarcode?promotionCouponId=${promotionCoupon.id}" id="reportPromotionCoupon" class="btn btn-small btn-danger">重新导出</a>
							</c:if>
						</c:if>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>