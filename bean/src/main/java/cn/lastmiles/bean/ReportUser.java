package cn.lastmiles.bean;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 会员统计数据
 * 
 * @author hql
 *
 */
@Table(name = "t_report_user")
public class ReportUser {
	@Column
	@Id
	private Long id;
	
	@Column
	private Long storeId;

	@Column
	private Date reportDate;

	@Column
	private Integer userNum = 0;// 当天用户数量 

	@Column
	private Double consumption = 0D;// 当天消费金额

	@Column
	private Double recharge = 0D;// 当天充值金额
	
	@Column
	private Integer totalUserNum = 0 ;//累计会员
	
	@Column
	private Double totalConsumption = 0D;// 累计消费金额

	@Column
	private Double totalRecharge = 0D;// 累计充值金额
	
	@Column
	private Double totalBalance = 0D;//会员总余额
	
	@Column
	private Double totalPoint = 0D;//会员总积分
	
	private Integer totalUserNumSum = 0;//会员总和
	private Double rechargeSum = 0D;//充值总和
	private Double consumptionSum = 0D ;//消费总和
	private String storeName;//商店名称
	private Integer storeSum = 0;//商店总数
	
	private String dateString;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}


	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public Integer getUserNum() {
		return userNum;
	}

	public void setUserNum(Integer userNum) {
		this.userNum = userNum;
	}

	public Double getConsumption() {
		return consumption;
	}

	public void setConsumption(Double consumption) {
		this.consumption = consumption;
	}

	public Double getRecharge() {
		return recharge;
	}

	public void setRecharge(Double recharge) {
		this.recharge = recharge;
	}

	public Integer getTotalUserNum() {
		return totalUserNum;
	}

	public void setTotalUserNum(Integer totalUserNum) {
		this.totalUserNum = totalUserNum;
	}

	public Double getTotalConsumption() {
		return totalConsumption; 
	}

	public void setTotalConsumption(Double totalConsumption) {
		this.totalConsumption = totalConsumption;
	}

	public Double getTotalRecharge() {
		return totalRecharge;
	}

	public void setTotalRecharge(Double totalRecharge) {
		this.totalRecharge = totalRecharge;
	}

	public Double getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(Double totalBalance) {
		this.totalBalance = totalBalance;
	}

	public Double getTotalPoint() {
		if( null != totalPoint ){
			totalPoint = BigDecimal.valueOf(totalPoint).setScale(0,BigDecimal.ROUND_FLOOR).doubleValue();			
		}
		return totalPoint;
	}

	public void setTotalPoint(Double totalPoint) {
		this.totalPoint = totalPoint;
	}

	public Integer getTotalUserNumSum() {
		if(totalUserNumSum==null){
			totalUserNumSum=0;
		}
		return totalUserNumSum;
	}

	public void setTotalUserNumSum(Integer totalUserNumSum) {
		this.totalUserNumSum = totalUserNumSum;
	}

	public Double getRechargeSum() {
		if(rechargeSum==null){
			rechargeSum=0D;
		}
		return rechargeSum;
	}

	public void setRechargeSum(Double rechargeSum) {
		this.rechargeSum = rechargeSum;
	}

	public Double getConsumptionSum() {
		if(consumptionSum==null){
			consumptionSum=0D;
		}
		return consumptionSum;
	}

	public void setConsumptionSum(Double consumptionSum) {
		this.consumptionSum = consumptionSum;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Integer getStoreSum() {
		return storeSum;
	}

	public void setStoreSum(Integer storeSum) {
		this.storeSum = storeSum;
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	@Override
	public String toString() {
		return "ReportUser [id=" + id + ", storeId=" + storeId
				+ ", reportDate=" + reportDate + ", userNum=" + userNum
				+ ", consumption=" + consumption + ", recharge=" + recharge
				+ ", totalUserNum=" + totalUserNum + ", totalConsumption="
				+ totalConsumption + ", totalRecharge=" + totalRecharge
				+ ", totalBalance=" + totalBalance + ", totalPoint="
				+ totalPoint + ", totalUserNumSum=" + totalUserNumSum
				+ ", rechargeSum=" + rechargeSum + ", consumptionSum="
				+ consumptionSum + ", storeName=" + storeName + ", storeSum="
				+ storeSum + ", dateString=" + dateString + "]";
	}
	
	
	
}
