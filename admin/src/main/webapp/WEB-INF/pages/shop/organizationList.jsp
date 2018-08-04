<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>组织结构管理</title>
	
	<!-- ztree.core核心包 -->
	<script type="text/javascript" src="${staticPath }/js/ztree/jquery.ztree.core-3.5.js"></script>
	<!-- 升级树控件 -->
	<script type="text/javascript" src="${staticPath }/js/ztree/jquery.ztree.exhide-3.5.js"></script>
	<!-- 树形样式 -->
	<link rel="stylesheet" href="${staticPath }/css/ztree/zTreeStyle/zTreeStyle.css" type="text/css">
	
	
	
<script type="text/javascript">
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
				   pIdKey: "parentId", // 节点数据中保存其父节点唯一标识的属性名称。 
				   rootPId: null  // 用于修正根节点父节点数据，即 pIdKey 指定的属性值。
				}                            
			},                           
			callback: {     /** 回调函数的设置 **/  
				  beforeClick: beforeClick,
				  onAsyncSuccess: zTreeOnAsyncSuccess
			}  
	}; 
		
	function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
		searchOrganizationName(true);
	};
	
	/** 动态设置异步加载的URL **/
	function getAsyncUrl(treeId, treeNode) {
	    return "${contextPath }/organization/ajax/loadZtreeList";
	};
	
	function beforeClick(treeId, treeNode) {  
		 //alert("beforeClick");  
	} 
		
	/** 初始化树  **/
	$(document).ready(function(){
		 $.fn.zTree.init($("#organizationTree"), setting);
	});
		
	function searchOrganizationName(notSearch){
		
		var organizationName = $("#organizationName").val();
		organizationName = $.trim(organizationName);
		
		var treeObj = $.fn.zTree.getZTreeObj("organizationTree");
		nodes = treeObj.getNodesByFilter(filter);
		for( var i = 0 ; i < nodes.length ; i++ ){
			nodes[i].highlight = false; 
			treeObj.updateNode(nodes[i]);
		}
		$("#emptyResult").text("");
		if( null != organizationName && "" != organizationName ){
			var nodes = treeObj.getNodesByParamFuzzy("name", organizationName, null);
			if( nodes.length > 0 ){
				updateNodes(true,nodes);				
			} else {
				$("#emptyResult").text("未查询到数据");
			}
		} 
	}
	$(document).ready(function(){
		$("#searchButtonId").click(function(){
			searchOrganizationName(false);
		});
		
		$("#resetButton").click(function(){
			$("#organizationName").val("");
			$("#emptyResult").text("");
			var treeObj = $.fn.zTree.getZTreeObj("organizationTree");
			nodes = treeObj.getNodesByFilter(filter);
			for( var i = 0 ; i < nodes.length ; i++ ){
				nodes[i].highlight = false; 
				treeObj.updateNode(nodes[i]);
			}
		});
		
	});
	function updateNodes(highlight,nodeList) {
		var zTree = $.fn.zTree.getZTreeObj("organizationTree");
		for( var i=0, l=nodeList.length; i<l; i++) {
			nodeList[i].highlight = highlight;
			zTree.updateNode(nodeList[i]);
			zTree.expandNode(nodeList[i].getParentNode(), true, false, true);
		}
	}
		
	function getFontCss(treeId, treeNode) {
		return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
	}
	
	function filter(node) {
	    return node.highlight == true ;
	}
	$(document).ready(function(){
		var treeObj = $.fn.zTree.getZTreeObj("organizationTree");
		$("#expandAll").click(function(){
			treeObj.expandAll(true);
		});
		
		$("#foldAll").click(function(){
			treeObj.expandAll(false);
		});
		
		$("#suerButtonId").click(function(){
			var nodes = treeObj.getSelectedNodes();
			if( null == nodes || nodes.length <= 0 ){
				lm.alert("请先选择组织架构节点");
				return ;
			}
			$("#mainShopName").val(nodes[0].name);
			$("#organizationId").val(nodes[0].id); 
			$("#organizationTreeModalBtn").click();
		});
	});

</script>

</head>

<body>
				            							
	<!-- 组织结构管理DIV   start -->
	<div class="panel">
        <div class="panel-heading">
        	<strong><i class="icon-list-ul"></i>组织结构管理</strong>
        </div>
	        <button type="button" class="close" data-dismiss="modal" id="organizationTreeModalBtn">
			</button>
        <div class="panel-body">
			 <div class="input-group" style="max-width: 600px;">
				<span class="input-group-addon">组织名称</span>
	            	<input type="text" id="organizationName" name="organizationName" value="${organizationName }" class="form-control" placeholder="组织名称" style="width: 200px;">
	            	<input type="hidden" id="addOrUpdateName" name="addOrUpdateName" value="${addOrUpdateName }" class="form-control" style="width: 200px;">
	            	&nbsp;&nbsp;
	            	<label class="col-md-0 control-label" style="color: red;font-size: 15px" id = "emptyResult" ></label>
			</div>
			
			<div id="_search_condition_2016" style="margin-top: 5px;">
				<button id="suerButtonId" type="button" name="suerButtonId" class="btn btn-primary" style="margin-top: 5px;" >
					<i class="icon-plus"></i>确定
				</button>
				<button id="searchButtonId" type="button" name="searchButtonId" class="btn btn-info" style="margin-top: 5px;" >
					<i class="icon icon-search"></i>搜索
				</button>
				<button type="button" name="resetButton" id="resetButton" class="btn btn-success" style="margin-top: 5px;" >
					<i class="icon icon-undo"></i>重置
				</button>
		  </div>
	   </div>
   </div>
   <!-- 组织结构管理DIV   end -->  
   
   <!-- 组织结构列表DIV   start --> 
    <div class="panel">
          <div class="panel-heading">
          	 <strong><i class="icon-list-ul"></i>组织结构列表</strong>
          	 &nbsp;&nbsp;&nbsp;&nbsp;
          	 <input id="expandAll" name="expandAll" class="btn btn-info" type="button" value="全部展开">
      		 <input id="foldAll" name="foldAll" class="btn btn-info" type="button" value="全部折叠">
      	  </div>
      	 <div class="panel-body">
      		
        	<ul id="organizationTree" class="ztree"></ul>
		 </div>
   </div>
   <!-- 组织结构列表DIV   end -->  
   
</body>

</html>