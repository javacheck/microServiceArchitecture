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
		lm.post("${contextPath }/agent/business/delete/delete-by-id",{id:id},function(data){
			if(data==1){
				lm.alert("删除成功！");
				window.location.href="${contextPath}/agent/businessList/"+${businessId};
			} else {
				lm.alert("删除失败！");
			}
		});
	});
}
</script>
</head>
<body>
	<m:hasPermission permissions="agentAdd" flagName="addFlag"/>
	<m:list title="结算账户列表" id="businessList"
		listUrl="${contextPath }/agent/businessList/business/list-data/${businessId}"  
		addUrl="${addFlag == true ? contextPath.concat('/agent/business/add/'.concat(businessId)): '' }">
		
		<%-- <div class="input-group" style="max-width: 1500px;">
			<input type="hidden" id="businessId" name="businessId" value="${businessId}"/>            
		</div> --%>
	</m:list>
</body>
</html>