/**
 * createDate : 2016年9月1日下午5:02:58
 */
package cn.lastmiles.alipay.model.status.auth;

public enum AuthStatus {
	SUCCESS, 
	AUTH_TOKEN_NOT_EXISTED, // 特定权限的令牌不存在
	ILLEGAL_PARAM, // 无效的业务参数
	APP_NOT_ISV, // 当前请求应用非第三方应用，请求失败
	SYSTEM_ERROR, // 系统异常
	AUTH_TOKEN_NOT_CONSISTENT, // 令牌的被授权应用与当前发起请求的应用不一致
	REFRESH_TOKEN_NOT_VALID, // 无效的刷新令牌
	GRANT_TYPE_INVALID, // grant_type必须是authorization_code或者refresh_token
	AUTH_CODE_NOT_EXIST, // auth_code不存在
	APP_ID_NOT_CONSISTENT, // 授权令牌授予的应用AppId与当前应用AppId不一致
	AUTH_CODE_NOT_VALID, // 无效的auth_code
	AUTH_TOKEN_NOT_FOUND, // 授权令牌不存在
	REFRESH_TOKEN_NOT_EXIST, // 刷新令牌不存在
	REFRESH_TOKEN_TIME_OUT, // 刷新令牌过期
}
