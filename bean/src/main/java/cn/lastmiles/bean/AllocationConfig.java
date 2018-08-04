package cn.lastmiles.bean;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "t_allocation_config")
public class AllocationConfig {
	@Column
	private Long storeId;// 店铺id
	@Column
	private Integer status;//0不允许，1允许分店之间调拨,2分店间调拨需总部审核
	public Long getStoreId() {
		return storeId;
	}
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "AllocationConfig [storeId=" + storeId + ", status=" + status
				+ "]";
	}
	
	
}
