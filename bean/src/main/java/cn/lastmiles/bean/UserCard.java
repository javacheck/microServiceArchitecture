package cn.lastmiles.bean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 会员卡
 * @author hql
 *
 */
@Table(name="t_user_card")
public class UserCard {
	@Id
	@Column
	private Long id;
	
	@Column
	private String cardNum;//卡号，默认是id一样，可以给商家输入
	
	@Column
	private String mobile;
	
	@Column
	private Double 	discount;//折扣
	
	@Column
	private Integer discountType;//折扣类型  1按照会员等级折扣，2固定折扣，3无折扣
	
	@Column
	private String memo;//会员卡备注
	
	@Column
	private Double balance = 0D;//余额 (可消费金额)
	
	@Column
	private Date createdTime = new Date();
	
	@Column
	private Long storeId;
	
	@Column
	private Long accountId;//操作帐号
	
	@Column
	private Integer status = 1;//状态 1正常，2冻结,3挂失
	
	@Column
	private String userLevelName;//会员等级名称
	
	@Column
	private Double userLevelDiscount;//会员等级折扣
	
	@Column
	private Double point = 0D;//当前积分
	
	@Column
	private Double totalPoint = 0D;//累计积分
	
	@Column
	private Double totalConsumption = 0D; //累计消费金额
	@Column
	private String name; // 会员卡名称(2016-04-14应客户要求加入)
	
	@Column
	private Long upgradedLevelId; // 可升级、与会员等级关联(2016-05-17应会员卡列表可升级列表要求而加入关联)
	
	@Column
	private String password;//支付密码
	@Column
	private Long levelId;//等级ID
	
	private int payWay = 0; 
	private List<UserCardRecord> ucrList;
	
	public int getPayWay() {
		return payWay;
	}

	public void setPayWay(int payWay) {
		this.payWay = payWay;
	}

	public Long getLevelId() {
		return levelId;
	}

	public void setLevelId(Long levelId) {
		this.levelId = levelId;
	}

	private Double actualAmount = 0D; // 充值金额

	private UserStoreServicePackage  userStoreServicePackage;
	private Double realityPrice = 0D; // 服务套餐实收金额
	
	private int source = 0;//0表示后台添加，1表示pos端
	
	private String identity; // 身份证号码（会员）
	private String birthTime;// 生日（会员）
	private Integer sex; // 性别（会员）性别 0为男 1为女 2保密
	
	@Column
	private Integer remindedSms;//余额不足短信提醒，1表示已经提醒过了，0或者其他表示还没有
	
	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getBirthTime() {
		return birthTime;
	}

	public void setBirthTime(String birthTime) {
		this.birthTime = birthTime;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Double getRealityPrice() {
		return realityPrice;
	}

	public Long getUpgradedLevelId() {
		return upgradedLevelId;
	}

	public void setUpgradedLevelId(Long upgradedLevelId) {
		this.upgradedLevelId = upgradedLevelId;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setRealityPrice(Double realityPrice) {
		this.realityPrice = realityPrice;
	}

	public Double getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(Double actualAmount) {
		this.actualAmount = actualAmount;
	}

	public Double getPoint() {
		if( null != point ){
			point = BigDecimal.valueOf(point).setScale(0,BigDecimal.ROUND_FLOOR).doubleValue();			
		}
		return point;
	}

	public void setPoint(Double point) {
		this.point = point;
	}

	private String storeName;
	
	private String accountName;
	
	private String serviceNameArray;
	
	public String getServiceNameArray() {
		return serviceNameArray;
	}

	public void setServiceNameArray(String serviceNameArray) {
		this.serviceNameArray = serviceNameArray;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public UserStoreServicePackage getUserStoreServicePackage() {
		return userStoreServicePackage;
	}

	public void setUserStoreServicePackage(
			UserStoreServicePackage userStoreServicePackage) {
		this.userStoreServicePackage = userStoreServicePackage;
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

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Integer getDiscountType() {
		return discountType;
	}

	public void setDiscountType(Integer discountType) {
		this.discountType = discountType;
	}

	public Double getBalance() {
		if( null != balance ){
			balance = BigDecimal.valueOf(balance).setScale(2,BigDecimal.ROUND_FLOOR).doubleValue();			
		}
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
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

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getUserLevelName() {
		return userLevelName;
	}

	public void setUserLevelName(String userLevelName) {
		this.userLevelName = userLevelName;
	}

	public Double getUserLevelDiscount() {
		if( null != userLevelDiscount ){
			userLevelDiscount = BigDecimal.valueOf(userLevelDiscount).setScale(1,BigDecimal.ROUND_FLOOR).doubleValue();			
		}
		return userLevelDiscount;
	}

	public void setUserLevelDiscount(Double userLevelDiscount) {
		this.userLevelDiscount = userLevelDiscount;
	}

	public Double getTotalPoint() {
		if( null != totalPoint ){
			totalPoint = BigDecimal.valueOf(totalPoint).setScale(0,BigDecimal.ROUND_FLOOR).doubleValue();			
		}
		return totalPoint;
	}

	public void setTotalPoint(Double totalPoint) {
		this.totalPoint = totalPoint;
	}

	public Double getTotalConsumption() {
		return totalConsumption;
	}

	public void setTotalConsumption(Double totalConsumption) {
		this.totalConsumption = totalConsumption;
	}


	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getRemindedSms() {
		if (remindedSms == null){
			remindedSms = 0;
		}
		return remindedSms;
	}

	public void setRemindedSms(Integer remindedSms) {
		this.remindedSms = remindedSms;
	}

	public List<UserCardRecord> getUcrList() {
		return ucrList;
	}

	public void setUcrList(List<UserCardRecord> ucrList) {
		this.ucrList = ucrList;
	}

	@Override
	public String toString() {
		return "UserCard [id=" + id + ", cardNum=" + cardNum + ", mobile="
				+ mobile + ", discount=" + discount + ", discountType="
				+ discountType + ", memo=" + memo + ", balance=" + balance
				+ ", createdTime=" + createdTime + ", storeId=" + storeId
				+ ", accountId=" + accountId + ", status=" + status
				+ ", userLevelName=" + userLevelName + ", userLevelDiscount="
				+ userLevelDiscount + ", point=" + point + ", totalPoint="
				+ totalPoint + ", totalConsumption=" + totalConsumption
				+ ", name=" + name + ", upgradedLevelId=" + upgradedLevelId
				+ ", password=" + password + ", levelId=" + levelId
				+ ", payWay=" + payWay + ", ucrList=" + ucrList
				+ ", actualAmount=" + actualAmount
				+ ", userStoreServicePackage=" + userStoreServicePackage
				+ ", realityPrice=" + realityPrice + ", source=" + source
				+ ", identity=" + identity + ", birthTime=" + birthTime
				+ ", sex=" + sex + ", remindedSms=" + remindedSms
				+ ", storeName=" + storeName + ", accountName=" + accountName
				+ ", serviceNameArray=" + serviceNameArray + "]";
	}

	
}