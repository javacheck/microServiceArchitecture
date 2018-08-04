<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="panel">
	<div class="panel-heading">子分类</div>
	<ul class="list-group">
		<c:choose>
			<c:when test="${not empty list }">
				<c:forEach items="${list }" var="category">
					<li class="list-group-item" name="parentCategory" val="${category.parentId }"><span id="${category.id }">${category.name }</span>
						<button name="addChild" val="${category.id }" nameval="${category.name }"
							class="btn btn-small btn-info"
							style="float: right; margin-top: -5px; margin-right: -10px;">
							<i class="icon-plus"></i>添加
						</button>
						<button name="editCategory" val="${category.id }" nameval="${category.name }" class="btn btn-small btn-info" style="float: right;margin-top: -5px;margin-right: 2px;"><i
						class="icon-edit"></i>修改</button>
						<button name="deleteCategory" val="${category.id }" class="btn btn-small btn-info" style="float: right;margin-top: -5px;margin-right: 2px;"><i
						class="icon-remove"></i>删除</button>
						<button name="showChild" val="${category.id }"
							class="btn btn-small btn-info"
							style="float: right; margin-top: -5px; margin-right: 2px;">查看</button>
					</li>
				</c:forEach>
			</c:when>
			<c:otherwise>没有子分类</c:otherwise>
		</c:choose>

	</ul>
</div>