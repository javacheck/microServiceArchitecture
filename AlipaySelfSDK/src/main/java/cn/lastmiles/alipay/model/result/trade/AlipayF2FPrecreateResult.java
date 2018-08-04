package cn.lastmiles.alipay.model.result.trade;

import cn.lastmiles.alipay.model.result.Result;
import cn.lastmiles.alipay.model.status.trade.TradeStatus;

import com.alipay.api.response.AlipayTradePrecreateResponse;

/**
 * Created by liuyangkly on 15/8/27.
 */
public class AlipayF2FPrecreateResult implements Result {
    private TradeStatus tradeStatus;
    private AlipayTradePrecreateResponse response;
    private String parametersError;
    
    public AlipayF2FPrecreateResult(String parametersError) {
		super();
		this.parametersError = parametersError;
	}

	public AlipayF2FPrecreateResult(AlipayTradePrecreateResponse response) {
        this.response = response;
    }

    public void setTradeStatus(TradeStatus tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public void setResponse(AlipayTradePrecreateResponse response) {
        this.response = response;
    }

    public TradeStatus getTradeStatus() {
        return tradeStatus;
    }

    public AlipayTradePrecreateResponse getResponse() {
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
