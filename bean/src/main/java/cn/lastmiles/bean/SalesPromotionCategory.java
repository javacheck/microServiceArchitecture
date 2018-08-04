package cn.lastmiles.bean;
/**
 * createDate : 2015年10月12日 上午10:31:58 
 */
import java.util.Date;

import cn.lastmiles.constant.Constants;

public class SalesPromotionCategory {
	private Long id ;
	private String name;
	private Long storeId;
	private Double amount;
	private Double discount;
	private Integer buyNum;
	private String shipType;
	private Integer payType;
	private Integer useBalance;
	private Integer useCashGift;
	private Date startDate;
	private Date endDate;
	private Integer status;
	private Integer salesNum;
	private Integer share;
	
	private String storeName; // 非数据库字段,仅用来列表显示商家名称
	private Integer isUpdate;
	
	public SalesPromotionCategory() {
		super(); 
	}

	public Integer getIsUpdate() {
		if( null == this.startDate ){
			return isUpdate;
		}
		Long isHaveUse = System.currentTimeMillis()-this.startDate.getTime() ;
		if(isHaveUse.longValue() >= 0){
			this.isUpdate = 1; // 已经开始
		}else {
			this.isUpdate = 0;
		}
		return isUpdate;
	}

	public void setIsUpdate(Integer isUpdate) {
		this.isUpdate = isUpdate;
	}

	public Integer getShare() {
		return share;
	}

	public void setShare(Integer share) {
		this.share = share;
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

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Integer getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(Integer buyNum) {
		if( null == buyNum ){
			buyNum = Constants.SalesPromotionCategory.unlimited;
		}
		this.buyNum = buyNum;
	}

	public String getShipType() {
		return shipType;
	}

	public void setShipType(String shipType) {
		this.shipType = shipType;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getUseBalance() {
		return useBalance;
	}

	public void setUseBalance(Integer useBalance) {
		this.useBalance = useBalance;
	}

	public Integer getUseCashGift() {
		return useCashGift;
	}

	public void setUseCashGift(Integer useCashGift) {
		this.useCashGift = useCashGift;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSalesNum() {
		return salesNum;
	}

	public void setSalesNum(Integer salesNum) {
		if( null == salesNum ){
			salesNum = Constants.SalesPromotionCategory.unlimited;
		}
		this.salesNum = salesNum;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	
}