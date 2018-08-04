<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>商品基础设置</title>
	
	<!-- ztree.core核心包 -->
	<script type="text/javascript" src="${staticPath }/js/ztree/jquery.ztree.core-3.5.js"></script>
	<!-- 升级树控件 -->
	<script type="text/javascript" src="${staticPath }/js/ztree/jquery.ztree.exhide-3.5.js"></script>
	<!-- 树形样式 -->
	<link rel="stylesheet" href="${staticPath }/css/ztree/zTreeStyle/zTreeStyle.css" type="text/css">
	
	
<script type="text/javascript">
/**
 * 定义全局树的句柄
 */
	var treeObj;
/**
 * ztree的参数配置，setting主要是设置一些tree的属性，是本地数据源，还是远程，动画效果，是否含有复选框等等
 **/ 
	var setting = { 
			// 异步加载
			async: {    
		        enable: true,  // 开启 异步加载模式
		        type:'post',  // 异步加载采用 post方法请求
		        autoParam:["id"], // 异步加载时需要自动提交父节点属性的参数
		        url: getAsyncUrl // 设置异步获取节点的 URL 为 function 动态获取
		    },
		    view: {     
		    	  fontCss: getFontCss, 
		          selectedMulti: false, // 设置是否允许同时选中多个节点(false 表示 不支持 同时选中多个节点)
				  dblClickExpand: false, // 双击节点时，是否自动展开父节点的标识 (false 表示双击节点 不切换 展开状态)  
				  expandSpeed: "fast"  // zTree 节点展开、折叠时的动画速度,IE6 下会自动关闭动画效果，以保证 zTree 的操作速度  
			},                            
			data: {
				keep: {
					parent: true // true表示 锁定 父节点属性(如果设置为 true，则所有 isParent = true 的节点，即使该节点的子节点被全部删除或移走，依旧保持父节点状态。)
				},
				key: {
					checked: "isChecked", // zTree 节点数据中保存 check 状态的属性名称。
					children: "children", // zTree 节点数据中保存子节点数据的属性名称。
					name: "name", // zTree 节点数据保存节点名称的属性名称。
					url: "url" // zTree 节点数据保存节点链接的目标 URL 的属性名称。
				},
			    //简单的数据源，一般开发中都是从数据库里读取，API有介绍，这里只是本地的                           
			    simpleData: {   
				   enable: true,  /** 确定 zTree 初始化时的节点数据、异步加载时的节点数据、或 addNodes 方法中输入的 newNodes 数据是否采用简单数据模式 (Array)
				   					    不需要用户再把数据库中取出的 List 强行转换为复杂的 JSON 嵌套格式
				   				   **/
				   idKey: "id",  // 节点数据中保存唯一标识的属性名称。  
				   pIdKey: "pId", // 节点数据中保存其父节点唯一标识的属性名称。 
				   isCurrentStoreData: "isCurrentStoreData",
				   rootPId: null  // 用于修正根节点父节点数据，即 pIdKey 指定的属性值。
				}                            
			},                           
			callback: {     /** 回调函数的设置  **/  
				  beforeClick: beforeClick,
				  onDblClick: zTreeOnDblClick,
				  onAsyncSuccess: zTreeOnAsyncSuccess
			}  
	}; 
	
/**
 * 异步记载成功时执行的方法
 **/
	function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
		//searchCategoryName(true);
	};
	
/**
 * 树被双击时触发的方法
 **/
	function zTreeOnDblClick(event, treeId, treeNode) {
	    //alert(treeNode ? treeNode.tId + ", " + treeNode.name : "isRoot");
	    //location.href ="${contextPath }/shop/list?storeId$="+treeNode.storeId;
	};
	
/** 
 * 动态设置异步加载的URL 
 **/
	function getAsyncUrl(treeId, treeNode) {
		var storeId = $("#productCategory_storeId").val();
		storeId = $.trim(storeId);
	    return "${contextPath }/commodityCategory/ajax/loadZtreeList?storeId=" + storeId;
	};
	
/**
 * 树节点点击之前触发的方法
 **/
	function beforeClick(treeId, treeNode) { 
		
	} 
	
/**
 * 树型(颜色变化)
 **/
	function getFontCss(treeId, treeNode) {
		return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
	}
	
/**
 * 树过滤条件
 **/
	function filter(node) {
	    return node.highlight == true ;
	}
	
/** 
 * 初始化树  
 **/
	$(document).ready(function(){
		 $.fn.zTree.init($("#commodityCategoryTree"), setting);
	});
	
/**
 * 全局赋值
 **/
	$(function(){
		treeObj = $.fn.zTree.getZTreeObj("commodityCategoryTree");
	});
	
/**
 * 修改树节点
 **/
	function updateNodes(highlight,nodeList) {
		for( var i=0, l=nodeList.length; i<l; i++) {
			nodeList[i].highlight = highlight;
			treeObj.updateNode(nodeList[i]);
			treeObj.expandNode(nodeList[i].getParentNode(), true, false, true);
		}
	}

