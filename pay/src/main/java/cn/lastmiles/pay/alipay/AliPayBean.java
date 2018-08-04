package cn.lastmiles.pay.alipay;


public class AliPayBean {
	/**
	 * out_trade  订单ID
	 */
	private String orderId;
	/**
	 * notify_time 通知时间
	 */
	private String notifyTime;
	/**
	 * notify_type 通知类型
	 */
	private String notifyType;
	/**
	 * sign_type 通知ID
	 */
	private String notifyId;
	/**
	 * sign_type 加密类型
	 */
	private String signType;
	/**
	 * sign 签名
	 */
	private String sign;
	/**
	 * trade_no 支付宝交易号
	 */
	private String tradeNo;
	/**
	 * trade_status  交易状态
	 */
	private String tradeStatus;
	/**
	 * total_fee 交易金额
	 */
	private Integer totalFee;
	/**
	 * gmt_create 交易创建时间
	 */
	private String gmtCreate;
	/**
	 * gmt_payment 交易付款时间
	 */
	private String gmtPayment;
	/**
	 * refund_status 退款状态
	 */
	private String refundStatus;
	/**
	 * gmt_refund  退款时间
	 */
	private String gmtRefund;
	
	
	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public String getNotifyTime() {
		return notifyTime;
	}


	public void setNotifyTime(String notifyTime) {
		this.notifyTime = notifyTime;
	}


	public String getNotifyType() {
		return notifyType;
	}


	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}


	public String getNotifyId() {
		return notifyId;
	}


	public void setNotifyId(String notifyId) {
		this.notifyId = notifyId;
	}


	public String getSignType() {
		return signType;
	}


	public void setSignType(String signType) {
		this.signType = signType;
	}


	public String getSign() {
		return sign;
	}


	public void setSign(String sign) {
		this.sign = sign;
	}


	public String getTradeNo() {
		return tradeNo;
	}


	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}


	public String getTradeStatus() {
		return tradeStatus;
	}


	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}


	public Integer getTotalFee() {
		return totalFee;
	}


	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
	}


	public String getGmtCreate() {
		return gmtCreate;
	}


	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}


	public String getGmtPayment() {
		return gmtPayment;
	}


	public void setGmtPayment(String gmtPayment) {
		this.gmtPayment = gmtPayment;
	}


	public String getRefundStatus() {
		return refundStatus;
	}


	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}


	public String getGmtRefund() {
		return gmtRefund;
	}


	public void setGmtRefund(String gmtRefund) {
		this.gmtRefund = gmtRefund;
	}


	@Override
	public String toString() {
		return "AliPayBean [orderId=" + orderId + ", notifyTime=" + notifyTime
				+ ", notifyType=" + notifyType + ", notifyId=" + notifyId
				+ ", signType=" + signType + ", sign=" + sign + ", tradeNo="
				+ tradeNo + ", tradeStatus=" + tradeStatus + ", totalFee="
				+ totalFee + ", gmtCreate=" + gmtCreate + ", gmtPayment="
				+ gmtPayment + ", refundStatus=" + refundStatus
				+ ", gmtRefund=" + gmtRefund + "]";
	}
	
	
}
