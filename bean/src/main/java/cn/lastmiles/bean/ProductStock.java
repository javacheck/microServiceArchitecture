package cn.lastmiles.bean;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.lastmiles.common.utils.StringUtils;


/**
 * 产品库存
 * 
 * @author hql
 *
 */
@Table(name="t_product_stock")
public class ProductStock {
	@Id
	@Column
	private Long id;
	@Column
	private Long productId;
	@Column
	private Double price;
	@Column
	private Double memberPrice;//会员价
	// 库存
	@Column
	private Double stock;
	@Column
	private Double alarmValue;
	// product attribute value id按照大小顺序连接，和productId组成唯一
	@Column
	private String attributeCode;
	@Column
	private Long accountId;//创建者
	@Column
	private Long storeId;
	private String storeName;
	@Column
	private String barCode;
	@Column
	private String remarks;
	
	private List<ProductAttributeValue> pavList;
	private String categoryName;
	@Column
	private Long categoryId;
	private String productName;
	private List<String> picUrlList;
	private Product product;
	private Store store;
	@Column
	private Long sales = 0L; // 销售量
	@Column
	private Long hits = 0L; // 点击量
	private String isType; // 区分标识 之前项目
	@Column
	private Date createTime; // 创建时间
	@Column
	private Long createId; // 创建人
	@Column
	private Date updateTime; // 修改时间
	@Column
	private Long updateId; // 修改人
	@Column
	private Integer recommended; // 推荐 
	@Column
	private Integer shelves; // pos端上架0  ,pos端下架1，app端上架2，app端下架3，4全部上架，5全部下架
	@Column
	private Integer type;//0表示有属性 1表示无属性
	@Column
	private Double marketPrice;//市场价格
	@Column
	private Double costPrice;//成本单价
	@Column
	private String details;//详情说明
	private Integer isAddCategory;//是否添加分类
	@Column
	private Integer sort;//排序
	@Column
	private String imageId;
	private List<ProductImage> imageList;
	
	private List<String> imgUrlList;
	
	private String attributeValuesListJointValue; // 属性值集合连接值，无属性值时，为空。 
	@Column
	private String attributeName;//属性值连接
	private String attributeValues;//属性值连接
	private Integer condition = 0;//下订单条件，0表示无任何限制，（1表示只能单个商品下单，不能使用优惠卷、余额）
	private int promotion = 0; //0表示不是促销商品，其他表示促销商品
	private Integer promotionNum;//促销数量
	private Double promotionPrice;//促销价格
	private Double discount = 10D;//促销折扣
	
	private int limitBuy = 0;//促销商品限制购买0不限制，1限制
	@Column
	private Integer freezeNum = 0;//冻结库存数量
	
	private Integer index; // 用于商品批量导入时的标识excel行数(2016.07.03)
	
	private Integer stockSum;//库存总量
	private Double costPriceSum;//库存总成本
	private Double priceSum;//库存总销售额
	private Double grossProfit;//库存总毛利
	private String brandName;//品牌名称
	@Column
	private Long unitId;
	
	@Column
	private Integer weighing = 0;// 是否称重，0不称重，1称重（2016.06.16因商品管理优化而加入）
	@Column
	private Integer returnGoods = 1;// 是否支持退货,0不支持、1支持（2016.06.16因商品管理优化而加入）
	
	private String value_1; // 仅仅用于商品管理时的赋值操作(2016.06.20)
	private String value_2; // 仅仅用于商品管理时的赋值操作(2016.06.20)
	
	private String unitName;//单位名称
	
	private String accountMobile;//创建者手机号
	
	private Double buyNum = 0.0;
	public ProductStock() {
		super();
	}

	// ----2016-01-15因修改促销组合活动为一全新商品而设置ID关联start
	@Column
	private Long promotionId = null; // 促销组合活动ID -- 2016-01-15add 初始化默认为null
	// --------2016-01-15因修改促销组合活动为一全新商品而设置ID关联end
	
	public String getAttributeValuesListJointValue() {
		return attributeValuesListJointValue;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getValue_1() {
		return value_1;
	}

	public void setValue_1(String value_1) {
		this.value_1 = value_1;
	}

	public String getValue_2() {
		return value_2;
	}

	public void setValue_2(String value_2) {
		this.value_2 = value_2;
	}

	public Long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}

