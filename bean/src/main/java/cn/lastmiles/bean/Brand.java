package cn.lastmiles.bean;

public class Brand {
	private Long id;//品牌ID
	private String name;//品牌名称
	private Long storeId;//商店ID
	private String storeName;//商店名称
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
	public Long getStoreId() {
		return storeId;
	}
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	@Override
	public String toString() {
		return "Brand [id=" + id + ", name=" + name + ", storeId=" + storeId
				+ ", storeName=" + storeName + "]";
	}
	
	
}
