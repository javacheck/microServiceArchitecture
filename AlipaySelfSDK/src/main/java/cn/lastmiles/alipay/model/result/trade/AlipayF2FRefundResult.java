package cn.lastmiles.alipay.model.result.trade;

import cn.lastmiles.alipay.model.result.Result;
import cn.lastmiles.alipay.model.status.trade.TradeStatus;

import com.alipay.api.response.AlipayTradeRefundResponse;

/**
 * Created by liuyangkly on 15/8/27.
 */
public class AlipayF2FRefundResult implements Result {
    private TradeStatus tradeStatus;
    private AlipayTradeRefundResponse response;
    private String parametersError;
    
    public AlipayF2FRefundResult(String parametersError) {
		super();
		this.parametersError = parametersError;
	}

	public AlipayF2FRefundResult(AlipayTradeRefundResponse response) {
        this.response = response;
    }

    public void setTradeStatus(TradeStatus tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public void setResponse(AlipayTradeRefundResponse response) {
        this.response = response;
    }

    public TradeStatus getTradeStatus() {
        return tradeStatus;
    }

    public AlipayTradeRefundResponse getResponse() {
        return response;
    }

	public String getParametersError() {
		return parametersError;
	}

	public void setParametersError(String parametersError) {
		this.parametersError = parametersError;
	}

	@Override
	public boolean isOperationSuccess() {
		return response != null &&
                TradeStatus.SUCCESS.equals(tradeStatus);
	}
}
