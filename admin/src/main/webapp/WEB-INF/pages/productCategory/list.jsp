<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>分类列表</title>
<script type="text/javascript" src="${staticPath }/js/ztree/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${staticPath }/js/ztree/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="${staticPath }/js/ztree/jquery.ztree.exedit-3.5.js"></script>
<link rel="stylesheet" href="${staticPath }/css/ztree/zTreeStyle/zTreeStyle.css" type="text/css">
<style type="text/css">
.ztree li span.button.add {margin-left:2px; margin-right: -1px; background-position:-144px 0; vertical-align:top; *vertical-align:middle}
	</style>
<script type="text/javascript">
		var setting = {
			view: {
				addHoverDom: addHoverDom,//用于当鼠标移动到节点上时，显示用户自定义控件
				removeHoverDom: removeHoverDom,//鼠标移开的时候显示控件
				selectedMulti: false//设置是否允许同时选中多个节点。
			},
			edit: {
				enable: true,//设置 zTree 是否处于编辑状态
				editNameSelectAll: true,//设置 txt 内容是否为全选状态。
				showRemoveBtn: showRemoveBtn,//设置是否显示删除按钮
				showRenameBtn: showRenameBtn//设置是否显示编辑名称按钮
			},
			data: {
				simpleData: {
					enable: true//数据是否为简单模式
				}
			},
			callback: {
				beforeDrag: beforeDrag,//用于捕获节点被拖拽之前的事件回调函数
				beforeEditName: beforeEditName,//用于捕获节点编辑按钮的 click 事件，并且根据返回值确定是否允许进入名称编辑状态
				beforeRemove: beforeRemove,//用于捕获节点被删除之前的事件回调函数
				beforeRename: beforeRename,//用于捕获节点编辑名称结束
				onRemove: onRemove,//用于捕获删除节点之后的事件回调函数。
				onRename: onRename//用于捕获节点编辑名称结束之后的事件回调函数。
			}
		};

		var zNodes = ${empty jsonTree ? '': jsonTree};
		var log, className = "dark";
		//拖拽
		function beforeDrag(treeId, treeNodes) {
			return false;
		}
		//是否进入编辑状态
		function beforeEditName(treeId, treeNode) {
			/* 2015/8/19 张鹏程  修改为排序
			className = (className === "dark" ? "":"dark");
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			zTree.selectNode(treeNode);
			return confirm("是否修改" + treeNode.name + "的名称？");
			*/
			
			lm.post("${contextPath }/productCategory/ajax/find", {id:treeNode.id}, function(productCategory) {
				var productCategoryPId = productCategory.pId==null?'-1':productCategory.pId;
				$('#pId').val(productCategoryPId);//设置为顶级分类
				$('#name').val(productCategory.name);
				$('#id').val(productCategory.id);
				$('#sort').val(productCategory.sort);
				$("#addTreeNodesId").text("修改");
				$('#productCategoryAddModal').modal();//弹窗
				
			});
			
			
			
			return false;//永不进入修改状态
		}
		//是否删除节点
		function beforeRemove(treeId, treeNode) {
			className = (className === "dark" ? "":"dark");
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			zTree.selectNode(treeNode);
			var isDelete = false;
			if(confirm("确认删除" + treeNode.name + "分类吗？")){
				lm.postSync("${contextPath }/productCategory/manager/delete/"+treeNode.id, {}, function(data) {
					if(data == 1){
						lm.alert('此分类关联了商品，不能删除');
					} else {
						isDelete = true;
						return true;
					}
				});
			} 
			if(isDelete){
				return true;
			}
			return false;
		}
		//编辑节点名称是否合法
		function beforeRename(treeId, treeNode, newName, isCancel) {
			className = (className === "dark" ? "":"dark");
			if (newName.length == 0||newName.length>=50) {
				lm.alert("分类名称输入太长啦");
				var zTree = $.fn.zTree.getZTreeObj("treeDemo");
				setTimeout(function(){zTree.editName(treeNode)}, 10);
				return false;
			}
			var returnId ;
			
			lm.postSync("${contextPath }/productCategory/manager/add/", {id:treeNode.id,name:newName,pId:treeNode.pId,type:type}, function(data) {
				returnId=data;
			});
			if (returnId !=0) {
				return true;
			}else{
				lm.alert('名称存在');
				return false;
			}
		}
		//执行修改名称方法
		function onRename(e, treeId, treeNode, isCancel) {
			lm.noty('修改成功');
		}
		//执行删除方法
		function onRemove(e, treeId, treeNode) {
			lm.noty('删除成功');
		}
		
		//删除按钮显示判定
		function showRemoveBtn(treeId, treeNode) {
			if (!treeNode.isParent) {//不是父节点
				return getDeletePermission();
			}
			return false;
		}
		//修改按钮显示判定
		function showRenameBtn(treeId, treeNode) {
			return getEditPermission();
		}
		//添加按钮显示判定
		function showAddnameBtn(treeId, treeNode) {
			//if (treeNode.pId==null||treeNode.pId==0) {//顶级节点才能添加
			//	return true;
			//}
			return getAddPermission();
		}

		var newCount = 1;
		//用于当鼠标移动到节点上时，显示用户自定义控件，显示隐藏状态同 zTree 内部的编辑、删除按钮
		function addHoverDom(treeId, treeNode) {
			var sObj = $("#" + treeNode.tId + "_span");
			if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
			var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
				+ "' title='add node' onfocus='this.blur();'></span>";
			if (showAddnameBtn(treeId,treeNode)) {
				sObj.after(addStr);
				var btn = $("#addBtn_"+treeNode.tId);
				if (btn) btn.bind("click", function(){//按钮添加绑定事件
					$('#pId').val(treeNode.id);
					$('#name').val('');
					$("#addTreeNodesId").text("添加");
					$('#productCategoryAddModal').modal();
					treeNode1=treeNode;
					return false;
				});
			}
		};
		//鼠标移除节点
		function removeHoverDom(treeId, treeNode) {
			$("#addBtn_"+treeNode.tId).unbind().remove();
		};
		//全选
		function selectAll() {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			zTree.setting.edit.editNameSelectAll =  $("#selectAll").attr("checked");
		}
		var treeNode1;
		//添加-修改 弹出页面回调
		function addTreeNodes(){
			var name =$('#name').val();
			var pId  =$('#pId').val();
			var id   =$('#id').val();
			var sort =$('#sort').val();
			if ($.trim(sort)=="") {
				lm.alert("亲！快去输入分类顺序");
				return;
			}
			if (isNaN(sort)) {
				lm.alert("排序只能输入数字哦");
				return;
			}
			if (name.length == 0||name.length>=50) {
				lm.alert("分类名称输入太长或太短啦");
				var zTree = $.fn.zTree.getZTreeObj("treeDemo");
				setTimeout(function(){zTree.editName(treeNode)}, 10);
				return false;
			}
			lm.postSync("${contextPath }/productCategory/manager/add/", {name:name,pId:pId,type:type,storeId:storeId,id:id,sort:sort}, function(data) {
				if (data!=0) {
					var zTree = $.fn.zTree.getZTreeObj("treeDemo");
					if (pId=='-1') {treeNode1==null;}//如添加顶级节点 则 为空
					zTree.addNodes(treeNode1, {id:id, pId:pId, name:name});
				}else{
					lm.alert('已存在名称');
				}
			});
			$('#productCategoryModalCloseBtn').click();
			
			initProductCategoryTree();//刷新树
		}
		//弹出商店之后回调函数
		function callback(store){
			storeId=store.id;
			type=1;
			initProductCategoryTree();
		}
		var type = "${type}";//类型的全局变量
		var storeId = "${storeId}";//类型的全局变量
		
		//权限控制
		function getAddPermission(){//获取添加权限
			return false ;//暂时不可添加二级分类
			<m:hasPermission permissions="productCategorySysAdd" flagName="productCategorySysAdd"/>
			<m:hasPermission permissions="productCategoryAdd" flagName="productCategoryAdd"/>
			if (type == 0){//系统分类
				return ${productCategorySysAdd};
			}
			if (type == 1) {//自定义分类
				return ${productCategoryAdd};
			}
			return false;
		}
		function getEditPermission(){//获取修改权限
			<m:hasPermission permissions="productCategorySysEdit" flagName="productCategorySysEdit"/>
			<m:hasPermission permissions="productCategoryEdit" flagName="productCategoryEdit"/>
			if (type == 0){//系统分类
				return ${productCategorySysEdit};
			}
			if (type == 1) {//自定义分类
				return ${productCategoryEdit};
			}
			return false;
		}
		function getDeletePermission(){//获取删除权限
			<m:hasPermission permissions="productCategorySysDelete" flagName="productCategorySysDelete"/>
			<m:hasPermission permissions="productCategoryDelete" flagName="productCategoryDelete"/>
			if (type == 0){//系统分类
				return ${productCategorySysDelete};
			}
			if (type == 1) {//自定义分类
				return ${productCategoryDelete};
			}
			return false;
		}
		
		function initProductCategoryTree(){
			lm.post("${contextPath }/productCategory/ajax/list", {type:type,storeId:storeId}, function(data) {
				$.fn.zTree.init($("#treeDemo"), setting, data);
			});
		}
		
		//准备事件
		$(document).ready(function(){
			initProductCategoryTree();
			$("#AddFatherProductCategory").bind("click", function(){
				if (storeId=="") {
					lm.alert('亲！先选择商店哟');
					return;
				}
				$('#pId').val('-1');//设置为顶级分类
				$('#name').val('');
				$('#id').val('0');
				$('#sort').val('');
				$("#addTreeNodesId").text("添加");
				$('#productCategoryAddModal').modal();//弹窗
			});
			$("#productCategorySystem").bind("click", function(){
				location.href="${contextPath }/productCategory/manager?type=0";
			});
			$("#productCategoryCustom").bind("click", function(){//店铺选择弹窗
				$("#productCategoryShowStore").modal();
			});
			$("#myproductCategoryCustom").bind("click", function(){//查看店铺
				location.href="${contextPath }/productCategory/manager?type=1";
			});
			$("#selectAll").bind("click", selectAll);
		});
	</script>
