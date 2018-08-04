<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div class="panel" id="show_userCard_Model">
<m:table pageNo="${page.pageNo }" pages="${page.pages }" pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th width="5%"><input type="checkbox" id="pageALLCheckBox" onclick="checkPageALL(this);"/>全选</th>
			<th>会员手机号</th>
			<th>姓名</th>
			<th>卡号</th>
			<th>账户余额</th>
			<th>账户积分</th>
			<th>会员等级</th>
			<th>享受折扣</th>
			<th width="20%">服务套餐</th>
			<th>发卡人</th>
			<th>发卡日期</th>
			<th>卡状态</th>
			<th>发卡商家</th>
			<th width="6%">会员备注</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="userCard">
			<tr>
				<td><input type="checkbox"  id="${userCard.id }" sign="${userCard.id }" onclick="checkboxSelectEvent(this);"/></td>
				<td>${userCard.mobile }</td>
				<td>${userCard.name }</td>
				<td>${userCard.cardNum }</td>
				<td><fmt:formatNumber value="${userCard.balance }" type="currency" pattern="0.00"/></td>
				<td><fmt:formatNumber value="${userCard.point }" type="currency" pattern="0"/></td>
				<td>${userCard.userLevelName }</td>
				<td>${userCard.userLevelDiscount }</td>
					<td>${userCard.serviceNameArray }</td>
						<td>${userCard.accountName }</td>
							<td><fmt:formatDate value="${userCard.createdTime }" pattern="yyyy-MM-dd" /></td>
								<td>
								<c:if test="${userCard.status == 1 }">
									启用
								</c:if>
								<c:if test="${userCard.status == 2 }">
									<span style="color:red">禁用</span>
								</c:if>
								<c:if test="${userCard.status == 3 }">
									 <span style="color:#176AE8">挂失</span>
								</c:if>
								</td>
								<td>${userCard.storeName }</td>
								<td>${userCard.memo }</td>
				<td>
					<m:hasPermission permissions="userAccountManagerEdit"> 
						<c:if test="${userCard.status == 1 || userCard.status == 3 }">
							<a href='${contextPath }/userAccountManager/update/${userCard.id}' class='btn btn-small btn-warning'>充值</a>						
						</c:if>
					</m:hasPermission>
					<m:hasPermission permissions="userAccountManagerDetails"> 
						<c:if test="${userCard.status != 3 }">
							<a href='${contextPath }/userAccountManager/details/${userCard.id}' class='btn btn-small btn-warning'>详情</a>						
						</c:if>
					</m:hasPermission>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>
</div>