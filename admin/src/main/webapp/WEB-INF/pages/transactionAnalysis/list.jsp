<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>交易分析</title>
<script type="text/javascript" src="${staticPath }/js/ichart.latest.min.js"></script>
<script type="text/javascript">

	$(function(){
		$("#dateType").val(0);
		var isSys=${isSys};
		var isMainStore=${isMainStore};
		// 点击弹出商家选择窗口
		$("#reportProduct_storeName").click(function (){
			$("#reportProduct_stockId").val("");
			$("#reportProduct_stockName").val("");
			$("#product_name").val("");
			$("#reportProductShowStore").modal();
			$("#analysisType option[value='2']").remove(); 
		});
		$("#fresetButton").click(function (){
			$("#reportProduct_storeId").val("");
			$("#reportProduct_storeName").val("");
			$("#reportProduct_stockId").val("");
			$("#reportProduct_stockName").val("");
			$("#analysisType").val("0");
			$("#analysisType option[value='2']").remove();   
		});
		$("[name = 'stockName']").bind("click", function () {
			$("#reportProduct_stockId").val("");
			$("#reportProduct_stockName").val("");
			$("#product_name").val("");
			if(isSys || isMainStore){
				if($("#reportProduct_storeId").val()==null || $("#reportProduct_storeId").val()=="" || $("#reportProduct_storeId").val()==-1){
					lm.alert("请先选择商家！");
					return;
				  }else{
					  var storeId=$("#reportProduct_storeId").val();
						$("#product_storeId").val(storeId);
						$('#stockNameAddModal').modal();
						$("#productStockList_search_btn").click();
				  }
			}else{
				var storeId="${isStoreId}";
				$("#product_storeId").val(storeId);
				$('#stockNameAddModal').modal();
				$("#analysisType option[value='2']").remove(); 
			}
		  }); 
		var formatdate = "yyyy-MM-dd";//默认格式
		var formatMonth = "yyyy-MM";//默认格式
		var formatYear = "yyyy";//默认格式
		var now = lm.Now;//当前日期
		var old = new Date(now.getTime() - 24*60*60*6000);  //前7天
		var nowFormat=now.Format(formatdate);
		var nowMonthFormat=now.Format(formatMonth);
		var nowYearFormat=now.Format(formatYear);
		var oldFormat=old.Format(formatdate);
		$("#dateBeginTime").val(oldFormat);
		$("#dateEndTime").val(nowFormat);
		$("#monthTime").val(nowMonthFormat);
		$("#yearTime").val(nowYearFormat);
		//$("#yearTime").val(nowYearFormat);
		$("#dateBeginTime").datetimepicker({
			minView: "month", //选择日期后，不会再跳转去选择时分秒 
			format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
			language: 'zh-CN', //汉化 
			autoclose:true , //选择日期后自动关闭
			todayBtn:true,//可选择当天按钮
			todayHighlight:false,//高亮当前日期
			endDate:""+nowFormat+""//默认最后可选择为当前日期
			
		 });
		$("#dateEndTime").datetimepicker({
			minView: "month", //选择日期后，不会再跳转去选择时分秒 
			format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
			language: 'zh-CN', //汉化 
			autoclose:true, //选择日期后自动关闭 
			todayBtn:true,//可选择当天按钮
			todayHighlight:false,//高亮当前日期
			endDate:""+nowFormat+""//默认最后可选择为当前日期
		 });
		var endDateInput=nowFormat;
		$('#dateBeginTime').datetimepicker().on('changeDate', function(ev){
			$("#dateType").val("");
		});
		//判断输入结束日期时候输入合法
		$('#dateEndTime').datetimepicker().on('changeDate', function(ev){
			$("#dateType").val("");
			if((new Date(ev.date.valueOf()))<(new Date($('#dateBeginTime').val().replace(/-/g,"/")))){//开始时间大于结束时间
				lm.alert("结束时间不能大于开始时间");
				$("#dateEndTime").val(endDateInput);//默认值上一次输入
				$('#dateEndTime').datetimepicker('update');//更新
			}
			endDateInput= $("#dateEndTime").val();
			$('#dateBeginTime').datetimepicker('setEndDate', $("#dateEndTime").val());//设置开始时间最后可选结束时间
		});
		$("#dateType").change(function(){
			if($(this).val()==0){
				var old = new Date(now.getTime() - 24*60*60*6000);  //前7天
				var oldFormat=old.Format(formatdate);
				$("#dateBeginTime").val(oldFormat);
				
			}else if($(this).val()==1){
				var old = new Date(now.getTime() - 24*60*60*29000);  //前30天
				var oldFormat=old.Format(formatdate);
				$("#dateBeginTime").val(oldFormat);
			}else if($(this).val()==2){
				var old = new Date(now.getTime() - 24*60*60*89000);  //前90天
				var oldFormat=old.Format(formatdate);
				$("#dateBeginTime").val(oldFormat);
			}else if($(this).val()==3){
				var old = new Date(now.getTime() - 24*60*60*364000);  //前1年
				var oldFormat=old.Format(formatdate);
				$("#dateBeginTime").val(oldFormat);
			}
			$("#dateEndTime").val(nowFormat); 
		});
		$("#monthTime").datetimepicker({
			startView:3,
			minView: 4, //选择日期后，不会再跳转去选择时分秒 
			format: "yyyy-mm", //选择日期后，文本框显示的日期格式 
			language: 'zh-CN', //汉化 
			autoclose:true, //选择日期后自动关闭 
			todayBtn:false,//可选择当天按钮
			todayHighlight:false,//高亮当前日期
			endDate:""+nowFormat+""//默认最后可选择为当前日期
		 });
		$("#yearTime").datetimepicker({
			startView:4,
			minView: 5, //选择日期后，不会再跳转去选择时分秒 
			format: "yyyy", //选择日期后，文本框显示的日期格式 
			language: 'zh-CN', //汉化 
			autoclose:true, //选择日期后自动关闭 
			todayBtn:false,//可选择当天按钮
			todayHighlight:false,//高亮当前日期
			endDate:""+nowFormat+""//默认最后可选择为当前日期
		 });
		//日
		var dOrderSumCash='${dOrderSum}';
		var dAppSumCash='${dAppSum}';
		var dDevicesCash='${dDevicesSum}';
		dateDraw(dOrderSumCash,dAppSumCash,dDevicesCash);
		//月
		var mOrderSumCash='${mOrderSum}';
		var mAppSumCash='${mAppSum}';
		var mDevicesCash='${mDevicesSum}';
		monthDraw(mOrderSumCash,mAppSumCash,mDevicesCash);
		//年
		var yOrderSumCash='${yOrderSum}';
		var yAppSumCash='${yAppSum}';
		var yDevicesCash='${yDevicesSum}';
		yearDraw(yOrderSumCash,yAppSumCash,yDevicesCash);
		$("#analysisType").change(function(){
			if($(this).val()==2){
				$("#dateOrderType").hide();
				$("#monthOrderType").hide();
				$("#yearOrderType").hide();
			}else{
				$("#dateOrderType").show();
				$("#monthOrderType").show();
				$("#yearOrderType").show();
			}
		});
	});
	function callback(obj){
		$("#reportProduct_storeName").val(obj.name);
		$("#reportProduct_storeId").val(obj.id);
	}

	function formReset(){
		$("#reportProduct_storeName").val("");
		$("#reportProduct_storeId").val("");
	}
	function productListDataIdCallback(){
		$("#productListDataId").find("tbody tr").click(function(){
			$("#reportProduct_stockId").val($(this).attr("val"));
			$("#reportProduct_stockName").val(($.trim($($(this).find("td")[0]).html())));
			$("#productstockListModalBtn").click();
			$("#analysisType").append("<option  value ='2'>商品销量</option>");
		});
	}
	
	function getAll(){
		var isSys=${isSys};
		var isMainStore=${isMainStore};
		var isStoreId="${isStoreId}";
	 	if(!isSys && !isMainStore){
	 		$("#reportProduct_storeId").val(isStoreId);// 商家ID
	 	}
		var stockId=$("#reportProduct_stockId").val();
		
		var storeId=$("#reportProduct_storeId").val();
		var analysisType=$("#analysisType option:selected").val(); //分析类型
		//日
		var dateBeginTime=$("#dateBeginTime").val();//日查询开始
		var dateEndTime=$("#dateEndTime").val();//日查询结束
		var dateOrderType=$("#dateOrderType option:selected").val(); //总订单，平均订单
		//月
		var monthTime=$("#monthTime").val();//月时间
		var monthOrderType=$("#monthOrderType option:selected").val(); //总订单，平均订单
		//年
		var yearTime=$("#yearTime").val();//年时间
		var yearOrderType=$("#yearOrderType option:selected").val(); //总订单，平均订单
		lm.post("${contextPath}/transactionAnalysis/findAll",{storeId:storeId,stockId:stockId,analysisType:analysisType,
			dateBeginTime:dateBeginTime,dateEndTime:dateEndTime,dateOrderType:dateOrderType
			,monthTime:monthTime,monthOrderType:monthOrderType
			,yearTime:yearTime,yearOrderType:yearOrderType},function(data){
			if(analysisType==2){
				dSalesNumDraw(data[0]);
				mSalesNumDraw(data[3]);
				ySalesNumDraw(data[6]);
			}else{
				dateDraw(data[0],data[1],data[2],analysisType);
				monthDraw(data[3],data[4],data[5],analysisType);	
				yearDraw(data[6],data[7],data[8],analysisType);	
			}	
		}); 
	}
	function dSalesNumDraw(dOrderSumCash){
		var dOrderSum=[];
		var arrayOrder =dOrderSumCash.split(",");
		for (var i=0 ; i< arrayOrder.length ; i++){
			dOrderSum.push(arrayOrder[i]);
		}
		var data = [
		         	{
		         		name : '商品销量',
		         		value:dOrderSum,
		         		color:'#FF0000',
		         		line_width:2
		         	}
		         ];
         
		var datelabels = ["0点","1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点","14点","15点","16点","17点","18点","19点","20点","21点","22点","23点"];
		var line = new iChart.LineBasic2D({
			render : 'dateChart',
			data: data,
			align:'center',
			title : '',
			subtitle : '',
			footnote : '',
			width : 1100,
			height : 400,
			tip:{
				enable:true,
				shadow:true,
				listeners:{
					 //tip:提示框对象、name:数据名称、value:数据值、text:当前文本、i:数据点的索引
					parseText:function(tip,name,value,text,i){
							return value+"个";
					}
				}
			},
			legend : {
				enable : true,
				row:1,//设置在一行上显示，与column配合使用
				column : 'max',
				valign:'top',
				sign:'bar',
				background_color:null,//设置透明背景
				offsetx:-80,//设置x轴偏移，满足位置需要
				border : true
			},
			crosshair:{
				enable:true,
				line_color:'#62bce9'
			},
			sub_option : {
				label:false,
				point_hollow : false
			},
			coordinate:{
				width:640,
				height:240,
				axis:{
					color:'#9f9f9f',
					width:[0,0,2,2]
				},
				grids:{
					vertical:{
						way:'share_alike',
				 		value:5
					}
				},
				scale:[{
					 position:'left',	
					 //start_scale:0,
					 //end_scale:5000,
					 //scale_space:2,
					 scale_size:0,
					 scale_color:'#9f9f9f',
				},{
					 position:'bottom',	
					 labels:datelabels
				}]
			}
		});
		//利用自定义组件构造左侧说明文本
		line.plugin(new iChart.Custom({
				drawFn:function(){
					//计算位置
					var coo = line.getCoordinate(),
						x = coo.get('originx'),
						y = coo.get('originy'),
						w = coo.width,
						h = coo.height;
					//在左上侧的位置，渲染一个单位的文字
					line.target.textAlign('start')
					.textBaseline('bottom')
					.textFont('600 11px 微软雅黑')
					.fillText('商品售量(个)',x-40,y-12,false,'#9d987a')
					.textBaseline('top')
					.fillText('(时间)',x+w+12,y+h+10,false,'#9d987a');
					
				}
		}));
		//开始画图
		line.draw();
		
	}
	function mSalesNumDraw(mOrderSumCash){
		var mOrderSum=[];
		var arrayOrder =mOrderSumCash.split(",");
		for (var i=0 ; i< arrayOrder.length ; i++){
			mOrderSum.push(arrayOrder[i]);
		}
		
		var data = [
		         	{
		         		name : '商品销量',
		         		value:mOrderSum,
		         		color:'#FF0000',
		         		line_width:2
		         	}
		         ];
         
		var labels = ["01号","02号","03号","04号","05号","06号","07号","08号","09号","10号","11号","12号","13号","14号","15号","16号","17号","18号","19号","20号","21号","22号","23号","24号","25号","26号","27号","28号","29号","30号","31号"];
		var monthlabels=[];
		//var m=labels.length-mOrderSum.length;
		for(var i=0;i<mOrderSum.length;i++){
			monthlabels.push(labels[i]);
		}
		var line = new iChart.LineBasic2D({
			render : 'monthChart',
			data: data,
			align:'center',
			title : '',
			subtitle : '',
			footnote : '',
			width : 1100,
			height : 400,
			tip:{
				enable:true,
				shadow:true,
				listeners:{
					 //tip:提示框对象、name:数据名称、value:数据值、text:当前文本、i:数据点的索引
					parseText:function(tip,name,value,text,i){
							return value+"个";
					}
				}
			},
			legend : {
				enable : true,
				row:1,//设置在一行上显示，与column配合使用
				column : 'max',
				valign:'top',
				sign:'bar',
				background_color:null,//设置透明背景
				offsetx:-80,//设置x轴偏移，满足位置需要
				border : true
			},
			crosshair:{
				enable:true,
				line_color:'#62bce9'
			},
			sub_option : {
				label:false,
				point_hollow : false
			},
			coordinate:{
				width:1000,
				height:240,
				axis:{
					color:'#9f9f9f',
					width:[0,0,2,2]
				},
				grids:{
					vertical:{
						way:'share_alike',
				 		value:5
					}
				},
				scale:[{
					 position:'left',	
					 //start_scale:0,
					 //end_scale:5000,
					 //scale_space:2,
					 scale_size:0,
					 scale_color:'#9f9f9f'
				},{
					
					 position:'bottom',	
					 labels:monthlabels
				}]
			}
		});
		//利用自定义组件构造左侧说明文本
		line.plugin(new iChart.Custom({
				drawFn:function(){
					//计算位置
					var coo = line.getCoordinate(),
						x = coo.get('originx'),
						y = coo.get('originy'),
						w = coo.width,
						h = coo.height;
					//在左上侧的位置，渲染一个单位的文字
					line.target.textAlign('start')
					.textBaseline('bottom')
					.textFont('600 11px 微软雅黑')
					.fillText('商品售量(个)',x-40,y-12,false,'#9d987a')
					.textBaseline('top')
					.fillText('(时间)',x+w+12,y+h+10,false,'#9d987a');
					
				}
		}));

		//开始画图
		line.draw();
		
	}
	function ySalesNumDraw(yOrderSumCash){
		var yOrderSum=[];
		var arrayOrder =yOrderSumCash.split(",");
		for (var i=0 ; i< arrayOrder.length ; i++){
			yOrderSum.push(arrayOrder[i]);
		}
		var data = [
		         	{
		         		name : '商品销量',
		         		value:yOrderSum,
		         		color:'#FF0000',
		         		line_width:2
		         	}
		         ];
         
		var yearlabels = ["01月","02月","03月","04月","05月","06月","07月","08月","09月","10月","11月","12月"];
		var line = new iChart.LineBasic2D({
			render : 'yearChart',
			data: data,
			align:'center',
			title : '',
			subtitle : '',
			footnote : '',
			width : 1100,
			height : 400,
			tip:{
				enable:true,
				shadow:true,
				listeners:{
					 //tip:提示框对象、name:数据名称、value:数据值、text:当前文本、i:数据点的索引
					parseText:function(tip,name,value,text,i){
							return value+"个";
					}
				}
			},
			legend : {
				enable : true,
				row:1,//设置在一行上显示，与column配合使用
				column : 'max',
				valign:'top',
				sign:'bar',
				background_color:null,//设置透明背景
				offsetx:-80,//设置x轴偏移，满足位置需要
				border : true
			},
			crosshair:{
				enable:true,
				line_color:'#62bce9'
			},
			sub_option : {
				label:false,
				point_hollow : false
			},
			coordinate:{
				width:640,
				height:240,
				axis:{
					color:'#9f9f9f',
					width:[0,0,2,2]
				},
				grids:{
					vertical:{
						way:'share_alike',
				 		value:5
					}
				},
				scale:[{
					 position:'left',	
					 //start_scale:0,
					 //end_scale:5000,
					 //scale_space:2,
					 scale_size:0,
					 scale_color:'#9f9f9f'
				},{
					 position:'bottom',	
					 labels:yearlabels
				}]
			}
		});
		//利用自定义组件构造左侧说明文本
		line.plugin(new iChart.Custom({
				drawFn:function(){
					//计算位置
					var coo = line.getCoordinate(),
						x = coo.get('originx'),
						y = coo.get('originy'),
						w = coo.width,
						h = coo.height;
					//在左上侧的位置，渲染一个单位的文字
					line.target.textAlign('start')
					.textBaseline('bottom')
					.textFont('600 11px 微软雅黑')
					.fillText('商品售量(个)',x-40,y-12,false,'#9d987a')
					.textBaseline('top')
					.fillText('(时间)',x+w+12,y+h+10,false,'#9d987a');
					
				}
		}));
		//开始画图
		line.draw();
		
	}
	function dateDraw(dOrderSumCash,dAppSumCash,dDevicesCash,analysisType){
		var dOrderSum=[],dAppSum=[],dDevicesSum=[];
		var arrayOrder =dOrderSumCash.split(",");
		var arrayApp=dAppSumCash.split(",");
		var arrayDevices=dDevicesCash.split(",");
		for (var i=0 ; i< arrayOrder.length ; i++){
			dOrderSum.push(arrayOrder[i]);
		}
		for (var i=0 ; i< arrayApp.length ; i++){
			dAppSum.push(arrayApp[i]);
		} 
		for (var i=0 ; i< arrayDevices.length ; i++){
			dDevicesSum.push(arrayDevices[i]);
		} 
		var data = [
		         	{
		         		name : '线上订单',
		         		value:dAppSum,
		         		color:'#00FF00',
		         		line_width:2
		         	},
		         	{
		         		name : '店面订单',
		         		value:dDevicesSum,
		         		color:'#0099FF',
		         		line_width:2
		         	},
		         	{
		         		name : '所有订单',
		         		value:dOrderSum,
		         		color:'#FF0000',
		         		line_width:2
		         	}
		         ];
         
		var datelabels = ["0点","1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点","14点","15点","16点","17点","18点","19点","20点","21点","22点","23点"];
		var line = new iChart.LineBasic2D({
			render : 'dateChart',
			data: data,
			align:'center',
			title : '',
			subtitle : '',
			footnote : '',
			width : 1100,
			height : 400,
			tip:{
				enable:true,
				shadow:true,
				listeners:{
					 //tip:提示框对象、name:数据名称、value:数据值、text:当前文本、i:数据点的索引
					parseText:function(tip,name,value,text,i){
						if(analysisType==1){
							return "￥"+value;
						}else{
							return value+"个";
						}
					}
				}
			},
			legend : {
				enable : true,
				row:1,//设置在一行上显示，与column配合使用
				column : 'max',
				valign:'top',
				sign:'bar',
				background_color:null,//设置透明背景
				offsetx:-80,//设置x轴偏移，满足位置需要
				border : true
			},
			crosshair:{
				enable:true,
				line_color:'#62bce9'
			},
			sub_option : {
				label:false,
				point_hollow : false
			},
			coordinate:{
				width:640,
				height:240,
				axis:{
					color:'#9f9f9f',
					width:[0,0,2,2]
				},
				grids:{
					vertical:{
						way:'share_alike',
				 		value:5
					}
				},
				scale:[{
					 position:'left',	
					 //start_scale:0,
					 //end_scale:5000,
					 //scale_space:2,
					 scale_size:0,
					 scale_color:'#9f9f9f',
				},{
					 position:'bottom',	
					 labels:datelabels
				}]
			}
		});
		//利用自定义组件构造左侧说明文本
		line.plugin(new iChart.Custom({
				drawFn:function(){
					//计算位置
					var coo = line.getCoordinate(),
						x = coo.get('originx'),
						y = coo.get('originy'),
						w = coo.width,
						h = coo.height;
					//在左上侧的位置，渲染一个单位的文字
					if(analysisType==1){
						line.target.textAlign('start')
						.textBaseline('bottom')
						.textFont('600 11px 微软雅黑')
						.fillText('销售额(元)',x-40,y-12,false,'#9d987a')
						.textBaseline('top')
						.fillText('(时间)',x+w+12,y+h+10,false,'#9d987a');
					}else{
						line.target.textAlign('start')
						.textBaseline('bottom')
						.textFont('600 11px 微软雅黑')
						.fillText('订单数量(个)',x-40,y-12,false,'#9d987a')
						.textBaseline('top')
						.fillText('(时间)',x+w+12,y+h+10,false,'#9d987a');
					}
					
				}
		}));
		//开始画图
		line.draw();
		
	}
	function monthDraw(mOrderSumCash,mAppSumCash,mDevicesCash,analysisType){
		var mOrderSum=[],mAppSum=[],mDevicesSum=[];
		var arrayOrder =mOrderSumCash.split(",");
		var arrayApp=mAppSumCash.split(",");
		var arrayDevices=mDevicesCash.split(",");
		for (var i=0 ; i< arrayOrder.length ; i++){
			mOrderSum.push(arrayOrder[i]);
		}
		for (var i=0 ; i< arrayApp.length ; i++){
			mAppSum.push(arrayApp[i]);
		} 
		for (var i=0 ; i< arrayDevices.length ; i++){
			mDevicesSum.push(arrayDevices[i]);
		} 
		var data = [
		         	{
		         		name : '线上订单',
		         		value:mAppSum,
		         		color:'#00FF00',
		         		line_width:2
		         	},
		         	{
		         		name : '店面订单',
		         		value:mDevicesSum,
		         		color:'#0099FF',
		         		line_width:2
		         	},
		         	{
		         		name : '所有订单',
		         		value:mOrderSum,
		         		color:'#FF0000',
		         		line_width:2
		         	}
		         ];
         
		var labels = ["01号","02号","03号","04号","05号","06号","07号","08号","09号","10号","11号","12号","13号","14号","15号","16号","17号","18号","19号","20号","21号","22号","23号","24号","25号","26号","27号","28号","29号","30号","31号"];
		var monthlabels=[];
		//var m=labels.length-mOrderSum.length;
		for(var i=0;i<mOrderSum.length;i++){
			monthlabels.push(labels[i]);
		}
		var line = new iChart.LineBasic2D({
			render : 'monthChart',
			data: data,
			align:'center',
			title : '',
			subtitle : '',
			footnote : '',
			width : 1100,
			height : 400,
			tip:{
				enable:true,
				shadow:true,
				listeners:{
					 //tip:提示框对象、name:数据名称、value:数据值、text:当前文本、i:数据点的索引
					parseText:function(tip,name,value,text,i){
						if(analysisType==1){
							return "￥"+value;
						}else{
							return value+"个";
						}
					}
				}
			},
			legend : {
				enable : true,
				row:1,//设置在一行上显示，与column配合使用
				column : 'max',
				valign:'top',
				sign:'bar',
				background_color:null,//设置透明背景
				offsetx:-80,//设置x轴偏移，满足位置需要
				border : true
			},
			crosshair:{
				enable:true,
				line_color:'#62bce9'
			},
			sub_option : {
				label:false,
				point_hollow : false
			},
			coordinate:{
				width:1000,
				height:240,
				axis:{
					color:'#9f9f9f',
					width:[0,0,2,2]
				},
				grids:{
					vertical:{
						way:'share_alike',
				 		value:5
					}
				},
				scale:[{
					 position:'left',	
					 //start_scale:0,
					 //end_scale:5000,
					 //scale_space:2,
					 scale_size:0,
					 scale_color:'#9f9f9f'
				},{
					
					 position:'bottom',	
					 labels:monthlabels
				}]
			}
		});
		//利用自定义组件构造左侧说明文本
		line.plugin(new iChart.Custom({
				drawFn:function(){
					//计算位置
					var coo = line.getCoordinate(),
						x = coo.get('originx'),
						y = coo.get('originy'),
						w = coo.width,
						h = coo.height;
					//在左上侧的位置，渲染一个单位的文字
					if(analysisType==1){
						line.target.textAlign('start')
						.textBaseline('bottom')
						.textFont('600 11px 微软雅黑')
						.fillText('销售额(元)',x-40,y-12,false,'#9d987a')
						.textBaseline('top')
						.fillText('(时间)',x+w+12,y+h+10,false,'#9d987a');
					}else{
						line.target.textAlign('start')
						.textBaseline('bottom')
						.textFont('600 11px 微软雅黑')
						.fillText('订单数量(个)',x-40,y-12,false,'#9d987a')
						.textBaseline('top')
						.fillText('(时间)',x+w+12,y+h+10,false,'#9d987a');
					}
					
				}
		}));
		//开始画图
		line.draw();
		
	}
	function yearDraw(yOrderSumCash,yAppSumCash,yDevicesCash,analysisType){
		var yOrderSum=[],yAppSum=[],yDevicesSum=[];
		var arrayOrder =yOrderSumCash.split(",");
		var arrayApp=yAppSumCash.split(",");
		var arrayDevices=yDevicesCash.split(",");
		for (var i=0 ; i< arrayOrder.length ; i++){
			yOrderSum.push(arrayOrder[i]);
		}
		for (var i=0 ; i< arrayApp.length ; i++){
			yAppSum.push(arrayApp[i]);
		} 
		for (var i=0 ; i< arrayDevices.length ; i++){
			yDevicesSum.push(arrayDevices[i]);
		} 
		var data = [
		         	{
		         		name : '线上订单',
		         		value:yAppSum,
		         		color:'#00FF00',
		         		line_width:2
		         	},
		         	{
		         		name : '店面订单',
		         		value:yDevicesSum,
		         		color:'#0099FF',
		         		line_width:2
		         	},
		         	{
		         		name : '所有订单',
		         		value:yOrderSum,
		         		color:'#FF0000',
		         		line_width:2
		         	}
		         ];
         
		var yearlabels = ["01月","02月","03月","04月","05月","06月","07月","08月","09月","10月","11月","12月"];
		var line = new iChart.LineBasic2D({
			render : 'yearChart',
			data: data,
			align:'center',
			title : '',
			subtitle : '',
			footnote : '',
			width : 1100,
			height : 400,
			tip:{
				enable:true,
				shadow:true,
				listeners:{
					 //tip:提示框对象、name:数据名称、value:数据值、text:当前文本、i:数据点的索引
					parseText:function(tip,name,value,text,i){
						if(analysisType==1){
							return "￥"+value;
						}else{
							return value+"个";
						}
					}
				}

				
			},
			legend : {
				enable : true,
				row:1,//设置在一行上显示，与column配合使用
				column : 'max',
				valign:'top',
				sign:'bar',
				background_color:null,//设置透明背景
				offsetx:-80,//设置x轴偏移，满足位置需要
				border : true
			},
			crosshair:{
				enable:true,
				line_color:'#62bce9'
			},
			sub_option : {
				label:false,
				point_hollow : false
			},
			coordinate:{
				width:640,
				height:240,
				axis:{
					color:'#9f9f9f',
					width:[0,0,2,2]
				},
				grids:{
					vertical:{
						way:'share_alike',
				 		value:5
					}
				},
				scale:[{
					 position:'left',	
					 //start_scale:0,
					 //end_scale:5000,
					 //scale_space:2,
					 scale_size:0,
					 scale_color:'#9f9f9f'
				},{
					 position:'bottom',	
					 labels:yearlabels
				}]
			}
		});
		//利用自定义组件构造左侧说明文本
		line.plugin(new iChart.Custom({
				drawFn:function(){
					//计算位置
					var coo = line.getCoordinate(),
						x = coo.get('originx'),
						y = coo.get('originy'),
						w = coo.width,
						h = coo.height;
					//在左上侧的位置，渲染一个单位的文字
					if(analysisType==1){
						line.target.textAlign('start')
						.textBaseline('bottom')
						.textFont('600 11px 微软雅黑')
						.fillText('销售额(元)',x-40,y-12,false,'#9d987a')
						.textBaseline('top')
						.fillText('(时间)',x+w+12,y+h+10,false,'#9d987a');
					}else{
						line.target.textAlign('start')
						.textBaseline('bottom')
						.textFont('600 11px 微软雅黑')
						.fillText('订单数量(个)',x-40,y-12,false,'#9d987a')
						.textBaseline('top')
						.fillText('(时间)',x+w+12,y+h+10,false,'#9d987a');
					}
					
				}
		}));
		//开始画图
		line.draw();
		
	}
