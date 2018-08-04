/**
 * createDate : 2016年7月27日下午4:45:04
 */
package cn.lastmiles.alipay.bean;

public class SubMerchant {
	/**
	 * <必填>
	 * 最大长度为11位
	 * 二级商户的支付宝id
	 */
	private String merchantId;
	
	public SubMerchant() {
		super();
	}

	public SubMerchant(String merchantId) {
		super();
		this.merchantId = merchantId;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	@Override
	public String toString() {
		return "SubMerchant [merchantId=" + merchantId + "]";
	}
	
}