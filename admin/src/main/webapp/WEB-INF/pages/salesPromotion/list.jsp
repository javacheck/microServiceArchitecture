<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>促销商品列表</title>
<script type="text/javascript">
function deleteId(id,stockId,shelves){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/salesPromotion/delete/delete-by-Id",{id:id,stockId:stockId,shelves:shelves},function(data){
			if(data==1){
				lm.alert("删除成功！");
			} else {
				lm.alert("删除失败！");
			}
		});
		
		var storeId = $("#salesPromotion_storeId").val();
		var salesPromotionCategoryId = $("#salesPromotionCategoryId").val();
		location.href = "${contextPath }/salesPromotion/list?storeId=" + storeId + "&salesPromotionCategoryId="+salesPromotionCategoryId;
	});
}

function callback(){
	var global_text_price;
    $("#salespromotion_list_data_array").find("td[name='price_show']").click(function(){
		var td=$(this); 
		var text=td.text();
		if( null != text && "" != text ){
			global_text_price = text;			
		}
		global_text_price = $.trim(global_text_price);
		td.html("<input/>");
		td.find("input").attr("value",global_text_price); 
		td.find("input").focus();
		td.find("input").blur(function(){
			var price = $(this).val();
			price = $.trim(price);  // 用jQuery的trim方法删除前后空格
			td.remove("input");
			if( null == price || "" == price){
				td.html(global_text_price); // 赋上原来的值
				return;
			}
			if(!(lm.isFloat(price))){
				td.html(global_text_price); // 赋上原来的值
				return;
			}
			td.html(price);
			var id = td.attr("id");
			lm.postSync("${contextPath }/salesPromotion/updatePrice", {id:id,price:price}, function(data) {
				if (data == 0) {
					td.html(global_text_price);
				} 
			});
		});
	});
	 
	 
	var global_text_salesNum;
    $("#salespromotion_list_data_array").find("td[name='sales_num_show']").click(function(){
		var td=$(this);
		var text=td.text();
		if( null != text && "" != text ){
			global_text_salesNum = text;	
		}
		global_text_salesNum= $.trim(global_text_salesNum);
		td.html("<input/>");
		td.find("input").attr("value",global_text_salesNum); 
		td.find("input").focus();
		td.find("input").blur(function(){
			var salesNum = $(this).val();
			salesNum = $.trim(salesNum);  // 用jQuery的trim方法删除前后空格
			td.remove("input");
			if( null != salesNum && "" != salesNum){
				if( !(/^\+?[1-9][0-9]*$/.test(salesNum)) ){
					td.html(global_text_salesNum); // 赋上原来的值
					return;
				}
				td.html(salesNum);
			} else {
				td.html("无限数量");
			}
			var stockId = td.attr("stockId_sign");
			var id = td.attr("sign");
			lm.postSync("${contextPath }/salesPromotion/updateSalesNum", {id:id,salesNum:salesNum,stockId:stockId}, function(data) {
				if (data == 0) {
					td.html(global_text_salesNum);
				} else if(data == 2){
					td.html(global_text_salesNum);
					lm.alert("商品库存不足");
				}
			});
		});
	});
};
</script>

</head>
<body>
	<m:list title="促销商品列表" id="salesPromotionList"
		listUrl="/salesPromotion/list-data"
		searchButtonId="salesPromotion_search_btn" callback="callback">
		
		<div class="input-group" style="max-width: 500px;">
			<span class="input-group-addon">商品分类</span>
				<select id="productCategoryId" class='form-control' style="width: auto;float:left;margin-right:40px;" name="productCategoryId">
					<option id="listPleaseSelect" value="-1">请选择</option>
					<c:forEach items="${categoryList }" var="category" >
						<option value ="${category.id}">${category.name}</option>
					</c:forEach>
				</select>
						
			<span class="input-group-addon">商品名称</span> 
			<input type="text" name="productName" class="form-control" placeholder="商品名称">
			<input type="hidden" name="storeId" id="salesPromotion_storeId"  value="${storeId}">
			<input type="hidden" name="salesPromotionCategoryId" id="salesPromotionCategoryId" value="${salesPromotionCategoryId}">
		</div>
	</m:list>

</body>
</html>