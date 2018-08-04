package cn.lastmiles.bean;

import java.util.Date;

/**
 * 用户平台余额记录
 * @author hql
 *
 */
public class UserBalanceRecord {
	
	private Long userId;//用户ID
	private Date createdTime;//使用时间
	private Double amount;//使用金额
	private String memo;//备注
	private Integer type;//类型
	private Long orderId;//使用的订单ID
	
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
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	


}
