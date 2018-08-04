package cn.lastmiles.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 短信记录
 * 
 * @author hql
 *
 */
@Table(name = "t_sms_record")
public class SmsRecord {
	@Column
	private String content;
	@Column
	private String mobiles;
	@Column
	private Date createdTime = new Date();
	@Column
	private String token;//如果是同一批发送，token一样

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMobiles() {
		return mobiles;
	}

	public void setMobiles(String mobiles) {
		this.mobiles = mobiles;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	@Override
	public String toString() {
		return "SmsRecord [content=" + content + ", mobiles=" + mobiles
				+ ", createdTime=" + createdTime + "]";
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
