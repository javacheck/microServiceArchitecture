<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }" >
	<thead>
		<tr> 
			<th>供应商名称</th>
			<th>联系人</th>
			<th>电话</th>
			<th>地址</th>
			<th>备注</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="supplier">
			<tr>
				
				<td>${supplier.name }</td>
				<td>${supplier.contacts }</td>
				<td>${supplier.phone }</td>
				<td>${supplier.address }</td>
				<td>${supplier.memo }</td>
				<td>
					<m:hasPermission permissions="supplierEdit">
						<a href="${contextPath }/supplier/update/${supplier.id}" class="btn btn-small btn-warning">修改</a> 
					</m:hasPermission>
					<m:hasPermission permissions="supplierDelete">
						<a  onclick="deletesupplier(${supplier.id});" id="deletebutton"class="btn btn-small btn-danger">删除</a> 
					</m:hasPermission>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>