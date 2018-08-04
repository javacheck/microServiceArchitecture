<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty isUpdate ? '添加' : '修改' }会员等级设置</title> 
<script type="text/javascript" src="${staticPath }/js/ztree/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${staticPath }/js/ztree/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="${staticPath }/js/ztree/jquery.ztree.exedit-3.5.js"></script>
<link rel="stylesheet" href="${staticPath }/css/ztree/zTreeStyle/zTreeStyle.css" type="text/css">
<style type="text/css">
.ztree li span.button.add {margin-left:2px; margin-right: -1px; background-position:-144px 0; vertical-align:top; *vertical-align:middle}
</style>
<script type="text/javascript">
  
	var setting = { 
			
			check: {
				enable: true,
				chkStyle: "checkbox",
				autoCheckTrigger: true,
	 			chkboxType: { "Y": "s", "N": "s" }
			},
			data: {
			    //简单的数据源，一般开发中都是从数据库里读取，API有介绍，这里只是本地的                           
			    simpleData: {   
				   enable: true,  /** 确定 zTree 初始化时的节点数据、异步加载时的节点数据、或 addNodes 方法中输入的 newNodes 数据是否采用简单数据模式 (Array)
				   					    不需要用户再把数据库中取出的 List 强行转换为复杂的 JSON 嵌套格式
				   				   **/
				   idKey: "id",  // 节点数据中保存唯一标识的属性名称。  
				   pIdKey: "pId" // 节点数据中保存其父节点唯一标识的属性名称。 (第二个I必须是大写的，不然没有父子结构)
				}                            
			}
		};                
	    

	
	  
var zTree;  
var treeNodes;
 
