<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty productCategory ? '添加' : '修改' }商品分类</title> 
	
		<!-- ztree.core核心包 -->
	<script type="text/javascript" src="${staticPath }/js/ztree/jquery.ztree.core-3.5.js"></script>
	<!-- 升级树控件 -->
	<script type="text/javascript" src="${staticPath }/js/ztree/jquery.ztree.exhide-3.5.js"></script>
	<!-- 树形样式 -->
	<link rel="stylesheet" href="${staticPath }/css/ztree/zTreeStyle/zTreeStyle.css" type="text/css">
	
<script type="text/javascript">

$(function(){
	var entObj = document.getElementById("parentName");	
	var vTop = getPosition(entObj).top + 30;
	var vLeft = getPosition(entObj).left;
	//创建一个显示选择地区树的DIV
	var treeDIV = document.createElement("DIV");	
	treeDIV.id = "commodityCategoryTree";
	treeDIV.style.top = vTop + "px";	
	treeDIV.style.left = vLeft + "px";
	treeDIV.style.position = "absolute";
	treeDIV.style.borderStyle = "outset";	//设置DIV边框样式		
	treeDIV.className = "ztree";			//设置DIV的样式
	treeDIV.style.height = "auto";		    //设置选择IDV的高度
	treeDIV.style.width = 215 + "px";		//设置选择IDV的宽度
	treeDIV.style.zIndex = "310";			//设置选择IDV的宽度
	treeDIV.style.background = "#ffffff";	//DIV背景颜色
	treeDIV.style.overflow = "scroll";		//设置DIV滚动条	

	document.body.appendChild(treeDIV);
	$("#commodityCategoryTree").hide();
	
	$("#parentName").click(function(){
		searchCategoryName();
		$("#commodityCategoryTree").show();
	});
	
	$("#cancelSelect").click(function(){
		 $("#pId").val("");
		 $("#parentName").val("");
	});
	
});

	 $(document).click(function(e){ 
		 e = window.event || e; // 兼容IE7
		 var obj = (e.srcElement || e.target);
		 
		 var id = $(obj).attr("id");
		 
		 if( undefined == id || null == id ){
			 $("#commodityCategoryTree").hide();
			 return;
		 }
		 
		 if(!estimateTreeScope(id)){
			 $("#commodityCategoryTree").hide(); 			 			 
		 }
	});
	
	function estimateTreeScope(id){
		if( id== "commodityCategoryTree" || id == "parentName" || (id+"").indexOf('commodityCategoryTree') != -1 ){
			 return true;
		} else {
			 return false;
		}
	} 
	
	$(function(){
		
		$("#productCategoryAddBtn").click(function(){
			
			var name = $("#name").val(); // 
			name = $.trim(name);
			
			if( name == "" || name == null ){
				lm.alert("分类名称不能为空!");
				return;
			}
			
			var id = $("#id").val();
			id = $.trim(id);
			var nameFlag = false;
			lm.postSync("${contextPath}/commodityCategory/list/ajax/existCategoryName",{name:name,id:id},function(data){
				if(data >= 1){
					lm.alert("此分类名称已存在！");
					nameFlag = true;
					return ;				
				}
			});
			if(nameFlag){
				return;
			}
			
			var sort = $("#sort").val();
			sort = $.trim(sort);
			
			if( sort != "" && null != sort ){
				if( !sort.match(/^[\d]+$/) ){
					lm.alert("排序只能输入数字");
					return;
				}
			}
			
			$("#productCategoryAddForm").submit();
		});
	});
	
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
				  //beforeClick: beforeClick,
				  onDblClick: zTreeOnDblClick,
				  onAsyncSuccess: zTreeOnAsyncSuccess,
				  onClick: zTreeOnClick
			}  
	}; 
	
/**
 * 异步记载成功时执行的方法
 **/
	function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
		searchCategoryName();
	};
	
