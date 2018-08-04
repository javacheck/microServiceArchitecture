/**
 * createDate : 2016年7月25日下午4:01:33
 * 口碑管理
 */
package cn.lastmiles.alipay.model.result.shop;

import cn.lastmiles.alipay.model.result.Result;
import cn.lastmiles.alipay.model.status.shop.CreateStatus;
import com.alipay.api.response.AlipayOfflineMarketShopCreateResponse;

public class AlipayCreateShopInfoResult implements Result{
	
	 private AlipayOfflineMarketShopCreateResponse response;
	 private CreateStatus createStatus;
	 private String parametersError;
	 
	 public AlipayCreateShopInfoResult(String parametersError) {
		super();
		this.parametersError = parametersError;
	}

	public AlipayCreateShopInfoResult(AlipayOfflineMarketShopCreateResponse response) {
		super();
		this.response = response;
	}

	public AlipayOfflineMarketShopCreateResponse getResponse() {
		return response;
	}

	public void setResponse(AlipayOfflineMarketShopCreateResponse response) {
		this.response = response;
	}

	public CreateStatus getCreateStatus() {
		return createStatus;
	}

	public void setCreateStatus(CreateStatus createStatus) {
		this.createStatus = createStatus;
	}

	public String getParametersError() {
		return parametersError;
	}

	public void setParametersError(String parametersError) {
		this.parametersError = parametersError;
	}

	@Override
	 public boolean isOperationSuccess() {
		 return null != response && CreateStatus.SUCCESS.equals(createStatus);
	 }

	@Override
	public String toString() {
		return "AlipayCreateShopInfoResult [response=" + response
				+ ", createStatus=" + createStatus + ", parametersError="
				+ parametersError + "]";
	}

}