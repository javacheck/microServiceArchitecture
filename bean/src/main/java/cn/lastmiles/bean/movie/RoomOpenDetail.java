package cn.lastmiles.bean.movie;

import javax.persistence.Column;
import javax.persistence.Table;

import cn.lastmiles.common.utils.ClassUtil;

/**
 * 开房信息详情
 * 
 * @author hql
 *
 */
@Table(name = "t_movie_room_open_detail")
public class RoomOpenDetail {
	@Column
	private Long roomOpenId;

	@Column
	private Long productStockId;
	
	@Column
	private String name;
	
	private Double actualPrice;

	@Column
	private Double price;// 单价

	private Double number;// 数量
	
	private Double total;//小计
	
	private Integer status;//状态
	
	private Integer type;
	
	private Double refundPrice;
	
	private Integer duration;

	public Long getRoomOpenId() {
		return roomOpenId;
	}

	public void setRoomOpenId(Long roomOpenId) {
		this.roomOpenId = roomOpenId;
	}

	public Long getProductStockId() {
		return productStockId;
	}

	public void setProductStockId(Long productStockId) {
		this.productStockId = productStockId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getNumber() {
		return number;
	}

	public void setNumber(Double number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String toString(){
        return ClassUtil.getFields(this).toString();
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Double getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(Double refundPrice) {
		this.refundPrice = refundPrice;
	}

	public Double getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(Double actualPrice) {
		this.actualPrice = actualPrice;
	}
}
