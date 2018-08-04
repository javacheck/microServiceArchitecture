package cn.lastmiles.bean;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "t_product_stock_attribute_value")
public class ProductStockAttributeValue {
	@Column
	private Long id;

	@Column
	private Long productStockId;// 库存id

	@Column
	private Long productAttributeId;// 属性id

	@Column
	private String value;// 属性值
	
	@Column
	private Integer number = 0; // 用于记录是第一位还是第二位
	
	@Column
	private Long productId; 

	
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductStockId() {
		return productStockId;
	}

	public void setProductStockId(Long productStockId) {
		this.productStockId = productStockId;
	}

	public Long getProductAttributeId() {
		return productAttributeId;
	}

	public void setProductAttributeId(Long productAttributeId) {
		this.productAttributeId = productAttributeId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ProductStockAttributeValue [id=" + id + ", productStockId="
				+ productStockId + ", productAttributeId=" + productAttributeId
				+ ", value=" + value + ", number=" + number + ", productId="
				+ productId + "]";
	}


	
}
