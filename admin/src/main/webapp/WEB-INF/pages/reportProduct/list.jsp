<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品销量统计</title>
<script type="text/javascript">
function callback(obj){
	$("#reportProduct_storeName").val(obj.name);
	$("#reportProduct_storeId").val(obj.id);
}

function formReset(){
	$("#reportProduct_storeName").val("");
	$("#reportProduct_storeId").val("");
	var formatdate0 = "yyyy-MM-dd";//默认格式
	var formatdate = "yyyy-MM-dd hh:00";//默认格式
	var now = lm.Now;//当前日期
	var nowFormat0=now.Format(formatdate0);
	var nowFormat=now.Format(formatdate);
	$("#beginTime").val(nowFormat0+" 00");
	$("#endTime").val(nowFormat);
}
$(function(){
	$("#dateType").val(0);
	$('.boult').css('border-bottom', '8px solid #5B5B5B');
    $('.boult').css('border-top', '8px solid #5B5B5B');
    var formatdate0 = "yyyy-MM-dd";//默认格式
	var formatdate = "yyyy-MM-dd hh:59";//默认格式
	var now = lm.Now;//当前日期
	var nowFormat0=now.Format(formatdate0);
	var nowFormat=now.Format(formatdate);
	$("#beginTime").val(nowFormat0+" 00:00");
	$("#endTime").val(nowFormat);
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
	$('#beginTime').datetimepicker().on('changeDate', function(ev){
		$("#dateType").val("");
	});
	$('#endTime').datetimepicker().on('changeDate', function(ev){
		$("#dateType").val("");
		if((new Date(ev.date.valueOf()))<(new Date($('#beginTime').val().replace(/-/g,"/")))){//开始时间大于结束时间
			lm.alert("结束时间不能大于开始时间");
			$("#endTime").val(endDateInput);//默认值上一次输入
			$('#endTime').datetimepicker('update');//更新
		}
		endDateInput= $("#endTime").val();
		$('#beginTime').datetimepicker('setEndDate', $("#endTime").val());//设置开始时间最后可选结束时间
	});
	$("#dateType").change(function(){
		if($(this).val()==0){
			$("#beginTime").val(nowFormat0+" 00:00");
			$("#endTime").val(nowFormat);
			
		}else if($(this).val()==1){
			var old = new Date();  //当前月
			var oldFormat=old.Format(formatdate);
			oldFormat=oldFormat.substring(0,oldFormat.lastIndexOf("-"))+"-01 00:00";
			$("#beginTime").val(oldFormat);
			$("#endTime").val(nowFormat);
		}else if($(this).val()==2){
			var lastFormat=getLastDay();
			var firstFormat =lastFormat.substring(0,lastFormat.lastIndexOf("-"))+"-01 00:00";
			$("#beginTime").val(firstFormat);
			$("#endTime").val(lastFormat+" 23:59");
		}else if($(this).val()==3){
			var old = new Date();  //当前月
			var dateFormat=old.Format(formatdate);
			var oldFormat=dateFormat.substring(0,4)+"-01-01 00:00";
			$("#beginTime").val(oldFormat);
			$("#endTime").val(nowFormat); 
		}
	});
	$("#type").change(function(){
		if($(this).val()==0){
			$("#name").attr("placeholder","商品名称");
		}else if($(this).val()==1){
			$("#name").attr("placeholder","商品分类");
		}else{
			$("#name").attr("placeholder","品牌名称");
		}
	});
	// 点击弹出商家选择窗口
	$("#reportProduct_storeName").click(function (){
		$("#reportProductShowStore").modal();
	});
	
});
//修改选择的店铺时触发
function byStoreAjaxRefreshList(){
	loadList_reportProductList(); 
}

