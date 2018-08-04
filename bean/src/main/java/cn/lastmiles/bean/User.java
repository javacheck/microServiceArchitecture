package cn.lastmiles.bean;

import java.io.Serializable;
import java.util.Date;

import cn.lastmiles.common.utils.ConfigUtils;
import cn.lastmiles.common.utils.StringUtils;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8463494953077723182L;
	private Long id;
	private String mobile;
	private String name;
	
	private Date createdTime;
	private Long storeId;
	private Store store;
	private Long createdId;
	private String password;

	private Long identifyTypeId;// 身份证明类型代码
	private String identity;// 证件号码
	private String nickName;// 昵称
	private String iconUrl;// 图片(头像)
	private String phoneNumber;// 联系电话
	private String address;// 地址
	private String memo;// 备注(我的描述)
	private Date updateTime;// 更新时间
	private String realName; // 真实姓名
	private Integer status;// 状态 0 为启用 1为 禁用
	private String identifyTypeName;
	private Integer idAudit;// 身份审核状态0审核中，1审核通过，2审核失败
	private String token;
	private String recommended;// 推荐人
	private Double point=0D;//积分
	private Double totalPoint = 0D;//累计积分
	private Integer grade;//等级
	private String cardNumber;//会员卡号
	private Double balance;//余额 
	
	private Integer serviceTimes;//服务次数
	
	private Integer publishTimes;//发布次数
	
	private Double totalConsumption = 0D;//累计消费金额
	
	private Date birthDay;//生日
	
	private Double 	discount;
	

	// 推送对应的id
	private String cid;

	// 性别 0为男 1为女 2保密
	private Integer sex = 2;

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Long getCreatedId() {
		return createdId;
	}

	public void setCreatedId(Long createdId) {
		this.createdId = createdId;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Long getIdentifyTypeId() {
		return identifyTypeId;
	}

	public void setIdentifyTypeId(Long identifyTypeId) {
		this.identifyTypeId = identifyTypeId;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getIconUrl() {
		if (StringUtils.isBlank(iconUrl)) {
			return getDefaultIcon();
		}
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getIdentifyTypeName() {
		return identifyTypeName;
	}

	public void setIdentifyTypeName(String identifyTypeName) {
		this.identifyTypeName = identifyTypeName;
	}

	public Integer getIdAudit() {
		return idAudit;
	}

	public void setIdAudit(Integer idAudit) {
		this.idAudit = idAudit;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRecommended() {
		return recommended;
	}

	public void setRecommended(String recommended) {
		this.recommended = recommended;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}
	

	

	@Override
	public String toString() {
		return "User [id=" + id + ", mobile=" + mobile + ", name=" + name
				+ ", discount=" + discount + ", createdTime=" + createdTime
				+ ", storeId=" + storeId + ", store=" + store + ", createdId="
				+ createdId + ", password=" + password + ", identifyTypeId="
				+ identifyTypeId + ", identity=" + identity + ", nickName="
				+ nickName + ", iconUrl=" + iconUrl + ", phoneNumber="
				+ phoneNumber + ", address=" + address + ", memo=" + memo
				+ ", updateTime=" + updateTime + ", realName=" + realName
				+ ", status=" + status + ", identifyTypeName="
				+ identifyTypeName + ", idAudit=" + idAudit + ", token="
				+ token + ", recommended=" + recommended + ", point=" + point
				+ ", grade=" + grade + ", cardNumber=" + cardNumber
				+ ", balance=" + balance + ", serviceTimes=" + serviceTimes
				+ ", publishTimes=" + publishTimes + ", cid=" + cid + ", sex="
				+ sex + "]";
	}

	public static String getDefaultIcon(){
		return ConfigUtils.getProperty("user.default.icon");
	}

	public Integer getServiceTimes() {
		return serviceTimes;
	}

	public void setServiceTimes(Integer serviceTimes) {
		this.serviceTimes = serviceTimes;
	}

	public Integer getPublishTimes() {
		return publishTimes;
	}

	public void setPublishTimes(Integer publishTimes) {
		this.publishTimes = publishTimes;
	}

	

	public Double getPoint() {
		return point == null ? 0D :point;
	}

	public void setPoint(Double point) {
		this.point = point;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Double getTotalConsumption() {
		return totalConsumption == null ? 0 : totalConsumption;
	}

	public void setTotalConsumption(Double totalConsumption) {
		this.totalConsumption = totalConsumption;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	public Double getTotalPoint() {
		return totalPoint == null ? 0D : totalPoint;
	}

	public void setTotalPoint(Double totalPoint) {
		this.totalPoint = totalPoint;
	}
}
