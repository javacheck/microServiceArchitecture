<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th>序号</th>
			<th>采购单号</th>
			<th>状态</th>
			<th>入库单号</th>
			<th>操作时间</th>
			<th>操作人</th>
			<th>单据图片</th>
			<th>备注</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="purchaseAcceptance" varStatus="v">
			<tr>
				<td>${v.count }</td>
				<td>${purchaseAcceptance.purchaseNumber }</td>
				<td>${purchaseAcceptance.status==1?"已入库":"未入库" }</td>
				<td>${purchaseAcceptance.storageNumber }</td>
				<td><fmt:formatDate value="${purchaseAcceptance.createdTime }" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
				<td>${purchaseAcceptance.mobile }</td>
				<td><img alt="单据图片"  width="80" height="80" src="${purchaseAcceptance.picUrl }" data-toggle="modal" name='infoButton' data-remote="${contextPath }/purchaseAcceptance/imgToBig/showMode/${purchaseAcceptance.id }" style="cursor: pointer;"/></td>
				<td>${purchaseAcceptance.memo }</td>
				
				<td>
					<m:hasPermission permissions="purchaseAcceptanceEdit">
						<c:if test="${purchaseAcceptance.status==0 }">
							<a href='${contextPath }/purchaseAcceptance/update/${purchaseAcceptance.id}' class='btn btn-small btn-warning'>修改</a>
						</c:if>
					</m:hasPermission> 
			</tr>
		</c:forEach>
	</tbody>
</m:table>
