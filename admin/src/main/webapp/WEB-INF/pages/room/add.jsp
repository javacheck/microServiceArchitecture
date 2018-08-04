<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty room ? '添加' : '修改' }包间 </title>
<style type="text/css">
.bod{
height: 32px;
  padding: 5px 8px;
  font-size: 13px;
  line-height: 1.53846154;
  color: #222;
  vertical-align: middle;
  background-color: #fff;
  border: 1px solid #ccc;
  border-radius: 4px;
  -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
  box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
  -webkit-transition: border-color ease-in-out .15s, -webkit-box-shadow ease-in-out .15s;
  -o-transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
  transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;

}
</style>
<script type="text/javascript">
	var contextPath = "${contextPath}";
	//初始化事件
	$(function() {
		var categoryId  = "${room.categoryId}";
		if(categoryId != ''){
			$("#categoryId").val(categoryId);
		}
		
		$("#roomAddForm").validator({
		    rules: {
		    	TwoPointFloat:[/^[0-9]+([.]\d{1,2})?$/,'请输入正确的价格']
		    },
		    fields: {
		    	number: "required;integer[+];remote["+contextPath+"/room/checkNumberRepeat, id]"
		    	,name:"required;remote["+contextPath+"/room/checkNameRepeat, id]"
		    	,persons:"required;integer[+]"
		    	,basePrice :"required;TwoPointFloat"
		    	,baseUserPrice :"required;TwoPointFloat"
		    }
		});
		
		//绑定 提交按钮点击事件
		$("#roomAddBtn").click(function() {
			$("#roomAddForm").submit();
		});//提交按钮点击事件结束
		
	});//初始化事件结束
	
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong><i class='icon-plust'></i>${empty room ? '添加' : '修改' }包间  </strong>
		</div>
		<div class='panel-body'>
			<form id="roomAddForm" repeatSubmit='1' method='post' class='form-horizontal'
				action="${contextPath }/room/add">
				
				<input name="id" id="id" type="hidden" value="${room.id }" />
				<div class="form-group">
					<label class="col-md-1 control-label">
						<span style="color: red;font-size: 15px" class="">*</span>包间名称
					</label>
					<div class="col-md-2">
						<input type='text' id="name" name="name" maxlength="20"	value="${room.name }" class='form-control' />
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" id="error"></label>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px" class="">*</span>包间号码</label>
					<div class="col-md-2">
						<input type='text' id="number" name="number" maxlength="6"
							value="${room.number }" class='form-control' />
					</div>
				</div>
				<div class="form-group">	
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px" class="">*</span>包间类型</label>
					<div class="col-md-2">
					<select class="form-control" id = "categoryId" name="categoryId">
						<c:forEach items="${categorys}" var="category">
							<option  value="${category.id }">${category.name }</option>
						</c:forEach>
					</select>
					</div>
				</div>
				<div class="form-group">	
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px" class="">*</span>额定人数</label>
					<div class="col-md-2">
						<input type='text' id="persons" name="persons" maxlength="3"
							value="${room.persons }" class='form-control' />
					</div>
				</div>
				<div class="form-group">	
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px" class="">*</span>基础收费</label>
					<div class="col-md-2  control-label" >
					<div style="overflow: hidden; margin-bottom: 10px;">
						<span class="col-md-6" style="line-height: 32px;">非会员每小时收取</span>
						<input  class="col-md-4 bod" type='text' id="basePrice" name="basePrice" maxlength="9"
							value="${room.basePrice }" class='form-control' />
						<span class="col-md-2" style="line-height: 32px;">元<span class="msg-box n-right" style="position:fixed;" for="basePrice"></span></span>
					</div>
					<div  style="overflow: hidden; margin-bottom: 10px;">
						<span class="col-md-6" style="line-height: 32px;">会员每小时收取</span>
						<input class="col-md-4 bod" type='text' id="baseUserPrice" name="baseUserPrice" maxlength="9"
							value="${room.baseUserPrice }" class='form-control' />
						<span class="col-md-2" style="line-height: 32px;">元<span class="msg-box n-right" style="position:fixed;" for="baseUserPrice"></span></span>
					</div>
					</div>
				</div>
				<div class="form-group">	
					<label class="col-md-1 control-label">包间备注</label>
					<div class="col-md-2">
						<input type='text' id="memo" name="memo" maxlength="200"
							value="${room.memo }" class='form-control' />
					</div>
				</div>
				
				<div class="form-group">
					<div class="col-md-offset-1 col-md-10">
						<input type="button"  id='roomAddBtn' class='btn btn-primary'
							value="${empty room ? '添加' : '修改' }" data-loading='稍候...' />
					</div>
				</div>
			</form>
		</div>
	</div>

</body>
</html>