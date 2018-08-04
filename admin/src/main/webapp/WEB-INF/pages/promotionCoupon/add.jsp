<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty promotionCoupon.isUpdate ? '添加' : '修改' }优惠</title> 
<script type="text/javascript">
function callback(obj){
	$("#promotionCoupon_storeId").val(obj.name);
	$("#storeId").val(obj.id);
}
$(function(){
	$("#promotionCoupon_storeId").click(function (){
		$("#promotionCouponStore").modal();
	});
	$("#cancelStoreBtn").click(function(){
		$("[name='storeId']").val('');
		$("[name='storeName']").val('');
	});
	var format = "yyyy-MM-dd";//默认格式
	var now = lm.Now;//当前日期
	var nowFormat=now.Format(format);
	$("#beginDate").datetimepicker({
		minView: "month", //选择日期后，不会再跳转去选择时分秒 
		format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
		language: 'zh-CN', //汉化 
		autoclose:true , //选择日期后自动关闭
		startDate:now,
		todayBtn:true,//可选择当天按钮
		todayHighlight:false//高亮当前日期
	 });
	$("#LastDate").datetimepicker({
		minView: "month", //选择日期后，不会再跳转去选择时分秒 
		format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
		language: 'zh-CN', //汉化 
		autoclose:true , //选择日期后自动关闭
		startDate:now,
		todayBtn:true,//可选择当天按钮
		todayHighlight:false//高亮当前日期
	 });
	var lastDateInput=nowFormat;
	//判断输入结束日期时候输入合法
	$('#LastDate').datetimepicker().on('changeDate', function(ev){
		if((new Date($('#LastDate').val()))<(new Date($('#beginDate').val().replace(/-/g,"/")))){//开始时间大于结束时间
			lm.alert("结束时间不能大于开始时间");
			$("#LastDate").val($("#LastDate").val());//默认值上一次输入
			$('#LastDate').datetimepicker('update');//更新
		}
		lastDateInput= $("#LastDate").val();
		$('#beginDate').datetimepicker('setEndDate', $("#LastDate").val());//设置开始时间最后可选结束时间
	});
	$("#minAmount").blur(function(){
        var value = $.trim($(this).val());
        if($("#minAmountFlag").html() == "最大折扣"){
    		if( parseFloat($.trim($("#minAmount").val())) > 100 ){
    			lm.alert("最大折扣不能大于100!");
    			$("#minAmount").focus();
    			return ;
    		}
    	}
        var length = $(this).val().length;
        if(value.indexOf(".")>0){
	        var first = value.indexOf(".");//判断第一个小数点所在位置
	        var last = value.lastIndexOf(".");//判断最后一个小数点所在的位置
	        var temp_length = value.split(".").length - 1;//含有.的个数
	        if(!isNaN(value) && (temp_length == 1) && (first==last) && (length - last <=3) ){
	            
	        }else{
	        	lm.alert("请输入小于两位小数的数字！");
	        }
        }
   });
	$("#maxAmount").blur(function(){
        var value = $.trim($(this).val());
        if($("#maxAmountFlag").html() == "最小折扣"){
    		if( parseFloat($.trim($("#maxAmount").val())) > 100 ){
    			lm.alert("最小折扣不能大于100!");
    			$("#maxAmount").focus();
    			return ;
    		}
    	}
        var length = $(this).val().length;
        if(value.indexOf(".")>0){
	        var first = value.indexOf(".");//判断第一个小数点所在位置
	        var last = value.lastIndexOf(".");//判断最后一个小数点所在的位置
	        var temp_length = value.split(".").length - 1;//含有.的个数
	        if(!isNaN(value) && (temp_length == 1) && (first==last) && (length - last <=3) ){
	           
	        }else{
	           lm.alert("请输入小于两位小数的数字！");
	        }
        }
   });

	$("#minAmountFlag").html("最大折扣");
	$("#maxAmountFlag").html("最小折扣");
	$("input:radio[name='type']").click(function(){
		if($(this).val() == 0 ){
			$("#minAmountFlag").html("最大折扣");
			$("#maxAmountFlag").html("最小折扣");
			$("#totalAmountFlag").hide();
		} else {
			$("#minAmountFlag").html("最小金额");
			$("#maxAmountFlag").html("最大金额");
			//$("#totalAmountFlag").show();
		}
	});
	
	var type="${promotionCoupon.type}";//类型
	if(type=="1"){
		$("input[name='type']").eq(1).prop("checked",true);
		$("#minAmountFlag").html("最小金额");
		$("#maxAmountFlag").html("最大金额");
		//$("#totalAmountFlag").show();
	}
	var shared="${promotionCoupon.shared}";//是否共享
	if(shared=="1"){
		$("input[name='shared']").eq(1).prop("checked",true);
	}
	var status="${promotionCoupon.status}";//是否有效
	if(status=="1"){
		$("input[name='status']").eq(1).prop("checked",true);
	}
	var range="${promotionCoupon.range}";//用户
	$("#range").find("option[value='"+range+"']").prop("selected",true);
}); 

