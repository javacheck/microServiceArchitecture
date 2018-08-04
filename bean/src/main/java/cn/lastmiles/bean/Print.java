package cn.lastmiles.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name="t_print")
public class Print {
	@javax.persistence.Id
	@Column
	private Long Id;//打印机ID
	
	@Column
	private Long storeId;//商家ID
	
	private String storeName;//商家名称
	
	@Column
	private String printName;//打印机名称

	@Column
	private String printSn;//打印机编号
	
	@Column
	private String printKey;//打印机KEY
	
	@Column
	private Integer status;//0关闭1开启
	
	@Column
	private Date updatedTime;
	
	@Column
	private String memo;//备注
	@Column
	private String categoryIds;//分类ID以,连接
	@Column
	private String categoryNames;//分类名称以,连接
	
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public Long getStoreId() {
		return storeId;
	}
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getPrintSn() {
		return printSn;
	}
	public void setPrintSn(String printSn) {
		this.printSn = printSn;
	}
	public String getPrintKey() {
		return printKey;
	}
	public void setPrintKey(String printKey) {
		this.printKey = printKey;
	}
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getCategoryIds() {
		return categoryIds;
	}
	public void setCategoryIds(String categoryIds) {
		this.categoryIds = categoryIds;
	}
	public String getCategoryNames() {
		return categoryNames;
	}
	public void setCategoryNames(String categoryNames) {
		this.categoryNames = categoryNames;
	}
	public String getPrintName() {
		return printName;
	}
	public void setPrintName(String printName) {
		this.printName = printName;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	
	
	
}
