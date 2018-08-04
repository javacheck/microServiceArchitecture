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
	var bankId="${businessBank.bankId}";
	if(bankId!=""){
		$("#bankName option[value=" + bankId + "]").attr("selected",true);
	}
	var isDefault="${businessBank.isDefault}";
	if(isDefault==1){
		$("#isDefault").attr("checked",true);
	}
});

//银行卡号校验
//Description:  银行卡号Luhm校验
//Luhm校验规则：16位银行卡号（19位通用）:
// 1.将未带校验位的 15（或18）位卡号从右依次编号 1 到 15（18），位于奇数位号上的数字乘以 2。
// 2.将奇位乘积的个十位全部相加，再加上所有偶数位上的数字。
// 3.将加法和加上校验位能被 10 整除。
function luhmCheck(bankno){
	if (bankno.length < 16 || bankno.length > 19) {
		lm.alert("银行卡号长度必须在16到19之间");
		return false;
	}
	var num = /^\d*$/;  //全数字
	if (!num.exec(bankno)) {
		lm.alert("银行卡号必须全为数字");
		return false;
	}
	//开头6位
	var strBin="10,18,30,35,37,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,58,60,62,65,68,69,84,87,88,94,95,98,99";    
	if (strBin.indexOf(bankno.substring(0, 2))== -1) {
		lm.alert("银行卡号开头6位不符合规范");
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
    	lm.alert("银行卡号必须符合Luhm校验");
    	return false;
    }        
}
//保存代理商信息
function saveAgent(){
	var id=$("#id").val();
	
	if($("#businessId").val()==""){
		var businessId=$("#businessId1").val();
	}else{
		var businessId=$("#businessId").val();
	}
	var bankId=$("#bankName").find("option:selected").val();
	var bankName=$("#bankName").find("option:selected").text();
	var subbranch=$.trim($("#subbranch").val());//支行
	if(subbranch==null || subbranch==""){
		lm.alert("支行不能为空！");
		$("#subbranch").focus();
		return;
	}
	var accountNumber=$("#accountNumber").val();
	
	if(accountNumber==""){
		lm.alert("卡号不能为空！");
		$("#accountNumber").focus();
		return; 
	}
	//alert(luhmCheck(accountNumber));
	if(!luhmCheck(accountNumber)){
		return;
	}
	var accountName=$("#accountName").val();
	if(accountName==""){
		lm.alert("持卡人姓名不能为空！"); 
		$("#accountName").focus();
		return; 
	}
	var mobile=$("#mobile").val();
	if(mobile==""){
		lm.alert("持卡人手机不能为空！"); 
		$("#mobile").focus();
		return; 
	}
	if(!(/^1[3|4|5|8][0-9]\d{8}$/.test(mobile))){ 
		lm.alert("请输入正确的手机号码"); 
		$("#mobile").focus();
		return; 
	 }
	if(document.getElementById("isDefault").checked==true){
		isDefault=1;   
	}else{ 
		isDefault=0;  
	}
	var flag=true;
	
	lm.postSync("${contextPath }/agent/checkBank/", {id:id,businessId:businessId,accountNumber:accountNumber}, function(data) {
		if (data == 1) { 
			lm.alert("此银行卡已被绑定,请重新输入!");
			flag=false;
		}
	});
	if(flag){
		lm.post("${contextPath}/agent/businessList/ajax/editBusinessBank",{id:id,businessId:businessId,bankName:bankName,accountNumber:accountNumber,accountName:accountName,mobile:mobile,isDefault:isDefault,subbranch:subbranch,bankId:bankId},function(data){
			if(data=="0"){
				lm.alert("该卡号已存在！");
				return;
			}else{
				lm.alert("保存成功！");
				window.location.href="${contextPath}/agent/businessList/"+businessId;
			}
		});
	}
	
}
</script>
</head>
<body>
	<div class='panel'>
		<div class='panel-heading'>
			<strong>
				<i class='icon-plust'></i>${empty businessBank ? '添加' : '修改' }银行卡
			</strong>
		</div>
		<div class='panel-body'>
			<div  class='form-horizontal' id="agent">
			
				<!-- 修改时传过来的绑定银行卡ID -->
				<input id="id" name="id" type="hidden" value="${businessBank.id }" />
				<input id="businessId" name="businessId" type="hidden" value="${businessId }" />
				<input id="businessId1" name="businessId1" type="hidden" value="${businessBank.businessId }" />
				
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>银行卡名称</label>
					<div class="col-md-2">
		            	<select name="bankName" class="form-control" style="width: 250px;" id="bankName">
							<c:forEach items="${bankList }" var="bank" >
								<option  value ="${bank.id}">${bank.name}</option>
							</c:forEach>
		            	</select>
	            	</div>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>支行</label>
					<div class="col-md-2">
						<input type='text' id="subbranch" name="subbranch" style="width: 250px;" class='form-control' value="${businessBank.subbranch }"  maxlength="100"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>卡号</label>
					<div class="col-md-2">
						<input type='text' id="accountNumber" name="accountNumber" style="width: 250px;" class='form-control' value="${businessBank.accountNumber }" maxlength="19"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>持卡人姓名</label>
					<div class="col-md-2">
						<input type='text' id="accountName" name="accountName" style="width: 250px;" class='form-control' value="${businessBank.accountName }"  maxlength="10"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"><span style="color: red;font-size: 15px">*</span>持卡人手机</label>
					<div class="col-md-2">
						<input type='text' id="mobile" name="mobile" style="width: 250px;" class='form-control' value="${businessBank.mobile }"  maxlength="11"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label"></label>
					<div class="col-md-2">
						<input type="checkbox" name="isDefault" id ="isDefault" value="" >设为默认结算账户
					</div>
				</div>
				
				
				<div class="form-group">
					<div class="col-md-offset-1 col-md-1 0">
						<input type='button' id='addAgentBtn' class='btn btn-primary' value="${empty businessBank ? '添加' : '修改' }" onclick="saveAgent();"/>
					</div>
				</div>
		 </div>
	  </div>
   </div>
</body>
</html>