package cn.lastmiles.bean;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 客屏广告
 * @author hql
 *
 */
@Table(name = "t_user_screen_ad")
public class UserScreenAd {
	@Column
	private Long storeId;// 总部或者实体店id
	
	@Id
	@Column
	private String imageId;//广告图片ID
	
	private String picUrl;//图片路径

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	
}
