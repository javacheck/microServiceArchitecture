package cn.lastmiles.bean;

import java.util.Date;

public class Address {
	private Long id;//地址ID
	private String name;//用户姓名
	private String address;//地址
	private String phone;//手机号码
	private Date createdTime;//创建时间
	private Integer isDefault;//1默认，0不是
	private Long userId;//用户ID
	private Long areaId;//省市区ID
	private String path;//省市区路径
	private String areaFullName;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Integer getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getAreaId() {
		return areaId;
	}
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	@Override
	public String toString() {
		return "Address [id=" + id + ", name=" + name + ", address=" + address
				+ ", phone=" + phone + ", createdTime=" + createdTime
				+ ", isDefault=" + isDefault + ", userId=" + userId
				+ ", areaId=" + areaId + ", path=" + path + "]";
	}
	public String getAreaFullName() {
		return areaFullName;
	}
	public void setAreaFullName(String areaFullName) {
		this.areaFullName = areaFullName;
	}
	
	
}
