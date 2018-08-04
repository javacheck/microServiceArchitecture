<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }" >
	<thead>
		<tr> 
			<th>商店名称</th>
			<th>appId</th>
			<th>appKey</th>
			<th>支付渠道</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="payChannelInfo">
			<tr>
				<td>${payChannelInfo.storeName }</td>
				<td>${payChannelInfo.appId }</td>
				<td value='${payChannelInfo.appKey }'>******</td>
				<td>
					${payChannelInfo.type==0 ?'支付宝':'微信' }
				</td>
				<td>
						<a href="${contextPath }/payChannelInfo/update/${payChannelInfo.id}" class="btn btn-small btn-warning">修改</a> 
						<a  onclick="deletepayChannelInfo(${payChannelInfo.id});" id="deletebutton"class="btn btn-small btn-danger">删除</a> 
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>