<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<m:table pageNo="${page.pageNo }" pages="${page.pages }" pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th width="35%">等级名称</th>
			<th>升级方式</th>
			<th>类型</th>
			<th>累计积分/累计消费(元)</th>
			<th>享受折扣</th>
			<th>创建时间</th>
			<th>创建人</th>
			<th>更新时间</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="userLevelDefinition">
			<tr>
				<td>${userLevelDefinition.name }</td>
				<td>
					<c:if test="${userLevelDefinition.mode == 0 }">
						自动升级
					</c:if>
					<c:if test="${userLevelDefinition.mode == 1 }">
						手动升级
					</c:if>
				</td>
				<td>
					<c:if test="${userLevelDefinition.type == 0 }">
						累计积分
					</c:if>
					<c:if test="${userLevelDefinition.type == 1 }">
						累计消费
					</c:if>
				</td>
				<td>
					<c:if test="${userLevelDefinition.type == 0 }">
						<fmt:formatNumber value='${userLevelDefinition.point }' type='currency' pattern='0'/>
					</c:if>
					<c:if test="${userLevelDefinition.type == 1 }">
						<fmt:formatNumber value='${userLevelDefinition.point }' type='currency' pattern='0.00'/>
					</c:if>
				
				</td>
				<td>
					<c:if test="${userLevelDefinition.discount != 10  }">
						${userLevelDefinition.discount }					
					</c:if>
					
				</td>
				<td><fmt:formatDate value="${userLevelDefinition.createdTime }" pattern="yyyy-MM-dd" /></td>
				<td>${userLevelDefinition.storeName }</td>
				<td><fmt:formatDate value="${userLevelDefinition.updateTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>
						<c:if test="${isMainStore == true }">
						<m:hasPermission permissions="userLevelDefinitionEdit">
							<a href='${contextPath }/userLevelDefinition/update/${userLevelDefinition.id}' class='btn btn-small btn-warning'>修改</a>
						</m:hasPermission>
						
						<m:hasPermission permissions="userLevelDefinitionDelete">
							<a onclick='deleteById(${userLevelDefinition.id });' id="deletebutton"class="btn btn-small btn-danger">删除</a>
						</m:hasPermission>
						</c:if>
						<c:if test="${isMainStore == false }">
							<a href='${contextPath }/userLevelDefinition/details/${userLevelDefinition.id}' class='btn btn-small btn-warning'>详情</a>
						</c:if>
					</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>