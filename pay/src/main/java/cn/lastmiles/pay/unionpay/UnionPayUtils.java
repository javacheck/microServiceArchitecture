package cn.lastmiles.pay.unionpay;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unionpay.acp.sdk.HttpClient;
import com.unionpay.acp.sdk.SDKConfig;
import com.unionpay.acp.sdk.SDKConstants;
import com.unionpay.acp.sdk.SDKUtil;

public abstract class UnionPayUtils {
	private final static Logger logger = LoggerFactory
			.getLogger(UnionPayUtils.class);
//	static {
//		logger.debug("loading acp_sdk.properties");
//		SDKConfig.getConfig().loadPropertiesFromPath("F:\\config\\pay");
//	}

	public final static String encoding = "UTF-8";
	/**
	 * 5.0.0
	 */
	public final static String version = "5.0.0";
	

	public static void main(String[] args) {
//		System.out.println(refund("201509161557469983688", "10000179", "20150916155746", 1));
	}
	
	
	public static boolean refund(String queryId,String refundId,String txnTime,Integer txnAmt){
		/**
		 * 组装请求报文
		 */
		Map<String, String> data = new HashMap<String, String>();
		// 版本号
		data.put("version", version);
		// 字符集编码 默认"UTF-8"
		data.put("encoding", encoding);
		// 签名方法 01 RSA
		data.put("signMethod", "01");
		// 交易类型 
		data.put("txnType", "04");
		// 交易子类型 
		data.put("txnSubType", "00");
		// 业务类型
		data.put("bizType", "000201");
		// 渠道类型，07-PC，08-手机
		data.put("channelType", "08");
		// 前台通知地址 ，控件接入方式无作用
//		data.put("frontUrl", "");
		// 后台通知地址
		data.put("backUrl", SDKConfig.getConfig().getProperties()
				.getProperty("acpsdk.refundBackUrl"));
		// 接入类型，商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
		data.put("accessType", "0");
		// 商户号码，请改成自己的商户号
		data.put("merId", SDKConfig.getConfig().getProperties()
				.getProperty("acpsdk.merId"));
		//原消费的queryId，可以从查询接口或者通知接口中获取
		data.put("origQryId", queryId);
		// 商户订单号，8-40位数字字母，重新产生，不同于原消费
		data.put("orderId", refundId);
		// 订单发送时间，取系统时间
		data.put("txnTime", txnTime);
		// 交易金额
		data.put("txnAmt", txnAmt.toString());
		// 交易币种
		data.put("currencyCode", "156");
		// 请求方保留域，透传字段，查询、通知、对账文件中均会原样出现
		// data.put("reqReserved", "透传信息");

		data = signData(data);

		// 交易请求url 从配置文件读取
		String url = SDKConfig.getConfig().getBackRequestUrl();
		Map<String, String> resmap = submitUrl(data, url);

		logger.debug("refund ret = {}" ,resmap);
		
		return true;
	}

	/**
	 * 构造HTTP POST交易表单的方法示例
	 * 
	 * @param action
	 *            表单提交地址
	 * @param hiddens
	 *            以MAP形式存储的表单键值
	 * @return 构造好的HTTP POST交易表单
	 */
	public static String createHtml(String action, Map<String, String> hiddens) {
		StringBuffer sf = new StringBuffer();
		sf.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/></head><body>");
		sf.append("<form id = \"pay_form\" action=\"" + action
				+ "\" method=\"post\">");
		if (null != hiddens && 0 != hiddens.size()) {
			Set<Entry<String, String>> set = hiddens.entrySet();
			Iterator<Entry<String, String>> it = set.iterator();
			while (it.hasNext()) {
				Entry<String, String> ey = it.next();
				String key = ey.getKey();
				String value = ey.getValue();
				sf.append("<input type=\"hidden\" name=\"" + key + "\" id=\""
						+ key + "\" value=\"" + value + "\"/>");
			}
		}
		sf.append("</form>");
		sf.append("</body>");
		sf.append("<script type=\"text/javascript\">");
		sf.append("document.all.pay_form.submit();");
		sf.append("</script>");
		sf.append("</html>");
		return sf.toString();
	}

