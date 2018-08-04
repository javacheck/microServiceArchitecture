<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>莱麦商户后台</title>
<%-- <link rel="stylesheet" href="${staticPath }/css/all.admin.css"> --%>
<link rel="stylesheet" type="text/css" href="${staticPath }/css/shouye.css" />
<script type="text/javascript">var config={}</script>
<script type="text/javascript" src="${staticPath }/js/all.admin.js"></script>
<script type="text/javascript">
	$(function() {
		$("#_captcha").click(function() {
			$(this).attr("src", "${contextPath }/captcha?" + new Date());
		});
	});
	$(function() {
		var _type = $("#_type").val();
		if( null != _type && "" != _type ){
			$("#type").val(_type);			
		}
		//绑定 提交方法
		 $("#loginAddBtn").click(function() {
			 $("#loginSave").submit();
		 });
	});
</script>
<style type="text/css">
.content-l{
   position: relative;
}       
.popup{
    position: absolute;
    top:12px;
    left:220px;
    color:red;
    font-size:20px;
}
</style>
<body class="body-bg">
    
    <div class="section w">
        <div class="head">
            <img src="${staticPath }/images/LOGO2.png" alt="logo" width="100%" />
            <h1>莱麦商户后台</h1>
        </div>
        <input type="hidden" id="_type" value="${type }"/>
        <div class="content">
            <div class="content-l">
                <form  method="post" id="loginSave" action="${contextPath }/login">
                	<c:if test="${not empty error }">
						<div id='formError'  class="popup" >${error }</div>
					</c:if>
					<div class="import">
	                    <span>用户名：</span>
	                    <input type='text' name='username' id='username' value='${username }' class='form-control' placeholder='请输入用户名' />
              		</div>
              		<div class="import">
	                    <span>密码：</span>
	                    <input type='password' name='password' id='password' value='${password }' autocomplete="off" class='form-control' placeholder='请输入密码' />
                	</div>
                	<div class="import">
	                    <span>登录身份：</span>
	                    <select class='form-control pull-down' name="type" id="type" style="width: 331px;">
							<option id="1" value="1">管理员</option>
							<option id="3" value="3" selected="selected">商家</option>
							<option id="2" value="2">代理商</option>
						</select>
                	</div>
                <div class="import" style="margin-bottom:14px;">
                    <span>验证码：</span>
                    <input  style="width: 192px;" type='text' id="captcha" name="captcha" value='' autocomplete="off"
									class='form-control' placeholder='请输入验证码' />
                    <div class="yangzheng"><img id="_captcha" style="cursor: pointer;" alt="" src="${contextPath }/captcha"></div>
                </div>
                <%-- <div class="import-che">
                    <span></span>
                    <input type="checkbox" <c:if test="${not empty remember }">checked="checked"</c:if>  name="remember" value="1"/><i>记住密码</i>
                </div> --%>
                <button id="loginAddBtn" style="cursor:pointer;">登录系统</button>
                <div class="shuxian">
                    <img src="${staticPath }/images/shuxian.png" alt="" height="100%" />
                </div>
               </form>
            </div>
            
            <div class="content-r">
                <img src="${staticPath }/images/code.png" alt="二维码" width="100%" />
                <i>扫一扫</i><br />
                <i>关注莱麦微信公众号</i>
            </div>
        </div>
    </div>
	<footer>
        <p>广州市天河区中山大道建工路13、15号503室 联系电话：020-28871503  </p>
        <p>Copyright  2015 广州市莱麦互联网科技有限公司 版权所有. 粤ICP备15062264号-1</p>
    </footer>
	
</body>
</html>