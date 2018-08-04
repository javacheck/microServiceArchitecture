<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Cache-Control" content="no-transform">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<style>
.liclass{width:200px;float:left;display:block;}
</style>
<script type="text/javascript">
	$(function(){
		var nowRoles = ${permissionIds};//拥有权限
		$("input[name='permissionIds']").each(function() {//初始化选中
			var thisVal=$(this).val();
			for (var i=0;i<nowRoles.length;i++){
				if(thisVal==nowRoles[i]){
					this.checked=true;
				}
			}
		}) //循环事件结束
		$("input[name='categoryNames']").each(function (){
			var thisId=$(this).attr("id");
			var checked = true;
			$("input[items='"+thisId+"']").each(function() {
				if (this.checked==false) {	checked=false;}
			}) //循环事件结束
			$(this).prop("checked",checked); 
		});
		
		var status = "${status}";
		if (status=='select') {//如果为查看权限
			$('#AllSelected').hide();//隐藏按钮
			$('#ChangeSelected').hide();//隐藏按钮
			$('#accountAddBtn').hide();//隐藏按钮
			$("input[type='checkbox']").css('display','none')//隐藏所有checkBox
			$(".liclass").css('display', 'none');//隐藏所有li
			$(".liclass").each(function() {//显示拥有权限
				for (var i=0;i<nowRoles.length;i++){
					if($(this).attr("items")==nowRoles[i]){
						$(this).css('display', '');
					}
				}
			}) //循环事件结束
		}
		
		//
		$("input[name='permissionIds']").click(function (){
			var items=$(this).attr("items");
			var checked;
			if (this.checked==true) {
				checked=true;
				$("input[items='"+items+"']").each(function() {
					checked=this.checked==false?false:checked;
				}) //循环事件结束
			}else{checked=false;}
			
			if(checked){
				$("#"+items).prop("checked",true); 
			}else{
				$("#"+items).prop("checked",false); 
			}
			
		});
		
		//分类点击事件
		$("input[name='categoryNames']").click(function (){
			var thisId=$(this).attr("id");
			var checked = this.checked;
			$("input[items='"+thisId+"']").each(function() {
				this.checked=checked;
			}) //循环事件结束
		});
		
		
		//选中所有check
		$("#AllSelected").click(function (){
			$("input[name='permissionIds']").each(function() {
				this.checked=true;
			}) //循环事件结束
			$("input[name='categoryNames']").each(function() {
				this.checked=true;
			}) //循环事件结束
		});
		//清除状态
		$("#ChangeSelected").click(function (){
			$("input[name='permissionIds']").each(function() {
				this.checked=false;
			}) //循环事件结束
			$("input[name='categoryNames']").each(function() {
				this.checked=false;
			}) //循环事件结束
		});
		
	});//准备事件结束
</script>
<title>角色列表</title>
</head>
<body>
		<div class='panel'>
		<div class='panel-heading'>
		<strong><i class='icon-plust'></i>对角色&nbsp;&nbsp;“${role.name}”&nbsp;&nbsp;${status=='select'?'查看权限':'授权'}</strong>
		</div>
		<div class='panel-body'>
			<form id="roleAddForm" method='post' class='form-horizontal'
				action="${contextPath }/role/permissionAdd">
				
				<input name="roleId" type="hidden" value="${role.id }" />

				<table class='table table-hover table-striped table-bordered'>
					<thead>
						<tr class='text-center'>
							<th>分类</th>
							<th>权限</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${permissions }" var="list">
							<c:if test="${ not empty list.value}">
							<tr align="left" >
								<td>
								<input id = "${list.key}" type="checkbox"  name="categoryNames">
								${list.key }
								</td>
								<td>
								<ul>
								<c:forEach items="${list.value}" var="permission">
									<li items="${permission.id}"  class="liclass">
										<input items="${list.key}" type="checkbox" value="${permission.id}" name="permissionIds">&nbsp;&nbsp;
									${permission.desc}
									</li>
								</c:forEach>
								</ul>
								</td>
							</tr>
							</c:if>
						</c:forEach>
					</tbody>

				</table>

				
				<div class="form-group">
					<div class="col-md-offset-2 col-md-10">
						<input type="button"  id='AllSelected' class='btn btn-small btn-info'	value="全选" />
						<input type="button"  id='ChangeSelected' class='btn btn-small btn-warning'value="清除"  />
						<input type="submit"  id='accountAddBtn' class='btn btn-primary'value="保存" data-loading='稍候...' />
					</div>
				</div>
			</form>
		</div>
	</div>
	
</body>
</html>