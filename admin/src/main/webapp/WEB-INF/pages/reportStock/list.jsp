<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>库存统计</title>
<script type="text/javascript">
function callback(obj){
	$("#reportStock_storeName").val(obj.name);
	$("#reportStock_storeId").val(obj.id);
}

function formReset(){
	$("#reportStock_storeName").val("");
	$("#reportStock_storeId").val("");
}
$(function(){
	$('.boult').css('border-bottom', '8px solid #5B5B5B');
    $('.boult').css('border-top', '8px solid #5B5B5B');
	
	// 点击弹出商家选择窗口
	$("#reportStock_storeName").click(function (){
		$("#reportStockShowStore").modal();
	});
	
});
//修改选择的店铺时触发
function byStoreAjaxRefreshList(){
	loadList_reportStockList(); 
}

 
function getExe() {
	var flag=${isSys};
	var storeId="";
	if(flag){
		storeId=$("#reportStock_storeId").val();//店铺
	}
	var sort=$("#sort").val();
	var name=$("#name").val();
	var barCode=$("#barCode").val();
	window.open("${contextPath}/reportStock/list/ajax/list-by-search?name="+name
					+"&barCode="+barCode+"&storeId="+storeId+"&sort="+sort);
}
function callback1(){
	$("[name='numAsc']").click(function (){
		$('.boult').css('border-bottom', '8px solid #5B5B5B');
        $('.boult').css('border-top', '8px solid #5B5B5B');
        var sortValue=$(this).attr("value");
        $("#sort").val(sortValue);
        loadList_reportStockList();
       
		
	});
	$("[name='numAsc']").each(function(index,item){
		//alert($(this).attr("value")+"===="+$("#sort").val());
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
        width:20%;
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
	<m:list title="库存统计" id="reportStockList" callback="callback1"
		listUrl="${contextPath }/reportStock/list/list-data" 
		searchButtonId="cateogry_search_btn" formReset="formReset">
		
		<div class="input-group" style="max-width:1500px;">
			<input type="hidden" name="sort" id="sort" value="0" />
			<c:choose>  
			   <c:when test="${isSys==true }">  
			   		<span class="input-group2">商家</span>
					<input name="storeName" id="reportStock_storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="" class="form-control" isRequired="1" tipName="商家"  />
					<input type="hidden" name="storeId" id="reportStock_storeId" value="" />
			   </c:when>  
			   <c:otherwise> 
			   		<m:hasPermission permissions="mainShopOrderStoreView">
			   			<c:if test="${isMainStore==true }">
							<span class="input-group2">商家</span>
							<input name="storeName" id="reportStock_storeName" style="width: 200px;margin-right:40px;" readonly="readonly" value="" class="form-control" isRequired="1" tipName="商家"  />
							<input type="hidden" name="storeId" id="reportStock_storeId" value="-1" />
						</c:if>
       				</m:hasPermission>  
			   </c:otherwise>  
			</c:choose>  
			
            <span class="input-group2">商品名称</span> 
            	<input type="text" id="name" name="name" class="form-control" placeholder="商品名称" style="width: 200px;float:left;margin-right:40px;">
            	
             <span class="input-group2">商品条码</span> 
            	<input type="text" id="barCode" name="barCode" class="form-control" placeholder="商品条码" style="width: 200px;float:left;margin-right:40px;">	
		 
		</div>
		<a href="" onclick="getExe()" class="btn btn-small btn-danger" style="width: auto;margin-top:5px;">导出报表</a>
	</m:list>
	<m:select_store path="${contextPath}/reportStock/showModel/list/list-data" modeId="reportStockShowStore" callback="callback"> </m:select_store>
</body>
</html>