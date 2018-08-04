package cn.lastmiles.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 会员购买套餐记录
 * 
 * @author hql
 *
 */
@Table(name = "t_user_store_service_package_record")
public class UserStoreServicePackageRecord {
	private Long id;
	
	@Column
	private Long userCardId;
	
	@Column
	private Long storeServicePackageId;
	
	@Column
	private Long accountId;// 操作员
	
	@Column
	private Integer times = 0; // 充值或者消费次数
	
	@Column
	private Integer beforeTimes = 0; // 消费或者充值前次数
	
	@Column
	private Integer remainTimes = 0; // 剩余次数 等于原来的remainTimes+times，如果是消费remainTimes = remainTimes - times
	
	@Column
	private Date createdTime;
	
	@Column
	private Integer type = 1; //使用类型，1充值，2消费
	
	@Column
	private Long orderId;
	
	@Column
	private String mobile;
	
	@Column
	private Double realityPrice = 0D; // 实收金额
	
	@Column
	private int source = 0;//来源 0 从后台 1 pos
	
	@Column
	private int payWay = 0; // 支付方式 0 现金支付  、1 微信支付、2 银联支付，3支付宝
	@Column
	private int payStatus = 0; // 支付状态  0 支付成功、1 其他支付方式
	
	
	
	public int getPayWay() {
		return payWay;
	}

	public void setPayWay(int payWay) {
		this.payWay = payWay;
	}

	public int getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}

	private String servicePackageName;
	private UserCard userCard;
	
	private String cardNum;
	
	public Double getRealityPrice() {
		return realityPrice;
	}

	public void setRealityPrice(Double realityPrice) {
		this.realityPrice = realityPrice;
	}

	public Integer getBeforeTimes() {
		return beforeTimes;
	}

	public void setBeforeTimes(Integer beforeTimes) {
		this.beforeTimes = beforeTimes;
	}

	public Integer getRemainTimes() {
		return remainTimes;
	}

	public void setRemainTimes(Integer remainTimes) {
		this.remainTimes = remainTimes;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getServicePackageName() {
		return servicePackageName;
	}

	public void setServicePackageName(String servicePackageName) {
		this.servicePackageName = servicePackageName;
	}

	public UserCard getUserCard() {
		return userCard;
	}

	public void setUserCard(UserCard userCard) {
		this.userCard = userCard;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getStoreServicePackageId() {
		return storeServicePackageId;
	}

	public void setStoreServicePackageId(Long storeServicePackageId) {
		this.storeServicePackageId = storeServicePackageId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Long getUserCardId() {
		return userCardId;
	}

	public void setUserCardId(Long userCardId) {
		this.userCardId = userCardId;
	}

	@Override
	public String toString() {
		return "UserStoreServicePackageRecord [id=" + id + ", userCardId="
				+ userCardId + ", storeServicePackageId="
				+ storeServicePackageId + ", accountId=" + accountId
				+ ", times=" + times + ", beforeTimes=" + beforeTimes
				+ ", remainTimes=" + remainTimes + ", createdTime="
				+ createdTime + ", type=" + type + ", orderId=" + orderId
				+ ", mobile=" + mobile + ", realityPrice=" + realityPrice
				+ ", source=" + source + ", payWay=" + payWay + ", payStatus="
				+ payStatus + ", servicePackageName=" + servicePackageName
				+ ", userCard=" + userCard + ", cardNum=" + cardNum + "]";
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	
}