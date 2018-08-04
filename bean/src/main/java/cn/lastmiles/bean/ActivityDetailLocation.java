package cn.lastmiles.bean;

/**
 * 藏宝地点
 * @author zhangpengcheng
 *
 */
public class ActivityDetailLocation {
	
	private Long id ;//ID 
	
	private String shopName;//商店名称
	
	private String name ;//藏宝点
	
	private Long activityDetailId;//关联的活动详情ID

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getActivityDetailId() {
		return activityDetailId;
	}

	public void setActivityDetailId(Long activityDetailId) {
		this.activityDetailId = activityDetailId;
	}
	
	
}