function savePromotionCoupon(){
	//var id=$("#id").val();//优惠ID
	var storeId=$("#storeId").val();//商店
	storeId = $.trim(storeId);
	var isSys = '${isSys}';
	if(isSys == 'true'){
		if( null == storeId || "" == storeId){
			lm.alert("必须选择优惠活动的商家");
			return;
		}
	}
	
	var name=$("#name").val();//优惠名称
	if($.trim($("#name").val())=="" || $.trim($("#name").val())==null){
		lm.alert("活动名称不能为空！");
		$("#name").focus();
		return false;
	}
	var type=$("input:radio[name='type']:checked").val();//类型 
	var minAmount=$.trim($("#minAmount").val());//最大折扣（最小金额）
	var maxAmount=$.trim($("#maxAmount").val());//最小折扣（最大金额）
	if( $.trim($("#minAmount").val()) == "" || $.trim($("#minAmount").val()) == null ){
		lm.alert($("#minAmountFlag").html() + "不能为空");
		$("#minAmount").focus();
		return ;
	}
	if( $.trim($("#maxAmount").val()) == "" || $.trim($("#maxAmount").val()) == null ){
		lm.alert($("#maxAmountFlag").html() + "不能为空");
		$("#maxAmount").focus();
		return ;
	}
	if( !(lm.isFloat($.trim($("#minAmount").val())) && $.trim($("#minAmount").val()) > 0) ){
		lm.alert($("#minAmountFlag").html() + "输入错误！");
		$("#minAmount").focus();
		return ;
	}
	if( !(lm.isFloat($.trim($("#maxAmount").val())) && $.trim($("#maxAmount").val()) > 0) ){
		lm.alert($("#maxAmountFlag").html() + "输入错误！");
		$("#maxAmount").focus();
		return ;
	}
	if( !(/^[0-9]*(\.[0-9]{1,2})?$/.test($.trim($("#minAmount").val()))) ){
		lm.alert($("#minAmountFlag").html() + "输入错误！");
		$("#minAmount").focus();
		return ;
	}
	if( !(/^[0-9]*(\.[0-9]{1,2})?$/.test($.trim($("#maxAmount").val()))) ){
		lm.alert($("#maxAmountFlag").html() + "输入错误！");
		$("#maxAmount").focus();
		return ;
	}
	if($("#minAmountFlag").html() == "最大折扣"){
		if( parseFloat($.trim($("#minAmount").val())) > 100 ){
			lm.alert("最大折扣不能大于100!");
			$("#minAmount").focus();
			return ;
		}
	}
	if($("#maxAmountFlag").html() == "最小折扣"){
		if( parseFloat($.trim($("#maxAmount").val())) > 100 ){
			lm.alert("最小折扣不能大于100!");
			$("#maxAmount").focus();
			return ;
		}
	}
	if($("#maxAmountFlag").html() == "最小折扣"){
		if(parseFloat($.trim($("#maxAmount").val()))<parseFloat($.trim($("#minAmount").val()))){
			lm.alert($("#maxAmountFlag").html()+"不能小于"+$("#minAmountFlag").html());
			$("#maxAmountFlag").focus();
			return ;
		}
	}else{
		if(parseFloat(minAmount) > parseFloat(maxAmount)){
			lm.alert($("#minAmountFlag").html()+"不能大于"+$("#maxAmountFlag").html());
			$("#maxAmountFlag").focus();
			return ;
		}
	}
	
	var totalNum=$("#totalNum").val();//总数量
	totalNum = $.trim(totalNum);
	var issueType = $("#issueType").val();
	if(issueType == 2 ){
		if( null == totalNum || "" == totalNum ){
			lm.alert("线下发放时，总数量不能为空");
			return ;
		}
	}
	
	if(totalNum!="" && totalNum!=null){
		if(!IsNum(totalNum)){
			lm.alert("总数量必须为大于0的整数！");
			$("#totalNum").focus();
			return false;
		}else if(totalNum==0){
			lm.alert("总数量必须为大于0的整数！");
			$("#totalNum").focus();
			return false;
		}
		
		var isUpdate = $("#isUpdate").val();
		if( 1 == isUpdate ){ // 修改
			var totalNumCache = $("#totalNumCache").val();
			totalNumCache = $.trim(totalNumCache);
			if( null != totalNumCache && "" != totalNumCache ){
				if( totalNumCache - totalNum > 0 ){
					lm.alert("总数量不能小于之前的"+totalNumCache+"张");
					return false;
				}
			}
		}
	} else {
		lm.alert("总数量不能为空");
		return ;
	}
	
	var totalAmount=$("#totalAmount").val();//总额
	if(type==1){
		if($.trim($("#totalAmount").val())!="" && $.trim($("#totalAmount").val())!=null){
			if( !(lm.isFloat($.trim($("#totalAmount").val())) && $.trim($("#totalAmount").val()) > 0) ){
				lm.alert("总额输入错误！");
				$("#totalAmount").focus();
				return ;
			}
			if( !(/^[0-9]*(\.[0-9]{1,2})?$/.test($.trim($("#totalAmount").val()))) ){
				lm.alert("总额输入错误！");
				$("#totalAmount").focus();
				return ;
			}
		}
		
	}
	
	
	var beginDate=$("#beginDate").val();//开始时间
	if($.trim($("#beginDate").val())=="" || $.trim($("#beginDate").val())==null){
		lm.alert("开始时间不能为空！");
		$("#beginDate").focus();
		return false;
	}
	var LastDate=$("#LastDate").val();//结束时间
	if($.trim($("#LastDate").val())=="" || $.trim($("#LastDate").val())==null){
		lm.alert("结束时间不能为空！");
		$("#LastDate").focus();
		return false;
	}
	var range=$("#range option:selected").val(); //用户类型
	var shared=$("input:radio[name='shared']:checked").val();//是否共享
	var orderAmount=$("#orderAmount").val();//订单金额满
	if($.trim($("#orderAmount").val())=="" || $.trim($("#orderAmount").val())==null){
		lm.alert("订单金额满不能为空！");
		$("#orderAmount").focus();
		return false;
	}
	if( !(lm.isFloat($.trim($("#orderAmount").val())) && $.trim($("#orderAmount").val()) > 0) ){
		lm.alert("订单金额满输入错误！");
		$("#orderAmount").focus();
		return ;
	}
	
	if( !(/^[0-9]*(\.[0-9]{1,2})?$/.test($.trim($("#orderAmount").val()))) ){
		lm.alert("订单金额满输入错误！");
		$("#orderAmount").focus();
		return ;
	}
	var validDay=$("#validDay").val();//有效期
	if($.trim($("#validDay").val())=="" || $.trim($("#validDay").val())==null){
		lm.alert("有效期不能为空！");
		$("#validDay").focus();
		return false;
	}
	if(!IsNum(validDay)){
		lm.alert("有效期必须为大于0的整数！");
		$("#validDay").focus();
		return false;
	}else if(validDay==0){
		lm.alert("有效期必须为大于0的整数！");
		$("#validDay").focus();
		return false;
	}
	
	
	var memo=$("#memo").val();//说明
	var status=$("input:radio[name='status']:checked").val();//是否有效
	
	
	$("#promotionCouponAddBtn").prop("disabled","disabled");
	lm.post("${contextPath}/promotionCoupon/list/ajax/save",$("#promotionCouponSave").serialize(),function(data){
		
		if(data=='1'){
	    	lm.alert("操作成功！");
	    	 window.location.href="${contextPath}/promotionCoupon/list";
		}
	}); 
		
}
//判断是否是正整数
function IsNum(s)
{
    if(s!=null){
        var r,re;
        re = /\d*/i; //\d表示数字,*表示匹配多个数字
        r = s.match(re);
        return (r==s)?true:false;
    }
    return false;
}