/**
 * 树被双击时触发的方法
 **/
	function zTreeOnDblClick(event, treeId, treeNode) {
		var id = $("#id").val();
		id = $.trim(id);
		if( null != id && "" != id ){ // 修改时,不能选择自己做自己的上级
			if( treeNode.id == id ){
				lm.alert("不能选择自己做自己的上级分类！");
				return;
			}
			if( treeNode.level >= 3 ){
				lm.alert("最多支持四级分类哦!!!");
				return;
			}
			
			// 修改节点原来的等级
			var cacheLevel = $("#level").val();
			var isParent = '${isParent}' == 'true' ? true : false ;
			if(isParent){ // 如果其下还有子节点
				if( (cacheLevel-0) == 0 ){
					lm.alert("最多支持四级分类哦---");
					return;
				} else {
					if( ((cacheLevel-0) - (treeNode.level-0)) <= 0 ){
						lm.alert("最多支持四级分类哦---");
						return;
					}
				}
				
			} else {
				if( (cacheLevel -0) == 0  ){ // 顶级进行迁移
					if( (treeNode.level) >= 4 ){
						lm.alert("最多支持四级分类哦---");
						return;
					} 
				} else {
					console.log(( (3-(cacheLevel-0)) + ((treeNode.level-0)) ) + ">>--cacheLevel ==>> " + cacheLevel + ">>--treeNode.Level ==>> " + treeNode.level);
					// 原来的等级+新增的节点不超过4
					if( ( (3-(cacheLevel-0)) + ((treeNode.level-0)) ) > 4 ){
						lm.alert("最多支持四级分类哦---");
						return;
					} 									
				}
			}
		} else {
			if( treeNode.level >= 3 ){
				lm.alert("最多支持四级分类哦~~~");
				return;
			}
		} 
		
	    $("#pId").val(treeNode.id);
	    $("#parentName").val(treeNode.name);
	    $("#commodityCategoryTree").hide(); 
	};
	
	function zTreeOnClick(event, treeId, treeNode) {
		var id = $("#id").val();
		id = $.trim(id);
		if( null != id && "" != id ){ // 修改时,不能选择自己做自己的上级
			if( treeNode.id == id ){
				lm.alert("不能选择自己做自己的上级分类！");
				return;
			}
			if( treeNode.level >= 3 ){
				lm.alert("最多支持四级分类哦!!!");
				return;
			}
			
			// 修改节点原来的等级
			var cacheLevel = $("#level").val();
			var isParent = '${isParent}' == 'true' ? true : false ;
			if(isParent){ // 如果其下还有子节点
				if( (cacheLevel-0) == 0 ){
					lm.alert("最多支持四级分类哦---");
					return;
				} else {
					if( ((cacheLevel-0) - (treeNode.level-0)) <= 0 ){
						lm.alert("最多支持四级分类哦---");
						return;
					}
				}
				
			} else {
				if( (cacheLevel -0) == 0  ){ // 顶级进行迁移
					if( (treeNode.level) >= 4 ){
						lm.alert("最多支持四级分类哦---");
						return;
					} 
				} else {
					console.log(( (3-(cacheLevel-0)) + ((treeNode.level-0)) ) + ">>--cacheLevel ==>> " + cacheLevel + ">>--treeNode.Level ==>> " + treeNode.level);
					// 原来的等级+新增的节点不超过4
					if( ( (3-(cacheLevel-0)) + ((treeNode.level-0)) ) > 4 ){
						lm.alert("最多支持四级分类哦---");
						return;
					} 									
				}
			}
		} else {
			if( treeNode.level >= 3 ){
				lm.alert("最多支持四级分类哦~~~");
				return;
			}
		} 
		
	    $("#pId").val(treeNode.id);
	    $("#parentName").val(treeNode.name);
	    $("#commodityCategoryTree").hide(); 
	};
	
/** 
 * 动态设置异步加载的URL 
 **/
	function getAsyncUrl(treeId, treeNode) {
		var storeId = $("#productCategory_storeId").val();
		storeId = $.trim(storeId);
		var id = $("#id").val();
		id = $.trim(id);
	    return "${contextPath }/commodityCategory/ajax/loadZtreeListNoSubordinate?storeId=" + storeId + "&id=" + id;
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
	function searchCategoryName(){
		
		// 得到搜素条件
		var pId = $("#pId").val();
		pId = $.trim(pId);
		nodes = treeObj.getNodesByFilter(filter);
		for( var i = 0 ; i < nodes.length ; i++ ){
			nodes[i].highlight = false; 
			treeObj.updateNode(nodes[i]);
		}
		
		if( null != pId && "" != pId ){
			var nodes = treeObj.getNodeByParam("id", pId, null);
			if( null == nodes ){
				return;
			}
			treeObj.selectNode(nodes); // 设置选中
			var cacheArray = new Array();
			cacheArray.push(nodes);
			updateNodes(true,cacheArray);				
		}
	}


/**
 * @Method Name	: getPosition
 * @Description	: 获取节点的坐标
 * @param		: el 节点对象
 * @return		: 节点的坐标
 */	
function getPosition(el){
	var ex = el.offsetLeft;
	var ey = el.offsetTop;
	while(el = el.offsetParent){
		ex += el.offsetLeft;
		ey += el.offsetTop;
	}
	return{ left:ex,top:ey };
}
	

</script>	
</head>
<body>

<!-- 内层DIV   start -->
    <div class='panel'>
	    <div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>${empty productCategory ? '添加' : '修改' }商品分类
			</strong>
		</div>
		
	    <div class='panel-body'>
	      	<form id="productCategoryAddForm" method='post' class='form-horizontal' repeatSubmit='1' autocomplete="off" action="${contextPath }/commodityCategory/save">
	      		  <input id="id" name="id" type="hidden" value="${productCategory.id }" />
	      		  <input id="level" name="level" type="hidden" value="${productCategory.level }" />
	      		  <input id="isParent" name="isParent" type="hidden" value="${isParent }" />
				
		          <div class="form-group">
			            <label class="col-md-1 control-label">上级分类</label>
			            <div class="col-md-2">
				              <input maxlength="32" type="text" name="parentName" id="parentName" readOnly="readOnly" placeholder="不选择则默认为顶级分类" value="${productCategory.parentName }" class="form-control">
				              <input maxlength="32" type="hidden" name="pId" id="pId" readOnly="readOnly" placeholder="不选择则默认为顶级分类" value="${productCategory.pId }" class="form-control">
			            </div>
			            <input type="button" name="cancelSelect" id="cancelSelect" value="取消选择"/>
		          </div>
		          
		          <div class="form-group">
			            <label class="col-md-1 control-label">分类名称</label>
			            <div class="col-md-2">
			               <input maxlength="32" type="text" name="name" id="name" value="${productCategory.name }" class="form-control">
			            </div>
			            <label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
		          </div>
							
					<div class="form-group">
						<label class="col-md-1 control-label">排序</label>
						<div class="col-md-2">
							<input type="text" name="sort" placeholder="由小到大排序,不填则默认最前" class='form-control' id="sort" value="${productCategory.sort }" maxlength="7"  >
						</div>
					</div>
					
					 <div class="form-group">
						<div class="col-md-offset-1 col-md-10">
							<input type="button"  id='productCategoryAddBtn' class='btn btn-primary' value="${empty productCategory ? '添加' : '修改' }" />
						</div>
				  	</div>
	        </form>
	    </div>
  </div>
<!-- 内层DIV   end -->

</body>
</html>