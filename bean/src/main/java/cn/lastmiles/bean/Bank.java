package cn.lastmiles.bean;

/**
 * 银行表
 * @author hql
 *
 */
public class Bank {
	private Integer id;
	private String code;
	private String name;
	
	private String iconUrl; // 银行图标(2015.12.15)

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
