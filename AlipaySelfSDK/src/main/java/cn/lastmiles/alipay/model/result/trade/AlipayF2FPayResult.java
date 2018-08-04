package cn.lastmiles.alipay.model.result.trade;

import cn.lastmiles.alipay.model.result.Result;
import cn.lastmiles.alipay.model.status.trade.TradeStatus;

import com.alipay.api.response.AlipayTradePayResponse;

/**
 * Created by liuyangkly on 15/8/26.
 */
public class AlipayF2FPayResult implements Result {
    private AlipayTradePayResponse response;
    private TradeStatus tradeStatus;
    private String parametersError;

    public AlipayF2FPayResult(String parametersError) {
		super();
		this.parametersError = parametersError;
	}

	public AlipayF2FPayResult(AlipayTradePayResponse response) {
        this.response = response;
    }

    public void setTradeStatus(TradeStatus tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public void setResponse(AlipayTradePayResponse response) {
        this.response = response;
    }

    public TradeStatus getTradeStatus() {
        return tradeStatus;
    }

    public AlipayTradePayResponse getResponse() {
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
		return response != null && TradeStatus.SUCCESS.equals(tradeStatus);
	}
}