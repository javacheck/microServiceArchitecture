package cn.lastmiles.bean;
/**
 * createDate : 2015-07-14 PM 15:32
 */
import java.util.Date;

import cn.lastmiles.constant.Constants;

/**
 * 流水账 
 */
public class SettlementsRecord {
	private Long id;
	private Long orderId;
	private Long ownerId;
	private Integer type;
	private Double amount;
	private Date createdTime;
	private Double balance;
	private Integer status = Constants.SettlementsRecord.STATUS_NOT_SETTLED;
	private String nameS;
	
	private Store store;
	private Agent agent;
	private BusinessBank businessBank;
	private String amountString;
	
	public SettlementsRecord() {
		super();
	}
	
	public BusinessBank getBusinessBank() {
		return businessBank;
	}

	public void setBusinessBank(BusinessBank businessBank) {
		this.businessBank = businessBank;
	}

	public SettlementsRecord(Long orderId,Long ownerId,Integer type,Double amount) {
		super();
		this.orderId = orderId;
		this.ownerId = ownerId;
		this.type = type;
		this.amount = amount;
	}

	public SettlementsRecord(Long id, Long orderId, Long ownerId, Integer type,
			Double amount, Date createdTime, Double balance, Store store,
			Agent agent) {
		super();
		this.id = id;
		this.orderId = orderId;
		this.ownerId = ownerId;
		this.type = type;
		this.amount = amount;
		this.createdTime = createdTime;
		this.balance = balance;
		this.store = store;
		this.agent = agent;
	}



	@Override
	public String toString() {
		return "SettlementsRecord [id=" + id + ", orderId=" + orderId
				+ ", ownerId=" + ownerId + ", type=" + type + ", amount="
				+ amount + ", createdTime=" + createdTime + ", balance="
				+ balance + ", status=" + status + ", nameS=" + nameS
				+ ", store=" + store + ", agent=" + agent + ", amountString="
				+ amountString + "]";
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

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}