<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>支付宝授权详情</title>
<style type="text/css">
h4.hh{
    text-align: center;
font-size: 20px;
font-weight: 100;
}
.guanbi{
    font-size: 10px;
color: rgb(0, 0, 0);
line-height: 25px;
}
.modal-header{
    border-bottom: 1px solid #797979;
}

.bor-no{
    border:none;
    width:92%;
    margin:0 auto;
}
.bor-no td{
    border:none;
    background-color:#fff;
    font-family: 'Arial Normal', 'Arial';
}
.biaoti{
    color:#169BD5;
    font-size:16px;
}
.bg-co td{
    background-color:rgba(242, 242, 242, 1);
    padding-left:30px;
}
.table-t{
    padding:0;
    border:1px solid #797979;
}
.table-t th,.table-t td{
    background-color:#fff;
    text-align: center;
}
.first{
    width:305px;
    border-left:none;
}
.text1{
    font-size: 20px;
color: #0000FF;
font-weight: 400;
}
.text2{
    font-size: 22px;
color: #FF0000;
font-weight: 400;
}
.text3{
    font-size: 22px;
font-weight: 400;
}
.text4{
    font-weight: 600;
}

</style>
<script type="text/javascript">
</script>
</head>
<body>
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">×</span><span class="sr-only">关闭</span>
				</button>
				<h4 class="modal-title">支付宝授权详情</h4>
			</div>
			<table class="table table-hover table-striped table-bordered" cellpadding="0" cellspacing="0">
				 <tbody>
				 	<tr class="biaoti">
                		<td colspan="3">授权请求信息</td>
            		</tr>
            		<tr class="bg-co"  style="width: 100%;height: 30px">
                    	<td width="50%">商家：${alipayAuthSituation.storeName }</td>
                    	<td width="50%">请求ID：${alipayAuthSituation.getAuthID }</td>
                	</tr>
                	<tr class="bg-co" style="width: 100%; height: 30px;">
						<td width="50%">授权类型：${alipayAuthSituation.grant_type }</td>
                    	<td width="50%">刷新令牌：${alipayAuthSituation.refresh_token }</td>
            		</tr>
            		<tr class="bg-co"  style="width: 100%;height: 30px">
                    	<td colspan="2" width="100%">请求时间：<fmt:formatDate value="${alipayAuthSituation.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                	</tr>
            		
				 	<tr class="biaoti">
                		<td colspan="3">授权响应信息</td>
            		</tr>
            		<tr class="bg-co" style="width: 100%; height: 30px;">
						<td colspan="2" width="100%">商家：${alipayAuthSituation.storeName }</td>
            		</tr>
            		<tr class="bg-co"  style="width: 100%;height: 30px">
                    	<td width="50%">响应状态码：${alipayAuthSituation.code }</td>
                    	<td width="50%">响应MSG：${alipayAuthSituation.msg }</td>
                	</tr>
                	<tr class="bg-co"  style="width: 100%;height: 30px">
                    	<td width="50%">响应code：${alipayAuthSituation.sub_code }</td>
                    	<td width="50%">响应原因：${alipayAuthSituation.sub_msg }</td>
                	</tr>
                	<tr class="bg-co"  style="width: 100%;height: 30px">
                    	<td width="50%">商户授权令牌：${alipayAuthSituation.app_auth_token }</td>
                    	<td width="50%">授权商户的ID：${alipayAuthSituation.user_id }</td>
                	</tr>
                	<tr class="bg-co"  style="width: 100%;height: 30px">
                    	<td width="50%">授权商户的AppId：${alipayAuthSituation.auth_app_id }</td>
                    	<td width="50%">令牌有效期（天）：${alipayAuthSituation.expires_in }</td>
                	</tr>
                	<tr class="bg-co"  style="width: 100%;height: 30px">
                    	<td width="50%">刷新令牌TOKEN：${alipayAuthSituation.app_refresh_token }</td>
                    	<td width="50%">刷新令牌有效期（天）：${alipayAuthSituation.re_expires_in }</td>
                	</tr>
                
				 </tbody>
			</table>
		</div>
	</div>
</body>
</html>