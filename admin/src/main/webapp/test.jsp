<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<meta name="viewport" content="initial-scale=1.0,user-scalable=no">
<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
<title>Hello,world</title>
<style type="text/css">
html{height:100%}
body{height:100%;margin:0px;padding:0px}
#container{height:100%}
</style>
<script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/1.9.0/jquery.min.js"></script>
</head>
<script type="text/javascript">
	$(function(){
		 $('#btn').bind("click",function(event,a){alert(a);});  
		  $('#btn').trigger("click");  
	});
</script> 
<body>

<input type="button" id="btn" value="button"/>
</body>
</html>
