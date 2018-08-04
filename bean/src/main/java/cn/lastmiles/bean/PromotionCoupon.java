package cn.lastmiles.bean;

import java.util.Date;

public class PromotionCoupon {
	private Long id;//优惠ID
	private String name;//优惠名称
	private Integer type;//类型 0表示折扣卷，1表示现金卷
	private Double minAmount;//最小金额或者折扣 根据type决定
	private Double maxAmount;//最大金额或者折扣 根据type决定
	private Integer totalNum;//总数量
	private Double totalAmount;//总额
	private Date startDate;//开始时间
	private Date endDate;//结束时间
	private Long storeId;//商户id，null表示所有商家可以使用
	private Integer range;//0所有用户，1新注册用户，2已经注册用户
	private Integer shared;//0不共享，1共享
	private Double orderAmount;//订单金额多少才可以使用，0表示无限制
	private Integer validDay;//有效期，天
	private String memo;//说明
	private Integer status;//0无效，1有效
	private Integer decimalBit;//小数位数
	private Integer promotionStatus;
	
	private String storeName;//商店名称
	
	private Integer issueType = 0;//发布方式，0表示自动，1表示手动，2表示线下
	
	private String URL;
	private Integer isUpdate;
	
	private Integer reportNum = 0; // 已经导出的数量
	
	public Integer getReportNum() {
		return reportNum;
	}
	public void setReportNum(Integer reportNum) {
		this.reportNum = reportNum;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public Integer getIsUpdate() {
		return isUpdate;
	}
	public void setIsUpdate(Integer isUpdate) {
		this.isUpdate = isUpdate;
	}
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Double getMinAmount() {
		return minAmount;
	}
	public void setMinAmount(Double minAmount) {
		this.minAmount = minAmount;
	}
	public Double getMaxAmount() {
		return maxAmount;
	}
	public void setMaxAmount(Double maxAmount) {
		this.maxAmount = maxAmount;
	}
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Long getStoreId() {
		return storeId;
	}
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	public Integer getRange() {
		return range;
	}
	public void setRange(Integer range) {
		this.range = range;
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
	public Integer getValidDay() {
		return validDay;
	}
	public void setValidDay(Integer validDay) {
		this.validDay = validDay;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public Integer getDecimalBit() {
		return decimalBit;
	}
	public void setDecimalBit(Integer decimalBit) {
		this.decimalBit = decimalBit;
	}
	public Integer getPromotionStatus() {
		if( null == this.startDate ){
			return promotionStatus;
		}
		Long isHaveUse = System.currentTimeMillis()-this.startDate.getTime() ;
		Long overTime = null;
		if( null != this.endDate ){
			overTime = System.currentTimeMillis() - this.endDate.getTime();			
		}
		if(isHaveUse.longValue() < 0 && this.status.intValue()==1){
			this.promotionStatus = 2; // 即将开始
		}else if(this.status.intValue()==2 || this.status.intValue()==0){
			this.promotionStatus=0;
		}else if( null != overTime && overTime > 0 ){
			this.promotionStatus = 0;
		} else {			
			this.promotionStatus=1;
		}
		return promotionStatus;
	}
	public void setPromotionStatus(Integer promotionStatus) {
		this.promotionStatus = promotionStatus;
	}
	
	public Integer getIssueType() {
		return issueType;
	}
	public void setIssueType(Integer issueType) {
		this.issueType = issueType;
	}
	@Override
	public String toString() {
		return "PromotionCoupon [id=" + id + ", name=" + name + ", type="
				+ type + ", minAmount=" + minAmount + ", maxAmount="
				+ maxAmount + ", totalNum=" + totalNum + ", totalAmount="
				+ totalAmount + ", startDate=" + startDate + ", endDate="
				+ endDate + ", storeId=" + storeId + ", range=" + range
				+ ", shared=" + shared + ", orderAmount=" + orderAmount
				+ ", validDay=" + validDay + ", memo=" + memo + ", status="
				+ status + ", decimalBit=" + decimalBit + ", promotionStatus="
				+ promotionStatus + ", storeName=" + storeName + ", issueType="
				+ issueType + ", URL=" + URL + ", isUpdate=" + isUpdate
				+ ", getURL()=" + getURL() + ", getIsUpdate()=" + getIsUpdate()
				+ ", getId()=" + getId() + ", getName()=" + getName()
				+ ", getType()=" + getType() + ", getMinAmount()="
				+ getMinAmount() + ", getMaxAmount()=" + getMaxAmount()
				+ ", getTotalNum()=" + getTotalNum() + ", getTotalAmount()="
				+ getTotalAmount() + ", getStartDate()=" + getStartDate()
				+ ", getEndDate()=" + getEndDate() + ", getStoreId()="
				+ getStoreId() + ", getRange()=" + getRange()
				+ ", getShared()=" + getShared() + ", getOrderAmount()="
				+ getOrderAmount() + ", getValidDay()=" + getValidDay()
				+ ", getMemo()=" + getMemo() + ", getStatus()=" + getStatus()
				+ ", getStoreName()=" + getStoreName() + ", getDecimalBit()="
				+ getDecimalBit() + ", getPromotionStatus()="
				+ getPromotionStatus() + ", getIssueType()=" + getIssueType()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	
	
	
	
	
}
