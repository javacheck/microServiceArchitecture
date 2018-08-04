/**
 * createDate : 2016年5月13日下午2:16:42
 */
package cn.lastmiles.bean;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="t_message_sale_record")
public class MessageSaleRecord {
	@Id
	@Column
	private Long id ; // 记录ID 
	
	@Column
	private Long userAccountMobile; // 会员帐号
	
	@Column 
	private String name; // 姓名
	
	@Column
	private Integer type = 0; // 类型
	
	@Column
	private Integer saleNumber = 0 ; // 消费数量
	
	@Column
	private Date createdTime; // 消费时间
	
	@Column
	private Long storeId; // 关联商家
	
	@Column
	private String messageContent; // 短信息内容
	
	@Column
	private Date sendTime; // 信息发送时间

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserAccountMobile() {
		return userAccountMobile;
	}

	public void setUserAccountMobile(Long userAccountMobile) {
		this.userAccountMobile = userAccountMobile;
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

	public Integer getSaleNumber() {
		return saleNumber;
	}

	public void setSaleNumber(Integer saleNumber) {
		this.saleNumber = saleNumber;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	@Override
	public String toString() {
		return "MessageSaleRecord [id=" + id + ", userAccountMobile="
				+ userAccountMobile + ", name=" + name + ", type=" + type
				+ ", saleNumber=" + saleNumber + ", createdTime=" + createdTime
				+ ", storeId=" + storeId + ", messageContent=" + messageContent
				+ ", sendTime=" + sendTime + "]";
	}
	
}