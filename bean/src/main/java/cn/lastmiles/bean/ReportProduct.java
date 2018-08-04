package cn.lastmiles.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "t_report_product")
public class ReportProduct {
	@Column
	private Long storeId;// 店铺id
	@Column
	private Long brandId;// 品牌id
	@Column
	private String brandName;// 品牌名称
	@Column
	private Long categoryId;// 分类id
	@Column
	private String categoryName;// 分类名称
	@Column
	private Long stockId;// 库存ID
	@Column
	private String stockName;// 库存名称
	@Column
	private String barCode;// 商名条码（新加）
	@Column
	private Double salesNum;// 销量 = orderItem 的amount
	@Column
	private Integer source;// 订单来源 常量ORDER_SOURCE_APP = 0;// 从APP获取//// ORDER_SOURCE_DEVICES = 1;// 从终端获取
	@Column
	private Long orderId;
	@Column
	private Double price;// 价格
	@Column
	private Double costPrice;// 成本
	@Column
	private Date reportDate;// 日期
	@Column
	private Double actualPrice;
	
	private Integer type = 0;//0普通商品，1包间套餐的商品

	private String storeName;// 商店名称
	
	private Double totalCostPrice;//销售成本
	private Double totalPrice;//销售额
	private Double totalGrossProfit;//参考毛利
	private Integer storeSum;//商店数量
	private Integer stockSum;//商品总数
	private String productName;
	private String attributeValues;
	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
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

	

	public Double getSalesNum() {
		return salesNum;
	}

	public void setSalesNum(Double salesNum) {
		this.salesNum = salesNum;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Long getStockId() {
		return stockId;
	}

	public void setStockId(Long stockId) {
		this.stockId = stockId;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

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

	public Double getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(Double actualPrice) {
		this.actualPrice = actualPrice;
	}

	public Double getTotalCostPrice() {
		return totalCostPrice;
	}

	public void setTotalCostPrice(Double totalCostPrice) {
		this.totalCostPrice = totalCostPrice;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Double getTotalGrossProfit() {
		return totalGrossProfit;
	}

	public void setTotalGrossProfit(Double totalGrossProfit) {
		this.totalGrossProfit = totalGrossProfit;
	}

	public Integer getStoreSum() {
		return storeSum;
	}

	public void setStoreSum(Integer storeSum) {
		this.storeSum = storeSum;
	}

	public Integer getStockSum() {
		return stockSum;
	}

	public void setStockSum(Integer stockSum) {
		this.stockSum = stockSum;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	

	
	
	
}
