package cn.lastmiles.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单退款
 * @author zhangpengcheng
 *
 */
public class OrderRefund implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8312439869879524418L;
	
	private Long id;
	private Long orderId;//订单ID
	private Integer status;//状态
	private Double totalPrice;//总价
	private Double refundPrice;//退还价格
	private Integer channel;//渠道
	private Date createdTime ;//创建时间
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Double getRefundPrice() {
		return refundPrice;
	}
	public void setRefundPrice(Double refundPrice) {
		this.refundPrice = refundPrice;
	}
	public Integer getChannel() {
		return channel;
	}
	public void setChannel(Integer channel) {
		this.channel = channel;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
}
