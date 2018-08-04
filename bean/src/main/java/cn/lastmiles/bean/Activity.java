package cn.lastmiles.bean;

import java.io.Serializable;
import java.util.Date;


/**
 * 活动
 * 
 * @author hql
 *
 */
public class Activity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -326206519811668206L;
	private Long id;
	private String title;// 标题
	private Date startDate;// 开始时间
	private Date endDate;// 结束时间

	private String imageId;// 活动图片
	private String imageUrl;// 图片地址
	private String memo;// 备注

	private Date createdTime;
	private Integer status;// 1 正常，0无效
	
	private Long areaId;//地区id ，关联 t_area
	
	private String areaName;
	
	private ActivityDetail activityDetail;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public ActivityDetail getActivityDetail() {
		return activityDetail;
	}

	public void setActivityDetail(ActivityDetail activityDetail) {
		this.activityDetail = activityDetail;
	}

	
}