function getLastDay(){  
	var dt = new Date();  
	dt.setDate(1);  
	dt.setMonth(dt.getMonth());  
	cdt = new Date(dt.getTime()-1000*60*60*24);
	var month=Number(cdt.getMonth())+1;
	
	if(month<10){
		month="0"+String(month);
	}
	var lastDay=cdt.getFullYear()+"-"+month+"-"+cdt.getDate();
	return   lastDay;
}  
function getExe() {
	var flag=${isSys};
	var storeId="";
	if(flag){
		storeId=$("#reportProduct_storeId").val();//店铺
	}
	var beginTime =$("#beginTime").val();//开始时间
	var endTime = $("#endTime").val();//结束时间
	var sort=$("#sort").val();
	var source = $("#source option:selected").val(); //交易类型
	var type = $("#type option:selected").val(); //统计类型
	var name=$("#name").val();
	window.open("${contextPath}/reportProduct/list/ajax/list-by-search?beginTime="+beginTime
					+"&endTime="+endTime+"&storeId="+storeId+"&sort="+sort+"&source="+source+"&type="+type+"&name="+name);
}
function callback1(){
	$("[name='numAsc']").click(function (){
		$('.boult').css('border-bottom', '8px solid #5B5B5B');
        $('.boult').css('border-top', '8px solid #5B5B5B');
        var sortValue=$(this).attr("value");
        $("#sort").val(sortValue);
        loadList_reportProductList();
       
		
	});
	$("[name='numAsc']").each(function(index,item){
		if($(this).attr("value")==$("#sort").val()){
			if($(this).attr("value")%2==0){
				$(this).css('border-top', '8px solid #0080FF');
			}else{
				$(this).css('border-bottom', '8px solid #0080FF');
			}
		}
	});
	
	
}
</script>

 <style>
	.input-group2{
	float:left;width:auto; background-color: #e5e5e5;border: 1px solid #ccc;border-radius: 4px; color: #222;font-size: 13px;font-weight: 400; padding: 5px 12px;text-align: center;
	}
	.table{
	   	background-color:#F7F7F7;
	}
	.mal{
	  border-left:none !important;
	}
	.lines td{
	  line-height: 15px; 
	  background-color:#F7F7F7;
	  order-top:none;
	  border-bottom:none;
	  border-right:none;
	  padding-left:20px;
	  float:left;
	}
	.lines-tr{
	  border:none;
	  font-weight: 1500px; 
	  background-color:#F7F7F7;
	  color: #ff3333;
	  font-size: 20px;
	  font-weight: 700;
	}
	.lines-tr td{
	  margin-bottom:20px;
	  margin-top:0;
	  border-bottom:none;
	}
	.mag{
		border:none;
	}
	.mag td{
	    margin-top:20px;
	    border-bottom:none;
	    border-right:none;
	    
	    border-top:none;
	}
	
    .one td{
        width:16%;
        box-sizing: border-box;
        display:inline-block;
        padding-left: 20px;
       line-height: 35px;
       border-bottom:none;
       border-top:none;
       
       
       
    }
    .two{
    	position: relative;
/*     	display: block; */
    }
     

		.arrows{
		            width: 0;
		            height: 0;
		            border-left: 7px solid transparent;
		            border-right: 7px solid transparent;
		            border-bottom: 8px solid  #5B5B5B;
		            position: absolute;
		            right: 8px;
		            top: 9px;
		            cursor: pointer;
		        }
	        .boult{
	            right: 8px;
	            top: 18px;
	            border-bottom:none;
              	border-top: 8px solid  #5B5B5B;
              	cursor: pointer;
	              
	        }
    
  
</style>
</head>
<body>
	<m:list title="商品销量统计" id="reportProductList" callback="callback1"
		listUrl="${contextPath }/reportProduct/list/list-data" 
		searchButtonId="cateogry_search_btn" formReset="formReset">
		
		<div class="input-group" style="max-width:1500px;">
			<input type="hidden" name="sort" id="sort" value="0" />
			<c:choose>  
			   <c:when test="${isSys==true }">  
			   		<span class="input-group2">商家</span>
					<input name="storeName" id="reportProduct_storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="" class="form-control" isRequired="1" tipName="商家"  />
					<input type="hidden" name="storeId" id="reportProduct_storeId" value="" />
			   </c:when>  
			   <c:otherwise> 
			   		<m:hasPermission permissions="mainShopOrderStoreView">
			   			<c:if test="${isMainStore==true }">
							<span class="input-group2">商家</span>
							<input name="storeName" id="reportProduct_storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="" class="form-control" isRequired="1" tipName="商家"  />
							<input type="hidden" name="storeId" id="reportProduct_storeId" value="-1" />
						</c:if>
       				</m:hasPermission>  
			   </c:otherwise>  
			</c:choose> 
			<span class="input-group2">交易类型</span>
            <select name="source" class="form-control" style="width: auto;margin-right:40px;" id="source" >
           		<option  value ="">全部</option>
				<option  value ="0">APP</option>
				<option  value ="1">终端</option>
           	</select> 
            <span class="input-group2">统计类型</span>
            <select name="type" class="form-control" style="width: auto;margin-right:40px;" id="type" >
           		<option  value ="0">按商品</option>
				<option  value ="1">按分类</option>
				<option  value ="2">按品牌</option>
           	</select>
			<span class="input-group2">名称</span> 
            	<input type="text" id="name" name="name" class="form-control" placeholder="商品名称" style="width: 200px;float:left;margin-right:40px;">
            </div>	
           <br> 
           <div class="input-group" style="max-width:1500px;">	
			<span class="input-group2">快速查看</span>
			<select name="dateType" class="form-control" style="width: auto;margin-right:40px;" id="dateType">
				<option  value ="">请选择</option>
           		<option  value ="0" selected="selected">今日</option>
				<option  value ="1">本月</option>
				<option  value ="2">上月</option>
				<option  value ="3">本年</option>
           	</select>
            <span  class="input-group2">开始时间</span> 
			<input id = "beginTime"  type="text"    name="beginTime" class="form-control form-date" placeholder="选择开始日期" readonly  style="width: 120px;margin-right:40px;">
			<span  class="input-group2">结束时间</span> 
			<input id = "endTime"  type="text"	name="endTime" class="form-control form-date" placeholder="选择结束日期" readonly style="width: 120px;margin-right:40px;">	
		
		</div>
		<button type="button" onclick="getExe()" class="btn btn-small btn-danger" style="width: auto;margin-top:5px;">导出报表</button>
	</m:list>
	<m:select_store path="${contextPath}/reportProduct/showModel/list/list-data" modeId="reportProductShowStore" callback="callback"> </m:select_store>
</body>
</html>