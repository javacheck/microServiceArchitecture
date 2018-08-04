<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看商家信息</title> 
<style type="text/css">
input.file{
    vertical-align:middle;
    position:relative;
    left:-218px;
    filter:alpha(opacity=0);
    opacity:0;
	z-index:1;
	*width:223px;
}
</style>
<script type="text/javascript">

$(document).ready(function(){
	var isUpdate = false;
	if( null != $("#id").val() && "" != $("#id").val() ){ // 修改
		isUpdate = true;
		$("#onLineRateType").val('${store.onLineRateType}');
		
		// 送货上门方式
		var shipType = $("#shipTypeCache").val();
		var shipTypeArray = shipType.split(",");
		$("input:checkBox[name='shipTypeCheckBox']").each(function(key,value){
			for (var i = 0; i < shipTypeArray.length; i++) {
				if( shipTypeArray[i] == $(value).val() ){
					$(value).attr("checked",true);
					return ; // 匹配到了就不找了
				}
			}
		});
		$("#printer").val('${store.printer}');
		// 修改时判断交易类型是否是两种交易类型均可
		var payTypeValue =  $("#payType").val();
		if( payTypeValue == "2" ){
			$("[name = payTypeCheckBox]:checkbox").attr("checked",true);
		} else {
			
			$("[name = payTypeCheckBox]:checkbox").each(function(key,value){
				if( payTypeValue == $(value).val() ){
					$(value).attr("checked",true);
					return ; // 匹配到了就不找了
				}
			});
		}
		
		// 修改时判断营业状态
		$("input:radio[name='status']").each(function(key,value){
			 if( $("#statusCache").val() == $(value).val() ){
				 $(value).attr("checked",true);
				 return false;
			 }			
		});
		
		// 展示已经添加了的商家LOLO
		var logo = $("#logo");
		if( undefined != logo && null != logo.val() && "" != logo.val() ){
			$("#logoImagediv").show();
			$("#logoImage").attr("src","${picUrl }"+logo.val() );
			$("#logo").attr("type","hidden");
		} else {
			$("#logoImagediv").hide();
			$("#logo").val("没有商家LOGO");
		}
	}
	
	 // 检测交易类型
	 $("[name = payTypeCheckBox]:checkbox").bind("click", function () {
		  var isCheck = $('input:checkbox[name="payTypeCheckBox"]:checked').val();
		  if(isCheck == 1 ){ 
			  $("#payType").val(2);
		  } else {
			  $("#payType").val(0);
		  }
	  })
	  	  	   
	// 保存商家信息(每个填写的input框均需要isRequired字段，0为非必填字段。1为必填字段)
	$("#storeAddBtn").click(function(){	
		window.history.back(-1);  
	}); 	
});

