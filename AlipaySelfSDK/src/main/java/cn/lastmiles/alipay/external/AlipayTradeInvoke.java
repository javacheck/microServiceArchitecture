/**
 * createDate : 2016年7月25日下午2:57:40
 */
package cn.lastmiles.alipay.external;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import cn.lastmiles.alipay.bean.ExtendParams;
import cn.lastmiles.alipay.bean.Pay;
import cn.lastmiles.alipay.config.Configs;
import cn.lastmiles.alipay.config.Constants;
import cn.lastmiles.alipay.model.builder.trade.AlipayTradePayRequestBuilder;
import cn.lastmiles.alipay.model.builder.trade.AlipayTradeRefundRequestBuilder;
import cn.lastmiles.alipay.model.result.trade.AlipayF2FPayResult;
import cn.lastmiles.alipay.model.result.trade.AlipayF2FRefundResult;
import cn.lastmiles.alipay.service.abs.AbsAlipayTradeService;
import cn.lastmiles.alipay.service.impl.AlipayTradeServiceImpl;

public class AlipayTradeInvoke {

	private static AbsAlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
	
	public Map<String,Object> tradePay(Pay pay,String authToken){
		 AlipayTradePayRequestBuilder builder = new AlipayTradePayRequestBuilder();
		 builder.setOutTradeNo(pay.getOutTradeNo());  // 商户订单号
		 builder.setScene(pay.getScene()); // 支付场景 ,默认是 条码支付
		 builder.setAuthCode(pay.getAuthCode()); // 支付授权码
		 if( StringUtils.isNotBlank(pay.getSellerId()) ){
			 builder.setSellerId(pay.getSellerId()); // 如果该值为空，则默认为商户签约账号对应的支付宝用户ID			 
		 }
		 builder.setTotalAmount( pay.getTotalAmount()+"" ); // 订单总金额
		 builder.setDiscountableAmount( (null == pay.getDiscountableAmount() ? "" : pay.getDiscountableAmount()+"") ); // 参与优惠计算的金额
		 builder.setUndiscountableAmount((null == pay.getUndiscountableAmount() ? "" : pay.getUndiscountableAmount()+"")); // 不参与优惠计算的金额
		 builder.setSubject(pay.getSubject()); // 订单标题
		 builder.setBody(pay.getBody()); // 订单描述
		 builder.setGoodsDetailList(pay.getGoodsDetail()); // 订单包含的商品列表信息
		 builder.setOperatorId(pay.getOperatorId()); // 商户操作员编号
		 builder.setStoreId(pay.getStoreId()); // 商户门店编号
		 builder.setTerminalId(pay.getTerminalId()); // 商户机具终端编号
		 builder.setAlipayStoreId(pay.getAlipayStoreId()); // 支付宝的店铺编号
		 builder.setExtendParams(pay.getExtendParams()); // 业务扩展参数
		 builder.setTimeoutExpress(pay.getTimeoutExpress()); // 该笔订单允许的最晚付款时间
		 
		 if( StringUtils.isNotBlank(authToken) ){
			 builder.setAppAuthToken(authToken);	
			 ExtendParams extendParams = new ExtendParams();
			 extendParams.setSysServiceProviderId(Configs.getPid());
			 builder.setExtendParams(extendParams);
		 }
		 
		 System.out.println(builder.toString());
//		 logger.debug("tradePay builder is <{}>",builder.toString());
		
		 Map<String,Object> returnMap = new HashMap<String, Object>();
		 returnMap.put("requestParameters", builder);
		 // 调用tradePay方法获取当面付应答
	     AlipayF2FPayResult result = tradeService.tradePay(builder);
//	     logger.debug("tradePay result is <{}>",result.getResponse());
	     System.out.println(result.getResponse());
	     
	     returnMap.put("responseParameters", result);
	    
	     return returnMap;
	}
		
	public static void main(String[] args) {
//		Pay c = new Pay();
//		c.setAuthCode("281722737142019108");
//		c.setOutTradeNo("201609091636");
//		c.setTotalAmount(0.01D);
//		c.setSubject("测试扫码付款0909");
//		c.setTimeoutExpress("1c");
//		c.setStoreId("202020");
//		ExtendParams extendParams = new ExtendParams();
//		extendParams.setSysServiceProviderId("2088021174383713");
//		c.setExtendParams(extendParams);
//		AlipayTradeInvoke acf = new AlipayTradeInvoke();
//		acf.tradePay(c,"201609BB46f0fe1b545d4495b6786424d4c85F71"); 
		
//		acf.storesImageUpload("营业执照", new File("F:/yyzz.png"));
		AlipayTradeRefundRequestBuilder builder = new AlipayTradeRefundRequestBuilder();
		builder.setAppAuthToken("201610BB4aaace4004f44660ab3a319b96911X13");
		builder.setOutTradeNo("20074442"); // 商户订单号
		builder.setRefundAmount("0.1");
		builder.setAlipayStoreId("2016072600077000000017781832");
		builder.setRefundReason("支付宝退款");
		
		AlipayF2FRefundResult result = tradeService.tradeRefund(builder);
		System.out.println(result.getResponse().getCode() + "===" + result.getResponse().getMsg());
	}
	
}