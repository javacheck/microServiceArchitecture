<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }" >
	<thead>
		<tr> 
			<th>盘点编号</th>
			<th>导入日期</th>
			<th>盘点人</th>
			<th>导入人员</th>
			<th>商家名称</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="stockCheck">
			<tr>
				<td>${stockCheck.id }</td>
				<td><fmt:formatDate value="${stockCheck.createdTime }" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
				<td>${stockCheck.checkedName }</td>
				<td>${stockCheck.accountMobile }</td>
				<td>${stockCheck.storeName }</td>
				<td>
					<m:hasPermission permissions="detailsList">
						<a  href="${contextPath }/stockCheck/detailsList/${stockCheck.id }" class="btn btn-small btn-danger" style="width: auto;margin-top:5px;">详情</a>
					</m:hasPermission>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>