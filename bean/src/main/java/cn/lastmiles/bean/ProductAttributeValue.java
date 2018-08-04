package cn.lastmiles.bean;

/**
 * 产品属性值
 * 
 * @author hql
 *
 */
public class ProductAttributeValue {
	private Long id;
	private String value;
	//属性id
	private Long attributeId;
	private String storeName;//商家

	private String productAttributeName;//商品属性名称
	private String categoryName;//商品名称
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(Long attributeId) {
		this.attributeId = attributeId;
	}

	public String getProductAttributeName() {
		return productAttributeName;
	}

	public void setProductAttributeName(String productAttributeName) {
		this.productAttributeName = productAttributeName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attributeId == null) ? 0 : attributeId.hashCode());
		result = prime * result
				+ ((categoryName == null) ? 0 : categoryName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((productAttributeName == null) ? 0 : productAttributeName
						.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductAttributeValue other = (ProductAttributeValue) obj;
		if (attributeId == null) {
			if (other.attributeId != null)
				return false;
		} else if (!attributeId.equals(other.attributeId))
			return false;
		if (categoryName == null) {
			if (other.categoryName != null)
				return false;
		} else if (!categoryName.equals(other.categoryName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (productAttributeName == null) {
			if (other.productAttributeName != null)
				return false;
		} else if (!productAttributeName.equals(other.productAttributeName))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	@Override
	public String toString() {
		return "ProductAttributeValue [id=" + id + ", value=" + value
				+ ", attributeId=" + attributeId + ", storeName=" + storeName
				+ ", productAttributeName=" + productAttributeName
				+ ", categoryName=" + categoryName + "]";
	}
	
	
}