	public void setAttributeValuesListJointValue(
			String attributeValuesListJointValue) {
		this.attributeValuesListJointValue = attributeValuesListJointValue;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

	public Integer getRecommended() {
		return recommended;
	}

	public void setRecommended(Integer recommended) {
		this.recommended = recommended;
	}

	public Integer getShelves() {
		return shelves;
	}

	public void setShelves(Integer shelves) {
		this.shelves = shelves;
	}
	
	public String getIsType() {
		return isType;
	}

	public void setIsType(String isType) {
		this.isType = isType;
	}

	public Long getSales() {
		return sales;
	}

	public void setSales(Long sales) {
		this.sales = sales;
	}

	public Long getHits() {
		return hits;
	}

	public void setHits(Long hits) {
		this.hits = hits;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getAttributeCode() {
		if (StringUtils.isNotBlank(attributeCode)) {
			List<String> attributeCodes=Arrays.asList(attributeCode.split("-")) ;
			Collections.sort(attributeCodes);
			attributeCode="";
			for (String string : attributeCodes) {
				attributeCode+=string+"-";
			}
			attributeCode=attributeCode.substring(0,attributeCode.length()-1);
		}
		return attributeCode;
	}

	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	
	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Double getStock() {
		return stock;
	}

	public void setStock(Double stock) {
		this.stock = stock;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public List<ProductAttributeValue> getPavList() {
		return pavList;
	}

	public void setPavList(List<ProductAttributeValue> pavList) {
		this.pavList = pavList;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Double getAlarmValue() {
		return alarmValue;
	}

	public void setAlarmValue(Double alarmValue) {
		this.alarmValue = alarmValue;
	}
	
	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public List<String> getPicUrlList() {
		return picUrlList;
	}

	public void setPicUrlList(List<String> picUrlList) {
		this.picUrlList = picUrlList;
	}
	
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Double getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(Double marketPrice) {
		this.marketPrice = marketPrice;
	}

	public Double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public Integer getIsAddCategory() {
		return isAddCategory;
	}

	public void setIsAddCategory(Integer isAddCategory) {
		this.isAddCategory = isAddCategory;
	}

	
	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public List<ProductImage> getImageList() {
		return imageList;
	}

	public void setImageList(List<ProductImage> imageList) {
		this.imageList = imageList;
	}

	public List<String> getImgUrlList() {
		return imgUrlList;
	}

	public void setImgUrlList(List<String> imgUrlList) {
		this.imgUrlList = imgUrlList;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}


	public Integer getCondition() {
		return condition;
	}

	public void setCondition(Integer condition) {
		this.condition = condition;
	}

	public int getPromotion() {
		return promotion;
	}

	public void setPromotion(int promotion) {
		this.promotion = promotion;
	}

	public Integer getPromotionNum() {
		return promotionNum;
	}

	public void setPromotionNum(Integer promotionNum) {
		this.promotionNum = promotionNum;
	}

	public Double getPromotionPrice() {
		return promotionPrice;
	}

	public void setPromotionPrice(Double promotionPrice) {
		this.promotionPrice = promotionPrice;
	}

	public int getLimitBuy() {
		return limitBuy;
	}

	public void setLimitBuy(int limitBuy) {
		this.limitBuy = limitBuy;
	}

	public Double getDiscount() {
		return discount == null ? 10D : discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getMemberPrice() {
		return memberPrice;
	}

	public void setMemberPrice(Double memberPrice) {
		this.memberPrice = memberPrice;
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

	public Integer getFreezeNum() {
		return freezeNum;
	}

	public void setFreezeNum(Integer freezeNum) {
		this.freezeNum = freezeNum;
	}

	public Integer getStockSum() {
		if(stockSum==null){
			stockSum=0;
		}
		return stockSum;
	}

	public void setStockSum(Integer stockSum) {
		this.stockSum = stockSum;
	}

	public Double getCostPriceSum() {
		if(costPriceSum==null){
			costPriceSum=0D;
		}
		return costPriceSum;
	}

	public void setCostPriceSum(Double costPriceSum) {
		this.costPriceSum = costPriceSum;
	}

	public Double getPriceSum() {
		if(priceSum==null){
			priceSum=0D;
		}
		return priceSum;
	}

	public void setPriceSum(Double priceSum) {
		this.priceSum = priceSum;
	}

	public Double getGrossProfit() {
		if(grossProfit==null){
			grossProfit=0D;
		}
		return grossProfit;
	}

	public void setGrossProfit(Double grossProfit) {
		this.grossProfit = grossProfit;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getAccountMobile() {
		return accountMobile;
	}

	public void setAccountMobile(String accountMobile) {
		this.accountMobile = accountMobile;
	}

	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getAttributeValues() {
		return attributeValues;
	}

	public void setAttributeValues(String attributeValues) {
		this.attributeValues = attributeValues;
	}

	@Override
	public String toString() {
		return "ProductStock [id=" + id + ", productId=" + productId
				+ ", price=" + price + ", memberPrice=" + memberPrice
				+ ", stock=" + stock + ", alarmValue=" + alarmValue
				+ ", attributeCode=" + attributeCode + ", accountId="
				+ accountId + ", storeId=" + storeId + ", storeName="
				+ storeName + ", barCode=" + barCode + ", remarks=" + remarks
				+ ", pavList=" + pavList + ", categoryName=" + categoryName
				+ ", categoryId=" + categoryId + ", productName=" + productName
				+ ", picUrlList=" + picUrlList + ", product=" + product
				+ ", store=" + store + ", sales=" + sales + ", hits=" + hits
				+ ", isType=" + isType + ", createTime=" + createTime
				+ ", createId=" + createId + ", updateTime=" + updateTime
				+ ", updateId=" + updateId + ", recommended=" + recommended
				+ ", shelves=" + shelves + ", type=" + type + ", marketPrice="
				+ marketPrice + ", costPrice=" + costPrice + ", details="
				+ details + ", isAddCategory=" + isAddCategory + ", sort="
				+ sort + ", imageId=" + imageId + ", imageList=" + imageList
				+ ", imgUrlList=" + imgUrlList
				+ ", attributeValuesListJointValue="
				+ attributeValuesListJointValue + ", attributeName="
				+ attributeName + ", attributeValues=" + attributeValues
				+ ", condition=" + condition + ", promotion=" + promotion
				+ ", promotionNum=" + promotionNum + ", promotionPrice="
				+ promotionPrice + ", discount=" + discount + ", limitBuy="
				+ limitBuy + ", freezeNum=" + freezeNum + ", index=" + index
				+ ", stockSum=" + stockSum + ", costPriceSum=" + costPriceSum
				+ ", priceSum=" + priceSum + ", grossProfit=" + grossProfit
				+ ", brandName=" + brandName + ", unitId=" + unitId
				+ ", weighing=" + weighing + ", returnGoods=" + returnGoods
				+ ", value_1=" + value_1 + ", value_2=" + value_2
				+ ", unitName=" + unitName + ", accountMobile=" + accountMobile
				+ ", promotionId=" + promotionId + "]";
	}

	public Double getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(Double buyNum) {
		this.buyNum = buyNum;
	}

	
	
	
}
