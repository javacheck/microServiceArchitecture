<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>促销商品列表</title>
<script type="text/javascript">
function deleteId(id,storeId){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/promotion/delete/delete-by-Id",{id:id,storeId:storeId},function(data){
			if(data==1){
				lm.alert("删除成功！");
			} else {
				lm.alert("删除失败！");
			}
			loadCurrentList_promotionList();
		});
	});
}
</script>

</head>
<body>
	<m:list title="促销列表" id="promotionList"
		listUrl="/promotion/list-data"
		searchButtonId="promotion_search_btn">
		
		<div class="input-group" style="max-width: 800px;">
			<span class="input-group-addon">促销类型</span>
				<select id="promotionType" class='form-control' style="width: auto;float:left;margin-right:40px;" name="promotionType">
					<option id="pleaseSelect" value="-10">全部</option>
					<option id="first_order" value="1">首单</option>
					<option id="full_subtract" value="2">满减</option>
					<option id="discount" value="3">折扣</option>
					<option id="combination" value="4">组合</option>
				</select>
			
			<span class="input-group-addon">促销状态</span>
				<select id="promotionStatus" class='form-control' style="width: auto;float:left;margin-right:40px;" name="promotionStatus">
					<option id="pleaseSelect" value="-10">全部</option>
					<option id="on" value="1">开启</option>
					<option id="off" value="0">关闭</option>
				</select>
							
			<span class="input-group-addon">促销名称</span> 
			<input type="text" name="promotionName" class="form-control" placeholder="促销名称">
		</div>
		<div class="dropdown">
			<input type="button" id='promotionAddBtn' class='btn btn-primary' value="添加" data-toggle="dropdown"/>
        	<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
        	 <li role="presentation" class="dropdown-header">请选择促销类型</li>
	          <li>
	            <a href="${contextPath }/promotion/add?promotion_type=1">首单</a>
	          </li>
	          <li>
	            <a href="${contextPath }/promotion/add?promotion_type=2">满减</a>
	          </li>
	          <li>
	            <a href="${contextPath }/promotion/add?promotion_type=3">折扣</a>
	          </li>
	          <li>
	            <a href="${contextPath }/promotion/add?promotion_type=4">组合</a>
	          </li>
         </ul>
      </div>
	</m:list>
</body>
</html>