package cn.lastmiles.bean;

import java.util.Date;

import cn.lastmiles.common.utils.DateUtils;

public class Report {
	private Date createdTime;
	private Long stockId;
	private String productName;
	private double sumPrice;
	private Integer sumAmount;
	private Long categoryId;
	private String categoryName;
	private Long storeId;
	private String storeName;
	private Long accountId;
	private String accountName;
	
	public String getDate(){
		return createdTime != null ? DateUtils.format(createdTime, "YYYY-MM-dd") : "";
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Long getStockId() {
		return stockId;
	}

	public void setStockId(Long stockId) {
		this.stockId = stockId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getSumPrice() {
		return sumPrice;
	}

	public void setSumPrice(double sumPrice) {
		this.sumPrice = sumPrice;
	}

	public Integer getSumAmount() {
		return sumAmount;
	}

	public void setSumAmount(Integer sumAmount) {
		this.sumAmount = sumAmount;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	@Override
	public String toString() {
		return "Report [createdTime=" + createdTime + ", stockId=" + stockId
				+ ", productName=" + productName + ", sumPrice=" + sumPrice
				+ ", sumAmount=" + sumAmount + ", categoryId=" + categoryId
				+ ", categoryName=" + categoryName + ", storeId=" + storeId
				+ ", storeName=" + storeName + ", accountId=" + accountId
				+ ", accountName=" + accountName + "]";
	}
	
}
