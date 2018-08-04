<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>POS收银列表</title>
<script type="text/javascript">
function callback(obj){
	$("#order_storeName").val(obj.name);
	$("#order_storeId").val(obj.id);
}
	$(function(){
		var format = "yyyy-MM-dd hh";//默认格式
		var now = lm.Now;//当前日期
		var nowFormat=now.Format(format);
		$("#beginTime").datetimepicker({
			minView: "day", //选择日期后，不会再跳转去选择时分秒 
			format: "yyyy-mm-dd hh:00", //选择日期后，文本框显示的日期格式 
			language: 'zh-CN', //汉化 
			autoclose:true , //选择日期后自动关闭
			todayBtn:true,//可选择当天按钮
			todayHighlight:false,//高亮当前日期
			endDate:""+nowFormat+""//默认最后可选择为当前日期
			
		 });
		$("#endTime").datetimepicker({
			minView: "day", //选择日期后，不会再跳转去选择时分秒 
			format: "yyyy-mm-dd hh:59", //选择日期后，文本框显示的日期格式 
			language: 'zh-CN', //汉化 
			autoclose:true, //选择日期后自动关闭 
			todayBtn:true,//可选择当天按钮
			todayHighlight:false,//高亮当前日期
			endDate:""+nowFormat+""//默认最后可选择为当前日期
		 });
		var endDateInput=nowFormat;
		//判断输入结束日期时候输入合法
		$('#endTime').datetimepicker().on('changeDate', function(ev){
			if((new Date(ev.date.valueOf()))<(new Date($('#beginTime').val().replace(/-/g,"/")))){//开始时间大于结束时间
				lm.alert("结束时间不能大于开始时间");
				$("#endTime").val(endDateInput);//默认值上一次输入
				$('#endTime').datetimepicker('update');//更新
			}
			endDateInput= $("#endTime").val();
			$('#beginTime').datetimepicker('setEndDate', $("#endTime").val());//设置开始时间最后可选结束时间
		});
		// 点击弹出商家选择窗口
		$("#order_storeName").click(function (){
			$("#orderShowStore").modal();
		});
		
		

	});
	
	function infoButtonFun(){
		$('[name="infoButton"]').each(function(){
			var a = $(this).attr("data-remote");
			$(this).attr("data-remote",a+"?date="+new Date());
		});
		
		$('.nameHide').hover(function() {
         	var title = $(this).html();
            $(this).after('<div class="fader"><span></span><div class="tooltip-inner"></div></div>');
            $('.tooltip-inner').html(title);
        }, function() {
            $('.fader').remove();
        });
	}
	function typeChage(orderId,status,pageNo){
		lm.confirm("确定要改变订单状态吗？",function(){
			lm.post("${contextPath }/order/typeChange/change-by-orderId",{orderId:orderId,status:status},function(data){
				if(data==1){
					lm.alert("操作成功！");
					loadCurrentList_orderList(pageNo);
				} 
			});
		});
	}
	function getStatus(){
		var source=$("#source").find("option:selected").val();
	    if(source == ""){
	       $("#status").empty();
	       $("#status").prop("disabled",true);
	       $("#status").append("<option value='' >全部</option>");
	       return ;
	    }
	    if(source==0){
		    $("#status").empty();
		    $("#status").prop("disabled",false);
	    	$("#status").append("<option value='' >全部</option>");
	    	$("#status").append("<option value='0' >待付款</option>");
	    	$("#status").append("<option value='1' >待确认</option>");
	    	$("#status").append("<option value='2' >已取消</option>");
	    	$("#status").append("<option value='3' >已完成</option>");
	    	$("#status").append("<option value='4' >待签收</option>");
	    	$("#status").append("<option value='5' >待发货</option>");
	    	$("#status").append("<option value='6' >已确认</option>");
	    	$("#status").append("<option value='7' >退换货</option>");
	    	$("#status").append("<option value='8' >待评价</option>");
	    	$("#status").append("<option value='9' >缺货</option>");
	    	$("#status").append("<option value='10' >用户取消</option>");
	    	$("#status").append("<option value='11' >超时未支付，系统取消</option>");
	    	$("#status").append("<option value='12' >退款中</option>");
	    	$("#status").append("<option value='13' >已退款</option>");
	    	$("#status").append("<option value='99' >交易关闭</option>");
	    }else if(source==1){
	    	$("#status").empty();
	    	$("#status").prop("disabled",false);
	    	$("#status").append("<option value='' >全部</option>");
	    	$("#status").append("<option value='0' >待付款</option>");
	    	$("#status").append("<option value='1' >已支付</option>");
	    }
	}
	
	function getExe() {
		var storeId=$("#order_storeId").val();//店铺
		if( null == storeId || undefined == storeId ){
			storeId = "";
		} 
		var beginTime =$("#beginTime").val();//开始时间
		var endTime = $("#endTime").val();//结束时间
		var mobile=$("#mobile").val();//用户账号
		var paymentMode=$("#paymentMode").val();//支付方式
		var orderId=$("#orderId").val();//订单编号
		var source=$("#source").val();//订单来源
		var status=$("#status").val();//订单状态
		var memo=$("#memo").val();//会员备注
		var haveReturnGoods=$("#haveReturnGoods").val();//有退货
		window.open("${contextPath}/order/list/ajax/list-by-search?beginTime="+beginTime+"&endTime="+endTime
						+"&storeId="+storeId+"&mobile="+mobile+"&paymentMode="+paymentMode
						+"&orderId="+orderId+"&source="+source+"&memo="+memo+"&status="+status+"&haveReturnGoods="+haveReturnGoods);
	}
	
	
	
