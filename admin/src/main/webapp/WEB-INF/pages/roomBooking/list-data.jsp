<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr>
			<th>房间号码</th>
			<th>额定人数</th>
			<th>联系人</th>
			<th>手机号码</th>
			<th>预定时间</th>
			<th>预定时长</th>
			<th>创建时间</th>
			<th>操作人员</th>
			<th>状态</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="roomBooking">
			<tr>
				<td>${roomBooking.room.number }</td>
				<td>${roomBooking.room.persons }</td>
				<td>${roomBooking.contacts }</td>
				<td>${roomBooking.phone }</td>
				<td><fmt:formatDate value="${roomBooking.reserveDate }" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
				<td>${roomBooking.reserveDuration }小时</td>
				<td><fmt:formatDate value="${roomBooking.createdTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${roomBooking.accountMobile }</td>
				<td>
				 <c:if test="${roomBooking.status==0 }">预定中</c:if> 
				 <c:if test="${roomBooking.status==1 }">已开台</c:if>
				 <c:if test="${roomBooking.status==3 }">已取消</c:if>
				 </td>
				<td>
					<a  href="${contextPath }/roomBooking/update/${roomBooking.id}"	class="btn btn-small btn-info">详情</a>
					<m:hasPermission permissions="accountEdit">
					</m:hasPermission>
					<!-- 
					<a  href="${contextPath }/room/update/${room.id}"	class="btn btn-small btn-warning"></a>
					<m:hasPermission permissions="accountDelete">
						<a id="delete_account_${account.id}" onclick="deleteConfirm(${account.id})" class="btn btn-small btn-danger">删除</a>
					</m:hasPermission> 
					 -->
				</td>
					
			</tr>
		</c:forEach>
	</tbody>
</m:table>