package cn.self.cloud.bean;

public class ProductImage {
	private String id;//图片ID
	private Long productStockId;//库存ID
	private String suffix;//图片后缀
	private Integer sort;//图片顺序

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getProductStockId() {
		return productStockId;
	}

	public void setProductStockId(Long productStockId) {
		this.productStockId = productStockId;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	
	
	
}
