package cn.lastmiles.bean.movie;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.lastmiles.common.utils.ClassUtil;
import cn.lastmiles.constant.Constants;

/**
 * 开房信息
 * 
 * @author hql
 *
 */
@Table(name = "t_movie_room_open")
public class RoomOpen {
	@Id
	@Column
	private Long id;

	@Column
	private Long storeId;

	@Column
	private Long roomId;
	
	@Column
	private Long orderId;
	
	@Column
	private Integer duration;//时长，单位分钟

	@Column
	private Date startTime;

	@Column
	private Date endTime;// 根据房间或者套餐时长duration加上startTime

	@Column
	private Double totalAmount;// 消费总额 actualPrice
	
	@Column
	private Integer chargeType;//收费标准  1 免费，0 时间，2套餐  
	
	private Room room;
	
	private Double refundPrice;
	
	private Double price;
	
	public RoomOpen(){
		
	}

	public RoomOpen(Long id, Long storeId, Long roomId, Integer duration,
			Date startTime, Date endTime, Double totalAmount,
			Integer chargeType) {
		this.id = id;
		this.storeId = storeId;
		this.roomId = roomId;
		this.duration = duration;
		this.startTime = startTime;
		this.endTime = endTime;
		this.totalAmount = totalAmount;
		this.chargeType = chargeType;
	}



	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Integer getChargeType() {
		return chargeType;
	}

	public void setChargeType(Integer chargeType) {
		this.chargeType = chargeType;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public String toString(){
        return ClassUtil.getFields(this).toString();
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Double getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(Double refundPrice) {
		this.refundPrice = refundPrice;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
}
