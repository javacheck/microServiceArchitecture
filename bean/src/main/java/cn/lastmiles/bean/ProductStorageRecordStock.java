package cn.lastmiles.bean;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name="t_product_storage_record_stock")
public class ProductStorageRecordStock {
	@Column
	private Long productStorageRecordId;//入库单号
	@Column
	private Long stockId;//库存ID
	@Column
	private String productName;//商品名称
	@Column
	private String barCode;//条码
	@Column
	private String attributeValues;//规格(属性值以逗号连起来)
	@Column
	private String unitName;//单位名称
	@Column
	private Double costPrice;//进货价格
	@Column
	private Double amount;//入库数量
	@Column
	private Double stock;//库存数量
	@Column
	private String supplierName;//供应商名称
	@Column
	private String memo;//备注
	public Long getProductStorageRecordId() {
		return productStorageRecordId;
	}
	public void setProductStorageRecordId(Long productStorageRecordId) {
		this.productStorageRecordId = productStorageRecordId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getAttributeValues() {
		return attributeValues;
	}
	public void setAttributeValues(String attributeValues) {
		this.attributeValues = attributeValues;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	
	public Double getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}
	
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getStock() {
		return stock;
	}
	public void setStock(Double stock) {
		this.stock = stock;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Long getStockId() {
		return stockId;
	}
	public void setStockId(Long stockId) {
		this.stockId = stockId;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	@Override
	public String toString() {
		return "ProductStorageRecordStock [productStorageRecordId="
				+ productStorageRecordId + ", stockId=" + stockId
				+ ", productName=" + productName + ", barCode=" + barCode
				+ ", attributeValues=" + attributeValues + ", unitName="
				+ unitName + ", costPrice=" + costPrice + ", amount="
				+ amount + ", stock=" + stock + ", supplierName="
				+ supplierName + ", memo=" + memo + "]";
	}
	
	
	
	
}