</script>
</head>
<body>
	<form>
		<div class="panel">
			<div class="panel-heading">
				<strong><i class="icon-list-ul"></i>交易分析</strong>
				<div class="input-group" style="max-width: 1000px;">
					<c:choose>  
					   <c:when test="${isSys==true }">  
					   		<span class="input-group-addon">商家</span>
							<input name="storeName" id="reportProduct_storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="" class="form-control" isRequired="1" tipName="商家"  />
							<input type="hidden" name="storeId" id="reportProduct_storeId" value="" />
					   </c:when>  
					   <c:otherwise> 
					   		<m:hasPermission permissions="mainShopOrderStoreView">
					   			<c:if test="${isMainStore==true }">
									<span class="input-group-addon">商家</span>
									<input name="storeName" id="reportProduct_storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="" class="form-control" isRequired="1" tipName="商家"  />
									<input type="hidden" name="storeId" id="reportProduct_storeId" value="-1" />
								</c:if>
		       				</m:hasPermission>  
					   </c:otherwise>  
					</c:choose>
           			<span class="input-group-addon">选择商品</span>
					<input name="stockName" id="reportProduct_stockName" style="width: 200px;margin-right:40px;" readonly="readonly" value="" class="form-control" isRequired="1" tipName="商家"  />
					<input type="hidden" name="stockId" id="reportProduct_stockId" value="" />
					
					<span class="input-group-addon">分析类型</span>
					<select name="analysisType" class="form-control" style="width: auto;margin-right:40px;" id="analysisType" >
		           		<option  value ="0">订单数量</option>
						<option  value ="1">销售额</option>
