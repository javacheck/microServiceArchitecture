package cn.lastmiles.bean;

public class ReportStock {
	private String storeName;//商店名称
	private String name;//商品名称
	private String barCode;//商品条码
	private String categoryName;//商品分类
	private Double stock;//库存数量
	private Double costPrice;//进货价(平均)
	private Double price;//销售单价
	private Double totalCostPrice;//库存成本
	private Double preSales;//预计销售额
	private Double preGrossProfit;//预计毛利
	
	
	private Double stockSum;//库存总数量
	private Double totalCostPriceSum;//库存总成本
	private Double preSalesSum;//预计总销售额
	private Double preGrossProfitSum;//预计总毛利
	
	private Integer nameSum;//商品总数
	private String productName;
	private String attributeValues;
	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	

	public Double getStock() {
		return stock;
	}

	public void setStock(Double stock) {
		this.stock = stock;
	}

	public Double getCostPrice() {
		if(costPrice==null){
			costPrice=0D;
		}
		return costPrice;
	}

	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}

	public Double getPrice() {
		if(price==null){
			price=0D;
		}
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getTotalCostPrice() {
		if(totalCostPrice==null){
			totalCostPrice=0D;
		}
		return totalCostPrice;
	}

	public void setTotalCostPrice(Double totalCostPrice) {
		this.totalCostPrice = totalCostPrice;
	}

	public Double getPreSales() {
		if(preSales==null){
			preSales=0D;
		}
		return preSales;
	}

	public void setPreSales(Double preSales) {
		this.preSales = preSales;
	}

	public Double getPreGrossProfit() {
		if(preGrossProfit==null){
			preGrossProfit=0D;
		}
		return preGrossProfit;
	}

	public void setPreGrossProfit(Double preGrossProfit) {
		this.preGrossProfit = preGrossProfit;
	}

	

	public Double getStockSum() {
		return stockSum;
	}

	public void setStockSum(Double stockSum) {
		this.stockSum = stockSum;
	}

	public Double getTotalCostPriceSum() {
		if(totalCostPriceSum==null){
			totalCostPriceSum=0D;
		}
		return totalCostPriceSum;
	}

	public void setTotalCostPriceSum(Double totalCostPriceSum) {
		this.totalCostPriceSum = totalCostPriceSum;
	}

	public Double getPreSalesSum() {
		if(preSalesSum==null){
			preSalesSum=0D;
		}
		return preSalesSum;
	}

	public void setPreSalesSum(Double preSalesSum) {
		this.preSalesSum = preSalesSum;
	}

	public Double getPreGrossProfitSum() {
		if(preGrossProfitSum==null){
			preGrossProfitSum=0D;
		}
		return preGrossProfitSum;
	}

	public void setPreGrossProfitSum(Double preGrossProfitSum) {
		this.preGrossProfitSum = preGrossProfitSum;
	}

	public Integer getNameSum() {
		return nameSum;
	}

	public void setNameSum(Integer nameSum) {
		this.nameSum = nameSum;
	}

	@Override
	public String toString() {
		return "ReportStock [storeName=" + storeName + ", name=" + name
				+ ", barCode=" + barCode + ", categoryName=" + categoryName
				+ ", stock=" + stock + ", costPrice=" + costPrice + ", price="
				+ price + ", totalCostPrice=" + totalCostPrice + ", preSales="
				+ preSales + ", preGrossProfit=" + preGrossProfit
				+ ", stockSum=" + stockSum + ", totalCostPriceSum="
				+ totalCostPriceSum + ", preSalesSum=" + preSalesSum
				+ ", preGrossProfitSum=" + preGrossProfitSum + ", nameSum="
				+ nameSum + "]";
	}
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getAttributeValues() {
		return attributeValues;
	}

	public void setAttributeValues(String attributeValues) {
		this.attributeValues = attributeValues;
	}
	
	
}
