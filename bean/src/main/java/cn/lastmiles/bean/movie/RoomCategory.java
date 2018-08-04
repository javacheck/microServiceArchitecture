package cn.lastmiles.bean.movie;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.lastmiles.common.utils.ClassUtil;

/**
 * 包间类型
 * 
 * @author hql
 *
 */
@Table(name = "t_movie_room_category")
public class RoomCategory {
	@Column
	@Id
	private Long id;

	@Column
	private String name;

	@Column
	private Long storeId;
	
	@Column
	private Long createAccountId;
	
	@Column
	private Long updateAccountId;
	@Column
	private Date createDate;
	@Column
	private Date updateDate;
	
	@Column
	private Double holidayPrice;//节假日收费
	
	@Column
	private Double lowestPrice;//最低消费
	
	private String storeName;
	
	private String createAccountName;
	
	private String updateAccountName;

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

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Double getHolidayPrice() {
		return holidayPrice;
	}

	public void setHolidayPrice(Double holidayPrice) {
		this.holidayPrice = holidayPrice;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getCreateAccountName() {
		return createAccountName;
	}

	public void setCreateAccountName(String createAccountName) {
		this.createAccountName = createAccountName;
	}

	public String getUpdateAccountName() {
		return updateAccountName;
	}

	public void setUpdateAccountName(String updateAccountName) {
		this.updateAccountName = updateAccountName;
	}

	public Long getCreateAccountId() {
		return createAccountId;
	}

	public void setCreateAccountId(Long createAccountId) {
		this.createAccountId = createAccountId;
	}

	public Long getUpdateAccountId() {
		return updateAccountId;
	}

	public void setUpdateAccountId(Long updateAccountId) {
		this.updateAccountId = updateAccountId;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String toString(){
        return ClassUtil.getFields(this).toString();
	}

	public Double getLowestPrice() {
		return lowestPrice;
	}

	public void setLowestPrice(Double lowestPrice) {
		this.lowestPrice = lowestPrice;
	}
}
