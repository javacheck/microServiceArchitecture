package cn.self.cloud.bean;

import java.util.List;

/**
 * 产品属性
 * 
 * @author hql
 *
 */
public class ProductAttribute {
	private Long id;//产品属性id
	private String name;//产品属性名称
	private Long productId;//产品id
	private String productName;//产品名称
	private String productIds;//产品ID数组
	
	private List<ProductAttributeValue> productAttributeValues;//产品属性值列表

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

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductIds() {
		return productIds;
	}

	public void setProductIds(String productIds) {
		this.productIds = productIds;
	}

	@Override
	public String toString() {
		return "ProductAttribute [id=" + id + ", name=" + name + ", productId="
				+ productId + ", productName=" + productName + ", productIds="
				+ productIds + "]";
	}

	public List<ProductAttributeValue> getProductAttributeValues() {
		return productAttributeValues;
	}

	public void setProductAttributeValues(List<ProductAttributeValue> productAttributeValues) {
		this.productAttributeValues = productAttributeValues;
	}

	
}
