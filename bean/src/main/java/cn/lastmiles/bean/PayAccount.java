package cn.lastmiles.bean;

/**
 * createDate : 2015-07-10 15:49
 */
import java.util.Date;

import cn.lastmiles.common.utils.NumberUtils;

public class PayAccount {
	private Long id;
	private Long ownerId; // 商户id，或者代理商id，用户id
	private Integer type; // 0商户，1代理商,2用户
	private String password; // 支付密码
	private Date createdTime;
	private Integer status; // 0未激活，1正常，2冻结，3销户，4挂失，5锁定
	private Double balance = 0D; // 余额
	private Double frozenAmount=0D; // 冻结金额
	private String name;//名称
	private Double allowBalance = 0D;//可使用余额
	private String mobile;//手机号码

	public Double getAllowBalance() {
		this.allowBalance = NumberUtils.add(balance == null ? 0 : balance, frozenAmount == null ? 0 : frozenAmount);
		return this.allowBalance;
	}

	public void setAllowBalance(Double allowBalance) {
		this.allowBalance = allowBalance;
	}

	public Double getTotalBalance() {
		return NumberUtils.add(balance == null ? 0 : balance,
				frozenAmount == null ? 0 : frozenAmount);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Double getFrozenAmount() {
		return frozenAmount;
	}

	public void setFrozenAmount(Double frozenAmount) {
		this.frozenAmount = frozenAmount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBalanceString() {
		return NumberUtils.dobleToString(balance);
	}


	public String getFrozenAmountString() {
		return NumberUtils.dobleToString(frozenAmount);
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


}