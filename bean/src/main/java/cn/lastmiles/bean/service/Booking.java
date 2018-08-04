package cn.lastmiles.bean.service;

import java.util.Date;

import cn.lastmiles.bean.User;

/**
 * 服务预约
 * 
 * @author HEQINGLANG
 *
 */
public class Booking {
	
	@Override
	public String toString() {
		return "Booking [id=" + id + ", createdTime=" + createdTime
				+ ", publishId=" + publishId + ", userId=" + userId
				+ ", publishTimeId=" + publishTimeId + ", confirmTime="
				+ confirmTime + ", status=" + status + ", payStatus="
				+ payStatus + ", addressId=" + addressId + ", memo=" + memo
				+ ", user=" + user + ", reply=" + reply + ", userName="
				+ userName + ", address=" + address + ", phone=" + phone
				+ ", imageID=" + imageID + ", publishTime=" + publishTime
				+ ", serviceCompletionTime=" + serviceCompletionTime
				+ ", orderCompleteTime=" + orderCompleteTime + ", cancelTime="
				+ cancelTime + ", cancelReason=" + cancelReason
				+ ", cancelUserId=" + cancelUserId + "]";
	}

	private Long id;// ID
	private Date createdTime;// 创建时间
	private Long publishId;// 发布ID
	private Long userId;// 会员ID
	private Long publishTimeId;// 预约时间ID
	private Date confirmTime;// 确认时间
	private Integer status;// 0表示确认中(这个时候publish的可预约数-1)，1表示已经确认，2表示取消(这个时候publish的可预约数+1)，3表示交易完成
	private Integer payStatus = 0;// 线上支付状态 0未支付，1支付
	
	private Long addressId;//地址ID
	
	private String memo;//备注
	
	private User user;//用户
	
	private String reply;//回复
	private String userName;//会员名称
	private String address;//地址
	private String phone;//电话
	
	private String imageID; // 图片
	
	private PublishTime publishTime ;
	
	private Date serviceCompletionTime;
	
	private Date orderCompleteTime;
	
	private Date cancelTime;
	
	private String cancelReason;
	
	private Long  cancelUserId;

	
	public String getImageID() {
		return imageID;
	}

	public void setImageID(String imageID) {
		this.imageID = imageID;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Long getPublishId() {
		return publishId;
	}

	public void setPublishId(Long publishId) {
		this.publishId = publishId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getPublishTimeId() {
		return publishTimeId;
	}

	public void setPublishTimeId(Long publishTimeId) {
		this.publishTimeId = publishTimeId;
	}

	public Date getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public PublishTime getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(PublishTime publishTime) {
		this.publishTime = publishTime;
	}

	public Date getServiceCompletionTime() {
		return serviceCompletionTime;
	}

	public void setServiceCompletionTime(Date serviceCompletionTime) {
		this.serviceCompletionTime = serviceCompletionTime;
	}

	public Date getOrderCompleteTime() {
		return orderCompleteTime;
	}

	public void setOrderCompleteTime(Date orderCompleteTime) {
		this.orderCompleteTime = orderCompleteTime;
	}

	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}


	public Long getCancelUserId() {
		return cancelUserId;
	}

	public void setCancelUserId(Long cancelUserId) {
		this.cancelUserId = cancelUserId;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

}
