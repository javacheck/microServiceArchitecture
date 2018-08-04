package cn.lastmiles.bean;

import java.util.Date;

public class UserReport {
	private Long id;
	private Integer typeId;
	private String typeName;
	private String content;
	private String contact;
	private Long UserId;
	private Date createdTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public Long getUserId() {
		return UserId;
	}
	public void setUserId(Long userId) {
		UserId = userId;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	@Override
	public String toString() {
		return "UserReport [id=" + id + ", typeId=" + typeId + ", typeName="
				+ typeName + ", content=" + content + ", contact=" + contact
				+ ", UserId=" + UserId + ", createdTime=" + createdTime + "]";
	}
	
	
	
}
