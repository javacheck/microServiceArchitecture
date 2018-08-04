package cn.lastmiles.bean;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 积分规则
 * 
 * @author hql
 *
 */
@Table(name = "t_point_rule")
public class PointRule {
	@Id
	@Column(name = "id")
	private Long id;

	@Column(name = "storeId")
	private Long storeId;

	@Column(name = "money")
	private Double money;// 消费多少钱

	@Column(name = "point")
	private Double point;// 获取积分
	
	@Column(name="validTime")
	private Integer validTime;//-1表示永久有效，单位是年

	@Column(name = "status")
	private Integer status = 1;// 1有效，0无效
	
	@Column(name = "evaluatePoint")
	private Double evaluatePoint;//评价送积分
	
	@Column
	private Integer numberDay = 0;//会员日 1 - 28表示每月，比如1表示每月1号 ，32 - 38表示每周 ， 比如32表示周一，0表示没有会员日 
	
	@Column
	private Integer birthdayDoublePoint = 0;//会员生日双倍积分 1表示当天，2表示当周，3表示当月 ，默认0没有会员生日双倍积分
	
	@Column
	private Double costPoint;//消耗积分
	
	@Column
	private Double equalMoney;//消耗积分可以兑换的钱
	
	@Column
	private Integer restriction;//积分生成限制  0无限制（默认）、1折扣订单不产生积分、2积分抵扣订单不产生积分、3余额消费不产生积分、4所有优惠订单不产生积分

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

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Double getPoint() {
		return point;
	}

	public void setPoint(Double point) {
		this.point = point;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getValidTime() {
		return validTime;
	}

	public void setValidTime(Integer validTime) {
		this.validTime = validTime;
	}


	public Double getEvaluatePoint() {
		return evaluatePoint;
	}

	public void setEvaluatePoint(Double evaluatePoint) {
		this.evaluatePoint = evaluatePoint;
	}

	public Integer getBirthdayDoublePoint() {
		return birthdayDoublePoint;
	}

	public void setBirthdayDoublePoint(Integer birthdayDoublePoint) {
		this.birthdayDoublePoint = birthdayDoublePoint;
	}

	public Integer getNumberDay() {
		return numberDay;
	}

	public void setNumberDay(Integer numberDay) {
		this.numberDay = numberDay;
	}

	public Double getCostPoint() {
		return costPoint;
	}

	public void setCostPoint(Double costPoint) {
		this.costPoint = costPoint;
	}

	public Double getEqualMoney() {
		return equalMoney;
	}

	public void setEqualMoney(Double equalMoney) {
		this.equalMoney = equalMoney;
	}

	public Integer getRestriction() {
		return restriction;
	}

	public void setRestriction(Integer restriction) {
		this.restriction = restriction;
	}

	@Override
	public String toString() {
		return "PointRule [id=" + id + ", storeId=" + storeId + ", money="
				+ money + ", point=" + point + ", validTime=" + validTime
				+ ", status=" + status + ", evaluatePoint=" + evaluatePoint
				+ ", numberDay=" + numberDay + ", birthdayDoublePoint="
				+ birthdayDoublePoint + ", costPoint=" + costPoint
				+ ", equalMoney=" + equalMoney + ", restriction=" + restriction
				+ "]";
	}
	
	
}
