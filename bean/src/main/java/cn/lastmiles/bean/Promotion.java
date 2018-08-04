package cn.lastmiles.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.lastmiles.common.utils.JsonUtils;
import cn.lastmiles.common.utils.MapBeanUtil;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
/**
 * 促销父类
 * @author hql
 *
 */
public class Promotion {
	private Long storeId;
	private int type;
	private Long[] fullSubtractKey;
	private String[] fullSubtractValue;
	private String[] promotionProductCache;
	private String imagePIC;
	
	private Long id;
	private String name;
	private Date startDate;
	private Date endDate;

	private int status = 1;// 0 关闭，1启用
	private int shared = 0; // 0不共享，1共享
	
	private Integer scope = 0;//适用范围 0 表示全部，1表示app端使用，2表示pos端使用

	private Integer total; // 所有商品加起来的促销数量,-1表示无限制
	private Double discount;//折扣
	private String condition;// 折扣条件，比如购买2件才享受这个折扣，-1表示无限制
	
	private Double amount;// 减免金额
	
	private Map<String, Object> conditions; // 满减条件,key表示满多少钱，value表示减多少
	
	private List<MapBean> appShowCondition;// 满减条件,key表示满多少钱，value表示减多少
	
	private String imageId;
	private Double price;
	
	private String appShowMessage;//前端显示文字
	private Long categoryId; // 分类ID
	private Long accountId; // 
	
	private Double costPrice;//成本单价
	private String barCode; // 条形码
	private String suffix;//图片后缀
	
	public Double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getSuffix() {
		return null == suffix ? "jpg" : suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getImagePIC() {
		return imagePIC;
	}

	public void setImagePIC(String imagePIC) {
		this.imagePIC = imagePIC;
	}

	public String[] getPromotionProductCache() {
		return promotionProductCache;
	}

	public void setPromotionProductCache(String[] promotionProductCache) {
		this.promotionProductCache = promotionProductCache;
	}

	public Long[] getFullSubtractKey() {
		return fullSubtractKey;
	}

	public void setFullSubtractKey(Long[] fullSubtractKey) {
		this.fullSubtractKey = fullSubtractKey;
	}

	public String[] getFullSubtractValue() {
		return fullSubtractValue;
	}

	public void setFullSubtractValue(String[] fullSubtractValue) {
		this.fullSubtractValue = fullSubtractValue;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getShared() {
		return shared;
	}

	public void setShared(int shared) {
		this.shared = shared;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Map<String, Object> getConditions() {
		return conditions;
	}

	public void setConditions(Map<String, Object> conditions) {
		this.conditions = conditions;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getAppShowMessage() {
		String msg = "";
		switch (this.type) {
		case Constants.Promotion.TYPE_FIRST_ORDER:msg="首单立减"+this.amount+"元";	break;
		case Constants.Promotion.TYPE_FULL_SUBTRACT:
			Map<String, Object> map = JsonUtils.jsonToMap(this.condition);
			for (String key : map.keySet()) {
				msg+="满"+key+"减"+map.get(key)+",";
			}
			msg =  msg.substring(0,msg.length()-1);
		break;
		case Constants.Promotion.TYPE_DISCOUNT:msg="全场商品"+this.discount+"折";	break;
		case Constants.Promotion.TYPE_PREFERENTIAL_VOLUME:msg="支持使用优惠券";	break;
		default:break;
		}
		return msg;
	}

	public void setAppShowMessage(String appShowMessage) {
		this.appShowMessage = appShowMessage;
	}

	public List<MapBean> getAppShowCondition() {
		if (StringUtils.isBlank(this.condition)||this.type != Constants.Promotion.TYPE_FULL_SUBTRACT ) {
			return new ArrayList<MapBean>();
		}
		Map<String, Object> objs= JsonUtils.jsonToMap(this.condition);
		List<MapBean> mapBeans =MapBeanUtil.toMapBean(objs);
		Collections.sort(mapBeans, new Comparator<MapBean>(){
			@Override
			public int compare(MapBean o1,	MapBean o2) {
				return Integer.valueOf(String.valueOf(o1.getKey()))-Integer.valueOf(String.valueOf(o2.getKey()));
			}
        });
		return mapBeans;
	}

	public void setAppShowCondition(List<MapBean> appShowCondition) {
		this.appShowCondition = appShowCondition;
	}

	public Integer getScope() {
		return scope == null ? 0 : scope;
	}

	public void setScope(Integer scope) {
		this.scope = scope;
	}
}
