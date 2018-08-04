package cn.lastmiles.bean;

public class HtmlAd {
	private Long id;//广告Id
	private String url;//url
	private String title;//标题
	private Integer share;//0没有分享，1有分享
	private String imageId;//图片ID
	private Long storeId;//空表示全局
	private String storeName;//商家名称
	private Long cityId;//城市ID
	private String cityName;//城市名称
	private String areaPath;
	private String picUrl;//图片路径
	private Integer valid;//0有效1无效
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getShare() {
		return share;
	}
	public void setShare(Integer share) {
		this.share = share;
	}
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public Long getStoreId() {
		return storeId;
	}
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public String getAreaPath() {
		return areaPath;
	}
	public void setAreaPath(String areaPath) {
		this.areaPath = areaPath;
	}
	
	public Integer getValid() {
		return valid;
	}
	public void setValid(Integer valid) {
		this.valid = valid;
	}
	@Override
	public String toString() {
		return "HtmlAd [id=" + id + ", url=" + url + ", title=" + title
				+ ", share=" + share + ", imageId=" + imageId + ", storeId="
				+ storeId + ", storeName=" + storeName + ", cityId=" + cityId
				+ ", cityName=" + cityName + ", areaPath=" + areaPath
				+ ", picUrl=" + picUrl + ", valid=" + valid + "]";
	}
	
}
