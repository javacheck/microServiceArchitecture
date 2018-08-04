<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<m:table pageNo="${page.pageNo }" pages="${page.pages }" pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th>订单编号</th>
			<th>包间号码</th>
			<th>包间名称</th>
			<th>包间类型</th>
			<th>额定人数</th>
			<th>消费时长</th>
			<th>消费总额</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="roomOpenRecord">
			<tr>
				<td>${roomOpenRecord.id }</td>
				<td>${roomOpenRecord.room.number }</td>
				<td>${roomOpenRecord.room.name }</td>
				<td>${roomOpenRecord.room.categoryName }</td>
				<td>${roomOpenRecord.room.persons }人</td>
				<td>${roomOpenRecord.duration }分钟</td>
				<td>
					￥<fmt:formatNumber value="${roomOpenRecord.totalAmount }" type="currency" pattern="0.00"/>
				</td>
				<td>
					<m:hasPermission permissions="roomOpenRecordDetails"> 
						<a href='${contextPath }/roomOpenRecord/details/${roomOpenRecord.id}' class='btn btn-small btn-warning'>详情</a>
					</m:hasPermission>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>