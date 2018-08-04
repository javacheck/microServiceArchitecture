package cn.lastmiles.bean;

import java.util.Date;

public class OrderHandle {
	private Long orderId;//订单ID
	private Integer status;//订单状态
	private Date handleTime;//创建时间
	private String memo;//备注
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getHandleTime() {
		return handleTime;
	}
	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	@Override
	public String toString() {
		return "OrderHandle [orderId=" + orderId + ", status=" + status
				+ ", handleTime=" + handleTime + ", memo=" + memo + "]";
	}
	
	
}
