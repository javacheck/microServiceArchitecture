package cn.lastmiles.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.constant.Constants;

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
	private Account account;// 账户
	private Store store;// 商店
	private User user;// 会员
	private CashGift cashGift;
	
	// 订单状态 0待付款 1待确认(已支付,线下没有支付) 2商家取消 3已完成,4待签收(商家已发货),5待发货
	// (商家确认),7退换货，8待评价(已签收)，9缺货,10用户取消 ，11超时未支付，系统取消,12退款中，13已退款，14退款失败，15刷卡撤销
	//99交易关闭,100支付中（线下）
	private Integer status;
	// 商品总价
	private Double price;
	private Date createdTime;
	// 支付方式 0 支付宝 1 微信支付 2 刷卡 3 现金，4银联在线,7货到付款，8全部平台余额支付，9会员卡支付
	private Integer paymentMode;
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();

	private Double discount = 10.0;// 订单折扣

	private Double actualPrice = 0d;// 实际支付价格
	private String storeName;

	private Integer source;// 订单来源 常量ORDER_SOURCE_APP = 0;// 从APP获取
							// ORDER_SOURCE_DEVICES = 1;// 从终端获取
	private Long addressId;
	private String message;
	private Double sumPrice;// 订单总价price+shipAmount
	
	private Double refundPrice = 0D;// 退款金额
	private Integer settlement = Constants.Order.SETTLEMENT_STATUS_NOT;

	// 收货地址
	private Address address;
	// 订单评价
	private String evaluate;

	// 送货方式
	private String shipType;
	private Integer shipTypeId;
	// 需要送货费用
	private Double shipAmount;
	//减免的送货费用 
	private Double freeShipAmount = 0D;
	
	private Long roomId;
	
	// 配送时间
	private String shipTime;
	// 使用余额
	private Double balance = 0D;
	// 优惠卷id
	private Long cashGiftId;
	// 优惠卷说明
	private String cashGiftDesc;
	private String printAddress; // 打印地址(小票打印功能) -- 来自t_user_address表

	private String storeMobile; // 商家手机号(小票打印功能) -- 来自t_store表

	private String storePhone; // 商家联系电话(小票打印功能) -- 来自t_store表

	private String userName; // 用户名称(APP订单) -- 来自t_user表

	private String userMobile; // 用户联系电话(APP订单) -- 来自t_user表

	// 是否已经打印 0未打印，1打印
	private Integer printed = Constants.Order.NOTPRINTSTATUS;

	private Integer payType;// 0在线支付，1货到付款，2两种方式
	
	private Double cashPrice = 0D;//现金
	
	private Double weixinPay = 0D;//微信支付金额
	
	private Double alipay = 0D;//支付宝支付金额
	
	private Double bankcardPay = 0D;//银行卡支付金额
	
	private Double userCardPay = 0D;//会员卡支付金额
	
	//使用积分兑换支付
	private Double point = 0D;
	
	private Long firstOrderId;//首单ID
	
	private Long fullSubtractId;//满减id
	
	private Double promotionPrice;//优惠金额优惠金额=优惠活动总额  优惠金额不含余额支付，但运费减免计入到优惠金额里 price - actualPrice - balance + freeShipAmount 
	
	private Integer duration = 0;//消费时长
	
	private Integer haveReturnGoods = 0; //是不是有退货
	
	private List<OrderReturnGoods> orderReturnGoods = new ArrayList<OrderReturnGoods>();; // 退货表
	
	private String memo;//会员备注
	private Double change = 0d;//会员卡支付找零
	private Double useBalance = 0d;//使用的余额
	
	private Double getPoint;//这个订单获取的积分
	private Double remainPoint;//这个订单支付的时候剩余的积分
	private UserCard userCard;
	
	private String fullSubstract;
	
	private Double fullSubstractAmount = 0D;//满减金额
	private Double promotionAmount = 0D;//优惠券金额
	private Double promotionDiscount = 10d;//优惠券折扣
	private Double reductionAmount = 0D;//减免金额
	private Double reductionDiscount = 10D;//减免折扣
	private String fullSubstractCondition;//满减条件 100:20 表示满100减20
	
	private Double sumReturnGoodsPrice = 0D; // 总退货金额(2016.09.13)

	public List<OrderReturnGoods> getOrderReturnGoods() {
		return orderReturnGoods;
	}

	public Double getSumReturnGoodsPrice() {
		return sumReturnGoodsPrice;
	}

	public void setSumReturnGoodsPrice(Double sumReturnGoodsPrice) {
		this.sumReturnGoodsPrice = sumReturnGoodsPrice;
	}

	public void setOrderReturnGoods(List<OrderReturnGoods> orderReturnGoods) {
		this.orderReturnGoods = orderReturnGoods;
	}

	public Integer getHaveReturnGoods() {
		return haveReturnGoods;
	}

	public void setHaveReturnGoods(Integer haveReturnGoods) {
		this.haveReturnGoods = haveReturnGoods;
	}

	public CashGift getCashGift() {
		return cashGift;
	}

	public void setCashGift(CashGift cashGift) {
		this.cashGift = cashGift;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getStoreMobile() {
		return storeMobile;
	}

	public void setStoreMobile(String storeMobile) {
		this.storeMobile = storeMobile;
	}

	public String getStorePhone() {
		return storePhone;
	}

	public void setStorePhone(String storePhone) {
		this.storePhone = storePhone;
	}

	public Order() {
		super();
	}

	public void orderItemsAdd(OrderItem orderItem) {
		this.orderItems.add(orderItem);
	}

	public String getPrintAddress() {
		return printAddress;
	}

	public void setPrintAddress(String printAddress) {
		this.printAddress = printAddress;
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

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(Double actualPrice) {
		this.actualPrice = actualPrice;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public int getOrderItemAmount() {
		if (orderItems == null) {
			return 0;
		} else {
			int total = 0;
			for (OrderItem item : orderItems) {
				
				if (item!=null&&item.getAmount()!=null) {
					total = total + item.getAmount().intValue();
					
				}
			}
			return total;
		}
	}

	public Integer getSettlement() {
		return settlement;
	}

	public void setSettlement(Integer settlement) {
		this.settlement = settlement;
	}

	public String getEvaluate() {
		return evaluate;
	}

	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
	}

	public String getShipType() {
		return shipType;
	}

	public void setShipType(String shipType) {
		this.shipType = shipType;
	}

	public Double getShipAmount() {
		return shipAmount;
	}

	public void setShipAmount(Double shipAmount) {
		this.shipAmount = shipAmount;
	}

	public String getShipTime() {
		return shipTime;
	}

	public void setShipTime(String shipTime) {
		this.shipTime = shipTime;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Long getCashGiftId() {
		return cashGiftId;
	}

	public void setCashGiftId(Long cashGiftId) {
		this.cashGiftId = cashGiftId;
	}

	public Integer getPrinted() {
		return printed;
	}

	public void setPrinted(Integer printed) {
		this.printed = printed;
	}

	public String getCashGiftDesc() {
		return cashGiftDesc;
	}

	public void setCashGiftDesc(String cashGiftDesc) {
		this.cashGiftDesc = cashGiftDesc;
	}

	public Integer getShipTypeId() {
		return shipTypeId;
	}

	public void setShipTypeId(Integer shipTypeId) {
		this.shipTypeId = shipTypeId;
	}

	public Double getSumPrice() {
		return sumPrice;
	}

	public void setSumPrice(Double sumPrice) {
		this.sumPrice = sumPrice;
	}

	public String getStatusDes() {
		String returnStr = "";
		switch (this.getStatus().intValue()) {
		case Constants.Order.TYPE_NO_PAY:
			returnStr = "待付款";
			break;
		case Constants.Order.TYPE_CANCEL:
			returnStr = "订单取消";
			break;
		case Constants.Order.TYPE_CLOSED:
			returnStr = "已关闭";
			break;
		case Constants.Order.TYPE_FINISHED:
			returnStr = "已完成";
			break;
		case Constants.Order.TYPE_PAY:
			returnStr = this.source.intValue() == Constants.Order.ORDER_SOURCE_APP ? "待确认"
					: "已支付";
			break;
		case Constants.Order.TYPE_REFUND_FAIL:
			returnStr = "退款失败";
			break;
		case Constants.Order.TYPE_REFUND_ING:
			returnStr = "退款中";
			break;
		case Constants.Order.TYPE_REFUND_SUCCESS:
			returnStr = "已退款";
			break;
		case Constants.Order.TYPE_REJECTED:
			returnStr = "退换货";
			break;
		case Constants.Order.TYPE_STORE_CONFIRM:
			returnStr = "未支付";
			break;
		case Constants.Order.TYPE_SYS_CANCEL:
			returnStr = "系统超时取消";
			break;
		case Constants.Order.TYPE_USER_CANCEL:
			returnStr = "已取消";
			break;
		case Constants.Order.TYPE_WAITING_DELIVER:
			returnStr = "待发货";
			break;
		case Constants.Order.TYPE_WAITING_EVALUATE:
			returnStr = "待评价";
			break;
		case Constants.Order.TYPE_WAITING_RECEIVING:
			returnStr = "待签收";
			break;
		case Constants.Order.TYPE_WAITING_STOCKOUT:
			returnStr = "缺货";
			break;
		case Constants.Order.TYPE_BANK_CARD_REVOKE:
			returnStr = "刷卡撤销";
			break;
		case Constants.Order.TYPE_PAYING:
			returnStr = "支付中";
			break;
		default:
			break;
		}
		return returnStr;
	}

	public boolean isLineOffPayment() {// 是否为线下支付
		if (paymentMode != null
				&& paymentMode.intValue() == Constants.Order.PAYMENT_LINE_PAY) {
			return true;
		}
		return false;
	}

	public boolean isLineOnPayment() {// 是否为线下支付
		return !isLineOffPayment();
	}

	/**
	 * 支付方式
	 * 
	 * @return
	 */
	public String getPaymentDesc() {
		if (actualPrice != null && actualPrice.doubleValue() == 0.0d) {
			if (balance != null && balance.doubleValue() > 0d) {
				return "余额支付";
			} else {
				return "无需支付";
			}
		} else {
			if (paymentMode == null) {
				return "";
			} else if (paymentMode.intValue() == Constants.Order.PAYMENT_LINE_PAY) {
				return "线下支付";
			} else if (paymentMode.intValue() == Constants.Order.PAYMENT_APP_BALANCE_PAY) {
				return "余额支付";
			} else if (paymentMode.intValue() == Constants.Order.PAYMENT_USER_CARD_BALANCE_PAY){
				return "会员卡支付";
			}else {
				return "线上支付";
			}
		}
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Double getCashPrice() {
		return cashPrice;
	}

	public void setCashPrice(Double cashPrice) {
		this.cashPrice = cashPrice;
	}

	public Long getFirstOrderId() {
		return firstOrderId;
	}

	public void setFirstOrderId(Long firstOrderId) {
		this.firstOrderId = firstOrderId;
	}

	public Long getFullSubtractId() {
		return fullSubtractId;
	}

	public void setFullSubtractId(Long fullSubtractId) {
		this.fullSubtractId = fullSubtractId;
	}

	public Double getPromotionPrice() {
		return promotionPrice;
	}

	public void setPromotionPrice(Double promotionPrice) {
		this.promotionPrice = promotionPrice;
	}

	public Double getFreeShipAmount() {
		return freeShipAmount;
	}

	public void setFreeShipAmount(Double freeShipAmount) {
		this.freeShipAmount = freeShipAmount;
	}

	public Double getPoint() {
		return point == null ? 0 : point;
	}

	public void setPoint(Double point) {
		this.point = point;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public Double getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(Double refundPrice) {
		this.refundPrice = refundPrice;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Double getWeixinPay() {
		return weixinPay;
	}

	public void setWeixinPay(Double weixinPay) {
		this.weixinPay = weixinPay;
	}

	public Double getAlipay() {
		return alipay;
	}

	public void setAlipay(Double alipay) {
		this.alipay = alipay;
	}

	public Double getBankcardPay() {
		return bankcardPay;
	}

	public void setBankcardPay(Double bankcardPay) {
		this.bankcardPay = bankcardPay;
	}

	public Double getUserCardPay() {
		return userCardPay;
	}

	public void setUserCardPay(Double userCardPay) {
		this.userCardPay = userCardPay;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Double getGetPoint() {
		return getPoint;
	}

	public void setGetPoint(Double getPoint) {
		this.getPoint = getPoint;
	}

	public UserCard getUserCard() {
		return userCard;
	}

	public void setUserCard(UserCard userCard) {
		this.userCard = userCard;
	}
	
	public Double getRemainPoint() {
		return remainPoint;
	}

	public void setRemainPoint(Double remainPoint) {
		this.remainPoint = remainPoint;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", accountId=" + accountId + ", storeId="
				+ storeId + ", userId=" + userId + ", account=" + account
				+ ", store=" + store + ", user=" + user + ", cashGift="
				+ cashGift + ", status=" + status + ", price=" + price
				+ ", createdTime=" + createdTime + ", paymentMode="
				+ paymentMode + ", orderItems=" + orderItems + ", discount="
				+ discount + ", actualPrice=" + actualPrice + ", storeName="
				+ storeName + ", source=" + source + ", addressId=" + addressId
				+ ", message=" + message + ", sumPrice=" + sumPrice
				+ ", refundPrice=" + refundPrice + ", settlement=" + settlement
				+ ", address=" + address + ", evaluate=" + evaluate
				+ ", shipType=" + shipType + ", shipTypeId=" + shipTypeId
				+ ", shipAmount=" + shipAmount + ", freeShipAmount="
				+ freeShipAmount + ", roomId=" + roomId + ", shipTime="
				+ shipTime + ", balance=" + balance + ", cashGiftId="
				+ cashGiftId + ", cashGiftDesc=" + cashGiftDesc
				+ ", printAddress=" + printAddress + ", storeMobile="
				+ storeMobile + ", storePhone=" + storePhone + ", userName="
				+ userName + ", userMobile=" + userMobile + ", printed="
				+ printed + ", payType=" + payType + ", cashPrice=" + cashPrice
				+ ", weixinPay=" + weixinPay + ", alipay=" + alipay
				+ ", bankcardPay=" + bankcardPay + ", userCardPay="
				+ userCardPay + ", point=" + point + ", firstOrderId="
				+ firstOrderId + ", fullSubtractId=" + fullSubtractId
				+ ", promotionPrice=" + promotionPrice + ", duration="
				+ duration + ", haveReturnGoods=" + haveReturnGoods
				+ ", orderReturnGoods=" + orderReturnGoods + ", memo=" + memo
				+ ", change=" + change + ", useBalance=" + useBalance
				+ ", getPoint=" + getPoint + ", remainPoint=" + remainPoint
				+ ", userCard=" + userCard + ", fullSubstract=" + fullSubstract
				+ ", sumReturnGoodsPrice=" + sumReturnGoodsPrice + "]";
	}

	public Double getChange() {
		if (change == null){
			change = 0d;
		}
		return change;
	}

	public void setChange(Double change) {
		this.change = change;
	}

	public String getFullSubstract() {
		return fullSubstract;
	}

	public void setFullSubstract(String fullSubstract) {
		this.fullSubstract = fullSubstract;
	}

	public Double getUseBalance() {
		if (paymentMode != null && paymentMode.intValue() == 9 && status.intValue() == 1){
			if (NumberUtils.subtract(NumberUtils.subtract(cashPrice, change),actualPrice )  == 0.0){
				useBalance = 0.0;
			}else if (NumberUtils.subtract(NumberUtils.subtract(cashPrice, change),actualPrice ) < 0.0){
				useBalance = NumberUtils.add(NumberUtils.subtract(actualPrice, cashPrice),change );
			}else {
				useBalance = NumberUtils.subtract(actualPrice,cashPrice);
			}
		}
		return useBalance;
	}

	public void setUseBalance(Double useBalance) {
		this.useBalance = useBalance;
	}

	public Double getFullSubstractAmount() {
		return fullSubstractAmount;
	}

	public void setFullSubstractAmount(Double fullSubstractAmount) {
		this.fullSubstractAmount = fullSubstractAmount;
	}

	public Double getPromotionAmount() {
		return promotionAmount;
	}

	public void setPromotionAmount(Double promotionAmount) {
		this.promotionAmount = promotionAmount;
	}

	public Double getPromotionDiscount() {
		return promotionDiscount;
	}

	public void setPromotionDiscount(Double promotionDiscount) {
		this.promotionDiscount = promotionDiscount;
	}

	public Double getReductionAmount() {
		return reductionAmount;
	}

	public void setReductionAmount(Double reductionAmount) {
		this.reductionAmount = reductionAmount;
	}

	public Double getReductionDiscount() {
		return reductionDiscount;
	}

	public void setReductionDiscount(Double reductionDiscount) {
		this.reductionDiscount = reductionDiscount;
	}

	public String getFullSubstractCondition() {
		return fullSubstractCondition;
	}

	public void setFullSubstractCondition(String fullSubstractCondition) {
		this.fullSubstractCondition = fullSubstractCondition;
	}
	
}