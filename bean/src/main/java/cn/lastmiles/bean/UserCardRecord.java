package cn.lastmiles.bean;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 会员交易记录
 * @author hql
 *
 */
@Table(name="t_user_card_record")
public class UserCardRecord {
	@Id
	@Column
	private Long id;
	
	@Column
	private Long userCardId;
	
	@Column
	private Long storeId;
	
	@Column
	private Date createdTime;
	
	@Column
	private Integer type = 1; //使用类型，1充值，2消费，3积分兑换，4积分抵扣
	
	@Column
	private Long accountId;// 操作员id
	
	@Column
	private Double amount = 0D;// 充值金额
	
	@Column
	private Double actualAmount = 0D;// 实际可以使用金额   默认等于amount，如果是消费，则等于消费金额
	
	@Column
	private Double balance = 0D;// 用户余额 等于原来的balance+actualAmount，如果是消费balance = balance - actualAmount
	
	@Column
	private Double point = 0D;//这个记录获取或者消耗的积分
	
	@Column
	private Double totalPoint = 0D;//累计积分
	
	@Column
	private Double remainPoint = 0D;//剩余积分
	
	@Column
	private Long orderId;
	
	@Column
	private String mobile;
	
	@Column
	private Date validDate;//积分的有效期
	
	@Column
	private int source = 0;//来源 0 从后台 1 pos
	
	private String accountMobile;//
	private UserCard userCard;
	
	private String cardNum;
	
	@Column
	private int payWay = 0; // 支付方式 0 现金支付  、1 微信支付、2 银联支付，3支付宝
	@Column
	private int payStatus = 0; // 支付状态  0 支付成功、1 其他支付方式
	
	
	public UserCard getUserCard() {
		return userCard;
	}

	public void setUserCard(UserCard userCard) {
		this.userCard = userCard;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(Double actualAmount) {
		this.actualAmount = actualAmount;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
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

	public Double getTotalPoint() {
		if( null != totalPoint ){
			totalPoint = BigDecimal.valueOf(totalPoint).setScale(0,BigDecimal.ROUND_FLOOR).doubleValue();			
		}
		return totalPoint;
	}

	public void setTotalPoint(Double totalPoint) {
		this.totalPoint = totalPoint;
	}

	public Long getUserCardId() {
		return userCardId;
	}

	public void setUserCardId(Long userCardId) {
		this.userCardId = userCardId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
	public String getAccountMobile() {
		return accountMobile;
	}

	public void setAccountMobile(String accountMobile) {
		this.accountMobile = accountMobile;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}



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

	public Date getValidDate() {
		return validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}

	public Double getRemainPoint() {
		if( null != remainPoint ){
			remainPoint = BigDecimal.valueOf(remainPoint).setScale(0,BigDecimal.ROUND_FLOOR).doubleValue();			
		}
		return remainPoint;
	}

	public void setRemainPoint(Double remainPoint) {
		this.remainPoint = remainPoint;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	@Override
	public String toString() {
		return "UserCardRecord [id=" + id + ", userCardId=" + userCardId
				+ ", storeId=" + storeId + ", createdTime=" + createdTime
				+ ", type=" + type + ", accountId=" + accountId + ", amount="
				+ amount + ", actualAmount=" + actualAmount + ", balance="
				+ balance + ", point=" + point + ", totalPoint=" + totalPoint
				+ ", remainPoint=" + remainPoint + ", orderId=" + orderId
				+ ", mobile=" + mobile + ", validDate=" + validDate
				+ ", source=" + source + ", accountMobile=" + accountMobile
				+ ", userCard=" + userCard + ", cardNum=" + cardNum
				+ ", payWay=" + payWay + ", payStatus=" + payStatus + "]";
	}
	
	
}
