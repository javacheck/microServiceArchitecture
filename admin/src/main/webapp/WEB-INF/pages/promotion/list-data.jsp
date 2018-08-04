<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th>序号</th>
			<th>促销类型</th>
			<th>促销名称</th>
			<th>促销状态</th>
			<th>适用范围</th>
			<th>开始时间</th>
			<th>结束时间</th>
			<th>促销数量</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody id="promotion_list_data_array">
		<c:forEach items="${page.data}" var="promotion">
			<tr>
				<td>${promotion.id }</td>
				
				<td>
				  <c:if test="${promotion.type == 1 }">
				  	首单
				  </c:if>
				  <c:if test="${promotion.type == 2 }">
				  	满减
				  </c:if>
				  <c:if test="${promotion.type == 3 }">
				  	折扣
				  </c:if>
				  <c:if test="${promotion.type == 4 }">
				  	组合
				  </c:if>
				</td>
				<td>${promotion.name }</td>
				
				<td>
				  <c:if test="${promotion.status == 0 }">
				  	关闭
				  </c:if>
				  <c:if test="${promotion.status == 1 }">
				  	打开
				  </c:if>
				</td>
				<td>
				  <c:if test="${promotion.scope == 0 }">
				  	全部
				  </c:if>
				  <c:if test="${promotion.scope == 1 }">
				  	app端
				  </c:if>
				  <c:if test="${promotion.scope == 2 }">
				  	pos端
				  </c:if>
				</td>
				<td><fmt:formatDate value="${promotion.startDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td><fmt:formatDate value="${promotion.endDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				
				<td>
					<c:if test="${promotion.total == -1 }">
						无限数量
					</c:if>
					<c:if test="${promotion.total != null && promotion.total != -1 }">
						${promotion.total}
					</c:if>
				</td>
				<td>
					<m:hasPermission permissions="promotionEdit">
						<a href='${contextPath }/promotion/update/${promotion.type}/${promotion.id}' class='btn btn-small btn-warning'>修改</a>
					</m:hasPermission>
					<m:hasPermission permissions="promotionDelete">
						<a onclick='deleteId(${promotion.id },${promotion.storeId });' id="deletebutton"class="btn btn-small btn-danger">删除</a>
					</m:hasPermission>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>