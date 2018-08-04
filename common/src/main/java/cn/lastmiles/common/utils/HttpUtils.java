package cn.lastmiles.common.utils;

import java.util.HashMap;
import java.util.Map;

import jodd.http.HttpRequest;

public final class HttpUtils {
	public static String post(String url, Map<String, Object> parameters) {
		return HttpRequest.post(url).form(parameters).send().bodyText();
	}
	/**
	 * 判断是否请求是否来源支付宝
	 * @param url
	 * @param parameters
	 * @return
	 */
	public static boolean isAlipayRequest(String partner,String notifyId) {
		String url = "https://mapi.alipay.com/gateway.do";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("service", "notify_verify");
			parameters.put("partner", partner);
			parameters.put("notify_id", notifyId);
			String bodyText= HttpRequest.post(url).form(parameters).send().bodyText();
			if (bodyText.equals("true")) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	public static void main(String[] args) {
		isAlipayRequest("","");
	}
}
