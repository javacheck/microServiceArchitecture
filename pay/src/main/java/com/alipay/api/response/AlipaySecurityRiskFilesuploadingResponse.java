package com.alipay.api.response;

import com.alipay.api.internal.mapping.ApiField;

import com.alipay.api.AlipayResponse;

/**
 * ALIPAY API: alipay.security.risk.filesuploading response.
 * 
 * @author auto create
 * @since 1.0, 2016-03-04 11:35:32
 */
public class AlipaySecurityRiskFilesuploadingResponse extends AlipayResponse {

	private static final long serialVersionUID = 1629363183217412737L;

	/** 
	 * outputparamtest
	 */
	@ApiField("outputparamtest")
	private String outputparamtest;

	public void setOutputparamtest(String outputparamtest) {
		this.outputparamtest = outputparamtest;
	}
	public String getOutputparamtest( ) {
		return this.outputparamtest;
	}

}
