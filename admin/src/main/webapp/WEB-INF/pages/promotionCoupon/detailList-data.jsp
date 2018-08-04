<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }" >
	<thead>
		<tr> 
			<th>活动名称</th>
			<th>绑定用户</th>
			<th>使用范围</th>
			<th>优惠类型</th>
			<th>金额/折扣</th>
			<th>创建时间</th>
			<th>失效日期</th>
			<th>使用情况</th>
			<!-- <th>操作</th> -->
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="cashGift">
			<tr>
				<td>${cashGift.pcName }</td>
				<td>${cashGift.mobile }</td>
				<td>
					<c:if test="${cashGift.storeId == null }">全部商家</c:if>
					<c:if test="${cashGift.storeId!=null }">${cashGift.storeName}</c:if>
				</td>
				<td>
					<c:if test="${cashGift.type==0 }">折扣券</c:if>
					<c:if test="${cashGift.type==1 }">现金券</c:if>
				</td>
				<td>
						${cashGift.amount }
				</td>
				
				<td><fmt:formatDate value="${cashGift.createdTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td><fmt:formatDate value="${cashGift.validTime }" pattern="yyyy-MM-dd" /></td>
				
				<td>
					<c:if test="${cashGift.status==0 }">未使用</c:if>
					<c:if test="${cashGift.status==1 }">已使用</c:if>
					<c:if test="${cashGift.status==2 }">已过期</c:if>
				</td>
				<%-- <td>
					<a onclick='deleteCashGift(${cashGift.id});'
							id="deletebutton" class="btn btn-small btn-danger">删除</a> 
				</td>--%>
			</tr>
		</c:forEach>
	</tbody>
</m:table>