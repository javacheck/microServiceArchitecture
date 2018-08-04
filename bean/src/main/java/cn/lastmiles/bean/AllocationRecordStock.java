package cn.lastmiles.bean;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 库存调拨 商品关联表
 * 
 * @author hql
 *
 */
@Table(name = "t_allocation_record_stock")
public class AllocationRecordStock {
	@Column
	private Long allocationRecordId;

	@Column
	private String productName;

	@Column
	private String barCode;

	@Column
	private String categoryName;

	@Column
	private String attributeName;

	@Column
	private Double amount;// 数量
	
	@Column
	private Double lastAmount;// 实际到货数量
	
	@Column
	private Long stockId;//库存ID
	
	private Double stock;//库存
	private Integer alarmValue;//库存警报值
	
	private String allocationNumber;//调拨单号：规则为年月日末尾加两位数
	public Long getAllocationRecordId() {
		return allocationRecordId;
	}

	public void setAllocationRecordId(Long allocationRecordId) {
		this.allocationRecordId = allocationRecordId;
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

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Long getStockId() {
		return stockId;
	}

	public void setStockId(Long stockId) {
		this.stockId = stockId;
	}


	public Double getStock() {
		return stock;
	}

	public void setStock(Double stock) {
		this.stock = stock;
	}

	public Double getLastAmount() {
		return lastAmount;
	}

	public void setLastAmount(Double lastAmount) {
		this.lastAmount = lastAmount;
	}

	public Integer getAlarmValue() {
		return alarmValue;
	}

	public void setAlarmValue(Integer alarmValue) {
		this.alarmValue = alarmValue;
	}
	
	public String getAllocationNumber() {
		return allocationNumber;
	}

	public void setAllocationNumber(String allocationNumber) {
		this.allocationNumber = allocationNumber;
	}

	@Override
	public String toString() {
		return "AllocationRecordStock [allocationRecordId="
				+ allocationRecordId + ", productName=" + productName
				+ ", barCode=" + barCode + ", categoryName=" + categoryName
				+ ", attributeName=" + attributeName + ", amount=" + amount
				+ ", lastAmount=" + lastAmount + ", stockId=" + stockId
				+ ", stock=" + stock + ", alarmValue=" + alarmValue
				+ ", allocationNumber=" + allocationNumber + "]";
	}

	

	

	

	
}
