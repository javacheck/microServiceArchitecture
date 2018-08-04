package com.alipay.api.response;

import com.alipay.api.internal.mapping.ApiField;

import com.alipay.api.AlipayResponse;

/**
 * ALIPAY API: alipay.offline.saleleads.claimstatus response.
 * 
 * @author auto create
 * @since 1.0, 2016-03-04 12:13:05
 */
public class AlipayOfflineSaleleadsClaimstatusResponse extends AlipayResponse {

	private static final long serialVersionUID = 8787642253414983927L;

	/** 
	 * success(成功);fail(失败)
	 */
	@ApiField("res_msg")
	private String resMsg;

	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}
	public String getResMsg( ) {
		return this.resMsg;
	}

}
