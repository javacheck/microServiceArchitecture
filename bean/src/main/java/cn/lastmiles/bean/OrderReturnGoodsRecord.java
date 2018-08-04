/**
 * createDate : 2016年4月29日下午4:00:33
 */
package cn.lastmiles.bean;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Table;

@Table(name="t_order_returnGoods_record")
public class OrderReturnGoodsRecord {
	@Column
	private Long returnGoodsId; // 退货的总记录ID(与主表关联的ID)
	
	@Column
	private Long stockId; // 退货商品ID
	
	@Column
	private Long orderId; // 退货订单
	
	@Column
	private Double returnNumber; // 商品退货数量
	
	@Column
	private Double returnPrice; // 商品退货金额
	
	@Column
	private Date createdTime; // 创建时间
	
	@Column
	private String productName; // 商品名称
	
	@Column
	private String barcode; // 商品条码
	
	@Column
	private String standard; // 规格
	
	@Column
	private Long categoryId; // 商品分类
	
	@Column
	private Double price ; // 销售价
	
	@Column
	private Integer type; // 退货方式
	
	@Column
	private Long storeId ; // 商家ID
	
	@Column
	private String operationName; // 操作人
	private String storeName;//商家名称
	private String categoryName;//分类名称

	public Long getCategoryId() {
		return categoryId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getReturnGoodsId() {
		return returnGoodsId;
	}

	public void setReturnGoodsId(Long returnGoodsId) {
		this.returnGoodsId = returnGoodsId;
	}

	public Long getStockId() {
		return stockId;
	}

	public void setStockId(Long stockId) {
		this.stockId = stockId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
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

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	
	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public String toString() {
		return "OrderReturnGoodsRecord [returnGoodsId=" + returnGoodsId
				+ ", stockId=" + stockId + ", orderId=" + orderId
				+ ", returnNumber=" + returnNumber + ", returnPrice="
				+ returnPrice + ", createdTime=" + createdTime
				+ ", productName=" + productName + ", barcode=" + barcode
				+ ", standard=" + standard + ", categoryId=" + categoryId
				+ ", price=" + price + ", type=" + type + ", storeId="
				+ storeId + ", operationName=" + operationName + "]";
	}

}