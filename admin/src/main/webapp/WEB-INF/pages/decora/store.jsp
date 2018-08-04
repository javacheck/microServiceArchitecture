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
<title>莱麦商户后台 - <decora:title /></title>
<script src="${staticPath }/js/jquery-1.11.2.min.js"></script>

<link rel="stylesheet" href="${staticPath }/css/datetimepicker.min.css"><!-- 日期JS -->
<link rel="stylesheet" href="${staticPath }/css/zui.min.css">
<link rel="stylesheet" type="text/css" href="${staticPath }/css/shouye.css" />
<link rel="stylesheet" href="${staticPath }/css/slider.css"><!-- 滑块JS -->

<!-- validator验证框架导入开始  -->
<link rel="stylesheet" href="${staticPath }/css/jquery.validator.css">
<script type="text/javascript" src="${staticPath }/js/jquery.validator.js"></script>
<script type="text/javascript" src="${staticPath }/js/zh_CN.validator.js"></script>
<!-- validator验证框架导入结束  -->
<script type="text/javascript" src="${staticPath }/js/layer/layer.js"></script>
<link rel="stylesheet" href="${staticPath }/css/layer/skin/layer.css">
<script type="text/javascript" src="${staticPath }/js/layer/layer.js"></script>
<style>
.user-control-nav {
	margin-bottom: 20px;
}

@media ( max-width : 480px) {
	.hidden-xxs {
		display: none
	}
	.page {
		font-size: 12px
	}
}
@media ( max-width : 400px) {
	.hidden-xxxs {
		display: none
	}
}

.panel {
	margin: 0 15px 0 0;
}

.panel-actions>form {
	max-width: 400px;
}

.panel-actions>form .input-group {
	margin-top: 3px;
}

.panel-actions>form .input-group .btn {
	margin: 0;
}
</style>
<script type="text/javascript">
	var config = {}
</script>

<script src="${staticPath }/js/zui.min.js"></script>
<!--[if lt IE 9]>
<script src='${staticPath }/js/all.ie8.js' type='text/javascript'></script>
<![endif]-->
<!--[if lt IE 10]>
<script src='${staticPath }/js/all.ie9.js' type='text/javascript'></script>
<![endif]-->
<script type="text/javascript" src="${staticPath }/js/common.js"></script>
<script type="text/javascript" src="${staticPath }/js/jquery.pager.js"></script>
<script type="text/javascript" src="${staticPath }/js/datetimepicker.min.js"></script>
<script type="text/javascript" src="${staticPath }/js/list_page.js"></script>
<script type="text/javascript">

	$(function() {
		$("#left_menu").hide();
		
		$("#main-leftmenu").css("max-height",$(document).height() - 150);
		
		$(window).resize( function(){
			$("#main-leftmenu").css("max-height",$(document).height() - 150);
		});
		
		
		$(".li").hover(function() {
	        $(this).children("ul").show(); 
	    }, function() {
	        $(this).children("ul").hide(); 
	    });
		
		$(".li").find("a").click(function(){
			var name = ($(this).parent().attr("name"));
			var value = ($(this).attr("value"));
			$("#mainNavbar").find("a[value='"+value+"']").css('background', '#0078A8');
			$(this).css('background', '#0078A8');
			$(this).parent().parent().children().each(function(){
				
				var thisValue = $(this).find("a").attr("value");
				if (thisValue != value){
					$(this).find("a").css('background', '');
				}
				
			});
			$("[name='_main_menu_ul']").find("li").hide();
			$("#left_menu").show();
			
			$("[name='_main_menu_ul']").find("li[name='"+name+"']").show();
			$("[name='_main_menu_ul']").find("li[name='"+name+"']").find("li").show();
			
			$("[name='_main_menu_ul']").find("li[name='"+name+"']").each(function(){
				if ($(this).hasClass('show')){
					$(this).click();
				}
			});
			
			var m = $("[name='_main_menu_ul']").find("a[value='"+value+"']");
			if (!m.parent().hasClass('show')){
				m.click();
			}
			$("#main-content").html('');
			$("a[name='main-submenu']").parent().removeClass("active");
		});
		var submenu = null;
		$("a[name='main-submenu']").each(function(i, v) {
			var contrastHref = $(v).attr("contrastHref");
			if (location.href.indexOf(contrastHref) != -1) {
				if(location.href.indexOf("/mainShop/") != -1 && contrastHref == "${contextPath }/shop/"){
					return; // 如果是总部请求,但是在商家标签循环来的时候，并不展开商家标签，直接continue 2016-01-10
				}
				if(location.href.indexOf("/myStore/") != -1 && contrastHref == "${contextPath }/shop/"){
					return;
				}
				if(location.href.indexOf("/payaccount/list-p") != -1 && contrastHref == "${contextPath }/payaccount/"){
					return;
				}
				
				if(location.href.indexOf("/userAccountManager/upgrade/list") != -1 && contrastHref == "${contextPath }/userAccountManager/"){
					return;
				}
				
				if(location.href.indexOf("/commodityManager/uploadList/list") != -1 && contrastHref == "${contextPath }/commodityManager/"){
					return;
				}
				
				if(location.href.indexOf("/userAccountManager/uploadList/list") != -1){
					$("a[name='main-submenu']").parent().removeClass("active");
					submenu = $(v);
					submenu.parent().addClass("active");
					return;
				}
				
				submenu = $(v);
				submenu.parent().addClass("active");
			}
			
			// 因库存列表属于商品列表中的子项，而此子项并未定义在此main.jsp页面中，此事例属于特例。
			// 为了使点击库存列表项页面时也展开至商品列表项，故加入如下的判断。2015-05-28
			if(location.href.indexOf("${contextPath }/productStock/uploadImage/") != -1 
					&& contrastHref == "${contextPath }/product/"){
				submenu = $(v);
				submenu.parent().addClass("active");
			}
			if(location.href.indexOf("${contextPath }/salesPromotion/") != -1 
					&& contrastHref == "${contextPath }/salesPromotionCategory/"){
				submenu = $(v);
				submenu.parent().addClass("active");
			}
			
		});

		$("ul[name='_main_menu_ul']").find("ul").each(function(){
			if ($.trim($(this).html()) == ""){
				$(this).parent().remove();
			}
		});
		
		if (submenu != null) {
			submenu.parent().parent().parent().find("a").click();
			var name = submenu.parent().parent().parent().attr('name');
			
			$("[name='_main_menu_ul']").find("li").hide();
			
			$("[name='_main_menu_ul']").find("li[name='"+name+"']").show();
			$("[name='_main_menu_ul']").find("li[name='"+name+"']").find("li").show();
			
			$("#left_menu").show();
		}
		
		$("#main_account_mobile").click(function(event) {
	        var dropdown = $("#main_account_menu");
	        if (dropdown.css("display")=="none") {
	            dropdown.slideDown("fast");
	        } else{
	            dropdown.slideUp("fast");
	        };
	    });
		
		
		var ctx = "${contextPath}";
		var wlh=window.location.href;
		var a=wlh.indexOf(ctx)+ctx.length;
		var b=wlh.substr(a,wlh.length);
		$("#mainNavbar").find("a[name='main-submenu']").each(function(){
			var href = $(this).attr("href");
			if(b!='/'){
				if (href.indexOf(b) != -1){
					$(this).parent().parent().parent().children().first().css('background', '#0078A8');
				}
			}
		});
	});
