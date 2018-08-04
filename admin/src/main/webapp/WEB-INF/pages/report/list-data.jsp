<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<a href="" onclick="getExe()" class="btn btn-small btn-danger">导出报表</a>
<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr> 
			<th></th>
			<c:forEach items="${dateList }" var="date">
				<th>${date }</th>
			</c:forEach>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${sumproductNameListList }" var="name">
			<tr>
				<td>${name }</td>
			</tr>
		</c:forEach>
	</tbody>	
	<%-- 
	<thead>
		<tr> 
			<th>时间</th>
			<c:if test="${category==0}">
				<th>产品名称</th>
			</c:if>
			<c:if test="${category==1}">
				<th>分类名称</th>
			</c:if>
			<c:if test="${category==2}">
				<th>店铺</th>
			</c:if>
			<c:if test="${category==3}">
				<th>账号</th>
			</c:if>
			<th>金额</th>
			<th>数量</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="report">
			<tr>
				<td><fmt:formatDate value="${report.createdTime }" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
				<c:if test="${category==0}">
					<td>${report.productName}</td>
				</c:if>
				<c:if test="${category==1}">
					<td>${report.categoryName}</td>
				</c:if>
				<c:if test="${category==2}">
					<td>${report.storeName}</td>
				</c:if>
				<c:if test="${category==3}">
					<td>${report.accountName}</td>
				</c:if>
				<td>${report.sumPrice }</td>
				<td>${report.sumAmount }</td>
			</tr>
		</c:forEach>
			<tr>
				<td>总计</td>
				<td></td>
				<c:set var="total" value="${0 }"/>
				<c:forEach items="${page.data }" var="report">
					<c:set var="total" value="${total+report.sumPrice }"/>
				</c:forEach>
				<td><c:out value="${total }"/></td>
				<c:set var="total" value="${0 }"/>
				<c:forEach items="${page.data }" var="report">
					<c:set var="total" value="${total+report.sumAmount }"/>
				</c:forEach>
				<td><c:out value="${total }"/></td>
			</tr>
	</tbody>--%>
</m:table>