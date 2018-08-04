package cn.lastmiles.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 登录帐号
 * 
 * @author hql
 *
 */
public class Account implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4234701583914231327L;
	private Long id;
	private String mobile;
	private String password;
	private String name;
	// 0男，1女
	private Integer sex = 0;
	private String email;
	private String address;
	// 身份证
	private String idCard;
	private Date createdTime;
	private Long parentId;
	private String path;
	//商店id ， 可以空
	private Long storeId;
	private String storeName;
	
	private Store store;
	
	private Device device;
	
	private Long agentId;//代理商ID
	private String cid;//消息推送ID
	private String agentName;
	private Integer type;  // 账户类型 1 为 管理员  2 为 代理商  3 为 商家
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Store getStore() {
		return store;
	}

	public Account setStore(Store store) {
		this.store = store;
		return this;
	}

	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}


	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", mobile=" + mobile + ", password="
				+ password + ", name=" + name + ", sex=" + sex + ", email="
				+ email + ", address=" + address + ", idCard=" + idCard
				+ ", createdTime=" + createdTime + ", parentId=" + parentId
				+ ", path=" + path + ", storeId=" + storeId + ", storeName="
				+ storeName + ", store=" + store + ", agentId=" + agentId
				+ ", agentName=" + agentName + ", type=" + type + "]";
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}
	
	
}
