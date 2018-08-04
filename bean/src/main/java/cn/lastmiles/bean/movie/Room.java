package cn.lastmiles.bean.movie;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.lastmiles.common.utils.ClassUtil;

/**
 * 包间
 * 
 * @author hql
 *
 */
@Table(name = "t_movie_room")
public class Room {
	@Column
	@Id
	private Long id;
	@Column
	private Long storeId;// 所属商家

	@Column
	private String name;// 名称
	@Column
	private String number;// 包间号码

	@Column
	private Long categoryId;// 分类id RoomCategoryId
	@Column
	private Integer persons;// 额定人数

	@Column
	private Double basePrice;//基础收费
	@Column
	private Double baseUserPrice;//基础收费
	@Column
	private Integer basePriceUnit = 1;// 收费单位 1小时(默认)，2分钟，3免费

	@Column
	private String memo;

	@Column
	private Date createdTime = new Date();
	
	@Column
	private Integer status ;
	
	@Column
	private Long bookingId ;
	
	@Column
	private Integer isRemind =0;//0为不提醒 1为提醒
	
	@Column
	private Integer remindMinute ;
	
	private String storeName;
	
	private String categoryName;
	
	private RoomCategory roomCategory;
	
	private List<DateSetting> dateSettings;

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getPersons() {
		return persons;
	}

	public void setPersons(Integer persons) {
		this.persons = persons;
	}

	public Double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(Double basePrice) {
		this.basePrice = basePrice;
	}

	public Integer getBasePriceUnit() {
		return basePriceUnit;
	}

	public void setBasePriceUnit(Integer basePriceUnit) {
		this.basePriceUnit = basePriceUnit;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String toString(){
        return ClassUtil.getFields(this).toString();
	}

	public List<DateSetting> getDateSettings() {
		return dateSettings;
	}

	public void setDateSettings(List<DateSetting> dateSettings) {
		this.dateSettings = dateSettings;
	}

	public RoomCategory getRoomCategory() {
		return roomCategory;
	}

	public void setRoomCategory(RoomCategory roomCategory) {
		this.roomCategory = roomCategory;
	}

	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	public Integer getRemindMinute() {
		return remindMinute;
	}

	public void setRemindMinute(Integer remindMinute) {
		this.remindMinute = remindMinute;
	}

	public Integer getIsRemind() {
		return isRemind;
	}

	public void setIsRemind(Integer isRemind) {
		this.isRemind = isRemind;
	}

	public Double getBaseUserPrice() {
		return baseUserPrice;
	}

	public void setBaseUserPrice(Double baseUserPrice) {
		this.baseUserPrice = baseUserPrice;
	}
}
