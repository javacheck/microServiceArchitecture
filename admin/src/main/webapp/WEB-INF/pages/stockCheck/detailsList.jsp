<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>库存盘点详情</title>
<script type="text/javascript">
$(function(){
	$('.boult').css('border-bottom', '8px solid #5B5B5B');
    $('.boult').css('border-top', '8px solid #5B5B5B');
});
function getExe() {
	var stockCheckId=$("#stockCheckId").val();//盘点编号
	var productName =$("#productName").val();//商品名称
	var barCode = $("#barCode").val();//条码
	window.open("${contextPath}/stockCheck/list/ajax/detailslist-by-search?productName="+productName
					+"&barCode="+barCode+"&stockCheckId="+stockCheckId);
}
function callback(){
	$("[name='numAsc']").click(function (){
		$('.boult').css('border-bottom', '8px solid #5B5B5B');
        $('.boult').css('border-top', '8px solid #5B5B5B');
        var sortValue=$(this).attr("value");
        $("#sort").val(sortValue);
        loadList_detailsList();
       
		
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
        width:14%;
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
background-clip: padding-box;

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
	<m:list title="库存盘点详情" id="detailsList" callback="callback"
		listUrl="${contextPath }/stockCheck/detailsList/detailsList-data" 
		searchButtonId="cateogry_search_btn" formReset="formReset">
		
		<div class="input-group" style="max-width:1500px;">
			<input type="hidden" name="sort" id="sort" value="0" />
            <input type="hidden" id="stockCheckId" name="stockCheckId" value="${stockCheckId}">
            <span class="input-group2">商品名称</span> 
            <input type="text" id="productName" name="productName" class="form-control" placeholder="商品名称" style="width: 200px;float:left;margin-right:40px;">
            
             <span class="input-group2">商品条码</span> 
            <input type="text" id="barCode" name="barCode" class="form-control" placeholder="商品条码" style="width: 200px;float:left;margin-right:40px;">	
		</div>
			<button type="button" onclick="getExe();" class="btn btn-small btn-danger" style="width: auto;margin-top:5px;">导出报表</button>
	</m:list>
</body>
</html>