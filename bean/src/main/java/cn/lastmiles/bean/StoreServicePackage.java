package cn.lastmiles.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 商家服务套餐
 * @author hql
 *
 */
@Table(name="t_store_service_package")
public class StoreServicePackage {
	@Id
	@Column
	private Long id;
	
	@Column
	private Long storeId;
	
	@Column
	private String name;//套餐名称
	
	@Column
	private Integer times = 0;//服务次数
	
	@Column
	private Long accountId;//创建者id
	
	@Column
	private Date createdTime;
	
	@Column
	private Double packagePrice; // 2016.10.26 因服务套餐优化而新增的套餐总价字段
	
	@Column
	private String remark; // 2016.10.26 因服务套餐优化而新增的备注字段
	
	private String operator; // 2016.10.26 因服务套餐优化为了页面显示而临时用的一个操作人字段(非数据库字段---accountId相关)

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Double getPackagePrice() {
		return packagePrice;
	}

	public void setPackagePrice(Double packagePrice) {
		this.packagePrice = packagePrice;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Override
	public String toString() {
		return "StoreServicePackage [id=" + id + ", storeId=" + storeId
				+ ", name=" + name + ", times=" + times + ", accountId="
				+ accountId + ", createdTime=" + createdTime
				+ ", packagePrice=" + packagePrice + ", remark=" + remark
				+ ", operator=" + operator + "]";
	}

}