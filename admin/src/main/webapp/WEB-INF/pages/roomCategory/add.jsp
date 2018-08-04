<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty roomCategory ? '添加' : '修改' }包间类型 </title>
<style type="text/css">

.man{
    color: red;
    font-size: 15px;
    text-align: left;
}
.tex-ct{
    text-align: center;
}
.tan{
    text-align:center;
    float:left;
}
.tex-ri{
    text-align:right;
    float:left;
}
</style>
<script type="text/javascript">
	$(function(){
		//绑定 提交按钮点击事件
		$("#roomCategoryAddBtn").click(function() {
			var id = $("#id").val();
			var list= new Array();
			$("input[name='categoryPrice']").each(function() {
				var obj =new Object();
				obj.dateSettingId= $(this).attr("id");
				obj.userPrice =$("#"+obj.dateSettingId+"_user").val();
				obj.price= $(this).val();
				obj.categoryId=id;
				list.push(obj);
			});
			$("#dateSettingsJson").val(JSON.stringify(list));
			$("#roomCategoryAddForm").submit();
		});//提交按钮点击事件结束
	});
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong><i class='icon-plust'></i>${empty roomCategory ? '添加' : '修改' }包间类型  </strong>
		</div>
		<div class='panel-body'>
			<form id="roomCategoryAddForm" repeatsubmit="1" method="post" class="form-horizontal" action="${contextPath}/roomCategory/add" >
                
	        <input name="id" id="id" type="hidden" value="${roomCategory.id}">
	        <input name="dateSettingsJson" id="dateSettingsJson" type="hidden" value="">
	        <div class="form-group">
	            <label class="col-md-1 control-label"style="padding-left: 35px;text-align: left;">
	                <span class="man">*</span>分类名称
	            </label>
	            <div class="col-md-2">
	                <input type="text" id="name" name="name" data-rule="类型名称:required;remote[${contextPath}/roomCategory/checkName, id]" maxlength="12" value="${roomCategory.name }" class="form-control" >
	            </div>
	        </div>
	         <div class="form-group">
	            <label class="col-md-1 control-label"style="padding-left: 35px;text-align: left;">
	                <span class="man">*</span>最低消费
	            </label>
	            <div class="col-md-2">
	                <input type="text" id="lowestPrice" name="lowestPrice" data-rule="最低消费:required;twoPointFloat" data-rule-twoPointFloat="[/^[0-9]+([.]\d{1,2})?$/, '请输入正确价格']" maxlength="8" value="${roomCategory.lowestPrice }" class="form-control" >
	            </div>
	        </div>
	        <div class="form-group tex-ct">
	            <label class="col-md-2 control-label" style="padding-left: 35px;text-align: left;">
	            <span  class="man">*</span>时间段收费</label>
	
	        </div>
	        <c:forEach items="${dateSettingSpecials}" var="dateSetting">
		        <div class="form-group" style=" overflow: hidden;margin-left: 10px; line-height:25px;">
		            <span class="col-md-1 tan" >${dateSetting.name}</span>
		            <span class=" tex-ri" >会员每小时收取</span>
		            <div class="col-md-1">
		                <input type="text" id="${dateSetting.id}_user" priceType="user" data-rule="required;twoPointFloat" data-rule-twoPointFloat="[/^[0-9]+([.]\d{1,2})?$/, '请输入正确价格']" name=""  maxlength="5" value="${dateSetting.userPrice}" class="form-control n-invalid" aria-required="true" style="height: 25px; placeholder=" aria-invalid="true" data-inputstatus="error">
		            </div>
		            <span>元</span><span class="msg-box n-right" style="position:static;" for="${dateSetting.id}_user"></span>
		        </div>
		        <div style="padding-top:6px; link-height:25px;overflow: hidden;">
	                <span class="tan" style="padding-left: 116px;">&nbsp;&nbsp;&nbsp;&nbsp;非会员每小时收取</span>
	                <div class="col-md-1">
	                    <input type="text" id="${dateSetting.id}"  priceType="notUser" data-rule="required;twoPointFloat" data-rule-twoPointFloat="[/^[0-9]+([.]\d{1,2})?$/, '请输入正确价格']" name="categoryPrice" maxlength="5" value="${dateSetting.categoryPrice}" class="form-control n-invalid" aria-required="true" style="height: 25px; placeholder=" aria-invalid="true" data-inputstatus="error">
	                </div>
	                <span>元<span class="msg-box n-right" style="position:static;" for="${dateSetting.id}"></span></span>
	        	</div>
	        	<hr class="col-md-3" style="margin-left: 35px;"><br/><br/><br/>
	        </c:forEach>
	         <div class="form-group tex-ct">
	            <label class="col-md-2 control-label" style="padding-left: 35px;text-align: left;">
	            <span  class="man">*</span>节假日收费</label>
	
	        </div>
	        <c:forEach items="${dateSettingFestivals}" var="dateSetting">
		        <div class="form-group" style=" overflow: hidden;margin-left: 10px; line-height:25px;">
		            <span class="col-md-1 tan" >${dateSetting.name}</span>
		            <span class=" tex-ri" >会员每小时收取</span>
		            <div class="col-md-1">
		                <input type="text" id="${dateSetting.id}_user" dateSettingId ="${dateSetting.id }" priceType="user" data-rule="required;twoPointFloat" data-rule-twoPointFloat="[/^[0-9]+([.]\d{1,2})?$/, '请输入正确价格']" name=""  maxlength="5" value="${dateSetting.userPrice}" class="form-control n-invalid" aria-required="true" style="height: 25px; placeholder=" aria-invalid="true" data-inputstatus="error">
		            </div>
		            <span>元<span class="msg-box n-right" style="position:static;" for="${dateSetting.id}_user"></span></span>
		        </div>
		        <div style="padding-top:6px; link-height:25px;overflow: hidden;">
	                <span class="tan" style="padding-left: 116px;">&nbsp;&nbsp;&nbsp;&nbsp;非会员每小时收取</span>
	                <div class="col-md-1">
	                    <input type="text" id="${dateSetting.id}"  priceType="notUser" data-rule="required;twoPointFloat" data-rule-twoPointFloat="[/^[0-9]+([.]\d{1,2})?$/, '请输入正确价格']" name="categoryPrice" maxlength="5" value="${dateSetting.categoryPrice}" class="form-control n-invalid" aria-required="true" style="height: 25px; placeholder=" aria-invalid="true" data-inputstatus="error">
	                </div>
	                <span>元<span class="msg-box n-right" style="position:static;" for="${dateSetting.id}"></span></span>
	        	</div>
	        	<hr class="col-md-3" style="margin-left: 35px;"><br/><br/><br/>
	        </c:forEach>
	        
	        <div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button"   id='roomCategoryAddBtn' class='btn btn-primary'
							value="${empty roomCategory ? '添加' : '修改' }" data-loading='稍候...' />
					</div>
			</div>
		</form>
		</div>
	</div>

</body>
</html>