/**
 * createDate : 2016年9月2日上午11:54:30
 */
package cn.lastmiles.alipay.bean;

public class Auth {
	private Enum<?> GrantType;
	private String code;
	private String refreshToken;
	public Enum<?> getGrantType() {
		return GrantType;
	}
	public void setGrantType(Enum<?> grantType) {
		GrantType = grantType;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
		
}