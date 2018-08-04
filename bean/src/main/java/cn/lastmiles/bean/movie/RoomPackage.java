package cn.lastmiles.bean.movie;

import java.util.Arrays;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 包间套餐
 * @author hql
 *
 */
@Table(name="t_movie_room_package")
public class RoomPackage {
	@Id
	@Column
	private Long id;
	
	@Column
	private Double price = 0D;//套餐价格
	
	@Column
	private Date createdTime;
	
	@Column
	private Date updatedTime;//更新时间
	
	@Column
	private Long accountId;//创建者
	
	@Column
	private Long updatedId;//更新者
	
	@Column
	private String name;
	
	@Column
	private String memo;
	
	@Column
	private Long storeId;
	
	@Column
	private Integer duration = 0;//时长，单位小时
	
	private String[] roomPackageProductCache;
	private String[] roomPackageProductCacheAmount;
	
	
	private Double duration_Cache = 0D;// 临时存储数值
	
	private String updateName;
	private String createName;
	
	public Double getDuration_Cache() {
		return duration_Cache;
	}

	public void setDuration_Cache(Double duration_Cache) {
		this.duration_Cache = duration_Cache;
	}

	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String[] getRoomPackageProductCache() {
		return roomPackageProductCache;
	}

	public void setRoomPackageProductCache(String[] roomPackageProductCache) {
		this.roomPackageProductCache = roomPackageProductCache;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getUpdatedId() {
		return updatedId;
	}

	public void setUpdatedId(Long updatedId) {
		this.updatedId = updatedId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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



	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}



	public String[] getRoomPackageProductCacheAmount() {
		return roomPackageProductCacheAmount;
	}

	public void setRoomPackageProductCacheAmount(
			String[] roomPackageProductCacheAmount) {
		this.roomPackageProductCacheAmount = roomPackageProductCacheAmount;
	}

	@Override
	public String toString() {
		return "RoomPackage [id=" + id + ", price=" + price + ", createdTime="
				+ createdTime + ", updatedTime=" + updatedTime + ", accountId="
				+ accountId + ", updatedId=" + updatedId + ", name=" + name
				+ ", memo=" + memo + ", storeId=" + storeId + ", duration="
				+ duration + ", roomPackageProductCache="
				+ Arrays.toString(roomPackageProductCache)
				+ ", roomPackageProductCacheAmount="
				+ Arrays.toString(roomPackageProductCacheAmount)
				+ ", updateName=" + updateName + ", createName=" + createName
				+ "]";
	}
	
	
}
