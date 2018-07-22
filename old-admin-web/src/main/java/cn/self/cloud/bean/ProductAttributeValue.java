package cn.self.cloud.bean;

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
	@Override
	public String toString() {
		return "ProductAttributeValue [id=" + id + ", value=" + value
				+ ", attributeId=" + attributeId + ", productAttributeName="
				+ productAttributeName + ", productName=" + productName + "]";
	}

	private String productAttributeName;//商品属性名称
	private String productName;//商品名称
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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
}
