/**
 * createDate : 2016年7月27日下午4:44:30
 */
package cn.lastmiles.alipay.bean;

import com.google.gson.annotations.SerializedName;

public class ExtendParams {
	/**
	 * <可选>
	 * 最大长度为64位
	 * 系统商编号 该参数作为系统商返佣数据提取的依据，请填写系统商签约协议的PID
	 */
    @SerializedName("sys_service_provider_id")
	private String sysServiceProviderId;
	/**
	 * <可选>
	 * 最大长度为5位
	 * 使用花呗分期要进行的分期数
	 */
	private String hbFqNum;
	/**
	 * <可选>
	 * 最大长度为3位
	 * 使用花呗分期需要卖家承担的手续费比例的百分值，传入100代表100%
	 */
	private String hbFqSellerPercent;
	
	public String getSysServiceProviderId() {
		return sysServiceProviderId;
	}
	
	public void setSysServiceProviderId(String sysServiceProviderId) {
		this.sysServiceProviderId = sysServiceProviderId;
	}
	
	public String getHbFqNum() {
		return hbFqNum;
	}
	
	public void setHbFqNum(String hbFqNum) {
		this.hbFqNum = hbFqNum;
	}
	
	public String getHbFqSellerPercent() {
		return hbFqSellerPercent;
	}
	
	public void setHbFqSellerPercent(String hbFqSellerPercent) {
		this.hbFqSellerPercent = hbFqSellerPercent;
	}

	@Override
	public String toString() {
		return "ExtendParams [sysServiceProviderId=" + sysServiceProviderId
				+ ", hbFqNum=" + hbFqNum + ", hbFqSellerPercent="
				+ hbFqSellerPercent + "]";
	}
	
}