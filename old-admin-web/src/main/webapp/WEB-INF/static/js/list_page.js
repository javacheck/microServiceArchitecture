$(function(){
	$.loadData = function(formId,successmsg,callback){
		var $listForm = $("#" + formId);
		var $list = $listForm.find("div[name='list']");
		var $data = $listForm.serialize();
		var $url = $listForm.attr("action");
		var $pageNo = $listForm.find("input[name='pageNo']");
		
		$.ajax({
			type : 'POST',
			dataType : 'html',
			url : $url,
			data : $data,
			async : false,
			success : function(data,textStatus,xhr) {
				if(xhr.getResponseHeader("sessionstatus") == "0"){
					window.location.replace(xhr.getResponseHeader("loginUrl")); 
				}
				$list.html(data);
				// 分页
				var $pager = $listForm.find("[name='pager']");
				var $pagerFooter = $listForm.find("[name='pager-footer']");
				var $listTable = $listForm.find("table[name='listTable']");

				var _pageNo = $listTable.attr("pageNo");
				var _pageCount = $listTable.attr("pages");
				var _total = $listTable.attr("total");
				
				$listForm.find("span[name='total']").html(_total);
				$listForm.find("span[name='pages']").html(_pageCount);

				$pager.pager({
					pagenumber : _pageNo,
					pagecount : _pageCount,
					buttonClickCallback : function(id){
						$pageNo.val(id);
						$.loadData(formId,successmsg,callback);
					}
				});
				if (_total == 0){
					$list.html("<div class=\"noRecord\">没有找到任何记录!&nbsp;&nbsp;&nbsp;&nbsp;</div>");
					$pagerFooter.hide();
				}else {
					$pagerFooter.show();
				}
				callback();
				$list.show();
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert("服务器忙......" + textStatus + errorThrown);
			},
			beforeSend : function() {
				$list.html("数据加载中......");
			}
		});
		
		var $searchButton = $listForm.find("[name='searchButton']");// 查找按钮
		var $allCheck = $listForm.find("input[name='allCheck']");// 全选复选框
		var $listTable = $listForm.find("table[name='listTable']");
		var $listTableTr = $listTable.find("tr:gt(0)");
		var $idsCheck = $listForm.find("input[name='ids']");// ID复选框
		var $deleteButton = $listForm.find("[name='deleteButton']");// 删除按钮
		var $pageSize = $listForm.find("select[name='pageSize']");// 每页显示数
		
		$.clearBind([$searchButton,$allCheck,$idsCheck,$deleteButton,$pageSize]);

		// 全选
		$allCheck.click(function() {
			var $this = $(this);
			if ($this.prop("checked")) {
				$idsCheck.prop("checked", true);
				$deleteButton.prop("disabled", false);
				$listTableTr.addClass("checked");
			} else {
				$idsCheck.prop("checked", false);
				$deleteButton.prop("disabled", true);
				$listTableTr.removeClass("checked");
			}
		});
		
		// 无复选框被选中时,删除按钮不可用
		$idsCheck.click(function() {
			var $this = $(this);
			if ($this.prop("checked")) {
				$this.parent().parent().addClass("checked");
				$deleteButton.prop("disabled", false);
			} else {
				$this.parent().parent().removeClass("checked");
				var $idsChecked = $listForm.find("input[name='ids']:checked");
				if ($idsChecked.size() > 0) {
					$deleteButton.prop("disabled", false);
				} else {
					$deleteButton.prop("disabled", true);
				}
			}
		});
		
		// 批量删除
		$deleteButton.click(function() {
			var url = $(this).attr("url");
			var $idsCheckedCheck = $listForm.find("input[name='ids']:checked");
			if (confirm("确定要删除吗？")){
				batchDelete();
			}else {
				return false;
			}
			function batchDelete() {
				$.ajax({
					url : url,
					data : $idsCheckedCheck.serialize(),
					type : "POST",
					dataType : "json",
					cache : false,
					success : function(data) {
						if (data == true) {
							$deleteButton.prop("disabled", true);
							$allCheck.prop("checked", false);
							$idsCheckedCheck.prop("checked", false);
							noty({'text':'操作成功','layout':'center','type':'success'});
							$.loadData(formId,successmsg,callback);
						}
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						alert("服务器忙......" + textStatus);
					}
				});
			}
		});
		
		// 查找
		$searchButton.click(function() {
			$pageNo.val("1");
			$.loadData(formId,successmsg,callback);
		});

		// 每页显示数
		$pageSize.change(function() {
			$pageNo.val("1");
			$.loadData(formId,successmsg,callback);
		});
	};
});
