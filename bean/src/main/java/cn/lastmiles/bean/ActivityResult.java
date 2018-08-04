package cn.lastmiles.bean;

import java.util.Date;

public class ActivityResult {
	
	private Long id ;//ID
	
	private Integer status;// 状态 0 表示审核中 , 1表示审核通过，2表示审核不通过
	
	private Long accountId;//审核的管理员帐号id
	
	private String imageId;//上传的图片id
	
	private Long userId;//参与者
	
	private Long locationId;//地点ID
	
	private Date createdTime;//上传时间
	
	private Long partakeId;//参与ID

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Long getPartakeId() {
		return partakeId;
	}

	public void setPartakeId(Long partakeId) {
		this.partakeId = partakeId;
	}
	
	

}
