package cn.lastmiles.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 交接记录
 * @author hql
 *
 */
@Table(name="t_work_record")
public class WorkRecord {
	@Id
	@Column
	private Long id;
	
	@Column
	private Long accountId;
	
	private String accountName;
	private String accountMobile;
	private Date currentDate;
	
	@Column
	private Long storeId;
	
	@Column
	private Date startDate = new Date();
	
	@Column
	private Date updateDate = new Date();
	
	@Column
	private Date endDate;
	
	@Column
	private String token;
	
	//销售额
	@Column
	private Double sales = 0d;//总额，包括订单和会员充值

	@Column
	private Integer totalNum = 0;//总数，包括会订单和会员充值
	
	@Column
	private Double wxPay  = 0d;//微信收入
	
	@Column
	private Double alipay = 0d;//支付宝收入
	
	@Column
	private Double cashPay = 0d;//现金收入
	
	@Column
	private Double bankCardPay = 0d;//刷卡收入
	
	@Column
	private Integer wxPayNum = 0;//微信笔数
	
	@Column
	private Integer alipayNum = 0;//支付宝笔数
	
	@Column
	private Integer cashPayNum = 0;//现金笔数
	
	@Column
	private Integer bankCardPayNum = 0;//刷卡笔数
	
	@Column
	private Integer userCardNum = 0;//订单会员卡支付数
	
	@Column
	private Double userCard = 0D;//会员卡支付    订单会员卡支付金额
	
	@Column
	private Integer orderNum = 0;//销售订单笔数
	
	@Column
	private Integer userCardRechargeNum = 0;//会员充值笔数
	
	@Column
	private Double orderSales = 0d;//销售订单总额
	
	@Column
	private Double userCardRechargeSales = 0d;//会员充值总额
	
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Double getSales() {
		return sales;
	}

	public void setSales(Double sales) {
		this.sales = sales;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Double getWxPay() {
		return wxPay;
	}

	public void setWxPay(Double wxPay) {
		this.wxPay = wxPay;
	}

	public Double getAlipay() {
		return alipay;
	}

	public void setAlipay(Double alipay) {
		this.alipay = alipay;
	}

	public Double getCashPay() {
		return cashPay;
	}

	public void setCashPay(Double cashPay) {
		this.cashPay = cashPay;
	}

	public Double getBankCardPay() {
		return bankCardPay;
	}

	public void setBankCardPay(Double bankCardPay) {
		this.bankCardPay = bankCardPay;
	}

	public Integer getWxPayNum() {
		return wxPayNum;
	}

	public void setWxPayNum(Integer wxPayNum) {
		this.wxPayNum = wxPayNum;
	}

	public Integer getAlipayNum() {
		return alipayNum;
	}

	public void setAlipayNum(Integer alipayNum) {
		this.alipayNum = alipayNum;
	}

	public Integer getCashPayNum() {
		return cashPayNum;
	}

	public void setCashPayNum(Integer cashPayNum) {
		this.cashPayNum = cashPayNum;
	}

	public Integer getBankCardPayNum() {
		return bankCardPayNum;
	}

	public void setBankCardPayNum(Integer bankCardPayNum) {
		this.bankCardPayNum = bankCardPayNum;
	}

	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	@Override
	public String toString() {
		return "WorkRecord [id=" + id + ", accountId=" + accountId
				+ ", storeId=" + storeId + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", token=" + token + ", sales="
				+ sales + ", wxPay=" + wxPay + ", alipay=" + alipay
				+ ", cashPay=" + cashPay + ", bankCardPay=" + bankCardPay
				+ ", wxPayNum=" + wxPayNum + ", alipayNum=" + alipayNum
				+ ", cashPayNum=" + cashPayNum + ", bankCardPayNum="
				+ bankCardPayNum + ", totalNum=" + totalNum + ", getId()="
				+ getId() + ", getAccountId()=" + getAccountId()
				+ ", getStoreId()=" + getStoreId() + ", getStartDate()="
				+ getStartDate() + ", getEndDate()=" + getEndDate()
				+ ", getSales()=" + getSales() + ", getToken()=" + getToken()
				+ ", getWxPay()=" + getWxPay() + ", getAlipay()=" + getAlipay()
				+ ", getCashPay()=" + getCashPay() + ", getBankCardPay()="
				+ getBankCardPay() + ", getWxPayNum()=" + getWxPayNum()
				+ ", getAlipayNum()=" + getAlipayNum() + ", getCashPayNum()="
				+ getCashPayNum() + ", getBankCardPayNum()="
				+ getBankCardPayNum() + ", getTotalNum()=" + getTotalNum()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountMobile() {
		return accountMobile;
	}

	public void setAccountMobile(String accountMobile) {
		this.accountMobile = accountMobile;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getUserCardNum() {
		return userCardNum;
	}

	public void setUserCardNum(Integer userCardNum) {
		this.userCardNum = userCardNum;
	}

	public Double getUserCard() {
		return userCard;
	}

	public void setUserCard(Double userCard) {
		this.userCard = userCard;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public Integer getUserCardRechargeNum() {
		return userCardRechargeNum;
	}

	public void setUserCardRechargeNum(Integer userCardRechargeNum) {
		this.userCardRechargeNum = userCardRechargeNum;
	}

	public Double getOrderSales() {
		return orderSales;
	}

	public void setOrderSales(Double orderSales) {
		this.orderSales = orderSales;
	}

	public Double getUserCardRechargeSales() {
		return userCardRechargeSales;
	}

	public void setUserCardRechargeSales(Double userCardRechargeSales) {
		this.userCardRechargeSales = userCardRechargeSales;
	}
}
