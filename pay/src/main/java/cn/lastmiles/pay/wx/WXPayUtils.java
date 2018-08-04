package cn.lastmiles.pay.wx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jodd.lagarto.dom.Document;
import jodd.lagarto.dom.Element;
import jodd.lagarto.dom.LagartoDOMBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tencent.WXPay;
import com.tencent.business.RefundBusiness;
import com.tencent.common.Configure;
import com.tencent.protocol.refund_protocol.RefundReqData;
import com.tencent.protocol.refund_protocol.RefundResData;

import cn.lastmiles.common.utils.ConfigUtils;
import cn.lastmiles.common.utils.StringUtils;

public abstract class WXPayUtils {
	private final static Logger logger = LoggerFactory
			.getLogger(WXPayUtils.class);
	
	/**
	 * 刷卡支付
	 */
	public static void micropay(){
		
	}
	

	/**
	 * 退款
	 * 
	 * @param orderId
	 * @param totalPrice
	 *            总金额
	 * @param refundPrice
	 *            退款金额 一般同总金额一样
	 * @return
	 */
	public static boolean refund(String refundId, String orderId,
			Integer totalPrice, Integer refundPrice) {
		final RefundRet ret = new RefundRet();
		if (StringUtils.isNotBlank(ConfigUtils.getProperty("wx_appid"))) {
			Configure.setAppID(ConfigUtils.getProperty("wx_appid"));
		}
		if (StringUtils.isNotBlank(ConfigUtils.getProperty("wx_mch_id"))) {
			Configure.setMchID(ConfigUtils.getProperty("wx_mch_id"));
		}
		if (StringUtils.isNotBlank(ConfigUtils.getProperty("wx_key"))) {
			Configure.setKey(ConfigUtils.getProperty("wx_key"));
		}
		if (StringUtils.isNotBlank(ConfigUtils.getProperty("wx_certLocalPath"))) {
			Configure.setCertLocalPath(ConfigUtils
					.getProperty("wx_certLocalPath"));
		}

		RefundReqData refundReqData = new RefundReqData(null, orderId, null,
				refundId, totalPrice, refundPrice, Configure.getMchid(), "CNY");
		try {
			WXPay.doRefundBusiness(refundReqData,
					new RefundBusiness.ResultListener() {
						@Override
						public void onRefundSuccess(RefundResData refundResData) {
							logger.debug("onRefundSuccess="
									+ refundResData);
							ret.retsult = true;
						}

						@Override
						public void onRefundFail(RefundResData refundResData) {
							logger.warn(
									"orderId = {},refundId = {},onRefundFail= {}",
									orderId, refundId, refundResData);
						}

						@Override
						public void onFailBySignInvalid(
								RefundResData refundResData) {
							logger.warn(
									"orderId = {},refundId = {},onFailBySignInvalid= {}",
									orderId, refundId, refundResData);
						}

						@Override
						public void onFailByReturnCodeFail(
								RefundResData refundResData) {
							logger.warn(
									"orderId = {},refundId = {},onFailByReturnCodeFail= {}",
									orderId, refundId, refundResData);
						}

						@Override
						public void onFailByReturnCodeError(
								RefundResData refundResData) {
							logger.warn(
									"orderId = {},refundId = {},onFailByReturnCodeError= {}",
									orderId, refundId, refundResData);
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret.retsult;
	}

	/**
	 * 
	 * @param orderId
	 * @param price
	 * @param desc
	 *            商品名称
	 * @return
	 */
	public static WXPayBean getAppPayTn(String orderId, Integer price,
			String desc, String ip) {
		Map<String, String> forms = new HashMap<String, String>();
		forms.put("appid", ConfigUtils.getProperty("wx_appid"));
		forms.put("mch_id", ConfigUtils.getProperty("wx_mch_id"));
		forms.put("nonce_str", StringUtils.getRandomStr(32));
		forms.put("body", desc);
		forms.put("out_trade_no", orderId);
		forms.put("total_fee", price.toString());
		forms.put("spbill_create_ip", ip);
		forms.put("notify_url", ConfigUtils.getProperty("wx_notify_url"));
		forms.put("trade_type", "APP");

		String formStr = getSignString(forms);
		String sign = StringUtils.md5(formStr).toUpperCase();
		String xml = getXmlString(forms, sign);

		try {
			URL url = new URL(ConfigUtils.getProperty("wx_unifiedorder"));
			URLConnection con = url.openConnection();
			con.setDoOutput(true);
			con.setRequestProperty("Pragma:", "no-cache");
			con.setRequestProperty("Cache-Control", "no-cache");
			con.setRequestProperty("Content-Type", "text/xml");

			OutputStreamWriter out = new OutputStreamWriter(
					con.getOutputStream());
			out.write(new String(xml.getBytes("utf-8")));
			out.flush();
			out.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String line = "";
			StringBuilder ret = new StringBuilder();
			for (line = br.readLine(); line != null; line = br.readLine()) {
				ret.append(line);
			}

			logger.debug("ret xml = {} ", ret);
			LagartoDOMBuilder domBuilder = new LagartoDOMBuilder();
			domBuilder.enableXmlMode();
			Document doc = domBuilder.parse(ret.toString());
			Element root = doc.getChildElement(0);
			Element returnCode = root.getFirstChildElement("return_code");
			Element resultCode = root.getFirstChildElement("result_code");

			String success = "SUCCESS";
			if (StringUtils.equals(returnCode.getTextContent(), success)
					&& StringUtils.equals(resultCode.getTextContent(), success)) {
				String prepayId = root.getFirstChildElement("prepay_id")
						.getTextContent();

				Map<String, String> map = new HashMap<String, String>();

				map.put("appid", ConfigUtils.getProperty("wx_appid"));
				map.put("partnerid", ConfigUtils.getProperty("wx_mch_id"));
				map.put("prepayid", prepayId);
				map.put("package", WXPayBean.PACKAGEVALUE);
				map.put("noncestr", StringUtils.getRandomStr(32));
				map.put("timestamp", (System.currentTimeMillis() / 1000) + "");

				String _sign = StringUtils.md5(getSignString(map))
						.toUpperCase();

				return new WXPayBean(map.get("partnerid"), map.get("prepayid"),
						WXPayBean.PACKAGEVALUE, map.get("noncestr"),
						map.get("timestamp"), _sign);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void main(String[] args) throws Exception {
	}

	private static String getXmlString(Map<String, String> forms, String sign) {
		Set<String> keys = forms.keySet();
		List<String> list = new ArrayList<String>();

		for (String key : keys) {
			list.add(key);
		}
		Collections.sort(list);
		StringBuilder sb = new StringBuilder("<xml>");
		for (String str : list) {
			sb.append("<" + str + "><![CDATA[" + forms.get(str) + "]]></" + str
					+ ">");
		}
		sb.append("<sign><![CDATA[" + sign + "]]></sign>");
		sb.append("</xml>");
		return sb.toString();
	}

	public static String getSignString(Map<String, String> forms) {
		Set<String> keys = forms.keySet();
		List<String> list = new ArrayList<String>();

		for (String key : keys) {
			list.add(key + "=" + forms.get(key));
		}
		Collections.sort(list);
		StringBuilder sb = new StringBuilder();
		for (String str : list) {
			if (sb.length() == 0) {
				sb.append(str);
			} else {
				sb.append("&" + str);
			}
		}

		return sb.toString() + "&key=" + ConfigUtils.getProperty("wx_key");
	}
	
	private static class RefundRet{
		boolean retsult = false;
	}
}