	/**
	 * java main方法 数据提交 　　 对数据进行签名
	 * 
	 * @param contentData
	 * @return　签名后的map对象
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> signData(Map<String, ?> contentData) {
		Entry<String, String> obj = null;
		Map<String, String> submitFromData = new HashMap<String, String>();
		for (Iterator<?> it = contentData.entrySet().iterator(); it.hasNext();) {
			obj = (Entry<String, String>) it.next();
			String value = obj.getValue();
			if (StringUtils.isNotBlank(value)) {
				// 对value值进行去除前后空处理
				submitFromData.put(obj.getKey(), value.trim());
			}
		}
		/**
		 * 签名
		 */
		SDKUtil.sign(submitFromData, encoding);
		return submitFromData;
	}

	public static boolean frontResponseValidation(HttpServletRequest req) {
		Map<String, String> respParam = getAllRequestParam(req);

		Map<String, String> valideData = null;
		if (null != respParam && !respParam.isEmpty()) {
			Iterator<Entry<String, String>> it = respParam.entrySet()
					.iterator();
			valideData = new HashMap<String, String>(respParam.size());
			while (it.hasNext()) {
				Entry<String, String> e = it.next();
				String key = (String) e.getKey();
				String value = (String) e.getValue();
				// value = new String(value.getBytes("ISO-8859-1"), encoding);
				valideData.put(key, value);
			}
		}
		if (!SDKUtil.validate(valideData, encoding)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 获取请求参数中所有的信息
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> getAllRequestParam(
			final HttpServletRequest request) {
		Map<String, String> res = new HashMap<String, String>();
		Enumeration<?> temp = request.getParameterNames();
		if (null != temp) {
			while (temp.hasMoreElements()) {
				String en = (String) temp.nextElement();
				String value = request.getParameter(en);
				res.put(en, value);
				// 在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
				if (res.get(en) == null || "".equals(res.get(en))) {
					// System.out.println("======为空的字段名===="+en);
					res.remove(en);
				}
			}
		}
		return res;
	}

	public static String getAppPayTn(String orderId, Integer price) {
		/**
		 * 组装请求报文
		 */
		Map<String, String> data = new HashMap<String, String>();
		// 版本号
		data.put("version", version);
		// 字符集编码 默认"UTF-8"
		data.put("encoding", encoding);
		// 签名方法 01 RSA
		data.put("signMethod", "01");
		// 交易类型 01-消费
		data.put("txnType", "01");
		// 交易子类型 01:自助消费 02:订购 03:分期付款
		data.put("txnSubType", "01");
		// 业务类型
		data.put("bizType", "000201");
		// 渠道类型，07-PC，08-手机
		data.put("channelType", "08");
		// 前台通知地址 ，app支付没有用
		data.put("frontUrl", "");
		// 后台通知地址
		data.put(
				"backUrl",
				SDKConfig.getConfig().getProperties()
						.getProperty("acpsdk.backUrl"));
		logger.debug(data.get("backUrl"));
		// 接入类型，商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
		data.put("accessType", "0");
		// 商户号码，请改成自己的商户号
		data.put(
				"merId",
				SDKConfig.getConfig().getProperties()
						.getProperty("acpsdk.merId"));
		// 商户订单号，8-40位数字字母
		data.put("orderId", orderId);
		// 订单发送时间，取系统时间
		data.put("txnTime",
				new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		// 交易金额，单位分
		data.put("txnAmt", price.toString());
		// 交易币种
		data.put("currencyCode", "156");
		// 请求方保留域，透传字段，查询、通知、对账文件中均会原样出现
		// data.put("reqReserved", "透传信息");
		// 订单描述，可不上送，上送时控件中会显示该信息
		// data.put("orderDesc", "订单描述");

		data = signData(data);
		// 交易请求url 从配置文件读取
		String requestAppUrl = SDKConfig.getConfig().getAppRequestUrl();
		Map<String, String> resmap = submitUrl(data, requestAppUrl);

		return resmap.get("tn");
	}

	private static Map<String, String> submitUrl(
			Map<String, String> submitFromData, String requestUrl) {
		String resultString = "";
		logger.debug("requestUrl====" + requestUrl);
		logger.debug("submitFromData====" + submitFromData.toString());
		/**
		 * 发送
		 */
		HttpClient hc = new HttpClient(requestUrl, 30000, 30000);
		try {
			int status = hc.send(submitFromData, encoding);
			if (200 == status) {
				resultString = hc.getResult();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, String> resData = new HashMap<String, String>();
		/**
		 * 验证签名
		 */
		if (null != resultString && !"".equals(resultString)) {
			// 将返回结果转换为map
			resData = SDKUtil.convertResultStringToMap(resultString);
			if (SDKUtil.validate(resData, encoding)) {
				logger.debug("验证签名成功");
			} else {
				logger.debug("验证签名失败");
			}
			// 打印返回报文
			logger.debug("打印返回报文：" + resultString);
		}
		return resData;
	}

	public static Map<String, String> back(HttpServletRequest req)
			throws UnsupportedEncodingException {
		req.setCharacterEncoding("ISO-8859-1");
		String encoding = req.getParameter(SDKConstants.param_encoding);
		// 获取请求参数中所有的信息
		Map<String, String> reqParam = getAllRequestParam(req);

		Map<String, String> valideData = null;
		if (null != reqParam && !reqParam.isEmpty()) {
			Iterator<Entry<String, String>> it = reqParam.entrySet().iterator();
			valideData = new HashMap<String, String>(reqParam.size());
			while (it.hasNext()) {
				Entry<String, String> e = it.next();
				String key = (String) e.getKey();
				String value = (String) e.getValue();
				value = new String(value.getBytes("ISO-8859-1"), encoding);
				valideData.put(key, value);
			}
		}

		// 验证签名
		if (!SDKUtil.validate(valideData, encoding)) {
			logger.error("验证签名结果[失败].");
		} else {
			logger.debug("验证签名结果[成功].");
		}
		logger.debug("BackRcvResponse接收后台通知结束");

		return valideData;
	}

	public static Map<String, String> query(String orderId, String txnTime) {
		/**
		 * 组装请求报文
		 */
		Map<String, String> data = new HashMap<String, String>();
		// 版本号
		data.put("version", version);
		// 字符集编码 默认"UTF-8"
		data.put("encoding", encoding);
		// 签名方法 01 RSA
		data.put("signMethod", "01");
		// 交易类型
		data.put("txnType", "00");
		// 交易子类型
		data.put("txnSubType", "00");
		// 业务类型
		data.put("bizType", "000000");
		// 渠道类型，07-PC，08-手机
		data.put("channelType", "08");
		// 接入类型，商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
		data.put("accessType", "0");
		// 商户号码，请改成自己的商户号
		data.put(
				"merId",
				SDKConfig.getConfig().getProperties()
						.getProperty("acpsdk.merId"));
		// 商户订单号，请修改被查询的交易的订单号
		data.put("orderId", orderId);
		// 订单发送时间，请修改被查询的交易的订单发送时间
		data.put("txnTime", txnTime);

		data = signData(data);

		// 交易请求url 从配置文件读取
		String url = SDKConfig.getConfig().getSingleQueryUrl();

		Map<String, String> resmap = submitUrl(data, url);

		logger.debug("请求报文=[" + data.toString() + "]");
		logger.debug("应答报文=[" + resmap.toString() + "]");

		return resmap;
	}

}
