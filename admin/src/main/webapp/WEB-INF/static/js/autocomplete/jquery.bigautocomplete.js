// --  全局变量   -- 便于管理
var $bigAutocompleteContent$;
// -- 键盘上功能键键值数组  -- 
var functionalKeyArray = [9,20,13,16,17,18,91,92,93,45,36,33,34,35,37,39,112,113,114,115,116,117,118,119,120,121,122,123,144,19,145,40,38,27];
// 输入框中原始输入的内容
var holdText = null;

(function($){
	var bigAutocomplete = new function(){
		
		// 目前获得光标的输入框（解决一个页面多个输入框绑定自动补全功能）
		currentInputText = null;
		
		this.autocomplete = autoCompleteFunction;
	};
	
	$.fn.bigAutocomplete = bigAutocomplete.autocomplete;
	
})(jQuery)

/**
 * 初始化插入自动补全DIV，并在document注册mouse down，点击非IDV区域隐藏DIV
 */
function init(){
	$("body").append("<div id='bigAutocompleteContent' class='bigautocomplete-layout'></div>");
	$bigAutocompleteContent$ = $("#bigAutocompleteContent");
	
	$(document).bind('mousedown',function(event){
		var $target = $(event.target);
		
		if( (!($target.parents().andSelf().is('#bigAutocompleteContent'))) && (!$target.is($(currentInputText))) ){
			hideAutoComplete();
		}
	})
	
	// 鼠标悬停时选中当前行
	$("#bigAutocompleteContent").delegate("tr", "mouseover", function() {
		$("#bigAutocompleteContent tr").removeClass("ct");
		$(this).addClass("ct");
	}).delegate("tr", "mouseout", function() {
		$("#bigAutocompleteContent tr").removeClass("ct");
	});		
	
	// 单击选中行后，选中行内容设置到输入框中，并执行callback函数
	$("#bigAutocompleteContent").delegate("tr", "click", function() {
		$(currentInputText).val( $(this).find("div:last").html());
		var config = $(currentInputText).data("config");
		var callback_;
		if( undefined != config ){
			callback_ = config.callback;			
		}
		if( $("#bigAutocompleteContent").css("display") != "none" && undefined != callback_ && null != callback_ && $.isFunction(callback_) ){
			callback_($(this).data("jsonDataKey"));
		}				
		hideAutoComplete();
	})			
}

/**
 * 自动补全功能入口
 */
function autoCompleteFunction(param){

	if( $("body").length > 0 && $("#bigAutocompleteContent").length <= 0 ){
		
		// 初始化信息
		init();
	}
	
	// 为绑定自动补全功能的输入框jquery对象
	var $this = this;
	
	this.config = {
		               // width:下拉框的宽度，默认使用输入框宽度
		               width:0,
		               
		               // URL：格式URL:""用来AJAX后台获取数据，返回的数据格式为data参数一样
		               url:null,
		               
		               /* 
		                *  data：格式{data:[{title:null,result:{}},{title:null,result:{}}]}
		               	*  URL和data参数只有一个生效，data优先
		                */
		               data:null,
		               
		               // callback：选中行后按回车或单击时回调的函数
		               callback:null
	             };
	
	$.extend(this.config,param);
	
	$this.data("config",this.config);
	
	// 输入框key down事件
	bindingKeyDownEvent($this);
	
	// 输入框key up事件
	bindingKeyUpEvent($this);
	
	// 输入框focus事件
	$this.focus(function(event){
		currentInputText = event.currentTarget;
	});
}

/**
 * 获取数据的请求方式并响应结果(如果是URL请求,则请求参数名是keyword)
 * @param config 配置的请求方式
 * @param keyword_ 查询的请求数据
 */
function getRequestMethodAndResponseResult(config,keyword_){
	var have_data = true;
	var data = config.data;
	if( undefined != data && null != data && $.isArray(data) ){
		have_data = false;
		var data_ = new Array();
		for( var i = 0 ; i < data.length ; i++ ){
			if( data[i].title.indexOf(keyword_) > -1 ){
				data_.push(data[i]);
			}
		}
		makeContentAndShow(data_);
	}
	
	if( have_data ){
		var url = config.url; 
		
		//AJAX请求数据(参数名为:keyword)
		if( undefined != url && null != url && "" != url ){ 
			$.post( url , { keyword:keyword_ } , function(result){ 
				makeContentAndShow(result)
			} , "json" )
		}
	}
}

/**
 * 组装下拉框HTML内容并显示(显示的是对象的name属性)
 * @param data_ 传入的显示数组列表
 */
function makeContentAndShow(data_){
	if( null == data_ || "" == data_ || data_.length <= 0 ){
		return;
	}
	
	if( undefined == $bigAutocompleteContent$ || null == $bigAutocompleteContent$ || "" == $bigAutocompleteContent$ ){
		return;
	}
	
	// -- 组拼下拉显示内容   -- start
	var content = "<table><tbody>";
	for( var i = 0 ; i < data_.length ; i++ ){
		content += "<tr><td><div>" + data_[i].name + "</div></td></tr>"
	}
	content += "</tbody></table>";
	// -- 组拼下拉显示内容   -- end
	
	$bigAutocompleteContent$.html(content);
	$bigAutocompleteContent$.show();
	
	//每行TR绑定数据，返回给回调函数
	$bigAutocompleteContent$.find("tr").each(function(index){
		
		// 将每个TR行都存放对应的响应数据
		$(this).data("jsonDataKey",data_[index]); 
	})
}

/**
 * 隐藏组拼的下拉框
 */
