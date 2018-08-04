package cn.lastmiles.bean;

import java.util.Date;

/**
 * createDate : 2015年8月18日 下午4:10:47
 */

public class SupermarketAd {
	private Long id; // 唯一ID
	private Long storeId; // 商家ID
	private String imageId; // 图片ID
	private Long productCategoryId; // 商品分类id
	private Date createTime; // 创建时间
	private Integer type; // 系统分类（0）或者商家自定义分类（1） -- 数据库暂已删除

	private Integer position; // 广告位置 0显示在主广告位，1次广告位

	private String storeName; // 商家名称
	private String storeAddress;
	private Double storeShipAmount;
	private String storeBusinessTime;
	private String shopPhone;

	private String productCategoryName; // 商品分类名称

	private String picURL; // 图片显示
	private String storeLogo;
	private String storeAdLogo;

	private Integer payType;
	private Double minAmount;
	private Double freeShipAmount;
	private Double shipAmount;
	private String[] shipTypeDesc;
	private String shipType;

	public SupermarketAd() {
		super();
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getPicURL() {
		return picURL;
	}

	public void setPicURL(String picURL) {
		this.picURL = picURL;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getProductCategoryName() {
		return productCategoryName;
	}

	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public Long getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Long productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public String getStoreLogo() {
		return storeLogo;
	}

	public void setStoreLogo(String storeLogo) {
		this.storeLogo = storeLogo;
	}

	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	public Double getStoreShipAmount() {
		return storeShipAmount == null ? 0.0 : storeShipAmount;
	}

	public void setStoreShipAmount(Double storeShipAmount) {
		this.storeShipAmount = storeShipAmount;
	}

	public String getStoreBusinessTime() {
		return storeBusinessTime;
	}

	public void setStoreBusinessTime(String storeBusinessTime) {
		this.storeBusinessTime = storeBusinessTime;
	}

	public String getShopPhone() {
		return shopPhone;
	}

	public void setShopPhone(String shopPhone) {
		this.shopPhone = shopPhone;
	}

	public String getStoreAdLogo() {
		return storeAdLogo;
	}

	public void setStoreAdLogo(String storeAdLogo) {
		this.storeAdLogo = storeAdLogo;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Double getMinAmount() {
		return minAmount == null ? 0.0 : minAmount;
	}

	public void setMinAmount(Double minAmount) {
		this.minAmount = minAmount;
	}

	public Double getFreeShipAmount() {
		return freeShipAmount == null ? 0.0 : freeShipAmount;
	}

	public void setFreeShipAmount(Double freeShipAmount) {
		this.freeShipAmount = freeShipAmount;
	}

	public Double getShipAmount() {
		return shipAmount;
	}

	public void setShipAmount(Double shipAmount) {
		this.shipAmount = shipAmount;
	}

	public String getShipType() {
		return shipType;
	}

	public void setShipType(String shipType) {
		this.shipType = shipType;
	}

	public String[] getShipTypeDesc() {
		return Store.getShipTypeDesc(shipType);
	}

	public void setShipTypeDesc(String[] shipTypeDesc) {
		this.shipTypeDesc = shipTypeDesc;
	}
	
	public boolean getServiceMySelf() {
		return Store.getServiceMySelf(shipType);
	}

}