<!-- 						<option  value ="2">商品销量</option> -->
		           	</select>
				</div>
				<br />
				<div class="input-group">
					<button id="submitButton" class="btn btn-info"  name="submitButton" type="button" onclick="getAll();">搜索</button>
					<button id="fresetButton" class="btn btn-success"  name="resetButton" type="button">重置</button>
				</div>
			</div>
			<div class="panel-heading">
				<div class="input-group" style="max-width: 1000px;">
					<span class="input-group-addon">日订单趋势</span>
					<span class="input-group-addon">快速查看</span>
					<select name="dateType" class="form-control" style="width: auto;margin-right:40px;" id="dateType">
						<option  value ="">请选择</option>
		           		<option  value ="0" selected="selected">最近7天</option>
						<option  value ="1">最近30天</option>
						<option  value ="2">最近90天</option>
						<option  value ="3">最近1年</option>
		           	</select>
		           	 
		           	<input id="dateBeginTime" value="" type="text" name="dateBeginTime" class="form-control" readonly style="width: 200px;">
		           	<label style="position: relative;float: left;padding: 5px 8px;">至</label>
		           	<input id="dateEndTime" value="" type="text" name="dateEndTime" class="form-control" readonly style="width: 200px;margin-right:40px;">
					<select name="dateOrderType" class="form-control" style="width: auto;margin-right:40px;" id="dateOrderType" >
		           		<option  value ="0">总订单</option>
						<option  value ="1">平均订单</option>
		           	</select>
		           	<button id="dateButton" class="btn btn-info"  name="dateButton" type="button" onclick="getAll();">确认</button>
				</div>
			</div>
			<!-- 日订单趋势开始 -->
			<div class="panel-body" style="overflow-x:auto;">
				<div id="dateChart" width="1000" height="600"></div>
			</div>
			<!-- 日订单趋势结束 -->
			
			<div class="panel-heading">
				<div class="input-group" style="max-width: 1000px;">
					<span class="input-group-addon">月订单趋势</span>
					<span class="input-group-addon">快速查看</span>
		           	<input id="monthTime" value="" type="text" name="monthTime" class="form-control" readonly style="width: 200px;margin-right:40px;" >
					<select name="monthOrderType" class="form-control" style="width: auto;margin-right:40px;" id="monthOrderType" >
		           		<option  value ="0">总订单</option>
