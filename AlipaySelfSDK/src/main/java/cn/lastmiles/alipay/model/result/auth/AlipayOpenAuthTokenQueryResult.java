/**
 * createDate : 2016年9月5日下午2:16:41
 * 第三方应用授权
 */
package cn.lastmiles.alipay.model.result.auth;

import com.alipay.api.response.AlipayOpenAuthTokenAppQueryResponse;
import cn.lastmiles.alipay.model.result.Result;
import cn.lastmiles.alipay.model.status.auth.AuthStatus;

public class AlipayOpenAuthTokenQueryResult implements Result{
	private AlipayOpenAuthTokenAppQueryResponse response;
	private AuthStatus authStatus;
	private String parametersError;
	
	public AlipayOpenAuthTokenQueryResult(String parametersError) {
		super();
		this.parametersError = parametersError;
	}

	public AlipayOpenAuthTokenQueryResult( AlipayOpenAuthTokenAppQueryResponse response) {
		super();
		this.response = response;
	}

	public AlipayOpenAuthTokenAppQueryResponse getResponse() {
		return response;
	}

	public void setResponse(AlipayOpenAuthTokenAppQueryResponse response) {
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
		return "AlipayOpenAuthTokenQueryResult [response=" + response
				+ ", authStatus=" + authStatus + ", parametersError="
				+ parametersError + "]";
	}

}