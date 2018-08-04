package cn.lastmiles.bean;

import java.util.Date;

/**
 * createDate : 2015年7月28日 下午2:10:38 
 */

/**
 * 主要实现的功能模块：API那个项目，用户登录或者商家登录的时候，成功了，就把这些信息记录到这个表
 */
public class LoginRecord {

	private String token; // 主键
	private Integer type; // 0表示商家，1表示用户
	private Long ownerId; // 商家accountId 或者用户id
	private Date createTime; // 新增时间
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}