/**
 * createDate : 2016年9月5日下午2:20:38
 * 第三方应用授权
 */
package cn.lastmiles.alipay.model.builder.auth;

import org.apache.commons.lang.StringUtils;
import com.google.gson.annotations.SerializedName;
import cn.lastmiles.alipay.config.Constants;
import cn.lastmiles.alipay.model.builder.RequestBuilder;

public class AlipayAuthTokenQueryRequestBuilder extends RequestBuilder{

	private BizContent bizContent = new BizContent();
	
	@Override
	public String validate() {
		if( StringUtils.isBlank(bizContent.getAppAuthToken())){
			return ("bizContent.appAuthToken should not be NULL!");
		}
		return Constants.VALIDATESUCCESS;
	}

	@Override
	public Object getBizContent() {
		return bizContent;
	}
	
	public static class BizContent {
		@SerializedName("app_auth_token")
		private String appAuthToken;

		@Override
		public String toString() {
			return "BizContent [appAuthToken=" + appAuthToken + "]";
		}

		public String getAppAuthToken() {
			return appAuthToken;
		}

		public void setAppAuthToken(String appAuthToken) {
			this.appAuthToken = appAuthToken;
		}
	}

	public void setBizContent(BizContent bizContent) {
		this.bizContent = bizContent;
	}

	@Override
	public String toString() {
		return "AlipayAuthTokenQueryRequestBuilder [bizContent=" + bizContent
				+ "]";
	}
}