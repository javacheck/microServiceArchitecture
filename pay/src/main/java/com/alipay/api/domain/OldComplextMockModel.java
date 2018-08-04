package com.alipay.api.domain;

import com.alipay.api.AlipayObject;
import com.alipay.api.internal.mapping.ApiField;

/**
 * 老版本复杂对象模型
 *
 * @author auto create
 * @since 1.0, 2015-05-06 15:42:29
 */
public class OldComplextMockModel extends AlipayObject {

	private static final long serialVersionUID = 1458916867268737897L;

	/**
	 * biz_num
	 */
	@ApiField("biz_num")
	private Long bizNum;

	/**
	 * biz_type
	 */
	@ApiField("biz_type")
	private String bizType;

	/**
	 * 简单model
	 */
	@ApiField("simple_mock_model")
	private SimpleMockModel simpleMockModel;

	public Long getBizNum() {
		return this.bizNum;
	}
	public void setBizNum(Long bizNum) {
		this.bizNum = bizNum;
	}

	public String getBizType() {
		return this.bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public SimpleMockModel getSimpleMockModel() {
		return this.simpleMockModel;
	}
	public void setSimpleMockModel(SimpleMockModel simpleMockModel) {
		this.simpleMockModel = simpleMockModel;
	}

}
