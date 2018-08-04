package cn.lastmiles.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "t_report_sales")
public class ReportSales {
	@Column
	private Date reportDate;//
	@Column
	private Integer orderNum;// 订单数量
	@Column
	private Double salesNum;// 销售额
	@Column
	private Double costPrice;// 成本
	@Column
	private Double pricePerCustomer;// 客单价 salesNum 除以 用户数
	@Column
	private Long storeId;
	@Column
	private Integer source;// 订单来源 常量ORDER_SOURCE_APP = 0;// 从APP获取//
							// ORDER_SOURCE_DEVICES = 1;// 从终端获取
	
	@Column
	private Double salesPrice;//商品销售价格
	@Column
	private Double  promotionsGrossProfit;//促销让利
	private Double actualGrossProfit;//实销毛利
	
	
	private Double grossProfit;//毛利
	private Double grossProfitSum;//毛利总额
	private String storeName;//商店名称
	private Integer orderNumSum;//订单总数
	private Double salesNumSum;//销售总额
	
	private Integer storeSum;//商店总数
	private String dateString;
	
	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public Double getSalesNum() {
		return salesNum;
	}

	public void setSalesNum(Double salesNum) {
		this.salesNum = salesNum;
	}

	public Double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}

	public Double getPricePerCustomer() {
		return pricePerCustomer;
	}

	public void setPricePerCustomer(Double pricePerCustomer) {
		this.pricePerCustomer = pricePerCustomer;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Integer getOrderNumSum() {
		if(orderNumSum==null){
			orderNumSum=0;
		}
		return orderNumSum;
	}

	public void setOrderNumSum(Integer orderNumSum) {
		this.orderNumSum = orderNumSum;
	}

	public Double getSalesNumSum() {
		if(salesNumSum==null){
			salesNumSum=0D;
		}
		return salesNumSum;
	}

	public void setSalesNumSum(Double salesNumSum) {
		this.salesNumSum = salesNumSum;
	}

	public Double getGrossProfit() {
		if(grossProfit==null){
			grossProfit=0D;
		}
		return grossProfit;
	}

	public void setGrossProfit(Double grossProfit) {
		this.grossProfit = grossProfit;
	}

	public Integer getStoreSum() {
		return storeSum;
	}

	public void setStoreSum(Integer storeSum) {
		this.storeSum = storeSum;
	}

	public Double getGrossProfitSum() {
		if(grossProfitSum==null){
			grossProfitSum=0D;
		}
		return grossProfitSum;
	}

	public void setGrossProfitSum(Double grossProfitSum) {
		this.grossProfitSum = grossProfitSum;
	}

	public Double getPromotionsGrossProfit() {
		if(promotionsGrossProfit==null){
			promotionsGrossProfit=0D;
		}
		return promotionsGrossProfit;
	}

	public void setPromotionsGrossProfit(Double promotionsGrossProfit) {
		this.promotionsGrossProfit = promotionsGrossProfit;
	}

	public Double getActualGrossProfit() {
		if(actualGrossProfit==null){
			actualGrossProfit=0D;
		}
		return actualGrossProfit;
	}

	public void setActualGrossProfit(Double actualGrossProfit) {
		this.actualGrossProfit = actualGrossProfit;
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public Double getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(Double salesPrice) {
		this.salesPrice = salesPrice;
	}

	
	
	
}
