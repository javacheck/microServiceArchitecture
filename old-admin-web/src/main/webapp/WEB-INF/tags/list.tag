<%-- 需要导入
<script type="text/javascript" src="/js/jquery/jquery.pager.js"></script>
<script type="text/javascript" src="/js/common/list_page.js"></script>
<script type="text/javascript" src="/charisma-master/js/jquery.noty.js"></script>
列表刷新js,loadList_${id}();
列表当前页刷新,loadCurrentList_${id}();
列表加载完的回调,callback
 --%>
<%@tag import="java.util.UUID"%>
<%@ tag language="java" pageEncoding="UTF-8" 
	trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- 表格标题 --%>
<%@ attribute name="title"%>
<%-- 列表数据url --%>
<%@ attribute name="listUrl" required="true"%>
<%-- 添加url --%>
<%@ attribute name="addUrl"%>
<%-- 删除url --%>
<%@ attribute name="deleteUrl"%>
<%@ attribute name="pageNo"%>
<%@ attribute name="id"%>
<%@ attribute name="callback"%>
<%@ attribute name="searchButtonId"%>

<%
	String formId = UUID.randomUUID().toString();
	this.getJspContext().setAttribute("formId", formId);
	
	String contextPath = (String)application.getAttribute("contextPath");
	
	String addUrl = (String) this.getJspContext().getAttribute(
			"addUrl");
	
	if (addUrl != null && !addUrl.startsWith(contextPath)){
		this.getJspContext().setAttribute("addUrl", contextPath + addUrl);
	}
	
	String listUrl = (String) this.getJspContext().getAttribute(
			"listUrl");
	
	if (listUrl != null && !listUrl.startsWith(contextPath)){
		this.getJspContext().setAttribute("listUrl", contextPath + listUrl);
	}
	
%>

<script type="text/javascript">
	$(function() {
		_load = function(){
			$.loadData("${formId}", '',function(){
				var form = $("#${formId}");
				var _total = form.find("span[name='total']").html();
				if (_total > 0){
					form.find("span[name='recordNum']").show();
					form.find("div[name='pagerBar']").show();
				}else {
					form.find("span[name='recordNum']").hide();
					form.find("div[name='pagerBar']").hide();
				}
				<c:if test="${not empty callback}">
					$("#${formId}").find(':input').unbind();
					${callback}();
				</c:if>
				
			});
		};
		_load();
	});
	<c:if test="${not empty id}">
		function loadList_${id}(){
			var $pageNo = $("#${formId}").find("input[name='pageNo']");
			$pageNo.val(1);
			_load();
		}
		
		function loadCurrentList_${id}(){
			_load();
		}
		
		function ${id}_DoSubmit(){
			$("#${searchButtonId }").click();
			return false;
		}
	</c:if>
</script>
<form id="${formId }" action="${listUrl }" method="post" onsubmit="return ${id}_DoSubmit();">
	<input type="hidden" name="pageNo"
		value="${(pageNo == null || pageNo == '') ? '1' : pageNo }" />

	<div class="panel">
		<div class="panel-heading">
			<strong><i class="icon-list-ul"></i>${title }</strong>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<c:if test="${not empty addUrl }">
				<a href="${addUrl }" class="btn btn-primary" style="float: right;margin-top: -6px;"><i
					class="icon-plus"></i>添加</a>
			</c:if>
			<c:if test="${not empty deleteUrl }">
				<a href="${deleteUrl }" class="btn btn-danger"><i
					class="icon-remove"></i>删除</a>
			</c:if>

			<c:if test="${not empty searchButtonId }">
				<div id="_search_condition_${formId }" style="margin-top: 5px;">
					<jsp:doBody></jsp:doBody>
					<input id="${searchButtonId }" type="button" name="searchButton"
						class="btn btn-info" value="搜 索" style="margin-top: 5px;" />
				</div>
			</c:if>
		</div>
		<div class="panel-body">
			<div name="list"></div>
			<div class="col-md-12" name="pager-footer" style="display: none;">
				<div class="col-md-3" style="margin-top: 10px;">
					每页显示：<select name="pageSize" size="1"><option value="10"
							selected="selected">10</option>
						<option value="25">25</option>
						<option value="50">50</option>
						<option value="100">100</option></select> 总记录数：<span name="total"></span>
					(共<span name="pages"></span>页)
				</div>
				<div class="col-md-6" style="margin-top: -10px;">
					<span name="pager"></span>
				</div>
			</div>
		</div>
	</div>

</form>
