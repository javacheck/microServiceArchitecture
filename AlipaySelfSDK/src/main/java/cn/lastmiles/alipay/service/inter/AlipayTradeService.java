package cn.lastmiles.alipay.service.inter;

import cn.lastmiles.alipay.model.builder.trade.AlipayTradePayRequestBuilder;
import cn.lastmiles.alipay.model.builder.trade.AlipayTradePrecreateRequestBuilder;
import cn.lastmiles.alipay.model.builder.trade.AlipayTradeQueryRequestBuilder;
import cn.lastmiles.alipay.model.builder.trade.AlipayTradeRefundRequestBuilder;
import cn.lastmiles.alipay.model.result.trade.AlipayF2FPayResult;
import cn.lastmiles.alipay.model.result.trade.AlipayF2FPrecreateResult;
import cn.lastmiles.alipay.model.result.trade.AlipayF2FQueryResult;
import cn.lastmiles.alipay.model.result.trade.AlipayF2FRefundResult;

/**
 * Created by liuyangkly on 15/7/29.
 */
public interface AlipayTradeService {

    // 当面付2.0流程支付
    public AlipayF2FPayResult tradePay(AlipayTradePayRequestBuilder builder);

    // 当面付2.0消费查询
    public AlipayF2FQueryResult queryTradeResult(AlipayTradeQueryRequestBuilder builder);

    // 当面付2.0消费退款
    public AlipayF2FRefundResult tradeRefund(AlipayTradeRefundRequestBuilder builder);

    // 当面付2.0预下单(生成二维码)
    public AlipayF2FPrecreateResult tradePrecreate(AlipayTradePrecreateRequestBuilder builder);
}