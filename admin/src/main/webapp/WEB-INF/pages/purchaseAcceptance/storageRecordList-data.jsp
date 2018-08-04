<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<m:table pageNo="${page.pageNo }" pages="${page.pages } " id="storageListDataId"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr> 
			<th>序号</th>
			<th>入库单号</th>
			<th>操作人</th>
			<th>入库时间</th>
			<th>操作时间</th>
			<th>入库备注</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="productStorageRecord" varStatus="var">
			<tr style="cursor: pointer;">
				<td valueName="a">${var.count }</td>
				<td valueName="a">${productStorageRecord.storageNumber }</td>
				<td valueName="a">${productStorageRecord.mobile }</td>
				<td valueName="a"><fmt:formatDate value="${productStorageRecord.storageTime }" pattern="yyyy-MM-dd"/> </td>
				<td valueName="a"><fmt:formatDate value="${productStorageRecord.createdTime }" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
				<td valueName="a">${productStorageRecord.memo }</td>
				<td>
					<button type="button"  data-toggle="modal" name='infoButton' data-remote="${contextPath }/productStorageRecord/info/showMode/${productStorageRecord.id }" class="btn btn-primary ">查看明细</button>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>