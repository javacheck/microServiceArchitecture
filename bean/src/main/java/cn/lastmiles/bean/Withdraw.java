package cn.lastmiles.bean;

import java.util.Date;

/**
 * createDate : 2015-07-13 PM 16:52
 * table: t_withdraw(提现记录)
 */
public class Withdraw {

	private Long id;
	private Date createdTime;
	private Integer type; // 0商户，1代理商,2用户
	private Long ownerId; // 商户id，或者代理商id，用户id
	private Double amount; // 金额
	private Long accountId; // 代理商或者商家的accountId，如果是用户null值
	private String bankName; // 银行名称
	private String bankAccountNumber; // 银行帐号
	private String bankAccountName; // 帐号名称
	private Integer status; // 状态 0 处理中 1 成功 2 失败 3 审核通过 4 审核失败
	
	// 非数据库字段(引用商家表和代理商表)
	private String nameS;
	
	private String amountString ;
	
	@Override
	public String toString() {
		return "Withdraw [id=" + id + ", createdTime=" + createdTime
				+ ", type=" + type + ", ownerId=" + ownerId + ", amount="
				+ amount + ", accountId=" + accountId + ", bankName="
				+ bankName + ", bankAccountNumber=" + bankAccountNumber
				+ ", bankAccountName=" + bankAccountName + ", status=" + status
				+ ", nameS=" + nameS + ", amountString=" + amountString + "]";
	}
	
	public String getAmountString() {
		return amountString;
	}
	public void setAmountString(String amountString) {
		this.amountString = amountString;
	}
	public String getNameS() {
		return nameS;
	}
	public void setNameS(String nameS) {
		this.nameS = nameS;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankAccountNumber() {
		return bankAccountNumber;
	}
	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}
	public String getBankAccountName() {
		return bankAccountName;
	}
	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}