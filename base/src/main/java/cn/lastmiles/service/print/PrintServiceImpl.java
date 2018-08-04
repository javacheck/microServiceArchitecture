package cn.lastmiles.service.print;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jodd.http.HttpRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.Order;
import cn.lastmiles.bean.OrderItem;
import cn.lastmiles.bean.Print;
import cn.lastmiles.bean.ProductAttributeValue;
import cn.lastmiles.common.utils.JsonUtils;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.service.OrderServise;
import cn.lastmiles.service.PrintService;
import freemarker.template.utility.StringUtil;

@Service
public class PrintServiceImpl {
	private final static Logger logger = LoggerFactory
			.getLogger(PrintServiceImpl.class);
	public static final String IP = "http://115.28.225.82";
	public static final String HOSTNAME = "/FeieServer";
	
	private static OrderServise orderServise;
	private static PrintService printService;
	private static TaskExecutor taskExecutor;
	private static Integer status=1;
	
	@Autowired
	public PrintServiceImpl(OrderServise os,PrintService ps ,TaskExecutor te){
		orderServise=os;
		printService=ps;
		taskExecutor = te;
	}
	//***接口返回值有如下几种***
	//return "0";//商家下没有找到打印机，返回0
	//{"responseCode":0,"msg":"服务器接收订单成功","orderindex":"xxxxxxxxxxxxxxxxxx"}
	//{"responseCode":1,"msg":"打印机编号错误"};
	//{"responseCode":2,"msg":"服务器处理订单失败"};
	//{"responseCode":3,"msg":"打印内容太长"};
	//{"responseCode":4,"msg":"请求参数错误"};
	public static void print(Long orderId) {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				print2(orderId);
			}
		});
	}
	
	public static void main(String[] args){
		Map<String, Object> forms = new HashMap<String, Object>();
		StringBuilder content = new StringBuilder();
		content.append("          莱麦小店<BR><BR>");
		content.append("订单编号：" + 10110010 + "<BR>");
		content.append("收银员：" + 10110010 + "<BR>");
		content.append("订单编号：" + 10110010 + "<BR>");
		content.append("打印时间：" + "2016-02-04 15:10:20" + "<BR>");
		content.append("------------------------------<BR>");
		
		content.append("商品名称");
		content.append("                 ");
		content.append("数量<BR>");
		
		content.append("------------------------------<BR>");
		content.append("合计：                    " + 3);
		
		
		forms.put("sn", "915501079");
		forms.put("printContent", content.toString());
		forms.put("key", "n5zimkXc");
		forms.put("times", "1");
		String ret = HttpRequest.post(IP+HOSTNAME+"/printOrderAction").form(forms).send().bodyText();
		Object obj = JsonUtils.jsonToMap(ret).get("responseCode");
     	System.out.println("0".equals(obj+""));
	}

	private static void print2(Long orderId){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowDate=sf.format(new Date());
		//标签说明："<BR>"为换行符,"<CB></CB>"为居中放大,"<B></B>"为放大,"<C></C>"为居中,<L></L>字体变高
		//"<QR></QR>"为二维码,"<CODE>"为条形码,后面接12个数字
		logger.debug("orderId={}",orderId);	
		
		Order order=orderServise.findById(orderId);
		//Print print= printService.findByStoreId(order.getStoreId());
		List<Print> prints=printService.findListByStoreId(order.getStoreId(),status);
		if(!prints.isEmpty()){
			String content;
			content = "<CB>"+order.getStore().getName()+"</CB><BR>";
			content += "订单编号："+order.getId()+"<BR>";
			content += "订单时间："+sf.format(order.getCreatedTime())+"<BR>";
			content += "打印时间："+nowDate+"<BR>";
			content += "留言：<BR>";
			content += order.getMessage()+"<BR>";
			content += "------------------------<BR>";
			content += "商品名称   数量   单价   小计<BR>";
			for(OrderItem orderItem:order.getOrderItems()){
				content +=orderItem.getProductStock().getProduct().getName()+" ";
				for(ProductAttributeValue productAttributeValue:orderItem.getProductStock().getPavList()){
					content +=productAttributeValue.getValue()+" ";
				}
				content+="<BR>";
				content += "            "+orderItem.getAmount()+"     "+orderItem.getPrice()+"    "+orderItem.getPrice()*orderItem.getAmount()+"<BR>";
			}
			content += "------------------------<BR>";
			content += "                 合计:   "+order.getPrice()+"<BR>";
			if(order.getShipAmount()!=null){
				content += "               配送费:   "+order.getShipAmount()+"<BR>";
			}else{
				content += "               配送费:   "+0.0+"<BR>";
			}
			if(order.getCashGiftDesc()!=null){
				content += "               优惠券:   -"+order.getCashGiftDesc()+"<BR>";
			}
			if(order.getBalance()!=null && order.getBalance().doubleValue()>0){
				content += "                 余额:   -"+order.getBalance()+"<BR>";
			}
			content += "<CB>应收(￥)："+(NumberUtils.add(order.getActualPrice(),0D))+"</CB><BR>";
			if(order.getPaymentMode().intValue()==7){
				content += "付款状态：货到付款                                <BR>";
			}else{
				content += "付款状态：已支付                                <BR>";
			}
			content += "--------------------------------<BR>";
			if(order.getShipType()!=null){
				content += "配送方式："+order.getShipType()+"<BR>";
			}else{
				content += "配送方式：<BR>";
			}
			if(order.getShipTime()!=null){
				content += "配送时间："+order.getShipTime()+"<BR>";
			}else{
				content += "配送时间：<BR>";
			}
			if(order.getAddress().getPhone()!=null){
				content += "收货人姓名："+order.getAddress().getName()+"<BR>";
			}else{
				content += "收货人姓名：<BR>";
			}
			if(order.getAddress().getPhone()!=null){
				content += "联系电话："+order.getAddress().getPhone()+"<BR>";
			}else{
				content += "联系电话：<BR>";
			}
			if(order.getAddress().getAddress()!=null){
				content += "送货地址:"+order.getAddress().getAddress()+"<BR>";
			}else{
				content += "送货地址:<BR>";
			}
			content += "--------------------------------<BR>";
			if(order.getStore().getPhone()!=null){
				content += "外送电话："+order.getStore().getPhone()+"<BR>";
			}else{
				content += "外送电话：<BR>";
			}
//					content += "<QR>http://www.lastmiles.com</QR>";
			for(int i=0;i<prints.size();i++){
				Map<String, Object> forms = new HashMap<String, Object>();
				forms.put("sn", prints.get(i).getPrintSn().trim());
				forms.put("printContent", content);
				forms.put("key", prints.get(i).getPrintKey().trim());
				forms.put("times", "1");
				logger.debug("forms >>>>>>>>>>>>>>>>>> = {} " , forms);
				String ret = HttpRequest.post(IP+HOSTNAME+"/printOrderAction").form(forms).send().bodyText();
				Object obj = JsonUtils.jsonToMap(ret).get("responseCode");
				logger.debug("order id = {},result = {}",orderId,obj);
				if(obj != null && obj.toString().equals("0")){
					orderServise.changeWifiPrinted(orderId,1);//打印成功，改变订单的打印状态为1已打印
				}
			}
		}
	
	}
}
