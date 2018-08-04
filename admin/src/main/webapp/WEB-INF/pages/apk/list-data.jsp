<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }">
	<thead>
		<tr class='text-center'>
			<th>APK类型</th>
			<th>APK名称</th>
			<th>APK版本</th>
			<th>APK创建时间</th>
			<th>备注</th>
			<th>修改</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data}" var="apk">
			<tr>
				<td>${apk.type == 0 ? '随身社区APP' : apk.type == 1 ? '商户APP' : apk.type == 2 ? 'POSAPP' : '应用中心' } </td>
				<td>${apk.name }</td>
				<td>${apk.version }</td>
				<td><fmt:formatDate value="${apk.createdTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td style="word-break:break-all;width:500px"  >${apk.memo }</td>
				<td>
					<a href='${contextPath }/apk/update/${apk.id}' class='btn btn-small btn-warning'>修改</a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>
