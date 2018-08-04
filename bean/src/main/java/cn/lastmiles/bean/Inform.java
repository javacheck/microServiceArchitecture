/**
 * createDate : 2016年5月20日上午10:56:39
 */
package cn.lastmiles.bean;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="t_inform")
public class Inform {
	@Id
	@Column
	private Long id; // 通知ID
	
	@Column
	private String name; // 通知名称
	
	@Column
	private String content; // 通知内容
	
	@Column
	private Date loseTime; // 失效时间
	
	@Column
	private Long storeId; // 通知范围  商家ID
	
	@Column
	private Date createdTime; //  创建时间
	
	@Column
	private Long accountId; // 创建人
	
	@Column
	private Long isMainStoreId; // 是否选择的是所有商家-1表示是，0表示不是
	
	private Integer markRead;
	
	private String createdName; // 创建人
	private String storeName; // 通知商家
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getLoseTime() {
		return loseTime;
	}
	public void setLoseTime(Date loseTime) {
		this.loseTime = loseTime;
	}
	public Long getStoreId() {
		return storeId;
	}
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public Long getIsMainStoreId() {
		return isMainStoreId;
	}
	public void setIsMainStoreId(Long isMainStoreId) {
		this.isMainStoreId = isMainStoreId;
	}
	public String getCreatedName() {
		return createdName;
	}
	public void setCreatedName(String createdName) {
		this.createdName = createdName;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	@Override
	public String toString() {
		return "Inform [id=" + id + ", name=" + name + ", content=" + content
				+ ", loseTime=" + loseTime + ", storeId=" + storeId
				+ ", createdTime=" + createdTime + ", accountId=" + accountId
				+ ", isMainStoreId=" + isMainStoreId + ", createdName="
				+ createdName + ", storeName=" + storeName + "]";
	}
	public Integer getMarkRead() {
		return markRead;
	}
	public void setMarkRead(Integer markRead) {
		this.markRead = markRead;
	}
	
}