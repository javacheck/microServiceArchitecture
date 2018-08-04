package cn.lastmiles.bean;


import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 盘点详情
 * 
 * @author hql
 *
 */
@Table(name = "t_stock_check_detail")
public class StockCheckDetail {
	@Column
	private Long stockCheckId;

	@Column
	private Long storeId;

	@Column
	private String productName;

	@Column
	private String barCode;

	@Column
	private String categoryName;

	@Column
	private Integer stock;

	@Column
	private Integer checkedStock;

	@Column
	private String checkedName;

	@Column
	private String checkedTime;
	private String storeName;
	@Column
	private Integer inventoryProfit ;//盘盈
	@Column
	private Integer inventoryLoss;//盘亏
	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Long getStockCheckId() {
		return stockCheckId;
	}

	public void setStockCheckId(Long stockCheckId) {
		this.stockCheckId = stockCheckId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getCheckedStock() {
		return checkedStock;
	}

	public void setCheckedStock(Integer checkedStock) {
		this.checkedStock = checkedStock;
	}

	public String getCheckedName() {
		return checkedName;
	}

	public void setCheckedName(String checkedName) {
		this.checkedName = checkedName;
	}

	public String getCheckedTime() {
		return checkedTime;
	}

	public void setCheckedTime(String checkedTime) {
		this.checkedTime = checkedTime;
	}

	public Integer getInventoryProfit() {
		return inventoryProfit;
	}

	public void setInventoryProfit(Integer inventoryProfit) {
		this.inventoryProfit = inventoryProfit;
	}

	public Integer getInventoryLoss() {
		return inventoryLoss;
	}

	public void setInventoryLoss(Integer inventoryLoss) {
		this.inventoryLoss = inventoryLoss;
	}

	
}
