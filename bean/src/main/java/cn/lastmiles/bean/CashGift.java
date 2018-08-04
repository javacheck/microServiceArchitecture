package cn.lastmiles.bean;

import java.util.Date;

public class CashGift {
	private Long id;
	private Long userId;
	private Double amount;// 金额或者折扣 根据type决定
	private Integer status;// 0 未使用，1已经使用
	private Date createdTime;
	private String memo;
	private Date validTime;// 失效日期
	private Long storeId;// 商户id，null表示所有商家可以使用
	private Integer type;// 类型 0表示折扣卷，1表示现金卷
	private String validTimeReplace;//
	private String mobile ;
	private Double orderAmount;
	private Long  couponId;
	private Integer  shared;
	private String storeName;
	private Date usedTime;
	private String useTipInfo;// APP端
	private String name;//活动名称
	
	private String desc = "线上线下通用券";
	
	private String pcName; // 最新的优惠劵中的名称(2015.12.10)
	
	public String getPcName() {
		return pcName;
	}

	public void setPcName(String pcName) {
		this.pcName = pcName;
	}

	public String getUseTipInfo() {
		return useTipInfo;
	}

	public void setUseTipInfo(String useTipInfo) {
		this.useTipInfo = useTipInfo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getValidTimeReplace() {
		return validTimeReplace;
	}

	public void setValidTimeReplace(String validTimeReplace) {
		this.validTimeReplace = validTimeReplace;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getStatus() {
		if( null == this.validTime){
			return status;
		}
		Long isHaveUse = System.currentTimeMillis()-this.validTime.getTime() ;
		if(isHaveUse.longValue() >= 0){
			this.status = 2; // 过期
		}else if(isHaveUse.longValue() <0 && this.status==0){
			this.status = 0; // 未使用
		}
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getValidTime() {
		return validTime;
	}

	public void setValidTime(Date validTime) {
		this.validTime = validTime;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		if (null != type && type.intValue() == 0){
			return amount + "折优惠券";
		}else if (null != type && type.intValue() == 1){
			return amount + "元现金券";
		}
		return "";
	}

	public Date getUsedTime() {
		return usedTime;
	}

	public void setUsedTime(Date usedTime) {
		this.usedTime = usedTime;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public Integer getShared() {
		return shared;
	}

	public void setShared(Integer shared) {
		this.shared = shared;
	}

	public Double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "CashGift [id=" + id + ", userId=" + userId + ", amount="
				+ amount + ", status=" + status + ", createdTime="
				+ createdTime + ", memo=" + memo + ", validTime=" + validTime
				+ ", storeId=" + storeId + ", type=" + type
				+ ", validTimeReplace=" + validTimeReplace + ", mobile="
				+ mobile + ", orderAmount=" + orderAmount + ", couponId="
				+ couponId + ", shared=" + shared + ", storeName=" + storeName
				+ ", usedTime=" + usedTime + ", useTipInfo=" + useTipInfo
				+ ", name=" + name + "]";
	}

	public String getDesc() {
		if (type == 0){
			return amount + "折折扣券";
		}else {
			return amount + "元现金券";
		}
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
