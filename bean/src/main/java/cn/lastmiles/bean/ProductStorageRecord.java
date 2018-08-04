package cn.lastmiles.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="t_product_storage_record")
public class ProductStorageRecord {
	@Id
	@Column
	private Long id;//入库单号
	@Column
	private String storageNumber;//入库单号：规则为 RK+时间  RK代表（入库）如RK201605151730
	@Column
	private Long accountId;//操作人ID
	private String mobile;//操作人手机号
	@Column
	private Date storageTime;//入库时间
	@Column
	private Date createdTime;//创建时间
	@Column
	private String memo;//备注
	@Column
	private Long storeId;//商店ID
	private String storeName;//商店名称
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public Date getStorageTime() {
		return storageTime;
	}
	public void setStorageTime(Date storageTime) {
		this.storageTime = storageTime;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getStorageNumber() {
		return storageNumber;
	}
	public void setStorageNumber(String storageNumber) {
		this.storageNumber = storageNumber;
	}
	public Long getStoreId() {
		return storeId;
	}
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	@Override
	public String toString() {
		return "ProductStorageRecord [id=" + id + ", storageNumber="
				+ storageNumber + ", accountId=" + accountId + ", mobile="
				+ mobile + ", storageTime=" + storageTime + ", createdTime="
				+ createdTime + ", memo=" + memo + ", storeId=" + storeId
				+ ", storeName=" + storeName + "]";
	}
	
	
}
