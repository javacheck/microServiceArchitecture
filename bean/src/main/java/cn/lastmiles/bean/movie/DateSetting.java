package cn.lastmiles.bean.movie;

import java.util.Arrays;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 日期设置
 * @author hql
 *
 */
@Table(name = "t_movie_date_setting")
public class DateSetting {
	@Column
	@Id
	private Long id;
	@Column
	private String name;

	@Column
	private Long storeId;

	@Column
	private Integer type;// 1普通日期，2周末，3特殊时间（普通时间段），4自定义节假日，5节假日时间段

	@Column
	private Double startTime;// 开始时间 普通日期和周末1-7表示周一到周日，特殊时间0-24表示0点到24点

	@Column
	private Double endTime;// 结束时间 普通日期和周末1-7表示周一到周日，特殊时间0-24表示0点到24点
	
	@Column
	private Date holiday;//节假日日期
	
	private Double categoryPrice;//页面实现该房间时间价格
	
	private Double userPrice;//页面实现该房间时间价格
	
	private Long[] idArray;
	private String[] nameArray;
	private Double[] startTimeArray;
	private Double[] endTimeArray;
	private Integer[] typeArray;
	
	private String[] cacheMapData;

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

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Double getStartTime() {
		return startTime;
	}

	public void setStartTime(Double startTime) {
		this.startTime = startTime;
	}

	public Double getEndTime() {
		return endTime;
	}

	public void setEndTime(Double endTime) {
		this.endTime = endTime;
	}

	public Date getHoliday() {
		return holiday;
	}

	public void setHoliday(Date holiday) {
		this.holiday = holiday;
	}

	public Double getCategoryPrice() {
		return categoryPrice;
	}

	public void setCategoryPrice(Double categoryPrice) {
		this.categoryPrice = categoryPrice;
	}

	public Long[] getIdArray() {
		return idArray;
	}

	public void setIdArray(Long[] idArray) {
		this.idArray = idArray;
	}

	public String[] getNameArray() {
		return nameArray;
	}

	public void setNameArray(String[] nameArray) {
		this.nameArray = nameArray;
	}

	public Double[] getStartTimeArray() {
		return startTimeArray;
	}

	public void setStartTimeArray(Double[] startTimeArray) {
		this.startTimeArray = startTimeArray;
	}

	public Double[] getEndTimeArray() {
		return endTimeArray;
	}

	public void setEndTimeArray(Double[] endTimeArray) {
		this.endTimeArray = endTimeArray;
	}

	public Integer[] getTypeArray() {
		return typeArray;
	}

	public void setTypeArray(Integer[] typeArray) {
		this.typeArray = typeArray;
	}

	public String[] getCacheMapData() {
		return cacheMapData;
	}

	public void setCacheMapData(String[] cacheMapData) {
		this.cacheMapData = cacheMapData;
	}

	@Override
	public String toString() {
		return "DateSetting [id=" + id + ", name=" + name + ", storeId="
				+ storeId + ", type=" + type + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", holiday=" + holiday
				+ ", categoryPrice=" + categoryPrice + ", idArray="
				+ Arrays.toString(idArray) + ", nameArray="
				+ Arrays.toString(nameArray) + ", startTimeArray="
				+ Arrays.toString(startTimeArray) + ", endTimeArray="
				+ Arrays.toString(endTimeArray) + ", typeArray="
				+ Arrays.toString(typeArray) + ", cacheMapData="
				+ Arrays.toString(cacheMapData) + "]";
	}

	public Double getUserPrice() {
		return userPrice;
	}

	public void setUserPrice(Double userPrice) {
		this.userPrice = userPrice;
	}
	
}