<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>优惠劵列表</title>
<script type="text/javascript">
function deleteId(id,userId){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/cashGift/delete/delete-by-Id",{id:id,userId:userId},function(data){
			if(data==1){
				lm.alert("删除成功！");
			} else {
				lm.alert("删除失败！");
			}
			loadCurrentList_cashGiftList();
		});
	});
}
</script>
</head>
<body>
	<m:hasPermission permissions="cashGiftAdd" flagName="addFlag"/>
	<m:list title="优惠劵列表" id="cashGiftList"
		listUrl="${contextPath }/cashGift/list-data"
		addUrl="${addFlag == true ? '/cashGift/add' : '' }"
		searchButtonId="cashGif_search_btn">
		
		<div class="input-group" style="max-width: 600px;">
			<m:acountType isTrue="false" accountType="store">
			<span class="input-group-addon">商家</span>
            <input type="text" id="shopName" name="shopName" class="form-control" placeholder="商家名称" style="width: 200px;">
            </m:acountType>
            <span class="input-group-addon">手机号码</span> 
            <input type="text" id="mobile" name="mobile" class="form-control" placeholder="手机号码" style="width: 200px;">
           
		</div>
	</m:list>
</body>
</html>