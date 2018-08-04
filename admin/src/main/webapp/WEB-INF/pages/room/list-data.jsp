<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr>
			<th>包间号码</th>
			<th>包间名称</th>
			<th>包间类型</th>
			<th>包间状态</th>
			<th>目前收费标准</th>
			<th>额定人数</th>
			<th>所属商家</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="room">
			<tr>
				<td>${room.number }</td>
				<td>${room.name }</td>
				<td>${room.categoryName }</td>
				<td>
				 <c:if test="${room.status==0 }">关闭</c:if> 
				 <c:if test="${room.status==1 }">空闲</c:if>
				 <c:if test="${room.status==2 }">使用中</c:if>
				 <c:if test="${room.status==3 }">预定中</c:if>
				</td>
				<td>暂无标准</td>
				<td>${room.persons }</td>
				<td>${room.storeName }</td>
				<td>
					<c:if test="${room.status!=0 }">
					<a  href="${contextPath }/roomBooking/add/${room.id}"	class="btn btn-small btn-info">预定</a>
					</c:if>
					<m:hasPermission permissions="accountEdit">
					<a  href="${contextPath }/room/update/${room.id}"	class="btn btn-small btn-warning">包间设置</a>
					<c:if test="${room.status==1 }"><a  onclick="changStatus(${room.id},0)" class="btn btn-small btn-danger">关闭</a></c:if>
					<c:if test="${room.status==0 }"><a  onclick="changStatus(${room.id},1)" class="btn btn-small btn-danger">开启</a></c:if>
					</m:hasPermission>
					<!-- 
					<m:hasPermission permissions="accountDelete">
						<a id="delete_account_${account.id}" onclick="deleteConfirm(${account.id})" class="btn btn-small btn-danger">删除</a>
					</m:hasPermission> 
					 -->
				</td>
					
			</tr>
		</c:forEach>
	</tbody>
</m:table>