</script>
<style>
  .input-group2{
  float:left;width:auto; background-color: #e5e5e5;border: 1px solid #ccc;border-radius: 4px; color: #222;font-size: 13px;font-weight: 400; padding: 5px 12px;text-align: center;
  }
  
    .nameHide{
            line-height: 30px;
            text-align: center;
            text-overflow:ellipsis;/*//让超出的用...实现*/
            white-space:nowrap;/*//禁止换行*/
            overflow:hidden;/*//超出的隐藏*/
            display: block;
            width:50px;
            cursor:pointer;
        }
        
        .fader{
            position: relative;
            z-index: 1;
            line-height: 2;
            color:white;
            
        }
            .fader span{
                width:0; 
                height:0; 
                border-left:6px solid transparent;
                border-right:6px solid transparent;
                border-bottom:8px solid #000;
                position: absolute;
                top: -6px;
                left: 12px;
            }
            .tooltip-inner{
                position: absolute;
                background-color: #000;
                border-radius: 5px;
                top: 0;
                left: 0;
                padding:0 10px;
            }

  
</style>
</head>
<body>
	<m:list title="POS收银列表" id="orderList"
		listUrl="${contextPath }/order/list-data" callback="infoButtonFun"
		 searchButtonId="user_search_btn" >
		<div class="input-group" style="max-width: 1500px;">
            <c:choose>  
			   <c:when test="${isSys==true }">  
			   		<span class="input-group2">商家</span>
					<input name="storeName" id="order_storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="" class="form-control" isRequired="1" tipName="商家"  />
					<input type="hidden" name="storeId" id="order_storeId" value="" />
			   </c:when>  
			   <c:otherwise> 
			   		<m:hasPermission permissions="mainShopOrderStoreView">
			   			<c:if test="${isMainStore==true }">
							<span class="input-group2">商家</span>
							<input name="storeName" id="order_storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="" class="form-control" isRequired="1" tipName="商家"  />
							<input type="hidden" name="storeId" id="order_storeId" value="-1" />
						</c:if>
       				</m:hasPermission>  
			   </c:otherwise>  
			</c:choose>  
       
			<span  class="input-group2">开始时间</span> 
			<input id = "beginTime"  type="text"    name="beginTime" class="form-control form-date" placeholder="选择开始日期" readonly  style="width: 140px;margin-right:40px;">
			<span  class="input-group2">结束时间</span> 
			<input id = "endTime"  type="text"	name="endTime" class="form-control form-date" placeholder="选择结束日期" readonly style="width: 140px;margin-right:40px;">
			<span class="input-group2">会员账号</span> 
			<input type="text"	id="mobile" name="mobile" class="form-control" placeholder="请输入用户账号" style="width: 200px;margin-right:40px;">
			<span class="input-group2">支付方式</span>
           	<select name="paymentMode" class="form-control" style="width: auto;" id="paymentMode">
           		<option  value ="">全部</option>
           		<option  value ="0">支付宝</option>
           		<option  value ="1">微信</option>
           		<option  value ="2">刷卡</option>
				<option  value ="3">现金</option>
				<option  value ="9">会员卡</option>
           	</select>
           	<span class="input-group2" style="margin-left: 40px;">状态</span>
           	<select name="status" class="form-control" style="width: auto;" id="status">
           		<option  value ="">全部</option>
           		<option  value ="1">已支付</option>
           		<option  value ="100">支付中</option>
           		<option  value ="15">刷卡撤销</option>
           	</select>
			</div>
			<br />
			<div class="input-group" style="max-width: 1500px;">
			<span class="input-group2">订单编号</span> 
			<input type="text"	id="orderId" name="orderId" class="form-control" placeholder="请输入订单编号" style="width: 200px;margin-right:40px;">
			<!-- 订单状态 0待付款 1已支付 2已取消 3已完成,4待签收,5待发货 ,6已确认,7退换货，8待评价，9缺货， 99交易关闭， -->
           	
           	<span class="input-group2">售后</span>
           	<select name="haveReturnGoods" class="form-control" style="width: 200px;margin-right:40px;" id="haveReturnGoods">
				<option  value ="">全部</option>
				<option value='1' >有退货</option>
	    		<option value='0' >无</option>
           	</select>
           	<span class="input-group2">会员备注</span> 
			<input type="text"	id="memo" name="memo" class="form-control" placeholder="请输入会员备注" style="width: 200px;margin-right:40px;">
           	</div>
			<button type="button" onclick="getExe()" class="btn btn-small btn-danger" style="width: auto;margin-top:5px;">批量导出</button>
	</m:list>
	<m:select_store path="${contextPath}/order/showModel/list/list-data" modeId="orderShowStore" callback="callback"> </m:select_store>
</body>
</html>