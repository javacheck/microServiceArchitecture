/**
 * createDate : 2016年4月28日上午10:31:35
 */
package cn.lastmiles.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.lastmiles.common.utils.StringUtils;

@Table(name="t_order_returnGoods")
public class OrderReturnGoods {
	@Id
	@Column(name="id")
	private Long id; // 退货ID（与订单表关联）
	
	@Column(name="createdTime")
	private Date createdTime; // 退货时间
	
	@Column(name="returnType")
	private Integer returnType = 0; // 退货方式   0： 退储值  1 ： 退现金
	
	@Column(name="returnNumber")
	private Double returnNumber = 0D; // 退货数量
	
	@Column(name="returnPrice")
	private Double returnPrice = 0D; // 退款金额
	
	@Column(name="returnPoint")
	private Integer returnPoint = 0; // 减扣积分
	
	@Column(name="returnReason")
	private String returnReason = "其他原因"; // 退款原因
	
	@Column(name="orderId")
	private Long orderId; // 订单ID（与订单ID相关联）
	
	private Long userId ; // 用户ID
	
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	public Integer getReturnType() {
		return returnType;
	}
	public void setReturnType(Integer returnType) {
		this.returnType = returnType;
	}

	public Double getReturnNumber() {
		return returnNumber;
	}
	public void setReturnNumber(Double returnNumber) {
		this.returnNumber = returnNumber;
	}
	public Double getReturnPrice() {
		return returnPrice;
	}
	public void setReturnPrice(Double returnPrice) {
		this.returnPrice = returnPrice;
	}
	public Integer getReturnPoint() {
		return returnPoint;
	}
	public void setReturnPoint(Integer returnPoint) {
		this.returnPoint = returnPoint;
	}
	public String getReturnReason() {
		return returnReason;
	}
	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	@Override
	public String toString() {
		return "OrderReturnGoods [id=" + id + ", createdTime=" + createdTime
				+ ", returnType=" + returnType + ", returnNumber="
				+ returnNumber + ", returnPrice=" + returnPrice
				+ ", returnPoint=" + returnPoint + ", returnReason="
				+ returnReason + ", orderId=" + orderId + ", userId=" + userId
				+ "]";
	}

}