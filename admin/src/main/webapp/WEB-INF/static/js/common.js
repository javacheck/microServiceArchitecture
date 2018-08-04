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
	
	lm.loading = function(){
		layer.load();
	}

	lm.confirm = function(text, confirmfn,cancelfn) {
		/**
		$("#LMConfirmModalTitle").html(text);
		$("#LMConfirmModal").modal({
			backdrop : 'static',
		    show     : true
		});
		$("#LMConfirmBtn").click(function() {
			confirmfn();
			$("#LMConfirmCloseBtn").click();
		});
		
		if (confirm(text)){
			confirmfn();
		}*/
		
		layer.confirm(text, {icon: 3}, function(index){
		    layer.close(index);
		    confirmfn();
		},function(){
			if (cancelfn != null){
				cancelfn();
			}
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
	
	//是否为正整数 
	lm.isPositiveNum = function(s){
		var re = /^[0-9]*[1-9][0-9]*$/; 
		return re.test(s);
	};
	
	//是否是正浮点数
	lm.isFloat = function(num) {
		var type = "^\\d+(\\.\\d+)?$";
		var re = new RegExp(type);
		if (num.match(re) == null) {
			return false;
		}
		return true;
	}
	
	//是否两位小数点的浮点数
	lm.isTwoPointFloat = function(num){
		var pattern =/^[0-9]+([.]\d{1,2})?$/;
		 
		 if(!pattern.test(num)){
		    return false;
		 }
		 return true;
	}
	
	//是否为正确的电话号码
	lm.isMobile = function(mobile) {
		//var mobile = $("#mobile").val();
		
		if(/^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/.test(mobile)){
		return true;
		}
		//if(/^1[3|4|5|8][0-9]\d{8}$/.test(mobile)){ 
	      //	return true; 
	 	//}
	 	return false; 
	}
	//是否为正确的邮箱输入
	lm.isEmail = function(email) {
		if (/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/.test(email)) {
			return true;
		}
	 	return false; 
	}
	//是否为正确的邮箱输入
	lm.isIdCard = function(idCard) {
		if ((/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(idCard))) {
			return true;
		}
	 	return false; 
	}
	
	//验证密码是否不为空且只能为英文或者数字
	lm.isPassword = function(password) {
		return !$.trim(password) || !password.match(/([\w]){2,15}$/)? false:true;
	}
	
	lm.Now=new Date();
	
	//获取当前时间的js
	lm.NowFormat = function(format) {
		return format==null||format==""?new Date().Format("yyyy-MM-dd"):new Date().Format(format);
	}
	//格式化输出时间接口
	lm.DateFormat = function(date,format) {
		return  date.Format(format);
	}
	//格式化时间开始
	Date.prototype.Format = function(fmt){  
		var o = {
	        "M+" : this.getMonth()+1,                 //月份
	        "d+" : this.getDate(),                    //日
	        "h+" : this.getHours(),                   //小时
	        "m+" : this.getMinutes(),                 //分
	        "s+" : this.getSeconds(),                 //秒
	        "q+" : Math.floor((this.getMonth()+3)/3), //季度
	        "S"  : this.getMilliseconds()             //毫秒
		};
	    if(/(y+)/.test(fmt)){
	        fmt = fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
	    }
	    for(var k in o){  
	        if(new RegExp("("+ k +")").test(fmt)){
	            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
	        }
	    }
	    return fmt;
	}//格式化时间结束
	
	//防止多次提交JS代码
	var repeatSubmitStatus=false;
	lm.onSbumitBefore=function(obj){
		var repeatSubmit  = $(obj).attr("repeatSubmit");
		$(obj).find("input[repeatSubmit=1]").each(function (){
			$(this).attr("disabled","disabled");
		});
		if (repeatSubmit=='1'&&!repeatSubmitStatus) {
			repeatSubmitStatus=true;
			return true;
		}else{
			repeatSubmitStatus=true;
			return false;
		}
	}
	
		$("form[repeatSubmit='1']").each(function (){
			$(this).attr("onsubmit","return lm.onSbumitBefore(this)");
		});
		$("form[repeatSubmit='2']").each(function (){
			$(this).attr("onsubmit","return lm.onSbumitBefore(this)");
		});
	
	//防止多次提交JS代码结束
	
});
