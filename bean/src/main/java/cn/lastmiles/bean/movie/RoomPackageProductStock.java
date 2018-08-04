package cn.lastmiles.bean.movie;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 套餐商品关联表
 * 
 * @author hql
 *
 */
@Table(name = "t_movie_room_package_product_stock")
public class RoomPackageProductStock {
	@Column
	private Long roomPackageId;
	@Column
	private Long productStockId;
	@Column
	private Integer amount;//数量

	public Long getRoomPackageId() {
		return roomPackageId;
	}

	public void setRoomPackageId(Long roomPackageId) {
		this.roomPackageId = roomPackageId;
	}

	public Long getProductStockId() {
		return productStockId;
	}

	public void setProductStockId(Long productStockId) {
		this.productStockId = productStockId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
}
