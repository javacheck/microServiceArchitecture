<%@tag import="java.util.UUID"%>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="path" %>
<%@ attribute name="inputName" required="true" %>
<%@ attribute name="inputId" %>
<%@ attribute name="areaId" %>

<%
	String id = UUID.randomUUID().toString().replaceAll("-", "");
	this.getJspContext().setAttribute("id", id);
%>

<span id="${id}select_area" >
	<div class="input-group">
	<input id="${inputId }" name="${inputName }" type="hidden"/>
	<select class='form-control' style="width: auto;" id="${id}select_area_0" depth="0">
		<option value="">请选择</option>
	</select>
	</div>
</span>

<script type="text/javascript">
	$(function(){
		var selectArea${id} = {};
		
		selectArea${id}.service = function(){
			var path = "${path}";
			var areaId="${areaId}";
			if (areaId != ""){
				lm.postSync("${contextPath }/area/path",{id:"${areaId}"},function(data){
					path = data;
				});
			}
			if (path != ""){
				var arr = path.split("-");
				for (var i = 0; i < arr.length; i++){
					if (i == 0){
						selectAreaInit();
						$("#${id}select_area_"+i).val(arr[i]);
						$("#${id}select_area_"+i).change();
					}else {
						$("#${id}select_area_"+i).val(arr[i]);
						$("#${id}select_area_"+i).change();
					}
				}
			}else {
				selectAreaInit();
			}
			function selectAreaInit(){
				//初始化
				$.ajax({url:"${contextPath}/area/list",async:false,
					success:function(data){
								if (data.length > 0){
									for (var i = 0; i < data.length; i++){
										$("#${id}select_area_0").append("<option value='"+data[i].id+"'>" + data[i].name + "</option>");
									}
									areaChange(0);
								}
							}
				});	
			}
			
			function areaChange(depth){
				var _depth = new Number(depth) + 1;
				$("#${id}select_area_"+depth).change(function(){
					var areaElement = $(this);
					var areaId = areaElement.val();
					var depth = areaElement.attr("depth");
					
					if (areaId != "" && areaId != null){
						$.ajax({url:"${contextPath}/area/list?parentId="+areaId,async:false,
							success: function(data){
										areaElement.nextAll().remove();
										if (data.length > 0){
											areaElement.parent().append('<select class="form-control" style="width: auto;" id="${id}select_area_'+_depth+'" depth="'+_depth+'"><option value="">请选择</option></select>');
											for (var i = 0; i < data.length; i++){
												$("#${id}select_area_"+_depth).append("<option value='"+data[i].id+"'>" + data[i].name + "</option>");
											}
											areaChange(_depth);
										}
										getSelectVal();
									}});
					}else {
						areaElement.nextAll().remove();
						getSelectVal();
					}
				});
			}
			
			function getSelectVal(){
				var e = $("#${id}select_area").find("[name='${inputName }']");
				e.val("");
				$("#${id}select_area").find("select").each(function(k,v){
					if ($(v).val() != ""){
						e.val($(v).val());
					}
				});
			}
		};
		
		selectArea${id}.service();
	});
</script>