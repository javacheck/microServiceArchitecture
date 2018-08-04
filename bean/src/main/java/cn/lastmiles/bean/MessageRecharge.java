/**
 * createDate : 2016年5月11日上午10:54:50
 */
package cn.lastmiles.bean;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="t_message_recharge")
public class MessageRecharge {
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
	private Date updateTime; // 充值时间
	
	@Column
	private Integer remainNumber = 0 ; //  剩余短信数量
	
	@Column 
	private Integer beforeRemainNumber = 0 ; // 上一次的剩余短信数量
	
	private String storeName; // 商家名称

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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getRemainNumber() {
		return remainNumber;
	}

	public void setRemainNumber(Integer remainNumber) {
		this.remainNumber = remainNumber;
	}

	public Integer getBeforeRemainNumber() {
		return beforeRemainNumber;
	}

	public void setBeforeRemainNumber(Integer beforeRemainNumber) {
		this.beforeRemainNumber = beforeRemainNumber;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	@Override
	public String toString() {
		return "MessageRecharge [id=" + id + ", storeId=" + storeId
				+ ", accountId=" + accountId + ", price=" + price + ", number="
				+ number + ", updateTime=" + updateTime + ", remainNumber="
				+ remainNumber + ", beforeRemainNumber=" + beforeRemainNumber
				+ ", storeName=" + storeName + "]";
	}
	
	
	
}