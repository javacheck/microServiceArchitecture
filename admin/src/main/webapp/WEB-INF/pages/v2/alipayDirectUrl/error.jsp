<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>授权成功！</title>
<script language="javascript" type="text/javascript"> 
var i = 5; 
var intervalid; 
intervalid = setInterval("fun()", 1000); 
function fun() { 
	if (i == 0) { 
		//window.location.href = "../shop";  // 跳转到商家列表页面
		window.close();
		clearInterval(intervalid); 
	} 
	document.getElementById("mes").innerHTML = i; 
	i--; 
} 
</script> 
</head>
<body>
	<p><font color="red">授权失败！</font>
		失败原因：
		${errorMessge }
        ${queryAuthError }
		${getAuthError }
	</p>
	<p>将在 <span id="mes">5</span> 秒钟后返回商家列表页面！</p> 
</body>
</html>