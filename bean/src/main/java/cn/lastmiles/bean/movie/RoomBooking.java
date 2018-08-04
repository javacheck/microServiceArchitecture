package cn.lastmiles.bean.movie;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.lastmiles.common.utils.ClassUtil;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.constant.Constants;

/**
 * 房间预定
 * 
 * @author hql
 *
 */
@Table(name = "t_movie_room_booking")
public class RoomBooking {
	@Id
	@Column
	private Long id;
	@Column
	private Long roomId;
	@Column
	private Date createdTime;//创建时间
	@Column
	private Date arrivalTime;// 到达时间
	@Column
	private Long dateSettingId;//预定时间段
	@Column
	private String contacts;// 预定联系人
	@Column
	private String phone;// 联系电话
	@Column
	private String memo;// 备注 100字以内
	@Column
	private Long accountId;// 操作人
	@Column
	private Integer status = Constants.RoomBooking.STATUS_BOOKING;// 0表示预定中，1表示已经开台，3表示已经取消.
	@Column
	private Date bookingDate;
	
	@Column
	private Date reserveDate; // 预定时间(2016-04-18 PM 19:26)
	@Column
	private Double reserveDuration; // 预定时长(2016-04-18 PM 19:26)，单位：小时
	@Column
	private Date reserveEndDate; // 预定时间(2016-04-18 PM 19:26)
	
	private Room room;
	
	private String dateSettingName;
	
	private String accountMobile;
	
	@Column
	private Long storeId;
	
	private String storeName;

	public Date getReserveEndDate() {
		return reserveEndDate;
	}

	public void setReserveEndDate(Date reserveEndDate) {
		this.reserveEndDate = reserveEndDate;
	}

	public Date getReserveDate() {
		return reserveDate;
	}
	public Long getReserveDateLong() {
		return reserveDate==null?0l:(reserveDate.getTime()- new Date().getTime())/1000/60;
	}

	public void setReserveDate(Date reserveDate) {
		this.reserveDate = reserveDate;
	}

	public Double getReserveDuration() {
		return reserveDuration;
	}

	public void setReserveDuration(Double reserveDuration) {
		this.reserveDuration = reserveDuration;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public Long getDateSettingId() {
		return dateSettingId;
	}

	public void setDateSettingId(Long dateSettingId) {
		this.dateSettingId = dateSettingId;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	public String toString(){
        return ClassUtil.getFields(this).toString();
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public String getDateSettingName() {
		return dateSettingName;
	}

	public void setDateSettingName(String dateSettingName) {
		this.dateSettingName = dateSettingName;
	}

	public String getAccountMobile() {
		return accountMobile;
	}

	public void setAccountMobile(String accountMobile) {
		this.accountMobile = accountMobile;
	}

	public Date getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
}