/**
 * 在树型中检索搜索内容
 */
	function searchCategoryName(notSearch){
		
		// 得到搜素条件
		var categoryName = $("#categoryName").val();
		categoryName = $.trim(categoryName);
		
		// 不是搜索时,则调用此方法可能是跳转时调用,则获取存储的之前搜索的值
		var addOrUpdateName = null;
		if(notSearch){
			addOrUpdateName = $("#addOrUpdateName").val();
			addOrUpdateName = $.trim(addOrUpdateName);			
		}
		
		nodes = treeObj.getNodesByFilter(filter);
		for( var i = 0 ; i < nodes.length ; i++ ){
			nodes[i].highlight = false; 
			treeObj.updateNode(nodes[i]);
		}
		
		$("#emptyResult").text("");
		
		if( null != categoryName && "" != categoryName ){
			var nodes = treeObj.getNodesByParamFuzzy("name", categoryName, null);
			if( nodes.length > 0 ){
				updateNodes(true,nodes);				
			} else {
				$("#emptyResult").text("未查询到数据");
			}
		} else if( null != addOrUpdateName && "" != addOrUpdateName ){
			var nodes = treeObj.getNodesByParamFuzzy("name", addOrUpdateName, null);
			if( nodes.length > 0 ){
				updateNodes(true,nodes);				
			} 
		}
		if( null == categoryName ){
			categoryName = "";
		}
		var href = "${contextPath }/commodityCategory/add?categoryName="+categoryName;
		$("#commodityCategory_add_click").attr("href",href);
	}


/**
 * 搜索、重置、修改
 **/
	$(function(){
			// 搜索
			$("#searchButtonId").click(function(){
				// 此行代码会影响搜索时的判断,故去掉(2016.07.07) 
				//$.fn.zTree.init($("#commodityCategoryTree"), setting);
				 
				 searchCategoryName(false);
			});
			
			// 重置
			$("#resetButton").click(function(){
				
				   $("#categoryName").val("");
				   $("#emptyResult").text("");
				   
				   formReset();
				   
				   // 过滤符合条件的树节点
				   nodes = treeObj.getNodesByFilter(filter);
				   for( var i = 0 ; i < nodes.length ; i++ ){
						nodes[i].highlight = false; 
						treeObj.updateNode(nodes[i]);
				   }
				   $("#commodityCategory_add_click").attr("href","${contextPath }/commodityCategory/add");
			});
			
			// 修改
			$("#commodityCategory_update_click").click(function(){
				   var nodes = treeObj.getSelectedNodes();
				
				   if( null == nodes || nodes.length <= 0 ){
					   lm.alert("请先选择需要修改的分类名称");
					   return ;
				   }
				   
				   if( nodes[0].isCurrentStoreData == false ){
					   lm.alert("您只能修改属于当前登录帐号的商品分类!");
					   return ;
				   }
				   
				   var categoryName = $("#categoryName").val();
				   categoryName = $.trim(categoryName);
				   if( null == categoryName ){
					   categoryName = "";
				   }
				   window.location.href="${contextPath }/commodityCategory/update?categoryName="+categoryName+"&id="+nodes[0].id+"&isParent="+nodes[0].isParent; 
			});
			
			// 删除
			$("#commodityCategory_delete_click").click(function(){
				 var nodes = treeObj.getSelectedNodes();
					
				   if( null == nodes || nodes.length <= 0 ){
					   lm.alert("请先选择需要删除的分类名称");
					   return ;
				   }
				   
				   if( nodes[0].isCurrentStoreData == false ){
					   lm.alert("您只能删除属于当前登录帐号的商品分类!");
					   return ;
				   }
				   
				   lm.confirm("确定要删除吗？",function(){
						lm.post("${contextPath }/commodityCategory/delete/deleteById",{id:nodes[0].id},function(data){
							if(data != 0){
								lm.alert("删除成功！");
								$.fn.zTree.init($("#commodityCategoryTree"), setting);
							} else {
								lm.alert("此商品分类已经用于商品中,不能删除！");
							}
						});
					});
			});
	});
	
