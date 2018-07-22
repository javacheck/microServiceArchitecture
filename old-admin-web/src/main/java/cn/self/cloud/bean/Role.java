package cn.self.cloud.bean;

import java.io.Serializable;

public class Role implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2659577610078042777L;
	private Long id;
	private String name;
	private Long createdId;
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

	//默认的角色
	public static enum DefaultRole {
		// 管理员，总店，代理商，加盟店，店长，店员
		ADMIN(200), HEADQUARTERS(100), AGENCY(90), FRANCHISE(80), SHOPKEEPER(70), SALES(
				60);

		private Integer value;

		private DefaultRole(Integer value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
}
