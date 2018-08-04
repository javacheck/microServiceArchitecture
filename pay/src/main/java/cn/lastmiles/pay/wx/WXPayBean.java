package cn.lastmiles.pay.wx;

public class WXPayBean {
	public static final String PACKAGEVALUE = "Sign=WXPay";
	
	private String orderId;
	private String partnerId;
	private String prepayId;
	private String packageValue = PACKAGEVALUE;
	private String nonceStr;
	private String timeStamp;
	private String sign;
	private String paymentMode;
	//需要支付金额
	private String price;
	
	public WXPayBean(){super();};

	public WXPayBean(String partnerId, String prepayId, String packageValue,
			String nonceStr, String timeStamp, String sign) {
		super();
		this.partnerId = partnerId;
		this.prepayId = prepayId;
		this.packageValue = packageValue;
		this.nonceStr = nonceStr;
		this.timeStamp = timeStamp;
		this.sign = sign;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

	public String getPackageValue() {
		return packageValue;
	}

	public void setPackageValue(String packageValue) {
		this.packageValue = packageValue;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
