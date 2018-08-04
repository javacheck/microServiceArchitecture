package cn.lastmiles.bean;

public class UserCer {
	private String cerId;
	private Long userId;
	private String cerName;
	
	
	public String getCerId() {
		return cerId;
	}
	public void setCerId(String cerId) {
		this.cerId = cerId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getCerName() {
		return cerName;
	}
	public void setCerName(String cerName) {
		this.cerName = cerName;
	}
	@Override
	public String toString() {
		return "UserCer [cerId=" + cerId + ", userId=" + userId + ", cerName="
				+ cerName + "]";
	}
	
	
}
