<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="${staticPath }/css/ztree/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${staticPath }/js/ztree/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${staticPath }/js/ztree/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="${staticPath }/js/ztree/jquery.ztree.exedit-3.5.js"></script>
<title>分类管理</title>
<script type="text/javascript">
	$(function(){
		function showChild(){
			$("[name='showChild']").unbind();
			$("[name='showChild']").click(function(){
				var id = $(this).attr("val");
				var parentVal = $(this).parent().parent().parent().parent().attr("val");
				parentVal = parseInt(parentVal);
				for (var i = parentVal + 1; i < 5; i++){
					$("div[val='"+i+"']").html("");
				}
				lm.post("${contextPath}/productCategory/manager/ajax/list-by-parent",{parentId:id},function(data){
					$("div[val='"+ (parentVal + 1) + "']").html(data);
					showChild();
				});
			});
			
			$("[name='addChild']").unbind();
			$("[name='addChild']").click(function(){
				$("#addForm").find("[name='name']").val('');
				var parentId = $(this).attr("val");
				if (parentId == ""){
					$("#addModal").find(".modal-title").html("添加总分类");
				}else {
					$("#addModal").find(".modal-title").html("添加[" + $(this).attr("nameval")+"]分类");
				}
				$("[name='addBtn']").html("添加");
				$("#addForm").find("[name='parentId']").val(parentId);
				$('#addModal').modal({
				    show     : true,
				    backdrop : 'static'
				});
			});
			
			$("[name='editCategory']").unbind();
			$("[name='editCategory']").click(function(){
				$("#addModal").find(".modal-title").html("修改[" + $(this).attr("nameval")+"]");
				$("#addForm").find("[name='id']").val($(this).attr("val"));
				$("#addForm").find("[name='name']").val($(this).attr("nameval"));
				var storeId = $(this).attr("storeId");
				$("#addForm").find("[id='selectId']").find("option").each(function(key,value){
					if( storeId == $(value).val() ){
						$(value).attr("selected",true);
					}
				});
				$("[name='addBtn']").html("修改");
				$('#addModal').modal({
				    show     : true,
				    backdrop : 'static'
				});
			});
			
			$("[name='deleteCategory']").unbind();
			$("[name='deleteCategory']").click(function(){
				var element = $(this);
				var id = element.attr("val");
				lm.confirm("确定要删除吗？",function(){
					lm.post("${contextPath}/productCategory/manager/delete/"+element.attr("val"),function(data){
						if (data == "0"){
							element.parent().parent().remove();
							noty("删除成功");
							var val = null;
							$("[name='parentCategory']").each(function(k,v){
								if ($(v).attr("val") == id){
									val = $(v).parent().parent().parent().attr("val");
									val = parseInt(val);
								}
							});
							if (val != null){
								for (var i = val; i < 5; i++){
									$("div[val='"+i+"']").html("");
								}
							}
						}else {
							noty("该分类下有商品，不能删除");
						}
					});
				});
			});
		}
		
		showChild();
		
		$("[name='addBtn']").click(function(){
			var name = $("#addForm").find("[name='name']").val();
			if ($.trim(name) == ""){
				lm.alert("请输入名称");
				return false;
			}
			
			if ($.trim(name).length > 20){
				lm.alert("名称不能超过20个字");
				return false;
			}
			
			lm.post("${contextPath}/productCategory/manager/add",$("#addForm").serialize(),function(data){
				if (data == "1"){
					$("[name='closeBtn']").click();
					noty("操作成功");
					var name = $("#addForm").find("[name='name']").val();
					var operator = $("[name='addBtn']").html();
					if (operator == "添加"){
						var parentId = $("[name='parentId']").val();
						if (parentId == ""){
							//location.reload();
							window.location.href = window.location.href;
						}else {
							$("button[val='"+parentId+"']").each(function(k,v){
								if ($(v).attr("name") == "showChild"){
									$(v).click();
								}
							});
						}
					}else {
						var id = $("#addForm").find("[name='id']").val();
						$("#"+id).html(name);
					}
				}else if (data == "0"){
					lm.alert("名称已经存在");
				}
			});
		});
	});
</script>
</head>
<body>
	<table class='table table-hover table-striped table-bordered'>
			<thead>
				<tr class='text-center'>
					<th colspan="3"><div class="panel-heading"><i class="icon-list-ul"></i>分类管理<button name="addChild" val="" class="btn btn-small btn-info" style="float: right;margin-right: -10px;margin-top: -5px;"><i
					class="icon-plus"></i>添加</button></div>
					</th>
				</tr>
			</thead>
			<tbody>
				<tr class='text-center'>
					<td ><strong>名称</strong></td>
					<td ><strong>所属店铺</strong></td>
					<td ><strong>操作</strong></td>
				</tr>
				<c:forEach items="${list }" var="category">
					<tr class='text-center'>
					<td>
					<span id="${category.id }">${category.name }</span>
					</td>
						<%--
						<button name="addChild" val="${category.id }" nameval="${category.name }" class="btn btn-small btn-info" style="float: right;margin-top: -5px;margin-right: -10px;"><i
						class="icon-plus"></i>添加</button>
						--%>
					<td>	
						<span id="${category.storeId }">${category.storeName }</span>
					</td>
					<td>	
						<button name="editCategory" val="${category.id }" storeId="${category.storeId }" storeName="${category.storeName }" nameval="${category.name }" class="btn btn-small btn-warning" ><i
						class="icon-edit"></i>修改</button>
						<button name="deleteCategory" val="${category.id }" class="btn btn-small btn-danger" ><i
						class="icon-remove"></i>删除</button>
						<%-- 
						<button name="showChild" val="${category.id }" class="btn btn-small btn-info" style="float: right;margin-top: -5px;margin-right: 2px;">查看</button>
						--%>
					</td>
				</c:forEach>
			</tbody>
		</table>
	<div class='col-md-3' val="2"></div>
	<div class='col-md-3' val="3"></div>
	<div class='col-md-3' val="4"></div>
	<div class='col-md-3' val="5"></div>
	
	
</body>
</html>