package cn.lastmiles.bean;

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
	private Long categoryId;//分类ID
	private String categoryName;//分类名称
	//private Long productId;//产品id
	//private String productName;//产品名称
	private String categoryIds;//产品ID数组
	private String storeName;//商家
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
	

	public String getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(String categoryIds) {
		this.categoryIds = categoryIds;
	}

	public List<ProductAttributeValue> getProductAttributeValues() {
		return productAttributeValues;
	}

	public void setProductAttributeValues(List<ProductAttributeValue> productAttributeValues) {
		this.productAttributeValues = productAttributeValues;
	}
	
	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	@Override
	public String toString() {
		return "ProductAttribute [id=" + id + ", name=" + name
				+ ", categoryId=" + categoryId + ", categoryName="
				+ categoryName + ", categoryIds=" + categoryIds
				+ ", storeName=" + storeName + ", productAttributeValues="
				+ productAttributeValues + "]";
	}

	

	
}