</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>查看商家信息
			</strong>
		</div>
		<div class='panel-body'>
			<form id="shopSignFormSubmit" method='post' class='form-horizontal' autocomplete="off" action="" enctype="multipart/form-data" >
			
				<!-- 修改时传过来的商家ID -->
				<input id="id" name="id" type="hidden" value="${store.id }" />
				<input id="statusCache" name="statusCache" type="hidden" value="${store.status }" />
				<input id="shipTypeCache" name="shipTypeCache" type="hidden" value="${store.shipType }" />
				
				<div class="form-group">
					<label class="col-md-1 control-label">商家名称</label>
					<div class="col-md-2">
						<input type="text" id="name"  name="name" isRequired="1" tipName="商家名称" value="${store.name }" class='form-control' maxlength="50"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">商家简介</label>
					<div class="col-md-2">
						<textarea id="description"  name="description" isRequired="1" tipName="商家简介" value="${store.description }" cols=15 rows=5  class='form-control' maxlength="150">${store.description }</textarea>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">公司名称</label>
					<div class="col-md-2">
						<input type="text" id="companyName"  name="companyName" isRequired="1" tipName="公司名称" value="${store.companyName }" class='form-control' maxlength="50"/>
					</div>
				</div>
						
				<div class="form-group">
					<label class="col-md-1 control-label">商家类型</label>
					<div class="col-md-2">
						<input name="shopTypeName"  class="form-control" value="${store.shopTypeName }" isRequired="1" tipName="商家类型" id="shopTypeName" />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">支付类型</label>
					<div class="col-md-2">
						<input type="hidden" name="payType" id="payType" value="${store.payType }"/>
						<input type="checkbox" name="payTypeCheckBox" id="payTypeCheckBox" value="1"/>&nbsp;&nbsp;货到付款
						<input type="checkbox" name="payTypeCheckBox" id="payTypeCheckBox" value="0"/>&nbsp;&nbsp;在线支付
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">代理商名称</label>
					<div class="col-md-2">
						<input name="agentName" id="agentName"  value="${store.agentName }" class="form-control" isRequired="1" tipName="代理商名称" />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">合作者名称</label>
					<div class="col-md-2">
						<input name="partnerName" id="partnerName"  value="${store.partnerName }" class="form-control" isRequired="1" tipName="合作者名称" />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">总部名称</label>
					<div class="col-md-2">
						<input name="mainShopName" id="mainShopName"  value="${store.mainShopName }" class="form-control" isRequired="1" tipName="总部名称" />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">POS商户号</label>
					<div class="col-md-2">
						<input type="text" id="merchantNo"  name="merchantNo" isRequired="1" tipName="POS商户号" value="${store.merchantNo }" class='form-control'  maxlength="50"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">POS商户名称</label>
					<div class="col-md-2">
						<input type='text' id="merchantName"  name="merchantName" isRequired="1" tipName="POS商户名称" class='form-control' value="${store.merchantName }"  maxlength="50"/>
					</div>
				</div>
				
				<c:forEach items="${terminalIdArray }" varStatus="sortNo" var="terminalIdArray">
								<c:set var="v" value="${ sortNo.index}"></c:set>
									<div class="form-group" id="${v}">
									<label class="col-md-1 control-label">终端号</label>
									<div class="col-md-2">
										<input type="text" id="terminalId_${v}" onblur="checkTerminalId(this);" name="terminalIdArray" isRequired="0" tipName="终端号" value="${terminalIdArray.terminalId }" class='form-control' maxlength="50"/>
									</div>
									</div>
							</c:forEach>
							
				<div class="form-group">
					<label class="col-md-1 control-label">标准签约费率( % )</label>
					<div class="col-md-2">
						<input type='text' id="mcc"  name="mcc" isRequired="1" tipName="标准签约费率" fieldType="pRate" class='form-control' value="<fmt:formatNumber value="${store.mcc }" type="currency" pattern="0.00"/>"  maxlength="9"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">签约费率( % )</label>
					<div class="col-md-2">
						<input type='text' id="mccSign"  name="mccSign" isRequired="1" tipName="签约费率" fieldType="pRate" class='form-control' value="<fmt:formatNumber value="${store.mccSign }" type="currency" pattern="0.00"/>"  maxlength="9"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">收单成本费率( % )</label>
					<div class="col-md-2">
						<input type='text' id="mccCost"  name="mccCost" isRequired="1" tipName="收单成本费率" fieldType="pRate" class='form-control' value="<fmt:formatNumber value="${store.mccCost }" type="currency" pattern="0.00"/>"  maxlength="9"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">线上手续费率计算类型</label>
					<div class="col-md-2">
						<select id="onLineRateType" name="onLineRateType" class='form-control'> 
							<option id="0" value="0" selected="selected">比例计算</option>
							<option id="1" value="1">固定金额计算</option>
						</select>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>线上交易手续费率( % )</label>
					<div class="col-md-2">
						<input type='text' id="onLineRate"  name="onLineRate" isRequired="1" tipName="线上交易手续费率" fieldType="pRate" class='form-control' value="<fmt:formatNumber value="${store.onLineRate }" type="currency" pattern="0.00"/>" maxlength="9"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">收单开户行</label>
					<div class="col-md-2">
						<input type='text' id="posBankDeposit"  name="posBankDeposit" isRequired="1" tipName="收单开户行" class='form-control' value="${store.posBankDeposit }"  maxlength="50"/>
					</div>
				</div>
				
				
				<div class="form-group">
					<label class="col-md-1 control-label">收单银行账户</label>
					<div class="col-md-2">
						<input type='text' id="posBankAccount"  name="posBankAccount" isRequired="1" tipName="收单银行账户" fieldType="pInteger" class='form-control' value="${store.posBankAccount }" maxlength="19"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">收单账户名</label>
					<div class="col-md-2">
						<input type='text' id="posAccountName"  name="posAccountName" isRequired="1" tipName="收单账户名" class='form-control' value="${store.posAccountName }"  maxlength="50"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">收单银行行号</label>
					<div class="col-md-2">
						<input type='text' id="posBankLine"  name="posBankLine" isRequired="1" tipName="收单银行行号" fieldType="pInteger" class='form-control' value="${store.posBankLine }" maxlength="19"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label">使用的打印机</label>
					<div class='col-md-1'>
    					<select name="printer" id="printer" class="form-control" >
    							<option value="0" selected="selected">默认打印机</option>
    							<option value="1">映美打印机</option>
    					</select>
    				</div>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label">地址</label>
					<div class="col-md-2">
						<input type='text' id="address"  name="address" isRequired="1" tipName="地址" class='form-control' value="${store.address }" maxlength="200"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">联系人</label>
					<div class="col-md-2">
						<input type='text' id="contact"  name="contact" isRequired="1" tipName="联系人" class='form-control' value="${store.contact }"  maxlength="50"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">配送时间</label>
						<div class="col-md-1">
							<input type='text' id="startBusinessString"  name="startBusinessString" class='form-control' isRequired="1" tipName="配送时间" ignoreNull value="<fmt:formatDate value="${store.startBusinessDate }" pattern="HH:mm"/>"/>
						</div>
						<label class="" style="float: left;padding-top: 6px">至</label>
						<div class="col-md-1">
							<input type='text' id="endBusinessString"  name="endBusinessString" class='form-control' isRequired="1" tipName="配送时间" ignoreNull value="<fmt:formatDate value="${store.endBusinessDate }" pattern="HH:mm"/>"/>
						</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">营业状态</label>
					<div class="col-md-2">
						<input type="radio" name="status" id="status" checked="checked" value="1"/>&nbsp;&nbsp;营业
						<input type="radio" name="status" id="status" value="0"/>&nbsp;&nbsp;结业
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">起送金额( 元 )</label>
					<div class="col-md-2">
						<input type='text' id="minAmount"  name="minAmount" isRequired="0" tipName="起送金额" fieldType="pDouble" class='form-control' value="<fmt:formatNumber value="${store.minAmount }" type="currency" pattern="0.00"/>" maxlength="7"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">配送范围( 公里 )</label>
					<div class="col-md-2">
						<input type='text' id="shipRange"  name="shipRange" isRequired="0" tipName="配送范围" fieldType="pDouble" class='form-control' value="${store.shipRange }"  maxlength="50"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">送货费用( 元 )</label>
					<div class="col-md-2">
						<input type='text' id="shipAmount"  name="shipAmount" isRequired="0" tipName="送货费用" fieldType="pDouble" class='form-control' value="<fmt:formatNumber value="${store.shipAmount }" type="currency" pattern="0.00"/>" maxlength="7"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">免配送费限定额( 元 )</label>
					<div class="col-md-2">
						<input type='text' id="freeShipAmount"  name="freeShipAmount" isRequired="0" tipName="免配送费限定额" fieldType="pDouble" class='form-control' value="<fmt:formatNumber value="${store.freeShipAmount }" type="currency" pattern="0.00"/>" maxlength="7"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">服务</label>
					<div class="col-md-2">
						<input type='text' id="service"  name="service" isRequired="0" tipName="服务" class='form-control' value="${store.service }"  maxlength="200"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">送货方式</label>
					<div class="col-md-2">
						<input type="checkbox" name="shipTypeCheckBox" value="0"/>&nbsp;&nbsp;店铺送货上门&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="checkbox" name="shipTypeCheckBox" value="1"/>&nbsp;&nbsp;到店自提&nbsp;&nbsp;&nbsp;&nbsp;
						<br>
						<input type="checkbox" name="shipTypeCheckBox" value="2"/>&nbsp;&nbsp;快递&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="checkbox" name="shipTypeCheckBox" value="3"/>&nbsp;&nbsp;第三方配送&nbsp;&nbsp;&nbsp;&nbsp;
						<input type='hidden' id="shipType" name="shipType" class='form-control' value=""/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">送达时间</label>
					<div class="col-md-2">
						<input type='text' id="shipTime"  name="shipTime" isRequired="0" tipName="送达时间" class='form-control' value="${store.shipTime }"  maxlength="50"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">联系电话</label>
					<div class="col-md-2">
						<input type='text' id="phone"  name="phone" isRequired="0" tipName="联系电话" fieldType="phone" class='form-control' value="${store.phone }"  maxlength="15"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">手机号码</label>
					<div class="col-md-2">
						<input type='text' id="mobile"  name="mobile" isRequired="1" tipName="手机号码" fieldType="mobile" class='form-control' value="${store.mobile }"  maxlength="50"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">商家LOGO</label>
					<div id="divLogo" class="col-md-2">
						<input type='text' id="logo" name="logo"  isRequired="0" tipName="商家LOGO" class='form-control' value="${store.logo }"/>
						<div id="logoImagediv">
							<img alt="商家LOGO" style="cursor: pointer;" id="logoImage" name="logoImage" src="">
						</div>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"></label>
					<m:baimap mapContainerId="container" latitudeInputId="latitude" longitudeInputId="longitude"></m:baimap>
					<div id="container" style="width: 800px;height: 500px">
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" ></label>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">经度</label>
					<div class="col-md-2">
						<input type='text' id="longitude"  name="longitude" isRequired="1" tipName="经度" class='form-control' value="${store.longitude }"  maxlength="50"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">纬度</label>
					<div class="col-md-2">
						<input type='text' id="latitude"  name="latitude" isRequired="1" tipName="纬度" class='form-control' value="${store.latitude }"  maxlength="50"/>
					</div>
				</div>
				
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button"  id='storeAddBtn' class='btn btn-primary' value="返回列表页面" data-loading='稍候...' />
					</div>
				</div>
			</form>
	  </div>
   </div>
</body>
</html>