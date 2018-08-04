/**
 * createDate : 2016年9月1日下午5:11:05
 * 第三方应用授权
 */
package cn.lastmiles.alipay.model.builder.auth;

import org.apache.commons.lang.StringUtils;
import com.google.gson.annotations.SerializedName;
import cn.lastmiles.alipay.config.Constants;
import cn.lastmiles.alipay.model.builder.RequestBuilder;
import cn.lastmiles.alipay.model.status.auth.GrantType;

public class AlipayAuthTokenRequestBuilder extends RequestBuilder {

	private BizContent bizContent = new BizContent();
	
	@Override
	public String validate() {
		if( StringUtils.isBlank(bizContent.grantType)){
			return "grantType should not be NULL!";
		}
		if( GrantType.AUTHORIZATION_CODE.name().equals(bizContent.grantType)){
			if( StringUtils.isBlank(bizContent.code)){
				return "by grantType is authorization_code , code should not be NULL!";	
			}
		} else if( GrantType.REFRESH_TOKEN.name().equals(bizContent.grantType) ){
			if( StringUtils.isBlank(bizContent.refreshToken)){
				return "by grantType is refresh_token , refreshToken should not be NULL!";	
			}
		}
		return Constants.VALIDATESUCCESS;
	}

	@Override
	public BizContent getBizContent() {
		return bizContent;
	}
	
	public static class BizContent {
		// <必填> authorization_code表示换取app_auth_token。 refresh_token表示刷新app_auth_token。
		@SerializedName("grant_type")
		private String grantType;
		
		// 可选  授权码，如果grant_type的值为authorization_code。该值必须填写
		@SerializedName("code")
		private String code;
		
		// 可选  刷新令牌，如果grant_type值为refresh_token。该值不能为空。
		// 该值来源于此接口的返回值app_refresh_token
		//（至少需要通过grant_type=authorization_code调用此接口一次才能获取）
		@SerializedName("refresh_token")
		private String refreshToken;

		@Override
		public String toString() {
			return "BizContent [grantType=" + grantType + ", code=" + code
					+ ", refreshToken=" + refreshToken + "]";
		}
	}


	public String getGrantType() {
		return bizContent.grantType;
	}

	public void setGrantType(String grantType) {
		bizContent.grantType = grantType;
	}

	public String getCode() {
		return bizContent.code;
	}

	public void setCode(String code) {
		bizContent.code = code;
	}

	public String getRefreshToken() {
		return bizContent.refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		bizContent.refreshToken = refreshToken;
	}

	@Override
	public String toString() {
		return "AlipayAuthTokenRequestBuilder [bizContent=" + bizContent + "]";
	}
}