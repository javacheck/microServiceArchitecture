package cn.lastmiles.bean;

import java.util.Date;

/**
 * createDate : 2015年8月19日 下午3:01:14 
 */

public class SalesPromotion {
	private Long id;
	private Long storeId; // 商家ID
	private Long stockId; // 商品ID
	private Date createTime; // 创建时间
	
	private String storeName ; // 商家名称
	private String productName ; // 商品名称
	
	private String attributeCode; // 商品属性值Code连接
	
	private String attributeName; // 商品属性值名称连接
	
	private Double price;
	private Double originalPrice;
	private int salesNum;
	
	private int shelves;
	
	private Long salesPromotionCategoryId;
	
	public SalesPromotion() {
		super();
	}
	
	public int getShelves() {
		return shelves;
	}

	public void setShelves(int shelves) {
		this.shelves = shelves;
	}

	public String getAttributeCode() {
		return attributeCode;
	}

	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	
	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(Double originalPrice) {
		this.originalPrice = originalPrice;
	}

	public int getSalesNum() {
		return salesNum;
	}

	public void setSalesNum(int salesNum) {
		this.salesNum = salesNum;
	}

	public Long getSalesPromotionCategoryId() {
		return salesPromotionCategoryId;
	}

	public void setSalesPromotionCategoryId(Long salesPromotionCategoryId) {
		this.salesPromotionCategoryId = salesPromotionCategoryId;
	}
}