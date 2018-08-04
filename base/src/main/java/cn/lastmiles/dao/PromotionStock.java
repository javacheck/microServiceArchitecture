package cn.lastmiles.dao;

import java.util.List;

public class PromotionStock {
	private Double discount;
	private Long promotionId;
	private List<Long> stockIds;

	public Long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}

	public List<Long> getStockIds() {
		return stockIds;
	}

	public void setStockIds(List<Long> stockIds) {
		this.stockIds = stockIds;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	@Override
	public String toString() {
		return "PromotionStock [discount=" + discount + ", promotionId="
				+ promotionId + ", stockIds=" + stockIds + "]";
	}
}
