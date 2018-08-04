package cn.lastmiles.bean;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 商店员工
 * @author hql
 *
 */
@Table(name="t_employee")
public class Employee {
	@Id
	@Column(name="id")
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="type")
	private Integer type;//0表示导购员，1表示收银员
	
	@Column(name="storeId")
	private Long storeId;
	
	@Column
	private Integer sex = 1; // 0 女；1 男

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", type=" + type
				+ ", storeId=" + storeId + ", sex=" + sex + "]";
	}
	
	
}