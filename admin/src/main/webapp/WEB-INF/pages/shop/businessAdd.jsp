<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${empty businessBank ? '添加' : '修改' }银行卡</title> 
<script type="text/javascript">
$(document).ready(function(){
	if( null != $("#id").val() && "" != $("#id").val() ){ // 修改
		$("#accountType").val('${businessBank.accountType}'); // 赋值类型
	}
});
$(document).ready(function(){
	var bankName="${businessBank.bankName}";
	if(bankName!=""){
		$("#bankName option[value=" + bankName + "]").attr("selected",true);
	}
	var isDefault="${businessBank.isDefault}";
	if(isDefault=="1"){
		$("#default").attr("checked",true);
	}
	
	if( null != $("#businessId1").val() && "" != $("#businessId1").val() ){
		$("#businessId").val($("#businessId1").val());
	}
	
	 // 
	 $("[name = default]:checkbox").bind("click", function () {
		 var isCheck = $('input:checkbox[name="default"]:checked').val();
		 	if(isCheck == "1"){
		 		$("#isDefault").val(1);
		 	} else {
		 		$("#isDefault").val(0);
		 	}
	  })
	  
	//保存代理商信息
	$("#businessBankAddBtn").click(function(){	
			var id=$("#id").val();
			
			if($("#businessId").val()==""){
				var businessId=$("#businessId1").val();
			}else{
				var businessId=$("#businessId").val();
			}
			var bankName=$("#bankName").find("option:selected").val();
			var subbranch=$("#subbranch").val();//支行
			var subbranch=$.trim($("#subbranch").val());//支行
			if(subbranch==null || subbranch==""){
				lm.alert("支行不能为空！");
				$("#subbranch").focus();
				return;
			}
			if(luhmCheck()){
				var flag = false;
				var businessId = $("#businessId").val();
				var businessId1 = $("#businessId1").val();
				var accountNumber = $("#accountNumber").val();
				var accountNumberCache = $("#accountNumberCache").val();
				if( businessId == businessId1){
					if(accountNumberCache!=accountNumber){
						lm.postSync("${contextPath }/shop/checkBank/", {businessId:businessId,accountNumber:accountNumber}, function(data) {
							if (data == 1) { 
								lm.alert("此账户号已被绑定,请重新输入!");
								flag = true;
								return;
							} 
						});		
					}
				} else {
					lm.postSync("${contextPath }/shop/checkBank/", {businessId:businessId,accountNumber:accountNumber}, function(data) {
						if (data == 1) { 
							lm.alert("此账户号已被绑定,请重新输入!");
							flag = true;
							return ;
						} 
					});
				}

				if(flag){
					return ;
				}
				var accountName=$("#accountName").val();
				if(accountName==""){
					lm.alert("账户名称不能为空！"); 
					$("#accountName").focus();
					return; 
				}
				var mobile=$("#mobile").val();
				if( null != mobile && mobile!=""){ // 不为空，则判断手机号码是否符合条件
					if(!(/^1[3|4|5|8][0-9]\d{8}$/.test(mobile))){ 
						lm.alert("请输入正确的手机号码"); 
						$("#mobile").focus();
						return; 
					 }					
				}
				if("" == $("#isDefault").val()){
					$("#isDefault").val(0)
				}
				$("#businessBankAddForm").submit();	
				$("#businessBankAddBtn").attr("disabled",true);
			};
	});
});

