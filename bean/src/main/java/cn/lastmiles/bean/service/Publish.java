package cn.lastmiles.bean.service;

import java.util.Date;
import java.util.List;

/**
 * 服务发布
 * 
 * @author HEQINGLANG
 *
 */
public class Publish {
	private Long id;//ID
	private Date createdTime;//创建时间
	private String keywords;// 关键字
	private String title;//标题
	private Double price;//价格
	private Long categoryId;//分类ID
	private Integer type;// 0表示买买买，1表示卖卖卖
	private Integer payType;// 支付方式，0表示线上，1表示线下
	private Integer payStatus = 0;// 线上支付状态 0未支付，1支付
	private Integer viewNumber = 0;// 浏览数
	private Integer status = 0; // 0 待审，1 已审 ，2 取消
	private String mobile;//联系电话
	private String range;//发布范围
	private String content;//服务内容
	private String address;//服务地址
	private String description;//我的描述
	private Long userId;//会员id
	private Integer bookingType;//预约类型 0为 时间 1为 次数
	
	private Integer bookedNumber = 0;//已经预约次数
	private Integer bookingStatus = 0;//0表示确认中(这个时候publish的可预约数-1)，1表示已经确认，2表示取消(这个时候publish的可预约数+1)，3表示交易完成
	
	private String times;// 时间段，客户端传过来。，分隔
	private String numbers;// 次数，客户端传过来。，分隔
	
	private String cerImages;//证书图片,客户端传过来。，分隔
	private String desImages;//描述图片，客户端传过来。，分隔
	
	private Long bookingId;
	private String iconUrl;
	//1 表示通过身份验证，其他没有
	private Integer idAudit;
	
	private Double longitude;// 经度
	
	private Double latitude;// 纬度
	
	private Double distance;// 距离
	
	private boolean mine;//是否是自己发布，如果是自己发布列表不给预约
	
	private List<PublishTime> publishTimes;//时间
	private List<PublishImage> publishImages;//图片
	
	private List<PublishImage> cerImageList;
	private List<PublishImage> desImageList;
	
	private PublishCategory publishCategory;//发布分类
	
	private String userName;
	private String categoryName;
	
	private Integer serviceTimes;
	
	private String userMobile;
	
	public Integer getIdAudit() {
		return idAudit == null ? -1 : idAudit;
	}

	public void setIdAudit(Integer idAudit) {
		this.idAudit = idAudit;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Integer getViewNumber() {
		return viewNumber;
	}

	public void setViewNumber(Integer viewNumber) {
		this.viewNumber = viewNumber;
	}


	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}


	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getNumbers() {
		return numbers;
	}

	public void setNumbers(String numbers) {
		this.numbers = numbers;
	}

	public List<PublishTime> getPublishTimes() {
		return publishTimes;
	}

	public void setPublishTimes(List<PublishTime> publishTimes) {
		this.publishTimes = publishTimes;
	}

	public List<PublishImage> getPublishImages() {
		return publishImages;
	}

	public void setPublishImages(List<PublishImage> publishImages) {
		this.publishImages = publishImages;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public String toString() {
		return "Publish [id=" + id + ", createdTime=" + createdTime
				+ ", keywords=" + keywords + ", title=" + title + ", price="
				+ price + ", categoryId=" + categoryId + ", type=" + type
				+ ", payType=" + payType + ", payStatus=" + payStatus
				+ ", viewNumber=" + viewNumber + ", status=" + status
				+ ", mobile=" + mobile + ", range=" + range + ", content="
				+ content + ", description=" + description + ", userId="
				+ userId + ", times=" + times + ", numbers=" + numbers
				+ ", publishTimes=" + publishTimes + ", publishImages="
				+ publishImages + ",mine=" + mine + "]";
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public PublishCategory getPublishCategory() {
		return publishCategory;
	}

	public void setPublishCategory(PublishCategory publishCategory) {
		this.publishCategory = publishCategory;
	}

	public Integer getBookingType() {
		return bookingType;
	}

	public void setBookingType(Integer bookingType) {
		this.bookingType = bookingType;
	}

	public Integer getBookedNumber() {
		return bookedNumber;
	}

	public void setBookedNumber(Integer bookedNumber) {
		this.bookedNumber = bookedNumber;
	}

	public String getCerImages() {
		return cerImages;
	}

	public void setCerImages(String cerImages) {
		this.cerImages = cerImages;
	}

	public String getDesImages() {
		return desImages;
	}

	public void setDesImages(String desImages) {
		this.desImages = desImages;
	}

	public boolean isMine() {
		return mine;
	}

	public void setMine(boolean mine) {
		this.mine = mine;
	}

	public Integer getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(Integer bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getDistance() {
		//转成米
		if (distance != null){
			distance = distance * 1000;
			return distance > 1 ? distance.intValue() : distance; 
		}
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Integer getServiceTimes() {
		return serviceTimes;
	}

	public void setServiceTimes(Integer serviceTimes) {
		this.serviceTimes = serviceTimes;
	}

	public List<PublishImage> getCerImageList() {
		return cerImageList;
	}

	public void setCerImageList(List<PublishImage> cerImageList) {
		this.cerImageList = cerImageList;
	}

	public List<PublishImage> getDesImageList() {
		return desImageList;
	}

	public void setDesImageList(List<PublishImage> desImageList) {
		this.desImageList = desImageList;
	}
}
