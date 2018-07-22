package cn.self.cloud.bean;

import java.util.Date;

/**
 * 商家
 * 
 * @author hql
 *
 */
public class Store {
	private Long id;
	private String name;
	private Date createdTime;
	private Long accountId;
	//上一级商家
	private Long parentId;
	private String parentName;
	
	/**
	 * 全路径，从根节点开始的id连接 1-10-100
	 */
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

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
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

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	
	
}
