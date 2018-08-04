/**
 * createDate : 2016年7月27日下午4:32:21
 */
package cn.lastmiles.alipay.bean;

import java.util.List;

public class Pay {
	/**
	 * <必填>
	 * 最大长度64位
	 * 商户订单号,64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复
	 */
	private String outTradeNo;
	/**
	 * <必填>
	 * 最大长度32位
	 * 支付场景 条码支付，取值：bar_code 声波支付，取值：wave_code
	 */
	private String scene = "bar_code";
	/**
	 * <必填>
	 * 最大长度32位
	 * 支付授权码
	 */
	private String authCode;
	/**
	 * <可选>
	 * 最大长度28位
	 * 如果该值为空，则默认为商户签约账号对应的支付宝用户ID
	 */
	private String sellerId;
	/**
	 * <可选>
	 * 最大长度11位
	 * 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]。 如果同时传入【可打折金额】和【不可打折金额】，该参数可以不用传入； 
	 * 如果同时传入了【可打折金额】，【不可打折金额】，【订单总金额】三者，则必须满足如下条件：【订单总金额】=【可打折金额】+【不可打折金额】
	 */
	private Double totalAmount;
	/**
	 * <可选>
	 * 最大长度11位
	 * 参与优惠计算的金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]。 
	 * 如果该值未传入，但传入了【订单总金额】和【不可打折金额】，则该值默认为【订单总金额】-【不可打折金额】
	 */
	private Double discountableAmount;
	/**
	 * <可选>
	 * 最大长度11位
	 * 不参与优惠计算的金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]。
	 * 如果该值未传入，但传入了【订单总金额】和【可打折金额】，则该值默认为【订单总金额】-【可打折金额】
	 */
	private Double undiscountableAmount;
	/**
	 * <必填>
	 * 最大长度256位 
	 * 订单标题
	 */
	private String subject;
	/**
	 * <可选>
	 * 最大长度128位
	 * 订单描述
	 */
	private String body;
	/**
	 * <可选>
	 * 订单包含的商品列表信息，Json格式，其它说明详见商品明细说明
	 */
	private List<GoodsDetail> goodsDetail;
	/**
	 * <可选>
	 * 最大长度28位
	 * 商户操作员编号
	 */
	private String operatorId;
	/**
	 * <可选>
	 * 最大长度32位
	 * 商户门店编号
	 */
	private String storeId;
	/**
	 * <可选>
	 * 最大长度32位
	 * 商户机具终端编号
	 */
	private String terminalId;
	/**
	 * <可选>
	 * 最大长度32位
	 * 支付宝的店铺编号
	 */
	private String alipayStoreId;
	/**
	 * 业务扩展参数
	 */
	private ExtendParams extendParams;
	/**
	 * <可选>
	 * 最大长度6位
	 * 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 
	 * 该参数数值不接受小数点， 如 1.5h，可转换为 90m
	 */
	private String timeoutExpress;
	/**
	 * 描述分账信息，Json格式，其它说明详见分账说明
	 */
	private RoyaltyInfo royaltyInfo;
	/**
	 * 二级商户信息,当前只对特殊银行机构特定场景下使用此字段
	 */
	private SubMerchant subMerchant;
	
	public Pay() {
		super();
	}
	
	public Pay(String outTradeNo, String authCode, Double totalAmount,String subject, String storeId) {
		super();
		this.outTradeNo = outTradeNo;
		this.authCode = authCode;
		this.totalAmount = totalAmount;
		this.subject = subject;
		this.storeId = storeId;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}
	
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	
	public String getScene() {
		return scene;
	}
	
	public void setScene(String scene) {
		this.scene = scene;
	}
	
	public String getAuthCode() {
		return authCode;
	}
	
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	
	public String getSellerId() {
		return sellerId;
	}
	
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	
	public Double getTotalAmount() {
		return totalAmount;
	}
	
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public Double getDiscountableAmount() {
		return discountableAmount;
	}
	
	public void setDiscountableAmount(Double discountableAmount) {
		this.discountableAmount = discountableAmount;
	}
	
	public Double getUndiscountableAmount() {
		return undiscountableAmount;
	}
	
	public void setUndiscountableAmount(Double undiscountableAmount) {
		this.undiscountableAmount = undiscountableAmount;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public List<GoodsDetail> getGoodsDetail() {
		return goodsDetail;
	}
	
	public void setGoodsDetail(List<GoodsDetail> goodsDetail) {
		this.goodsDetail = goodsDetail;
	}
	
	public String getOperatorId() {
		return operatorId;
	}
	
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	
	public String getStoreId() {
		return storeId;
	}
	
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	
	public String getTerminalId() {
		return terminalId;
	}
	
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	
	public String getAlipayStoreId() {
		return alipayStoreId;
	}
	
	public void setAlipayStoreId(String alipayStoreId) {
		this.alipayStoreId = alipayStoreId;
	}
	
	public ExtendParams getExtendParams() {
		return extendParams;
	}
	
	public void setExtendParams(ExtendParams extendParams) {
		this.extendParams = extendParams;
	}
	
	public String getTimeoutExpress() {
		return timeoutExpress;
	}
	
	public void setTimeoutExpress(String timeoutExpress) {
		this.timeoutExpress = timeoutExpress;
	}
	
	public RoyaltyInfo getRoyaltyInfo() {
		return royaltyInfo;
	}
	
	public void setRoyaltyInfo(RoyaltyInfo royaltyInfo) {
		this.royaltyInfo = royaltyInfo;
	}
	
	public SubMerchant getSubMerchant() {
		return subMerchant;
	}
	
	public void setSubMerchant(SubMerchant subMerchant) {
		this.subMerchant = subMerchant;
	}

	@Override
	public String toString() {
		return "Pay [outTradeNo=" + outTradeNo + ", scene=" + scene
				+ ", authCode=" + authCode + ", sellerId=" + sellerId
				+ ", totalAmount=" + totalAmount + ", discountableAmount="
				+ discountableAmount + ", undiscountableAmount="
				+ undiscountableAmount + ", subject=" + subject + ", body="
				+ body + ", goodsDetail=" + goodsDetail + ", operatorId="
				+ operatorId + ", storeId=" + storeId + ", terminalId="
				+ terminalId + ", alipayStoreId=" + alipayStoreId
				+ ", extendParams=" + extendParams + ", timeoutExpress="
				+ timeoutExpress + ", royaltyInfo=" + royaltyInfo
				+ ", subMerchant=" + subMerchant + "]";
	}
	
}