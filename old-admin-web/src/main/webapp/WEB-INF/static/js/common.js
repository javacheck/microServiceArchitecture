$(function() {
	$.clearBind = function(items) {
		$.each(items, function() {
			$(this).unbind();
		});
	};

	lm = {};
	lm.post = function(url, data, callback, type) {
		$.post(url, data, callback, type);
	};

	lm.postSync = function(url, data, callback, type) {
		lm.ajax({
			async : false,
			url : url,
			data : data,
			success : callback
		});
	};

	lm.ajax = function(options) {
		var defaults = {
			type : "POST",
			async : true
		}
		var opts = $.extend(defaults, options);
		$.ajax(opts);
	};

	lm.confirm = function(text, confirmfn) {
		$("#LMConfirmModalTitle").html(text);
		$("#LMConfirmModal").modal();
		$("#LMConfirmBtn").click(function() {
			confirmfn();
			$("#LMConfirmCloseBtn").click();
		});
	};

	lm.noty = function(text, options) {
		// type值有info,primary,danger,warning,important,special
		var defaults = {
			type : 'success',
			placement : 'center',
			time : 2000
		};
		// Extend our default options with those provided.
		var opts = $.extend(defaults, options);
		var msg = new $.Messager(text, opts);
		msg.show();
	};

	noty = function(text, options) {
		lm.noty(text, options);
	};

	lm.alert = function(text) {
		noty("" + text, {
			type : 'warning',
			time : 2000
		});
	}
	//是否是正浮点数
	lm.isFloat = function(num) {
		var type = "^\\d+(\\.\\d+)?$";
		var re = new RegExp(type);
		if (num.match(re) == null) {
			return false;
		}
		return true;
	}
});
