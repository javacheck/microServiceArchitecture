<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>合作者列表</title>
<script type="text/javascript">
	function callback(){
		$("#mainShopListDataId").find("tbody tr").click(function(){
			$("#parentId").val($(this).attr("val"));
			$("#mainShopName").val(($($(this).find("td")[0]).html())); 
			$("#mainShopModalBtn").click();
		});
	}

</script>
</head>
<body>
	<div class="modal-dialog modal-lg" style="width: 1200px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" id="mainShopModalBtn">
					<span aria-hidden="true">×</span><span class="sr-only">关闭</span>
				</button>
			</div>
			<m:list title="总部列表" id="mainShopList"
				listUrl="${contextPath }/shop/mainShopShow/list-data" callback="callback"
				searchButtonId="mainShop_cateogry_search_btn">
				<div class="input-group" style="max-width: auto">
					<span class="input-group-addon">总部名称</span>
						<input type="text" id="name" name="name" class="form-control" placeholder="总部名称" style="width: 200px;"> 
				</div>
			</m:list>
		</div>
	</div>
</body>
</html>