/**
 * 全部展开、全部折叠、提示信息
 **/
	$(function(){
			// 全部展开
			$("#expandAll").click(function(){
				 treeObj.expandAll(true);
			});
			
			// 全部折叠
			$("#foldAll").click(function(){
				  treeObj.expandAll(false);
			});
			
			// 提示信息
			$("#readMetip").mouseover(function(){
				  layer.tips('收银端按照第一级分类显示商品列表。', this, {
				      tips: [2, '#78BA32']
				  });
			});
	});
	
	
	function callback(obj){
		$("#productCategory_storeName").val(obj.name);
		$("#productCategory_storeId").val(obj.id);
		$.fn.zTree.init($("#commodityCategoryTree"), setting);
	}
	
	function formReset(){
		var productCategoryStoreName = $("#productCategory_storeName").val();
		productCategoryStoreName = $.trim(productCategoryStoreName);
		if( null != productCategoryStoreName && "" != productCategoryStoreName ){
			$("#productCategory_storeName").val("");
			$("#productCategory_storeId").val("");
			$.fn.zTree.init($("#commodityCategoryTree"), setting);				
		}
	}
	
	$(function(){
		$("#productCategory_storeName").click(function (){
			$("#productCategoryShowStore").modal();
		});
		
		$("#cancelSelect").click(function(){
			var productCategoryStoreName = $("#productCategory_storeName").val();
			productCategoryStoreName = $.trim(productCategoryStoreName);
			if( null != productCategoryStoreName && "" != productCategoryStoreName ){
				$("#productCategory_storeName").val("");
				$("#productCategory_storeId").val("");
				$.fn.zTree.init($("#commodityCategoryTree"), setting);				
			}
		});
	});
	

</script>

</head>

<body>
				            							
<!-- 商品基础设置DIV   start -->
	<div class="panel">
        <div class="panel-heading">
        	<strong><i class="icon-list-ul" id="icon-list-ul"></i>商品分类列表</strong>&nbsp;&nbsp;&nbsp;&nbsp;
        </div>
        
        <div class="panel-body">
			 <div class="input-group" style="max-width: 800px;">
			 	<c:if test="${loginIdentityMarking == 0 || loginIdentityMarking == 1 }">
			 		<span class="input-group-addon" >商家</span>
						<input <c:if test="${loginIdentityMarking ==  1 }"> value='${store_session_key.name}' </c:if> name="storeName" id="productCategory_storeName" readOnly="readOnly" placeholder="请选择商家" style="width: 200px;" class="form-control" />
						<input type="hidden" name="storeId" id="productCategory_storeId" value="" />
				</c:if>
					<span class="input-group-addon">分类名称</span>
		            	<input type="text" id="categoryName" name="categoryName" value="${categoryName }" class="form-control" placeholder="分类名称" style="width: 200px;">
		            	<input type="hidden" id="addOrUpdateName" name="addOrUpdateName" value="${addOrUpdateName }" class="form-control" style="width: 200px;">
			</div>
			
			<div id="_search_condition_2016_05_26" style="margin-top: 5px;">
				<m:hasPermission permissions="commodityCategoryAdd">
					<c:if test="${loginIdentityMarking == 1 || loginIdentityMarking == 2 }">
		          		<a href="${contextPath }/commodityCategory/add" class="btn btn-primary" id="commodityCategory_add_click" name="commodityCategory_add_click" style="margin-top: 5px;">
		          			<i class="icon-plus"></i>新增分类
		          		</a>					
					</c:if>
          		</m:hasPermission>
          		
          		<m:hasPermission permissions="commodityCategoryEdit">
          			<c:if test="${loginIdentityMarking == 1 || loginIdentityMarking == 2 }">
		          		<a href="javascript:void(0)" class="btn btn-small btn-warning" id="commodityCategory_update_click" name="commodityCategory_update_click" style="margin-top: 5px;">
		          			<i class="icon-plus"></i>修改
		          		</a>
	          		</c:if>
          		</m:hasPermission>
          		
          		<m:hasPermission permissions="commodityCategoryDelete">
          			<c:if test="${loginIdentityMarking == 1 || loginIdentityMarking == 2 }">
		          		<a href="javascript:void(0)" class="btn btn-small btn-warning" id="commodityCategory_delete_click" name="commodityCategory_delete_click" style="margin-top: 5px;">
		          			<i class="icon-plus"></i>删除
		          		</a>
	          		</c:if>
          		</m:hasPermission>
          		
				<button id="searchButtonId" type="button" name="searchButtonId" class="btn btn-info" style="margin-top: 5px;" >
					<i class="icon icon-search"></i>搜索
				</button>
				
				<button type="button" name="resetButton" id="resetButton" class="btn btn-success" style="margin-top: 5px;" >
					<i class="icon icon-undo"></i>重置
				</button>
		  </div>
	   </div>
   </div>
<!-- 商品基础设置DIV   end -->  
   
   
<!-- 商品分类列表DIV   start --> 
    <div class="panel">
        <div class="panel-heading">
          	 <input id="expandAll" name="expandAll" class="btn btn-info" type="button" value="全部展开">
      		 <input id="foldAll" name="foldAll" class="btn btn-info" type="button" value="全部折叠">
      		 <input id="readMetip" name="readMetip" class="btn btn-info" type="button" value="?">
      	</div>
      	  
      	<div class="panel-body">
        	 <ul id="commodityCategoryTree" class="ztree"></ul>
		</div>
   </div>
<!-- 商品分类列表DIV   end -->  
   
   <m:select_store path="${contextPath}/commodityCategory/showModel/list/list-data" modeId="productCategoryShowStore" callback="callback"> </m:select_store>
</body>

</html>