package cn.lastmiles.bean.movie;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.lastmiles.common.utils.ClassUtil;

@Table(name="t_room_timeend_remind")
public class RoomTimeEndRemind {
	
	
	/**
	 * ID
	 */
	@Column
	@Id
	private Long id;
	/**
	 * 商店ID
	 */
	@Column
	private Long storeId;
	/**
	 *  提醒时间
	 */
	@Column
	private Integer minute;
	/**
	 * 提醒排序（1，2，3）
	 */
	@Column
	private Integer order;
	
	
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
	public Integer getMinute() {
		return minute;
	}
	public void setMinute(Integer minute) {
		this.minute = minute;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public String toString(){
        return ClassUtil.getFields(this).toString();
	}

}
