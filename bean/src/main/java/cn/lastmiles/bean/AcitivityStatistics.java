package cn.lastmiles.bean;
/**
 * createDate : 2015年8月6日 下午4:50:13 
 */

public class AcitivityStatistics {
	private Long id ; // 记录ID
	private Long userId; // 会员ID
	private String mobile; // 会员手机号码
	private Integer allTreasurePoint; // 总寻宝点
	private String time; // 总用时
	private Integer ranklist; // 排行
	
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
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Integer getAllTreasurePoint() {
		return allTreasurePoint;
	}
	public void setAllTreasurePoint(Integer allTreasurePoint) {
		this.allTreasurePoint = allTreasurePoint;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Integer getRanklist() {
		return ranklist;
	}
	public void setRanklist(Integer ranklist) {
		this.ranklist = ranklist;
	}
	
	
}
