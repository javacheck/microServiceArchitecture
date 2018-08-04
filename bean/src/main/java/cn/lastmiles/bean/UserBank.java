package cn.lastmiles.bean;

public class UserBank {
	private Long bankId;//银行卡ID
	private String cardNumber;//持卡号
	private String cardHolder;//持卡人
	private Long userId;//用户ID
	private int inUse;//正在使用中0表示不是当前使用中1表示是当前使用中
	public Long getBankId() {
		return bankId;
	}
	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getCardHolder() {
		return cardHolder;
	}
	public void setCardHolder(String cardHolder) {
		this.cardHolder = cardHolder;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public int getInUse() {
		return inUse;
	}
	public void setInUse(int inUse) {
		this.inUse = inUse;
	}
	@Override
	public String toString() {
		return "UserBank [bankId=" + bankId + ", cardNumber=" + cardNumber
				+ ", cardHolder=" + cardHolder + ", userId=" + userId
				+ ", inUse=" + inUse + "]";
	}
	
	
}
