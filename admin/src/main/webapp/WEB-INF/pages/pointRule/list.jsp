<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="${contextPath}/static/js/uploadPreview.js" type="text/javascript"></script>
<title>积分规则</title>
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
$(function(){
	var isMainStore=${isMainStore};
	$("#week").attr("disabled",true);
	 $("#month").attr("disabled",true);
	 $("#weekType").bind("click", function () {
		 if($(this).prop("checked")){
			 $("#week").attr("disabled",false);
			 $("#monthType").attr("checked",false);
			 $("#month").val(1);
			 $("#month").attr("disabled",true); 
		 }else{
			 $("#week").val(32);
			 $("#week").attr("disabled",true); 
		 }
	 });
	
	 $("#monthType").bind("click", function () {
		 if($(this).prop("checked")){
			 $("#month").attr("disabled",false);
			 $("#weekType").attr("checked",false);
			 $("#week").val(32);
			 $("#week").attr("disabled",true); 
		 }else{
			 $("#month").val(1);
			 $("#month").attr("disabled",true); 
		 }
	 });
	 var numberDay="${pointRule.numberDay}";
	 if(numberDay!=''){
		 if(parseInt(numberDay)<=28){
			 $("#monthType").attr("checked",true);
			 $("#month").attr("disabled",false);
			 $("#month").val(parseInt(numberDay));
		 }else{
			 $("#weekType").attr("checked",true);
			 $("#week").attr("disabled",false);
			 $("#week").val(parseInt(numberDay));
		 }
	 }
	 var birthdayDoublePoint="${pointRule.birthdayDoublePoint}";
	 
	 if(birthdayDoublePoint!=''){
		 if(birthdayDoublePoint!='0'){
	 		$("#birthdayDoublePoint").val(parseInt(birthdayDoublePoint));
		 }
	 }
	 var validTime="${pointRule.validTime}";
	 if(validTime!=''){
	 	$("#validTime").val(parseInt(validTime));
	 }
	 var restriction="${pointRule.restriction}";
	 if(restriction!=''){
		 $("#restriction").val(parseInt(restriction));
	 }
	 var status="${pointRule.status}";
	 if(status=="0"){
		 $("#account_type_1").eq(0).prop("checked",true);
	 }
	$("#pointRuleAddBtn").bind("click", function () {
		var evaluatePoint=$.trim($("#evaluatePoint").val());//评价送积分
		var evaluatePointLength = evaluatePoint.length;
		if( evaluatePoint != "" && evaluatePoint != null ){
			if( !(/^[0-9]\d*$/.test(evaluatePoint)) ){
				lm.alert("评价送积分只能是正整数！");
				$("#evaluatePoint").focus();
				return ;
			}
		}
		var money=$.trim($("#money").val());
		var length = money.length;
		if( $.trim($("#money").val()) == "" || $.trim($("#money").val()) == null ){
			lm.alert("消费金额不能为空");
			$("#money").focus();
			return ;
		}
       
		if( !(lm.isFloat($.trim($("#money").val())) && $.trim($("#money").val()) > 0) ){
			lm.alert("消费金额输入错误！");
			$("#money").focus();
			return ;
		}else{
			if(money.indexOf(".")>0){
		        var first = money.indexOf(".");//判断第一个小数点所在位置
		        var last = money.lastIndexOf(".");//判断最后一个小数点所在的位置
		        var temp_length = money.split(".").length - 1;//含有.的个数
		        if(!isNaN(money) && (temp_length == 1) && (first==last) && (length - last <=3) ){
		           
		        }else{
		           lm.alert("请输入小于两位小数的数字！");
		           $("#money").focus();
		           return;
		        }
	        }
		}
		var point=$.trim($("#point").val());
		var pointLength = point.length;
		if($.trim($("#point").val())=="" || $.trim($("#point").val())==null){
			lm.alert("积分不能为空");
			$("#point").focus();
			return ;
		}
		if( point != "" && point != null ){
			if( !(/^[0-9]\d*$/.test(point)) ){
				lm.alert("积分只能是正整数！");
				$("#point").focus();
				return ;
			}
		}
		if($("#weekType").prop("checked")){
			var numberDay= $("#week option:selected").val();
			$("#numberDay").val(numberDay);
		}
		if($("#monthType").prop("checked")){
			var numberDay= $("#month option:selected").val();
			$("#numberDay").val(numberDay);
		}
		var costPoint=$.trim($("#costPoint").val());
		var costPointLength = costPoint.length;
		if($.trim($("#costPoint").val())=="" || $.trim($("#costPoint").val())==null){
			lm.alert("消耗积分不能为空");
			$("#costPoint").focus();
			return ;
		}
		if( costPoint != "" && costPoint != null ){
			if( !(/^[0-9]\d*$/.test(costPoint)) ){
				lm.alert("消耗积分只能是正整数！");
				$("#costPoint").focus();
				return ;
			}
		}
		var equalMoney=$.trim($("#equalMoney").val());
		var equalMoneyLength = equalMoney.length;
		if( $.trim($("#equalMoney").val()) == "" || $.trim($("#equalMoney").val()) == null ){
			lm.alert("抵扣金额不能为空");
			$("#equalMoney").focus();
			return ;
		}
       
		if( !(lm.isFloat($.trim($("#equalMoney").val())) && $.trim($("#equalMoney").val()) > 0) ){
			lm.alert("抵扣金额输入错误！");
			$("#equalMoney").focus();
			return ;
		}else{
			if(equalMoney.indexOf(".")>0){
		        var first = equalMoney.indexOf(".");//判断第一个小数点所在位置
		        var last = equalMoney.lastIndexOf(".");//判断最后一个小数点所在的位置
		        var temp_length = equalMoney.split(".").length - 1;//含有.的个数
		        if(!isNaN(equalMoney) && (temp_length == 1) && (first==last) && (equalMoneyLength - last <=3) ){
		           
		        }else{
		           lm.alert("请输入小于两位小数的数字！");
		           $("#equalMoney").focus();
		           return;
		        }
	        }
		}
		$("#pointRuleAddForm").submit();
		lm.alert("操作成功！");
	 })
});

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
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong><i class='icon-plust'></i>积分规则 </strong>
		</div>
		<div class='panel-body'>
			<c:if test="${empty pointRule && isMainStore==false}">
				<div  class='form-horizontal'>
					<div>末设置积分规则</div>
				</div>
			</c:if>
			<c:if test="${not empty pointRule && isMainStore==false}">
				<c:if test="${unifiedPointRule=='1' }">
					<div  class='form-horizontal'>
						<div>末启用积分规则</div>
					</div>
				</c:if>
				<c:if test="${unifiedPointRule=='0'}">
					<div  class='form-horizontal'>
						
							<input id="id" name="id" type="hidden" value="${pointRule.id }" />
							<input id="numberDay" name="numberDay" type="hidden" value="${pointRule.numberDay }" />
							<div class="form-group">
								<label class="col-md-1 control-label">评价送积分</label>
								<div class="col-md-2">
									<p style="padding-top: 6px;"><fmt:formatNumber value="${pointRule.evaluatePoint }" type="currency" pattern="0"/></p>
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>消费金额</label>
								<div class="col-md-1">
									<p style="padding-top: 8px;"><fmt:formatNumber value="${pointRule.money }" type="currency" pattern="0.00"/></p>
								</div>
								<label class="col-md-1 control-label" style="width:100px;">元，获得积分</label>
								<div class="col-md-1">
									<p style="padding-top: 5px;"><fmt:formatNumber value="${pointRule.point }" type="currency" pattern="0"/></p>
								</div>
							</div>
							<div class="form-group">
									<label class="col-md-1 control-label">会员生日双倍积分</label>
									<div class="col-md-2">
								         <p style="padding-top: 5px;">
								         	<c:if test="${pointRule.birthdayDoublePoint==1}">当天</c:if>
								         	<c:if test="${pointRule.birthdayDoublePoint==2}">一周</c:if>
								         	<c:if test="${pointRule.birthdayDoublePoint==1}">一个月</c:if>
								         </p>
									</div>
							</div>
							<div class="form-group">
								<label class="col-md-1 control-label">通用会员日</label>
									<div class="col-md-2" style="padding-top: 5px;">
										<c:if test="${pointRule.numberDay<=28}">
										每月的${pointRule.numberDay}号
										</c:if>
										<c:if test="${pointRule.numberDay==32}">
										每周的星期一
										</c:if>
										<c:if test="${pointRule.numberDay==33}">
										每周的星期二
										</c:if>
										<c:if test="${pointRule.numberDay==34}">
										每周的星期三
										</c:if>
										<c:if test="${pointRule.numberDay==35}">
										每周的星期四
										</c:if>
										<c:if test="${pointRule.numberDay==36}">
										每周的星期五
										</c:if>
										<c:if test="${pointRule.numberDay==37}">
										每周的星期六
										</c:if>
										<c:if test="${pointRule.numberDay==38}">
										每周的星期日
										</c:if>
									</div>
							</div>	
							<div class="form-group">
									<label class="col-md-1 control-label">积分有效期</label>
									<div class="col-md-2">
							         	<p style="padding-top: 5px;">
								         	<c:if test="${pointRule.validTime==-1}">永久</c:if>
								         	<c:if test="${pointRule.validTime==1}">一年</c:if>
								         	<c:if test="${pointRule.validTime==2}">二年</c:if>
							         	</p>
									</div>
							</div>
							<div class="form-group">
								<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>每消耗积分</label>
								<div class="col-md-1">
										<p style="padding-top: 8px;"><fmt:formatNumber value="${pointRule.costPoint }" type="currency" pattern="0"/></p>
								</div>
								<label class="col-md-1 control-label" style="width:78px;">抵扣</label>
								<div class="col-md-1">
										<p style="padding-top: 6px;"><fmt:formatNumber value="${pointRule.equalMoney }" type="currency" pattern="0.00"/></p>
								</div>
								<label class="col-md-0 control-label" style="font-size: 14px;" >元</label>
							</div>
							<div class="form-group">
									<label class="col-md-1 control-label">积分生成限制</label>
									<div class="col-md-2">
						           		<p style="padding-top: 5px;">
							           		<c:if test="${pointRule.restriction==0 }">无限制</c:if>
							           		<c:if test="${pointRule.restriction==1 }">折扣订单不产生积分</c:if>
							           		<c:if test="${pointRule.restriction==2 }">积分抵扣订单不产生积分</c:if>
							           		<c:if test="${pointRule.restriction==3 }">余额消费不产生积分</c:if>
							           		<c:if test="${pointRule.restriction==4 }">所有优惠订单不产生积分</c:if>
						           		</p>
									</div>
							</div>
							<div class="form-group">
								<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>是否启用</label>
								<div class="col-md-2" style="margin-top: 3px;">
									<p style="padding-top: 5px;">
										<c:if test="${pointRule.status==1}">是</c:if>
										<c:if test="${pointRule.status==0}">否</c:if>
									</p>
								</div>
							</div>
							
							<div class="form-group">
								<div class="col-md-offset-1 col-md-10">
									<c:if test="${isMainStore==true}">
										<input type="button"  repeatSubmit='1' id='pointRuleAddBtn' class='btn btn-primary' 
											value="保存" data-loading='稍候...' />
									</c:if>
								</div>
							</div>
						
					</div>
				</c:if>
			</c:if>
			<c:if test="${isMainStore==true || isNotChainStore==true}">
			<div  class='form-horizontal'>
				<form id="pointRuleAddForm" method='post' class='form-horizontal' repeatSubmit='1'
				action="${contextPath }/pointRule/save">
					<input id="id" name="id" type="hidden" value="${pointRule.id }" />
					<input id="numberDay" name="numberDay" type="hidden" value="${pointRule.numberDay }" />
					<div class="form-group">
						<label class="col-md-1 control-label">评价送积分</label>
						<div class="col-md-2">
								<input type='text' id="evaluatePoint" name="evaluatePoint" value="<fmt:formatNumber value="${pointRule.evaluatePoint }" type="currency" pattern="0"/>" class='form-control' maxlength="9" style="width:360px;"/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>消费金额</label>
						<div class="col-md-1">
								<input type='text' id="money" name="money" class='form-control' value="<fmt:formatNumber value="${pointRule.money }" type="currency" pattern="0.00"/>" maxlength="7"/>
						</div>
						<label class="col-md-1 control-label" style="width:100px;">元，获得积分</label>
						<div class="col-md-1">
							<input type='text' id="point" name="point" value="<fmt:formatNumber value="${pointRule.point }" type="currency" pattern="0"/>" class='form-control' maxlength="9"/>
						</div>
					</div>
					<div class="form-group">
							<label class="col-md-1 control-label">会员生日双倍积分</label>
							<div class="col-md-2">
								<select name="birthdayDoublePoint" class="form-control"  id="birthdayDoublePoint" style="width:360px;">
									<option  value ="0">请选择</option>
					           		<option  value ="1">当天</option>
									<option  value ="2">一周</option>
									<option  value ="3">一个月</option>
					           	</select>
							</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label">通用会员日</label>
						<div class="col-md-1" style="width: 60px;margin-top: 5px;">
							<input  type="checkbox"    id="weekType" name="weekType"/>每周
						</div>
						<div class="col-md-1" style="width: 140px;">
							<select name="week" class="form-control"  style="width: 100px;" id="week">
									<option  value ="32">星期一</option>
					           		<option  value ="33">星期二</option>
									<option  value ="34">星期三</option>
									<option  value ="35">星期四</option>
									<option  value ="36">星期五</option>
									<option  value ="37">星期六</option>
									<option  value ="38">星期日</option>
					       </select>
						</div>
						<div class="col-md-1" style="width: 60px;margin-top: 5px;">
							<input  type="checkbox"    id="monthType" name="monthType"/>每月
						</div>
						<div class="col-md-1" style="width: 60px;">
							<select name="month" class="form-control"  style="width: 100px;" id="month">
									<option  value ="1">1</option>
					           		<option  value ="2">2</option>
									<option  value ="3">3</option>
									<option  value ="4">4</option>
									<option  value ="5">5</option>
									<option  value ="6">7</option>
									<option  value ="8">8</option>
									<option  value ="9">9</option>
									<option  value ="10">10</option>
									<option  value ="11">11</option>
									<option  value ="12">12</option>
									<option  value ="13">13</option>
									<option  value ="14">14</option>
									<option  value ="15">15</option>
									<option  value ="16">16</option>
									<option  value ="17">17</option>
									<option  value ="18">18</option>
									<option  value ="19">19</option>
									<option  value ="20">20</option>
									<option  value ="21">21</option>
									<option  value ="22">22</option>
									<option  value ="23">23</option>
									<option  value ="24">24</option>
									<option  value ="25">25</option>
									<option  value ="26">26</option>
									<option  value ="27">27</option>
									<option  value ="28">28</option>
									
					       </select>
						</div>
					</div>	
					<div class="form-group">
							<label class="col-md-1 control-label">积分有效期</label>
							<div class="col-md-2">
								<select name="validTime" class="form-control"  id="validTime" style="width:360px;">
									<option  value ="-1">永久</option>
					           		<option  value ="1">一年</option>
									<option  value ="2">二年</option>
					           	</select>
							</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>每消耗积分</label>
						<div class="col-md-1">
								<input type='text' id="costPoint" name="costPoint" class='form-control' value="<fmt:formatNumber value="${pointRule.costPoint }" type="currency" pattern="0"/>" maxlength="7" />
						</div>
						<label class="col-md-1 control-label" style="width:78px;">抵扣</label>
						<div class="col-md-1">
								<input type='text' id="equalMoney" name="equalMoney" value="<fmt:formatNumber value="${pointRule.equalMoney }" type="currency" pattern="0.00"/>" class='form-control' maxlength="9"/>
						</div>
						<label class="col-md-0 control-label" style="font-size: 14px;" >元</label>
					</div>
					<div class="form-group">
							<label class="col-md-1 control-label">积分生成限制</label>
							<div class="col-md-2">
									<select name="restriction" class="form-control"  id="restriction" style="width:360px;">
										<option  value ="0">无限制</option>
						           		<option  value ="1">折扣订单不产生积分</option>
										<option  value ="2">积分抵扣订单不产生积分</option>
										<option  value ="3">余额消费不产生积分</option>
										<option  value ="4">所有优惠订单不产生积分</option>
						           	</select>
							</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>是否启用</label>
						<div class="col-md-2" style="margin-top: 3px;">
								<input id="account_type_0"  type='radio' value="1" name="status" checked="checked"/>是&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input id="account_type_1"  type='radio' value="0" name="status"/>否
						</div>
					</div>
					
					<div class="form-group">
						<div class="col-md-offset-1 col-md-10">
							<c:if test="${isMainStore==true || isNotChainStore==true}">
								<input type="button"  repeatSubmit='1' id='pointRuleAddBtn' class='btn btn-primary' 
									value="保存" data-loading='稍候...' />
							</c:if>
						</div>
					</div>
				</form>
			</div>
			</c:if>
		</div>
	</div>

</body>
</html>