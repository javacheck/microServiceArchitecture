/**
 * createDate : 2016年5月11日上午10:54:50
 */
package cn.lastmiles.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="t_message_recharge_record")
public class MessageRechargeRecord {
	@Id
	@Column
	private Long id; // 记录
	
	@Column
	private Long storeId; //关联的商家ID
	
	@Column
	private Long accountId; //操作人员
	
	@Column
	private Double price = 0D; // 充值金额
	
	@Column
	private Integer number = 0 ; // 充值数量
	
	@Column
	private Date createdTime; // 充值时间
	
	@Column
	private Integer remainNumber = 0 ; //  剩余短信数量

	private String storeName; // 充值商家
	
	private String operationName; // 操作人员

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
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

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getRemainNumber() {
		return remainNumber;
	}

	public void setRemainNumber(Integer remainNumber) {
		this.remainNumber = remainNumber;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	@Override
	public String toString() {
		return "MessageRechargeRecord [id=" + id + ", storeId=" + storeId
				+ ", accountId=" + accountId + ", price=" + price + ", number="
				+ number + ", createdTime=" + createdTime + ", remainNumber="
				+ remainNumber + ", storeName=" + storeName
				+ ", operationName=" + operationName + "]";
	}
	

}