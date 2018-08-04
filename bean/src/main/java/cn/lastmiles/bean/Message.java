package cn.lastmiles.bean;

/**
 * createDate : 2015-07-03 PM 14:46
 */
import java.util.Date;

public class Message {
	/**
	 * 消息代码
	 */
	private Long messageId;
	/**
	 * 消息内容
	 */
	private String message;
	/**
	 * 消息主题
	 */
	private String title;
	/**
	 * 用户代码
	 */
	private Long userId;
	/**
	 * 已读(1已读0未读)
	 */
	private String readed;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 店铺ID
	 */
	private Long storeId;
	/**
	 * 创建时间
	 */
	private Date createTime;

	//1发布通知，2审核通过，3审核不通过，4撤销，5预约，6确认成功，7拒绝预约，8发布方中途取消，9预约方中途取消
	private Integer type;
	
	//所属模块的id
	private Long ownerId;
	
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

	public Message() {
		super();
	}
	
	public Message(String title,String message, Long userId,Integer type,Long ownerId) {
		super();
		this.type=type;
		this.ownerId=ownerId;
		this.message = message;
		this.title = title;
		this.userId = userId;
		this.readed = "0";//默认设置为未读
		this.createTime = new Date();//设置时间为默认时间
	}
	public Message(String title,String message, Long userId,Integer type,Long ownerId,Long storeId) {
		super();
		this.type=type;
		this.ownerId=ownerId;
		this.message = message;
		this.title = title;
		this.userId = userId;
		this.storeId = storeId;
		this.readed = "0";//默认设置为未读
		this.createTime = new Date();//设置时间为默认时间
	}
	

	public Message(Long messageId, String message, String title,
			Long userId, String readed, String memo, Date createTime) {
		super();
		this.messageId = messageId;
		this.message = message;
		this.title = title;
		this.userId = userId;
		this.readed = readed;
		this.memo = memo;
		this.createTime = createTime;
	}

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getReaded() {
		return readed;
	}

	public void setReaded(String readed) {
		this.readed = readed;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "Message [messageId=" + messageId + ", message=" + message
				+ ", title=" + title + ", userId=" + userId + ", readed="
				+ readed + ", memo=" + memo + ", createTime=" + createTime
				+ ", type=" + type + ", ownerId=" + ownerId + "]";
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	

}