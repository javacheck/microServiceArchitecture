package cn.lastmiles.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name="t_store_message_recharge_record")
public class StoreMessageRechargeRecord {
	@Column(name="createdTime")
	private Date createdTime;
	
	@Column(name="storeId")
	private Long storeId;
	
	@Column(name="price")
	private Double price;//充值金额
	
	@Column(name="number")
	private Integer number;//充值数量
	
	@Column(name="amount")
	private Integer amount;//剩余数量 和 storemessage中的amount一致
	
	@Column(name="accountId")
	private Long accountId;

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
}
