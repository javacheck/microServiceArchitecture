<%@tag import="java.util.UUID"%>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ attribute name="path" required="true"%>
<%@ attribute name="callback" required="true"%>
<%@ attribute name="modeId" required="true"%>
<%
	String selectStoreId = UUID.randomUUID().toString().replaceAll("-", "");
	this.getJspContext().setAttribute("selectStoreId", selectStoreId);
%>

<script type="text/javascript">
	
	callback_${selectStoreId} = function(){
		$("#shopList_${selectStoreId}").find("table").find("tbody tr").click(function(){
			var obj = new Object();
			obj.id=$(this).attr("shopId");
			obj.name=$(this).attr("shopName");
			obj.moble=$(this).attr("mobile");
			${callback}(obj);
			$("#close_shop_${selectStoreId}").click();
		});
	};
	//列表生成回调函数
	
</script>

<div class="modal fade" id="${modeId}">
		<div class="modal-dialog modal-lg" style="width: 1200px;" >
		  <div class="modal-content">
		    <div class="modal-header">
		      <button id = "close_shop_${selectStoreId}" type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
		    </div>
		    <div class="modal-body">
		    	 <m:list title="商家列表" id="shopList_${selectStoreId}"  callback="callback_${selectStoreId}"
					listUrl="${path}"
					 searchButtonId="shop_mode_search_btn">
					<div class="input-group" style="max-width: 1300px;">
						<span class="input-group-addon">联系方式</span> 
						<input type="text"	name="mobile" class="form-control" placeholder="输入商家联系方式" style="width: 200px;">
						<span class="input-group-addon">商家名称</span> 
						<input type="text"	name="name" class="form-control" placeholder="请输入商家姓名" style="width: 200px;">
					</div>
				</m:list>
		    </div>
		  </div>
		</div>
	</div>