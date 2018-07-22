<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty productCategory ? '添加' : '修改' }商品分类</title>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong><i class='icon-plust'></i>${empty productCategory ? '添加' : '修改' }商品分类</strong>
		</div>
		<div class='panel-body'>
			<form method='post' class='form-horizontal'
				action="${contextPath }/productCategory/add">
				
				<c:if test="${type!='addChildren' }">
					<input name="id" type="hidden" value="${productCategory.id }" />
				</c:if>
				
				<div class="form-group">
					<label class="col-md-2 control-label">分类名称</label>
					<div class="col-md-4">
						<input type='text' id="name" name="name"
							value="${productCategory.name }" class='form-control' />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-2 control-label"> 上级分类  </label>
					<div class="col-md-4">
					
						<!-- 新增页面 -->
						<c:if test="${ type == 'add' }">
							 <select name="parentId">
								<option value="" >最高级</option>
								<c:forEach items="${list }" var="role">
									<option value ="${role.id}">${role.name}</option>
								</c:forEach>
							</select>
						</c:if>
						
						<!-- 修改页面 -->
						<c:if test="${type=='update'}">
							<select name="parentId">
								<option value="" >最高级</option>
								<c:forEach items="${list }" var="role">
									<option value ="${role.id}"
									<c:if test="${role.id == productCategory.parentId }">
										selected
									</c:if>
								>${role.name}</option>
							</c:forEach>
							</select>
						</c:if>
						
						<!-- 添加子节点页面 -->
						<c:if test="${type=='addChildren' }">
							<select disabled="disabled" name="parentId">
								<c:forEach items="${list }" var="role">
									<option value ="${role.id}"
								
									<c:if test="${role.id == productCategory.id }">
										selected
									</c:if>
								>${role.name}</option>
							</c:forEach>
							</select>
						</c:if>
					</div>
				</div>

				<div class="form-group">
					<div class="col-md-offset-2 col-md-10">
						<input type='submit' id='submit' class='btn btn-primary'
							value="${empty productCategory ? '添加' : '修改' }" data-loading='稍候...' />
						<input onclick="history.go(-1)" type="button" id='return' class='btn btn-primary'
							value="返回"  />
					</div>
				</div>
			</form>
		</div>
	</div>

</body>
</html>