$(function(){
	var isUpdate = $("#isUpdate").val();
	if( 1 == isUpdate ){
		var issueType = "${promotionCoupon.issueType}";
		$("#issueType").val(issueType);
		var reportNum = "${promotionCoupon.reportNum}";
		var type = "${promotionCoupon.type}";//类型
		
		if( reportNum>0 ){
			if( type == 1 ){ // 现金券
				$("input[name='type']").eq(0).parent().hide(); // 隐藏折扣券	
			} else {
				$("input[name='type']").eq(1).parent().hide();
			}
		}
		
		if( issueType == 1 ){
			$("#urlDIV").show();
			if( reportNum>0 ){
				$("#issueType option[value='2']").remove();
			}
		} else if(issueType == 2 ){
			if( reportNum>0 ){
				$("#issueType option[value='1']").remove();
			}
			$("#urlDIV").hide();
		}
	} 
	
	$("#range").change(function(){
		var issueType = $("#issueType").val();
		if(issueType == 2 ){ // 线下发放
			$("#range").val(0); // 修改时设置线下发放只能针对所有用户
		}
	});
	
	$("#issueType").change(function(){
		var issueType = $("#issueType").val();
		if(issueType == 1 ){ // 手动领取
			$("#urlDIV").show();
		} else if(issueType == 2 ){ // 线下发放
			$("#range").val(0); // 修改时设置线下发放只能针对所有用户
			$("#urlDIV").hide();
		}
	});
	
	$("#copyURL").click(function(){
		var status=$("input:radio[name='status']:checked").val();//是否有效
		if( status == 1 ){ //状态为有效
			copyUrl2();			
		} else {
			lm.alert("状态为无效,不能复制");
		}
	});
	
});
function copyUrl2()
{
	var Url2=document.getElementById("URL");
	Url2.select(); // 选择对象
	document.execCommand("Copy"); // 执行浏览器复制命令
	alert("已复制好，可贴粘。");
}

