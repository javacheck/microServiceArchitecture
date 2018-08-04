<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品分类列表</title>
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
				lm.post("${contextPath}/store/manager/ajax/list-by-parent",{parentId:id},function(data){
					$("div[val='"+ (parentVal + 1) + "']").html(data);
					showChild();
				});
			});
			
			$("[name='addChild']").unbind();
			$("[name='addChild']").click(function(){
				var parentId = $(this).attr("val");
				if (parentId == ""){
					$("#addModal").find(".modal-title").html("添加总分类");
				}else {
					$("#addModal").find(".modal-title").html("添加[" + $(this).attr("nameval")+"]分类");
				}
				$("[name='addBtn']").html("添加");
				$("#addForm").find("[name='parentId']").val(parentId);
				$('#addModal').modal();
			});
			
			$("[name='editstore']").unbind();
			$("[name='editstore']").click(function(){
				$("#addModal").find(".modal-title").html("修改[" + $(this).attr("nameval")+"]");
				$("#addForm").find("[name='id']").val($(this).attr("val"));
				$("#addForm").find("[name='name']").val($(this).attr("nameval"));
				$("[name='addBtn']").html("修改");
				$('#addModal').modal();
			});
			
			$("[name='deletestore']").unbind();
			$("[name='deletestore']").click(function(){
				var element = $(this);
				var id = element.attr("val");
				lm.confirm("确定要删除吗？",function(){
					lm.post("${contextPath}/store/manager/delete/"+element.attr("val"),function(data){
						element.parent().remove();
						noty("删除成功");
						var val = null;
						$("[name='parentstore']").each(function(k,v){
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
					});
				});
			});
		}
		
		showChild();
		
		$("[name='addBtn']").click(function(){
			lm.post("${contextPath}/store/manager/add",$("#addForm").serialize(),function(data){
				$("[name='closeBtn']").click();
				noty("操作成功");
				var operator = $("[name='addBtn']").html();
				if (operator == "添加"){
					var parentId = $("[name='parentId']").val();
					if (parentId == ""){
						location.reload();					
					}else {
						$("button[val='"+parentId+"']").each(function(k,v){
							if ($(v).attr("name") == "showChild"){
								$(v).click();
							}
						});
					}
				}else {
					var id = $("#addForm").find("[name='id']").val();
					$("#"+id).html($("#addForm").find("[name='name']").val());
				}
			});
		});
	});
</script>
</head>
<body>
	<div class='col-md-3' val="1">
		<div class="panel">
			<div class="panel-heading">总分类<button name="addChild" val="" class="btn btn-small btn-info" style="float: right;margin-right: -10px;margin-top: -5px;"><i
					class="icon-plus"></i>添加</button></div>
			<ul class="list-group">
				<c:forEach items="${list }" var="store">
					<li class="list-group-item"><span id="${store.id }">${store.name }</span>
						<button name="addChild" val="${store.id }" nameval="${store.name }" class="btn btn-small btn-info" style="float: right;margin-top: -5px;margin-right: -10px;"><i
						class="icon-plus"></i>添加</button>
						<button name="editstore" val="${store.id }" nameval="${store.name }" class="btn btn-small btn-info" style="float: right;margin-top: -5px;margin-right: 2px;"><i
						class="icon-edit"></i>修改</button>
						<button name="deletestore" val="${store.id }" class="btn btn-small btn-info" style="float: right;margin-top: -5px;margin-right: 2px;"><i
						class="icon-remove"></i>删除</button>
						<button name="showChild" val="${store.id }" class="btn btn-small btn-info" style="float: right;margin-top: -5px;margin-right: 2px;">查看</button>
					</li>
				</c:forEach>
			</ul>
		</div>
	</div>
	<div class='col-md-3' val="2"></div>
	<div class='col-md-3' val="3"></div>
	<div class='col-md-3' val="4"></div>
	<div class='col-md-3' val="5"></div>
	
	<div class="modal fade" id="addModal">
		<div class="modal-dialog">
		  <div class="modal-content">
		    <div class="modal-header">
		      <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
		      <h4 class="modal-title"></h4>
		    </div>
		    <div class="modal-body">
		      <form class="form-horizontal" role="form" method="post" id="addForm">
		      		<input type="hidden" name="parentId"/>
		      		<input type="hidden" name="id"/>
		          <div class="form-group">
		            <label class="col-md-2 control-label">名称</label>
		            <div class="col-md-6">
		               <input type="text" name="name" class="form-control">
		            </div>
		          </div>
		        </form>
		    </div>
		    <div class="modal-footer">
		      <button type="button" class="btn btn-default" data-dismiss="modal" name="closeBtn">关闭</button>
		      <button type="button" class="btn btn-primary" name="addBtn">添加</button>
		    </div>
		  </div>
		</div>
	</div>
</body>
</html>