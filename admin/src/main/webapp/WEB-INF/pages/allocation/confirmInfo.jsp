<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>确认到货明细</title>
<style type="text/css">
</style>
<script type="text/javascript">
function confirmTypeChage(){
	var id=${arss[0].allocationRecordId}
	var confirmAmounts="";
	var stockIds="";
	var flag=true;
	$("input[name='confirmAmount']").each(function(index,item){
		var confirmAmount=$.trim($(this).val());
		var amount=$(this).parent().prev().html();
		var temp=$(this).parent().prev().prev().prev().html();
		var attributeValues=$(this).parent().prev().prev().html();
		var sName;
		if(attributeValues!=null && attributeValues!=''){
			sName=temp+"|"+attributeValues;
		}else{
			sName=temp;
		}
		
		if(confirmAmount==null || confirmAmount==''){
			flag=false;
			lm.alert("商品:<span style='color:red;'>"+sName+"</span>实际到货数量不能为空！");
			return;
		}else if( isDouble(confirmAmount)==false){
			flag=false;
			lm.alert("<span style='color:red;'>"+sName+"</span>实际到货数量必须为整数或是两位小数的数字！");
			return;
		}else if(confirmAmount<0){
			flag=false;
			lm.alert("<span style='color:red;'>"+sName+"</span>实际到货数量的商品数量必须为大于等于0！");
			return;
		}else{
			if(Number(amount)<Number(confirmAmount)){
				flag=false;
				lm.alert("<span style='color:red;'>"+sName+"</span>实际到货数量不能大于申请数量！");
				return;
			}else{
				confirmAmounts += $(this).val()+",";
			}
		}
	});
	if(flag){
		confirmAmounts=confirmAmounts.substring( 0,confirmAmounts.length-1);
		lm.confirm("确定要收货吗？",function(){
			$("#auditModalBtn").click();
			lm.post("${contextPath }/allocation/confirm/change-by-allocationId",{status:4,confirmAmounts:confirmAmounts,id:id},function(data){
				if(data==1){
					lm.alert("操作成功！");
					loadCurrentList_allocationList();
				}
			});
		});
	}else{
		return;
	}
	
}
function isDouble(doubleName){
	var length = doubleName.length;
    if(doubleName.indexOf(".")>0){
	        var first = doubleName.indexOf(".");//判断第一个小数点所在位置
	        var last = doubleName.lastIndexOf(".");//判断最后一个小数点所在的位置
	        var temp_length = doubleName.split(".").length - 1;//含有.的个数
	        if(!isNaN(doubleName) && (temp_length == 1) && (first==last) && (length - last <=3) ){
	            return true;
	        }else{
	        	return false;
	        }
    }else{
    	return true;
    }
}
</script>
</head>
<body>
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" id="auditModalBtn"> 
					<span aria-hidden="true">×</span><span class="sr-only">关闭</span>
				</button>
				<h4 class="modal-title">确认到货明细</h4>
			</div>
				<div>
					<table style="width: 100%;height: 100%" class='table table-hover table-striped table-bordered'>
						<thead>
							<tr>
								<th style="text-align: center;">序号</th>
								<th style="text-align: center;">商品名称</th>
								<th style="text-align: center;">规格</th>
								<th style="text-align: center;">申请数量</th>
								<th style="text-align: center;">实际到货数量</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${arss}" var="allocationRecordStock" varStatus="v">
								<tr class='text-center'>
									<td>${v.index+1}</td>
									<td>
										${allocationRecordStock.productName}
									</td>
									<td>${allocationRecordStock.attributeName}</td>
									<td>${allocationRecordStock.amount}</td>
									<td value="${allocationRecordStock.stockId}"><input type='text' id='confirmAmount' name='confirmAmount'  value='${allocationRecordStock.amount}' style='width:50px;height:30px;' maxlength='7'/></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div class='text-center'>
					<button type="button"  name='typeChangeButton' onclick="confirmTypeChage();" class="btn btn-primary ">确认收货</button>
				</div>
		</div>
	</div>
</body>
</html>