$(document).ready(function(){
	
	$("#discountScope_0").click(function () {
		 if($(this).prop("checked")){
			 $("#categoryDiv").hide();
		 }
	 });
	
	$("#discountScope_1").click(function () {
		 if($(this).prop("checked")){
			 $("#categoryDiv").show();
			 initProductCategoryTree($("#id").val());
		 }
	 });
	var discountScope='${userLevelDefinition.discountScope}';
	if(discountScope==1){
		var categoryIdList='${userLevelDefinition.categoryIdList}';
		$("#discountScope_1").prop("checked",true);
		 $("#discountScope_0").prop("checked",false);
		 $("#categoryDiv").show();
		 initProductCategoryTree($("#id").val());
	}
	
	// 保存信息start
	$("#userLevelDefinitionAddBtn").click(function(){
		
		var storeId = $("#storeId").val();
		storeId = $.trim(storeId);
		if( null == storeId || "" == storeId ){
			lm.alert("商家不能为空");
			return;
		}
		var name = $("#name").val(); // 等级名称
		name = $.trim(name);
		
		if( name == "" || name == null ){
			lm.alert("等级名称不能为空!");
			return;
		}
		
		var isRepeat = false;
		var id = $("#id").val();
		lm.postSync("${contextPath }/userLevelDefinition/list/ajax/checkUserLevelDefinitionName/", {id:id,storeId:storeId,name:name}, function(data) {
			if (data == 1) {
				lm.alert("等级名称存在重复,请重新输入!");
				isRepeat = true;
			} 
		});
		
		if (isRepeat) {
			return;
		}
		var type = $("#type").val();
		
		var point = $("#point").val(); // 所需积分
		point = $.trim(point);
		
		if( point == "" || point == null ){
			if( type == 0 ){
				lm.alert("累计积分不能为空!");				
			} else {
				lm.alert("累计消费不能为空!");
			}
			return;
		}
		if( type == 0 ){
			
			if( !(/^(0|[1-9][0-9]*)$/.test(point)) ){
				lm.alert("累计积分格式错误!");
				return;
			}
		} else {
			if( !(lm.isTwoPointFloat(point)) ){
				lm.alert("累计消费格式错误!");
				return;
			}
		}
		
		var discount = $("#discount").val(); // 享受折扣
		discount = $.trim(discount);
		
		if( discount != "" && discount != null ){
			if ( !((/^[0-9]+([.]\d{1})?$/.test(discount)) && (discount > 0 && discount < 10)) ){
				lm.alert("享受折扣格式错误!");
				return;
			}
		}
		var discountScope=$('input:radio[name="discountScope"]:checked').val();
		var list= new Array();
		if(discountScope==1){//按分类
			var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
			var nodes = treeObj.getCheckedNodes(true);
			if(nodes.length<=0){
				lm.alert("请选择商品分类！");
				return;
			}else{
				for(var i=0;i<nodes.length;i++){
					var obj =new Object();
					obj.categoryId= nodes[i].id;
					list.push(obj);
				}
			}
		}
		$("#userLevelDefinitionAddBtn").prop("disabled",true);
		var mode = $("#mode").val();
		lm.post("${contextPath }/userLevelDefinition/save",{dateCategorySJson:JSON.stringify(list),storeId:storeId,name:name,
			id:id,type:type,point:point,discount:discount,discountScope:discountScope,mode:mode},function(data){
			if(data=='1'){
		    	lm.alert("操作成功！");
		    	 window.location.href="${contextPath}/userLevelDefinition/list";
			}
		}); 
		
	}); 
	// 保存信息end
});
function initProductCategoryTree(id){
	lm.postSync("${contextPath }/userLevelDefinition/ajax/loadZtreeListNoSubordinate", {id:id}, function(data) {
		zTree=$.fn.zTree.init($("#treeDemo"), setting, data);
	});
}
function type_select(){
	var type = $("#type").val();
	var storeId = $("#storeId").val();
	storeId = $.trim(storeId);
	if( null == storeId || undefined == storeId ){
		lm.alert("请先选择商家!");
		return ;
	} else {
		lm.postSync("${contextPath }/userLevelDefinition/ajax/judgeExistType", {storeId:storeId}, function(data) {
			if(data.status == 2){
				$("#type option[value!='"+data.type+"']").remove();  
				type = data.type;
			} else {
				if(data.status == 1 ){
					var id = $("#id").val();
					if( null == id || undefined == id || "" == id ){
						$("#type option[value!='"+data.type+"']").remove();  
						type = data.type;					
					}
				} 				
			}
		});
	}
	
	if(type == 0 ){
		$("#label_type").text("累计积分");
		$("#point").attr("placeholder","请输入所需积分,0或正整数");
		$("#point").val("<fmt:formatNumber value='${userLevelDefinition.point }' type='currency' pattern='0'/>");
	} else {
		$("#label_type").text("累计消费(元)");
		$("#point").attr("placeholder","请输入累计消费金额");
		$("#point").val("<fmt:formatNumber value='${userLevelDefinition.point }' type='currency' pattern='0.00'/>");
	}
}
$(function(){
	$("#type").change(function(){
		type_select();
	});
	
	var id = $("#id").val();
	if( null != id && "" != id ){
		$("#type").val('${userLevelDefinition.type}');
		$("#mode").val('${userLevelDefinition.mode}');
	} else {
		$("#type").val("0");
	}
	type_select();
});
</script>
</head>
<body>
	<div class='panel'>
	
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>${empty isUpdate ? '添加' : '修改' }会员等级设置
			</strong>
		</div>
		
		<div class='panel-body'>
			<form id="userLevelDefinitionAddForm"   class='form-horizontal'>
				
				<input type="hidden" id="id" name="id" class='form-control' value="${userLevelDefinition.id }" maxlength="100"  >
					<input type="hidden" name="storeId" class='form-control' id="storeId"  value="${userLevelDefinition.storeId }">
				<div class="form-group">
					<label class="col-md-1 control-label">等级名称</label>
					<div class="col-md-2">
						<input type="text" id="name" name="name" class='form-control' value="${userLevelDefinition.name }" maxlength="100"  >
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
					
				<div class="form-group">
					<label class="col-md-1 control-label">升级方式</label>
					<div class="col-md-2">
						<select id="mode" name="mode" class='form-control'> 
							<option id="0" value="0" selected="selected">自动升级</option>
							<option id="1" value="1">手动升级</option>
						</select>
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">升级条件</label>
					<div class="col-md-2">
						<select id="type" name="type" class='form-control'> 
							<option id="0" value="0" selected="selected">累计积分</option>
							<option id="1" value="1">累计消费</option>
						</select>
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label" id="label_type"></label>
					<div class="col-md-2">
						<input type="text" id="point" name="point" class='form-control' value="" maxlength="7"/>
					</div>
						<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>&nbsp;&nbsp;
				</div>
					
				<div class="form-group">
					<label class="col-md-1 control-label">享受折扣</label>
					<div class="col-md-2">
						<input type="text" id="discount" name="discount" class='form-control' placeholder="请输入折扣，如八五折输入8.5" value="<fmt:formatNumber value="${userLevelDefinition.discount == 10 ? '' : userLevelDefinition.discount }" type="currency" pattern="0.0"/>" maxlength="7"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label">折扣范围</label>
					<div style="padding-top:5px;">
						<input  type="radio"    id="discountScope_0" name="discountScope" value="0" checked="checked"/>全部商品&nbsp;&nbsp;&nbsp;&nbsp;
						<input  type="radio"    id="discountScope_1" name="discountScope"  value="1" />按分类
					</div>
				</div>
				<div  style="display: none;" id="categoryDiv">
					<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>&nbsp;&nbsp;商品分类</label>
					<div class="col-md-2">
						<ul id="treeDemo" class="ztree"></ul>
					</div>
				</div>
				</div>		
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button" id='userLevelDefinitionAddBtn' class='btn btn-primary' value="${empty isUpdate ? '添加' : '修改' }" data-loading='稍候...' />
					</div>
				</div>
			</form>
	  </div>
   </div>
   
    <m:select_store path="${contextPath}/shop/showModle/list/list-data" modeId="userLevelDefinitionStore" callback="callback"> </m:select_store>
</body>
</html>