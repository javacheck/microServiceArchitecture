<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }" >
	<thead>
		<tr> 
			<th>商家名称</th>
			<th>打印机名称</th>
			<th>打印机编号</th>
			<th>打印机KEY</th>
			<th>打印分类</th>
			<th>状态</th>
			<th>备注</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="print">
			<tr>
				<td>${print.storeName }</td>
				<td>${print.printName }</td>
				<td>${print.printSn }</td>
				<td>${print.printKey }</td>
				<td>${print.categoryNames }</td>
				<td>
					<c:if test="${print.status==0 }">已关闭</c:if>
					<c:if test="${print.status==1 }">已开启</c:if>
				</td>
				<td>${print.memo }</td>
				<td>
					<c:if test="${print.status==0 }"><a  onclick="typeChage(${print.id},1);" class="btn btn-primary">开启</a></c:if>
					<c:if test="${print.status==1 }"><a  onclick="typeChage(${print.id},0);" class="btn btn-primary">关闭</a></c:if>
					
					<m:hasPermission permissions="printEdit">
						<a href="${contextPath }/print/update/${print.id}" class="btn btn-small btn-warning">修改</a> 
					</m:hasPermission>
					<m:hasPermission permissions="printDelete">
						<a  onclick="deletePrint(${print.id});" id="deletebutton"class="btn btn-small btn-danger">删除</a> 
					</m:hasPermission>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>