$(function(){
	// 如果是现金券，则根据总数量和最大金额计算预计大约需要话费多少钱
	$("#totalNum").change(function(){
		counting($("#name"));
	});
	
	$("#maxAmount").change(function(){
		counting($("#name"));
	});
	
});

function counting(obj){
	var totalNum = $("#totalNum").val();
	totalNum = $.trim(totalNum);
	
	if( null != totalNum && "" != totalNum ){
		if(!IsNum(totalNum)){ // 不能为非数字
			return false;
		}else if(totalNum==0){ // 不能设置为0
			return false;
		}
		var type = $("input:radio[name='type']:checked").val();
		if( type == 1 ){ // 现金劵
			var maxAmount = $("#maxAmount").val();
			maxAmount = $.trim(maxAmount);
			if( null != maxAmount && "" != maxAmount ){
				if( !(lm.isFloat(maxAmount) && maxAmount > 0) ){
					return ;
				}
				if( !(/^[0-9]*(\.[0-9]{1,2})?$/.test(maxAmount)) ){
					return ;
				}
				tip = "此次活动最多花费的总金额大约为："+(totalNum*maxAmount).toFixed(2)+" 元";
				getTip(tip,obj);
			}
		}			
	}
}
function getTip(tip,obj){
	layer.tips(tip, obj, {
	    tips: [2, '#3595CC'], //还可配置颜色
	    time: 3000
	});
}
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
			<i class='icon-plust'></i>${empty promotionCoupon.isUpdate ? '添加' : '修改' }优惠
			</strong>
		</div>
		<div class='panel-body'>
			<form method='post'  id="promotionCouponSave" >
			<input id="isUpdate" name="isUpdate" type="hidden" value="${promotionCoupon.isUpdate }" />
				<div  class='form-horizontal'>
					<input id="id" name="id" type="hidden" value="${promotionCoupon.id }" />
					<c:if test="${isSys==true}">
						<c:if test="${promotionCoupon.isUpdate ==null }">
								<div class="form-group">
									<label class="col-md-1 control-label">商家</label>
									<div class="col-md-2">
										<input type="text" name="storeName" readonly="readonly" class='form-control' id = "promotionCoupon_storeId" value="${promotionCoupon.storeName }">
										<input type="hidden" name="storeId" class='form-control' id="storeId"  value="${promotionCoupon.storeId }">
									</div>
									<button type='button' class="btn btn-warning" id="cancelStoreBtn">取消商家</button>
								</div>
						</c:if>
						<c:if test="${promotionCoupon.isUpdate!=null}">
							<c:if test="${promotionCoupon.promotionStatus==2}">
								<div class="form-group">
								<label class="col-md-1 control-label">商家</label>
								<div class="col-md-2">
									<input type="text" name="storeName" readonly="readonly" class='form-control' id = "promotionCoupon_storeId" value="${promotionCoupon.storeName }">
									<input type="hidden" name="storeId" class='form-control' id="storeId"  value="${promotionCoupon.storeId }">
								</div>
								<button type='button' class="btn btn-warning" id="cancelStoreBtn">取消商家</button>
							</div>
						</c:if>
							<c:if test="${promotionCoupon.promotionStatus!=2}">
								<div class="form-group">
									<label class="col-md-1 control-label">商家</label>
									<div class="col-md-2">
										<input type="text"  readonly="readonly"  class='form-control'  value="${promotionCoupon.storeName }">
										<input type="hidden" name="storeId" class='form-control' id="storeId"  value="${promotionCoupon.storeId }">
									</div>
								</div>
							</c:if>
						</c:if>
					</c:if>
					
					<div class="form-group" >
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>活动名称</label>
						<div class="col-md-2">
							<input name="name" id="name"  value="${promotionCoupon.name }" class="form-control" maxlength="20"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>类型</label>
						<div class="col-md-2" style="margin-top: 6px;">
							<span>
								<input type="radio" name="type" id="type" checked="checked" value="0"/>&nbsp;&nbsp;折扣劵
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							</span>
							<span>
								<input type="radio" name="type" id="type" value="1"/>&nbsp;&nbsp;现金劵
							</span>
						</div>
					</div>
								
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span><span id="minAmountFlag"></span></label>
						<div class="col-md-2">
							<input type='text' id="minAmount" name="minAmount" placeholder="仅保留两位小数" class='form-control' value="<fmt:formatNumber value="${promotionCoupon.minAmount }" type="currency" pattern="0.00"/>" maxlength="7"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span><span id="maxAmountFlag"></span></label>
						<div class="col-md-2">
							<input type='text' id="maxAmount" name="maxAmount" placeholder="仅保留两位小数" class='form-control' value="<fmt:formatNumber value="${promotionCoupon.maxAmount }" type="currency" pattern="0.00"/>" maxlength="7"/>
						</div>
					</div>
					<div class="form-group" >
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>总数量(张)</label>
						<div class="col-md-2">
							<input name="totalNum" id="totalNum"  value="${promotionCoupon.totalNum==0?'':promotionCoupon.totalNum }" class="form-control" maxlength="7"/>
							<input name="totalNumCache" id="totalNumCache" type="hidden"  value="${promotionCoupon.totalNum==0?'':promotionCoupon.totalNum }" class="form-control" maxlength="7"/>
						</div>
					</div>
					
					<div class="form-group" id="totalAmountFlag" style="display:none;">
						<label class="col-md-1 control-label">总额</label>
						<div class="col-md-2">
							<input type='text' id="totalAmount" name="totalAmount" placeholder="仅保留两位小数" class='form-control' value="<fmt:formatNumber value="${promotionCoupon.totalAmount==0.0?'':promotionCoupon.totalAmount }" type="currency" pattern="0.00"/>" maxlength="7"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>发放开始时间</label>
						<div class="col-md-2">
							<input  readonly="readonly" type="text" id="beginDate" name="beginDate" value="<fmt:formatDate value="${promotionCoupon.startDate }" pattern="yyyy-MM-dd"/>"  class='form-control' />
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>发放结束时间</label>
						<div class="col-md-2">
							<input  readonly="readonly" type="text" id="LastDate" name="LastDate" value="<fmt:formatDate value="${promotionCoupon.endDate }" pattern="yyyy-MM-dd"/>"  class='form-control' />
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>发放用户</label>
						<div class='col-md-1'>
	    					<select name="range" id="range" class="form-control" style="width: auto;">
	    							<option value="0">所有用户</option>
	    							<option value="1">新注册用户</option>
	    							<option value="2">已注册用户</option>
	    					</select>
	    				</div>
					</div>
					
					<div class="form-group" style="display: none">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>是否共享</label>
						<div class="col-md-2" style="margin-top: 6px;">
							<input type="radio" name="shared" id="shared" checked="checked" value="0"/>&nbsp;&nbsp;不共享
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="radio" name="shared" id="shared" value="1"/>&nbsp;&nbsp;共享
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>订单金额满</label>
						<div class="col-md-2">
							<input type='text' id="orderAmount" name="orderAmount" placeholder="仅保留两位小数" class='form-control' value="<fmt:formatNumber value="${promotionCoupon.orderAmount }" type="currency" pattern="0.00"/>" maxlength="7"/>
						</div>
					</div>
					
					<div class="form-group" >
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>有效期（天）</label>
						<div class="col-md-2">
							<input name="validDay" id="validDay"  value="${promotionCoupon.validDay }" class="form-control" maxlength="3"/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-1 control-label">发放方式</label>
						<div class='col-md-1'>
	    					<select name="issueType" id="issueType" class="form-control" >
	    							<option value="1" selected="selected">手动领取</option>
	    							<option value="2">线下发放</option>
	    					</select>
	    				</div>
					</div>
					
					<div class="form-group" id="urlDIV" style="display: block">
						<label class="col-md-1 control-label">领取地址</label>
						<div class="col-md-2">
							<button id="copyURL" type="button" name="copyURL" class="btn btn-info" style="margin-top: 5px;" >
								<i class="icon icon-search"></i>复制链接
							</button>
							<input style="border:none;background:#fff; width:100%" type="text" id="URL" name="URL" value="${promotionCoupon.URL }"/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-1 control-label">说明</label>
						<div class="col-md-2">
							<textarea id="memo"  name="memo" value="${promotionCoupon.memo }" cols=15 rows=5  class='form-control' maxlength="150">${promotionCoupon.memo }</textarea>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>状态</label>
						<div class="col-md-2" style="margin-top: 6px;">
							<input type="radio" name="status" id="status" checked="checked" value="0"/>&nbsp;&nbsp;无效
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="radio" name="status" id="status" value="1"/>&nbsp;&nbsp;有效
						</div>
					</div>
					
					<div class="form-group">
						<div class="col-md-offset-1 col-md-1 0" style="float:left">
							<input type="button"  id='promotionCouponAddBtn' class='btn btn-primary' value="${empty promotionCoupon.isUpdate ? '添加' : '修改' }" onclick="savePromotionCoupon();" />
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
	<!-- 店铺弹窗标签 -->
	<m:select_store path="${contextPath}/shop/showModle/list/list-data" modeId="promotionCouponStore" callback="callback"> </m:select_store>
</body>
</html>