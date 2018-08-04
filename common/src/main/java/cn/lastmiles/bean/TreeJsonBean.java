package cn.lastmiles.bean;

import java.io.Serializable;

public class TreeJsonBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2741094818172779465L;

	private Object id ;//ID
	
	private Object pid;//父类ID
	
	private String name;//名称
	
	

	public TreeJsonBean() {
		super();
	}
	public TreeJsonBean(Object id ,Object pid,String name) {
		super();
		this.id=id;
		this.pid=pid;
		this.name=name;
	}
	public Object getId() {
		return id;
	}
	public void setId(Object id) {
		this.id = id;
	}
	public Object getPid() {
		return pid;
	}
	public void setPid(Object pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
