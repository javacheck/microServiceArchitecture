package com.alipay.api.response;

import com.alipay.api.internal.mapping.ApiField;

import com.alipay.api.AlipayResponse;

/**
 * ALIPAY API: alipay.pcredit.user.level.get response.
 * 
 * @author auto create
 * @since 1.0, 2016-03-03 17:47:15
 */
public class AlipayPcreditUserLevelGetResponse extends AlipayResponse {

	private static final long serialVersionUID = 1388895931327839171L;

	/** 
	 * 花呗额度等级
	 */
	@ApiField("amount_level")
	private String amountLevel;

	/** 
	 * 用户的花呗额度等级
	 */
	@ApiField("level")
	private String level;

	public void setAmountLevel(String amountLevel) {
		this.amountLevel = amountLevel;
	}
	public String getAmountLevel( ) {
		return this.amountLevel;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	public String getLevel( ) {
		return this.level;
	}

}