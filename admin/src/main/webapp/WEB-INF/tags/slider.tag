<%@tag import="java.util.UUID"%>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="inputName" required="true"%>
<%@ attribute name="inputId" %>
<!--默认状态 -->
<%@ attribute name="defaultStatus"  %>
<!-- 宽度 -->
<%@ attribute name="width"  %>  
<!-- 高度 -->
<%@ attribute name="height" %>
<!-- 打开状态显示文字 -->
<%@ attribute name="on" %>
<!-- 关闭状态显示文字 -->
<%@ attribute name="off" %>
<%
	String id = UUID.randomUUID().toString().replaceAll("-", "");
	this.getJspContext().setAttribute("id", id);
%>
<style type="text/css">
#switch_${id}{
    width:  ${not empty width ? width:'75'}px;
    height: ${not empty height ? height:'30'}px;
}

</style>
<script type="text/javascript">
$(function(){
	
	var defaultStatus = "${not empty defaultStatus ? defaultStatus:'1'}";
	if (defaultStatus=='1') {//打开状态
		$('[name = ${inputName}]').val('1');//打开是1
	}
	
	$('.toggle').click(function(e){//点击更改事件
		e.preventDefault(); // The flicker is a codepen thing
		$(this).toggleClass('toggle-on');//切换CLASS
		var objClass1=$(this).attr("class");
		if (objClass1=="toggle toggle-on") {
			$('[name = ${inputName}]').val('1');//打开是1
		}else{
			$('[name = ${inputName}]').val('0');//关闭是0
		}
	});//点击更改事件结束
	
});

</script>

<input type="hidden" id="${empty inputId ?id:inputId}" name="${inputName}" maxlength="2" value="0"  />
<div class="toggle${empty defaultStatus || defaultStatus==1? ' toggle-on':'' }" id='switch_${id}'>

	<div class='toggle-text-off'>${not empty off ? off:'关' }</div>
	<div class='glow-comp'></div>
	<div class='toggle-button'></div>
	<div class='toggle-text-on'>${not empty on ? on:'开' }</div>
</div>