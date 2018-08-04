package cn.lastmiles.bean;

public class ReportSum {
	private Integer orderNumSum;//订单数合计
	private Double salesNumSum;//销售额合计
	private Double costPriceSum;//商品成本合计
	private Double grossProfit;//商品收入合计
	
	public Integer getOrderNumSum() {
		return orderNumSum;
	}
	public void setOrderNumSum(Integer orderNumSum) {
		this.orderNumSum = orderNumSum;
	}
	public Double getSalesNumSum() {
		return salesNumSum;
	}
	public void setSalesNumSum(Double salesNumSum) {
		this.salesNumSum = salesNumSum;
	}
	public Double getCostPriceSum() {
		return costPriceSum;
	}
	public void setCostPriceSum(Double costPriceSum) {
		this.costPriceSum = costPriceSum;
	}
	public Double getGrossProfit() {
		return grossProfit;
	}
	public void setGrossProfit(Double grossProfit) {
		this.grossProfit = grossProfit;
	}
	@Override
	public String toString() {
		return "ReportSalesSum [orderNumSum=" + orderNumSum + ", salesNumSum="
				+ salesNumSum + ", costPriceSum=" + costPriceSum
				+ ", grossProfit=" + grossProfit + "]";
	}
	
}
