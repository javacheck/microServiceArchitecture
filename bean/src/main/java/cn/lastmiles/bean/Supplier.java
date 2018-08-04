package cn.lastmiles.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 供应商
 * @author hql
 *
 */
@Table(name="t_supplier")
public class Supplier {
	@Id
	@Column(name="id")
	private Long id;//供应商ID
	
	@Column(name="createdTime")
	private Date createdTime;//创建时间
	
	@Column
	private String name;//供应商名称
	
	@Column
	private String contacts;//联系人
	
	@Column
	private String phone;
	
	@Column
	private String address;
	
	@Column
	private Long storeId;//店铺ID
	
	@Column
	private String memo;

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

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Override
	public String toString() {
		return "Supplier [id=" + id + ", createdTime=" + createdTime
				+ ", name=" + name + ", contacts=" + contacts + ", phone="
				+ phone + ", address=" + address + ", storeId=" + storeId
				+ ", memo=" + memo + "]";
	}
	
	
}
