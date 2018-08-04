<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商家列表</title>
<script type="text/javascript">
	function callbackShop(){
		$("#shopListDataId").find("tbody tr").click(function(){
			$("#storeId").val($(this).attr("val"));
			$("#storeName").val(($($(this).find("td")[0]).html()));
			shopChange($(this).attr("val"));
			$("#productName").val("");
			$("#productId").val("");
			$("#addShopCategory").hide();
			$("#addAdminCategory").hide();
			$("#shopListModalBtn").click();
		});
		$("[name='shopListModalBtn']").each(function(key,value){
			var dataRemote = $(value).attr("data-remote");
			$(value).attr("data-remote",dataRemote + "?date="+new Date());
		});
	}
</script>
</head>
<body>
	<div class="modal-dialog modal-lg" style="width: 1200px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" id="shopListModalBtn">
					<span aria-hidden="true">×</span><span class="sr-only">关闭</span>
				</button>
			</div>
			<div class="modal-body">
				<m:list title="商家列表" id="shopList" listUrl="${contextPath }/product/shopList/list-data" callback="callbackShop" searchButtonId="cateogry_search_btn" >
					
					<div class="input-group" style="max-width: 1500px;">
						 <span class="input-group-addon">商家名称</span> 
		            	 <input type="text" id="name" name="name" class="form-control" placeholder="商家名称" style="width: 200px;">
				            	
				         <span class="input-group-addon">手机号码</span> 
		            	 <input type="text" id="mobile" name="mobile" class="form-control" placeholder="手机号码" style="width: 200px;">
					</div>
				</m:list>
			</div>
		</div>
	</div>
</body>
</html>