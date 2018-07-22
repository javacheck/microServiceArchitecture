package cn.self.cloud.bean;

/**
 * 订单项
 * 
 * @author hql
 *
 */
public class OrderItem {
	private Long orderId;
	
	private Long stockId;
	
	private ProductStock productStock;
	// 数量
	private Integer amount;
	// 单价格
	private Double price;
	
	//商品分类Id
	private Long categoryId;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getStockId() {
		return stockId;
	}

	public void setStockId(Long stockId) {
		this.stockId = stockId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public ProductStock getProductStock() {
		return productStock;
	}

	public void setProductStock(ProductStock productStock) {
		this.productStock = productStock;
	}

	@Override
	public String toString() {
		return "OrderItem [orderId=" + orderId + ", stockId=" + stockId
				+ ", productStock=" + productStock + ", amount=" + amount
				+ ", price=" + price + "]";
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	
}
