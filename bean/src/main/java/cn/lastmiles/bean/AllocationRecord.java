package cn.lastmiles.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 库存调拨
 * @author hql
 *
 */
@Table(name="t_allocation_record")
public class AllocationRecord {
	@Id
	@Column
	private Long id;
	
	@Column
	private Date createdTime;
	
	@Column
	private Date allocationTime;//调拨时间，默认等于createdTime
	
	@Column
	private Date finishTime;
	
	@Column
	private String memo;//备注
	
	@Column
	private Long fromStoreId;//调出店铺ID
	
	@Column
	private Long toStoreId;//调入店铺ID
	
	@Column
	private Integer status;//0待审核，1待发货，2待收货，3已拒绝，4已完成
	
	@Column
	private String allocationNumber;//调拨单号：规则为年月日末尾加两位数
	
	@Column
	private Long storeId;//操作商家id
	
	@Column
	private Long accountId;//操作帐号id
	
	private String fromStoreName;//调出店铺
	private String toStoreName;//调入店铺
	private String accountMobile;//操作人手机号
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

	public Date getAllocationTime() {
		return allocationTime;
	}

	public void setAllocationTime(Date allocationTime) {
		this.allocationTime = allocationTime;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Long getFromStoreId() {
		return fromStoreId;
	}

	public void setFromStoreId(Long fromStoreId) {
		this.fromStoreId = fromStoreId;
	}

	public Long getToStoreId() {
		return toStoreId;
	}

	public void setToStoreId(Long toStoreId) {
		this.toStoreId = toStoreId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAllocationNumber() {
		return allocationNumber;
	}

	public void setAllocationNumber(String allocationNumber) {
		this.allocationNumber = allocationNumber;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getFromStoreName() {
		return fromStoreName;
	}

	public void setFromStoreName(String fromStoreName) {
		this.fromStoreName = fromStoreName;
	}

	public String getToStoreName() {
		return toStoreName;
	}

	public void setToStoreName(String toStoreName) {
		this.toStoreName = toStoreName;
	}

	

	public String getAccountMobile() {
		return accountMobile;
	}

	public void setAccountMobile(String accountMobile) {
		this.accountMobile = accountMobile;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	@Override
	public String toString() {
		return "AllocationRecord [id=" + id + ", createdTime=" + createdTime
				+ ", allocationTime=" + allocationTime + ", finishTime="
				+ finishTime + ", memo=" + memo + ", fromStoreId="
				+ fromStoreId + ", toStoreId=" + toStoreId + ", status="
				+ status + ", allocationNumber=" + allocationNumber
				+ ", storeId=" + storeId + ", accountId=" + accountId
				+ ", fromStoreName=" + fromStoreName + ", toStoreName="
				+ toStoreName + ", accountMobile=" + accountMobile + "]";
	}

	
	
}
