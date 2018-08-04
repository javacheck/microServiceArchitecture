package cn.lastmiles.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 用户服务套餐表
 * @author hql
 *
 */
@Table(name="t_user_store_service_package")
public class UserStoreServicePackage {
	@Column
	private Long userCardId;
	
	@Column
	private Long storeId;
	
	@Column
	private Long storeServicePackageId;
	
	@Column
	private Date createdTime;
	
	@Column
	private Integer times = 0;//剩余服务次数
	
	private StoreServicePackage storeServicePackage;

	private Integer totalSaleTimes = 0; // 累计消费次数
	
	
	public Integer getTotalSaleTimes() {
		return totalSaleTimes;
	}

	public void setTotalSaleTimes(Integer totalSaleTimes) {
		this.totalSaleTimes = totalSaleTimes;
	}

	public StoreServicePackage getStoreServicePackage() {
		return storeServicePackage;
	}

	public void setStoreServicePackage(StoreServicePackage storeServicePackage) {
		this.storeServicePackage = storeServicePackage;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Long getStoreServicePackageId() {
		return storeServicePackageId;
	}

	public void setStoreServicePackageId(Long storeServicePackageId) {
		this.storeServicePackageId = storeServicePackageId;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Long getUserCardId() {
		return userCardId;
	}

	public void setUserCardId(Long userCardId) {
		this.userCardId = userCardId;
	}

	@Override
	public String toString() {
		return "UserStoreServicePackage [userCardId=" + userCardId
				+ ", storeId=" + storeId + ", storeServicePackageId="
				+ storeServicePackageId + ", createdTime=" + createdTime
				+ ", times=" + times + ", storeServicePackage="
				+ storeServicePackage + ", totalSaleTimes=" + totalSaleTimes
				+ "]";
	}

	
	
}
