package cn.lastmiles.bean;

import java.util.Date;

public class BusinessBank {
	private Long id;//帐号ID
	private Long businessId;//商户id，代理商id
	private Integer type;//0表示商家，1表示代理商
	private String bankName;//银行名称
	private String accountNumber;//卡号
	private String accountName;//持卡人姓名
	private String mobile;//持卡人手机
	private Integer isDefault;//1是默认帐号，0表示不是
	private Date createdTime;//创建时间
	private String subbranch;//支行
	private Long bankId;//银行ID
	private Integer accountType; // 账户类型
	private String iconUrl; // 银行图标
	
	//---2016-01-13add-start
	private String storeName;
	//---2016-01-13add-end

	public String getIconUrl() {
		return iconUrl;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public Integer getAccountType() {
		return accountType;
	}
	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getBusinessId() {
		return businessId;
	}
	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Integer getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public String getSubbranch() {
		return subbranch;
	}
	public void setSubbranch(String subbranch) {
		this.subbranch = subbranch;
	}
	public Long getBankId() {
		return bankId;
	}
	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}
	@Override
	public String toString() {
		return "BusinessBank [id=" + id + ", businessId=" + businessId
				+ ", type=" + type + ", bankName=" + bankName
				+ ", accountNumber=" + accountNumber + ", accountName="
				+ accountName + ", mobile=" + mobile + ", isDefault="
				+ isDefault + ", createdTime=" + createdTime + ", subbranch="
				+ subbranch + ", bankId=" + bankId + ", accountType="
				+ accountType + ", iconUrl=" + iconUrl + ", storeName="
				+ storeName + "]";
	}
	
}