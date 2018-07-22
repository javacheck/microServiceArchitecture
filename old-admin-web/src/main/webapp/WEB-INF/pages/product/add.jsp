<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty product ? '添加' : '修改' }商品</title> 
<script type="text/javascript">
$(document).ready(function(){
	getTopCategoryList();//返回一级列表
	var categoryId=$("#categoryId").val();
	if(categoryId!=""){
		lm.post("${contextPath}/product/list/ajax/list-by-categoryId",{categoryId:categoryId},function(data){
			var arr= new Array(); //定义一数组 
			arr=(data.path).split("-");
			if(arr.length==1){//分类只有一级
				$("#categoryId1").val(categoryId);//一级分类值
				$("#cat_id").attr("disabled","disabled");
				$("#childid").attr("disabled","disabled");
				$("#topid").find("option[value='"+arr[0]+"']").attr("selected",true);
	    		$("#topid").attr("disabled","disabled");
			}else if(arr.length==2){//分类有二级
				$("#categoryId2").val(categoryId);//二级分类值
				$("#cat_id").attr("disabled","disabled");
				//通过商品属于哪个分类的父类，查父类下的所有分类
				lm.post("${contextPath}/product/list/ajax/list-by-parent",{parentId:data.parentId},function(data1){
		        	for(var i=0;i<data1.length;i++){//二级列表
		        		$("#childid").append('<option id="' + data1[i].id + '" value="' + data1[i].id + '">' + data1[i].name + '</option>');
		    		}
		        		$("#childid").find("option[value='"+categoryId+"']").attr("selected",true); //二级通过categoryId选中
		    	 		$("#childid").attr("disabled","disabled");
				});
				$("#topid").find("option[value='"+arr[0]+"']").attr("selected",true);//通过path分割拿到arr[0],即一级分类ID
	    		$("#topid").attr("disabled","disabled");
			}else{//分类有三级
				$("#categoryId3").val(categoryId);//三级分类值
				//通过商品属于哪个分类的父类，查父类下的所有分类(现在分类是三级分类)
				lm.post("${contextPath}/product/list/ajax/list-by-parent",{parentId:data.parentId},function(data1){
		        	for(var i=0;i<data1.length;i++){//三级分类列表
		        		 $("#cat_id").append('<option id="' + data1[i].id + '" value="' + data1[i].id + '">' + data1[i].name + '</option>');
		    		}
	    	 		$("#cat_id").find("option[value='"+data.id+"']").attr("selected",true); 
	    	 		$("#cat_id").attr("disabled","disabled");
	    	 		//查父类下的所有分类(现在分类是二级分类)	
		    		lm.post("${contextPath}/product/list/ajax/list-by-parent",{parentId:arr[0]},function(data2){
			        	for(var i=0;i<data2.length;i++){//二级分类列表
			        		 $("#childid").append('<option id="' + data2[i].id + '" value="' + data2[i].id + '">' + data2[i].name + '</option>');
			    		}
		    	 		$("#childid").find("option[value='"+data1[0].parentId+"']").attr("selected",true); 
		    	 		$("#childid").attr("disabled","disabled");
			    	 });
		    		$("#topid").find("option[value='"+arr[0]+"']").attr("selected",true);
		    		$("#topid").attr("disabled","disabled");
		    	 });
			}
		});
	}
	//checkProduct();
});
function checkProduct(){
	
	$("#name").focus(function(){//名称焦点验证
		if($.trim(document.getElementById("name").value) == "" || $.trim(document.getElementById("name").value)==null){
			$("#emptype").show();
			$("#error").hide();
			return false;
		}else{
			$("#emptype").hide();
			$("#error").hide();
		}
	});
	$("#name").blur(function(){
		if($.trim(document.getElementById("name").value) == "" || $.trim(document.getElementById("name").value)==null){
			$("#error").show();
			$("#emptype").hide();
			return false;
		}else{
			$("#emptype").hide();
			$("#error").hide();
		}
	});
}
//获取一级分类
function getTopCategoryList() {
	lm.post("${contextPath}/product/list/ajax/list-by-parent1",function(data){
    	for(var i=0;i<data.length;i++){
    		 $("#topid").append('<option id="' + data[i].id + '" value="' + data[i].id + '">' + data[i].name + '</option>');
		}
	});
}
//根据一级分类所选获取二级分类
function getChildrenList() {
    var parentId=$("#topid").val();
	if(parentId == ""){
		$("#childid").empty();
		$("#childid").append("<option value='' >请选择二级分类</option>");
       	return ;
    }
    $("#categoryId1").val(parentId);//一级分类值
    $("#childid").empty();
    lm.post("${contextPath}/product/list/ajax/list-by-parent",{parentId:parentId},function(data){
    	$("#childid").append("<option value='' >请选择二级分类</option>");
    	for(var i=0;i<data.length;i++){
    		 $("#childid").append('<option id="' + data[i].id + '" value="' + data[i].id + '">' + data[i].name + '</option>');
		}
	});
}
//根据二级分类获取三级分类 
function getChildrenList1() {
	 var parentId=$("#childid").val();
     if(parentId == ""){
        $("#cat_id").empty();
        $("#cat_id").append("<option value='' >请选择三级分类</option>");
        return ;
     }
   	 $("#categoryId2").val(parentId);//二级分类值
	 $("#cat_id").empty();
     lm.post("${contextPath}/product/list/ajax/list-by-parent",{parentId:parentId},function(data){
    	$("#cat_id").append("<option value='' >请选择三级分类</option>");
    	for(var i=0;i<data.length;i++){
    		 $("#cat_id").append('<option id="' + data[i].id + '" value="' + data[i].id + '">' + data[i].name + '</option>');
		}
	 });
     
}
//获取分类属性id
function getcatId(){
	var catId=$("#cat_id").val();//三级分类值
	if(catId!=null){
   	 	$("#categoryId3").val(catId);
    }
}
//保存或修改商品
function saveProduct(){
	var id=$("#id").val();//商品属性ID
	var name=$("#name").val();//商品属性名称
	var categoryId;
	if($("#name").val()=="" ||$("#name").val()==null){
		lm.alert("商品名称不能为空!");
		return;
	}
	if($("#categoryId3").val()!="" && $("#categoryId3").val()!=null){
		categoryId=$("#categoryId3").val();//产品分类
	}else if($("#categoryId2").val()!="" && $("#categoryId2").val()!=null){
		categoryId=$("#categoryId2").val();//产品分类
	}else if($("#categoryId1").val()!="" && $("#categoryId1").val()!=null){
		categoryId=$("#categoryId1").val();//产品分类
	}else{
		lm.alert("产品分类不能为空！");
		return;
	}
	lm.post("${contextPath}/product/list/ajax/exist",{name:name,categoryId:categoryId},function(data){
		if(data==1){
			noty(name+"已存在该商品！");
		}else{
			var url="${contextPath}/product/list/ajax/save";
			lm.post(url,{id:id,name:name,categoryId:categoryId},function(data){
				if(data=='1'){
			    	lm.alert("操作成功！");
			    	 window.location.href="${contextPath}/product/list";
				}
			});
		}
	});
	
}
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
			<i class='icon-plust'></i>${empty product ? '添加' : '修改' }商品
			</strong>
		</div>
		<div class='panel-body'>
			<div  class='form-horizontal'>
				<input id="id" name="id" type="hidden" value="${product.id }" /><!-- 产品id -->
				<input id="categoryId1" name="categoryId1" type="hidden" value="" /><!-- 一级产品分类id -->
				<input id="categoryId2" name="categoryId2" type="hidden" value="" /><!-- 二级产品分类id -->
				<input id="categoryId3" name="categoryId3" type="hidden" value="" /><!-- 二级产品分类id -->
				<input id="categoryId" name="categoryId" type="hidden" value="${product.categoryId }" /><!-- 三级产品分类id -->
				<div class="form-group">
					<label class="col-md-2 control-label">商品名称<span style="color:red">*</span></label>
					<div class="col-md-2">
						<input type='text' id="name" name="name" value="${product.name}" class='form-control' onBlur="checkProduct();"  maxlength="10"/>
					</div>
					<label class="col-md-0 control-label" style="color: red;display:none;" id="error">商品名称不能为空</label>
					<label class="col-md-0 control-label" style="color: blue;display:none;" id="emptype">请输入商品名称</label>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">产品分类<span style="color:red">*</span></label>
					<div class="col-md-1">
                        	 <select name="topid" class="form-control"  style="width: auto;"  id="topid" onChange="getChildrenList()">
                        	 	<option value="">选择一级分类</option>
                        	</select>
                    </div>
                    <div class="col-md-1">
							<select name="childid" class="form-control"  style="width: auto;" id="childid" onchange="getChildrenList1();" >
								<option value="">
									选择二级分类
								</option>
							</select>
					 </div>
                    <div class="col-md-1">		
							<select name="cat_id" class="form-control"  style="width: auto;" id="cat_id" onchange="getcatId()" >
								<option value="">
									选择三级分类
								</option>
							</select>
					</div>
				</div>
				<div class="form-group">
					<div class="col-md-offset-2 col-md-1 0">
						<input type="button"  id='productAddBtn' class='btn btn-primary' value="${empty product ? '添加' : '修改' }" onclick="saveProduct()" />
					</div>
				</div>
		</div>
		</div>
	</div>

</body>
</html>