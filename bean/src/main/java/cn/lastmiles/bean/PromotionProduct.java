package cn.lastmiles.bean;
/**
 * createDate : 2015年11月5日下午2:54:37
 */
public class PromotionProduct {
	private Long promotionId;
	private Long productStockId;
	
	public PromotionProduct() {
		super();
	}
	
	public PromotionProduct(Long promotionId, Long productStockId) {
		super();
		this.promotionId = promotionId;
		this.productStockId = productStockId;
	}
	
	public Long getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}
	public Long getProductStockId() {
		return productStockId;
	}
	public void setProductStockId(Long productStockId) {
		this.productStockId = productStockId;
	}
}