<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Cache-Control" content="no-transform">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>后台管理 - <decora:title /></title>

<link rel="stylesheet" href="${staticPath }/css/datetimepicker.min.css">
<link rel="stylesheet" href="${staticPath }/css/zui.min.css">
<style>
.user-control-nav {
	margin-bottom: 20px;
}

@media ( max-width : 480px) {
	.hidden-xxs {
		display: none
	}
	.page {
		font-size: 12px
	}
}

@media ( max-width : 400px) {
	.hidden-xxxs {
		display: none
	}
}

.panel {
	margin: 0 15px 0 0;
}

.panel-actions>form {
	max-width: 400px;
}

.panel-actions>form .input-group {
	margin-top: 3px;
}

.panel-actions>form .input-group .btn {
	margin: 0;
}
</style>
<script type="text/javascript">
	var config = {}
</script>
<script src="${staticPath }/js/jquery-1.11.2.min.js"></script>
<script src="${staticPath }/js/zui.min.js"></script>
<!--[if lt IE 9]>
<script src='${staticPath }/js/all.ie8.js' type='text/javascript'></script>
<![endif]-->
<!--[if lt IE 10]>
<script src='${staticPath }/js/all.ie9.js' type='text/javascript'></script>
<![endif]-->
<script type="text/javascript" src="${staticPath }/js/common.js"></script>
<script type="text/javascript" src="${staticPath }/js/jquery.pager.js"></script>
<script type="text/javascript"
	src="${staticPath }/js/datetimepicker.min.js"></script>
<script type="text/javascript" src="${staticPath }/js/list_page.js"></script>
<script type="text/javascript">
	$(function() {
		var submenu = null;
		$("a[name='main-submenu']").each(function(i, v) {
			var href = $(v).attr("href");
			if (location.href.indexOf(href) != -1) {
				submenu = $(v);
				submenu.parent().addClass("active");
			}
		});

		if (submenu != null) {
			submenu.parent().parent().parent().find("a").click();
		}
	});
