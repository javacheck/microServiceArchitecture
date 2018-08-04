package cn.lastmiles.bean;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name="t_order_promotion")
public class OrderPromotion {
	@Column
	private Long orderId;
	
	@Column
	private Long cashGiftId;
	
	@Column
	private Long promotionId;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getCashGiftId() {
		return cashGiftId;
	}

	public void setCashGiftId(Long cashGiftId) {
		this.cashGiftId = cashGiftId;
	}

	public Long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}
}
