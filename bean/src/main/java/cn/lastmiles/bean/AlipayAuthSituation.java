package cn.lastmiles.bean;

import java.util.Date;

import cn.lastmiles.common.utils.StringUtils;

public class AlipayAuthSituation {
	
	private Long storeId;
	private Long getAuthID;
	private String grant_type;
	private String code;
	private String refresh_token;
	private Date createTime;
	private String response_code;
	private String msg;
	private String sub_code;
	private String sub_msg;
	private String app_auth_token;
	private String app_refresh_token;
	private String auth_app_id;
	private String expires_in;
	private String re_expires_in;
	private String user_id;
	private String storeName;
	public Long getStoreId() {
		return storeId;
	}
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	public Long getGetAuthID() {
		return getAuthID;
	}
	public void setGetAuthID(Long getAuthID) {
		this.getAuthID = getAuthID;
	}
	public String getGrant_type() {
		if( StringUtils.isBlank(grant_type) ){
			return "未知授权类型";
		} else {
			if("AUTHORIZATION_CODE".equalsIgnoreCase(grant_type)){
				grant_type = "首次授权";
			} else {
				grant_type = "刷新令牌";
			}
		}
		return grant_type;
	}
	public void setGrant_type(String grant_type) {
		this.grant_type = grant_type;
	}
	public String getCode() {
		if( StringUtils.isBlank(code) ){
			return "未知响应码";
		} else {
			if("10000".equalsIgnoreCase(code)){
				code = "授权操作成功";
			}
		}
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getResponse_code() {
		return response_code;
	}
	public void setResponse_code(String response_code) {
		this.response_code = response_code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getSub_code() {
		return sub_code;
	}
	public void setSub_code(String sub_code) {
		this.sub_code = sub_code;
	}
	public String getSub_msg() {
		return sub_msg;
	}
	public void setSub_msg(String sub_msg) {
		this.sub_msg = sub_msg;
	}
	public String getApp_auth_token() {
		return app_auth_token;
	}
	public void setApp_auth_token(String app_auth_token) {
		this.app_auth_token = app_auth_token;
	}
	public String getApp_refresh_token() {
		return app_refresh_token;
	}
	public void setApp_refresh_token(String app_refresh_token) {
		this.app_refresh_token = app_refresh_token;
	}
	public String getAuth_app_id() {
		return auth_app_id;
	}
	public void setAuth_app_id(String auth_app_id) {
		this.auth_app_id = auth_app_id;
	}
	public String getExpires_in() {
		if( StringUtils.isNotBlank(expires_in) ){
			Long time = Long.parseLong(expires_in);
			Integer day = (int) (time / (60*60*24));
			return day+"";
		}
		return expires_in;
	}
	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}
	public String getRe_expires_in() {
		if( StringUtils.isNotBlank(re_expires_in) ){
			Long time = Long.parseLong(re_expires_in);
			Integer day = (int) (time / (60*60*24));
			return day+"";
		}
		return re_expires_in;
	}
	public void setRe_expires_in(String re_expires_in) {
		this.re_expires_in = re_expires_in;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	
	@Override
	public String toString() {
		return "AlipayAuthSituation [storeId=" + storeId + ", getAuthID="
				+ getAuthID + ", grant_type=" + grant_type + ", code=" + code
				+ ", refresh_token=" + refresh_token + ", createTime="
				+ createTime + ", response_code=" + response_code + ", msg="
				+ msg + ", sub_code=" + sub_code + ", sub_msg=" + sub_msg
				+ ", app_auth_token=" + app_auth_token + ", app_refresh_token="
				+ app_refresh_token + ", auth_app_id=" + auth_app_id
				+ ", expires_in=" + expires_in + ", re_expires_in="
				+ re_expires_in + ", user_id=" + user_id + ", storeName="
				+ storeName + "]";
	}

}