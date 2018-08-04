<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="treeRoot" required="true"%>
<%@ attribute name="selectID" required="true"%>
<%@ attribute name="showValue"%>
<%@ attribute name="showId"%>
<%@ attribute name="ban"%>
<%@ attribute name="linkURL"%>
<%@ attribute name="showAllOption"%>

<script type="text/javascript">
$(function(){
	var showValue = '${showValue}';
	var showId = '${showId}';
	var ban = '${ban}';
	
	if( null != showValue && "" != showValue && null != showId && "" != showId ){
		$("#${selectID}").append("<option value="+ showId+">"+showValue+"</option>");
	} else {
		if( "" != '${showAllOption}' ){ 
			$("#${selectID}").append("<option value=''>全部</option>");
		}
	}
	if( null != ban && "" != ban ){
		$("#${selectID}").attr("disabled",true);
	}
	function createShowTreeDiv(objID){
		var entObj = $("#"+objID);
		if( entObj.length > 0 ){
			var top = "auto";
			if(entObj.parent(".input-group").length > 0 ){
				top = "30px";
			}
			//创建一个显示选择树的DIV
			var treeDIV = "<div id='${treeRoot}' class='ztree' style='margin-top:"+top+";background:#ffffff;position:absolute;width:215px;height:auto;overflow:scroll;border-style:outset;z-index:1000;'></div>";			
			$(treeDIV).insertAfter(entObj);
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
					   pIdKey: "pId", // 节点数据中保存其父节点唯一标识的属性名称。 (第二个I必须是大写的，不然没有父子结构)
					   isCurrentStoreData: "isCurrentStoreData",
					   rootPId: null  // 用于修正根节点父节点数据，即 pIdKey 指定的属性值。
					}                            
				},                           
				callback: {     /** 回调函数的设置  **/  
					  onAsyncSuccess: zTreeOnAsyncSuccess,
					  onClick: zTreeOnClick
				}  
			};
			// 初始化树
			return $.fn.zTree.init($("#${treeRoot}"), setting);
		}
		return null;
	}
	
	// 动态设置异步加载的URL 
   function getAsyncUrl(treeId, treeNode) {
		var linkURL = '${linkURL}';
		if( null == linkURL || "" == linkURL ){
			var storeId = $("#treeStoreId").val();
			if( undefined == storeId ){
				storeId = "";
			}
			return "${contextPath }/commodityCategory/ajax/loadZtreeListNoSubordinate?storeId="+storeId;
		}
  	    return linkURL;
   };

 	// 异步记载成功时执行的方法
	function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
		searchCategoryName($.fn.zTree.getZTreeObj("${treeRoot}"));
	};
		
    function zTreeOnClick(event, treeId, treeNode) {
   	 	var selectID = $("#${selectID}");
   	 	if( selectID.length > 0 ){
	   	 	selectID.empty();
	   		selectID.append("<option value="+ treeNode.id+">"+treeNode.name+"</option>"); 
	   		hideTreeDIVMenu();
   	 	}
    }
   	   	
	// 树型(颜色变化)
   function getFontCss(treeId, treeNode) {
   		return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
   }
   	
	// 树过滤条件
   function filter(node) {
       return node.highlight == true ;
   }
   	
	// 修改树节点
  	function updateNodes(treeRoot,highlight,nodeList) {
  		for( var i=0, l=nodeList.length; i<l; i++) {
  			nodeList[i].highlight = highlight;
  			treeRoot.updateNode(nodeList[i]);
  			treeRoot.expandNode(nodeList[i].getParentNode(), true, false, true);
  		}
  	}

	// 在树型中检索搜索内容
  	function searchCategoryName(treeRoot){
  		// 得到搜素条件
  		var pId = $("#${selectID}").val();
  		pId = $.trim(pId);
  		nodes = treeRoot.getNodesByFilter(filter);
  		for( var i = 0 ; i < nodes.length ; i++ ){
  			nodes[i].highlight = false; 
  			treeRoot.updateNode(nodes[i]);
  		}
  		if( null != pId && "" != pId ){
  			var nodes = treeRoot.getNodeByParam("id", pId, null);
  			if( null == nodes ){
  				nodes = treeRoot.getNodes();
  				if( nodes.length > 0 ){
  					treeRoot.selectNode(nodes[0]);
  				}
  				lm.alert("因找不到对应的选项,故默认选中第一项");
  				return false;
  			}
  			treeRoot.selectNode(nodes); // 设置选中
  			var cacheArray = new Array();
  			cacheArray.push(nodes);
  			updateNodes(treeRoot,true,cacheArray);				
  		} else {
  			// 将树形结构的第一个'全部'选项选中(如果有全部选项的话)
  			var nodes = treeRoot.getNodeByParam("id", "", null);
  			if( null != nodes ){
	  			treeRoot.selectNode(nodes);  				
  			} else {
  				nodes = treeRoot.getNodes();
  				if( nodes.length > 0 ){
  					treeRoot.selectNode(nodes[0]);
  				}
  			}
  		}
  	}
   
	// 分类select选择框点击时的触发事件
	$("#${selectID}").click(function(){
		
		// 不存在树形DIV的时候才创建
		if( $("#${treeRoot}").length <= 0 ){
			var treeRoot = createShowTreeDiv("${selectID}");
			if( null != treeRoot ){
				// 展示全部选项
				if( "" != '${showAllOption}' ){ 
					var newRootNode = {id:"",name:"全部"};
					treeRoot.addNodes(null, newRootNode);					
				}
				
				//searchCategoryName(treeRoot);
				$("#${treeRoot}").show();
			 	$("body").bind("mousedown", onBodyDownBy);
			}				
		} else {
			hideTreeDIVMenu();
		}
	});
		
	// Body鼠标按下事件回调函数
	function onBodyDownBy(e) {
		 e = window.event || e; // 兼容IE7
		 var obj = (e.srcElement || e.target);
		 
		 var id = $(obj).attr("id");
		 if( undefined == id || null == id ){
			 hideTreeDIVMenu();
			 return;
		 }
		 if(!estimateTreeScope(id)){
			 hideTreeDIVMenu();
		 }
	}

	// 删除树形DIV且注销绑定事件
	function hideTreeDIVMenu() {
		$("#${treeRoot}").remove();
	    $("body").unbind("mousedown", onBodyDownBy);
	}

	function estimateTreeScope(id){
		if( id== "${treeRoot}" || id == "${selectID}" || (id+"").indexOf('${treeRoot}') != -1 ){
			 return true;
		} else {
			 return false;
		}
	} 
	
});
</script>
<select class="form-control" style="width: 215px;height:auto;" id="${selectID}" name="${selectID}"></select>