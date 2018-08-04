package cn.lastmiles.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 活动参与
 * @author hql
 *
 */
public class ActivityPartake implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5616163168566148649L;
	
	
	private Long id;
	private Long activityDetailId;//活动详情id
	
	private Long userId;//用户id
	private Date createdTime;//用户参与时间
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Long getActivityDetailId() {
		return activityDetailId;
	}
	public void setActivityDetailId(Long activityDetailId) {
		this.activityDetailId = activityDetailId;
	}
	
	
}