</head>
<body>
	<div class="panel">
        <div class="panel-heading">
        <strong><i class="icon-list-ul"></i>${title } 商品分类范围</strong>
       </div>
        <div class="panel-body">
        	<!--  
        	<m:hasPermission permissions="productCategorySysList">
	       		<input id = "productCategorySystem" class="btn btn-info" type="button" value="查看系统分类">
				&nbsp;&nbsp;&nbsp;&nbsp;
			</m:hasPermission>
			-->
			<m:hasPermission permissions="productCategoryList">
				<m:acountType isTrue="false" accountType="store"><!-- 不是商家的时候显示 -->
				<input id = "productCategoryCustom"  class="btn btn-info" type="button" value="选择商家">
				&nbsp;&nbsp;&nbsp;&nbsp;
				</m:acountType>
				
				<m:acountType  accountType="store"><!-- 是商家的时候显示 -->
				<!--
				<input id = "myproductCategoryCustom"  class="btn btn-info" type="button" value="查看店铺分类">
				&nbsp;&nbsp;&nbsp;&nbsp;
				-->
				</m:acountType>
			</m:hasPermission>
			<m:hasPermission permissions="${type==0?'productCategorySysAdd':'productCategoryAdd'}">
				<input id = "AddFatherProductCategory" class="btn btn-info" type="button" value="新增一级分类">
			</m:hasPermission>
			
		</div>
      </div>
      
       <div class="panel">
        <div class="panel-heading">
        <strong><i class="icon-list-ul"></i>${title }<span ></span> 分类</strong>
       </div>
        <div class="panel-body">
        	<ul id="treeDemo" class="ztree"></ul>
		</div>
      </div>
      
      <!-- 模态窗 -->
      <div class="modal fade" id="productCategoryAddModal">
		<div class="modal-dialog">
		  <div class="modal-content">
		    <div class="modal-header">
		      <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
		      <h4 class="modal-title"></h4>
		    </div>
		    <div class="modal-body">
		      <form class="form-horizontal" role="form" method="post" id="addForm">
		      		<input type="hidden" id = "pId" name="productCategoryId"/>
		      		<input type="hidden" id = "id"; name="id" value="" />
		          <div class="form-group">
		            <label class="col-xs-2 control-label">名称</label>
		            <div class="col-xs-6">
		               <input maxlength="50" type="text" name="name" id="name" class="form-control">
		            </div>
		            <label class="col-xs-1 control-label" style="margin-left: -40px;color: red">*</label>
		          </div>
		           <div class="form-group">
		            <label class="col-xs-2 control-label">排序</label>
		            <div class="col-xs-6">
		               <input maxlength="5" type="text" name="sort" id="sort" class="form-control">
		            </div>
		            <label class="col-xs-1 control-label" style="margin-left: -40px;color: red">*</label>
		          </div>
		        </form>
		    </div>
		    <div class="modal-footer">
		      <button type="button" id = "productCategoryModalCloseBtn" class="btn btn-default" data-dismiss="modal" name="closeBtn">关闭</button>
		      <button type="button" class="btn btn-primary" id = "addTreeNodesId" onclick = "addTreeNodes()" name="addBtn">添加</button>
		    </div>
		  </div>
		</div>
	</div>
	<!-- 店铺弹窗标签 -->
	<m:select_store path="${contextPath}/shop/showModle/list/list-data" modeId="productCategoryShowStore" callback="callback"> </m:select_store>
</body>
</html>