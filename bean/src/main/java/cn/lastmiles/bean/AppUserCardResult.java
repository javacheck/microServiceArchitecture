package cn.lastmiles.bean;

import java.util.Date;


public class AppUserCardResult {
	

	/**
	 * 卡号
	 */
	private Long id;
	
	/**
	 * 卡号
	 */
	private String cardNum;
	/**
	 * 功能
	 */
	private String typeDescribe;
	/**
	 * 创建时间
	 */
	private Date createdTime;
	/**
	 * 第一列名称
	 */
	private String firstColumnName;
	/**
	 * 第一列内容
	 */
	private String firstColumnContent;
	/**
	 * 第二列名称
	 */
	private String secondColumnName;
	/**
	 * 第二列内容
	 */
	private String secondColumnContent;
	/**
	 * 商店名称
	 */
	private String storeName;
	
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getTypeDescribe() {
		return typeDescribe;
	}
	public void setTypeDescribe(String typeDescribe) {
		this.typeDescribe = typeDescribe;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public String getFirstColumnName() {
		return firstColumnName;
	}
	public void setFirstColumnName(String firstColumnName) {
		this.firstColumnName = firstColumnName;
	}
	public String getFirstColumnContent() {
		return firstColumnContent;
	}
	public void setFirstColumnContent(String firstColumnContent) {
		this.firstColumnContent = firstColumnContent;
	}
	public String getSecondColumnName() {
		return secondColumnName;
	}
	public void setSecondColumnName(String secondColumnName) {
		this.secondColumnName = secondColumnName;
	}
	public String getSecondColumnContent() {
		return secondColumnContent;
	}
	public void setSecondColumnContent(String secondColumnContent) {
		this.secondColumnContent = secondColumnContent;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
}
