<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>会员折扣</title>
<script type="text/javascript">
	var discounts= new Array();
	discounts = ${empty discounts ? '': discounts };
	$(function(){
		$("#setDiscountBtn").click(function(){
			var discount = $("#discount").val();
			var storeId = $("#storeId").val();
			if (lm.isFloat(discount) && (discount > 0 && discount <= 1)){
				lm.post("${contextPath}/discount/set",{discount:discount,storeId:storeId},function(data){
					lm.noty("设置成功");
				});
			}else {
				lm.alert("请输入正确的折扣");
			}
		});//setDiscountBtn 按钮点击事件结束
		
		dealDiscount();
		
		$("#storeId").change(function(){
			dealDiscount();
		});
	});
	
	//
	function dealDiscount(){
		$("#setDiscountBtn").val('设置');//默认VAL
		$("#setDiscountBtn").removeClass().addClass("btn btn-small btn-primary");//默认CSS 
		$('#discount').val('');
		var val=$("#storeId").val();
		for (var i = 0; i < discounts.length; i++) {
			if (discounts[i].storeId==val) {
				$('#discount').val(discounts[i].discount);
				$("#setDiscountBtn").val('修改');
				$("#setDiscountBtn").removeClass().addClass("btn btn-small btn-warning");//默认CSS 
				//btn btn-small btn-warning
			}
		}
	}
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong><i class='icon-plust'></i>设置折扣</strong>
		</div>
		<div class='panel-body'>
			<form class='form-horizontal'>
				
				<div class="form-group">
					<label class="col-md-2 control-label">商店</label>
					<div class="col-md-4">
						<select id = "storeId" name ="storeId" class='form-control' style="width: 535px;">
							<c:forEach items="${stores }" var="store">
								<option id="${store.id}"  value="${store.id}">${store.name}</option>
		           			 </c:forEach>  
						</select>
		            </div>
		            <label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">折扣</label>
					<div class="col-md-4">
						<input type='text' maxlength="3" id="discount" name="discount"
							value="${discount.discount }" class='form-control' />
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>

				<div class="form-group">
					<div class="col-md-offset-2 col-md-10">
						<input type='button' id='setDiscountBtn' class='btn btn-small btn-primary'
							value="设置" />
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>