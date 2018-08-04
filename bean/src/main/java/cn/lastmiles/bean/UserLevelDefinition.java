package cn.lastmiles.bean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.lastmiles.constant.Constants;

/**
 * 用户等级定义
 * 
 * @author hql
 *
 */
@Table(name = "t_user_level_definition")
public class UserLevelDefinition {
	@Column
	@Id
	private Long id;

	@Column
	private Long storeId;

	@Column
	private String name;

	@Column
	private Double point = 0D;// 所需积分 或者消费总额
	
	@Column
	private Integer type;//0表示积分，1表示消费总额
	
	@Column
	private Integer mode = Constants.UserLevelDefinition.AUTOMODE; // 0 代表自动升级、1代表手动升级

	@Column
	private Double discount = 10D;// 享受折扣

	@Column
	private Date createdTime;
	@Column
	private Date updateTime;
	
	private String storeName;
	
	@Column
	private Integer discountScope=0;//0全部商品 1按分类
	private List<Long> categoryIdList;
	
	
	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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

	public Double getPoint() {
		if( null != this.type ){
			if( this.type.intValue() == 0 ){ // 如果是积分的话，取整
				if( null != point ){
					point = BigDecimal.valueOf(point).setScale(0,BigDecimal.ROUND_FLOOR).doubleValue(); 					
				}
			}
		}
		return point;
	}

	public void setPoint(Double point) {
		this.point = point;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		if( null == discount ){
			discount = 10D;
		}
		this.discount = discount;
	}

	public Date getCreatedTime() {
		if( null == createdTime ){
			createdTime = new Date();
		}
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getDiscountScope() {
		return discountScope;
	}

	public void setDiscountScope(Integer discountScope) {
		this.discountScope = discountScope;
	}

	public List<Long> getCategoryIdList() {
		return categoryIdList;
	}

	public void setCategoryIdList(List<Long> categoryIdList) {
		this.categoryIdList = categoryIdList;
	}

	@Override
	public String toString() {
		return "UserLevelDefinition [id=" + id + ", storeId=" + storeId
				+ ", name=" + name + ", point=" + point + ", type=" + type
				+ ", mode=" + mode + ", discount=" + discount
				+ ", createdTime=" + createdTime + ", updateTime=" + updateTime
				+ ", storeName=" + storeName + ", discountScope="
				+ discountScope + ", categoryIdList=" + categoryIdList + "]";
	}

	

}
