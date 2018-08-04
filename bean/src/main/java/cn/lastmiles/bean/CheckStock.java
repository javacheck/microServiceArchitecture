package cn.lastmiles.bean;

import java.util.Date;

/**
 * 盘点
 * 
 * @author hql
 *
 */
public class CheckStock {
	//主键
	private Long stockId;
	private Long accountId;
	private Long storeId;
	private Date checkTime;
	private Integer beforeCheckNum;// 盘点前的库存
	private Integer afterCheckNum;// 盘点后的库存

	public Long getStockId() {
		return stockId;
	}

	public void setStockId(Long stockId) {
		this.stockId = stockId;
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

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public Integer getBeforeCheckNum() {
		return beforeCheckNum;
	}

	public void setBeforeCheckNum(Integer beforeCheckNum) {
		this.beforeCheckNum = beforeCheckNum;
	}

	public Integer getAfterCheckNum() {
		return afterCheckNum;
	}

	public void setAfterCheckNum(Integer afterCheckNum) {
		this.afterCheckNum = afterCheckNum;
	}
}
