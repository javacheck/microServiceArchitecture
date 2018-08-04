/**
 * createDate : 2016年7月25日下午3:55:44
 * 口碑店铺
 */
package cn.lastmiles.alipay.model.result.shop;

import cn.lastmiles.alipay.model.result.Result;
import cn.lastmiles.alipay.model.status.shop.UploadStatus;
import com.alipay.api.response.AlipayOfflineMaterialImageUploadResponse;

public class AlipayStoresImageUploadResult implements Result{

    private AlipayOfflineMaterialImageUploadResponse response;
    private UploadStatus uploadStatus;
    private String parametersError;

    public AlipayStoresImageUploadResult(String parametersError) {
		super();
		this.parametersError = parametersError;
	}

	public AlipayStoresImageUploadResult(AlipayOfflineMaterialImageUploadResponse response) {
        this.response = response;
    }

    public AlipayOfflineMaterialImageUploadResponse getResponse() {
		return response;
	}
    
    public void setResponse(AlipayOfflineMaterialImageUploadResponse response) {
        this.response = response;
    }
    
	public UploadStatus getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(UploadStatus uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	public String getParametersError() {
		return parametersError;
	}

	public void setParametersError(String parametersError) {
		this.parametersError = parametersError;
	}

	@Override
	public boolean isOperationSuccess() {
		return null != response && UploadStatus.SUCCESS.equals(uploadStatus);
	}
}