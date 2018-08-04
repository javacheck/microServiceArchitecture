// 测试
$( function(){
	var aArray = {};//定义一个数组
	aArray=[
		{"id":"1","productName":"玛琪雅朵","productPrice":'10',"productCup":"小杯","productSugar":"少糖","productSize":"小号"},
		{"id":"2","productName":"玛琪雅朵","productPrice":'20',"productCup":"中杯","productSugar":"少糖","productSize":"小号"},
		{"id":"3","productName":"玛琪雅朵","productPrice":'30',"productCup":"大杯","productSugar":"少糖","productSize":"小号"},
		
		{"id":"4","productName":"玛琪雅朵","productPrice":'40',"productCup":"小杯","productSugar":"中糖","productSize":"小号"},
		{"id":"5","productName":"玛琪雅朵","productPrice":'50',"productCup":"中杯","productSugar":"中糖","productSize":"小号"},
		{"id":"6","productName":"玛琪雅朵","productPrice":'60',"productCup":"大杯","productSugar":"中糖","productSize":"小号"},
		
		{"id":"7","productName":"玛琪雅朵","productPrice":'70',"productCup":"小杯","productSugar":"多糖","productSize":"小号"},
		{"id":"8","productName":"玛琪雅朵","productPrice":'80',"productCup":"中杯","productSugar":"多糖","productSize":"小号"},
		{"id":"9","productName":"玛琪雅朵","productPrice":'90',"productCup":"大杯","productSugar":"多糖","productSize":"小号"},
		
	]


	
	$("input[type='radio']").change(function(){
		var vCup=$("input[name='select0']:checked").val();
		var vSugar=$("input[name='select1']:checked").val();
		var vSize=$("input[name='select2']:checked").val();
		var vNum=$("input[name='productcountnow']").val();
		
		$.each(aArray,function(index,content){ 
			if(vCup==content.productCup && vSugar==content.productSugar && vSize==content.productSize ){
			 // alert("price="+content.productPrice);  
			  $('.totalPrice').html(content.productPrice*vNum);
			}
		});
	});
	
});