package cn.lastmiles.bean.movie;

import javax.persistence.Column;
import javax.persistence.Table;

import cn.lastmiles.common.utils.ClassUtil;

/**
 * 包间类型和时间段的关联表
 * 
 * @author hql
 *
 */
@Table(name = "t_movie_room_category_date_setting")
public class RoomCategoryDateSetting {
	@Column
	private Long categoryId;
	@Column
	private Long dateSettingId;
	@Column
	private Double price;
	@Column
	private Double userPrice;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getDateSettingId() {
		return dateSettingId;
	}

	public void setDateSettingId(Long dateSettingId) {
		this.dateSettingId = dateSettingId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	public String toString(){
        return ClassUtil.getFields(this).toString();
	}

	public Double getUserPrice() {
		return userPrice;
	}

	public void setUserPrice(Double userPrice) {
		this.userPrice = userPrice;
	}
}
