<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<m:table pageNo="${page.pageNo }" pages="${page.pages }" pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr>
			<th>会员帐号</th>
			<th>姓名</th>
			<th>类型</th>
			<th>消费数量</th>
			<th>消费时间</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="messageRechargeRecode">
			<tr>
				<td>${messageRechargeRecode.userAccountMobile }</td>
				<td>${messageRechargeRecode.name }</td>
				<td>
					<c:if test="${messageRechargeRecode.type == 1 }">
						通知
					</c:if>
				</td>
				<td>${messageRechargeRecode.saleNumber }</td>
				<td><fmt:formatDate value="${messageRechargeRecode.createdTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td>
					<input type="hidden" id="sendTime" name="sendTime" value="<fmt:formatDate value="${messageRechargeRecode.createdTime }" pattern="yyyy-MM-dd HH:mm:ss" />"/>
					<input type="hidden" id="messageContent" name="messageContent" value="${messageRechargeRecode.messageContent }"/>
					<input type="hidden" id="userAccountMobile" name="userAccountMobile" value="${messageRechargeRecode.userAccountMobile }"/>
					<input id="operationDetail" name="operationDetail" onclick="operationDetailFunction(this);" class="btn btn-info" type="button" style="width: auto;margin-top:5px;" value="查看信息详情">
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>