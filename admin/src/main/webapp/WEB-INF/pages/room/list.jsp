<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="${contextPath}/static/css/calendar/calendar.css">
<style type="text/css">
html {
	font: 500 14px 'roboto';
	color: #333;
	background-color: #fafafa;
}
ul, ol, li {
	list-style: none;
	padding: 0;
	margin: 0;
}
#demo {
	width: 300px;
	margin: 150px auto;
}
p {
	margin: 0;
}
#dt {
	margin: 30px auto;
	height: 28px;
	width: 200px;
	padding: 0 6px;
	border: 1px solid #ccc;
	outline: none;
}
</style>
<script src="${contextPath}/static/js/calendar/calendar.js"></script>
<script src="${contextPath}/static/js/map/map.js"></script>  
<title>包间列表</title>
<script type="text/javascript">
	$(function(){
		var msg = "${room_warning_msg}";
		if (msg != '') {
			lm.alert(msg);
		}
	});
	function deleteConfirm(id) {
		lm.confirm("确定要删除吗？", function() {
			lm.post("${contextPath }/room/delete/" + id, {}, function(data) {
				if (data == 1) {
					noty("删除成功");
					loadCurrentList_accountList();
				}
			});
		});
	}
	function changStatus(id,status){
		var info = status==0?"关闭":"开启";
		lm.confirm("确定要"+info+"吗？", function() {
			lm.post("${contextPath }/room/changStatus", {id:id,status:status}, function(data) {
				if (data == 1) {
					noty(info+"成功");
					loadCurrentList_roomList();
				}
			});
		});
	}
	function beforeSearch(){
		return true;
	}
	var index=1;
	$(function(){
		$("#roomTimeEndRemindButtonId").click(function(){
			index=1;
			var i =1;
			$("td[name='titleName']").each(function(){
				if (i>1) {
					$(this).parent().remove();
				}
				i++;
			});
			$("input[name='titleValue']").each(function(){
				$(this).val('');
			});
			
			lm.post("${contextPath }/roomTimeEndRemind/find", {}, function(data) {
				if (data != -1) {
					var list = new Array();
					list=data;
					index=list.length;
					for (var j = 0; j < list.length; j++) {
						var roomTimeEndRemind = list[j];
						if (j==0) {
							$("input[name='titleValue']").each(function(){
								$(this).val(roomTimeEndRemind.minute);
							});
						}else{
							var c = j+1;
							var k = j+500;
							$("#roomTimeEndRemindTableId").append('<tr><td style="line-height: 30px;" name = "titleName" width="20%">提醒'+c+':</td> <td style="line-height: 30px;">提前<input id = "'+k+'" data-rule="required;range[1~99]" class="form-control" style="width: 80px; display: inline-block; margin:0 10px;" name = "titleValue" value="'+roomTimeEndRemind.minute+'" style="width: 80px" type="text">分钟<span class="msg-box n-right" style="position:static;" for="'+k+'"></span></td><td><input name="deleteButtenName" onClick="getDel(this)" value="删除" type="button"></td></tr>');
						}
					}
				}
				$('#roomTimeEndRemindShowId').modal();//弹窗			
			});
		});
		
		$("#addTr").click(function() {
			if (index>=3) {
				lm.alert('最多可添加三个提醒');
				return ;
			}
			index++;
			var k = index+5000;
			$("#roomTimeEndRemindTableId").append('<tr><td style="line-height: 30px;" name = "titleName" width="20%">提醒'+index+':</td> <td style="line-height: 30px;">提前<input id = "'+k+'" data-rule="required;range[1~99]" class="form-control" style="width: 80px; display: inline-block; margin:0 10px;" name = "titleValue" style="width: 80px" type="text">分钟<span class="msg-box n-right" style="position:static;" for="'+k+'"></span></td><td><input name="deleteButtenName" onClick="getDel(this)" value="删除" type="button"></td></tr>');
		});
		
		$("#roomTimeEndRemindSubmitId").click(function() {
			var i = 1;
			var list = new Array();
			$("input[name='titleValue']").each(function(){
				var obj = new Object();
				obj.minute = $(this).val();
				obj.order= i;
				list.push(obj);
				i++;
			});
			//alert(JSON.stringify(list));
			$('#roomTimeEndRemindFormId').isValid(function(v){
				if (v) {
					lm.post("${contextPath }/roomTimeEndRemind/add", {json:JSON.stringify(list)}, function(data) {
						if (data == 1) {
							noty("保存成功");
						}
						$('#roomTimeEndRemindCloseId').click();
					});
				}
			});
			
		});
		$("#roomTimeEndRemindCancelBtn").click(function() {
			$('#roomTimeEndRemindCloseId').click();//关闭弹出页面
		});
		
		
		
	});
	
	function getDel(k){
		$(k).parent().parent().remove();
		index--;
		var i  = 1;
		$("td[name='titleName']").each(function(){//删除后重新排序
			$(this).text('提醒'+i+':');
			i++;
		});
	}
	
	
	//---------------------------------------------------------------节假日---------------------------------------
	$(function(){
		var calendarObj;
		$('#roomShowID').on('hidden.zui.modal', function() {
			cacheMap.clear();
			$("#selectYear").find("option").remove();
			$(calendarObj).data('calendar',null);
		});
		
		$("#roomCancelBtn").click(function(){
			$("#roomClose").click();
		});
		
		var cacheMap = new Map();
		$("#user-defined").click(function(){
			cacheMap.clear();
			selectYearSetting();
			lm.postSync("${contextPath}/room/roomDateSetting/ajax/addOrUpdateByHoliday",{},function(data){
				if( null != data && "" != data ){
					for( var i=0 ; i< data.length ; i++ ){
						var tt=new Date(data[i].holiday);
						var year_month = formatDateGetKey(tt)+"";
						var day = formatDateGetValue(tt); 
						 if( cacheMap.containsKey(year_month) ){
			        	    var cacheArray = cacheMap.get(year_month);
			        	    cacheArray.push(day);
			        	    cacheMap.remove(year_month);
			        	    cacheMap.put(year_month,cacheArray);
			             } else {
			                var array = new Array();
			                array.push(day);
				            cacheMap.put(year_month,array );	        	   
			             }
					}
				}
			});
			createCalendar();	
			$("#roomShowID").modal();
		});
		
		$("#roomAddBtn").click(function(){
			
			var cacheArrayData = new Array();
			
			for( var i = 0 ; i < cacheMap.size() ; i++ ){
				var tempKey = cacheMap.element(i).key;
				var tempArray = cacheMap.get(tempKey);
				for( var j = 0 ; j < tempArray.length ; j++ ){
					cacheArrayData.push(tempKey+"-"+tempArray[j]);
				}
			}
			
			$("#cacheMapData").val("");
			$("#cacheMapData").val(cacheArrayData);
			var cacheMapData = $("#cacheMapData").val(); 
			lm.postSync("${contextPath}/room/roomDateSetting/ajax/saveHoliday",{cacheMapData:cacheMapData},function(data){
				$("#cacheMapData").val("");
				$("#roomClose").click();
			});
		});
		function formatDateGetKey(now)   {     
            var year=now.getFullYear();     
            var month=now.getMonth()+1;     
            return year+"-"+month;
		}
		
		function formatDateGetValue(now)   {     
            var date=now.getDate(); 
            return date;
		}
		
		 function createCalendar(){
			 $("#calendarID").off('click',"[data-calendar-day]").off('click',"[data-calendar-month]");
			 calendarObj = $('#calendarID').calendar({
			        width: 420,
			        height: 400,
			        weekArray: ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'],
			        data: [
						{
						  date: '2015/12/24',
						  value: 'Christmas Eve'
						},
						{
						  date: '2015/12/25',
						  value: 'Merry Christmas'
						},
						{
						  date: '2016/01/01',
						  value: 'Happy New Year'
						}
					],
					date: new Date(),
			        onSelected: function (view, date, data,_this) {
			        	var year_month = date.format("yyyy-mm")+"";
			        	var day = date.format("dd");
			        	var a = _this.$dateItems.children(':eq(1)').find('[data-calendar-day]:not(.new, .old)');
			        	var z = a.filter(function(index) {
	            			return parseInt(this.innerHTML) === parseInt(day);
	            		});
			        	var flag = false; // 默认是取消
			        	if( z.attr("class") == 'selected' ){
			        		flag = true;
			        	}
			            if( cacheMap.containsKey(year_month) ){
			        	   var cacheArray = cacheMap.get(year_month);
			        	   if(flag){ // 新增
			        		   if( -1 == cacheArray.indexOf(day) ){
				        		   cacheArray.push(day);			        			   
				        		   cacheMap.remove(year_month);
					        	   cacheMap.put(year_month,cacheArray);	
			        		   } 
			        	   } else {
				        	   if( cacheArray.length <= 1 ){ // 如果只有一个,则移除map集合
			        			   cacheMap.remove(year_month); 
			        		   } else {
			        			   if( -1 !=  $.inArray(parseInt(day) ,cacheArray) ){
			        					cacheArray.splice($.inArray(parseInt(day) ,cacheArray),1); // 移除指定的元素
			        			   }
			        			   cacheMap.remove(year_month);
			        			   cacheMap.put(year_month,cacheArray);	
			        		   }
			        	   }
			            } else {
			            	if(flag){
				               var array = new Array();
				               array.push(day);
					           cacheMap.put(year_month,array);	        	   			            		
			            	}
			            }
			        },
			        viewChange: function (view, date, data,_this) {
			            var year_month = date +"-"+ data;
			        	if( cacheMap.containsKey(year_month) ) {
			        		var cacheArray = cacheMap.get(year_month);
			        		for( var i =0 ;i < cacheArray.length ; i++ ){
			        			_this.selectedDay(cacheArray[i],'',true);
			        		}
			        	} 
			        },onMouseenter: function(view,date,data){
			        	/*
			        	alert(_this);
			        	markData = this.getDayData(idt);

			             $item = $(DAY_ITEM_TPL.repeat(data));

			             if (markData) {
			                 $item.data(MARK_DATA, markData);
			                 $item.html(d + MARK_DAY_HTML);
			             }
			        	alert("-----------------"+view +"<<<<---- "+ date+"----->>>"+data);
			             */
			        },getDataArray:function(){
			        	var dataArray;
			        	if( cacheMap.containsKey(currentDate) ){
				        	 var cacheArray = cacheMap.get(currentDate);
				        	 dataArray = cacheArray;
				        } 
			        	
			        	return dataArray;
			        }
			    });
			 
			 $("#selectYear").change(function(){
				 var month = 1;
				 if( null != currentDate && undefined != currentDate && "" != currentDate ){
					 month = currentDate.split("-")[1];					 
				 }
				 $(calendarObj).data('calendar').updateDisDate($(this).val(),month);
				 $(calendarObj).data('calendar').updateMonthView($(this).val());
				 $(calendarObj).data('calendar').fillDateItems($(this).val(), month);
			 });
		 }
	});
	
	function selectYearSetting(){
		var notTime = new Date();
		var year = notTime.format("yyyy");
		for( var x =1 ; x < 4 ; x++ ){
			$("#selectYear").append("<option>"+(parseInt(year)-(4-x))+"</option>");							
		}
		for( var y =0 ; y < 4 ; y++ ){
			$("#selectYear").append("<option>"+(parseInt(year)+y)+"</option>");							
		}
		$("#selectYear").val(year);
	}
	//---------------------------------------------------------------节假日---------------------------------------updateDisDate
