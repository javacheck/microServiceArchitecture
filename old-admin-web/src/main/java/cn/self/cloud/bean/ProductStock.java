package cn.self.cloud.bean;

import java.util.List;

/**
 * 产品库存
 * 
 * @author hql
 *
 */
public class ProductStock {
	private Long id;
	private Long productId;
	private Double price;

	// 库存
	private Integer stock;
	private Integer alarmValue;
	// product attribute value id按照大小顺序连接，和productId组成唯一
	private String attributeCode;
	private Long accountId;
	private Long storeId;
	private String barCode;
	
	private List<ProductAttributeValue> pavList;
	private String productName;
	
	private Product product;
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

	public String getAttributeCode() {
		return attributeCode;
	}

	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public List<ProductAttributeValue> getPavList() {
		return pavList;
	}

	public void setPavList(List<ProductAttributeValue> pavList) {
		this.pavList = pavList;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getAlarmValue() {
		return alarmValue;
	}

	public void setAlarmValue(Integer alarmValue) {
		this.alarmValue = alarmValue;
	}
	
	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	@Override
	public String toString() {
		return "ProductStock [id=" + id + ", productId=" + productId
				+ ", price=" + price + ", stock=" + stock + ", alarmValue="
				+ alarmValue + ", attributeCode=" + attributeCode
				+ ", accountId=" + accountId + ", storeId=" + storeId
				+ ", pavList=" + pavList + ", productName=" + productName
				+ ", product=" + product + "]";
	}
	
}
