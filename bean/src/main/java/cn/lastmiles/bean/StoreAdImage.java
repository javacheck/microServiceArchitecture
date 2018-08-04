package cn.lastmiles.bean;
/**
 * createDate : 2015年8月25日 下午3:26:29 
 */

public class StoreAdImage {
	
	private Long storeId;
	private String imageId;
	private Integer type; //default : 0
	
	private String storeName ; // 商家名称
	
	private String picURL; // 图片显示
	private Long cacheStoreId; // 缓存商店ID--用于页面是否是修改判断
	
	public Long getCacheStoreId() {
		return cacheStoreId;
	}
	public void setCacheStoreId(Long cacheStoreId) {
		this.cacheStoreId = cacheStoreId;
	}
	public String getPicURL() {
		return picURL;
	}
	public void setPicURL(String picURL) {
		this.picURL = picURL;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}	
}