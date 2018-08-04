package cn.lastmiles.pay.pos.wxpay.protocol;

import java.util.HashMap;
import java.util.Map;

public class BaseReqData {
	/**
	 * 微信分配的公众号ID（开通公众号之后可以获取到）
	 */
	public String appid = "";
	/**
	 * 微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
	 */
	public String mch_id = "";
	/**
	 * 根据API给的签名规则进行签名
	 */
	public String sign = "";

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Map<String, Object> toMap() {
		System.out.println("===========>>>");
		return new HashMap<String, Object>();
	}

}
