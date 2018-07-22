<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>会员折扣</title>
<script type="text/javascript">
	$(function(){
		$("#setDiscountBtn").click(function(){
			var discount = $("#discount").val();
			if (lm.isFloat(discount) && discount > 0){
				lm.post("${contextPath}/discount/set",{discount:discount},function(data){
					lm.noty("设置成功");
				});
			}else {
				lm.alert("请输入正确的折扣");
			}
		});
	});
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
					<label class="col-md-2 control-label">折扣</label>
					<div class="col-md-4">
						<input type='text' id="discount" name="discount"
							value="${discount.discount }" class='form-control' />
					</div>
				</div>

				<div class="form-group">
					<div class="col-md-offset-2 col-md-10">
						<input type='button' id='setDiscountBtn' class='btn btn-primary'
							value="设置" />
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>