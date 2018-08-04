// 功能：此JS只适用于input输入框的字段检测和判断
// 说明：用此JS进行判断,必须有isRequired(是否必须有值0|1)标识
// 要进行提示,请写上tipName标识。
// 要进行数据格式判断,请写上如下的规则：
$(function(){
	checkParameters = function (formName){
		
		// form表单必须有method标识,所以用此标识来判断表单是否存在
		if( undefined == $("#"+formName).attr("method") ){
			lm.alert("表单" + formName + "未发现");
			return false;
		}
		 try{
			// 是否可以提交的关键标识
			var flag = false;
			
			// 开始循环input标签判断
			$("#" + formName + " div input,textarea").each(function(key,value){
				var isRequired = $(value).attr("isRequired");
				
				// 如果input标签没有isRequired标识,则不认为是用户输入框
				if( undefined == isRequired ){
					return ;//实现continue功能
				}
				
				var fieldValue = $(value).val();
				fieldValue = $.trim(fieldValue);
				var tipName = $(value).attr("tipName");
				if( undefined == tipName ){
					tipName = "此字段";
				}
				
				// 必填字段不允许为空判断
				if( isRequired == "1" ){ 
					// 判断为空
					if(  undefined == $(value) || null == fieldValue || "" == fieldValue ){
						lm.alert(tipName + "不能为空！");
						$(value).focus();
						flag = true;
						return false;
					}
				}
				
				// 统一检测不为空字段的数据合法性
				if( null != fieldValue && "" != fieldValue && undefined != $(value) ){
					if( !checkUniqueness(value) ){
						flag = true;
						return false;
					};
					
					if( !checkSyntax(value) ){
						flag = true;
						return false;
					}
				}
			});
			// 结束循环input标签判断
	     } catch(e) {
            return e.description;
         }
	        
		if( !flag ){
			if( !extraFunction() ){
				return false;
			}
			$("#"+ formName).submit();
		} 
	};
	
	blurParameters = function(formName){
		if( undefined == $("#"+formName).attr("method") ){
			lm.alert("表单" + formName + "未发现");
			return false;
		}
		
		$("#" + formName + " div input,textarea").blur(function(){
			var isRequired = $(this).attr("isRequired");
			
			// 如果input标签没有isRequired标识,则不认为是用户输入框
			if( undefined == isRequired ){
				return false;
			}
			
			var fieldValue = $(this).val();
			fieldValue = $.trim(fieldValue);
			var tipName = $(this).attr("tipName");
			if( undefined == tipName ){
				tipName = "此字段";
			}			
			
			// 统一检测不为空字段的数据合法性
			if( null != fieldValue && "" != fieldValue && undefined != $(this) ){
				if( !checkUniqueness(this) ){
					return false;
				};
				
				if( !checkSyntax(this) ){
					return false;
				}										
			}
		});
	}
	
	keyupParameters = function(formName){
		if( undefined == $("#"+formName).attr("method") ){
			lm.alert("表单" + formName + "未发现");
			return false;
		}
		
		$("#" + formName + " div input,textarea").keyup(function(){
			var isRequired = $(this).attr("isRequired");
			
			// 如果input标签没有isRequired标识,则不认为是用户输入框
			if( undefined == isRequired ){
				return false;
			}
			
			var fieldValue = $(this).val();
			fieldValue = $.trim(fieldValue);
			
			// 统一检测不为空字段的数据合法性
			if( null != fieldValue && "" != fieldValue && undefined != $(this) ){
				if( !checkSyntax(this) ){
					return false;
				}
			}
		});
	}
	
	// 用于一些单个字段唯一性验证的判断
	checkUniqueness = function(){
		return true;
	};
	
	// 用于自定义的特殊判断<非单个字段的判断>(比如两个输入框：密码和确认密码的对比判断)
	extraFunction = function(){
		return true;
	};
	
	// 根据规则检测语法
	checkSyntax = function (obj){
		var fieldType = $(obj).attr("fieldType");
		var value = $(obj).val();
		value = $.trim(value);
		if("" == value){
			return true; // 值为空的时候也不进行检验
		}
		// 当没有fieldType属性时，则不进行语法检测 
		if( undefined == fieldType ){
			return true;
		}
		var tipName = $(obj).attr("tipName");
		if( undefined == tipName ){
			tipName = "字段数据";
		}
		
		if( fieldType == "mobile" ) { // 手机号码类型
			if( isMobile(value) ){
				lm.alert("请输入正确的"+ tipName + "！");
				$(obj).focus();
				return false;
			}
		} else if( fieldType == "phone" ){ // 联系电话类型
			if( (!(/^(\d{3,4}-)\d{7,8}$/.test(value)) && (!(/^\+?[1-9][0-9]*$/.test(value)))) ){
				lm.alert("请输入正确的"+tipName + "！");
				$(obj).focus();
				return false;
			}
		} else if( fieldType == "pInteger" ){ // 只能输入正整数
			if( !(/^\+?[1-9][0-9]*$/.test(value)) ){
				lm.alert(tipName + "只能输入正整数！");
				$(obj).focus();
				return false;
			}
		} else if( fieldType == "pDouble" ){ // 只能输入两位小数的正实数
			if( !(lm.isFloat(value) && value >= 0) ){
				lm.alert(tipName + "输入错误！");
				$(obj).focus();
				return false;
			}
		} else if( fieldType == "password" ){ // 密码类型
			var result = isPassword(value);
			if( result == 0 ){
				lm.alert(tipName + "长度不能小于6位且不能大于18位");
				$(obj).focus();
				return false;
			}
			if( result == 1 ){
				lm.alert(tipName + "只能为英文或者数字！");
				$(obj).focus();
				return false;
			}
		} else if( fieldType == "pRate"){
			if( !(lm.isFloat(value) && value >= 0 && value <= 100) ){
				lm.alert(tipName + "输入错误！");
				$(obj).focus();
				return false;
			}
		}
		return true;
	};
	
	// 手机号码验证
	isMobile = function(fieldValue){
		if( !(/^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/.test(fieldValue)) ){
			return true;
		}
		return false;
	};
	
	isPassword = function(fieldValue){
		if( fieldValue.length < 6 || fieldValue.length > 19 ){
			return 0; // 长度限制
		}
		if( !(/([\w]){6,19}$/.test(fieldValue)) ){
			return 1; // 只能输入英文或者数字
		}
	}
	
});