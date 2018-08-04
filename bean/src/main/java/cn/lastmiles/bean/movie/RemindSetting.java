package cn.lastmiles.bean.movie;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.lastmiles.common.utils.ClassUtil;

/**
 * 提醒设置
 * 
 * @author hql
 *
 */
@Table(name = "t_movie_remind_setting")
public class RemindSetting {
	@Id
	@Column
	private Long id;

	@Column
	private Integer minute;

	@Column
	private Long storeId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getMinute() {
		return minute;
	}

	public void setMinute(Integer minute) {
		this.minute = minute;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	public String toString(){
        return ClassUtil.getFields(this).toString();
	}
}
