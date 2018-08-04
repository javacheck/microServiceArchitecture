/**
 * createDate : 2016年9月1日下午4:18:58
 * 第三方应用授权
 */
package cn.lastmiles.alipay.model.result.auth;

import com.alipay.api.response.AlipayOpenAuthTokenAppResponse;
import cn.lastmiles.alipay.model.result.Result;
import cn.lastmiles.alipay.model.status.auth.AuthStatus;

public class AlipayOpenAuthTokenAppResult implements Result {
	private AlipayOpenAuthTokenAppResponse response;
	private AuthStatus authStatus;
	private String parametersError;
	
	public AlipayOpenAuthTokenAppResult(String parametersError) {
		super();
		this.parametersError = parametersError;
	}

	public AlipayOpenAuthTokenAppResult(AlipayOpenAuthTokenAppResponse response) {
		super();
		this.response = response;
	}

	public AlipayOpenAuthTokenAppResponse getResponse() {
		return response;
	}

	public void setResponse(AlipayOpenAuthTokenAppResponse response) {
		this.response = response;
	}

	public AuthStatus getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(AuthStatus authStatus) {
		this.authStatus = authStatus;
	}

	public String getParametersError() {
		return parametersError;
	}

	public void setParametersError(String parametersError) {
		this.parametersError = parametersError;
	}

	@Override
	public boolean isOperationSuccess() {
		return response != null && AuthStatus.SUCCESS.equals(authStatus);
	}

	@Override
	public String toString() {
		return "AlipayOpenAuthTokenAppResult [response=" + response
				+ ", authStatus=" + authStatus + ", parametersError="
				+ parametersError + "]";
	}
}