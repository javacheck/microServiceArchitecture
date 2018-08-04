package cn.lastmiles.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 产品类型
 * 
 * @author hql
 *
 */
@Table(name="t_product_category")
public class ProductCategory {
	@Id
	@Column
	private Long id;//产品类型id
	@Column
	private String name;//产品类型名称
	@Column
	private Long pId;//产品类型父id
	private String parentName;//产品类型父名称
	/**
	 * 全路径，从根节点开始的id连接 1-10-100
	 */
	@Column
	private String path;
	//所属商店
	@Column
	private Long storeId;
	//所属商店名称
	private String storeName;
	@Column
	private Integer type = 0;//0为 系统分类 1 为 自定义分类
	@Column
	private Integer sort = 0;
	
	@Column
	private Integer level = 0; // 0 代表顶级分类(2016.05.31因新的商品分类设计而加入)
	
	private Boolean isCurrentStoreData = false; // true代表是当前店的商品分类数据(2016.06.01用于web前端判断是否可以新增和修改---自己只能修改自己商家的商品分类)
	
	private int shipType;// 配送方式 0 无限制 ，1表示只能到店自提
	private int payType;//0表示无限制，1表示只能线上支付
	private int useBalance;//0不可以使用余额，1可以使用余额
	private int useCashGift;//0不可以使用优惠卷，1可以使用
	private int buyNum = -1;//购买数量限制 -1 表示无限制
	private int share = 0;//1单独促销，0可以同其他商品一起购买
	private List<ProductAttribute> productAttributeList;

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


	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getpId() {
		return pId;
	}

	public void setpId(Long pId) {
		this.pId = pId;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public int getShipType() {
		return shipType;
	}

	public void setShipType(int shipType) {
		this.shipType = shipType;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public int getUseBalance() {
		return useBalance;
	}

	public void setUseBalance(int useBalance) {
		this.useBalance = useBalance;
	}

	public int getUseCashGift() {
		return useCashGift;
	}

	public void setUseCashGift(int useCashGift) {
		this.useCashGift = useCashGift;
	}

	public int getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}

	public int getShare() {
		return share;
	}

	public void setShare(int share) {
		this.share = share;
	}

	public List<ProductAttribute> getProductAttributeList() {
		return productAttributeList;
	}

	public void setProductAttributeList(List<ProductAttribute> productAttributeList) {
		this.productAttributeList = productAttributeList;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Boolean getIsCurrentStoreData() {
		return isCurrentStoreData;
	}

	public void setIsCurrentStoreData(Boolean isCurrentStoreData) {
		this.isCurrentStoreData = isCurrentStoreData;
	}

	@Override
	public String toString() {
		return "ProductCategory [id=" + id + ", name=" + name + ", pId=" + pId
				+ ", parentName=" + parentName + ", path=" + path
				+ ", storeId=" + storeId + ", storeName=" + storeName
				+ ", type=" + type + ", sort=" + sort + ", level=" + level
				+ ", isCurrentStoreData=" + isCurrentStoreData + ", shipType="
				+ shipType + ", payType=" + payType + ", useBalance="
				+ useBalance + ", useCashGift=" + useCashGift + ", buyNum="
				+ buyNum + ", share=" + share + ", productAttributeList="
				+ productAttributeList + "]";
	}

}