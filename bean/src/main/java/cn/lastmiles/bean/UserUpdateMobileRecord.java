package cn.lastmiles.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="t_user_update_mobile_record")
public class UserUpdateMobileRecord {
	@Id
	@Column
	private Long id;//修改会员手机记录ID
	@Column
	private Long accountStoreId;//操作商家ID
	@Column
	private Long storeId;//会员手机关联商家ID
	@Column
	private String oldMobile;//会员旧手机号
	@Column
	private String newMobile;//会员新手机号
	@Column
	private Long accountId;//操作人ID
	@Column
	private Date createdTime;//创建时间
	private String accountStoreName;//操作商家名称
	private String accountName;//操作人手机号
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getAccountStoreId() {
		return accountStoreId;
	}
	public void setAccountStoreId(Long accountStoreId) {
		this.accountStoreId = accountStoreId;
	}
	public Long getStoreId() {
		return storeId;
	}
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	public String getOldMobile() {
		return oldMobile;
	}
	public void setOldMobile(String oldMobile) {
		this.oldMobile = oldMobile;
	}
	public String getNewMobile() {
		return newMobile;
	}
	public void setNewMobile(String newMobile) {
		this.newMobile = newMobile;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public String getAccountStoreName() {
		return accountStoreName;
	}
	public void setAccountStoreName(String accountStoreName) {
		this.accountStoreName = accountStoreName;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	@Override
	public String toString() {
		return "UserUpdateMobileRecord [id=" + id + ", accountStoreId="
				+ accountStoreId + ", storeId=" + storeId + ", oldMobile="
				+ oldMobile + ", newMobile=" + newMobile + ", accountId="
				+ accountId + ", createdTime=" + createdTime
				+ ", accountStoreName=" + accountStoreName + ", accountName="
				+ accountName + "]";
	}
	
}
