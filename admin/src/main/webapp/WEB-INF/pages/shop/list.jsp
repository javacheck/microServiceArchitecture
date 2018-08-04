<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商家列表</title>
<script type="text/javascript">
function deleteById(id){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/shop/delete/delete-byId",{id:id},function(data){
			if(data==1){
				lm.alert("删除成功！");
			} else {
				lm.alert("删除失败！");
			}
			loadCurrentList_shopList();
		});
	});
}

// 点击搜索时先判断价格参数的正确性
function checkParameter(){
	var mobile = $("input[name='mobile']").val();
	mobile = $.trim(mobile); // 用jQuery的trim方法删除前后空格
	if( null != mobile && mobile != "" ){
		if( !(/^1[0-9]*$/.test(mobile)) ){
			lm.alert("请输入正确的手机号码！");
			return ;
		}
	}
	
	return true; 
}

function getExe() {
	var storeId=$("#storeId").val();//店铺
	if( null == storeId || undefined == storeId ){
		storeId = "";
	} 
	var shopName =$("#shopName").val();//商家名称
	var mobile =$("#mobile").val();//手机号码
	var agentName = $("#agentName").val();//代理商名称
	if (typeof(agentName) == "undefined" || agentName==null){
		agentName="";
	}
	var status=$("#status").val();//营业状态
	window.open("${contextPath}/shop/list/ajax/list-by-search?storeId="+storeId+"&mobile="+mobile
					+"&agentName="+agentName+"&status="+status+"&shopName="+shopName);
}
</script>
</head>
<body>
	<m:hasPermission permissions="shopAdd" flagName="addFlag"/>
	<m:list title="商家列表" id="shopList"
		listUrl="${contextPath }/shop/list/list-data"
		addUrl="${(addFlag == true) && (isStore == null) ? '/shop/add' : '' }"
		searchButtonId="cateogry_search_btn" beforeSearch="checkParameter" >
		
		<div class="input-group" style="max-width: 1500px;">
		
		 <span class="input-group-addon">商家名称</span> 
            	<input type="text" id="shopName" name="shopName" class="form-control" placeholder="商家名称" style="width: 200px;">
            	<input type="hidden" id="storeId" name="storeId$" class="form-control" value="${storeId$ }" style="width: 200px;">
            	
         <span class="input-group-addon">手机号码</span> 
            	<input type="text" id="mobile" name="mobile" class="form-control" placeholder="手机号码" style="width: 200px;">
		
		<c:if test="${isStore == null }">
				<span class="input-group-addon">代理商名称</span>
           		<input type="text" id="agentName" name="agentName" class="form-control" placeholder="代理商名称" style="width: 200px;">		
        </c:if>
        
		<span class="input-group-addon">营业状态</span>
           	<select name="status" class="form-control" style="width: 200px;" id="status">
           		<option id="all" value="-10" selected="selected">全部</option>
           		<option id="first" value="1">营业中</option>
           		<option id="last" value="0">已结业</option>
           	</select>
		</div>
		<button type="button" onclick="getExe()" class="btn btn-small btn-danger" style="width: auto;margin-top:5px;">批量导出</button>
	</m:list>
</body>
</html>