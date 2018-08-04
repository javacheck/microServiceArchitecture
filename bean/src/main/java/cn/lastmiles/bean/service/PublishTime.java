package cn.lastmiles.bean.service;

import java.util.Date;

/**
 * 服务预约时间段
 * 
 * @author HEQINGLANG
 *
 */
public class PublishTime {
	private Long id;//ID
	private Date startDate;//开始时间
	private Date endDate;//结束时间
	private Long publishId;//预约ID
	private Integer bookingNumber = 1;// 可以预约次数
	private Integer bookedNumber = 0;// 已经预约次数
	private Integer orderSort;// 排序
	
	public boolean booking;

	/**
	 * 是否可以预约
	 * @return
	 */
	public boolean isBooking() {
		return bookingNumber - bookedNumber > 0;
	}
	
	public void setBooking(boolean booking){
		this.booking = booking;
	}
	

	@Override
	public String toString() {
		return "PublishTime [id=" + id + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", publishId=" + publishId
				+ ", bookingNumber=" + bookingNumber + ", bookedNumber="
				+ bookedNumber + ", orderSort=" + orderSort + "]";
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Date getStartDate() {
		return startDate;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public Long getPublishId() {
		return publishId;
	}

	public void setPublishId(Long publishId) {
		this.publishId = publishId;
	}


	public Integer getBookingNumber() {
		return bookingNumber;
	}

	public void setBookingNumber(Integer bookingNumber) {
		this.bookingNumber = bookingNumber;
	}

	public Integer getBookedNumber() {
		return bookedNumber;
	}

	public void setBookedNumber(Integer bookedNumber) {
		this.bookedNumber = bookedNumber;
	}



	public Integer getOrderSort() {
		return orderSort;
	}


	public void setOrderSort(Integer orderSort) {
		this.orderSort = orderSort;
	}

}
