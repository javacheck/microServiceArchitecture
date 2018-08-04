package cn.lastmiles.bean;

import java.util.Date;

/**
 * 
 * lastUpdate 2016/10/20
 * @author shaoyikun
 *
 */
public class AfterSalesExcel {

	private String storeName;
	private Long orderId;
	private String productName;
	private String barCode;
	private String unitName;
	private String productCategoryName;
	private Double price;
	private String afterSalesTypeName;
	private Double amount;
	private String remark;
	private Date createdTime;
	private String username;

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getProductCategoryName() {
		return productCategoryName;
	}

	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getAfterSalesTypeName() {
		return afterSalesTypeName;
	}

	public void setAfterSalesTypeName(String afterSalesTypeName) {
		this.afterSalesTypeName = afterSalesTypeName;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
