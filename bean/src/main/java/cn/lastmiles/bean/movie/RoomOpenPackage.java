package cn.lastmiles.bean.movie;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 开房套餐关联表
 * @author hql
 *
 */
@Table(name="t_movie_room_open_package")
public class RoomOpenPackage {
	@Column
	private Long roomOpenId;
	
	@Column
	private Long roomPackageId;
	
	@Column
	private Double price;//套餐价格
	
	@Column
	private Integer duration;//时长，单位小时
	
	@Column
	private String name; //套餐名称
	
	@Column
	private String packageContent;//套餐内容，json保存

	public Long getRoomOpenId() {
		return roomOpenId;
	}

	public void setRoomOpenId(Long roomOpenId) {
		this.roomOpenId = roomOpenId;
	}

	public Long getRoomPackageId() {
		return roomPackageId;
	}

	public void setRoomPackageId(Long roomPackageId) {
		this.roomPackageId = roomPackageId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPackageContent() {
		return packageContent;
	}

	public void setPackageContent(String packageContent) {
		this.packageContent = packageContent;
	}

	@Override
	public String toString() {
		return "RoomOpenPackage [roomOpenId=" + roomOpenId + ", roomPackageId="
				+ roomPackageId + ", price=" + price + ", duration=" + duration
				+ ", name=" + name + ", packageContent=" + packageContent + "]";
	}
	
}
