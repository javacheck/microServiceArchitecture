package cn.self.cloud.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单表
 * 
 * @author hql
 *
 */
public class Order implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2233076857781548498L;
	
	private Long id;
	private Long accountId;
	private Long storeId;
	private Long userId;
	private Account account;//账户
	private Store store;//商店
	private User user;//会员
	//订单状态 0 表示成功 1 待发货  2 已取消 
	private Integer status;
	// 总价
	private Double price;
	private Date createdTime;
	
	private Integer paymentMode ;	
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
	
	
	public Order(){
		super();
	}
	
	public void orderItemsAdd(OrderItem orderItem){
		this.orderItems.add(orderItem);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", accountId=" + accountId + ", storeId="
				+ storeId + ", price=" + price + ", createdTime=" + createdTime
				+ ", orderItems=" + orderItems + "]";
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(Integer paymentMode) {
		this.paymentMode = paymentMode;
	}
	
}