//银行卡号校验
//Description:  银行卡号Luhm校验
//Luhm校验规则：16位银行卡号（19位通用）:
// 1.将未带校验位的 15（或18）位卡号从右依次编号 1 到 15（18），位于奇数位号上的数字乘以 2。
// 2.将奇位乘积的个十位全部相加，再加上所有偶数位上的数字。
// 3.将加法和加上校验位能被 10 整除。
function luhmCheck(){
	var bankno = $("#accountNumber");
	var accountType = $("#accountType").val();
	if( accountType == 1 ){ // 对公则不进行数据的验证
		if( !(/^\+?[1-9][0-9]*$/.test(bankno.val())) ){
			lm.alert("账户号输入错误");
			return false;
		}
		return true;
	}
	var obj = bankno;
	bankno = bankno.val();
	bankno = $.trim(bankno);
	if( "" == bankno ){
		lm.alert("账户号不能为空！");
		$(obj).focus();
		return false;
	}
	var num = /^\d*$/;  //全数字
	if (!num.exec(bankno)) {
		lm.alert("账户号必须全为数字");
		$(obj).focus();
		return false;
	}
	
	if (bankno.length < 16 || bankno.length > 19) {
		lm.alert("账户号长度必须在16到19之间");
		$(obj).focus();
		return false;
	}
	
	//开头6位
	var strBin="10,18,30,35,37,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,58,60,62,65,68,69,84,87,88,94,95,98,99";    
	if (strBin.indexOf(bankno.substring(0, 2))== -1) {
		lm.alert("账户号开头6位不符合规范");
		$(obj).focus();
		return false;
	}
    var lastNum=bankno.substr(bankno.length-1,1);//取出最后一位（与luhm进行比较）

    var first15Num=bankno.substr(0,bankno.length-1);//前15或18位
    var newArr=new Array();
    for(var i=first15Num.length-1;i>-1;i--){    //前15或18位倒序存进数组
        newArr.push(first15Num.substr(i,1));
    }
    var arrJiShu=new Array();  //奇数位*2的积 <9
    var arrJiShu2=new Array(); //奇数位*2的积 >9
    
    var arrOuShu=new Array();  //偶数位数组
    for(var j=0;j<newArr.length;j++){
        if((j+1)%2==1){//奇数位
            if(parseInt(newArr[j])*2<9)
            arrJiShu.push(parseInt(newArr[j])*2);
            else
            arrJiShu2.push(parseInt(newArr[j])*2);
        }
        else //偶数位
        arrOuShu.push(newArr[j]);
    }
    
    var jishu_child1=new Array();//奇数位*2 >9 的分割之后的数组个位数
    var jishu_child2=new Array();//奇数位*2 >9 的分割之后的数组十位数
    for(var h=0;h<arrJiShu2.length;h++){
        jishu_child1.push(parseInt(arrJiShu2[h])%10);
        jishu_child2.push(parseInt(arrJiShu2[h])/10);
    }        
    
    var sumJiShu=0; //奇数位*2 < 9 的数组之和
    var sumOuShu=0; //偶数位数组之和
    var sumJiShuChild1=0; //奇数位*2 >9 的分割之后的数组个位数之和
    var sumJiShuChild2=0; //奇数位*2 >9 的分割之后的数组十位数之和
    var sumTotal=0;
    for(var m=0;m<arrJiShu.length;m++){
        sumJiShu=sumJiShu+parseInt(arrJiShu[m]);
    }
    
    for(var n=0;n<arrOuShu.length;n++){
        sumOuShu=sumOuShu+parseInt(arrOuShu[n]);
    }
    
    for(var p=0;p<jishu_child1.length;p++){
        sumJiShuChild1=sumJiShuChild1+parseInt(jishu_child1[p]);
        sumJiShuChild2=sumJiShuChild2+parseInt(jishu_child2[p]);
    }      
    //计算总和
    sumTotal=parseInt(sumJiShu)+parseInt(sumOuShu)+parseInt(sumJiShuChild1)+parseInt(sumJiShuChild2);
    
    //计算Luhm值
    var k= parseInt(sumTotal)%10==0?10:parseInt(sumTotal)%10;        
    var luhm= 10-k;
    
    if(lastNum==luhm){
    	//$("#banknoInfo").html("Luhm验证通过");
   		return true;
    }
    else{
    	lm.alert("账户号必须符合Luhm校验");
    	$(obj).focus();
    	return false;
    }        
}

$(function(){
	var bankId= $(this).find("option:selected").attr("bankId");
	$("#bankId").val(bankId);
	$("#bankName").change(function(){
		var bankId= $(this).find("option:selected").attr("bankId");
		$("#bankId").val(bankId);
	});
});

</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>《${storeName}》${empty businessBank ? '添加' : '修改' }银行卡
			</strong>
		</div>
		<div class='panel-body'>
		<form id="businessBankAddForm" method='post' class='form-horizontal' action="${contextPath }/shop/saveBusinessBank" >
			
				<!-- 修改时传过来的绑定银行卡ID -->
				<input id="id" name="id" type="hidden" value="${businessBank.id }" />
				<input id="businessId" name="businessId" type="hidden" value="${businessId }" />
				<input id="businessId1" name="businessId1" type="hidden" value="${businessBank.businessId }" />
				<input id="accountNumberCache" name="accountNumberCache" type="hidden" value="${businessBank.accountNumber }"/>
				<input id="bankId" name="bankId" type="hidden" value=""/>
				
				<div class="form-group">
					<label class="col-md-1 control-label">账户类型</label>
					<div class="col-md-2">
						<select id="accountType" name="accountType" class='form-control'> 
							<option id="0" value="0">对私</option>
							<option id="1" value="1">对公</option>
						</select>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px">*</label>&nbsp;&nbsp;
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">开户银行</label>
					<div class="col-md-2">
		            	<select name="bankName" class="form-control"  id="bankName" >
							<c:forEach items="${bankList }" var="bank" >
								<option  value ="${bank.name}" bankId="${bank.id}">${bank.name}</option>
							</c:forEach>
		            	</select>
	            	</div>
	            	<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label">支行</label>
					<div class="col-md-2">
						<input type='text' id="subbranch" name="subbranch"  class='form-control' value="${businessBank.subbranch }"  maxlength="100"/>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">账户号</label>
					<div class="col-md-2">
						<input type='text' id="accountNumber" name="accountNumber"  class='form-control' value="${businessBank.accountNumber }" maxlength="19"/>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">账户名称</label>
					<div class="col-md-2">
						<input type='text' id="accountName" name="accountName"  class='form-control' value="${businessBank.accountName }"  maxlength="10"/>
					</div>
					<label class="col-md-0 control-label" style="color: red;font-size: 15px" >*</label>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">手机</label>
					<div class="col-md-2">
						<input type='text' id="mobile" name="mobile"  class='form-control' value="${businessBank.mobile }"  maxlength="11"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"></label>
					<div class="col-md-2">
						<input type="checkbox" name="default" id="default" value="1" >设为默认结算账户
						<input type="hidden" name="isDefault" id="isDefault" value="${businessBank.isDefault }">
					</div>
				</div>
				
				<div class="form-group">
					<div class="col-md-offset-1 col-md-1 0">
						<input type="button"  id='businessBankAddBtn' class='btn btn-primary' value="${empty businessBank ? '添加' : '修改' }" data-loading='稍候...' />
					</div>
				</div>
   			</form>
		 </div>
	  </div>
</body>
</html>