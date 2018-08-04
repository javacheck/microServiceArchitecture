package cn.lastmiles.bean.service;

import java.util.Date;

/**
 * 预约留言
 * 
 * @author HEQINGLANG
 *
 */
public class BookingMessage {
	private Long id;// ID
	private Long bookingId;// 预约ID
	private Long userId;// 会员ID
	private Date createdTime;// 留言时间
	private String message;// 具体信息
	private Integer type; //0为卖家 1 为买家
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
