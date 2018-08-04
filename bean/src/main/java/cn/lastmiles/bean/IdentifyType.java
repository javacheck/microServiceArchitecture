package cn.lastmiles.bean;
/**
 * createDate : 2015-07-03 AM 10:51
 */
public class IdentifyType {
	/**
	 * 身份证明类型代码
	 */
	private Long identifyTypeId;
	/**
	 * 身份证明类型名称
	 */
	private String identifyTypeName;
	/**
	 * 备注
	 */
	private String memo;
	
	public Long getIdentifyTypeId() {
		return identifyTypeId;
	}
	public void setIdentifyTypeId(Long identifyTypeId) {
		this.identifyTypeId = identifyTypeId;
	}
	public String getIdentifyTypeName() {
		return identifyTypeName;
	}
	public void setIdentifyTypeName(String identifyTypeName) {
		this.identifyTypeName = identifyTypeName;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
}