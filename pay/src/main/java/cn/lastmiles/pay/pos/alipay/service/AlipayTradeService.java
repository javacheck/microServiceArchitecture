package cn.lastmiles.pay.pos.alipay.service;

import cn.lastmiles.pay.pos.alipay.model.builder.AlipayTradePayContentBuilder;
import cn.lastmiles.pay.pos.alipay.model.builder.AlipayTradePrecreateContentBuilder;
import cn.lastmiles.pay.pos.alipay.model.builder.AlipayTradeRefundContentBuilder;
import cn.lastmiles.pay.pos.alipay.model.result.AlipayF2FPayResult;
import cn.lastmiles.pay.pos.alipay.model.result.AlipayF2FPrecreateResult;
import cn.lastmiles.pay.pos.alipay.model.result.AlipayF2FQueryResult;
import cn.lastmiles.pay.pos.alipay.model.result.AlipayF2FRefundResult;

/**
 * Created by liuyangkly on 15/7/29.
 */
public interface AlipayTradeService {

    // 当面付2.0流程支付
    public AlipayF2FPayResult tradePay(AlipayTradePayContentBuilder builder);

    // 当面付2.0消费查询
    public AlipayF2FQueryResult queryTradeResult(String outTradeNo);

    // 当面付2.0消费退款
    public AlipayF2FRefundResult tradeRefund(AlipayTradeRefundContentBuilder builder);

    // 当面付2.0预下单(生成二维码)
    public AlipayF2FPrecreateResult tradePrecreate(AlipayTradePrecreateContentBuilder builder);
}
