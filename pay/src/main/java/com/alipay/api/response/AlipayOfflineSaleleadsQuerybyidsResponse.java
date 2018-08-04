package com.alipay.api.response;

import java.util.List;
import com.alipay.api.internal.mapping.ApiField;
import com.alipay.api.internal.mapping.ApiListField;
import com.alipay.api.domain.MerchantSaleLeadsQueryDTO;

import com.alipay.api.AlipayResponse;

/**
 * ALIPAY API: alipay.offline.saleleads.querybyids response.
 * 
 * @author auto create
 * @since 1.0, 2016-01-27 19:32:46
 */
public class AlipayOfflineSaleleadsQuerybyidsResponse extends AlipayResponse {

	private static final long serialVersionUID = 8156667545689817378L;

	/** 
	 * 查询Leads信息对象集合
	 */
	@ApiListField("merchantsaleleadsquerylist")
	@ApiField("merchant_sale_leads_query_d_t_o")
	private List<MerchantSaleLeadsQueryDTO> merchantsaleleadsquerylist;

	public void setMerchantsaleleadsquerylist(List<MerchantSaleLeadsQueryDTO> merchantsaleleadsquerylist) {
		this.merchantsaleleadsquerylist = merchantsaleleadsquerylist;
	}
	public List<MerchantSaleLeadsQueryDTO> getMerchantsaleleadsquerylist( ) {
		return this.merchantsaleleadsquerylist;
	}

}