</script>
<decora:head />
</head>
<body>
	<nav class='navbar navbar-inverse navbar-fixed-top' role='navigation'
		id='mainNavbar'>
	<div class='collapse navbar-collapse navbar-ex1-collapse'>
		<ul class="nav navbar-nav navbar-right">
			<li style="margin-top: 10px;color: white;">欢迎您：${account_session_key.mobile }</li>
			<li class="dropdown"><a href="#" class="dropdown-toggle"
				data-toggle="dropdown"><i class="icon-user icon-large"></i>
					${loginName_session_key } <b class="caret"></b></a>
				<ul class="dropdown-menu">
					<li><a href='#updatePasswordModal' data-toggle='modal'>修改密码</a></li>
					<li><a href='${contextPath }/logout'>退出</a></li>
				</ul></li>
		</ul>
	</div>
	</nav>


	<div class="clearfix row-main" style="margin-top: 60px;">
		<div class='col-md-1'>
			<div class="leftmenu affix hiddden-xs hidden-sm" id="main-leftmenu">
				<nav class="menu" data-toggle="menu" style="width: 120px;">
				<ul class="nav nav-primary">
					<%-- 
					<c:forEach items="${menu_session_key }" var="resMap">
						<li><a href='#'><i class="icon-th"></i><b>${resMap.key }</b></a>
							<ul class='nav'>
								<c:forEach items="${resMap.value }" var="res">
									<li><a name="main-submenu"
										href="${contextPath }${fn:substring(res.get('url'),0,fn:length(res.get('url'))-2) }">${res.get('name') }
									</a></li>
								</c:forEach>
							</ul></li>
					</c:forEach>
					--%>
					<li><a href='#'><i class="icon-key"></i><b>帐号管理</b></a>
						<ul class='nav'>
							<m:hasPermission permissions="accountList">
								<li><a name="main-submenu" href="${contextPath }/account">帐号列表
								</a></li>
							</m:hasPermission>

							<m:hasPermission permissions="roleList">
								<li><a name="main-submenu" href="${contextPath }/role">角色列表
								</a></li>
							</m:hasPermission>
						</ul></li>
					<li><a href='#'><i class="icon-user"></i><b>会员管理</b></a>
						<ul class='nav'>
							<m:hasPermission permissions="userList">
								<li><a name="main-submenu" href="${contextPath }/user">会员列表
								</a></li>
							</m:hasPermission>
							<m:hasPermission permissions="setDiscount">
								<li><a name="main-submenu"
									href="${contextPath }/discount/set">会员折扣 </a></li>
							</m:hasPermission>
						</ul></li>
					<li><a href='#'><i class="icon-th"></i><b>店铺管理</b></a>
						<ul class='nav'>
							<m:hasPermission permissions="storeList">
								<li><a name="main-submenu"
									href="${contextPath }/store/list">店铺列表 </a></li>
							</m:hasPermission>
						</ul></li>
					<li><a href='#'><i class="icon-th"></i><b>商品管理</b></a>
						<ul class='nav'>
							<m:hasPermission
								permissions="productCategoryAdd,productCategoryDelete,productCategoryEdit">
								<li><a name="main-submenu"
									href="${contextPath }/productCategory/manager">分类管理 </a></li>
							</m:hasPermission>

							<m:hasPermission permissions="productCategoryList">
								<li><a name="main-submenu"
									href="${contextPath }/productCategory/list">分类列表 </a></li>
							</m:hasPermission>

							<m:hasPermission permissions="productList">
								<li><a name="main-submenu"
									href="${contextPath }/product/list">商品列表 </a></li>
							</m:hasPermission>

							<m:hasPermission permissions="productAttributeList">
								<li><a name="main-submenu"
									href="${contextPath }/productAttribute/list">属性列表 </a></li>
							</m:hasPermission>

							<m:hasPermission permissions="productAttributeValueList">
								<li><a name="main-submenu"
									href="${contextPath }/productAttributeValue/list">属性值列表 </a></li>
							</m:hasPermission>

							<m:hasPermission permissions="productImport">
								<li><a name="main-submenu"
									href="${contextPath }/productUpload/list">商品导入 </a></li>
							</m:hasPermission>
						</ul></li>
					<li><a href='#'><i class="icon-file"></i><b>统计报表</b></a>
						<ul class='nav'>
							<m:hasPermission permissions="report">
								<li><a name="main-submenu" href="${contextPath }/report">报表
								</a></li>
							</m:hasPermission>
						</ul></li>
					<li><a href='#'><i class="icon-file"></i><b>订单管理</b></a>
						<ul class='nav'>
							<m:hasPermission permissions="orderList">
								<li><a name="main-submenu" href="${contextPath }/order">查看</a>
								</li>
							</m:hasPermission>
						</ul></li>
				</ul>
				</nav>
			</div>
		</div>
		<div class='col-md-11' id="main-content">
			<decora:body />
		</div>
	</div>

	<div class="modal fade" id="LMConfirmModal">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span><span class="sr-only">关闭</span>
					</button>
					<h4 class="modal-title" id="LMConfirmModalTitle"></h4>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal"
						id="LMConfirmCloseBtn">取消</button>
					<button type="button" class="btn btn-primary" id="LMConfirmBtn">确定</button>
				</div>
			</div>
		</div>
	</div>
	
	
	<div class="modal fade" id="updatePasswordModal">
		<div class="modal-dialog">
		  <div class="modal-content">
		    <div class="modal-header">
		      <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
		      <h4 class="modal-title"></h4>
		    </div>
		    <div class="modal-body">
		      <form class="form-horizontal" role="form" method="post" id="updatePasswordForm">
		          <div class="form-group">
		            <label class="col-md-2 control-label">旧密码</label>
		            <div class="col-md-6">
		               <input type="password" name="oldPassword" class="form-control">
		            </div>
		          </div>
		          <div class="form-group">
		            <label class="col-md-2 control-label">新密码</label>
		            <div class="col-md-6">
		               <input type="password" name="password" class="form-control">
		            </div>
		          </div>
		          <div class="form-group">
		            <label class="col-md-2 control-label">确认新密码</label>
		            <div class="col-md-6">
		               <input type="password" name="password2" class="form-control">
		            </div>
		          </div>
		        </form>
		    </div>
		    <div class="modal-footer">
		      <button type="button" class="btn btn-default" data-dismiss="modal" name="closeBtn">关闭</button>
		      <button type="button" class="btn btn-primary" id="updatePasswordBtn">修改</button>
		    </div>
		  </div>
		</div>
	</div>
	
	<script type="text/javascript">
		$(function(){
			$("#updatePasswordBtn").click(function(){
				var oldPassword = $("#updatePasswordForm").find("[name='oldPassword']").val();
				var password = $("#updatePasswordForm").find("[name='password']").val();
				var password2 = $("#updatePasswordForm").find("[name='password2']").val();
				
				if ($.trim(oldPassword) == ""){
					lm.alert("请输入旧密码");
					return;
				}
				
				if ($.trim(password) == ""){
					lm.alert("请输入新密码");
					return;
				}
				
				if (password != password2){
					lm.alert("两次输入密码不一致");
					return;
				}
				
				lm.post("${contextPath}/update-password",{password:password,oldPassword:oldPassword},function(data){
					if (data == "1"){
						$("#updatePasswordModal").find("[name='closeBtn']").click();
						lm.noty("修改成功");
					}else if (data == "2"){
						lm.alert("旧密码不对");
					}
				});
			});
		});
	</script>
</body>
</html>