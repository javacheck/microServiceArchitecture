<%@tag import="java.util.UUID"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="path" %>
<%@ attribute name="inputName" required="true" %>
<%@ attribute name="inputId" %>
<%@ attribute name="isSys" required="true" %>
<%@ attribute name="disabled" %>
<%@ attribute name="onchageCallback"  %>
<%-- 商家改变的时候调用的js方法名称参数是商家id --%>
<%@ attribute name="onStoreChange"  %>
<%-- 是否自动初始化 空值表示自动初始化 --%>
<%@ attribute name="auto"  %>

<%
	String id = UUID.randomUUID().toString().replaceAll("-", "");
	this.getJspContext().setAttribute("id", id);
%>

<span id="${id}select_product_category" >
	<div class="input-group" style="width: auto;float:left;margin-right:40px;">
	<input id="${inputId }" name="${inputName }" productId="" type="hidden"/>
	<select class='form-control' " id="${id}select_product_category_0" depth="0">
		<option value="" selected="selected">请选择</option>
	</select>
	</div>
</span>
<script type="text/javascript">
	$(function(){
		var selectProductCategory${id} = {};
		var storeId${id} = -1;
		<c:if test="${not empty onStoreChange}"> 
		${onStoreChange} = function(storeId){
			storeId${id} = storeId;
			selectProductCategory${id}.selectAreaInit();
		};
		</c:if>
		
		selectProductCategory${id}.currentVal = "";
		selectProductCategory${id}.service = function(){
			var path = "${path}";
			if (path != ""){
				var arr = path.split("-");
				for (var i = 0; i < arr.length; i++){
					if (i == 0){
						selectProductCategory${id}.selectAreaInit();
						$("#${id}select_product_category_"+i).val(arr[i]);
						$("#${id}select_product_category_"+i).change();
					}else {
						$("#${id}select_product_category_"+i).val(arr[i]);
						$("#${id}select_product_category_"+i).change();
					}
				}
				<c:if test="${disabled == true}">
				$("#${id}select_product_category").find("select").prop("disabled","disabled");
				</c:if>
			}else {
				<c:if test="${empty auto}">
					selectProductCategory${id}.selectAreaInit();
				</c:if>
			}
		};
		
		selectProductCategory${id}.selectAreaInit = function (){
			var type = "${isSys == true ? 0 : 1}";
			//初始化
			$.ajax({url:"${contextPath}/productCategory/ajax/find-by-parent?type="+type+"&storeId="+storeId${id}+"&d="+new Date(),async:false,
				success:function(data){
							$("#${id}select_product_category_0").html('<option value="" selected="selected">请选择</option>');
							if (data.length > 0){
								for (var i = 0; i < data.length; i++){
									$("#${id}select_product_category_0").append("<option value='"+data[i].id+"'>" + data[i].name + "</option>");
								}
								selectProductCategory${id}.areaChange(0);
							}
						}
			});	
		};
		
		selectProductCategory${id}.areaChange = function (depth){
			var type = "${isSys == true ? 0 : 1}";
			var _depth = new Number(depth) + 1;
			$("#${id}select_product_category_"+depth).change(function(){
				var areaElement = $(this);
				var areaId = areaElement.val();
				var depth = areaElement.attr("depth");
				
				if (areaId != "" && areaId != null){
					$.ajax({url:"${contextPath}/productCategory/ajax/find-by-parent?parentId="+areaId+"&type="+type+"&storeId="+storeId${id}+"&d="+new Date(),async:false,
						success: function(data){
									areaElement.nextAll().remove();
									if (data.length > 0){
										areaElement.parent().append('<select class="form-control" style="width: auto;" id="${id}select_product_category_'+_depth+'" depth="'+_depth+'"><option value="">请选择</option></select>');
										for (var i = 0; i < data.length; i++){
											$("#${id}select_product_category_"+_depth).append("<option value='"+data[i].id+"'>" + data[i].name + "</option>");
										}
										selectProductCategory${id}.areaChange(_depth);
									}
									selectProductCategory${id}.getSelectVal();
								}});
				}else {
					areaElement.nextAll().remove();
					selectProductCategory${id}.getSelectVal();
				}
			});
		};
		
		selectProductCategory${id}.getSelectVal = function (){
			var e = $("#${id}select_product_category").find("[name='${inputName }']");
			e.val("");
			//最后一个select值
			var lastVal = "";
			var length = $("#${id}select_product_category").find("select").length;
			var index = 0;
			$("#${id}select_product_category").find("select").each(function(k,v){
				if ($(v).val() != ""){
					e.val($(v).val());
				}
				lastVal = $(v).val();
			});
			
			if (lastVal != "" && lastVal != selectProductCategory${id}.currentVal){
				<c:if test="${not empty onchageCallback}">${onchageCallback}();</c:if>
			}else if(lastVal == ""){
				<c:if test="${not empty onchageCallback}">${onchageCallback}();</c:if>
			}
			selectProductCategory${id}.currentVal = e.val();
		};
		
		selectProductCategory${id}.service();
	});
</script>