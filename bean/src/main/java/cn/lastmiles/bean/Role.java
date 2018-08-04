package cn.lastmiles.bean;

import java.io.Serializable;
import java.util.Date;

public class Role implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2659577610078042777L;
	private Long id;
	private String name;
	private Long createdId;
	
	private Date createTime;
	
	private Long ownerId;
	
	private Integer type;
	
	// 自定义的值都是-1，其他的系统默认角色
	private Integer value = -1;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	private String path;

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

	public Long getCreatedId() {
		return createdId;
	}

	public void setCreatedId(Long createdId) {
		this.createdId = createdId;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	//默认的角色
	public static enum DefaultRole {
		// 管理员，代理商，商家
		ADMIN(200), AGENCY(100), SHOP(90);

		private Integer value;

		private DefaultRole(Integer value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
}
