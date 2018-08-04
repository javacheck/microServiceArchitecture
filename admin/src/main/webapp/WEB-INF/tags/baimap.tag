<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="mapContainerId" required="true"%>
<%@ attribute name="longitudeInputId" required="false"%>
<%@ attribute name="latitudeInputId" required="false"%>

<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.5&ak=ULZFsRXXSxEoFDqaGzKf3mWS"></script>

<script type="text/javascript">
	var map = null;
	$(function(){
		// 百度地图API功能
		var lng = 113.329883
		var lat = 23.111916;
		
		var longitudeInputId = "${longitudeInputId}";
		var latitudeInputId = "${latitudeInputId}";
		
		if (longitudeInputId != ""){
			var lngVal = $("#"+longitudeInputId).val();
			if (lngVal != ""){
				lng = lngVal;
			}
		}
		
		if (latitudeInputId != ""){
			var latVal = $("#"+latitudeInputId).val();
			if (latVal != ""){
				lat = latVal;
			}
		}
		
		map = new BMap.Map("${mapContainerId}");
		var point = new BMap.Point(lng, lat);
		map.centerAndZoom(point, 15);
		map.enableScrollWheelZoom(true);
		map.disableDragging();     //禁止拖拽
		setTimeout(function(){
		   map.enableDragging();   //两秒后开启拖拽
		   //map.enableInertialDragging();   //两秒后开启惯性拖拽
		}, 2000);
		
		var marker = new BMap.Marker(point);  // 创建标注
		map.addOverlay(marker);               // 将标注添加到地图中
		marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
		
		function showInfo(e){
			if (longitudeInputId != ""){
				$("#"+longitudeInputId).val(e.point.lng);
			}
			
			if (latitudeInputId != ""){
				$("#"+latitudeInputId).val(e.point.lat);
			}
			map.clearOverlays();
			marker = new BMap.Marker(new BMap.Point(e.point.lng, e.point.lat));  // 创建标注
			map.addOverlay(marker);               // 将标注添加到地图中
			marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
		}
		map.addEventListener("click", showInfo);
	});
	
	function baidumapSearch(address){
		var local = new BMap.LocalSearch(map, {
			renderOptions:{map: map}
		});
		local.search(address);
	}
</script>