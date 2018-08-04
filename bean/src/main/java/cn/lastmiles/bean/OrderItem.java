package cn.lastmiles.bean;

import java.util.Date;

import cn.lastmiles.constant.Constants;

/**
 * 订单项
 * 
 * @author hql
 *
 */
public class OrderItem {
	private Long id;

	private Long orderId;

	private String name;// 商品名称

	private Long stockId;

	private Long roomId;// 关联的包间ID

	private Integer type = 1;// 0 代表房间 1代表 商品，2表示包间套餐

	private Double discount = 10d;

	private ProductStock productStock;
	// 数量
	private Double amount;
	// 单价格
	private Double price;

	private String productAttributeInfo; // 组拼后的商品库存属性(小票打印功能) -- 来自商品属性值表
	private String productName; // 商品名称(小票打印功能) -- 来自商品表
	private String productStockImageUrl; // 商品图片

	// -----------------------------
	private Double returnNumber = 0D; // 退货数量
	private Double returnPrice = 0D; // 退货金额
	// -----------------------------

	// 商品分类Id
	private Long categoryId;

	// 促销分类id
	private Long salesPromotionCategoryId;

	// 促销分类id
	private Long promotionId;

	private Long userId;
	private Date createdTime;

	private String realName;// 商品全名，真名
	private String afterSalesType;// 售后状态
	private Double afterSalesAmount;// 售后数量

	public String getAfterSalesType() {
		return afterSalesType;
	}

	public void setAfterSalesType(String afterSalesType) {
		this.afterSalesType = afterSalesType;
	}

	public Double getAfterSalesAmount() {
		return afterSalesAmount;
	}

	public void setAfterSalesAmount(Double afterSalesAmount) {
		this.afterSalesAmount = afterSalesAmount;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	private Integer status = Constants.OrderItem.STATUSNORMAL;// 0已取消，1正常 2代表未支付

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

	public String getProductStockImageUrl() {
		return productStockImageUrl;
	}

	public void setProductStockImageUrl(String productStockImageUrl) {
		this.productStockImageUrl = productStockImageUrl;
	}

	public String getProductAttributeInfo() {
		return productAttributeInfo;
	}

	public void setProductAttributeInfo(String productAttributeInfo) {
		this.productAttributeInfo = productAttributeInfo;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getStockId() {
		return stockId;
	}

	public void setStockId(Long stockId) {
		this.stockId = stockId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public ProductStock getProductStock() {
		return productStock;
	}

	public void setProductStock(ProductStock productStock) {
		this.productStock = productStock;
	}

	@Override
	public String toString() {
		return "OrderItem [id=" + id + ", orderId=" + orderId + ", name=" + name + ", stockId=" + stockId + ", roomId="
				+ roomId + ", type=" + type + ", discount=" + discount + ", productStock=" + productStock + ", amount="
				+ amount + ", price=" + price + ", productAttributeInfo=" + productAttributeInfo + ", productName="
				+ productName + ", productStockImageUrl=" + productStockImageUrl + ", returnNumber=" + returnNumber
				+ ", returnPrice=" + returnPrice + ", categoryId=" + categoryId + ", salesPromotionCategoryId="
				+ salesPromotionCategoryId + ", promotionId=" + promotionId + ", userId=" + userId + ", createdTime="
				+ createdTime + ", status=" + status + "]";
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Long getSalesPromotionCategoryId() {
		return salesPromotionCategoryId;
	}

	public void setSalesPromotionCategoryId(Long salesPromotionCategoryId) {
		this.salesPromotionCategoryId = salesPromotionCategoryId;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
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

	public Long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
