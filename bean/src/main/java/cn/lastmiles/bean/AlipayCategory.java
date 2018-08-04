package cn.lastmiles.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * createdTime:2016-10-11
 * 支付宝类目
 * @author shaoyikun
 *
 */

@Table(name = "t_alipay_category")
public class AlipayCategory {

	@Id
	private Long id;
	@Column(name = "parent_id")
	private Long parentId;
	private String name;
	private String note;
	@Column(name = "created_time")
	private Date createdTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	@Override
	public String toString() {
		return "AlipayCategory [id=" + id + ", parentId=" + parentId + ", name=" + name + "]";
	}
}
