package cn.lastmiles.bean.service;

/**
 * 服务图片
 * 
 * @author HEQINGLANG
 *
 */
public class PublishImage {
	private Long publishId;//发布ID
	private String imageId;//图片ID
	private String suffix;// 后缀
	private Integer type;//图片类型  1 为 描述图片  2 为 证书图片 
	private Integer orderSort;// 排序
	
	private String picUrl;//图片地址

	
	public Long getPublishId() {
		return publishId;
	}

	public void setPublishId(Long publishId) {
		this.publishId = publishId;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public Integer getOrderSort() {
		return orderSort;
	}

	public void setOrderSort(Integer orderSort) {
		this.orderSort = orderSort;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}


}
