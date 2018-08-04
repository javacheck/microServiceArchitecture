package cn.lastmiles.pay.alipay;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class AliPayUtil {
	private final static Logger logger = LoggerFactory
			.getLogger(AliPayUtil.class);
	
	public static AliPayBean back(HttpServletRequest req)
			throws UnsupportedEncodingException {

		//获取支付宝POST过来反馈信息
		req.setCharacterEncoding("ISO-8859-1");
		Map<String,String> params = new HashMap<String,String>();
		Map<?, ?> requestParams = req.getParameterMap();
		for (Iterator<?> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			logger.debug("back-->支付宝请求参数  {}  is:{}",name,valueStr);
			if (valueStr!=null) {
				params.put(name, new String(valueStr.getBytes("ISO-8859-1"), "UTF-8"));
			}
		}
		

		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		if (!AlipayNotify.verify(params)) {
			logger.error("不是支付宝发送的数据");
			return null;
		}
		String seller_id = new String(req.getParameter("seller_id").getBytes("ISO-8859-1"),"UTF-8");
		if (!seller_id.equals(AlipayConfig.partner)) {
			logger.error("该数据不是我们平台的seller_id is:{}",seller_id);
			return null;
		}
		AliPayBean aliPayBean = new AliPayBean();
		try {
			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			//商户订单号
			aliPayBean.setOrderId(params.get("out_trade_no"));
			//支付宝交易号
			aliPayBean.setTradeNo(params.get("trade_no"));
			//交易状态
			aliPayBean.setTradeStatus(params.get("trade_status"));
			//通知时间
			aliPayBean.setNotifyTime(params.get("notify_time"));
			//通知类型
			aliPayBean.setNotifyType(params.get("notify_type"));
			//通知ID
			aliPayBean.setNotifyId(params.get("notify_id"));
			//加密类型
			aliPayBean.setSignType(params.get("sign_type"));
			//签名
			aliPayBean.setSign(params.get("sign"));
			//交易金额
			aliPayBean.setTotalFee(params.get("total_fee")!=null?Double.valueOf(Double.valueOf("1.00")*100).intValue():null);
			//交易创建时间
			aliPayBean.setGmtCreate(params.get("gmt_create"));
			//交易付款时间
			aliPayBean.setGmtPayment(params.get("gmt_payment"));
			//退款状态
			aliPayBean.setRefundStatus(params.get("refund_status"));
			//退款时间
			aliPayBean.setGmtRefund(params.get("gmt_refund"));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("back-->数据接收解析式错误");
			return null;
		}
		return aliPayBean;
	}
	public static boolean refund(String queryId,String refundId,Integer txnAmt){
		try {
			//把请求参数打包成数组
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("service", "refund_fastpay_by_platform_pwd");
			//合伙人ID
			sParaTemp.put("partner", AlipayConfig.partner);
			//字符格式
			sParaTemp.put("_input_charset", AlipayConfig.input_charset);
			//服务器异步通知页面路径 需http://格式的完整路径，不允许加?id=123这类自定义参数
			sParaTemp.put("notify_url", AlipayConfig.refund_notify_url);
			//卖家支付宝帐户 必填
			sParaTemp.put("seller_email", AlipayConfig.partner_email);
			//退款当天日期
			sParaTemp.put("refund_date", new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss").format(new Date()));
			// 批次号  必填，格式：当天日期[8位]+序列号[3至24位]，如：201008010000001
			sParaTemp.put("batch_no",
					new SimpleDateFormat("yyyyMMdd").format(new Date()) + "");
			//退款笔数 必填，参数detail_data的值中，“#”字符出现的数量加1，最大支持1000笔（即“#”字符出现的数量999个）
			sParaTemp.put("batch_num", "1");
			//退款详细数据 必填，具体格式请参见接口技术文档 原付款支付宝交易号^退款总金额^退款理由；
			sParaTemp.put("detail_data", queryId + "^" + txnAmt + "^" + "协商退款");
			logger.debug("refund  sParaTemp is {}", sParaTemp);
			String sHtmlText = AlipaySubmit.buildRequest(sParaTemp);
			logger.debug("refund  sHtmlText is {}", sHtmlText);
			return true;
		} catch (Exception e) {
			logger.debug("-->  refund is error");
			e.printStackTrace();
			return false;
		}
	}
	public static String refundBack(HttpServletRequest request) throws UnsupportedEncodingException{
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map<?, ?> requestParams = request.getParameterMap();
		for (Iterator<?> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			logger.debug("refundBack ---> {} is :{} ",name,valueStr);
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//批次号

		String batch_no = new String(request.getParameter("batch_no").getBytes("ISO-8859-1"),"UTF-8");
		logger.debug("batch_no is :{}",batch_no);
		//批量退款数据中转账成功的笔数

		String success_num = new String(request.getParameter("success_num").getBytes("ISO-8859-1"),"UTF-8");
		logger.debug("success_num is :{}",success_num);
		//批量退款数据中的详细信息
		String result_details = new String(request.getParameter("result_details").getBytes("ISO-8859-1"),"UTF-8");
		logger.debug("result_details is :{}",result_details);
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

		if(AlipayNotify.verify(params)){//验证成功
			return result_details;
		}
		return null;
	}
	
}