</script>
</head>
<body>
	<m:hasPermission permissions="accountAdd" flagName="addFlag"/>


	<m:list title="包间列表" id="roomList" beforeSearch="beforeSearch"
		listUrl="/room/list-data"
		addUrl="${addFlag == true ? '/room/add' : '' }" addName="新增包间" searchButtonId="user_search_btn">
		<div class="input-group" style="max-width: 1300px;">
			<span  class="input-group-addon">包间号码</span> 
			<input type="text"	name="number" class="form-control" placeholder="请输入包间号码" style="width: 200px;">
			<span  class="input-group-addon">包间状态</span> 
			<select name="status" style="width: auto" class="form-control">
				<option value="-1">所有</option>
				<option value="0">关闭</option>
				<option value="1">空闲</option>
				<option value="2">使用中</option>
				<option value="3">预定中</option>
		    </select>
			<span  class="input-group-addon">包间类型</span> 
			<select name=categoryId style="width: auto" class="form-control">
				<option  value="-1">全部</option>
				<c:forEach items="${categorys}" var="category">
					<option  value="${category.id }">${category.name }</option>
				</c:forEach>
		    </select>
		</div>
		 <input id="user-defined" name="user-defined" class="btn btn-info" type="button" style="width: auto;margin-top:5px;" value="自定义节假日">
		 <input id = "roomTimeEndRemindButtonId"  style="width: auto;margin-top:5px;" type="button" class="btn btn-info" value="计时提醒设置">
		 <a href='${contextPath }/room/roomDateSetting/addOrUpdateBy/' class='btn btn-small btn-warning' style="width: auto;margin-top:5px;">时间段设置</a>
		 
		  <input type="hidden" name="cacheMapData" id="cacheMapData" value="">
		  <!-- 自定义节假日展示start -->
		  <div class="modal fade" id="roomShowID">
			 <div  class="modal-dialog modal-lg" style="width:500px">
				  <div class="modal-content">
					    <div class="modal-header">
					    	<i style="center">自定义节假日</i>
					      <button type="button" id="roomClose" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
					    </div>
					    <div class="modal-body" id="calendarID_parent">
					    		<select id="selectYear" style="text-align: rigth;width:100px; float: right;" class="form-control">
					    			
					    		</select>
								<div class="input-group" id="calendarID" style="max-width: 800px;">
									
								</div>
								<input style="margin-left: 5px;" type="button" id='roomCancelBtn' class='btn btn-primary' value="取消"/>
								<input style="margin-right: 5px;" type="button" id='roomAddBtn' class='btn btn-primary' value="保存"/>		
					    </div>
				  </div>
			</div>
		</div>
	  <!-- 自定义节假日展示end -->
	</m:list>
	
	
	<div class="modal fade" id="roomTimeEndRemindShowId">
		 <div class="modal-dialog modal-lg" style="width:500px">
			  <div class="modal-content">
				    <div class="modal-header">
				    	<i style="center">计时提醒设置</i>
				      <button type="button" id="roomTimeEndRemindCloseId" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
				    </div>
				    <form id  ="roomTimeEndRemindFormId" action="">
					    <div class="modal-body" id="calendarID_parent">
					    		<table id = "roomTimeEndRemindTableId" class="table datatable">
					    			<tr><td  style="line-height: 30px;" name = "titleName" width="20%">提醒1:</td> <td style="line-height: 30px;">提前<input id= "titleFirst" data-rule="required;range[1~99]" class="form-control" style="width: 80px; display: inline-block; margin:0 10px;" name = "titleValue"  type="text">分钟<span class="msg-box n-right" style="position:static;" for="titleFirst"></span> </td><td><input id="addTr" value="添加" type="button"></td></tr>
					    		</table>
								<div class="input-group" id="calendarID" style="max-width: 800px;">
									
								</div>
								<input style="margin-left: 5px;" type="button" id='roomTimeEndRemindCancelBtn' class='btn btn-primary' value="取消"/>
								<input style="margin-right: 5px;" type="button" id='roomTimeEndRemindSubmitId' class='btn btn-primary' value="保存"/>		
					    </div>
				    </form>
			  </div>
		</div>
	</div>
</body>
</html>