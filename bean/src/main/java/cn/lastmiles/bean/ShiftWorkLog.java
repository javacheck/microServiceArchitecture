package cn.lastmiles.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * 交接记录
 * @author zhangpengcheng
 *
 */
public class ShiftWorkLog implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6750794737053952217L;
	
	private Long id;//ID
	
	private String mobile;//用户手机
	
	private Long accountId;//账号Id
	
	private String accountName;//账号名称
	
	private Date beginTime;//开始时间
	
	private Date endTime;//结束时间
	
	private Long  storeId;//商店ID
	
	private List<PaymentModeInfo> paymentModeInfos;//支付方式详情

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public List<PaymentModeInfo> getPaymentModeInfos() {
		return paymentModeInfos;
	}

	public void setPaymentModeInfos(List<PaymentModeInfo> paymentModeInfos) {
		this.paymentModeInfos = paymentModeInfos;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	
	
	
}
