<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th>代理商ID</th>
			<th>代理商名称</th>
			<th>代理商类型</th>
			<th>上级代理商</th>
			<th>联系方式</th>
			<c:if test="${isAdmin }">
				<th>操作</th>
			</c:if>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="agent">
			<tr>
				<td>${agent.id }</td>
				<td>${agent.name }</td>
				<c:if test="${agent.type==1 }">
					<td>总代理商</td>
				</c:if>
				<c:if test="${agent.type==2 }">
					<td>运营商</td>
				</c:if>
				<c:if test="${agent.type==3 }">
					<td>子公司</td>
				</c:if>
				<td>${agent.parentName }</td>
				<td>${agent.contactName }</td>
				<c:if test="${isAdmin }">
					<td><m:hasPermission permissions="agentEdit">
							<a href='${contextPath }/agent/update/${agent.id}'
								class='btn btn-small btn-warning'>修改</a>
						</m:hasPermission> <a href='${contextPath }/agent/businessList/${agent.id}'
						class='btn btn-small btn-success'>结算账户</a></td>
				</c:if>
			</tr>
		</c:forEach>
	</tbody>
</m:table>
