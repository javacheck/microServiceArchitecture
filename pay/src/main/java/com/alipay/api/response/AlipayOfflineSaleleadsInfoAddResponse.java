package com.alipay.api.response;

import com.alipay.api.internal.mapping.ApiField;

import com.alipay.api.AlipayResponse;

/**
 * ALIPAY API: alipay.offline.saleleads.info.add response.
 * 
 * @author auto create
 * @since 1.0, 2016-03-04 12:10:56
 */
public class AlipayOfflineSaleleadsInfoAddResponse extends AlipayResponse {

	private static final long serialVersionUID = 7568314741193965375L;

	/** 
	 * 添加leads成功后返回
	 */
	@ApiField("leads_id")
	private String leadsId;

	/** 
	 * 说明
	 */
	@ApiField("message")
	private String message;

	public void setLeadsId(String leadsId) {
		this.leadsId = leadsId;
	}
	public String getLeadsId( ) {
		return this.leadsId;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessage( ) {
		return this.message;
	}

}
