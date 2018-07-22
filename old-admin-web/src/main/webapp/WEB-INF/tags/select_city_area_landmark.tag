<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="cityId" %>
<%@ attribute name="areaId" %>
<%@ attribute name="landmarkId" %>
<span id="select_city_area_landmark"></span>
<script type="text/javascript">
	var _cityId = "${cityId}";
	var _areaId = "${areaId}";
	var _landmarkId = "${landmarkId}";
	$(function(){
		$.get("${pageContext.request.contextPath}/city/ajaxList",
				  function(data){
					$("#select_city_area_landmark").append(data);
					cityChange();
					if (_cityId != ""){
						$("#cityId").val(_cityId);
						$("#cityId").change();
					}
				  }
		);
		
		function cityChange(){
			$("#cityId").change(function(){
				var cityElement = $(this);
				var cityId = cityElement.val();
				$.get("${pageContext.request.contextPath}/area/ajaxListByCityId", {cityId: cityId},
						  function(data){
							if (cityElement.siblings().length > 0){
								cityElement.siblings().remove();
								_areaId = "";
								_landmarkId = "";
							}
							cityElement.parent().append(data);
							areaChange();
							if (_areaId != ""){
								$("#areaId").val(_areaId);
								$("#areaId").change();
							}
						  }
				);
			});
		}
		
		function areaChange(){
			$("#areaId").change(function(){
				var areaElement = $(this);
				var areaId = areaElement.val();
				$.get("${pageContext.request.contextPath}/landmark/ajaxListByAreaId", {areaId: areaId},
						  function(data){
							if (areaElement.next().length > 0){
								areaElement.next().remove();
								_landmarkId = "";
							}
							areaElement.parent().append(data);
							if (_landmarkId != ""){
								$("#landmarkId").val(_landmarkId);
							}
						  }
				);
			});
		}
	});
</script>