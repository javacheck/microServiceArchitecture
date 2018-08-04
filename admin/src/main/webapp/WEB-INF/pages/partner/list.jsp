<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>合作者管理列表</title>
<script type="text/javascript">
function deletePartner(id){
	lm.confirm("确定要删除吗？",function(){
		lm.post("${contextPath }/partner/delete/delete-by-Id",{id:id},function(data){
			if(data==1){
				lm.alert("删除成功！");
			} else {
				lm.alert("删除失败！");
			}
			loadCurrentList_partnerList();
		});
	});
}
</script>
</head>
<body>
	<m:hasPermission permissions="partnerAdd" flagName="addFlag"/>
	<m:list title="合作者管理列表" id="partnerList"
		listUrl="${contextPath }/partner/list-data"
		addUrl="${addFlag == true ? '/partner/add' : '' }">
	</m:list>
</body>
</html>