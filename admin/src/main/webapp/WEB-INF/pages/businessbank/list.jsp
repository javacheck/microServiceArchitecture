<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>结算账户列表</title>
<script type="text/javascript">
function deleteBusinessBank(id){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/businessbank/delete/delete-by-id",{id:id},function(data){
			if(data==1){
				lm.alert("删除成功！");
				
			} else {
				lm.alert("删除失败！");
				window.location.href="${contextPath}/shop/businessList/"+businessId;
			}
			loadCurrentList_businessbankList();
		});
	});
}
</script>
</head>
<body>
	<m:hasPermission permissions="businessbankAdd" flagName="addFlag"/>
	<m:list title="银行卡管理列表" id="businessbankList"
		listUrl="${contextPath }/businessbank/list-data/"  
		addUrl="${true ? '/businessbank/add': '' }" 
		searchButtonId="businessbank_search_btn">
        
         <div class="input-group" style="max-width: 600px;">
         <c:if test="${isMainStore != null }">
            <span class="input-group-addon">商家</span>
            <select id="main_storeId" name="main_storeId" style="width: auto;" class="form-control" >
            	<option id="all" value="-1">全部</option>
            	<c:forEach items="${storeList }" var="store">
            		<option id="${store.id }" value="${store.id }">${store.name }</option>
            	</c:forEach>            
            </select> 
         </c:if>
            <span class="input-group-addon">银行名称</span> 
            	<input type="text" id="bankName" name="bankName" class="form-control" placeholder="银行名称" style="width: 200px;">
		</div>
	</m:list>
</body>
</html>