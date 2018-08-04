package cn.lastmiles.bean;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="t_product_delivery_record")
public class ProductDeliveryRecord {
	@Id
	@Column
	private Long id;//出库ID
	@Column
	private String deliveryNumber;//出库单号：规则为 CK+时间  RK代表（入库）如CK201605151730
	@Column
	private Long accountId;//操作人ID
	private String mobile;//操作人手机号
	@Column
	private Date deliveryTime;//出库时间
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
	
	public String getDeliveryNumber() {
		return deliveryNumber;
	}
	public void setDeliveryNumber(String deliveryNumber) {
		this.deliveryNumber = deliveryNumber;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Date getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
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
		return "ProductDeliveryRecord [id=" + id + ", deliveryNumber="
				+ deliveryNumber + ", accountId=" + accountId + ", mobile="
				+ mobile + ", deliveryTime=" + deliveryTime + ", createdTime="
				+ createdTime + ", memo=" + memo + ", storeId=" + storeId
				+ ", storeName=" + storeName + "]";
	}
	
	
}
