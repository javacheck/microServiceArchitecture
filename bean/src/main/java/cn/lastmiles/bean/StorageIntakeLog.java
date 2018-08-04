package cn.lastmiles.bean;

import java.io.Serializable;
import java.util.Date;


public class StorageIntakeLog implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4071599592581747734L;

	private Long id ;//Id
	
	private String productName;//商品名
	
	private Long productStockId;//库存ID
	
	private String attributeName;//属性值
	
	private String barCode;//条形码
	
	private Double costPrice;//进货价
	
	private Double price;//零售价
	
	private Integer intakeStock;//入库数量
	
	private Integer totalityStock;//总数
	
	private Date createdTime;//入库时间
	
	private String accountName;//操作人名称
	
	private Long accountId;//操作人Id
	
	private Long storeId;//商店Id
	
	private Integer shelves;//上下架 0 上架，1下架
	
	private String attributeValuesListJointValue; 
	
	private String categoryName ;

	
	public String getAttributeValuesListJointValue() {
		return attributeValuesListJointValue;
	}

	public void setAttributeValuesListJointValue(
			String attributeValuesListJointValue) {
		this.attributeValuesListJointValue = attributeValuesListJointValue;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public String toString() {
		return "StorageIntakeLog [id=" + id + ", productName=" + productName
				+ ", productStockId=" + productStockId + ", attributeName="
				+ attributeName + ", barCode=" + barCode + ", costPrice="
				+ costPrice + ", price=" + price + ", intakeStock="
				+ intakeStock + ", totalityStock=" + totalityStock
				+ ", createTime=" + getCreatedTime() + ", accountName=" + accountName
				+ ", accountId=" + accountId + ", storeId=" + storeId
				+ ", shelves=" + shelves + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getProductStockId() {
		return productStockId;
	}

	public void setProductStockId(Long productStockId) {
		this.productStockId = productStockId;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public Double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getIntakeStock() {
		return intakeStock;
	}

	public void setIntakeStock(Integer intakeStock) {
		this.intakeStock = intakeStock;
	}

	public Integer getTotalityStock() {
		return totalityStock;
	}

	public void setTotalityStock(Integer totalityStock) {
		this.totalityStock = totalityStock;
	}


	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Integer getShelves() {
		return shelves;
	}

	public void setShelves(Integer shelves) {
		this.shelves = shelves;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
	
	
	
	
}
