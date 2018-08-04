package cn.lastmiles.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 商家短信数量表
 * @author hql
 *
 */
@Table(name = "t_store_message")
public class StoreMessage {
	@Id
	@Column(name = "storeId")
	private Long storeId;

	@Column(name = "amount")
	private Integer amount;// 剩余短信数量

	@Column(name = "lastRechargeAmount")
	private Integer lastRechargeAmount;// 最新充值数量

	@Column(name = "lastRechargeDate")
	private Date lastRechargeDate;// 最新充值时间

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getLastRechargeAmount() {
		return lastRechargeAmount;
	}

	public void setLastRechargeAmount(Integer lastRechargeAmount) {
		this.lastRechargeAmount = lastRechargeAmount;
	}

	public Date getLastRechargeDate() {
		return lastRechargeDate;
	}

	public void setLastRechargeDate(Date lastRechargeDate) {
		this.lastRechargeDate = lastRechargeDate;
	}
}
