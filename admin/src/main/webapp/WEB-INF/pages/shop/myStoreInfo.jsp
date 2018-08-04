<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>关于商家</title> 
<script type="text/javascript">
	function showQRcode(){
		$('#aas').modal();//弹窗
	}
</script>
<style>
.divInfo{border-bottom:1px solid #ccc;padding:18px 10px ;margin-bottom:0;}
.columnClass{color:#8d8d8d;font-weight:400;font-style:normal;font-size:14px;margin-bottom:0;width:150px;display:block;float:left; }
.columnRightClass{color:#8d8d8d;color:#459ae9;font-weight:400;font-style:normal;font-size:14px;margin-bottom:0;width:110px;display:block;float:right; }
.contentClass{font-style:normal;font-weight:400;font-size:14px;margin-bottom:0;}
</style>
</head>
<body>
	<div class='panel' >
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>本店信息
					<c:if test="${isMainStore != null }">
						<m:hasPermission permissions="shopEdit">
							<a href='${contextPath }/shop/myStore/update/${store.id}' >[修改]</a>
						</m:hasPermission> 
					</c:if>
			</strong>
		</div>
		<div class='panel-body'>
			<form id="shopSignFormSubmit" method='post' class='form-horizontal'  action="${contextPath }/shop/save" enctype="multipart/form-data"  style="font-style:normal;">
				<div  class="form-group divInfo" >
					<label class=" columnClass"  >商标</label>
					<label class=" contentClass"  ><img style="width:132px;height:88px;" src="${store.logo }" ></label>
				</div>
				<div  class="form-group divInfo" >
					<label class=" columnClass"  >二维码</label>
					<label class=" contentClass"  ><a href="${contextPath }/qrcode/born?content=http://192.168.80.57:8080/api/wap/register/?storeId=${store.id}&qrcode=340&logo=96"><img style="width:150px;height:150px;" src="${contextPath }/qrcode/born?content=http://192.168.80.57:8080/api/wap/register/?storeId=${store.id}" ></a></label>
					<label class="columnRightClass"  ><a onclick="showQRcode()">下载更多尺寸</a></label>
				</div>
				<div  class="form-group divInfo" >
					<label class="columnClass"  >店铺名称</label>
					<label class="contentClass " >${store.name }</label>
				</div>
				<div class="form-group divInfo" >
					<label class="columnClass ">店铺类型</label>
					<label class="contentClass ">${store.shopTypeName }</label>
				</div>
				<div class="form-group divInfo">
					<label class="columnClass">店铺地址</label>
					<label class="contentClass">${store.address }</label>
				</div>
				<div class="form-group divInfo">
					<label class="columnClass">总部统一积分规则</label>
					<label class="contentClass">${store.unifiedPointRule == 0 ? '同意使用总部积分规则' : '不使用总部积分规则' }</label>
				</div>
				<div class="form-group divInfo">
					<label class="columnClass">购物车商品删除要权限</label>
					<label class="contentClass">${store.posCartAuthority == 0 ? '否' : '是' }</label>
				</div>
				<div class="form-group divInfo">
					<label class="columnClass">小票重打印</label>
					<label class="contentClass">${store.receiptReprint == 0 ? '关闭' : '开启' }</label>
				</div>
				<div class="form-group divInfo">
					<label class="columnClass">小票打印数</label>
					<label class="contentClass">${store.receiptPrintAmount == 1 ? '1联' : '2联' }</label>
				</div>
				<div class="form-group divInfo">
					<label class="columnClass">联系人</label>
					<label class="contentClass">${store.contact }</label>
				</div>
				<div class="form-group divInfo">
					<label class="columnClass">配送时间</label>
					<label class="contentClass">${store.businessTime }</label>
				</div>
				<div class="form-group divInfo">
					<label class="columnClass">营业状态</label>
					<label class="contentClass">${store.status==0 ? '结业' :'营业' }</label>
				</div>
				<div class="form-group divInfo">
					<label class="columnClass">起送金额</label>
					<label class="contentClass"><fmt:formatNumber value="${store.minAmount }" type="currency" pattern="0.00"/></label>
				</div>
				<div class="form-group divInfo">
					<label class="columnClass">送货费用(元)</label>
					<label class="contentClass"><fmt:formatNumber value="${store.shipAmount }" type="currency" pattern="0.00"/></label>
				</div>
				<div class="form-group divInfo">
					<label class="columnClass">免配送费限定额(元)</label>
					<label class="contentClass"><fmt:formatNumber value="${store.freeShipAmount }" type="currency" pattern="0.00"/></label>
				</div>
				<div class="form-group divInfo">
					<label class="columnClass">联系电话</label>
					<label class="contentClass">${store.phone }</label>
				</div>
				<div class="form-group divInfo">
					<label class="columnClass">手机号码</label>
					<label class="contentClass">${store.mobile }</label>
				</div>
				<div class="form-group divInfo">
					<label class="columnClass">代理商名称</label>
					<label class="contentClass">${store.agentName }</label>
				</div>
				
			</form>
			<div style="line-height: 1.6;color: #222;font-size: 14px" class="modal fade"  id="aas">
				<div class="modal-dialog "style="width: 960px">
				  <div class="modal-content">
				    <div class="modal-header">
				      <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
				      <h4 class="modal-title">更多尺寸</h4>
				    </div>
				    <div style="max-height: 485px;padding: 66px 45px 108px;" class="modal-body">
				    	<div style="padding: 20px 70px;">
					    	<table style="border: 1px solid #e7e7eb;overflow: hidden;background-color: #fff;text-align: center;border-top: 1px solid #e7e7eb;border-bottom: 1px solid #e7e7eb;border-spacing: 0;"   class="table datatable">
					    		<thead>
						    		<tr ><th  style="text-align:center;">二维码边长(cm)</th><th  style="text-align:center;" >建议扫描距离(米)</th><th  style="text-align:center;">下载链接</th> </tr>
					    		</thead>
					    		<tbody >
						    		<tr ><td>8cm </td>	<td>0.5m</td>	<td> 	<a href="${contextPath }/qrcode/born?content=http://192.168.80.57:8080/api/wap/register/?storeId=${store.id}&qrcode=227&logo=64"><i class="icon icon-circle-arrow-down"></i></a></td> </tr>
						    		<tr ><td>12cm</td>	<td>0.8m</td>	<td>	<a href="${contextPath }/qrcode/born?content=http://192.168.80.57:8080/api/wap/register/?storeId=${store.id}&qrcode=340&logo=96"><i class="icon icon-circle-arrow-down"></i></a></td> </tr>
						    		<tr ><td>15cm</td>	<td>1.0m</td>	<td>  	<a href="${contextPath }/qrcode/born?content=http://192.168.80.57:8080/api/wap/register/?storeId=${store.id}&qrcode=425&logo=120"><i class="icon icon-circle-arrow-down"></i></a></td> </tr>
						    		<tr ><td>30cm</td>	<td>1.5m</td>	<td>	<a href="${contextPath }/qrcode/born?content=http://192.168.80.57:8080/api/wap/register/?storeId=${store.id}&qrcode=850&logo=240"><i class="icon icon-circle-arrow-down"></i></a></td> </tr>
						    		<tr ><td>50cm</td>	<td>2.5m</td>	<td>	<a href="${contextPath }/qrcode/born?content=http://192.168.80.57:8080/api/wap/register/?storeId=${store.id}&qrcode=1417&logo=400"><i class="icon icon-circle-arrow-down"></i></a></td> </tr>
					    		</tbody>
					    	</table>
					    	<p>二维码尺寸请按照43像素的整数倍缩放，以保持最佳效果</p>
				    	</div>
				    </div>
				    <div  class="modal-footer">
				    	<div align="center">
				    	<span  style="min-width: 104px;padding: 0;background-color: #44b549;border-color: #44b549;color: #fff;display: inline-block;overflow: visible;  height: 30px;line-height: 30px;vertical-align: middle;text-align: center;text-decoration: none;-webkit-border-radius: 3px;font-size: 14px;border-width: 1px;border-style: solid;cursor: pointer;">
				    		<button  type="button" class="btn btn-success" data-dismiss="modal" style="-webkit-writing-mode: horizontal-tb;  width: 100%;  color: #fff;  display: block;  height: 100%;  background-color: transparent;  border: 0;outline: 0;  overflow: visible;padding: 0 22px;  cursor: pointer;  -webkit-appearance: button;  text-transform: none;font-family: inherit;font-size: 100%;margin: 0;  align-items: flex-start;text-align: center;box-sizing: border-box;  letter-spacing: normal;  word-spacing: normal;  text-indent: 0px;text-shadow: none;  -webkit-font-smoothing: antialiased;">关闭</button>
				    	</span>
				    	</div>
				    </div>
				  </div>
				</div>
			</div>
		</div>
   </div>
</body>
</html>