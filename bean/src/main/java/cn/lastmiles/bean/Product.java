package cn.lastmiles.bean;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 产品
 * 
 * @author hql
 *
 */
@Table(name="t_product")
public class Product {
	@Id
	@Column
	private Long id;//产品id
	@Column
	private String name;//产品名称
	private String oldName;//旧的产品名称
	@Column
	private Long categoryId;//产品分类id
	@Column
	private Long accountId;//当前登录帐号的id
	@Column
	private Long storeId;//商店ID
	@Column
	private Integer type;//是否含有属性0有属性1无属性
	private String categoryName;//产品分类名称
	private String storeName;//商家名称
	private Long categoryStoreId;//分类表里的storeId,为空表示是系统分类，不为空表示商家分类
	private String picUrl;//图片地址
	
	private ProductStock productStock;//图片地址
	@Column
	private Long brandId;//品牌ID
	private String brandName;//品牌名称
	
	@Column
	private String shortName;//拼音简写
	
	private String picture;
	private String pictureFileCache;
	
	private Long attribute_1;
	private Long attribute_2;
	
	private Integer weighing = 0;
	private Integer returnGoods = 1;
	private Integer shelves = 5;
	private Integer sort = 0;
	private String remarks;

	

	

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", oldName=" + oldName
				+ ", categoryId=" + categoryId + ", accountId=" + accountId
				+ ", storeId=" + storeId + ", type=" + type + ", categoryName="
				+ categoryName + ", storeName=" + storeName
				+ ", categoryStoreId=" + categoryStoreId + ", picUrl=" + picUrl
				+ ", productStock=" + productStock + ", brandId=" + brandId
				+ ", brandName=" + brandName + ", shortName=" + shortName
				+ ", picture=" + picture + ", pictureFileCache="
				+ pictureFileCache + ", attribute_1=" + attribute_1
				+ ", attribute_2=" + attribute_2 + ", weighing=" + weighing
				+ ", returnGoods=" + returnGoods + ", shelves=" + shelves
				+ ", sort=" + sort + ", remarks=" + remarks + "]";
	}

	public String getPictureFileCache() {
		return pictureFileCache;
	}

	public void setPictureFileCache(String pictureFileCache) {
		this.pictureFileCache = pictureFileCache;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getWeighing() {
		return weighing;
	}

	public void setWeighing(Integer weighing) {
		this.weighing = weighing;
	}

	public Integer getReturnGoods() {
		return returnGoods;
	}

	public void setReturnGoods(Integer returnGoods) {
		this.returnGoods = returnGoods;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Long getAttribute_1() {
		return attribute_1;
	}

	public void setAttribute_1(Long attribute_1) {
		this.attribute_1 = attribute_1;
	}

	public Long getAttribute_2() {
		return attribute_2;
	}

	public void setAttribute_2(Long attribute_2) {
		this.attribute_2 = attribute_2;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Long getCategoryStoreId() {
		return categoryStoreId;
	}

	public void setCategoryStoreId(Long categoryStoreId) {
		this.categoryStoreId = categoryStoreId;
	}
	
	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
	
	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}




	public ProductStock getProductStock() {
		return productStock;
	}

	public void setProductStock(ProductStock productStock) {
		this.productStock = productStock;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public Integer getShelves() {
		return shelves;
	}

	public void setShelves(Integer shelves) {
		this.shelves = shelves;
	}



}
