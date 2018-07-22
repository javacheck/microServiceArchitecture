package cn.self.cloud.bean;

/**
 * 产品类型
 * 
 * @author hql
 *
 */
public class ProductCategory {
	private Long id;//产品类型id
	
	private String name;//产品类型名称
	
	private Long parentId;//产品类型父id
	
	private String parentName;//产品类型父名称
	/**
	 * 全路径，从根节点开始的id连接 1-10-100
	 */
	private String path;
	
	//所属商店
	private Long storeId;


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

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}


	@Override
	public String toString() {
		return "ProductCategory [id=" + id + ", name=" + name + ", parentId="
				+ parentId + ", parentName=" + parentName + "]";
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

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	
}
