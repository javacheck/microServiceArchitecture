<%@page import="cn.lastmiles.utils.SecurityUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Cache-Control" content="no-transform">
<title>后台</title>
<%
	if (SecurityUtils.isStore()){
%>
<script type="text/javascript">

</script>
</head>
<body>
	<%
		String roleName = "";
		if (SecurityUtils.isAdmin()){
			roleName = "管理员";
		}else if (SecurityUtils.isAgent()){
			roleName = "代理商";
		}else if (SecurityUtils.isStore()){
			roleName = "商家";
		}else if (SecurityUtils.isSuperAccount()){
			roleName = "超级管理员";
		}
		pageContext.setAttribute("roleName", roleName);
	%>
	  
 <section>
        <div class="content" style="margin-left: -220px;margin-top: -110px;">
            <ul>
                <li class="li" >
                <div class="li-col" name="menu_1">
                    <a class="icon merchant" href="javascript:;" value="baseManager"></a>
                    <a href="javascript:;" value="baseManager"><i style="color:#EA8010;">基础信息</i></a>
                       	<ol>
                        	<li><span>基础信息可以设置商家的资料，连锁店铺组织 结构，收银机管理，登录账号管理等</span></li>
                        </ol>
                        
                </div>    
                </li>
                <li class="li" >
                <div class="li-col" name="menu_2">
                    <a class="icon commodity " href="javascript:;" value="productManager"></a>
                    <a href="javascript:;" value="productManager"><i style="color:#4DB159;">商品管理</i></a>
                    <ol>
                        	<li><span>商品管理可以设置商品资料，商品分类，批量导入商品信息等</span></li>
                    </ol>
                </div>
                </li>
                <li class="li" >
                <div class="li-col" name="menu_3">
                    <a class="icon sales" href="javascript:;" value="salesManager"></a>
                    <a href="javascript:;" value="salesManager"><i style="color:#56ABE4;">销售管理</i></a>
                    <ol>
                        	<li><span>销售管理可以查看门店销售记录，线上订单记录，促销折扣设置，优惠券设置等</span></li>
                    </ol>
                </div>
                </li>
               
            </ul>
            <ul>
                <li class="li" >
                <div class="li-col" name="menu_4">
                    <a class="icon member" href="javascript:;" value="userManager"></a>
                    <a href="javascript:;" value="userManager"><i style="color:#EB4F38;">会员管理</i></a>
                    <ol>
                            <li><span>会员管理可以管理商家的会员信息，会员等级，会员卡充值消费记录，会员导入等</span></li>
                    </ol>
                </div>
                </li>
                <li class="li" >
                <div class="li-col" name="menu_5">
                    <a class="icon procurement" href="javascript:;" value="buyerManager"></a>
                    <a href="javascript:;" value="buyerManager"><i style="color:#8E60AB;">采购管理</i></a>
                    <ol>
                            <li><span>采购管理可以设置供应商资料，采购单据，采购记录等</span></li>
                    </ol>
                </div>
                </li>
                <li class="li" >
                <div class="li-col" name="menu_6">
                    <a class="icon inventory" href="javascript:;" value="stockManager"></a>
                    <a href="javascript:;" value="stockManager"><i style="color:#00BB9C;">库存管理</i></a>
                    <ol>
                            <li><span>库存管理可以管理库存信息，商品入库，商品出库，库存调拨，库存盘点等</span></li>
                    </ol>
                </div>
                </li>
            </ul>
            <ul>
                <li class="li" >
                <div class="li-col" name="menu_7">
                    <a class="icon business" href="javascript:;" value="operationSummaryManager"></a>
                    <a href="javascript:;" value="operationSummaryManager"><i style="color:#7DC5EB;">经营报表</i></a>
                    <ol>
                            <li><span>经营报表可以查看销售统计，会员统计，商品销量，库存统计，交易分析等</span> </li>
                    </ol>
                </div>
                </li>
                <li class="li"  >
                <div class="li-col" name="menu_8">
                    <a class="icon account" href="javascript:;" value="settlementsManager"></a>
                    <a href="javascript:;" value="settlementsManager"><i style="color:#D62D80;">我的账户</i></a>
                    <ol>
                            <li><span>我的账户可以查看商家账号流水提现管理，账户余额，银行卡管理，支付密码修改等</span></li>
                    </ol>
                </div>
                </li>
                <li class="li">
                <div class="li-col">
                    <p class="icon help"></p>
                    <i style="color:#88147F;">帮助中心</i>
                    <ol>
                    	<li><span>帮助中心可以查看系统使用帮助文档，系统通知，问题反馈等</span></li>
                    </ol>
                </div>
                </li>
            </ul>
        </div> 
    </section>
</body>
<%} else { %>
</head>
<body></body>
<%} %>
</html>