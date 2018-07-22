package cn.self.cloud.bean;

/**
 * 产品
 * 
 * @author hql
 *
 */
public class Product {
	private Long id;//产品id
	private String name;//产品名称

	private Long categoryId;//产品分类id
	private Long accountId;//当前登录帐号的id
	
	private String categoryName;//产品分类名称

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
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

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
}