function hideAutoComplete(){
	if( undefined == $bigAutocompleteContent$ || null == $bigAutocompleteContent$ || "" == $bigAutocompleteContent$ ){
		return;
	}
	if( $bigAutocompleteContent$.css("display") != "none" ){
		$bigAutocompleteContent$.find("tr").removeClass("ct");
		$bigAutocompleteContent$.hide();
	}		
}

/**
 * 绑定自动补全对象的KEYUP事件
 * @param bindingObj 待绑定事件的自动补全对象
 */
function bindingKeyUpEvent(bindingObj){
	
	$(bindingObj).keyup(function(event) {
		
		// 按下的键值
		var k = event.keyCode;
		var node = event.currentTarget;
		var config = $(node).data("config");
		
		// 回车键处理
		if( k == 13 ){
			
			// 得到CONFIG配置中的回调函数
			var callback_;
			if( undefined != config ){
				callback_ = config.callback;				
			}
			if( $bigAutocompleteContent$.css("display") != "none" ){
				if( undefined != callback_ && null != callback_ && $.isFunction(callback_) ){
					
					// 调用配置中定义的回调函数
					callback_($bigAutocompleteContent$.find(".ct").data("jsonDataKey"));
				}
				$bigAutocompleteContent$.hide();						
			}
			
			// 如果是回车键则之后的判断不处理了
			return; 
		}
		
		// 按下的键是否是功能键
		var isFunctionalKey = false; 
		
		if( -1 != functionalKeyArray.indexOf(k) ){
			isFunctionalKey = true;
		}
		
		var ctrl = event.ctrlKey;
		
		// k键值不是功能键 或 是CTRL+c、CTRL+x时才触发自动补全功能
		if( !isFunctionalKey && ( !ctrl || (ctrl && k == 67) || (ctrl && k == 88) ) ){
			
			// -- 取到查询的值	--
			var keyword_ = $.trim( $(node).val() );
			if( undefined == keyword_ || null == keyword_ || "" == keyword_ ){
				hideAutoComplete();
				return;
			}
			
			if( undefined == config ){
				return;				
			}
			if( config.width <= 0 ){
				config.width  = $(node).outerWidth() - 2;
			}
			$bigAutocompleteContent$.width(config.width);
			
			var h = $(node).outerHeight() - 1;
			
			var offset = $(node).offset();
			
			$bigAutocompleteContent$.css({"top":offset.top + h,"left":offset.left});
			
			// -- 取到配置中的数据请求方式(优先data数据,如果data没有才采取根据URL获取数据方式) --
			getRequestMethodAndResponseResult(config,keyword_);
			
			holdText = $(node).val();
		}
	});	
}

/**
 * 绑定自动补全对象的KEYDOWN事件
 * @param bindingObj 待绑定事件的自动补全对象
 */
function bindingKeyDownEvent(bindingObj){
	
	$(bindingObj).keydown(function(event) {
		
		var node = event.currentTarget;
		
		switch( event.keyCode ) {
		
		    // ESC键隐藏下拉框
			case 27: 
				hideAutoComplete();
				break;
				
			// 向上键
			case 38: 
				if( $bigAutocompleteContent$.css("display") == "none" ){
					return;
				}
				
				var $previousSiblingTr = $bigAutocompleteContent$.find(".ct");
				
				// 没有选中行时，选中最后一行
				if( $previousSiblingTr.length <= 0 ){ 
					$previousSiblingTr = $bigAutocompleteContent$.find("tr:last");
				}else{
					$previousSiblingTr = $previousSiblingTr.prev();
				}
				
				$bigAutocompleteContent$.find("tr").removeClass("ct");
				
				// 有上一行时（不是第一行）
				if( $previousSiblingTr.length > 0 ){
					
					// 选中的行加背景
					$previousSiblingTr.addClass("ct");
					
					// 选中行内容设置到输入框中
					$(node).val($previousSiblingTr.find("div:last").html());
					
					//DIV滚动到选中的行,jquery-1.6.1 $$previousSiblingTr.offset().top 有bug，数值有问题
					$bigAutocompleteContent$.scrollTop($previousSiblingTr[0].offsetTop - $bigAutocompleteContent$.height() + $previousSiblingTr.height());
					
				} else {
					
					// 输入框显示用户原始输入的值
					$(node).val(holdText);
				}
				
				break;
				
			case 40: // 向下键
				
				if( $bigAutocompleteContent$.css("display") == "none" ){
					return;
				}
				
				var $nextSiblingTr = $bigAutocompleteContent$.find(".ct");
				
				// 没有选中行时，选中第一行
				if( $nextSiblingTr.length <= 0 ){
					$nextSiblingTr = $bigAutocompleteContent$.find("tr:first");
				} else {
					$nextSiblingTr = $nextSiblingTr.next();
				}
				
				$bigAutocompleteContent$.find("tr").removeClass("ct");
				
				// 有下一行时（不是最后一行）
				if( $nextSiblingTr.length > 0 ){
					
					// 选中的行加背景
					$nextSiblingTr.addClass("ct");
					
					// 选中行内容设置到输入框中
					$(node).val($nextSiblingTr.find("div:last").html());
					
					//div滚动到选中的行,jquery-1.6.1 $nextSiblingTr.offset().top 有bug，数值有问题
					$bigAutocompleteContent$.scrollTop($nextSiblingTr[0].offsetTop - $bigAutocompleteContent$.height() + $nextSiblingTr.height() );
					
				} else {
					
					// 输入框显示用户原始输入的值
					$(node).val(holdText);
				}
				
				break;
		}
	});	
}