<!-- 						<option  value ="1">平均订单</option> -->
		           	</select>
		           	<button id="dateButton" class="btn btn-info"  name="dateButton" type="button" onclick="getAll();">确认</button>
				</div>
			</div>
			<!-- 月订单趋势开始 -->
			<div class="panel-body" style="overflow-x:auto;">
				<div id="monthChart" width="1000" height="600"></div>
			</div>
			<!-- 月订单趋势结束 -->
			
			<div class="panel-heading">
				<div class="input-group" style="max-width: 1000px;">
					<span class="input-group-addon">年订单趋势</span>
					<span class="input-group-addon">快速查看</span>
		           	<input id="yearTime" value="" type="text" name="yearTime" class="form-control" readonly style="width: 200px;margin-right:40px;" >
					<select name="yearOrderType" class="form-control" style="width: auto;margin-right:40px;" id="yearOrderType" >
		           		<option  value ="0">总订单</option>
						<!-- <option  value ="1">平均订单</option> -->
		           	</select>
		           	<button id="dateButton" class="btn btn-info"  name="dateButton" type="button" onclick="getAll();">确认</button>
				</div>
			</div>
			<!-- 年订单趋势开始 -->
			<div class="panel-body" style="overflow-x:auto;">
				<div id="yearChart" width="1000" height="600"></div>
			</div>
			<!-- 年订单趋势结束 -->
		</div>
	</form>
	<m:select_store path="${contextPath}/reportProduct/showModel/list/list-data" modeId="reportProductShowStore" callback="callback"> </m:select_store>
	<!-- 模态窗 -->
	<div class="modal fade" id="stockNameAddModal">
		<div class="modal-dialog modal-lg" style="width: 1200px;">
		  <div class="modal-content">
			    <div class="modal-header">
			      <button type="button" class="close" data-dismiss="modal" id="productstockListModalBtn"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
			      <h4 class="modal-title"></h4>
			    </div>
				<div class="modal-body">
					<m:list title="商品列表" id="productStockList" listUrl="${contextPath }/transactionAnalysis/list/list-data" callback="productListDataIdCallback"  searchButtonId="productStockList_search_btn" >
						<div class="input-group" style="max-width: 1500px;">
							 <input type="hidden" id="product_storeId" name="product_storeId" value=""/> 
							 <span class="input-group-addon">商品名称</span> 
			            	 <input type="text" id="product_name" name="product_name" class="form-control" placeholder="商品名称" style="width: 200px;">
						</div>
					</m:list>
				</div>
			</div>
		</div>
	</div>
	<!-- 模态窗 -->
</body>
</html>