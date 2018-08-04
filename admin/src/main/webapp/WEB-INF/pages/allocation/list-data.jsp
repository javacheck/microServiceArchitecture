<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr> 
			<th>调拨时间</th>
			<th>调拨单号</th>
			<th>调出仓库</th>
			<th>调入仓库</th>
			<th>调拨状态</th>
			<th>调拨商品明细</th>
			<th>操作人</th>
			<th>备注</th>
			<th>创建时间</th>
			<th>完成时间</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="allocation">
			<tr>
				<td><fmt:formatDate value="${allocation.allocationTime }" pattern="yyyy-MM-dd"/> </td>
				<td>${allocation.allocationNumber }</td>
				<td>${allocation.fromStoreName }</td>
				<td>${allocation.toStoreName }</td>
				<td>
					<c:if test="${allocation.status==0 }">待审核</c:if>
					<c:if test="${allocation.status==1 }">待发货</c:if>
					<c:if test="${allocation.status==2 }">待收货</c:if>
					<c:if test="${allocation.status==3 }">已拒绝</c:if>
					<c:if test="${allocation.status==4 }">已完成</c:if>
				</td>
				<td>
					<c:if test="${allocation.status==4 }">
						<button type="button"  data-toggle="modal" name='infoButton' data-remote="${contextPath }/allocation/overInfo/showMode/${allocation.id }" class="btn btn-small btn-warning">查看</button>
					</c:if>
					<c:if test="${allocation.status==0 ||  allocation.status==1 || allocation.status==2 || allocation.status==3}">
						<button type="button"  data-toggle="modal" name='infoButton' data-remote="${contextPath }/allocation/info/showMode/${allocation.id }" class="btn btn-small btn-warning">查看</button>
					</c:if>
				</td>
				<td>${allocation.accountMobile }</td>
				<td>${allocation.memo }</td>
				<td><fmt:formatDate value="${allocation.createdTime }" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
				<td><fmt:formatDate value="${allocation.finishTime }" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
				<td>
					<c:if test="${allocation.status==0 }">
						<c:if test="${isMainStore==true}">
							<button type="button"  data-toggle="modal" name='infoButton' data-remote="${contextPath }/allocation/auditInfo/showMode/${allocation.id }" class="btn btn-small btn-warning">审核</button>
						</c:if>
					</c:if>
					<c:if test="${allocation.status==1}">
						<c:if test="${allocation.fromStoreId==storeId}">
							<button type="button"  data-toggle="modal" name='infoButton' data-remote="${contextPath }/allocation/sendInfo/showMode/${allocation.id }" class="btn btn-small btn-success">发货</button>
						</c:if>
					</c:if>
					<c:if test="${allocation.status==2}">
						<c:if test="${allocation.toStoreId==storeId}">
							<button type="button"  data-toggle="modal" name='infoButton' data-remote="${contextPath }/allocation/confirmInfo/showMode/${allocation.id }" class="btn btn-primary ">确认收货</button>
						</c:if>
					</c:if>
					
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>