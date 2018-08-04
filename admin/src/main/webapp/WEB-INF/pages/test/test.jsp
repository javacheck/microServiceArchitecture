<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>  
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">  
<html>  
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>print test</title>  
  <style media="print">
.noprint { DISPLAY: none }
.pagenext{ page-break-after: always; }
</style>
  
</head>  
<body >  
<object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" width="0" id="WebBrowser"></object>  
<div id="print">  
    <table border="1" bordercolor="#000000" cellspacing="0" width="300" height="120" align="center">  
        <tr><td>编号</td><td>姓名</td><td>性别</td><td>年龄</td></tr>  
       <tr><td>1</td><td>张三</td><td>男</td><td>20</td></tr>  
    </table>  
</div>  
<br/>  
<div id="notprint" name="notprint">  
<input onclick="document.all.WebBrowser.ExecWB(6,1)" type="button" value="打印">
<input onclick="document.all.WebBrowser.ExecWB(6,6)" type="button" value="直接打印">
<input onclick="document.all.WebBrowser.ExecWB(8,1)" type="button" value="页面设置">
<input onclick="document.all.WebBrowser.ExecWB(7,1)" type="button" value="打印预览">
<input onclick="javascript:window.close()" type="button" value="关闭窗口"> 
</div>  

</body>  
</html>  
