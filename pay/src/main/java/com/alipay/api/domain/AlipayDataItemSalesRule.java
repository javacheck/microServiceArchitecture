package com.alipay.api.domain;

import java.util.List;

import com.alipay.api.AlipayObject;
import com.alipay.api.internal.mapping.ApiField;
import com.alipay.api.internal.mapping.ApiListField;

/**
 * 商品销售规则(数据)
 *
 * @author auto create
 * @since 1.0, 2016-03-05 14:11:26
 */
public class AlipayDataItemSalesRule extends AlipayObject {

	private static final long serialVersionUID = 4484666197982861792L;

	/**
	 * 购买人群限制集合，多个类型以逗号分隔
	 */
	@ApiField("buyer_crowd_limit")
	private String buyerCrowdLimit;

	/**
	 * 商品单日销售上限
	 */
	@ApiField("daily_sales_limit")
	private Long dailySalesLimit;

	/**
	 * 用户购买策略
	 */
	@ApiListField("user_sales_limit")
	@ApiField("string")
	private List<String> userSalesLimit;

	public String getBuyerCrowdLimit() {
		return this.buyerCrowdLimit;
	}
	public void setBuyerCrowdLimit(String buyerCrowdLimit) {
		this.buyerCrowdLimit = buyerCrowdLimit;
	}

	public Long getDailySalesLimit() {
		return this.dailySalesLimit;
	}
	public void setDailySalesLimit(Long dailySalesLimit) {
		this.dailySalesLimit = dailySalesLimit;
	}

	public List<String> getUserSalesLimit() {
		return this.userSalesLimit;
	}
	public void setUserSalesLimit(List<String> userSalesLimit) {
		this.userSalesLimit = userSalesLimit;
	}

}