</script>
<decora:head />
<body style="min-width: 1570px;">
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
	<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation" id="mainNavbar" style="position: absolute;min-width: 1570px;">
        <div class="collapse navbar-collapse navbar-ex1-collapse" style="min-width: 1540px;min-height: 50px;">
            <div class="logo">
                <img src="${staticPath }/images/LOGO2-07.png" alt="" />
                <p>莱麦商户后台</p>
            </div>
            <ul class="nav navbar-nav navbar-left">
                <li>
                    <a  href="${contextPath }">首页</a> 
                </li>
                <li class="li" name="menu_1">
                    <a href="javascript:;" value="baseManager">基础信息</a>
                    <ul>
                    	<m:hasPermission permissions="shopList">
	                    	<span name="displayFlag">&nbsp;&nbsp;商家管理</span>
	                    	<m:hasPermission permissions="shopList">
								<li><a name="main-submenu" contrastHref="${contextPath }/shop/" href="${contextPath }/shop/list">商家列表 </a></li>
							</m:hasPermission>
							<li><a name="main-submenu" contrastHref="${contextPath }/shop/myStore/" href="${contextPath }/shop/myStore/">店铺设置</a></li>
							<m:hasPermission permissions="informList">
							<li><a name="main-submenu" contrastHref="${contextPath }/inform/" href="${contextPath }/inform/list">广播系统管理 </a></li>
							</m:hasPermission>
						</m:hasPermission>
						<m:hasPermission permissions="organizationList">
                    	<li>&nbsp;&nbsp;组织架构管理</li>
                    	<m:hasPermission permissions="organizationList">
							<li><a name="main-submenu" contrastHref="${contextPath }/organization/" href="${contextPath }/organization/list">组织结构管理</a></li>
						</m:hasPermission>
						</m:hasPermission>
						<m:hasPermission permissions="employeeList">
	                    	<span name="displayFlag">&nbsp;&nbsp;员工管理</span>
	                    	<m:hasPermission permissions="employeeList">
								<li><a name="main-submenu" contrastHref="${contextPath }/employee/" href="${contextPath }/employee/list">员工管理 </a></li>
							</m:hasPermission>
						</m:hasPermission>
						<m:hasPermission permissions="deviceList,userScreenAdList">
							<span name="displayFlag">&nbsp;&nbsp;终端管理</span>	
							<m:hasPermission permissions="deviceList">
								<li><a name="main-submenu" contrastHref="${contextPath }/device/" href="${contextPath }/device/list">终端列表 </a></li>
							</m:hasPermission>	
							<m:hasPermission permissions="userScreenAdList">
								<li><a name="main-submenu" contrastHref="${contextPath }/userScreenAd/" href="${contextPath }/userScreenAd/list">客屏广告列表 </a></li>
							</m:hasPermission>
						</m:hasPermission>
						<m:hasPermission permissions="accountList,roleList">
	                    	<span name="displayFlag">&nbsp;&nbsp;登录账号管理</span>
	                    	<m:hasPermission permissions="accountList">
								<li><a name="main-submenu" contrastHref="${contextPath }/account" href="${contextPath }/account/list">登录账号列表</a></li>
							</m:hasPermission>
							<m:hasPermission permissions="roleList">
								<li><a name="main-submenu" contrastHref="${contextPath }/role" href="${contextPath }/role/list">角色列表</a></li>
							</m:hasPermission>
						</m:hasPermission>
                    </ul>
                </li>
                <li class="li" name="menu_2">
                    <a href="javascript:;" value="productManager">商品管理</a>
                    <ul>
                    	<m:hasPermission permissions="productCategoryList,productAttributeList,productAttributeValueList,productUnitList,productStockList,productList,productImport,productBrandList,productSynchronizationList">
	                    	<span name="displayFlag">&nbsp;&nbsp;商品管理</span>
	                    	<%-- <m:hasPermission permissions="productCategoryList">
								<li><a name="main-submenu" contrastHref="${contextPath }/productCategory/manager"href="${contextPath }/productCategory/manager">分类管理 </a></li>
							</m:hasPermission> --%>
							
							
							<li><a name="main-submenu" contrastHref="${contextPath }/commodityManager/" href="${contextPath }/commodityManager/list">商品列表</a></li>
							<li><a name="main-submenu" contrastHref="${contextPath }/commodityCategory/" href="${contextPath }/commodityCategory/list">商品分类</a></li>
										
							<%-- <m:hasPermission permissions="productAttributeList">
								<li><a name="main-submenu" contrastHref="${contextPath }/productAttribute/" href="${contextPath }/productAttribute/list">属性列表 </a></li>
							</m:hasPermission>
							<m:hasPermission permissions="productAttributeValueList">
								<li><a name="main-submenu" contrastHref="${contextPath }/productAttributeValue/"href="${contextPath }/productAttributeValue/list">属性值列表 </a></li>
							</m:hasPermission> --%>
							<m:hasPermission permissions="productBrandList">
								<li><a name="main-submenu" contrastHref="${contextPath }/productBrand/" href="${contextPath }/productBrand/list">商品品牌 </a></li>
							</m:hasPermission>
							<m:hasPermission permissions="productUnitList">
								<li><a name="main-submenu" contrastHref="${contextPath }/productUnit/"href="${contextPath }/productUnit/list">商品单位 </a></li>
							</m:hasPermission>
							<li><a name="main-submenu" contrastHref="${contextPath }/commodityManager/uploadList/" href="${contextPath }/commodityManager/uploadList/list">商品导入</a></li>
							<%-- <m:hasPermission permissions="productStockList">
									<!-- 此处href链接最后保留/，便于公共方法遍历匹配(部分全匹配) -->
								<li><a name="main-submenu" contrastHref="${contextPath }/productStock/" href="${contextPath }/productStock/list">商品列表 </a></li>
							</m:hasPermission>
							<m:hasPermission permissions="productList">
									<!-- 此处href链接最后保留/，便于公共方法遍历匹配(部分全匹配) -->
								<li><a name="main-submenu" contrastHref="${contextPath }/product/" href="${contextPath }/product/list">库存列表 </a></li>
							</m:hasPermission>
							<m:hasPermission permissions="productImport">
								<li><a name="main-submenu" contrastHref="${contextPath }/productUpload/list" href="${contextPath }/productUpload/list">商品导入 </a></li>
							</m:hasPermission> --%>
							
							<m:isMainStore> 
								<m:hasPermission permissions="productSynchronizationList">
									<li><a name="main-submenu" contrastHref="${contextPath }/productSynchronization/" href="${contextPath }/productSynchronization/list">商品同步</a></li>
								</m:hasPermission>
							</m:isMainStore>
						</m:hasPermission>
						<%-- <m:hasPermission permissions="roomList,roomCategoryList,roomBookingList,roomPackageList,roomOpenRecordList,informList">
	                    	<span name="displayFlag">&nbsp;&nbsp;包间管理</span>
	                    	<m:hasPermission permissions="roomList">
								<li><a name="main-submenu" contrastHref="${contextPath }/room/" href="${contextPath }/room/list">包间管理 </a></li>
							</m:hasPermission>
							<m:hasPermission permissions="roomCategoryList">
								<li><a name="main-submenu" contrastHref="${contextPath }/roomCategory/" href="${contextPath }/roomCategory/list">包间类型 </a></li>
							</m:hasPermission>
							<m:hasPermission permissions="roomBookingList">
								<li><a name="main-submenu" contrastHref="${contextPath }/roomBooking/" href="${contextPath }/roomBooking/list">预订管理 </a></li>
							</m:hasPermission>
							<m:hasPermission permissions="roomPackageList">
								<li><a name="main-submenu" contrastHref="${contextPath }/roomPackage/" href="${contextPath }/roomPackage/list">套餐设置 </a></li>
							</m:hasPermission>
							<m:hasPermission permissions="roomOpenRecordList">
								<li><a name="main-submenu" contrastHref="${contextPath }/roomOpenRecord/" href="${contextPath }/roomOpenRecord/list">包间交易记录 </a></li>
							</m:hasPermission>
							<m:hasPermission permissions="informList">
							<li><a name="main-submenu" contrastHref="${contextPath }/inform/" href="${contextPath }/inform/list">广播系统管理 </a></li>
							</m:hasPermission>
						</m:hasPermission> --%>
                    </ul>
                </li>
                <li class="li" name="menu_3">
                    <a href="javascript:;" value="salesManager">销售管理</a>
                    <ul>
                    		<m:hasPermission permissions="orderAppList,orderList,returnGoodsRecordList">
		                    	<span name="displayFlag">&nbsp;&nbsp;订单管理</span>
		                    	<m:hasPermission permissions="orderAppList">
									<li><a name="main-submenu" contrastHref="${contextPath }/orderApp/list" href="${contextPath }/orderApp/list">线上订单列表</a></li>
								</m:hasPermission>
		                    	<m:hasPermission permissions="orderList">
									<li><a name="main-submenu" contrastHref="${contextPath }/order/list" href="${contextPath }/order/list">POS收银列表</a></li>
								</m:hasPermission>
								<m:hasPermission permissions="returnGoodsRecordList">
									<li><a name="main-submenu" contrastHref="${contextPath }/returnGoodsRecord/list" href="${contextPath }/returnGoodsRecord/list">退货记录</a>
									</li>
								</m:hasPermission>
								<m:hasPermission permissions="afterSalesList">
									<li><a name="main-submenu" contrastHref="${contextPath }/afterSales/" href="${contextPath }/afterSales/list">售后记录</a>
									</li>
								</m:hasPermission>
							</m:hasPermission>
							<m:hasPermission permissions="promotionList,promotionCouponList">
		                    	<li>&nbsp;&nbsp;促销管理</li>
		                    	<m:hasPermission permissions="promotionList">
									<li><a name="main-submenu" contrastHref="${contextPath }/promotion/" href="${contextPath }/promotion/list">促销列表</a></li>
								</m:hasPermission>
								<m:hasPermission permissions="promotionCouponList">
									<li><a name="main-submenu" contrastHref="${contextPath }/promotionCoupon/" href="${contextPath }/promotionCoupon/list">优惠券列表</a></li>
								</m:hasPermission>
							</m:hasPermission>
                    </ul>
                </li>
                <li class="li" name="menu_4">
                    <a href="javascript:;" value="userManager">会员管理</a>
                    <ul>
                    	<m:hasPermission permissions="userList,userLevelDefinitionList,userAccountManagerList,userCardRecordList,uploadList,pointRuleList,userUpdateMobileRecordList">
		                    <span name="displayFlag">&nbsp;&nbsp;会员管理</span>
	                    	<m:hasPermission permissions="userList">
								<li><a name="main-submenu" contrastHref="${contextPath }/user/list" href="${contextPath }/user/list">会员列表</a></li>
							</m:hasPermission>
							<m:hasPermission permissions="userLevelDefinitionList">
								<li><a name="main-submenu" contrastHref="${contextPath }/userLevelDefinition/" href="${contextPath }/userLevelDefinition/list">会员等级管理</a></li>
							</m:hasPermission>
							<m:hasPermission permissions="userAccountManagerList">
								<li><a name="main-submenu" contrastHref="${contextPath }/userAccountManager/" href="${contextPath }/userAccountManager/list">会员账户管理</a></li>
								<li><a name="main-submenu" contrastHref="${contextPath }/userAccountManager/upgrade/list" href="${contextPath }/userAccountManager/upgrade/list">可升级会员列表
										</a></li>
							</m:hasPermission>
							<m:hasPermission permissions="userCardRecordList">
								<li><a name="main-submenu" contrastHref="${contextPath }/userCardRecord/" href="${contextPath }/userCardRecord/list">卡交易记录</a></li>
							</m:hasPermission>
							<m:hasPermission permissions="uploadList">
								<li><a name="main-submenu" contrastHref="${contextPath }/userAccountManager/uploadList/list" href="${contextPath }/userAccountManager/uploadList/list">会员导入</a></li>
							</m:hasPermission>
							<m:hasPermission permissions="pointRuleList">
								<li><a name="main-submenu" contrastHref="${contextPath }/pointRule/" href="${contextPath }/pointRule/list">积分规则</a></li>
							</m:hasPermission>
							<m:hasPermission permissions="userUpdateMobileRecordList">
								<li><a name="main-submenu" contrastHref="${contextPath }/userUpdateMobileRecord/" href="${contextPath }/userUpdateMobileRecord/list">会员手机号修改记录</a></li>
							</m:hasPermission>
						</m:hasPermission>
                    </ul>
                </li>
                
                 <li class="li" name="menu_4">
                    <a href="javascript:;" value="storePackageManager">套餐管理</a>
                    <ul>
		                    <span name="displayFlag">&nbsp;&nbsp;套餐管理</span>
	                    	<m:hasPermission permissions="storePackageList">
								<li><a name="main-submenu" contrastHref="${contextPath }/storePackage/" href="${contextPath }/storePackage/list">套餐列表</a></li>
							</m:hasPermission>
                    </ul>
                </li>
                
                <li class="li" name="menu_5">
                    <a href="javascript:;"  value="buyerManager">采购管理</a>
                    <ul>
                    	<m:hasPermission permissions="supplierList,purchaseAcceptanceList">
							<m:hasPermission permissions="purchaseAcceptanceList">
								<li><a name="main-submenu" contrastHref="${contextPath }/purchaseAcceptance/" href="${contextPath }/purchaseAcceptance/list">采购验收</a></li>
							</m:hasPermission>
							<m:hasPermission permissions="supplierList">
								<li><a name="main-submenu" contrastHref="${contextPath }/supplier/" href="${contextPath }/supplier/list">供应商列表</a></li>
							</m:hasPermission>
						</m:hasPermission>
					</ul>
                </li>
                <li class="li" name="menu_6">
                    <a href="javascript:;" value="stockManager">库存管理</a>
                    <ul>
                    	<m:hasPermission permissions="allocationConfigList,allocationList,stockCheckList,productStockList,productStorageRecordList,productStorageRecordList">
		                    <span name="displayFlag">&nbsp;&nbsp;库存管理</span>
		                    <m:hasPermission permissions="productStockList">
							<!-- v2新的库存列表 -->
								<li><a name="main-submenu" contrastHref="${contextPath }/v2/productStock"
									href="${contextPath }/v2/productStock/list">库存列表 </a></li>
							</m:hasPermission>
		                    
								<m:hasPermission permissions="productStorageRecordList">
								<!-- v2新的库存列表 -->
									<li><a name="main-submenu" contrastHref="${contextPath }/productStorageRecord"
										href="${contextPath }/productStorageRecord/list">商品入库 </a></li>
								</m:hasPermission>
								<m:hasPermission permissions="productDeliveryRecordList">
								<!-- v2新的库存列表 -->
									<li><a name="main-submenu" contrastHref="${contextPath }/productDeliveryRecord"
										href="${contextPath }/productDeliveryRecord/list">商品出库 </a></li>
								</m:hasPermission>
							<m:isMainStore>
								<m:hasPermission permissions="allocationConfigList">
									<li><a name="main-submenu" contrastHref="${contextPath }/allocationConfig/" href="${contextPath }/allocationConfig/list">库存调拨权限设置</a></li>
								</m:hasPermission>
							</m:isMainStore>
							
							<m:acountType accountType="chainstore">
								<m:hasPermission permissions="allocationList">
									<li><a name="main-submenu" contrastHref="${contextPath }/allocation/" href="${contextPath }/allocation/list">库存调拨</a></li>
								</m:hasPermission>
							</m:acountType>
							<m:hasPermission permissions="stockCheckList">
								<li><a name="main-submenu" contrastHref="${contextPath }/stockCheck/" href="${contextPath }/stockCheck/list">库存盘点记录</a></li>
							</m:hasPermission>
						</m:hasPermission>
					</ul>
                </li>
                <li class="li" name="menu_7" >
                    <a href="javascript:;" value="operationSummaryManager">经营报表</a>
                   	<ul>
                   		<m:hasPermission permissions="operationSummaryList,reportSalesList,reportUserList,reportProductList,reportStockList,transactionAnalysisList,workrecord">
		                   	<span name="displayFlag">&nbsp;&nbsp;经营报表</span>
							<m:hasPermission permissions="operationSummaryList">
								<li><a name="main-submenu" contrastHref="${contextPath }/operationSummary/" href="${contextPath }/operationSummary/list">经营汇总表</a></li>
							</m:hasPermission>
							<m:acountType accountType="mainStore">
								<m:hasPermission permissions="reportSalesList">
									<li><a name="main-submenu" contrastHref="${contextPath }/reportSales/" href="${contextPath }/reportSales/list">商品销售统计</a></li>
								</m:hasPermission>
							</m:acountType>
							<m:acountType accountType="store">
								<m:hasPermission permissions="reportSalesList">
									<li><a name="main-submenu" contrastHref="${contextPath }/reportSales/" href="${contextPath }/reportSales/storeList">商品销售统计</a></li>
								</m:hasPermission>
							</m:acountType>
							<m:acountType accountType="mainStore">
								<m:hasPermission permissions="reportUserList">
								<li><a name="main-submenu" contrastHref="${contextPath }/reportUser/" href="${contextPath }/reportUser/list">会员统计</a></li>
								</m:hasPermission>
							</m:acountType>
							<m:acountType accountType="store">
								<m:hasPermission permissions="reportUserList">
								<li><a name="main-submenu" contrastHref="${contextPath }/reportUser/" href="${contextPath }/reportUser/storeList">会员统计</a></li>
								</m:hasPermission>
							</m:acountType>
							<m:hasPermission permissions="reportProductList">
								<li><a name="main-submenu" contrastHref="${contextPath }/reportProduct/" href="${contextPath }/reportProduct/list">商品销量统计</a></li>
							</m:hasPermission>
							<m:hasPermission permissions="reportStockList">
								<li><a name="main-submenu" contrastHref="${contextPath }/reportStock/" href="${contextPath }/reportStock/list">库存统计</a></li>
							</m:hasPermission>
							<m:hasPermission permissions="transactionAnalysisList">
								<li><a name="main-submenu" contrastHref="${contextPath }/transactionAnalysis/" href="${contextPath }/transactionAnalysis/list">交易分析</a></li>
							</m:hasPermission>
							<m:hasPermission permissions="workrecord">
								<li><a name="main-submenu" contrastHref="${contextPath }/workrecord/" href="${contextPath }/workrecord/list">交接班记录</a></li>
							</m:hasPermission>
							<m:hasPermission permissions="returnGoodsRecordStatisticalList">
							<li><a name="main-submenu" contrastHref="${contextPath }/returnGoodsRecordStatistical/list" href="${contextPath }/returnGoodsRecordStatistical/list">退货商品统计</a>
									</li>
									</m:hasPermission>
						</m:hasPermission>
					</ul>
                </li>
                <li class="li" name="menu_8">
                    <a href="javascript:;" value="settlementsManager">我的账户</a>
                    <ul>
                    	<m:hasPermission permissions="settlementsRecordList,withdrawList,balanceList,businessbankList,payaccountList">
	                    	<span name="displayFlag">&nbsp;&nbsp;我的账户</span>
							<m:hasPermission permissions="settlementsRecordList">
								<li><a name="main-submenu" contrastHref="${contextPath }/settlementsRecord/" href="${contextPath }/settlementsRecord/list">账户流水记录 </a></li>
							</m:hasPermission>
							<m:hasPermission permissions="withdrawList">
								<li><a name="main-submenu" contrastHref="${contextPath }/withdraw/withdrawData/" href="${contextPath }/withdraw/withdrawData/list">提现</a></li>
								<li><a name="main-submenu" contrastHref="${contextPath }/withdraw/expenditure/" href="${contextPath }/withdraw/expenditure/list">提现流水记录</a></li>
							</m:hasPermission>
							<m:hasPermission permissions="balanceList">
								<li><a name="main-submenu" contrastHref="${contextPath }/payaccount/list-p/" href="${contextPath }/payaccount/list-p">账户余额</a></li>
							</m:hasPermission>
							<m:hasPermission permissions="businessbankList">
								<li><a name="main-submenu" contrastHref="${contextPath }/businessbank/" href="${contextPath }/businessbank/list">银行卡管理</a></li>
							</m:hasPermission>
							<m:hasPermission permissions="payaccountList">
								<li><a name="main-submenu" contrastHref="${contextPath }/payaccount/" href="${contextPath }/payaccount/list">支付密码管理</a></li>
							</m:hasPermission>
						</m:hasPermission>
					</ul>
                </li>
                <li class="li">
                    <a href="javascript:;">帮助中心</a>
                    <ul>
                    <span name="displayFlag">正在开发中，敬请期待</span>
                	</ul>
                </li>
            </ul>
            <ul class="nav navbar-nav navbar-right" style="margin-right:20px;">
                <li style="margin-top: 14px;color: white;">
                    <p>${store_session_key.name}</p>
                    <p id="main_account_mobile" class="acc" style="cursor:pointer;">${account_session_key.mobile }<em></em></p>
                </li> 
                <li style="position: static;"> 
                    <ul class="dropdown-menu" id="main_account_menu">
                        <li><a href='#updatePasswordModal' data-toggle="modal">修改密码</a></li>
                        <li><a href='${contextPath }/logout'>退出</a></li>
                    </ul>
                </li>
            </ul>

        </div>
    </nav>


	<div class="clearfix row-main" style="margin-top: 100px;min-width: 1000px;">
		<div class='col-xs-1' id="left_menu" style="display: none;">
			<div class="leftmenu affix" id="main-leftmenu"  style="min-width: 200px;max-height: 1000px;overflow:auto;">
				<nav class="menu" data-toggle="menu" >
				<ul class="nav nav-primary" name="_main_menu_ul">
					<%
						if (SecurityUtils.isStore()){
					%>
						<li name="menu_1"><a href='#' value="baseManager"><i class="icon-th"></i><b>商家管理</b></a>
							<ul class='nav'>
								<m:hasPermission permissions="shopList">
									<li><a name="main-submenu" contrastHref="${contextPath }/shop/"
										href="${contextPath }/shop/list">商家列表 </a></li>
								</m:hasPermission>
								<li><a name="main-submenu" contrastHref="${contextPath }/shop/myStore/" href="${contextPath }/shop/myStore/">店铺设置</a></li>
								<m:hasPermission permissions="informList">
									<li><a name="main-submenu" contrastHref="${contextPath }/inform/" href="${contextPath }/inform/list">广播系统管理 </a></li>
								</m:hasPermission>
							</ul>
						</li>
						
						<li name="menu_1"><a href='#' value="baseManager"><i class="icon-key"></i><b>组织结构管理</b></a>
							<ul class='nav'>
								<m:hasPermission permissions="organizationList">
									<li><a name="main-submenu" contrastHref="${contextPath }/organization/" href="${contextPath }/organization/list">组织结构管理
									</a></li>
								</m:hasPermission>
							</ul>
						</li>
						
						<li name="menu_1"><a href='#' value="baseManager"><i class="icon-th"></i><b>员工管理</b></a>
							<ul class='nav'>
							<m:hasPermission permissions="employeeList">
							<li>
								<a name="main-submenu" contrastHref="${contextPath }/employee/"
										href="${contextPath }/employee/list">员工管理 </a></li>
							</m:hasPermission>									
							</ul>
						</li>
						<li name="menu_1"><a href='#' value="baseManager"><i class="icon-th"></i><b>终端管理</b></a>
							<ul class='nav'>
							<m:hasPermission permissions="deviceList">
								<li><a name="main-submenu" contrastHref="${contextPath }/device/" href="${contextPath }/device/list">终端列表 </a></li>
							</m:hasPermission>
							<m:hasPermission permissions="userScreenAdList">
								<li><a name="main-submenu" contrastHref="${contextPath }/userScreenAd/" href="${contextPath }/userScreenAd/list">客屏广告列表 </a></li>
							</m:hasPermission>									
							</ul>
						</li>
						<li name="menu_1"><a href='#' value="baseManager"><i class="icon-key"></i><b>登录账号管理</b></a>
							<ul class='nav'>
								<m:hasPermission permissions="accountList">
									<li><a name="main-submenu" contrastHref="${contextPath }/account" href="${contextPath }/account/list">登录账号列表
									</a></li>
								</m:hasPermission>
	
								<m:hasPermission permissions="roleList">
									<li><a name="main-submenu" contrastHref="${contextPath }/role" href="${contextPath }/role/list">角色列表
									</a></li>
								</m:hasPermission>
							</ul>
						</li>
						
						<li name="menu_2" style="margin-top: 0;"><a href='#' value="productManager"><i class="icon-th"></i><b>商品管理</b></a>
							<ul class='nav'>
								<%-- <m:hasPermission
									permissions="productCategoryList">
									<li><a name="main-submenu" contrastHref="${contextPath }/productCategory/manager"
										href="${contextPath }/productCategory/manager">分类管理 </a></li>
								</m:hasPermission> --%>
								
								
								<li><a name="main-submenu" contrastHref="${contextPath }/commodityManager/" href="${contextPath }/commodityManager/list">商品列表</a></li>
								<li><a name="main-submenu" contrastHref="${contextPath }/commodityCategory/" href="${contextPath }/commodityCategory/list">商品分类</a></li>
								<m:hasPermission permissions="productBrandList">
								<li><a name="main-submenu" contrastHref="${contextPath }/productBrand/"
									href="${contextPath }/productBrand/list">商品品牌 </a></li>
								</m:hasPermission>		
								<%-- <m:hasPermission permissions="productAttributeList">
									<li><a name="main-submenu" contrastHref="${contextPath }/productAttribute/"
										href="${contextPath }/productAttribute/list">属性列表 </a></li>
								</m:hasPermission>
	
								<m:hasPermission permissions="productAttributeValueList">
									<li><a name="main-submenu" contrastHref="${contextPath }/productAttributeValue/"
										href="${contextPath }/productAttributeValue/list">属性值列表 </a></li> 
								</m:hasPermission>--%>
								<m:hasPermission permissions="productUnitList">
									<li><a name="main-submenu" contrastHref="${contextPath }/productUnit/"href="${contextPath }/productUnit/list">商品单位 </a></li>
								</m:hasPermission>
								<li><a name="main-submenu" contrastHref="${contextPath }/commodityManager/uploadList/" href="${contextPath }/commodityManager/uploadList/list">商品导入</a></li>
								<%--< m:hasPermission permissions="productStockList">
								<!-- 此处href链接最后保留/，便于公共方法遍历匹配(部分全匹配) -->
									<li><a name="main-submenu" contrastHref="${contextPath }/productStock/"
										href="${contextPath }/productStock/list">商品列表 </a></li>
								</m:hasPermission>
								<m:hasPermission permissions="productList">
								<!-- 此处href链接最后保留/，便于公共方法遍历匹配(部分全匹配) -->
									<li><a name="main-submenu" contrastHref="${contextPath }/product/"
										href="${contextPath }/product/list">库存列表 </a></li>
								</m:hasPermission>
								<m:hasPermission permissions="productImport">
									<li><a name="main-submenu" contrastHref="${contextPath }/productUpload/list"
										href="${contextPath }/productUpload/list">商品导入 </a></li>
								</m:hasPermission> --%>
								
								
								<m:isMainStore> 
									<m:hasPermission permissions="productSynchronizationList">
										<li><a name="main-submenu" contrastHref="${contextPath }/productSynchronization/" href="${contextPath }/productSynchronization/list">商品同步</a></li>
									</m:hasPermission>
								</m:isMainStore>
							</ul>
						</li>
						
						<%-- <li name="menu_2"><a href='#' value="productManager"><i class="icon-th"></i><b>包间管理</b></a>
							<ul class='nav'>
							<m:hasPermission permissions="roomList">
							<li>
								<a name="main-submenu" contrastHref="${contextPath }/room/" href="${contextPath }/room/list">包间管理 </a>
							</li>
							</m:hasPermission>
							<m:hasPermission permissions="roomCategoryList">
							<li>
								<a name="main-submenu" contrastHref="${contextPath }/roomCategory/" href="${contextPath }/roomCategory/list">包间类型 </a>
							</li>
							</m:hasPermission>
							<m:hasPermission permissions="roomBookingList">
							<li>
								<a name="main-submenu" contrastHref="${contextPath }/roomBooking/" href="${contextPath }/roomBooking/list">预订管理 </a>
							</li>
							</m:hasPermission>
								<m:hasPermission permissions="roomPackageList">
							<li>
								<a name="main-submenu" contrastHref="${contextPath }/roomPackage/" href="${contextPath }/roomPackage/list">套餐设置 </a>
								</li>
								</m:hasPermission>
									<m:hasPermission permissions="roomOpenRecordList">
							<li>
								<a name="main-submenu" contrastHref="${contextPath }/roomOpenRecord/" href="${contextPath }/roomOpenRecord/list">包间交易记录 </a>
								</li>
								</m:hasPermission>
								<m:hasPermission permissions="informList">
								<li>
									<a name="main-submenu" contrastHref="${contextPath }/inform/" href="${contextPath }/inform/list">广播系统管理 </a>
								</li>
								</m:hasPermission>
							</ul >
						</li>--%>
						
						<li name="menu_3" style="margin-top: 0;"><a href='#' value="salesManager"><i class="icon-file"></i><b>订单管理</b></a>
							<ul class='nav'>
								<m:hasPermission permissions="orderAppList">
									<li><a name="main-submenu" contrastHref="${contextPath }/orderApp/list" href="${contextPath }/orderApp/list">线上订单列表</a>
									</li>
								</m:hasPermission>
							
								<m:hasPermission permissions="orderList">
									<li><a name="main-submenu" contrastHref="${contextPath }/order/list" href="${contextPath }/order/list">POS收银列表</a>
									</li>
								</m:hasPermission>
							
							<m:hasPermission permissions="returnGoodsRecordList">
									<li><a name="main-submenu" contrastHref="${contextPath }/returnGoodsRecord/list" href="${contextPath }/returnGoodsRecord/list">退货记录</a>
									</li>
							</m:hasPermission>
							<m:hasPermission permissions="afterSalesList">
									<li><a name="main-submenu" contrastHref="${contextPath }/afterSales/" href="${contextPath }/afterSales/list">售后记录</a>
									</li>
							</m:hasPermission>
							
							</ul>
							
						</li>
						
						<li name="menu_3" style="margin-top: 0;"><a href='#' value="salesManager"><i class="icon-file"></i><b>促销管理</b></a>
							<ul class='nav'>
								<m:hasPermission permissions="promotionList">
									<li><a name="main-submenu" contrastHref="${contextPath }/promotion/" href="${contextPath }/promotion/list">促销列表</a>
									</li>
								</m:hasPermission>
								<m:hasPermission permissions="promotionCouponList">
									<li>
										<a name="main-submenu" contrastHref="${contextPath }/promotionCoupon/" href="${contextPath }/promotionCoupon/list">优惠券列表</a>
									</li>
								</m:hasPermission>
							</ul>
						</li>
						
						<li name="menu_4" style="margin-top: 0;"><a href='#' value="userManager"><i class="icon-user"></i><b>会员管理</b></a>
							<ul class='nav'>
								<m:hasPermission permissions="userList">
									<li><a name="main-submenu" contrastHref="${contextPath }/user/list" href="${contextPath }/user/list">会员列表
									</a></li>
								</m:hasPermission>
								<m:hasPermission permissions="userLevelDefinitionList">
									<li><a name="main-submenu" contrastHref="${contextPath }/userLevelDefinition/" href="${contextPath }/userLevelDefinition/list">会员等级管理
									</a></li>
								</m:hasPermission>
								<m:hasPermission permissions="userAccountManagerList">
									<li><a name="main-submenu" contrastHref="${contextPath }/userAccountManager/" href="${contextPath }/userAccountManager/list">会员账户管理
									</a></li>
									<li><a name="main-submenu" contrastHref="${contextPath }/userAccountManager/upgrade/list" href="${contextPath }/userAccountManager/upgrade/list">可升级会员列表
									</a></li>
								</m:hasPermission>
								<m:hasPermission permissions="userCardRecordList">
									<li><a name="main-submenu" contrastHref="${contextPath }/userCardRecord/" href="${contextPath }/userCardRecord/list">卡交易记录
									</a></li>
								</m:hasPermission>
								<m:hasPermission permissions="uploadList">
									<li><a name="main-submenu" contrastHref="${contextPath }/userAccountManager/uploadList/list" href="${contextPath }/userAccountManager/uploadList/list">会员导入
									</a></li>
								</m:hasPermission>
								<m:hasPermission permissions="pointRuleList">
									<li>
										<a name="main-submenu" contrastHref="${contextPath }/pointRule/" href="${contextPath }/pointRule/list">积分规则</a>
									</li>
								</m:hasPermission>
								<m:hasPermission permissions="userUpdateMobileRecordList">
								<li><a name="main-submenu" contrastHref="${contextPath }/userUpdateMobileRecord/" href="${contextPath }/userUpdateMobileRecord/list">会员手机号修改记录</a></li>
								</m:hasPermission>
							</ul>
						</li>
						
						
						<li name="menu_4" style="margin-top: 0;"><a href='#' value="storePackageManager"><i class="icon-user"></i><b>套餐管理</b></a>
							<ul class='nav'>
								<m:hasPermission permissions="storePackageList">
									<li><a name="main-submenu" contrastHref="${contextPath }/storePackage/" href="${contextPath }/storePackage/list">套餐列表
									</a></li>
								</m:hasPermission>
							</ul>
						</li>
						
						
						<li name="menu_5" style="margin-top: 0;"><a href='#' value="buyerManager"><i class="icon-th"></i><b>采购管理</b></a>
							<ul class='nav'>
								<m:hasPermission permissions="purchaseAcceptanceList">
									<li><a name="main-submenu" contrastHref="${contextPath }/purchaseAcceptance/" href="${contextPath }/purchaseAcceptance/list">采购验收</a></li>
								</m:hasPermission>
								<m:hasPermission permissions="supplierList">
									<li>
										<a name="main-submenu" contrastHref="${contextPath }/supplier/" href="${contextPath }/supplier/list">供应商列表</a>
									</li>
								</m:hasPermission>
							</ul>
						</li>
						
						<li name="menu_6" style="margin-top: 0;"><a href='#' value="stockManager"><i class="icon-file"></i><b>库存管理</b></a>
							
							<ul class='nav'>
								<m:hasPermission permissions="productStockList">
								<!-- v2新的库存列表 -->
									<li><a name="main-submenu" contrastHref="${contextPath }/v2/productStock"
										href="${contextPath }/v2/productStock/list">库存列表 </a></li>
								</m:hasPermission>
									<m:hasPermission permissions="productStorageRecordList">
									<!-- v2新的库存列表 -->
										<li><a name="main-submenu" contrastHref="${contextPath }/productStorageRecord"
											href="${contextPath }/productStorageRecord/list">商品入库 </a></li>
									</m:hasPermission>
									<m:hasPermission permissions="productDeliveryRecordList">
									<!-- v2新的库存列表 -->
										<li><a name="main-submenu" contrastHref="${contextPath }/productDeliveryRecord"
											href="${contextPath }/productDeliveryRecord/list">商品出库 </a></li>
									</m:hasPermission>
								<%
									if (SecurityUtils.isMainStore()){
								%>
								<m:hasPermission permissions="allocationConfigList">
									<li><a name="main-submenu" contrastHref="${contextPath }/allocationConfig/" href="${contextPath }/allocationConfig/list">库存调拨权限设置</a>
									</li>
								</m:hasPermission>
								<%} %>
								<%if(SecurityUtils.isChainStore()) {%>
								<m:hasPermission permissions="allocationList">
									<li><a name="main-submenu" contrastHref="${contextPath }/allocation/" href="${contextPath }/allocation/list">库存调拨</a>
									</li>
								</m:hasPermission>
								<%} %>
								<m:hasPermission permissions="stockCheckList">
									<li><a name="main-submenu" contrastHref="${contextPath }/stockCheck/" href="${contextPath }/stockCheck/list">库存盘点记录</a></li>
								</m:hasPermission>
							</ul>
						</li>
						
						<li name="menu_7" style="margin-top: 0;"><a href='#'  value="operationSummaryManager"><i class="icon-th"></i><b>经营报表</b></a>
							<ul class='nav'>
								<m:hasPermission permissions="operationSummaryList">
									<li>
										<a name="main-submenu" contrastHref="${contextPath }/operationSummary/" href="${contextPath }/operationSummary/list">经营汇总表</a>
									</li>
								</m:hasPermission>
								
								<%
									if (SecurityUtils.isMainStore()){
								%>
									<m:hasPermission permissions="reportSalesList">
									<li>
										<a name="main-submenu" contrastHref="${contextPath }/reportSales/" href="${contextPath }/reportSales/list">商品销售统计</a>
									</li>
									</m:hasPermission>
								<%}else{ %>
									<m:hasPermission permissions="reportSalesList">
									<li>
										<a name="main-submenu" contrastHref="${contextPath }/reportSales/" href="${contextPath }/reportSales/storeList">商品销售统计</a>
									</li>
								</m:hasPermission>
								<% } %>
								<%
									if (SecurityUtils.isMainStore()){
								%>
									<m:hasPermission permissions="reportUserList">
									<li>
										<a name="main-submenu" contrastHref="${contextPath }/reportUser/" href="${contextPath }/reportUser/list">会员统计</a>
									</li>
									</m:hasPermission>
								<%}else{ %>
									<m:hasPermission permissions="reportUserList">
									<li>
										<a name="main-submenu" contrastHref="${contextPath }/reportUser/" href="${contextPath }/reportUser/storeList">会员统计</a>
									</li>
									</m:hasPermission>
								<% } %>
								
								<m:hasPermission permissions="reportProductList">
									<li>
										<a name="main-submenu" contrastHref="${contextPath }/reportProduct/" href="${contextPath }/reportProduct/list">商品销量统计</a>
									</li>
								</m:hasPermission>
								<m:hasPermission permissions="reportStockList">
									<li>
										<a name="main-submenu" contrastHref="${contextPath }/reportStock/" href="${contextPath }/reportStock/list">库存统计</a>
									</li>
								</m:hasPermission>
								<m:hasPermission permissions="transactionAnalysisList">
									<li>
										<a name="main-submenu" contrastHref="${contextPath }/transactionAnalysis/" href="${contextPath }/transactionAnalysis/list">交易分析</a>
									</li>
								</m:hasPermission>
								<m:hasPermission permissions="workrecord">
									<li>
										<a name="main-submenu" contrastHref="${contextPath }/workrecord/" href="${contextPath }/workrecord/list">交接班记录</a>
									</li>
								</m:hasPermission>
								<m:hasPermission permissions="returnGoodsRecordStatisticalList">
									<li><a name="main-submenu" contrastHref="${contextPath }/returnGoodsRecordStatistical/list" href="${contextPath }/returnGoodsRecordStatistical/list">退货商品统计</a>
									</li>
									</m:hasPermission>
							</ul>
						</li>
						
						<li name="menu_8" style="margin-top: 0;"><a href='#' value="settlementsManager"><i class="icon-file"></i><b>我的账户</b></a>
						<ul class='nav'>
							<m:hasPermission permissions="settlementsRecordList">
							<li><a name="main-submenu" contrastHref="${contextPath }/settlementsRecord/"
									href="${contextPath }/settlementsRecord/list">账户流水记录 </a></li>
							</m:hasPermission>
							<m:hasPermission permissions="withdrawList">
							<li><a name="main-submenu" contrastHref="${contextPath }/withdraw/withdrawData/"
										href="${contextPath }/withdraw/withdrawData/list">提现</a></li>
							<li><a name="main-submenu" contrastHref="${contextPath }/withdraw/expenditure/" 
							href="${contextPath }/withdraw/expenditure/list">提现流水记录</a>
							</li>
							</m:hasPermission>
							<m:hasPermission permissions="balanceList">
							<li><a name="main-submenu" contrastHref="${contextPath }/payaccount/list-p" 
							href="${contextPath }/payaccount/list-p">账户余额</a>
							</li>
							</m:hasPermission>
							<m:hasPermission permissions="businessbankList">
								<li><a name="main-submenu" contrastHref="${contextPath }/businessbank/"
										href="${contextPath }/businessbank/list">银行卡管理</a></li>
							</m:hasPermission>
							<m:hasPermission permissions="payaccountList">
								<li><a name="main-submenu" contrastHref="${contextPath }/payaccount/"
										href="${contextPath }/payaccount/list">支付密码管理</a></li>
							</m:hasPermission>
							
						</ul>
					</li>	
					<% } %>	
				</ul>
				</nav>
			</div>
		</div>
		<div class='col-xs-10.5' id="main-content" style="margin-left: 220px;">
			<decora:body />
		</div>
	</div>

	<div class="modal fade" id="LMConfirmModal">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span><span class="sr-only">关闭</span>
					</button>
					<h4 class="modal-title" id="LMConfirmModalTitle"></h4>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal"
						id="LMConfirmCloseBtn">取消</button>
					<button type="button" class="btn btn-primary" id="LMConfirmBtn">确定</button>
				</div>
			</div>
		</div>
	</div>
	
	
	<div class="modal fade" id="updatePasswordModal">
		<div class="modal-dialog ">
		  <div class="modal-content">
		    <div class="modal-header">
		      <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
		      <h4 class="modal-title"></h4>
		    </div>
		    <div class="modal-body">
		      <form class="form-horizontal" role="form" method="post" id="updatePasswordForm">
		          <div class="form-group">
		            <label class="col-md-2 control-label">旧密码</label>
		            <div class="col-md-6">
		               <input  type="password" name="oldPassword" class="form-control">
		            </div>
		          </div>
		          <div class="form-group">
		            <label class="col-md-2 control-label">新密码</label>
		            <div class="col-md-6">
		               <input type="password" name="password" class="form-control">
		            </div>
		          </div>
		          <div class="form-group">
		            <label class="col-md-2 control-label">确认新密码</label>
		            <div class="col-md-6">
		               <input type="password" name="password2" class="form-control">
		            </div>
		          </div>
		        </form>
		    </div>
		    <div class="modal-footer">
		      <button type="button" class="btn btn-default" data-dismiss="modal" name="closeBtn">关闭</button>
		      <button type="button" class="btn btn-primary" id="updatePasswordBtn">修改</button>
		    </div>
		  </div>
		</div>
	</div>
	
	<script type="text/javascript">
		
		$(function(){
			$("#updatePasswordForm").find("[name='password']").blur(function() {//用户名文本框失去焦点触发验证事件
				if (!lm.isPassword($(this).val())) {
					$(this).val('');
					$(this).focus();
					lm.alert("密码不能为空且只能为英文或者数字")
				}
			});
			$("#updatePasswordForm").find("[name='password2']").blur(function() {//用户名文本框失去焦点触发验证事件
				if (!lm.isPassword($(this).val())) {
					$(this).val('');
					lm.alert("密码不能为空且只能为英文或者数字")
				}
			});
			$("#updatePasswordBtn").click(function(){
				var oldPassword = $("#updatePasswordForm").find("[name='oldPassword']").val();
				var password = $("#updatePasswordForm").find("[name='password']").val();
				var password2 = $("#updatePasswordForm").find("[name='password2']").val();
				if ($.trim(oldPassword) == ""){
					lm.alert("请输入旧密码");
					return;
				}
				
				if ($.trim(password) == ""){
					lm.alert("请输入新密码");
					return;
				}
				
				if (password != password2){
					lm.alert("两次输入密码不一致");
					return;
				}
				
				//验证密码长度
					if (password.length < 6	||password.length > 18) {
						lm.alert("密码长度为6-18");
						return;
					}
				
				lm.post("${contextPath}/update-password",{password:password,oldPassword:oldPassword},function(data){
					if (data == "1"){
						$("#updatePasswordModal").find("[name='closeBtn']").click();
						lm.noty("修改成功");
						$("#updatePasswordForm").find("input").val("");
					}else if (data == "2"){
						lm.alert("旧密码不对");
					}
				});
			});
		});
	</script>

</body>
</html>